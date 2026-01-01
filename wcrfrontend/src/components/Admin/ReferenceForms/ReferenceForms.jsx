// import React, { useEffect, useState } from "react";
// import styles from "./ReferenceForms.module.css";
// import api from "../../../api/axiosInstance";
// import { referenceModules, referencePages } from "./referenceConfig";

// const DEFAULT_MODULE = "Contracts";

// export default function ReferenceForms() {
//   const [modules, setModules] = useState([]);
//   const [activeModule, setActiveModule] = useState(
//     localStorage.getItem("reference_module") || DEFAULT_MODULE
//   );
  
  
//   // const [pages, setPages] = useState([]);

//   const pages = referencePages[activeModule] || [];

//   // 🔹 Load module list (tabs)
//   useEffect(() => {
//     loadModules();
//   }, []);

//   // 🔹 Load iframe pages when module changes
//   useEffect(() => {
//     if (activeModule) {
//       loadReferencePages(activeModule);
//       localStorage.setItem("reference_module", activeModule);
//     }
//   }, [activeModule]);

//   const loadModules = async () => {
//     try {
//       const res = await api.get("/reference/modules");
//       setModules(res.data || []);
//     } catch (e) {
//       console.error("Failed to load modules", e);
//     }
//   };

//   const loadReferencePages = async (moduleName) => {
//     try {
//       const res = await api.get("/reference/pages", {
//         params: { module: moduleName },
//       });
//       setPages(res.data || []);
//     } catch (e) {
//       console.error("Failed to load pages", e);
//     }
//   };

// //   useEffect(() => {
// //   api.get("/reference/modules").then(...)
// // }, []);

// // useEffect(() => {
// //   api.get("/reference/pages?module=" + activeModule).then(...)
// // }, [activeModule]);


//   return (
//     <div className={styles.container}>
//       {/* ================= TOP MODULE TABS ================= */}
//       <div className={styles.moduleHolder}>
//         {modules.map((m) => (
//           <div
//             key={m.moduleName}
//             className={`${styles.moduleTab} ${
//               activeModule === m.moduleName ? styles.active : ""
//             }`}
//             onClick={() => setActiveModule(m.moduleName)}
//           >
//             {m.moduleName}
//           </div>
//         ))}
//       </div>

//       {/* ================= IFRAME GRID ================= */}
//       <div className={styles.frameHolder}>
//         {pages.length === 0 && (
//           <div className={styles.noData}>No reference pages available</div>
//         )}

//         {pages.map((p, i) => (
//           <div className={styles.frameBox} key={i}>
//             <iframe
//               src={p.formUrl}
//               title={p.formName || `frame-${i}`}
//               loading="lazy"
//             />
//           </div>
//         ))}
//       </div>
//     </div>
//   );
// }

import React, { useEffect, useState } from "react";
import styles from "./ReferenceForms.module.css";
import { referenceModules, referencePages } from "./referenceConfig";

const DEFAULT_MODULE = "Contracts";

export default function ReferenceForms() {
  const [activeModule, setActiveModule] = useState(
    localStorage.getItem("reference_module") || DEFAULT_MODULE
  );

  const pages = referencePages[activeModule] || [];

  useEffect(() => {
    localStorage.setItem("reference_module", activeModule);
  }, [activeModule]);

  return (
    <div className={styles.container}>
      {/* ================= TOP MODULE TABS ================= */}
      <div className={styles.moduleHolder}>
        {referenceModules.map((module) => (
          <button
            key={module}
            className={`btn btn-primary ${
              activeModule === module ? styles.active : ""
            }`}
            onClick={() => setActiveModule(module)}
          >
            {module}
          </button>
        ))}
      </div>

      {/* ================= IFRAME GRID ================= */}
      <div className={styles.frameHolder}>
        {pages.length === 0 ? (
          <div className={styles.noData}>
            No reference forms available for <b>{activeModule}</b>
          </div>
        ) : (
          pages.map((page, index) => (
            <div className={styles.frameBox} key={index}>
              <div className={styles.frameTitle}>{page.name}</div>
              <iframe
                src={page.url}
                title={page.name}
                loading="lazy"
              />
            </div>
          ))
        )}
      </div>
    </div>
  );
}

