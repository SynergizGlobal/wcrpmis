import React, { useEffect, useState } from "react";
import Select from "react-select";
import styles from './DeleteActivities.module.css';
import api from "../../../api/axiosInstance";
import { API_BASE_URL } from "../../../config";

export default function DeleteActivities() {

  const [projects, setProjects] = useState([]);
  const [contracts, setContracts] = useState([]);
  const [structureTypes, setStructureTypes] = useState([]);
  const [structures, setStructures] = useState([]);
  const [components, setComponents] = useState([]);
  const [componentIds, setComponentIds] = useState([]);
  const [activities, setActivities] = useState([]);

  const [selectedProject, setSelectedProject] = useState(null);
  const [selectedContract, setSelectedContract] = useState(null);
  const [selectedStructureType, setSelectedStructureType] = useState(null);
  const [selectedStructure, setSelectedStructure] = useState(null);
  const [selectedComponent, setSelectedComponent] = useState(null);
  const [selectedComponentId, setSelectedComponentId] = useState(null);
  const [selectedActivityIds, setSelectedActivityIds] = useState([]);

  const [page, setPage] = useState(1);
    const [perPage, setPerPage] = useState(10);
    const [search, setSearch] = useState('');

    const filteredActivities = activities.filter((a) => {
      const q = search.toLowerCase();
      return (
        (a.strip_chart_component ?? '').toLowerCase().includes(q) ||
        (a.strip_chart_activity_name ?? '').toLowerCase().includes(q) ||
        (a.p6_task_code ?? '').toLowerCase().includes(q)
      );
    });

    const totalPages = Math.ceil(filteredActivities.length / perPage);
    const paginatedActivities = filteredActivities.slice(
      (page - 1) * perPage,
      page * perPage
    );
	
  useEffect(() => {
      loadProjects();
  }, []);
  
  const renderPageButtons = (page, totalPages, setPageFn) => {
  	if (totalPages <= 1) return null;

  	const pages = Array.from({ length: totalPages }, (_, i) => i + 1)
  		.filter((p) => {
  			if (p <= 2 || p > totalPages - 2) return true;
  			if (p >= page - 1 && p <= page + 1) return true;
  			return false;
  		})
  		.reduce((acc, p, idx, arr) => {
  			if (idx > 0 && p - arr[idx - 1] > 1) acc.push("...");
  			acc.push(p);
  			return acc;
  		}, []);

  	return (
  		<>
  			<button
  				disabled={page === 1}
  				onClick={() => setPageFn(page - 1)}
  				className="pageBtn"
  			>
  				‹
  			</button>

  			{pages.map((item, idx) =>
  				item === "..." ? (
  					<span key={idx} className="ellipsis">
  						...
  					</span>
  				) : (
  					<button
  						key={idx}
  						onClick={() => setPageFn(item)}
  						className={`pageBtn ${item === page ? "activePage" : ""}`}
  					>
  						{item}
  					</button>
  				)
  			)}

  			<button
  				disabled={page === totalPages}
  				onClick={() => setPageFn(page + 1)}
  				className="pageBtn"
  			>
  				›
  			</button>
  		</>
  	);
  };

  const loadProjects = async () => {
      try {
          const res = await api.get(`${API_BASE_URL}/ajax/getDeleteActivitiesProjectsList`);
          
          const mapped = res.data.map((p) => ({
              label: p.project_name,
              value: p.project_id
          }));
          setProjects(mapped); // ← this was missing!
          
      } catch (err) {
          console.error("loadProjects error:", err);
      }
  };
  /* =========================
     LOAD CONTRACTS
  ========================= */
  useEffect(() => {
    if (selectedProject) {
      // clear all downstream
      setSelectedContract(null);
      setContracts([]);
      setSelectedStructureType(null);
      setStructureTypes([]);
      setSelectedStructure(null);
      setStructures([]);
      setSelectedComponent(null);
      setComponents([]);
      setSelectedComponentId(null);
      setComponentIds([]);
      setActivities([]);
      setSelectedActivityIds([]);

      getContractsList(selectedProject.value);
    }
  }, [selectedProject]);

  const getContractsList = async (projectId) => {
    try {
      const res = await api.get(
        `${API_BASE_URL}/ajax/getDeleteActivitiesContractsList`,
        { params: { project_id_fk: projectId } }
      );
      const mapped = res.data.map((c) => ({
        label: c.contract_short_name,
        value: c.contract_id
      }));
      setContracts(mapped);
    } catch (err) {
      console.error("getContractsList error:", err);
    }
  };

  /* =========================
     STRUCTURE TYPES
  ========================= */
  useEffect(() => {
    if (selectedContract) {
      setSelectedStructureType(null);
      setStructureTypes([]);
      setSelectedStructure(null);
      setStructures([]);
      setSelectedComponent(null);
      setComponents([]);
      setSelectedComponentId(null);
      setComponentIds([]);
      setActivities([]);
      setSelectedActivityIds([]);

      getStructureTypes();
    }
  }, [selectedContract]);

  const getStructureTypes = async () => {
    try {
      const res = await api.get(
        `${API_BASE_URL}/ajax/getStructureTypesInDeleteActivities`,
        { params: { contract_id_fk: selectedContract.value } }
      );
      const mapped = res.data.map((s) => ({
        label: s.structure_type,
        value: s.structure_type
      }));
      setStructureTypes(mapped);
    } catch (err) {
      console.error("getStructureTypes error:", err);
    }
  };

  /* =========================
     STRUCTURES
  ========================= */
  useEffect(() => {
    if (selectedStructureType) {
      setSelectedStructure(null);
      setStructures([]);
      setSelectedComponent(null);
      setComponents([]);
      setSelectedComponentId(null);
      setComponentIds([]);
      setActivities([]);
      setSelectedActivityIds([]);

      getStructures();
    }
  }, [selectedStructureType]);

  const getStructures = async () => {
    try {
      const res = await api.get(
        `${API_BASE_URL}/ajax/getDeleteActivitiesStructures`,
        {
          params: {
            contract_id_fk: selectedContract.value,
            structure_type_fk: selectedStructureType.value
          }
        }
      );
	  console.log("Structures response:", JSON.stringify(res.data[0]));
      const mapped = res.data.map((s) => ({
        label: s.strip_chart_structure_id_fk,
        value: s.strip_chart_structure_id_fk
      }));
      setStructures(mapped);
    } catch (err) {
      console.error("getStructures error:", err);
    }
  };

  /* =========================
     COMPONENTS
  ========================= */
  useEffect(() => {
    if (selectedStructure) {
      setSelectedComponent(null);
      setComponents([]);
      setSelectedComponentId(null);
      setComponentIds([]);
      setActivities([]);
      setSelectedActivityIds([]);

      getComponents();
    }
  }, [selectedStructure]);

  const getComponents = async () => {
      try {
          const res = await api.get(
              `${API_BASE_URL}/ajax/getDeleteActivitiesComponentsList`,
              {
                  params: {
                      contract_id_fk: selectedContract.value,
                      strip_chart_structure_id_fk: selectedStructure.value,
                      structure_type_fk: selectedStructureType.value  // ← ADD THIS
                  }
              }
          );
          console.log("Components response:", res.data);
          const mapped = res.data.map((c) => ({
              label: c.strip_chart_component,
              value: c.strip_chart_component
          }));
          setComponents(mapped);
      } catch (err) {
          console.error("getComponents error:", err);
      }
  };
  /* =========================
     COMPONENT IDs
  ========================= */
  useEffect(() => {
    if (selectedComponent) {
      setSelectedComponentId(null);
      setComponentIds([]);
      setActivities([]);
      setSelectedActivityIds([]);

      getComponentIds();
    }
  }, [selectedComponent]);

  const getComponentIds = async () => {
      try {
          const res = await api.get(
              `${API_BASE_URL}/ajax/getDeleteActivitiesComponentIdsList`,
              {
                  params: {
                      contract_id_fk: selectedContract.value,
                      strip_chart_structure_id_fk: selectedStructure.value,
                      strip_chart_component: selectedComponent.value,
                      structure_type_fk: selectedStructureType.value  
                  }
              }
          );
          console.log("ComponentIds response:", res.data);
          const mapped = res.data.map((c) => ({
              label: c.strip_chart_component_id,
              value: c.strip_chart_component_id
          }));
          setComponentIds(mapped);
      } catch (err) {
          console.error("getComponentIds error:", err);
      }
  };

  /* =========================
     ACTIVITIES (filters list)
  ========================= */
  useEffect(() => {
    if (selectedComponentId) {
      setActivities([]);
      setSelectedActivityIds([]);
      getActivities();
    }
  }, [selectedComponentId]);

  const getActivities = async () => {
    try {
      const res = await api.get(
        `${API_BASE_URL}/ajax/getDeleteActivitiesfiltersList`,
        {
          params: {
            contract_id_fk: selectedContract.value,
            strip_chart_structure_id_fk: selectedStructure.value,
            strip_chart_component: selectedComponent.value,
            strip_chart_component_id: selectedComponentId.value,
            structure_type_fk: selectedStructureType.value
          }
        }
      );
      setActivities(res.data);
    } catch (err) {
      console.error("getActivities error:", err);
    }
  };

  /* =========================
     CHECKBOX
  ========================= */
  const handleCheckboxChange = (activityId, checked) => {
    if (checked) {
      setSelectedActivityIds(prev => [...prev, activityId]);
    } else {
      setSelectedActivityIds(prev => prev.filter(id => id !== activityId));
    }
  };

  /* =========================
     DELETE
  ========================= */
  const handleDelete = async () => {
    if (selectedActivityIds.length === 0) {
      alert("Please select at least one activity to delete.");
      return;
    }
    if (!window.confirm("Are you sure you want to delete the selected activities?")) return;

    try {
      // Using URLSearchParams because backend uses @ModelAttribute (form params, not JSON)
      const params = new URLSearchParams();
      params.append("contract_id_fk", selectedContract?.value || "");
      params.append("structure_type_fk", selectedStructureType?.value || "");
      params.append("strip_chart_structure_id_fk", selectedStructure?.value || "");
      params.append("strip_chart_component", selectedComponent?.value || "");
      params.append("strip_chart_component_id", selectedComponentId?.value || "");
      selectedActivityIds.forEach(id => params.append("activity_ids", id));
      selectedActivityIds.forEach(() => params.append("ids", "1"));

      await api.post(`${API_BASE_URL}/delete-activities-bulk`, params, {
        headers: { "Content-Type": "application/x-www-form-urlencoded" }
      });

      alert("Activities deleted successfully.");
      getActivities(); // refresh table
      setSelectedActivityIds([]);
    } catch (err) {
      console.error("handleDelete error:", err);
      alert("Delete failed. Please try again.");
    }
  };

  /* =========================
     RESET
  ========================= */
  const handleReset = () => {
    setSelectedProject(null);
    setSelectedContract(null);
    setSelectedStructureType(null);
    setSelectedStructure(null);
    setSelectedComponent(null);
    setSelectedComponentId(null);
    setProjects([]);
    setContracts([]);
    setStructureTypes([]);
    setStructures([]);
    setComponents([]);
    setComponentIds([]);
    setActivities([]);
    setSelectedActivityIds([]);
    loadProjects(); // reload projects after reset
  };

  return (
    <div className={styles.container}>

      <div className="pageHeading">
        <h2>Delete Activities</h2>
        <div className="rightBtns">&nbsp;</div>
      </div>

      <div className={styles.fieldsSection}>

        {/* Row 1: Project + Contract */}
        <div className="form-row">
          <div className="form-field">
            <label>Project</label>
            <Select
              placeholder="Select Project"
              options={projects}
              value={selectedProject}
              onChange={setSelectedProject}
            />
          </div>
          <div className="form-field">
            <label>Contract <span style={{color:'red'}}>*</span></label>
            <Select
              placeholder="Select Contract"
              options={contracts}
              value={selectedContract}
              onChange={setSelectedContract}
              isDisabled={!selectedProject}
            />
          </div>
        </div>

        {/* Row 2: Structure Type + Structure + Component + Component ID */}
        <div className="form-row">
          <div className="form-field">
            <label>Structure Type <span style={{color:'red'}}>*</span></label>
            <Select
              placeholder="Structure Type"
              options={structureTypes}
              value={selectedStructureType}
              onChange={setSelectedStructureType}
              isDisabled={!selectedContract}
            />
          </div>
          <div className="form-field">
            <label>Structure <span style={{color:'red'}}>*</span></label>
            <Select
              placeholder="Structure"
              options={structures}
              value={selectedStructure}
              onChange={setSelectedStructure}
              isDisabled={!selectedStructureType}
            />
          </div>
          <div className="form-field">
            <label>Component</label>
            <Select
              placeholder="Component"
              options={components}
              value={selectedComponent}
              onChange={setSelectedComponent}
              isDisabled={!selectedStructure}
            />
          </div>
          <div className="form-field">
            <label>Component ID</label>
            <Select
              placeholder="Component ID"
              options={componentIds}
              value={selectedComponentId}
              onChange={setSelectedComponentId}
              isDisabled={!selectedComponent}
            />
          </div>
        </div>

        <br />

        {/* Activities Table */}
        {activities.length > 0 && (
			<div className={`dataTable ${styles.tableWrapper}`}>
			<div className={styles.tableToolbar}>
		      <div className={styles.showEntriesCount}>
		        <label>Show</label>
		        <select
		          value={perPage}
		          onChange={(e) => { setPerPage(Number(e.target.value)); setPage(1); }}
		        >
		          {[5, 10, 20, 50, 100].map((size) => (
		            <option key={size} value={size}>{size}</option>
		          ))}
		        </select>
		        <span>entries</span>
		      </div>

		      <div className={styles.searchBox}>
		        <label>Search</label>
		        <input
		          type="text"
		          placeholder="Search..."
		          value={search}
		          onChange={(e) => { setSearch(e.target.value); setPage(1); }}
		        />
		      </div>
		    </div>
          <table border="1" width="100%">
            <thead>
              <tr>
			  <th></th> 
                <th>Component</th>
                <th>Activity</th>
                <th>Scope</th>
                <th>Completed</th>
                <th>P6 Task Code</th>
              </tr>
            </thead>
            <tbody>
              {filteredActivities.map((a, index) => (
                <tr key={a.activity_id}>
                  <td>
                    <input
                      type="checkbox"
                      checked={selectedActivityIds.includes(a.activity_id)}
                      onChange={(e) => handleCheckboxChange(a.activity_id, e.target.checked)}
                    />
                  </td>
                  <td>{a.strip_chart_component}</td>
                  <td>{a.strip_chart_activity_name}</td>
                  <td>{a.scope}</td>
                  <td>{a.completed}</td>
                  <td>{a.p6_task_code}</td>
                </tr>
              ))}
            </tbody>
          </table>
		  <div className="paginationRow">
		    <div className="entryCount">
		       Showing {filteredActivities.length === 0 ? 0 : 1} – {filteredActivities.length} of {filteredActivities.length} entries
		    </div>
		    <div className="pagination">
		      {renderPageButtons(page, totalPages, setPage)}
		    </div>
		  </div>
		  </div>
        )}

        <br />

        <div className="form-post-buttons">
          <button
            className="btn btn-primary"
            onClick={handleDelete}
            disabled={selectedActivityIds.length === 0}
          >
            DELETE
          </button>
          <button
            className="btn btn-secondary"
            onClick={handleReset}
          >
            RESET
          </button>
        </div>

      </div>
    </div>
  );
}