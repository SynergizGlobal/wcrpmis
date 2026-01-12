import React, { useState, useEffect } from "react";
import styles from './YesOrNoStatus.module.css';
import { FaTrash } from "react-icons/fa";
import api from "../../../../../api/axiosInstance";
import { API_BASE_URL } from "../../../../../config";

export default function YesOrNoStatus() {

  const [data, setData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [yesOrNo, setYesOrNo] = useState("");
  const [mode, setMode] = useState("add"); // add | edit
  const [editIndex, setEditIndex] = useState(null);
  const [rows, setRows] = useState([]);
  const [columns, setColumns] = useState([]);
  const [counts, setCounts] = useState([])
  const [oldValue, setOldValue] = useState("");


  /* ================= LOAD ================= */
  useEffect(() => {
    loadYesOrNo();
  }, []);

  const loadYesOrNo = async () => {
    try {
      const res = await fetch(`${API_BASE_URL}/yes-or-no-status`);
      const data = await res.json();

      if (data.status === "success") {
        const details = data.yesOrNoStatusDetails;
        setRows(details.dList1 || []);
        setColumns(details.tablesList || []);
        setCounts(details.countList || []);
      }

    } catch (err) {
      console.error("Failed to load Yes / No", err);
    }
  };


  const getCount = (yesorno, tableName) => {
    const match = counts.find(
      c => c.yesorno === yesorno && c.tName === tableName
    );
    return match ? match.count : 0;
  };


  /* ================= SEARCH ================= */
  const filteredData = data.filter(item =>
    item.yesorno?.toLowerCase().includes(search.toLowerCase())
  );

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setMode("add");
    setYesOrNo("");
    setEditIndex(null);
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (row, index) => {
    setMode("edit");
    setYesOrNo(row.yesorno);
    setOldValue(row.yesorno);   // ✅ REAL OLD VALUE
    setEditIndex(index);
    setShowModal(true);
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (!yesOrNo.trim()) return;

    try {
      const params = new URLSearchParams();

      if (mode === "add") {
        params.append("yesorno", yesOrNo.trim());

        await fetch(`${API_BASE_URL}/add-yes-or-no-status`, {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          body: params.toString()
        });

		} else if (mode === "edit") {
		  params.append("value_old", oldValue);
		  params.append("value_new", yesOrNo.trim());

		  await fetch(`${API_BASE_URL}/update-yes-or-no-status`, {
		    method: "POST",
		    headers: {
		      "Content-Type": "application/x-www-form-urlencoded"
		    },
		    body: params.toString()
		  });
		}

      setShowModal(false);
      setYesOrNo("");
      setEditIndex(null);
      loadYesOrNo();

    } catch (err) {
      console.error("Save failed", err);
    }
  };


  /* ================= DELETE ================= */
  const handleDelete = async (row) => {
    if (
      columns.some(col => getCount(row.yesorno, col.tName) > 0)
    ) {
      alert("Cannot delete. This value is in use.");
      return;
    }

    const params = new URLSearchParams();
    params.append("yesorno", row.yesorno);

    await fetch(`${API_BASE_URL}/delete-yes-or-no-status`, {
      method: "POST",
      headers: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      body: params.toString()
    });

    loadYesOrNo();
  };

  return (
    <div className={styles.container}>
      <div className="card">

        <div className="formHeading">
          <h2 className="center-align">Yes Or No</h2>
        </div>

        <div className="innerPage">

          <div className={styles.topActions}>
            <button className="btn btn-primary" onClick={handleAddClick}>
              + Add Yes Or No
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
            />
          </div>

          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.table}>
			<thead>
			  <tr>
			    <th>Yes / No</th>

			    {columns.map((col, index) => (
			      <th key={index}>
			        {col.tName.replaceAll("_", " ")}
			      </th>
			    ))}

			    <th className={styles.actionCol}>Action</th>
			  </tr>
			</thead>
			<tbody>
			  {rows.map((row, rowIndex) => (
			    <tr key={rowIndex}>
			      <td>{row.yesorno}</td>

			      {columns.map((col, colIndex) => (
			        <td key={colIndex}>
			          {getCount(row.yesorno, col.tName)}
			        </td>
			      ))}

			      <td className={styles.actionCol}>
			        <button
			          className={styles.editBtn}
			          onClick={() => handleEditClick(row, rowIndex)}
			        >
			          ✎
			        </button>

			        <button
			          className={styles.deleteBtn}
			          disabled={
			            columns.some(
			              col => getCount(row.yesorno, col.tName) > 0
			            )
			          }
					  onClick={() => handleDelete(row)}
			        >
			          <FaTrash />
			        </button>
			      </td>
			    </tr>
			  ))}
			</tbody>
            </table>
          </div>

          <div className={styles.footerText}>
            Showing {filteredData.length} of {filteredData.length} entries
          </div>

        </div>
      </div>

      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>{mode === "add" ? "Add Yes Or No" : "Edit Yes Or No"}</span>
              <span className={styles.close} onClick={() => setShowModal(false)}>✕</span>
            </div>

            <div className={styles.modalBody}>
              <input
                type="text"
                placeholder="Yes Or No"
                value={yesOrNo}
                onChange={(e) => setYesOrNo(e.target.value)}
              />

              <div className={styles.modalActions}>
                <button className="btn btn-primary" onClick={handleSave}>
                  {mode === "add" ? "ADD" : "UPDATE"}
                </button>
                <button className="btn btn-white" onClick={() => setShowModal(false)}>
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