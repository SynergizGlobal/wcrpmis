import React from "react";
import styles from "./Home.module.css";
import { useEffect, useState } from "react";
import { API_BASE_URL } from "../../config";
export default function Home() {
  const [projects, setProjects] = useState([]);
  const [projectTypes, setProjectTypes] = useState([]);
  const [selectedType, setSelectedType] = useState(null);
  const [filteredProjects, setFilteredProjects] = useState([]);
  // Fetch project types from backend
  useEffect(() => {
    fetch(`${API_BASE_URL}/projects/api/getProjectTypes`) // 🔁 Replace with your actual API endpoint
      .then((response) => response.json())
      .then((data) => setProjectTypes(data))
      .catch((error) => console.error("Error fetching project types:", error));
  }, []);
  // Fetch data from your backend
  useEffect(() => {
    fetch(`${API_BASE_URL}/projects/api/getProjects`) // <-- change this to your actual endpoint
      .then((response) => response.json())
      .then((data) => setProjects(data))
      .catch((error) => console.error("Error fetching projects:", error));
  }, []);

   // Function to handle click (like your JSP onclick)
  const showProjectsByType = (projectTypeId, projectTypeName) => {
    const filtered = projects.filter((p) => p.project_type_id === projectTypeId);
    setFilteredProjects(filtered);
    setSelectedType({ id: projectTypeId, name: projectTypeName });
  };
  const goToDashboard = (projectId) => {
    console.log("Navigate to dashboard:", projectId);
  };
  // Helper values
  const projectCount = projects?.length || 0;
  const length = projects?.[0]?.length || 0;
  const commissionedLength = projects?.[0]?.commissioned_length || 0;

  return (
    <div className={styles.home}>
      { 
        !selectedType ? (<div className={styles.stats}>
        
          <div className={styles.homeStatsCard}>
          <h3>{projectCount}</h3>
          <p>Projects</p>
        </div>
        <div className={styles.homeStatsCard}>
          <h3>{length} <span>km</span></h3>
          <p>Length</p>
        </div>
        <div className={styles.homeStatsCard}>
          <h3>{commissionedLength} <span>km</span></h3>
          <p>Commissioned Length</p>
        </div>
      </div>) : (
      <div></div>
      )}

      {!selectedType ? (<div className={styles.buttons}>
        {projectTypes.length > 0 ? (
        projectTypes.map((projectType) => (
          <button
            key={projectType.project_type_id}
            onClick={() =>
              showProjectsByType(
                projectType.project_type_id,
                projectType.project_type_name
              )
            }
          >
            {projectType.project_type_name}
          </button>
        ))
      ) : (
        <p>Loading project types...</p>
      )}
      </div>): (
        <div id="projectListContainer" style={{ marginTop: "20px" }}>
          <h2 id="projectTypeHeading">{selectedType.name}</h2>

          <div
            id="projectGrid"
            style={{
              display: "flex",
              flexWrap: "wrap",
              justifyContent: "center",
              gap: "10px",
              marginTop: "20px",
            }}
          >
            {filteredProjects.map((p) => (
              <div
                key={p.project_id}
                className="project-card"
                onClick={() => goToDashboard(p.project_id)}
                style={{
                  background: "white",
                  padding: "15px 25px",
                  borderRadius: "8px",
                  cursor: "pointer",
                  boxShadow: "0 2px 4px rgba(0,0,0,0.1)",
                }}
              >
                {p.project_name}
              </div>
            ))}
          </div>

          {/* ✅ Stats Section */}
          <div id="projectStats" style={{ marginTop: "30px" }}>
            {filteredProjects.length > 0 ? (
              (() => {
                const stats = filteredProjects[0]; // Taking first item stats
                return (
                  <table
                     className={styles.table}
                  >
                    <tbody>
                      <tr>
                        <td><b>Total Projects</b> - {filteredProjects.length}</td>
                        <td><b>Ongoing Projects</b> - {filteredProjects.length}</td>
                      </tr>
                      <tr>
                        <td><b>Total Length</b> - {stats.total_length}</td>
                        <td><b>Total Earthwork</b> - {stats.total_earthwork}</td>
                      </tr>
                      <tr>
                        <td><b>Completed Track</b> - {stats.completed_track}</td>
                        <td><b>Commissioned Length</b> - {stats.commissioned_length}</td>
                      </tr>
                      <tr>
                        <td>
                          <b>Major Bridges</b> - {stats.completed_major_bridges}/
                          {stats.total_major_bridges}
                        </td>
                        <td>
                          <b>Minor Bridges</b> - {stats.completed_minor_bridges}/
                          {stats.total_minor_bridges}
                        </td>
                      </tr>
                      <tr>
                        <td>
                          <b>ROB</b> - {stats.completed_rob}/{stats.total_rob}
                        </td>
                        <td>
                          <b>RUB</b> - {stats.completed_rub}/{stats.total_rub}
                        </td>
                      </tr>
                    </tbody>
                  </table>
                );
              })()
            ) : (
              <p>No stats available</p>
            )}
          </div>

          {/* Back Button */}
          <div style={{ marginTop: "20px" }}>
            <button
              onClick={() => setSelectedType(null)}
            >
              ⬅ Back
            </button>
          </div>
        </div>
    )}
      
    </div>
  );
}
