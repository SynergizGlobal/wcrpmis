import { useEffect, useState } from "react";
import { Outlet } from 'react-router-dom';
import styles from './DashboardType.module.css';
import api from "../../../../../api/axiosInstance";
import { API_BASE_URL } from "../../../../../config";

import { MdDelete } from 'react-icons/md';

export default function DashboardType() {
    const [data, setData] = useState([]);
    const [showModal, setShowModal] = useState(false);
    const [search, setSearch] = useState("");
    const [value, setValue] = useState("");
    const [mode, setMode] = useState("add"); // add | edit
    const [editIndex, setEditIndex] = useState(null);
  
	const filteredData = data.filter(
	  item =>
	    typeof item === "string" &&
	    item.toLowerCase().includes(search.toLowerCase())
	);

    console.log("API URL:", process.env.REACT_APP_API_URL);

useEffect(() => {
  loadDashboardTypes();
}, []);

const loadDashboardTypes = async () => {
  try {
	const res = await api.get(
	      `${API_BASE_URL}/api/dashboard-type/list`
	    );
	setData(
	  res.data
	    .map(d => d.dashboard_type)
	    .filter(Boolean)
	);
  } catch (err) {
    console.error("LOAD ERROR:", err);
  }
};
  
    /* ===== ADD ===== */
    const handleAddClick = () => {
      setMode("add");
      setValue("");
      setEditIndex(null);
      setShowModal(true);
    };
  
    /* ===== EDIT ===== */
    const handleEditClick = (item, index) => {
      setMode("edit");
      setValue(item);
      setEditIndex(index);
      setShowModal(true);
    };
  
    /* ===== SAVE ===== */
	const handleSave = async () => {
	  try {
	    if (!value.trim()) {
	      alert("Dashboard Type is required");
	      return;
	    }

	    if (mode === "add") {
	      const payload = {
	        dashboard_type: value.trim()
	      };

	      await api.post(
	        `${API_BASE_URL}/api/dashboard-type/add`,
	        payload
	      );

	      alert("Dashboard Type saved successfully!");
	    }

	    await loadDashboardTypes();

	    setShowModal(false);
	    setValue("");
	    setEditIndex(null);

	  } catch (err) {
	    console.error("SAVE ERROR:", err);
	    alert("Error saving dashboard type");
	  }
	};

	const handleUpdate = async () => {
	  try {
	    if (!value.trim()) {
	      alert("Dashboard Type is required");
	      return;
	    }

	    const payload = {
	      old_dashboard_type: data[editIndex],
	      dashboard_type: value.trim()
	    };

	    await api.put(
	      `${API_BASE_URL}/api/dashboard-type/update`,
	      payload
	    );

	    alert("Dashboard Type updated successfully!");

	    await loadDashboardTypes();
	    setShowModal(false);
	    setValue("");
	    setEditIndex(null);

	  } catch (err) {
	    console.error("UPDATE ERROR:", err);
	    alert("Error updating dashboard type");
	  }
	};

	const handleDelete = async (item) => {
	  if (!window.confirm(`Delete "${item}" ?`)) return;

	  try {
	    await api.delete(
	      `${API_BASE_URL}/api/dashboard-type/delete`,
	      {
	        data: { dashboard_type: item }
	      }
	    );

	    alert("Dashboard Type deleted successfully!");
	    await loadDashboardTypes();

	  } catch (err) {
	    console.error("DELETE ERROR:", err);
	    alert("Error deleting dashboard type");
	  }
	};


   return (
      <div className={styles.container}>
        <div className="card">
          {/* HEADER */}
          <div className="formHeading">
            <h2 className="center-align ps-relative">
              Dashboard Type
            </h2>
          </div>
  
          {/* INNER PAGE */}
          <div className="innerPage">
            {/* ADD BUTTON */}
            <div className={styles.topActions}>
              <button
                className="btn btn-primary"
                onClick={handleAddClick}
              >
                + Add Dashboard Type
              </button>
            </div>
  
            {/* SEARCH */}
            <div className={styles.searchBox}>
              <input
                type="text"
                placeholder="Search"
                value={search}
                onChange={(e) => setSearch(e.target.value)}
              />
              {search && (
                <span
                  className={styles.clear}
                  onClick={() => setSearch("")}
                >
                  ✕
                </span>
              )}
            </div>
  
            {/* TABLE */}
            <div className={`dataTable ${styles.tableWrapper}`}>
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Dashboard Type</th>
                    <th className={styles.actionCol}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.map((item, index) => (
                    <tr key={index}>
                      <td>{item}</td>
                      <td className={styles.actionCol}>
                        <button
                          className={styles.editBtn}
                          onClick={() => handleEditClick(item, index)}
                        >
                          ✎
                        </button>
						<button
						 className={styles.deleteBtn}
						  onClick={() => handleDelete(item, index)}
						>
						<MdDelete size={14} />
						</button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
  
            <div className={styles.footerText}>
              Showing 1 to {filteredData.length} of {filteredData.length} entries
            </div>
          </div>
        </div>
  
        {/* MODAL */}
        {showModal && (
          <div className={styles.modalOverlay}>
            <div className={styles.modal}>
              <div className={styles.modalHeader}>
                <span>
                  {mode === "add"
                    ? "Add Dashboard Type"
                    : "Edit Dashboard Type"}
                </span>
                <span
                  className={styles.close}
                  onClick={() => setShowModal(false)}
                >
                  ✕
                </span>
              </div>
  
              <div className={styles.modalBody}>
                <input
                  type="text"
                  placeholder="Dashboard Type"
                  value={value}
                  onChange={(e) => setValue(e.target.value)}
                />
  
                <div className={styles.modalActions}>
                  <button
                    className="btn btn-primary"
                    onClick={mode === "add" ? handleSave : handleUpdate}
                  >
                    {mode === "add" ? "ADD" : "UPDATE"}
                  </button>
                  <button
                    className="btn btn-white"
                    onClick={() => setShowModal(false)}
                  >
                    CANCEL
                  </button>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    );
  }