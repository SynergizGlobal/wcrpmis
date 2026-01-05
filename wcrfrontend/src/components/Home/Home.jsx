// import React from "react";
// import styles from "./Home.module.css";
// import { useEffect, useState } from "react";
// import { API_BASE_URL } from "../../config";
// export default function Home() {
//   const [projects, setProjects] = useState([]);
//   const [projectTypes, setProjectTypes] = useState([]);
//   const [selectedType, setSelectedType] = useState(null);
//   const [filteredProjects, setFilteredProjects] = useState([]);
//   // Fetch project types from backend
//   useEffect(() => {
//     fetch(`${API_BASE_URL}/projects/api/getProjectTypes`) // 🔁 Replace with your actual API endpoint
//       .then((response) => response.json())
//       .then((data) => setProjectTypes(data))
//       .catch((error) => console.error("Error fetching project types:", error));
//   }, []);
//   // Fetch data from your backend
//   useEffect(() => {
//     fetch(`${API_BASE_URL}/projects/api/getProjects`) // <-- change this to your actual endpoint
//       .then((response) => response.json())
//       .then((data) => setProjects(data))
//       .catch((error) => console.error("Error fetching projects:", error));
//   }, []);

//    // Function to handle click (like your JSP onclick)
//   const showProjectsByType = (projectTypeId, projectTypeName) => {
//     const filtered = projects.filter((p) => p.project_type_id === projectTypeId);
//     setFilteredProjects(filtered);
//     setSelectedType({ id: projectTypeId, name: projectTypeName });
//   };
//   const goToDashboard = (projectId) => {
//     console.log("Navigate to dashboard:", projectId);
//   };
//   // Helper values
//   const projectCount = projects?.length || 0;
//   const length = projects?.[0]?.length || 0;
//   const commissionedLength = projects?.[0]?.commissioned_length || 0;

//   return (
//     <div className={styles.home}>
//       { 
//         !selectedType ? (<div className={styles.stats}>
        
//           <div className={styles.homeStatsCard}>
//           <h3>{projectCount}</h3>
//           <p>Projects</p>
//         </div>
//         <div className={styles.homeStatsCard}>
//           <h3>{length} <span>km</span></h3>
//           <p>Length</p>
//         </div>
//         <div className={styles.homeStatsCard}>
//           <h3>{commissionedLength} <span>km</span></h3>
//           <p>Commissioned Length</p>
//         </div>
//       </div>) : (
//       <div></div>
//       )}

//       {!selectedType ? (<div className={styles.buttons}>
//         {projectTypes.length > 0 ? (
//         projectTypes.map((projectType) => (
//           <button
//             key={projectType.project_type_id}
//             onClick={() =>
//               showProjectsByType(
//                 projectType.project_type_id,
//                 projectType.project_type_name
//               )
//             }
//           >
//             {projectType.project_type_name}
//           </button>
//         ))
//       ) : (
//         <p>Loading project types...</p>
//       )}
//       </div>): (
//         <div id="projectListContainer" style={{ marginTop: "20px" }}>
//           <h2 id="projectTypeHeading">{selectedType.name}</h2>

//           <div
//             id="projectGrid"
//             style={{
//               display: "flex",
//               flexWrap: "wrap",
//               justifyContent: "center",
//               gap: "10px",
//               marginTop: "20px",
//             }}
//           >
//             {filteredProjects.map((p) => (
//               <div
//                 key={p.project_id}
//                 className="project-card"
//                 onClick={() => goToDashboard(p.project_id)}
//                 style={{
//                   background: "white",
//                   padding: "15px 25px",
//                   borderRadius: "8px",
//                   cursor: "pointer",
//                   boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
//                 }}
//               >
//                 {p.project_name}
//               </div>
//             ))}
//           </div>

