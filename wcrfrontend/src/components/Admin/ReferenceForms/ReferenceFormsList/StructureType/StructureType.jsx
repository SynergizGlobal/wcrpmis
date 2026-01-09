import React, { useState, useEffect} from "react";
import { Outlet } from 'react-router-dom';
import { FaTrash } from "react-icons/fa";
import styles from './StructureType.module.css';
import api from "../../../../../api/axiosInstance";
import { API_BASE_URL } from "../../../../../config";


export default function StructureType() {
  const [data, setData] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add"); // add | edit
  const [editIndex, setEditIndex] = useState(null);
  const [rows, setRows] = useState([]);
  const [tables, setTables] = useState([]);
  const [oldValue, setOldValue] = useState("");


  const filteredData = rows.filter(row =>
    row.structure_type
      .toLowerCase()
      .includes(search.toLowerCase())
  );

  /* ===== ADD ===== */
  const handleAddClick = () => {
    setMode("add");
    setValue("");
    setEditIndex(null);
    setShowModal(true);
  };

  /* ===== EDIT ===== */
  const handleEditClick = (item) => {
    setMode("edit");
    setValue(item);
    setOldValue(item);   // ✅ store real old value
    setShowModal(true);
  };

  
  useEffect(() => {
    loadStructureTypes();
  }, []);

  const loadStructureTypes = async () => {
    try {
      const res = await api.get(`${API_BASE_URL}/structure-type`);

      const details = res.data.details;
      const masterList = details.dList1 || [];
      const countList = details.countList || [];
      const tablesList = details.tablesList || [];

      /* Build row-wise data */
      const mappedRows = masterList.map(st => {
        const counts = {};

        tablesList.forEach(t => {
          counts[t.tName] = 0;
        });

        countList.forEach(c => {
          if (c.structure_type === st.structure_type) {
            counts[c.tName] = c.count;
          }
        });

        return {
          structure_type: st.structure_type,
          counts
        };
      });

      setRows(mappedRows);
      setTables(tablesList);

    } catch (err) {
      console.error("Error loading structure types", err);
    }
  };



  /* ===== SAVE ===== */
  const handleSave = async () => {
    if (!value.trim()) return;
	console.log("HANDLE SAVE CALLED, mode =", mode);

    try {
      const params = new URLSearchParams();

      if (mode === "add") {
        params.append("structure_type", value.trim());

        await fetch(`${API_BASE_URL}/add-structure-type`, {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          body: params.toString()
        });

      } else if (mode === "edit") {
		params.append("value_old", oldValue);
		params.append("value_new", value.trim());

        await fetch(`${API_BASE_URL}/update-structure-type`, {
          method: "POST",
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          },
          body: params.toString()
        });
      }

      await loadStructureTypes();
      setShowModal(false);
      setValue("");
      setEditIndex(null);

    } catch (err) {
      console.error("Save failed", err);
    }
  };

  const handleDelete = async (item) => {
    if (!window.confirm(`Delete "${item}" ?`)) return;

    try {
      const params = new URLSearchParams();
      params.append("structure_type", item);

      await fetch(`${API_BASE_URL}/delete-structure-type`, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: params.toString()
      });

      await loadStructureTypes();

    } catch (err) {
      console.error("Delete failed", err);
    }
  };


  return (
    <div className={styles.container}>
      <div className="card">
        {/* HEADER */}
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            Structure Type
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
              + Add Structure Type
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
			    <th>Structure Type</th>

			    {tables.map(t => (
			      <th key={t.tName}>{t.captiliszedTableName}</th>
			    ))}

			    <th className={styles.actionCol}>Action</th>
			  </tr>
			</thead>
			<tbody>
			  {filteredData.map((row, index) => (
			    <tr key={index}>
			      <td>{row.structure_type}</td>

			      {tables.map(t => (
			        <td key={t.tName} className={styles.countCol}>
			          {row.counts[t.tName] || ""}
			        </td>
			      ))}

			      <td className={styles.actionCol}>
			        <button
			          className={styles.editBtn}
			          onClick={() => handleEditClick(row.structure_type, index)}
			        >
			          ✎
			        </button>

			        <button
			          className={styles.deleteBtn}
			          onClick={() => handleDelete(row.structure_type)}
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
                  ? "Add Structure Type"
                  : "Edit Structure Type"}
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
                placeholder="Structure Type"
                value={value}
                onChange={(e) => setValue(e.target.value)}
              />

              <div className={styles.modalActions}>
                <button
				  type="button"
                  className="btn btn-primary"
                  onClick={handleSave}
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
