#!/usr/bin/env node
// const fs = require("fs");
// const path = require("path");

// const name = process.argv[2];
// if (!name) {
//   console.log("❌ Please provide a component name");
//   console.log("👉 Example:");
//   console.log("   npm run generate Login");
//   console.log("   npm run generate updateforms/Projects");
//   console.log("   npm run generate updateforms/Project/ProjectForm");
//   process.exit(1);
// }

// // === PATH SETUP ===
// const componentsDir = path.join(__dirname, "../src/components");
// const parts = name.split(/[\\/]/);
// const componentName = parts.pop();
// const nestedPath = parts.join("/");
// const componentFolder = path.join(componentsDir, nestedPath, componentName);
// const jsxPath = path.join(componentFolder, `${componentName}.jsx`);
// const cssPath = path.join(componentFolder, `${componentName}.module.css`);

// const appJsx = path.join(__dirname, "../src/App.jsx");
// const appJs = path.join(__dirname, "../src/App.js");
// const appPath = fs.existsSync(appJsx) ? appJsx : appJs;

// // === CREATE COMPONENT FILES ===
// fs.mkdirSync(componentFolder, { recursive: true });

// const jsxTemplate = `
// import React from 'react';
// import { Outlet } from 'react-router-dom';
// import styles from './${componentName}.module.css';

// export default function ${componentName}() {
//   return (
//     <div className={styles.container}>
//       <h3>${componentName} Component</h3>
//       <Outlet />
//     </div>
//   );
// }
// `;

// const cssTemplate = `.container {
//   padding: 20px;
//   background-color: #f9f9f9;
//   border-radius: 8px;
// }`;

// if (!fs.existsSync(jsxPath)) fs.writeFileSync(jsxPath, jsxTemplate.trim());
// if (!fs.existsSync(cssPath)) fs.writeFileSync(cssPath, cssTemplate.trim());
// console.log(`✅ Component "${componentName}" created at src/components/${nestedPath}/${componentName}/`);

// // === MODIFY APP.JS ===
// if (!fs.existsSync(appPath)) {
//   console.log("⚠️ App.jsx not found — skipping route insertion.");
//   process.exit(0);
// }

// let appContent = fs.readFileSync(appPath, "utf8");

// // === ADD IMPORT ===
// const importPath = `./components/${nestedPath ? nestedPath + "/" : ""}${componentName}/${componentName}`;
// const importLine = `import ${componentName} from "${importPath}";\n`;
// if (!appContent.includes(importLine)) {
//   appContent = importLine + appContent;
//   console.log(`🧩 Added import for ${componentName}`);
// }

// // === BUILD ROUTE CHAIN ===
// const routeSegments = [...parts.map(p => p.toLowerCase()), componentName.toLowerCase()];
// const routeChain = routeSegments.map((seg, idx) => ({
//   path: seg,
//   component: idx === routeSegments.length - 1
//     ? componentName
//     : capitalize(parts[idx] || componentName),
// }));

// function capitalize(str) {
//   return str.charAt(0).toUpperCase() + str.slice(1);
// }

// // === HELPERS ===
// function findRouteBlock(content, path) {
//   const regex = new RegExp(`<Route[^>]*path=["']${path}["'][^>]*>`, "i");
//   const match = regex.exec(content);
//   if (!match) return null;
//   const start = match.index + match[0].length;
//   const end = findClosingIndex(content, start);
//   return { start, end };
// }

// function findClosingIndex(content, startIndex) {
//   let depth = 1;
//   const openTag = /<Route[^>]*>/gi;
//   const closeTag = /<\/Route>/gi;
//   openTag.lastIndex = startIndex;
//   closeTag.lastIndex = startIndex;

//   while (true) {
//     const nextOpen = openTag.exec(content);
//     const nextClose = closeTag.exec(content);
//     if (!nextClose) return -1;
//     if (nextOpen && nextOpen.index < nextClose.index) {
//       depth++;
//       continue;
//     }
//     depth--;
//     if (depth === 0) return nextClose.index;
//   }
// }

// // === SAFE INSERTION (inside <Routes> but before export) ===
// function insertRouteSafely(content, routeCode) {
//   // Find the last </Routes> before export default App
//   const exportIndex = content.indexOf("export default App");
//   const routesCloseRegex = /<\/Routes>/gi;
//   let lastRoutesCloseIndex = -1;
//   let match;
//   while ((match = routesCloseRegex.exec(content)) !== null) {
//     if (match.index < exportIndex) {
//       lastRoutesCloseIndex = match.index;
//     }
//   }

