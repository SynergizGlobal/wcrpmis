import React, { useState, useEffect } from "react";
import Select from "react-select";
import styles from './UtilityShifting.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload, LuUpload, LuDownload } from "react-icons/lu";
import axios from "axios";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";

export default function UtilityShifting() {

  const location = useLocation();
      const navigate = useNavigate();
      const [utilityShiftingId, setUtilityShiftingId] = useState([]);
      const [filePath, setFilePath] = useState([]);
      const [filters, setFilters] = useState({
        location: "",
        category: "",
        utilityType: "",
        status: "",
      });
  
    useEffect(() => {
      fetchUtilityShiftingId();
    }, []);
  
     useEffect(() => {
      fetchFilePath();
    }, []);

    const fetchUtilityShiftingId = async () => {
      try {
        const res = await axios.get(`${API_BASE_URL}/ajax/form/add-utility-shifting`, { withCredentials: true });
        setUtilityShiftingId(res.data || []);
      } catch (err) {
        console.error("Error fetching projects:", err);
      }
    };

const fetchFilePath = async () => {
      try {
        const res = await axios.get(`${API_BASE_URL}/ajax/form/get-utility-shifting/get-utility-shifting`, { withCredentials: true });
        setFilePath(res.data || []);
      } catch (err) {
        console.error("Error fetching projects:", err);
      }
    };

  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  };

  const handleAdd = () => navigate("add-utility-shifting");
  const handleEdit = (project) => navigate("add-utility-shifting", { state: { utilityShiftingId } });

  const isUtilityShiftingForm = location.pathname.endsWith("/add-utility-shifting");

  return (
    <div className={styles.container}>
     { !isUtilityShiftingForm &&(
      <div className="pageHeading">
        <h2>Utility Shifting</h2>
        <div  className="rightBtns">
          <button className="btn-2 transparent-btn">
              <LuDownload size={16} />
            </button>
          <button className="btn btn-primary">
            <LuUpload size={16} /> Upload
          </button>
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
      {!isUtilityShiftingForm && (
        
        <div className="innerPage">
          <div className={styles.filterRow}>
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
                    classNamePrefix="react-select"
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
            <table className={styles.designTable}>
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Description</th>
                  <th>Utility type</th>
                  <th>Custodian</th>
                  <th>HOD</th>
                  <th>Execution agency</th>
                  <th>Status</th>
                  <th>Last Update</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {utilityShiftingId.length > 0 ? (
                  utilityShiftingId.map((us, index) => (
                    <tr key={index}>
                      <td>{us.utility_shifting_id}</td>
                      <td>{us.utility_description}</td>
                      <td>{us.utility_type_fk}</td>
                      <td>{us.custodian}</td>
                      <td>{us.designation}</td>
                      <td>{us.execution_agency_fk}</td>
                      <td>{us.shifting_status_fk}</td>
                      <td>{us.modified_date}</td>
                      <td>
                        <button className="btn btn-sm btn-outline-primary" onClick={() => handleEdit(us)}>
                          Edit
                        </button>
                      </td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="9" style={{ textAlign: "center" }}>
                      No records found
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>

<br />
<br />
                  {/* Uploded Design Data table */}

           { !isUtilityShiftingForm &&(
            <div className="pageHeading">
              <h2>Uploded Utility Shifting Data</h2>
              <div  className="rightBtns">
                <button className="btn-2 transparent-btn hidden">
                    <LuDownload size={16} />
                  </button>
              </div>
            </div>
            )}

          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.uploadedDesignTable}>
              <thead>
                <tr>
                  <th>Uploaded File</th>
                  <th>Status</th>
                  <th>Remarks</th>
                  <th>Uploaded by</th>
                  <th>Uploaded On</th>
                </tr>
              </thead>
              <tbody>
                {filePath.length > 0 ? (
                  filePath.map((uf, index) => (
                    <tr key={index}>
                      <td>{uf.filePath}</td>
                      <td>{uf.status}</td>
                      <td>{uf.remarks}</td>
                      <td>{uf.uploaded_by_user_id_fk}</td>
                      <td>{uf.uploaded_on}</td>
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