//           {/* ✅ Stats Section */}
//           <div id="projectStats" style={{ marginTop: "30px" }}>
//             {filteredProjects.length > 0 ? (
//               (() => {
//                 const stats = filteredProjects[0]; // Taking first item stats
//                 return (
//                   <table
//                      className={styles.table}
//                   >
//                     <tbody>
//                       <tr>
//                         <td><b>Total Projects</b> - {filteredProjects.length}</td>
//                         <td><b>Ongoing Projects</b> - {filteredProjects.length}</td>
//                       </tr>
//                       <tr>
//                         <td><b>Total Length</b> - {stats.total_length}</td>
//                         <td><b>Total Earthwork</b> - {stats.total_earthwork}</td>
//                       </tr>
//                       <tr>
//                         <td><b>Completed Track</b> - {stats.completed_track}</td>
//                         <td><b>Commissioned Length</b> - {stats.commissioned_length}</td>
//                       </tr>
//                       <tr>
//                         <td>
//                           <b>Major Bridges</b> - {stats.completed_major_bridges}/
//                           {stats.total_major_bridges}
//                         </td>
//                         <td>
//                           <b>Minor Bridges</b> - {stats.completed_minor_bridges}/
//                           {stats.total_minor_bridges}
//                         </td>
//                       </tr>
//                       <tr>
//                         <td>
//                           <b>ROB</b> - {stats.completed_rob}/{stats.total_rob}
//                         </td>
//                         <td>
//                           <b>RUB</b> - {stats.completed_rub}/{stats.total_rub}
//                         </td>
//                       </tr>
//                     </tbody>
//                   </table>
//                 );
//               })()
//             ) : (
//               <p>No stats available</p>
//             )}
//           </div>

//           {/* Back Button */}
//           <div style={{ marginTop: "20px" }}>
//             <button
//               onClick={() => setSelectedType(null)}
//             >
//               ⬅ Back
//             </button>
//           </div>
//         </div>
//     )}
      
//     </div>
//   );
// }


import React, { useEffect, useMemo, useState } from "react";
import styles from "./Home.module.css";
import { API_BASE_URL } from "../../config";
import { useNavigate } from "react-router-dom";

