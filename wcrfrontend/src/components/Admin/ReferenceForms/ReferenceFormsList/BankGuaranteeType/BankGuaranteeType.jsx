import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle } from "react-icons/fa";
import "react-toastify/dist/ReactToastify.css";
import styles from "./BankGuaranteeType.module.css";
import { API_BASE_URL } from "../../../../../config";
import Swal from "sweetalert2";

const API_ENDPOINTS = {
  GET: "/bank-guarantee-type",
  ADD: "/add-bank-guarantee-type",
  UPDATE: "/update-bank-guarantee-type",
  DELETE: "/delete-bank-guarantee-type"
};

export default function BankGuaranteeType() {
  const [data, setData] = useState([]);
  const [tablesList, setTablesList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [mode, setMode] = useState("add");
  const [editId, setEditId] = useState(null);
  const [editIndex, setEditIndex] = useState(null);
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState({});
  const [editOldValue, setEditOldValue] = useState("");

  /* ================= FETCH DATA ================= */
  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.GET}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({})
      });

      const json = await response.json();

      const bankGaurenteeList = json?.bankGuaranteeDetails?.bankGaurenteeList1?.map((item) => ({
        id: item.id || Date.now() + Math.random(),
        bg_type: item.bg_type,
        counts: json?.bankGuaranteeDetails?.countList?.filter(count => count.bg_type_fk === item.bg_type) || []
      })) || [];

      const tables = json?.bankGuaranteeDetails?.tablesList?.map(item => ({
        tName: item.tName,
        captiliszedTableName: item.captiliszedTableName
      })) || [];

      setData(bankGaurenteeList.filter((i) => i.bg_type));
      setTablesList(tables);
    } catch (error) {
      console.error("Fetch error:", error);
      setData([]);
      setTablesList([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* ================= VALIDATION ================= */
  const validateInput = (inputValue) => {
    const errors = {};
    const trimmedValue = inputValue.trim();
    
    if (!trimmedValue) {
      errors.bg_type = "Bank Guarantee Type is required";
      return errors;
    }
    
    if (mode === "add") {
      const exists = data.some(item => 
        item.bg_type.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        errors.bg_type = `${trimmedValue} already exists`;
      }
    } else if (mode === "edit") {
      const exists = data.some((item, index) => 
        index !== editIndex && 
        item.bg_type.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        errors.bg_type = `${trimmedValue} already exists`;
      }
    }
    
    return errors;
  };

  /* ================= HANDLE INPUT CHANGE ================= */
  const handleInputChange = (e) => {
    const newValue = e.target.value;
    setValue(newValue);
    
    if (errors.bg_type) {
      setErrors({});
    }
  };

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setMode("add");
    setValue("");
    setEditId(null);
    setEditIndex(null);
    setEditOldValue("");
    setErrors({});
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item, index) => {
    if (item.counts && item.counts.length > 0) {
      setShowErrorModal(true);
      return;
    }

    setMode("edit");
    setValue(item.bg_type);
    setEditId(item.id);
    setEditIndex(index);
    setEditOldValue(item.bg_type);
    setErrors({});
    setShowModal(true);
  };

  /* ================= DELETE ================= */
  const handleDeleteClick = async (index) => {
    const item = data[index];
    
    if (item.counts && item.counts.length > 0) {
      setShowErrorModal(true);
      return;
    }

    const result = await Swal.fire({
      title: 'Are you sure?',
      text: "You will be changing the status of the record!",
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#DD6B55',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'No, cancel it!'
    });

    if (!result.isConfirmed) {
      Swal.fire('Cancelled', 'Record is safe :)', 'error');
      return;
    }

    try {
      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.DELETE}`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          bg_type: item.bg_type
        })
      });

      const result = await response.json();

      if (!result.success) {
        throw new Error(result.message);
      }

      setData((prev) => prev.filter((_, i) => i !== index));
      Swal.fire('Deleted!', 'Record has been deleted', 'success');
    } catch (error) {
      console.error(error);
      Swal.fire('Error', 'Failed to delete bank guarantee type', 'error');
    }
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    const validationErrors = validateInput(value);
    
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      setSaving(true);
      let response;
      const trimmedValue = value.trim();

      if (mode === "add") {
        response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.ADD}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            bg_type: trimmedValue
          })
        });
      } else {
        response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.UPDATE}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            bg_type_old: editOldValue,
            bg_type_new: trimmedValue
          })
        });
      }

      const result = await response.json();

      if (!result.success) {
        throw new Error(result.message);
      }

      if (mode === "add") {
        setData((prev) => [
          {
            id: result.id || Date.now() + Math.random(),
            bg_type: trimmedValue,
            counts: []
          },
          ...prev
        ]);
      } else {
        setData((prev) =>
          prev.map((item, index) =>
            index === editIndex
              ? { ...item, bg_type: trimmedValue }
              : item
          )
        );
      }

      setShowModal(false);
      setValue("");
      setEditId(null);
      setEditIndex(null);
      setEditOldValue("");
      setErrors({});
      
      Swal.fire(
        'Success!',
        `Bank Guarantee Type ${mode === 'add' ? 'added' : 'updated'} successfully`,
        'success'
      );
    } catch (error) {
      console.error(error);
      Swal.fire(
        'Error',
        `Failed to ${mode === "add" ? "add" : "update"} bank guarantee type`,
        'error'
      );
    } finally {
      setSaving(false);
    }
  };

  /* ================= FILTER DATA ================= */
  const filteredData = data.filter((item) =>
    item.bg_type?.toLowerCase().includes(search.toLowerCase())
  );

  /* ================= GET COUNT FOR TABLE ================= */
  const getCountForTable = (item, tableName) => {
    if (!item.counts || !Array.isArray(item.counts)) return null;
    const countObj = item.counts.find(count => count.tName === tableName);
    return countObj ? `(${countObj.count})` : null;
  };

  /* ================= MODAL CLOSE ================= */
  const handleModalClose = () => {
    if (!saving) {
      setShowModal(false);
      setValue("");
      setErrors({});
      setEditOldValue("");
    }
  };

  /* ================= KEY HANDLER ================= */
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleSave();
    } else if (e.key === 'Escape') {
      handleModalClose();
    }
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Bank Guarantee Type</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button
              className="btn btn-primary"
              onClick={handleAddClick}
              disabled={loading}
            >
              <FaPlusCircle /> &nbsp; Add Bank Guarantee Type
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search bank guarantee types..."
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
              <div className="center-align p-20">Loading bank guarantee types...</div>
            ) : (
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Bank Guarantee Type</th>
                    {tablesList.map((table, index) => (
                      <th key={index}>{table.captiliszedTableName}</th>
                    ))}
                    <th className={styles.actionCol}>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.length > 0 ? (
                    filteredData.map((item, index) => (
                      <tr key={item.id || index}>
                        <td>
                          <input 
                            type="hidden" 
                            className={styles.findLengths} 
                            value={item.bg_type} 
                          />
                          {item.bg_type}
                        </td>
                        {tablesList.map((table, tIndex) => (
                          <td key={tIndex}>
                            {getCountForTable(item, table.tName)}
                          </td>
                        ))}
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
						      onClick={() => handleDeleteClick(index)}
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
                      <td colSpan={tablesList.length + 2} className="center-align p-20">
                        {search ? "No matching results found" : "No bank guarantee types available"}
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>

      {/* ADD/EDIT MODAL */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>
                {mode === "add"
                  ? "Add Bank Guarantee Type"
                  : "Update Bank Guarantee Type"}
              </span>
              <FaTimes
                className={styles.close}
                onClick={handleModalClose}
                title="Close"
                disabled={saving}
              />
            </div>

            <div className={styles.modalBody}>
              <div className="input-field">
                <input
                  type="text"
                  placeholder="Enter bank guarantee type"
                  value={value}
                  onChange={handleInputChange}
                  onKeyDown={handleKeyDown}
                  autoFocus
                  disabled={saving}
                  className={errors.bg_type ? "error" : ""}
                />
                {errors.bg_type && (
                  <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{errors.bg_type}</span>
                )}
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleSave}
                  disabled={saving || !value.trim()}
                >
                  {saving ? "SAVING..." : mode === "add" ? "ADD" : "UPDATE"}
                </button>
                <button
                  className={styles.cancelBtn}
                  onClick={handleModalClose}
                  disabled={saving}
                >
                  CANCEL
                </button>
              </div>
            </div>
          </div>
        </div>
      )}

      {/* ERROR MODAL */}
      {showErrorModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>Error</span>
              <FaTimes
                className={styles.close}
                onClick={() => setShowErrorModal(false)}
                title="Close"
              />
            </div>
            <div className={styles.modalBody}>
              <div className="center-align">
                <p style={{color: 'red', marginBottom: '20px'}}>
                  Reference data cannot be edited or deleted when in use by other Data sets
                </p>
                <div className={styles.modalActions}>
                  <button
                    className={styles.saveBtn}
                    onClick={() => setShowErrorModal(false)}
                  >
                    OK
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}