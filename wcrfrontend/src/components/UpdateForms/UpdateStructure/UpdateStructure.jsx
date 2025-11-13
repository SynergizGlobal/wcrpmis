import React, { useState, useEffect } from "react";
import Select from "react-select";
import styles from './UpdateStructure.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import axios from "axios";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";

export default function UpdateStructure() {
  const location = useLocation();
  const navigate = useNavigate();
  const [structureFormGrid, setStructureFormGrid] = useState([]);
  const [filters, setFilters] = useState({
    contract: "",
    structureType: "",
    workStatus: "",
  });

  useEffect(() => {
    fetchStructureFormGrid();
  }, []);

  const fetchStructureFormGrid = async () => {
    try {
      const res = await axios.get(`${API_BASE_URL}/structure-form`, { withCredentials: true });
      setStructureFormGrid(res.data || []);
    } catch (err) {
      console.error("Error fetching projects:", err);
    }
  };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  };

  const handleAdd = () => navigate("get-structure-form");
  const handleEdit = (project) => navigate("get-structure-form", { state: { project } });

  const isStructureForm = location.pathname.endsWith("/get-structure-form");

  return (
    <div className={styles.container}>
      {/* Top Bar */}
      { !isStructureForm &&(
      <div className="pageHeading">
        <h2>Structure Form</h2>
        <div  className="rightBtns">
          {/* <button className="btn btn-primary" onClick={handleAdd}>
            <CirclePlus size={16} /> Add
          </button>
          <button className="btn btn-primary">
            <LuCloudDownload size={16} /> Export
          </button> */}

          &nbsp;
        </div>
      </div>
      )}
      
      {/* Filters */}
      {!isStructureForm && (
        
        <div className="innerPage">
          <div className="filterRow">
            {Object.keys(filters).map((key) => {
              const options = [
                { value: "", label: `Select ${key}` },
                { value: "demo1", label: `${key} 1` },
                { value: "demo2", label: `${key} 2` },
              ];

              return (
                <div className="filterOptions" key={key}>
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
                  <th>Project</th>
                  <th>Structure Type</th>
                  <th>Structure</th>
                  <th>Contract</th>
                  <th>Work Status</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {structureFormGrid.length > 0 ? (
                  structureFormGrid.map((sf, index) => (
                    <tr key={index}>
                      <td>{sf.projectId}</td>
                      <td>{sf.structure_type_fk}</td>
                      <td>{sf.structure}</td>
                      <td>{sf.contract_id_fk}</td>
                      <td>{sf.work_status_fk}</td>
                      <td>
                        <button className="btn btn-sm btn-outline-primary" onClick={() => handleEdit(sf)}>
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