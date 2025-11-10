import React, { useState, useEffect } from "react";
import Select from "react-select";
import styles from './Contract.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload, LuUpload, LuDownload } from "react-icons/lu";
import axios from "axios";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";

export default function Contract() {

  const location = useLocation();
      const navigate = useNavigate();
      const [contract, setContract] = useState([]);
      const [contractList, setcontractList] = useState([]);
      const [filters, setFilters] = useState({
        hod: "",
        dyHod: "",
        contractor: "",
        contractStatus: "",
        statusOfWork: "",
      });
  
    useEffect(() => {
      fetchContract();
    }, []);

    const fetchContract = async () => {
      try {
        const res = await axios.get(`${API_BASE_URL}/contract`, { withCredentials: true });
        setContract(res.data || []);
      } catch (err) {
        console.error("Error fetching projects:", err);
      }
    };

    const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  };

  const handleAdd = () => navigate("add-contract-form");
  const handleEdit = (project) => navigate("get-contract", { state: { project } });

  const isDesignDrawingForm = location.pathname.endsWith("/add-contract-form");

  return (
    <div className={styles.container}>
      { !isDesignDrawingForm &&(
      <div className="pageHeading">
        <h2>Update Design & Drawing</h2>
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
      {!isDesignDrawingForm && (
        
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
                  <th>PMIS Drawing No.</th>
                  <th>Structure Type</th>
                  <th>Structure</th>
                  <th>Title</th>
                  <th>Drawing Type</th>
                  <th>Drawing No</th>
                  <th>Last Update</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {contractList.length > 0 ? (
                  contractList.map((dd, index) => (
                    <tr key={index}>
                      <td>{dd.design_seq_id}</td>
                      <td>{dd.structure_type_fk}</td>
                      <td>{dd.structure_id_fk}</td>
                      <td>{dd.drawing_title}</td>
                      <td>{dd.drawing_type_fk}</td>
                      <td>{dd.mrvc_drawing_no}</td>
                      <td>{dd.modified_date}</td>
                      <td>
                        <button className="btn btn-sm btn-outline-primary" onClick={() => handleEdit(dd)}>
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