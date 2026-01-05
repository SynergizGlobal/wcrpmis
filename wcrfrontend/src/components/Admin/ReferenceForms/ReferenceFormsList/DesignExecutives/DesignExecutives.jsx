import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit } from "react-icons/fa";
import styles from "./DesignExecutives.module.css";

export default function DesignExecutives() {
  const [rows, setRows] = useState([]);
  const [users, setUsers] = useState([]);
  const [works, setWorks] = useState([]);

  const [showModal, setShowModal] = useState(false);
  const [mode, setMode] = useState("add");

  const [selectedWork, setSelectedWork] = useState("");
  const [selectedUsers, setSelectedUsers] = useState([]);
  const [editRow, setEditRow] = useState(null);

  /* ================= FETCH DATA ================= */
  const fetchDesignExecutives = async () => {
    const res = await fetch(`${API_BASE_URL}/design-executives`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({})
    });

    const json = await res.json();

    setRows(json.executivesDetails || []);
    setUsers(json.usersDetails || []);
  };

  useEffect(() => {
    fetchDesignExecutives();
  }, []);

  /* ================= ADD ================= */
  const handleAdd = () => {
    setMode("add");
    setSelectedWork("");
    setSelectedUsers([]);
    setEditRow(null);
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEdit = row => {
    setMode("edit");
    setSelectedWork(row.work_id_fk);
    setSelectedUsers(row.user_id.split(","));
    setEditRow(row);
    setShowModal(true);
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!selectedWork || selectedUsers.length === 0) {
      alert("Select Work and Executives");
      return;
    }

    const formData = new FormData();
    formData.append("work_id_fks", selectedWork);
    formData.append("executive_user_id_fks", selectedUsers.join(","));

    const url =
      mode === "add"
        ? "/add-design-executives"
        : "/update-design-executives";

    await fetch(`${API_BASE_URL}${url}`, {
      method: "POST",
      body: formData
    });

    setShowModal(false);
    fetchDesignExecutives();
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Design Executives</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAdd}>
              + Add Executives
            </button>
          </div>

          {/* TABLE */}
          <table className={styles.table}>
            <thead>
              <tr>
                <th>Work</th>
                <th>Executives</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>
              {rows.map((r, i) => (
                <tr key={i}>
                  <td>{r.work_short_name}</td>
                  <td>{r.user_name}</td>
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
            </tbody>
          </table>
        </div>
      </div>

      {/* MODAL */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <h4>{mode === "add" ? "Add Executives" : "Update Executives"}</h4>

            {/* WORK */}
            <select
              value={selectedWork}
              onChange={e => setSelectedWork(e.target.value)}
            >
              <option value="">Select Work</option>
              {rows.map(r => (
                <option key={r.work_id_fk} value={r.work_id_fk}>
                  {r.work_short_name}
                </option>
              ))}
            </select>

            {/* USERS */}
            <select
              multiple
              value={selectedUsers}
              onChange={e =>
                setSelectedUsers(
                  Array.from(e.target.selectedOptions, o => o.value)
                )
              }
            >
              {users.map(u => (
                <option key={u.user_id} value={u.user_id}>
                  {u.designation} - {u.user_name}
                </option>
              ))}
            </select>

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
      )}
    </div>
  );
}
