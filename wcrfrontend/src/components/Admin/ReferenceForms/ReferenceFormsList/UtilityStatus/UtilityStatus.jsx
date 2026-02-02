import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes } from "react-icons/fa";
import "react-toastify/dist/ReactToastify.css";
import styles from "./UtilityStatus.module.css";
import { API_BASE_URL } from "../../../../../config";

const API_ENDPOINTS = {
  GET: "/utility-statuss",
  ADD: "/add-utility-status",
  UPDATE: "/update-utility-status",
  DELETE: "/delete-utility-status"
};

export default function UtilityStatus() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add");
  const [editId, setEditId] = useState(null);
  const [editIndex, setEditIndex] = useState(null);
  const [saving, setSaving] = useState(false);

  /* ================= FETCH ================= */
  const fetchData = async () => {
    try {
      setLoading(true);

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.GET}`, {
        method: "GET"
      });

      const json = await response.json();

      const list =
        json?.utilityStatusList?.dlist1?.map((item, index) => ({
          id: index,
          utility_status: item.status
        })) || [];

      setData(list.filter(i => i.utility_status));

    } catch (error) {
      console.error(error);
      setData([]);
    } finally {
      setLoading(false);
    }
  };


  useEffect(() => {
    fetchData();
  }, []);

  /* ================= FILTER ================= */
  const filteredData = data.filter((item) =>
    item.utility_status
      ?.toLowerCase()
      .includes(search.toLowerCase())
  );

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setMode("add");
    setValue("");
    setEditId(null);
    setEditIndex(null);
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item, index) => {
    setMode("edit");
    setValue(item.utility_status);
    setEditId(item.id);
    setEditIndex(index);
    setShowModal(true);
  };

  /* ================= DELETE ================= */
  const handleDeleteClick = async (id, index) => {
    if (!window.confirm("Are you sure you want to delete this Utility Status?")) {
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.DELETE}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
	
		body: JSON.stringify({
		  status: data[index].utility_status
		})
      });

      const result = await response.json();

	  if (result.status !== "success") {
	    throw new Error(result.message || "Delete failed");
	  }

      setData((prev) => prev.filter((_, i) => i !== index));
    } catch (error) {
      console.error(error);
      alert("Failed to delete utility status");
    }
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!value.trim()) {
      alert("Please enter a utility status");
      return;
    }

    try {
      setSaving(true);
      let response;

      if (mode === "add") {
        // ADD
        response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.ADD}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            utility_status: value.trim()
          })
        });
      } else {
        // UPDATE (POST)
        response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.UPDATE}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            value_old: data[editIndex].utility_status,
            value_new: value.trim()
          })
        });
      }

      const result = await response.json();

	  if (result.status !== "success") {
	    throw new Error(result.message || "Operation failed");
	  }

      if (mode === "add") {
        setData((prev) => [
          {
            id: result.id ?? Date.now(),
            utility_status: value.trim()
          },
          ...prev
        ]);
      } else {
        setData((prev) =>
          prev.map((item, index) =>
            index === editIndex
              ? { ...item, utility_status: value.trim() }
              : item
          )
        );
      }

      setShowModal(false);
      setValue("");
      setEditId(null);
      setEditIndex(null);
    } catch (error) {
      console.error(error);
      alert(`Failed to ${mode === "add" ? "add" : "update"} utility status`);
    } finally {
      setSaving(false);
    }
  };

  /* ================= UI ================= */
  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Utility Status</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button
              className="btn btn-primary"
              onClick={handleAddClick}
              disabled={loading}
            >
              + Add Utility Status
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search utility status..."
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              disabled={loading}
            />
            {search && (
              <span
                className={styles.clear}
                onClick={() => setSearch("")}
              >
                <FaTimes />
              </span>
            )}
          </div>

          <div className={`dataTable ${styles.tableWrapper}`}>
            {loading ? (
              <div className={styles.loading}>Loading utility status...</div>
            ) : (
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Utility Status</th>
                    <th className={styles.actionCol}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.length ? (
                    filteredData.map((item, index) => (
                      <tr key={item.id ?? index}>
                        <td>{item.utility_status}</td>
                        <td className={styles.actionCol}>
                          <div className={styles.actionButtons}>
                            <button
                              className={styles.editBtn}
                              onClick={() => handleEditClick(item, index)}
                              title="Edit"
                            >
                              <FaEdit />
                            </button>
                            <button
                              className={styles.deleteBtn}
                              onClick={() => handleDeleteClick(item.id, index)}
                              title="Delete"
                            >
                              <FaTrash />
                            </button>
                          </div>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan="2" className={styles.noData}>
                        {search ? "No matching results found" : "No utility status available"}
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            )}
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
                  ? "Add Utility Status"
                  : "Edit Utility Status"}
              </span>
              <FaTimes
                className={styles.close}
                onClick={() => !saving && setShowModal(false)}
                title="Close"
              />
            </div>

            <div className={styles.modalBody}>
              <input
                type="text"
                placeholder="Enter utility status name"
                value={value}
                onChange={(e) => setValue(e.target.value)}
                autoFocus
                disabled={saving}
                onKeyDown={(e) => e.key === 'Enter' && handleSave()}
              />

              <div className={styles.modalActions}>
                <button 
                  className="btn btn-primary" 
                  onClick={handleSave}
                  disabled={saving || !value.trim()}
                >
                  {saving ? "Saving..." : mode === "add" ? "ADD" : "UPDATE"}
                </button>
                <button
                  className="btn btn-white"
                  onClick={() => setShowModal(false)}
                  disabled={saving}
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