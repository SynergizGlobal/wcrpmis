

import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes } from "react-icons/fa";
import "react-toastify/dist/ReactToastify.css";
import styles from "./UtilityRequirementStage.module.css";
import { API_BASE_URL } from "../../../../../config";

const API_ENDPOINTS = {
  GET: "/utility-requirement-stage",
  ADD: "/add-utility-requirement-stage",
  UPDATE: "/update-utility-requirement-stage",
  DELETE: "/delete-utility-requirement-stage"
};

export default function UtilityRequirementStage() {
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

      const response = await fetch(
        `${API_BASE_URL}${API_ENDPOINTS.GET}`,
        { method: "GET" }
      );

      const json = await response.json();

      if (!json.success) {
        throw new Error("Failed to fetch Utility Requirement Stage list");
      }

      // ✅ CORRECT JSON PATH
      const list =
        json?.utilityRequirementStageList?.dlist1?.map(item => ({
          utility_requirement_stage: item.requirement_stage
        })) || [];

      setData(list);

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
    item.utility_requirement_stage
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
    setValue(item.utility_requirement_stage);
    setEditId(item.id);
    setEditIndex(index);
    setShowModal(true);
  };

  /* ================= DELETE ================= */
  const handleDeleteClick = async (id, index) => {
    if (!window.confirm("Are you sure you want to delete this Utility Requirement Stage?")) {
      return;
    }

    try {
		const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.DELETE}`, {
		  method: "DELETE",
		  headers: { "Content-Type": "application/json" },
		  body: JSON.stringify({
		    requirement_stage: data[index].utility_requirement_stage
		  })
		});

		const result = await response.json();

		if (result.status !== "success") {
		  throw new Error(result.message);
		}

		setData(prev => prev.filter((_, i) => i !== index));

    } catch (error) {
      console.error(error);
      alert("Failed to delete utility requirement stage");
    }
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!value.trim()) {
      alert("Please enter a utility requirement stage");
      return;
    }

    try {
      setSaving(true);
      let response;

      if (mode === "add") {
        // ✅ ADD
        response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.ADD}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
		  body: JSON.stringify({
		    requirement_stage: value.trim()
		  })
        });
      } else {
        // ✅ UPDATE
        response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.UPDATE}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
		   body: JSON.stringify({
		     value_old: data[editIndex].utility_requirement_stage,
		     value_new: value.trim()
		   })
        });
      }

      const result = await response.json();

	  if (result.status !== "success") {
	    throw new Error(result.message || "Operation failed");
	  }

	  /* ✅ SUCCESS FLOW */
	  if (mode === "add") {
	    setData(prev => [
	      { utility_requirement_stage: value.trim() },
	      ...prev
	    ]);
	  } else {
	    setData(prev =>
	      prev.map((item, i) =>
	        i === editIndex
	          ? { ...item, utility_requirement_stage: value.trim() }
	          : item
	      )
	    );
	  }

	  setShowModal(false);
	  setValue("");
	  setEditIndex(null);
	  setEditId(null);


    } catch (error) {
      console.error(error);
      alert("Something went wrong");
    } finally {
      setSaving(false);
    }
  };

  /* ================= UI ================= */
  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Utility Requirement Stage</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button
              className="btn btn-primary"
              onClick={handleAddClick}
              disabled={loading}
            >
              + Add Utility Requirement Stage
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search utility requirement stages..."
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
              <div className={styles.loading}>Loading utility requirement stages...</div>
            ) : (
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Utility Requirement Stage</th>
                    <th className={styles.actionCol}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.length ? (
                    filteredData.map((item, index) => (
                      <tr key={item.id ?? index}>
                        <td>{item.utility_requirement_stage}</td>
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
                        {search ? "No matching results found" : "No utility requirement stages available"}
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
                  ? "Add Utility Requirement Stage"
                  : "Edit Utility Requirement Stage"}
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
                placeholder="Enter utility requirement stage name"
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