export default function Home() {
  const navigate = useNavigate();

  const [projects, setProjects] = useState([]);
  const [projectTypes, setProjectTypes] = useState([]);

  const [selectedType, setSelectedType] = useState(null);     // { id, name }
  const [selectedProject, setSelectedProject] = useState(null); // project object

  /* ================= FETCH DATA ================= */

  useEffect(() => {
  fetch(`${API_BASE_URL}/projects/api/projectTypes`)
    .then(async (res) => {
      if (!res.ok) {
        const text = await res.text();
        throw new Error(text);
      }
      return res.json();
    })
    .then((data) => {
      setProjectTypes(Array.isArray(data) ? data : []);
    })
    .catch((err) => {
      console.error("Project Types API Error:", err.message);
      setProjectTypes([]);
    });

  fetch(`${API_BASE_URL}/projects/api/getProjectList`)
    .then(async (res) => {
      if (!res.ok) {
        const text = await res.text();
        throw new Error(text);
      }
      return res.json();
    })
    .then((data) => {
      setProjects(Array.isArray(data) ? data : []);
    })
    .catch((err) => {
      console.error("Projects API Error:", err.message);
      setProjects([]);
    });
}, []);



  /* ================= DERIVED DATA ================= */

  const filteredProjects = useMemo(() => {
    if (!selectedType) return [];
    return projects.filter(
      p => p.project_type_id === selectedType.id
    );
  }, [projects, selectedType]);

  /* ================= DYNAMIC HEADING ================= */

  const headingText = selectedProject
    ? selectedProject.project_name
    : selectedType
    ? selectedType.name
    : "";

  /* ================= STATS (MEMOIZED) ================= */

  const stats = useMemo(() => {
    const list = selectedProject
      ? [selectedProject]
      : filteredProjects;

    const sum = (key) =>
      list.reduce((acc, p) => acc + Number(p[key] || 0), 0);

    return {
      totalProjects: list.length,
      ongoingProjects: list.length,
      totalLength: sum("proposed_length"),
      commissionedLength: sum("commissioned_length"),
      totalEarthwork: sum("total_earthwork"),
      completedTrack: sum("completed_track"),
      majorBridges: `${sum("completed_major_bridges")}/${sum("total_major_bridges")}`,
      minorBridges: `${sum("completed_minor_bridges")}/${sum("total_minor_bridges")}`,
      rob: `${sum("completed_rob")}/${sum("total_rob")}`,
      rub: `${sum("completed_rub")}/${sum("total_rub")}`,
    };
  }, [filteredProjects, selectedProject]);

  /* ================= UI ================= */

  return (
    <div className={styles.home}>

      {/* ================= TOP STATS (ONLY HOME) ================= */}
      {!selectedType && (
        <div className={styles.stats}>
          <div className={styles.homeStatsCard}>
            <h3>{projects.length}</h3>
            <p>Projects</p>
          </div>
          <div className={styles.homeStatsCard}>
            <h3>
              {projects
                .reduce((a, p) => a + Number(p.proposed_length || 0), 0)
                .toFixed(2)}{" "}
              <span>km</span>
            </h3>
            <p>Length</p>
          </div>
          <div className={styles.homeStatsCard}>
            <h3>
              {projects
                .reduce((a, p) => a + Number(p.commissioned_length || 0), 0)
                .toFixed(2)}{" "}
              <span>km</span>
            </h3>
            <p>Commissioned Length</p>
          </div>
        </div>
      )}

      {/* ================= PROJECT TYPE BUTTONS ================= */}
      {!selectedType && (
        <div className={styles.buttons}>
          {Array.isArray(projectTypes) && projectTypes.map(type => (
            <button
              key={type.project_type_id}
              onClick={() => {
                setSelectedType({
                  id: type.project_type_id,
                  name: type.project_type_name,
                });
                setSelectedProject(null);
              }}
            >
              {type.project_type_name}
            </button>
          ))}
        </div>
      )}

      {/* ================= PROJECT VIEW ================= */}
      {selectedType && (
        <>
        <div className={styles.topHeadingInner} >
            {/* BACK BUTTON */}
            <button
              className={styles.backBtn}
              onClick={() => {
                setSelectedType(null);
                setSelectedProject(null);
              }}
            >
              Back
            </button>

            {/* DYNAMIC HEADING */}
            {headingText && (
              <div className={styles.dynamicHeading}>
                <h2>{headingText}</h2>
              </div>
            )}

            <div>
            &nbsp;
            </div>
            
          </div>


          <div className={styles.projectSection}>
            {/* LEFT : PROJECT LIST */}
            <div className={styles.projectList}>
              {filteredProjects.map(p => (
                <div
                  key={p.project_id}
                  className={`${styles.projectCard} ${
                    selectedProject?.project_id === p.project_id
                      ? styles.active
                      : ""
                  }`}
                  onClick={() =>
                    setSelectedProject(
                      selectedProject?.project_id === p.project_id ? null : p
                    )
                  }
                >
                  {p.project_name}
                </div>
              ))}
            </div>

            {/* RIGHT : STATS */}
            <div className={styles.statsBox}>
              <h3>{selectedProject ? "Project Details" : "Overall"}</h3>

              <div className={styles.statsRow}>
                <div>Total Projects - {stats.totalProjects}</div>
                <div>Ongoing Projects - {stats.ongoingProjects}</div>
              </div>

              <div className={styles.statsRow}>
                <div>Total Length - {stats.totalLength.toFixed(2)} Km</div>
                <div>Total Earthwork - {stats.totalEarthwork.toFixed(2)} Cum</div>
              </div>

              <div className={styles.statsRow}>
                <div>Completed Track - {stats.completedTrack.toFixed(2)} Tkm</div>
                <div>
                  Commissioned Length -{" "}
                  {stats.commissionedLength.toFixed(2)} Tkm
                </div>
              </div>

              <div className={styles.statsRow}>
                <div>Major Bridges - {stats.majorBridges}</div>
                <div>Minor Bridges - {stats.minorBridges}</div>
              </div>

              <div className={styles.statsRow}>
                <div>ROB - {stats.rob}</div>
                <div>RUB - {stats.rub}</div>
              </div>

              {/* VIEW DASHBOARD */}
              {selectedProject && (
                <div className={styles.viewDashboard}>
                  <button
                    onClick={() =>
                      navigate(
                        `/work-overview-dashboard/${selectedProject.project_id}`
                      )
                    }
                  >
                    View Dashboard
                  </button>
                </div>
              )}
            </div>
          </div>
        </>
      )}
    </div>
  );
}
