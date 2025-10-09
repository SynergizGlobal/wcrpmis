#!/usr/bin/env node
const fs = require("fs");
const path = require("path");

const name = process.argv[2];
if (!name) {
  console.log("❌ Please provide a component name");
  console.log("👉 Example: npm run generate Login");
  process.exit(1);
}

const componentsDir = path.join(__dirname, "../src/components");
const componentFolder = path.join(componentsDir, name);
const jsxPath = path.join(componentFolder, `${name}.jsx`);
const cssPath = path.join(componentFolder, `${name}.module.css`);

const appJsx = path.join(__dirname, "../src/App.jsx");
const appJs = path.join(__dirname, "../src/App.js");
const appPath = fs.existsSync(appJsx) ? appJsx : appJs;

// ✅ Ensure component folder
if (!fs.existsSync(componentsDir)) fs.mkdirSync(componentsDir);
if (fs.existsSync(componentFolder)) {
  console.log(`⚠️ Component "${name}" already exists.`);
  process.exit(1);
}
fs.mkdirSync(componentFolder);

// ✅ Create component files
const jsxTemplate = `
import React from 'react';
import styles from './${name}.module.css';

export default function ${name}() {
  return (
    <div className={styles.container}>
      <h2>${name} Component</h2>
    </div>
  );
}
`;

const cssTemplate = `.container {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}`;

fs.writeFileSync(jsxPath, jsxTemplate.trim());
fs.writeFileSync(cssPath, cssTemplate.trim());
console.log(`✅ Component folder "${name}" created in src/components/${name}/`);

// ✅ Skip App update if missing
if (!fs.existsSync(appPath)) {
  console.log("⚠️ App.jsx not found — skipping route insertion.");
  process.exit(0);
}

// ✅ Read App.jsx content
let appContent = fs.readFileSync(appPath, "utf8");

// ✅ Add import if not already present
const importLine = `import ${name} from "./components/${name}/${name}";\n`;
if (!appContent.includes(importLine)) {
  appContent = importLine + appContent;
}

// ✅ Prepare route line
const newRoute = `\n          <Route path="${name.toLowerCase()}" element={<${name} />} />`;

// ✅ Preferred insertion marker
const protectedMarker = "{/* Add more nested pages here later */}";

// ✅ Try inserting above protected marker
if (appContent.includes(protectedMarker)) {
  appContent = appContent.replace(protectedMarker, `${newRoute}\n          ${protectedMarker}`);
  console.log(`🧩 Added <${name}> route under Layout protected section`);
}

// ✅ Fallback: if no marker found, insert before fallback routes
else if (appContent.includes("{/* Fallback route */}")) {
  appContent = appContent.replace("{/* Fallback route */}", `${newRoute}\n\n        {/* Fallback route */}`);
  console.log(`🧩 Added <${name}> route before fallback section`);
}

// ✅ Save updated App file
fs.writeFileSync(appPath, appContent);
console.log(`✅ App.jsx updated successfully!`);
