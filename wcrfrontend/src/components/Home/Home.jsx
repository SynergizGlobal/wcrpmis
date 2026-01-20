import React, { useEffect, useMemo, useState } from "react";
import styles from "./Home.module.css";
import { API_BASE_URL } from "../../config";
import { useNavigate } from "react-router-dom";

export default function Home() {
  const navigate = useNavigate();

  const [projects, setProjects] = useState([]);
  const [projectTypes, setProjectTypes] = useState([]);

  const [selectedType, setSelectedType] = useState(null);
  const [selectedProject, setSelectedProject] = useState(null);
  

  /* ================= FETCH DATA ================= */

  useEffect(() => {
    fetch(`${API_BASE_URL}/projects/api/projectTypes`)
      .then(res => res.json())
      .then(data => setProjectTypes(Array.isArray(data) ? data : []));

    fetch(`${API_BASE_URL}/projects/api/getProjectList`)
      .then(res => res.json())
      .then(data => setProjects(Array.isArray(data) ? data : []));
  }, []);


  /* ================= FILTER PROJECTS ================= */

  const filteredProjects = useMemo(() => {
    if (!selectedType) return [];
    return projects.filter(
      p => String(p.project_type_id) === String(selectedType.id)
    );
  }, [projects, selectedType]);
  
  
  const showProjectsByType = (typeId, typeName) => {
    setSelectedType({
      id: typeId,
      name: typeName
    });

    setSelectedProject(null); // same as selectedProjectId = null
  };

  

  /* ================= STATS ================= */

  const stats = useMemo(() => {
    const list = selectedProject ? [selectedProject] : filteredProjects;

    const sum = key =>
      list.reduce((a, p) => a + Number(p[key] || 0), 0);

    return {
      totalProjects: list.length,
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

      {/* ================= HOME ================= */}
      {!selectedType && (
        <>
          <div className={styles.stats}>
            <div className={styles.homeStatsCard}>
              <h3>{projects.length}</h3>
              <p>Projects</p>
            </div>

            <div className={styles.homeStatsCard}>
              <h3>
                {projects.reduce((a, p) => a + Number(p.proposed_length || 0), 0).toFixed(2)}
                <span> km</span>
              </h3>
              <p>Length</p>
            </div>

            <div className={styles.homeStatsCard}>
              <h3>
                {projects.reduce((a, p) => a + Number(p.commissioned_length || 0), 0).toFixed(2)}
                <span> km</span>
              </h3>
              <p>Commissioned Length</p>
            </div>
          </div>

          <div className={styles.buttons}>
            {projectTypes.map(t => (
				<button
				  onClick={() =>
				    showProjectsByType(
				      t.project_type_id,
				      t.project_type_name
				    )
				  }
				>
				  {t.project_type_name}
				</button>

            ))}
          </div>
        </>
      )}

      {/* ================= PROJECT TYPE VIEW ================= */}
      {selectedType && (
        <>
		<div className={styles.topHeadingInner}>
		  <button
		    className={styles.backBtn}
		    onClick={() => {
		      setSelectedType(null);
		      setSelectedProject(null);
		    }}
		  >
		    Back
		  </button>

		  <h2 className={styles.centerHeading}>
		    {selectedProject
		      ? selectedProject.project_name
		      : selectedType.name}
		  </h2>
		</div>


		<div className={styles.projectSection}>
		  
		  {/* LEFT SIDE – PROJECT GRID */}
		  <div className={styles.projectCard}>
		    {console.log("Selected Type:", selectedType?.name)}
		    {console.log("Projects array:", projects)}

		    {projects
		      .filter(
		        (p) =>
		          !selectedType || p.project_type_name === selectedType.name
		      )
		      .map((p) => (
		        <div
		          key={p.project_id}
		          className={`${styles.projectCard} ${
		            selectedProject?.project_id === p.project_id ? styles.active : ""
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




		  {/* RIGHT SIDE – OVERALL / PROJECT CARD */}
		  <div className={styles.statsBox}>
		    <h3>{selectedProject ? "Project Details" : "Overall"}</h3>

		    {!selectedProject && (
		      <div className={styles.statsRow}>
		        <div>Total Projects - {stats.totalProjects}</div>
		        <div>Ongoing Projects - {stats.totalProjects}</div>
		      </div>
		    )}

		    <div className={styles.statsRow}>
		      <div>Total Length - {stats.totalLength.toFixed(2)} Km</div>
		      <div>Total Earthwork - {stats.totalEarthwork.toFixed(2)} Cum</div>
		    </div>

		    <div className={styles.statsRow}>
		      <div>Completed Track - {stats.completedTrack.toFixed(2)} Tkm</div>
		      <div>Commissioned Length - {stats.commissionedLength.toFixed(2)} Tkm</div>
		    </div>

		    <div className={styles.statsRow}>
		      <div>Major Bridges - {stats.majorBridges} Nos</div>
		      <div>Minor Bridges - {stats.minorBridges} Nos</div>
		    </div>

		    <div className={styles.statsRow}>
		      <div>ROB - {stats.rob} Nos</div>
		      <div>RUB - {stats.rub} Nos</div>
		    </div>

		    {selectedProject && (
		      <div className={styles.viewDashboard}>
		        <button
		          onClick={() =>
		            navigate(`/work-overview-dashboard?project_id=${selectedProject.project_id}`)
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