//   if (lastRoutesCloseIndex === -1) {
//     console.error("❌ Could not find valid </Routes> inside App()");
//     return content;
//   }

//   // Insert route right before that </Routes>
//   return (
//     content.slice(0, lastRoutesCloseIndex) +
//     routeCode +
//     "\n      " +
//     content.slice(lastRoutesCloseIndex)
//   );
// }

// // === ROUTE INSERTION ===
// let lastParentPath = "";
// for (let i = 0; i < routeChain.length; i++) {
//   const { path: currentPath, component } = routeChain[i];
//   const parentPath = lastParentPath;
//   const parentBlock = parentPath ? findRouteBlock(appContent, parentPath) : null;

//   if (parentBlock) {
//     // Insert inside parent route
//     const newRoute = `\n            <Route path="${currentPath}" element={<${component} />} />`;
//     const blockContent = appContent.slice(parentBlock.start, parentBlock.end);
//     if (!blockContent.includes(`path="${currentPath}"`)) {
//       appContent =
//         appContent.slice(0, parentBlock.end) +
//         newRoute +
//         appContent.slice(parentBlock.end);
//       console.log(`🧩 Inserted <${component}> inside "${parentPath}" route`);
//     }
//   } else {
//     // Insert safely before </Routes> (and never after export)
//     const newRoute = `\n          <Route path="${currentPath}" element={<${component} />} />`;
//     if (!appContent.includes(`path="${currentPath}"`)) {
//       appContent = insertRouteSafely(appContent, newRoute);
//       console.log(`🆕 Added top-level route "${currentPath}"`);
//     }
//   }

//   lastParentPath = currentPath;
// }

// // === AUTO ADD <Outlet /> ===
// for (let i = 0; i < parts.length; i++) {
//   const parentName = capitalize(parts[i]);
//   const parentPath = path.join(componentsDir, ...parts.slice(0, i + 1), parentName, `${parentName}.jsx`);
//   if (fs.existsSync(parentPath)) {
//     let parentContent = fs.readFileSync(parentPath, "utf8");
//     if (!parentContent.includes("<Outlet")) {
//       parentContent = parentContent.replace(
//         /return\s*\(\s*<div[^>]*>/,
//         match => `${match}\n      <Outlet />`
//       );
//       if (!parentContent.includes("from 'react-router-dom'")) {
//         parentContent = parentContent.replace(
//           /from 'react';/,
//           "from 'react';\nimport { Outlet } from 'react-router-dom';"
//         );
//       }
//       fs.writeFileSync(parentPath, parentContent);
//       console.log(`🪄 Added <Outlet /> to ${parentName}.jsx`);
//     }
//   }
// }

// // === SAVE FILE ===
// fs.writeFileSync(appPath, appContent, "utf8");
// console.log("✅ App.jsx updated successfully!");



/**
 * 🚀 Enhanced generate-component.js
 * - Creates component + CSS files.
 * - Inserts nested routes into App.js automatically.
 * - 🧹 Runs cleanup-app.js automatically afterward to fix misplaced routes.
 */

const fs = require("fs");
const path = require("path");
const { execSync } = require("child_process");

// --- Get name from CLI args ---
const name = process.argv[2];
if (!name) {
  console.log("❌ Please provide a component name");
  console.log("👉 Example:");
  console.log("   npm run generate Login");
  console.log("   npm run generate updateforms/Projects");
  console.log("   npm run generate updateforms/Project/ProjectForm");
  process.exit(1);
}

// --- PATH SETUP ---
const componentsDir = path.join(__dirname, "../src/components");
const parts = name.split(/[\\/]/);
const componentName = parts.pop();
const nestedPath = parts.join("/");
const componentFolder = path.join(componentsDir, nestedPath, componentName);
const jsxPath = path.join(componentFolder, `${componentName}.jsx`);
const cssPath = path.join(componentFolder, `${componentName}.module.css`);

const appJsx = path.join(__dirname, "../src/App.jsx");
const appJs = path.join(__dirname, "../src/App.js");
const appPath = fs.existsSync(appJsx) ? appJsx : appJs;

// --- CREATE COMPONENT FILES ---
fs.mkdirSync(componentFolder, { recursive: true });

const jsxTemplate = `
import React from 'react';
import { Outlet } from 'react-router-dom';
import styles from './${componentName}.module.css';

export default function ${componentName}() {
  return (
    <div className={styles.container}>
      <h3>${componentName} Component</h3>
      <Outlet />
    </div>
  );
}
`;

