#!/usr/bin/env node
/**
 * FINAL BULLETPROOF CLEANUP SCRIPT
 * Fixes malformed routes like:
 *   <Route path="work" element={<Work />
 * And safely nests them under <Route path="updateforms">
 */

const fs = require("fs");
const path = require("path");
const parser = require("@babel/parser");
const traverse = require("@babel/traverse").default;
const generate = require("@babel/generator").default;
const t = require("@babel/types");

const appPath = path.join(__dirname, "../src/App.js");

if (!fs.existsSync(appPath)) {
  console.error("❌ App.js not found.");
  process.exit(1);
}

let code = fs.readFileSync(appPath, "utf8");

// STEP 1️⃣ — Split before/after export
const exportIndex = code.indexOf("export default App");
if (exportIndex === -1) {
  console.error("❌ Could not find 'export default App' in App.js.");
  process.exit(1);
}

let beforeExport = code.slice(0, exportIndex);
let afterExport = code.slice(exportIndex);

// STEP 2️⃣ — Find all misplaced routes
const rogueRouteRegex = /<Route[\s\S]*?(?:\/>|>)/g;
let rogueRoutes = afterExport.match(rogueRouteRegex) || [];

if (rogueRoutes.length === 0) {
  console.log("✅ No misplaced routes found.");
  process.exit(0);
}

console.log(`🧹 Found ${rogueRoutes.length} misplaced route(s). Checking format...`);

// STEP 3️⃣ — Sanitize malformed routes
rogueRoutes = rogueRoutes.map((r) => {
  let cleaned = r.replace(/\s+/g, " ").trim();

  // 🔧 If missing closing "}/>" — fix it
  if (/element={<[A-Za-z0-9_]+ ?\/?>$/.test(cleaned)) {
    cleaned = cleaned.replace(
      /element={<([A-Za-z0-9_]+) ?\/?>$/,
      'element={<$1 />} />'
    );
  }

  // Ensure always ends with "/>"
  if (!cleaned.endsWith("/>")) {
    cleaned = cleaned.replace(/>$/, " />");
  }

  // Remove stray semicolons or unmatched braces
  cleaned = cleaned.replace(/[;}]+$/, "").trim();

  return cleaned;
});

console.log("🧩 Sanitized Routes to insert:");
console.log(rogueRoutes.join("\n"));

// STEP 4️⃣ — Clean junk after export
afterExport = "\nexport default App;\n";

// STEP 5️⃣ — Parse JSX before export
let ast;
try {
  ast = parser.parse(beforeExport, {
    sourceType: "module",
    plugins: ["jsx"],
  });
} catch (err) {
  console.error("❌ Failed to parse App.js before export.");
  console.error(err.message);
  process.exit(1);
}

// STEP 6️⃣ — Find <Route path="updateforms">
let updateFormsNode = null;
traverse(ast, {
  JSXOpeningElement(path) {
    if (
      path.node.name.name === "Route" &&
      path.node.attributes.some(
        (a) => a.name?.name === "path" && a.value?.value === "updateforms"
      )
    ) {
      updateFormsNode = path.parent;
    }
  },
});

if (!updateFormsNode) {
  console.error("⚠️ No <Route path='updateforms'> found. Aborting cleanup.");
  process.exit(1);
}

// STEP 7️⃣ — Parse sanitized rogue routes safely
const wrappedJSX = `<>${rogueRoutes.join("\n")}</>`;
let rogueAST;
try {
  rogueAST = parser.parse(wrappedJSX, {
    sourceType: "module",
    plugins: ["jsx"],
  });
} catch (err) {
  console.error("❌ Still failed to parse rogue JSX. Offending content:");
  console.error(wrappedJSX);
  process.exit(1);
}

// STEP 8️⃣ — Collect Route nodes
let newRoutes = [];
traverse(rogueAST, {
  JSXElement(p) {
    if (p.node.openingElement.name.name === "Route") {
      newRoutes.push(t.cloneNode(p.node));
    }
  },
});

// STEP 9️⃣ — Insert them into updateforms route
updateFormsNode.children = updateFormsNode.children || [];
updateFormsNode.children.push(...newRoutes);

// STEP 🔟 — Generate new file and save
const output = generate(ast, { quotes: "double" }).code;
fs.writeFileSync(appPath, output + afterExport, "utf8");

console.log("✅ Successfully fixed malformed route and moved inside <Route path='updateforms'>!");
