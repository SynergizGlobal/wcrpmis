import React, { useState, useEffect } from "react";
import Select from "react-select";
import styles from "./Project.module.css";
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";

export default function Project() {
  const location = useLocation();
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [filters, setFilters] = useState({
    work: "",
    hod: "",
    dyhod: "",
    contractor: "",
    contractStatus: "",
    statusOfWork: "",
  });

  useEffect(() => {
    fetchProjects();
  }, []);

  const fetchProjects = async () => {
    try {
      const res = await api.get(`${API_BASE_URL}/projects`, { withCredentials: true });
      setProjects(res.data || []);
    } catch (err) {
      console.error("Error fetching projects:", err);
    }
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  };

  const handleAdd = () => navigate("projectform");
  const handleEdit = (project) => navigate("projectform", { state: { project } });

  const isProjectForm = location.pathname.endsWith("/projectform");

  return (
    <div className={styles.container}>
      {/* Top Bar */}
      { !isProjectForm &&(
      <div className="pageHeading">
        <h2>Project</h2>
        <div  className="rightBtns">
          <button className="btn btn-primary" onClick={handleAdd}>
            <CirclePlus size={16} /> Add
          </button>
          <button className="btn btn-primary">
            <LuCloudDownload size={16} /> Export
          </button>
        </div>
      </div>
      )}
      
      {/* Filters */}
      {!isProjectForm && (
        
        <div className="innerPage">
          <div className={styles.filterRow}>
            {Object.keys(filters).map((key) => {
              const options = [
                { value: "", label: `Select ${key}` },
                { value: "demo1", label: `${key} 1` },
                { value: "demo2", label: `${key} 2` },
              ];

              return (
                <div className={styles.filterOptions} key={key}>
                  <Select
                    options={options}
                    value={options.find((opt) => opt.value === filters[key])}
                    onChange={(selectedOption) =>
                      handleFilterChange({
                        target: { name: key, value: selectedOption.value },
                      })
                    }
                    placeholder={`Select ${key}`}
                    isSearchable
                    styles={{
                      control: (base) => ({
                        ...base,
                        minHeight: "32px",
                        borderColor: "#ced4da",
                        boxShadow: "none",
                        "&:hover": { borderColor: "#86b7fe" },
                      }),
                      dropdownIndicator: (base) => ({
                        ...base,
                        padding: "2px 6px",
                      }),
                      valueContainer: (base) => ({
                        ...base,
                        padding: "0 6px",
                      }),
                      menu: (base) => ({
                        ...base,
                        zIndex: 9999, // prevents clipping inside modals or cards
                      }),
                    }}
                  />
                </div>
              );
            })}
            <button className="btn btn-2 btn-primary">Clear Filters</button>
          </div>
          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.projectTable}>
              <thead>
                <tr>
                  <th>Project ID</th>
                  <th>Project Name</th>
                  <th>Project Status</th>
                  <th>Sanctioned Year</th>
                  <th>Division</th>
                  <th>Section</th>
                  <th>Remarks</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {projects.length > 0 ? (
                  projects.map((proj, index) => (
                    <tr key={index}>
                      <td>{proj.projectId}</td>
                      <td>{proj.projectName}</td>
                      <td>{proj.projectStatus}</td>
                      <td>{proj.sanctionedYear}</td>
                      <td>{proj.division}</td>
                      <td>{proj.section}</td>
                      <td>{proj.remarks}</td>
                      <td>
                        <button className="btn btn-sm btn-outline-primary" onClick={() => handleEdit(proj)}>
                          Edit
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="8" style={{ textAlign: "center" }}>
                      No records found
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      )}
        <Outlet />
    </div>
  );
}
