#!/usr/bin/env node

/**
 * Pre-render meta tags for article pages (no browser required).
 *
 * Parses article data directly from ClojureScript source files, then
 * generates per-route HTML files with correct OG/Twitter meta tags
 * for social media crawlers that don't execute JavaScript.
 *
 * Compatible with Node.js >= 10.
 */

'use strict';

var fs = require('fs');
var path = require('path');

var ROOT_DIR = path.join(__dirname, '..');
var PUBLIC_DIR = path.join(ROOT_DIR, 'public');
var PRODUCTION_ORIGIN = 'https://www.mapbh.org';
var DEFAULT_IMAGE = 'https://mapbh.org/img/ogbrand.png';
var DEFAULT_IMAGE_ALT = 'mapBH - Digital Map Archive';

// ── Source Parsing ──────────────────────────────────────────────────────

/**
 * Parse article entries from src/app/pages/articles/index.cljs.
 * Extracts route, titles, descriptions, and keywords for each article.
 */
function parseArticleEntries() {
  var src = fs.readFileSync(
    path.join(ROOT_DIR, 'src', 'app', 'pages', 'articles', 'index.cljs'),
    'utf-8'
  );

  // Find (def entries [...])
  var marker = src.indexOf('(def entries');
  if (marker === -1) {
    throw new Error('Could not find (def entries ...) in index.cljs');
  }

  // Find the opening [ of the vector
  var vecStart = src.indexOf('[', marker);
  if (vecStart === -1) {
    throw new Error('Could not find entries vector in index.cljs');
  }

  // Extract content between [ and matching ]
  var depth = 1;
  var i = vecStart + 1;
  while (i < src.length && depth > 0) {
    if (src[i] === '[') depth++;
    else if (src[i] === ']') depth--;
    i++;
  }
  var content = src.substring(vecStart + 1, i - 1);

  // Split into individual entry maps using brace matching
  var blocks = [];
  depth = 0;
  var start = -1;
  for (i = 0; i < content.length; i++) {
    if (content[i] === '{') {
      if (depth === 0) start = i;
      depth++;
    } else if (content[i] === '}') {
      depth--;
      if (depth === 0 && start >= 0) {
        blocks.push(content.substring(start + 1, i));
        start = -1;
      }
    }
  }

  return blocks.map(function (block) {
    function getString(key) {
      var m = block.match(new RegExp(':' + key + '\\s+"([^"]*)"'));
      return m ? m[1] : null;
    }
    function getArray(key) {
      var m = block.match(new RegExp(':' + key + '\\s+\\[([^\\]]*?)\\]'));
      if (!m) return [];
      var items = m[1].match(/"([^"]*)"/g);
      return items ? items.map(function (s) { return s.replace(/"/g, ''); }) : [];
    }
    return {
      route: getString('route'),
      enTitle: getString('en-title'),
      arTitle: getString('ar-title'),
      enDescription: getString('en-description'),
      arDescription: getString('ar-description'),
      enKeywords: getArray('en-keywords'),
      arKeywords: getArray('ar-keywords')
    };
  }).filter(function (e) { return e.route; });
}

/**
 * Extract the content of a brace-delimited block starting at the given
 * opening '{' position. Returns the inner content (excluding the braces).
 */
function extractBraceBlock(src, openPos) {
  var depth = 1;
  var i = openPos + 1;
  while (i < src.length && depth > 0) {
    if (src[i] === '{') depth++;
    else if (src[i] === '}') depth--;
    i++;
  }
  return src.substring(openPos + 1, i - 1);
}

/**
 * Extract title, description, and keywords from a ClojureScript map string.
 */
function extractLangMeta(section) {
  var title = section.match(/:title\s+"([^"]*)"/);
  var desc = section.match(/:description\s+"([^"]*)"/);
  var kwMatch = section.match(/:keywords\s+\[([^\]]*)\]/);
  var keywords = [];
  if (kwMatch) {
    var kws = kwMatch[1].match(/"([^"]*)"/g);
    keywords = kws ? kws.map(function (s) { return s.replace(/"/g, ''); }) : [];
  }
  return {
    title: title ? title[1] : null,
    description: desc ? desc[1] : null,
    keywords: keywords
  };
}

/**
 * Parse :article-index meta config from src/app/util/meta.cljs.
 * Uses brace-matching to correctly handle nested ClojureScript maps.
 */
function parseArticleIndexMeta() {
  var src = fs.readFileSync(
    path.join(ROOT_DIR, 'src', 'app', 'util', 'meta.cljs'),
    'utf-8'
  );

  // Find :article-index and its outer { ... } block
  var marker = src.indexOf(':article-index');
  if (marker === -1) {
    console.warn('Warning: could not find :article-index in meta.cljs');
    return { en: {}, ar: {} };
  }
  var outerOpen = src.indexOf('{', marker);
  var block = extractBraceBlock(src, outerOpen);

  // Extract :en { ... } and :ar { ... } using brace matching
  function extractLangSection(lang) {
    var langMarker = block.indexOf(':' + lang);
    if (langMarker === -1) return '';
    var langOpen = block.indexOf('{', langMarker);
    if (langOpen === -1) return '';
    return extractBraceBlock(block, langOpen);
  }

  return {
    en: extractLangMeta(extractLangSection('en')),
    ar: extractLangMeta(extractLangSection('ar'))
  };
}

// ── Meta Config Builder ─────────────────────────────────────────────────

function buildMeta(title, description, keywords, routePath) {
  var fullTitle = title + ' - mapBH';
  var url = PRODUCTION_ORIGIN + routePath;
  var keywordsStr = Array.isArray(keywords) ? keywords.join(', ') : (keywords || '');

  return {
    title: fullTitle,
    description: description || '',
    keywords: keywordsStr,
    ogTitle: fullTitle,
    ogDescription: description || '',
    ogUrl: url,
    ogImage: DEFAULT_IMAGE,
    ogImageAlt: DEFAULT_IMAGE_ALT,
    twitterTitle: fullTitle,
    twitterDescription: description || '',
    twitterImage: DEFAULT_IMAGE,
    twitterImageAlt: DEFAULT_IMAGE_ALT,
    canonical: url
  };
}

// ── HTML Template Replacement ───────────────────────────────────────────

function escapeHtml(str) {
  return str
    .replace(/&/g, '&amp;')
    .replace(/"/g, '&quot;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;');
}

function escapeRegex(str) {
  return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

function replaceMeta(html, attr, value, content) {
  if (!content) return html;
  var escaped = escapeHtml(content);
  var replacement = '<meta ' + attr + '="' + value + '" content="' + escaped + '">';

  // Try: <meta property="og:title" content="...">
  var regex = new RegExp(
    '<meta\\s+' + attr + '="' + escapeRegex(value) + '"\\s+content="[^"]*"\\s*/?>',
    'i'
  );
  if (regex.test(html)) {
    return html.replace(regex, replacement);
  }

  // Try alternate order: <meta content="..." property="og:title">
  var regexAlt = new RegExp(
    '<meta\\s+content="[^"]*"\\s+' + attr + '="' + escapeRegex(value) + '"\\s*/?>',
    'i'
  );
  if (regexAlt.test(html)) {
    return html.replace(regexAlt, replacement);
  }

  return html;
}

function renderHtml(template, meta) {
  var html = template;

  // <title>
  html = html.replace(
    /<title>[^<]*<\/title>/,
    '<title>' + escapeHtml(meta.title) + '</title>'
  );

  // Basic meta tags
  html = replaceMeta(html, 'name', 'description', meta.description);
  if (meta.keywords) {
    html = replaceMeta(html, 'name', 'keywords', meta.keywords);
  }

  // Open Graph
  html = replaceMeta(html, 'property', 'og:title', meta.ogTitle);
  html = replaceMeta(html, 'property', 'og:description', meta.ogDescription);
  html = replaceMeta(html, 'property', 'og:url', meta.ogUrl);
  html = replaceMeta(html, 'property', 'og:image', meta.ogImage);
  html = replaceMeta(html, 'property', 'og:image:alt', meta.ogImageAlt);

  // Twitter Card
  html = replaceMeta(html, 'name', 'twitter:title', meta.twitterTitle);
  html = replaceMeta(html, 'name', 'twitter:description', meta.twitterDescription);
  html = replaceMeta(html, 'name', 'twitter:image', meta.twitterImage);
  html = replaceMeta(html, 'name', 'twitter:image:alt', meta.twitterImageAlt);

  // Canonical link
  if (meta.canonical) {
    html = html.replace(
      /<link\s+rel="canonical"\s+href="[^"]*"\s*\/?>/,
      '<link rel="canonical" href="' + escapeHtml(meta.canonical) + '">'
    );
  }

  return html;
}

// ── Main ────────────────────────────────────────────────────────────────

function main() {
  var template = fs.readFileSync(path.join(PUBLIC_DIR, 'index.html'), 'utf-8');

  // Parse data from ClojureScript sources
  var articles = parseArticleEntries();
  var indexMeta = parseArticleIndexMeta();

  console.log(
    'Discovered ' + articles.length + ' articles: ' +
    articles.map(function (a) { return a.route; }).join(', ')
  );

  // Build all routes to prerender
  var routes = [];

  // Article index pages
  routes.push({
    path: '/en/articles',
    meta: buildMeta(indexMeta.en.title, indexMeta.en.description, indexMeta.en.keywords, '/en/articles/')
  });
  routes.push({
    path: '/ar/articles',
    meta: buildMeta(indexMeta.ar.title, indexMeta.ar.description, indexMeta.ar.keywords, '/ar/articles/')
  });

  // Individual article pages
  articles.forEach(function (article) {
    routes.push({
      path: '/en/articles/' + article.route,
      meta: buildMeta(article.enTitle, article.enDescription, article.enKeywords,
                       '/en/articles/' + article.route)
    });
    routes.push({
      path: '/ar/articles/' + article.route,
      meta: buildMeta(article.arTitle, article.arDescription, article.arKeywords,
                       '/ar/articles/' + article.route)
    });
  });

  // Generate HTML files
  routes.forEach(function (route) {
    var html = renderHtml(template, route.meta);
    var outDir = path.join(PUBLIC_DIR, route.path);
    fs.mkdirSync(outDir, { recursive: true });
    var outFile = path.join(outDir, 'index.html');
    fs.writeFileSync(outFile, html, 'utf-8');
    console.log('  ' + route.meta.title + ' -> ' + route.path + '/index.html');
  });

  console.log('\nPre-rendering complete. Generated ' + routes.length + ' files.');
}

main();
