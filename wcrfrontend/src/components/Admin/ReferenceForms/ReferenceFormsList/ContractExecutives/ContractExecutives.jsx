import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaPlus, FaTimes } from "react-icons/fa";
import styles from "./ContractExecutives.module.css";

export default function ContractExecutives() {
  const [rows, setRows] = useState([]);
  const [projects, setProjects] = useState([]);
  const [users, setUsers] = useState([]);
  const [search, setSearch] = useState("");

  const [showModal, setShowModal] = useState(false);
  const [mode, setMode] = useState("add");

  const [selectedProject, setSelectedProject] = useState("");
  const [executiveRows, setExecutiveRows] = useState([""]);
  const [editRow, setEditRow] = useState(null);

  /* ================= PAGE LOAD ================= */
  const fetchContractExecutives = async () => {
    try {
      const res = await fetch(`${API_BASE_URL}/contract-executives`);
      const json = await res.json();

      if (!json.success) {
        alert("Failed to load data");
        return;
      }

      setRows(json.executivesDetails || []);
      setProjects(json.projectDetails || []);
    } catch (e) {
      console.error("Failed to load page data", e);
      alert("Server error while loading data");
    }
  };

  useEffect(() => {
    fetchContractExecutives();
  }, []);

  /* ================= SEARCH ================= */
  const filteredData = rows.filter(r => {
    const term = search.toLowerCase();
    return (
      (r.project_name || "").toLowerCase().includes(term) ||
      (r.user_name || "").toLowerCase().includes(term)
    );
  });

  /* ================= LOAD USERS ================= */
  const loadProjectUsers = async projectId => {
    if (!projectId) {
      setUsers([]);
      return;
    }
	
	const formData = new FormData();
	
	formData.append("project_id_fk", projectId)

    try {
      const res = await fetch(
        `${API_BASE_URL}/ajax/getProjectWiseContractResponsibleUsers`,
        {
          method: "POST",
          credentials: "include",
          body: formData
        }
      );

      const data = await res.json();
      setUsers(Array.isArray(data) ? data : []);
    } catch (err) {
      console.error("Failed to load users", err);
      setUsers([]);
    }
  };

  /* ================= ROW HANDLERS ================= */
  const addExecutiveRow = () =>
    setExecutiveRows([...executiveRows, ""]);

  const removeExecutiveRow = index =>
    setExecutiveRows(executiveRows.filter((_, i) => i !== index));

  const updateExecutiveRow = (index, value) => {
    const updated = [...executiveRows];
    updated[index] = value;
    setExecutiveRows(updated);
  };

  /* ================= ADD ================= */
  const handleAdd = () => {
    setMode("add");
    setSelectedProject("");
    setExecutiveRows([""]);
    setUsers([]);
    setEditRow(null);
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEdit = row => {
    setMode("edit");
    setSelectedProject(row.project_id_fk);
    setExecutiveRows(
      row.user_id ? row.user_id.split(",").map(v => v.trim()) : [""]
    );
    setEditRow(row);
    setShowModal(true);
    loadProjectUsers(row.project_id_fk);
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    const cleanedExecutives = [
      ...new Set(executiveRows.filter(Boolean))
    ];

    if (!selectedProject || cleanedExecutives.length === 0) {
      alert("Please select Project and Executives");
      return;
    }

    const formData = new FormData();
    formData.append("project_id_fks", selectedProject);
    formData.append(
      "executive_user_id_fks",
      cleanedExecutives.join(",")
    );

    if (mode === "edit") {
      formData.append("project_id_fk_old", editRow.project_id_fk);
    }

    const url =
      mode === "add"
        ? "/add-contract-executives"
        : "/update-contract-executives";

    try {
      await fetch(`${API_BASE_URL}${url}`, {
        method: "POST",
        body: formData
      });

      alert(
        mode === "add"
          ? "Executives Added Successfully"
          : "Executives Updated Successfully"
      );

      setShowModal(false);
      fetchContractExecutives();
    } catch (err) {
      console.error("Save failed", err);
      alert("Server error. Try again.");
    }
  };

  /* ================= JSX ================= */
  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Contract Executives</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAdd}>
              + Add Executives
            </button>
          </div>

          {/* SEARCH */}
          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search"
              value={search}
              onChange={e => setSearch(e.target.value)}
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
                <th>Project</th>
                <th>Executives</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {filteredData.map((r, i) => (
                <tr key={i}>
                  <td>{r.project_name}</td>
                  <td>
                    {r.user_name
                      ? r.user_name.split(",").map((n, idx) => (
                          <div key={idx}>▶ {n.trim()}</div>
                        ))
                      : "-"}
                  </td>
                  <td>
                    <button
                      className={styles.editBtn}
                      onClick={() => handleEdit(r)}
                    >
                      <FaEdit />
                    </button>
                  </td>
                </tr>
              ))}

              {filteredData.length === 0 && (
                <tr>
                  <td colSpan={3} className="center-align">
                    No records found
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>

      {/* MODAL */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>
                {mode === "add" ? "Add Executives" : "Update Executives"}
              </span>
              <span
                className={styles.close}
                onClick={() => setShowModal(false)}
              >
                ✕
              </span>
            </div>

            <div className={styles.modalBody}>
              <select
                className={styles.select}
                value={selectedProject}
                onChange={e => {
                  setSelectedProject(e.target.value);
                  setExecutiveRows([""]);
                  loadProjectUsers(e.target.value);
                }}
              >
                <option value="">Select Project</option>
                {projects.map(p => (
                  <option key={p.project_id_fk} value={p.project_id_fk}>
                    {p.project_id_fk} - {p.project_name}
                  </option>
                ))}
              </select>

              {executiveRows.map((v, i) => (
                <div key={i} className={styles.contractRow}>
                  <select
                    className={styles.select}
                    value={v}
					disabled={selectedProject.length === 0}
                    onChange={e =>
                      updateExecutiveRow(i, e.target.value)
                    }
                  >
                    <option value="">Select Executive</option>
                    {users.map(u => (
                      <option
                        key={u.user_id}
                        value={u.user_id}
                        disabled={
                          executiveRows.includes(u.user_id) &&
                          u.user_id !== v
                        }
                      >
                        {u.designation} - {u.user_name}
                      </option>
                    ))}
                  </select>

                  {executiveRows.length > 1 && (
                    <button
                      className={styles.removeBtn}
                      onClick={() => removeExecutiveRow(i)}
                    >
                      <FaTimes />
                    </button>
                  )}
                </div>
              ))}

              <button className="btn btn-primary" onClick={addExecutiveRow}>
                <FaPlus /> Add
              </button>

              <div className={styles.modalActions}>
                <button className="btn btn-primary" onClick={handleSave}>
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
	</div>
  );
}