const cssTemplate = `.container {
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}`;

if (!fs.existsSync(jsxPath)) fs.writeFileSync(jsxPath, jsxTemplate.trim());
if (!fs.existsSync(cssPath)) fs.writeFileSync(cssPath, cssTemplate.trim());
console.log(`✅ Component "${componentName}" created at src/components/${nestedPath}/${componentName}/`);

// --- MODIFY APP.JS ---
if (!fs.existsSync(appPath)) {
  console.log("⚠️ App.jsx not found — skipping route insertion.");
  process.exit(0);
}

let appContent = fs.readFileSync(appPath, "utf8");

// --- ADD IMPORT ---
const importPath = `./components/${nestedPath ? nestedPath + "/" : ""}${componentName}/${componentName}`;
const importLine = `import ${componentName} from "${importPath}";\n`;
if (!appContent.includes(importLine)) {
  appContent = importLine + appContent;
  console.log(`🧩 Added import for ${componentName}`);
}

// --- BUILD ROUTE CHAIN ---
const routeSegments = [...parts.map(p => p.toLowerCase()), componentName.toLowerCase()];
const routeChain = routeSegments.map((seg, idx) => ({
  path: seg,
  component: idx === routeSegments.length - 1
    ? componentName
    : capitalize(parts[idx] || componentName),
}));

function capitalize(str) {
  return str.charAt(0).toUpperCase() + str.slice(1);
}

// --- HELPERS ---
function findRouteBlock(content, path) {
  const regex = new RegExp(`<Route[^>]*path=["']${path}["'][^>]*>`, "i");
  const match = regex.exec(content);
  if (!match) return null;
  const start = match.index + match[0].length;
  const end = findClosingIndex(content, start);
  return { start, end };
}

function findClosingIndex(content, startIndex) {
  let depth = 1;
  const openTag = /<Route[^>]*>/gi;
  const closeTag = /<\/Route>/gi;
  openTag.lastIndex = startIndex;
  closeTag.lastIndex = startIndex;

  while (true) {
    const nextOpen = openTag.exec(content);
    const nextClose = closeTag.exec(content);
    if (!nextClose) return -1;
    if (nextOpen && nextOpen.index < nextClose.index) {
      depth++;
      continue;
    }
    depth--;
    if (depth === 0) return nextClose.index;
  }
}

// --- INSERT ROUTE CHAIN ---
let lastParentPath = "";
for (let i = 0; i < routeChain.length; i++) {
  const { path: currentPath, component } = routeChain[i];
  const parentPath = lastParentPath;
  const parentBlock = parentPath ? findRouteBlock(appContent, parentPath) : null;

  if (parentBlock) {
    // Insert into existing parent
    const newRoute = `\n            <Route path="${currentPath}" element={<${component} />} />`;
    const blockContent = appContent.slice(parentBlock.start, parentBlock.end);
    if (!blockContent.includes(`path="${currentPath}"`)) {
      appContent =
        appContent.slice(0, parentBlock.end) +
        newRoute +
        appContent.slice(parentBlock.end);
      console.log(`🧩 Inserted <${component}> inside "${parentPath}" route`);
    }
  } else {
    // Insert safely inside <Routes>, before </Routes>
    const newRoute = `\n          <Route path="${currentPath}" element={<${component} />} />`;
    if (!appContent.includes(`path="${currentPath}"`)) {
      const routesRegex = /(<Routes>[\s\S]*?)(<\/Routes>)/i;
      appContent = appContent.replace(routesRegex, `$1${newRoute}\n      $2`);
      console.log(`🆕 Added top-level route "${currentPath}"`);
    }
  }

  lastParentPath = currentPath;
}

// --- SAVE CHANGES TO APP.JS ---
fs.writeFileSync(appPath, appContent, "utf8");
console.log("✅ App.jsx updated successfully!");

// --- 🧹 RUN AUTO CLEANUP SCRIPT ---
const cleanupPath = path.join(__dirname, "cleanup-app.js");
if (fs.existsSync(cleanupPath)) {
  console.log("🧹 Running cleanup script to fix misplaced routes...");
  try {
    execSync(`node "${cleanupPath}"`, { stdio: "inherit" });
    console.log("✅ Cleanup completed successfully!");
  } catch (err) {
    console.error("⚠️ Cleanup script failed, but component generation succeeded.");
  }
} else {
  console.warn("⚠️ Cleanup script not found — skipping auto-fix.");
}
