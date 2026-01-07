import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle } from "react-icons/fa";
import "react-toastify/dist/ReactToastify.css";
import styles from "./BankName.module.css";
import { API_BASE_URL } from "../../../../../config";
import Swal from "sweetalert2";

const API_ENDPOINTS = {
  GET: "/bank-name",
  ADD: "/add-bank-name",
  UPDATE: "/update-bank-name",
  DELETE: "/delete-bank-name"
};

export default function BankName() {
  const [data, setData] = useState([]);
  const [tablesList, setTablesList] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [updateValue, setUpdateValue] = useState("");
  const [updateOldValue, setUpdateOldValue] = useState("");
  const [mode, setMode] = useState("add");
  const [editId, setEditId] = useState(null);
  const [editIndex, setEditIndex] = useState(null);
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState({});
  const [updateErrors, setUpdateErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  /* ================= FETCH DATA ================= */
  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.GET}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" }
      });

      const json = await response.json();

      const bankNameList = json?.bankNameDetails?.bankNameList1?.map((item, index) => ({
        id: index,
        bank_name: item.bank_name,
        counts: json?.bankNameDetails?.countList?.filter(count => count.bank_name_fk === item.bank_name) || []
      })) || [];

      const tables = json?.bankNameDetails?.tablesList?.map(item => ({
        tName: item.tName,
        captiliszedTableName: item.captiliszedTableName
      })) || [];

      setData(bankNameList.filter((i) => i.bank_name));
      setTablesList(tables);

      if (json.success) {
        setSuccessMessage(json.success);
        setTimeout(() => setSuccessMessage(""), 3000);
      }
      if (json.error) {
        setErrorMessage(json.error);
        setTimeout(() => setErrorMessage(""), 3000);
      }

    } catch (error) {
      console.error("Fetch error:", error);
      setData([]);
      setTablesList([]);
      setErrorMessage("Failed to load bank names");
      setTimeout(() => setErrorMessage(""), 3000);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* ================= VALIDATION ================= */
  const validateInput = (inputValue, isUpdate = false, oldValue = "") => {
    const errors = {};
    const trimmedValue = inputValue.trim();
    
    if (!trimmedValue) {
      errors.bank_name = "Bank Name is required";
      return errors;
    }
    
    if (isUpdate) {
      const exists = data.some(item => 
        item.bank_name.toLowerCase() === trimmedValue.toLowerCase() && 
        item.bank_name.toLowerCase() !== oldValue.toLowerCase()
      );
      if (exists) {
        errors.bank_name = `${trimmedValue} already exists`;
      }
    } else {
      const exists = data.some(item => 
        item.bank_name.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        errors.bank_name = `${trimmedValue} already exists`;
      }
    }
    
    return errors;
  };

  /* ================= HANDLE INPUT CHANGE ================= */
  const handleInputChange = (e) => {
    const newValue = e.target.value;
    setValue(newValue);
    
    if (errors.bank_name) {
      const validationErrors = validateInput(newValue);
      setErrors(validationErrors);
    }
  };

  const handleUpdateInputChange = (e) => {
    const newValue = e.target.value;
    setUpdateValue(newValue);
    
    if (updateErrors.bank_name) {
      const validationErrors = validateInput(newValue, true, updateOldValue);
      setUpdateErrors(validationErrors);
    }
  };

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setMode("add");
    setValue("");
    setErrors({});
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item, index) => {
    const isInUse = item.counts && item.counts.length > 0;
    if (isInUse) {
      setShowErrorModal(true);
      return;
    }

    setUpdateValue(item.bank_name);
    setUpdateOldValue(item.bank_name);
    setEditIndex(index);
    setUpdateErrors({});
    setShowUpdateModal(true);
  };

  /* ================= DELETE ================= */
  const handleDeleteClick = async (index) => {
    const item = data[index];
    
    const isInUse = item.counts && item.counts.length > 0;
    if (isInUse) {
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
      const formData = new FormData();
      formData.append("bank_name", item.bank_name);

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.DELETE}`, {
        method: "POST",
        body: formData
      });

      const result = await response.json();

      if (result.success) {
        setData((prev) => prev.filter((_, i) => i !== index));
        Swal.fire('Deleted!', 'Record has been deleted', 'success');
        setSuccessMessage("Record deleted successfully");
        setTimeout(() => setSuccessMessage(""), 3000);
      } else {
        throw new Error(result.message || "Failed to delete");
      }
    } catch (error) {
      console.error(error);
      Swal.fire('Error', 'Failed to delete bank name', 'error');
      setErrorMessage("Failed to delete bank name");
      setTimeout(() => setErrorMessage(""), 3000);
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

      const payload = {
        bank_name: value.trim()
      };

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.ADD}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      });

      const result = await response.json();

      if (result.success) {
        setData(prev => [
          { id: data.length, bank_name: value.trim(), counts: [] },
          ...prev
        ]);
        setShowModal(false);
        setValue("");
        Swal.fire("Success!", "Bank Name added successfully", "success");
      } else {
        throw new Error(result.message);
      }
    } catch (e) {
      Swal.fire("Error", "Failed to add bank name", "error");
    } finally {
      setSaving(false);
    }
  };

  /* ================= UPDATE ================= */
  const handleUpdate = async () => {
    const validationErrors = validateInput(updateValue, true, updateOldValue);
    if (Object.keys(validationErrors).length > 0) {
      setUpdateErrors(validationErrors);
      return;
    }

    try {
      setSaving(true);

      const payload = {
        bank_name_old: updateOldValue,
        bank_name_new: updateValue.trim()
      };

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.UPDATE}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify(payload)
      });

      const result = await response.json();

      if (result.success) {
        setData(prev =>
          prev.map((item, index) =>
            index === editIndex
              ? { ...item, bank_name: updateValue.trim() }
              : item
          )
        );

        setShowUpdateModal(false);
        setUpdateValue("");
        setUpdateOldValue("");

        Swal.fire("Success!", "Bank Name updated successfully", "success");
      } else {
        throw new Error(result.message);
      }
    } catch (err) {
      Swal.fire("Error", "Failed to update bank name", "error");
    } finally {
      setSaving(false);
    }
  };

  /* ================= FILTER DATA ================= */
  const filteredData = data.filter((item) =>
    item.bank_name?.toLowerCase().includes(search.toLowerCase())
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
    }
  };

  const handleUpdateModalClose = () => {
    if (!saving) {
      setShowUpdateModal(false);
      setUpdateValue("");
      setUpdateOldValue("");
      setUpdateErrors({});
    }
  };

  /* ================= KEY HANDLER ================= */
  const handleKeyDown = (e, type = "add") => {
    if (e.key === 'Enter') {
      if (type === "add") handleSave();
      else handleUpdate();
    } else if (e.key === 'Escape') {
      if (type === "add") handleModalClose();
      else handleUpdateModalClose();
    }
  };

  const removeErrorMsg = () => {
    setUpdateErrors({});
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Bank Name</h2>
        </div>

        {/* Success/Error Messages */}
        {successMessage && (
          <div className={`center-align m-1 ${styles.successMessage}`}>
            {successMessage}
          </div>
        )}
        {errorMessage && (
          <div className={`center-align m-1 ${styles.errorMessage}`}>
            {errorMessage}
          </div>
        )}

        <div className="innerPage">
          <div className={styles.topActions}>
            <button
              className="btn btn-primary"
              onClick={handleAddClick}
              disabled={loading}
            >
              <FaPlusCircle /> &nbsp; Add Bank Name
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search bank names..."
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
              <div className="center-align p-20">
                <div className="preloader-wrapper big active">
                  <div className="spinner-layer spinner-blue-only">
                    <div className="circle-clipper left">
                      <div className="circle"></div>
                    </div>
                    <div className="gap-patch">
                      <div className="circle"></div>
                    </div>
                    <div className="circle-clipper right">
                      <div className="circle"></div>
                    </div>
                  </div>
                </div>
                <div>Loading bank names...</div>
              </div>
            ) : (
              <table className={styles.table} id="bank_name_table">
                <thead>
                  <tr>
                    <th>Bank Name</th>
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
                            value={item.bank_name} 
                            id={`value${index + 1}`}
                          />
                          {item.bank_name}
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
                        {search ? "No matching results found" : "No bank names available"}
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            )}
          </div>
          
          {!loading && filteredData.length > 0 && (
            <div className={`center-align ${styles.footerText}`}>
              Showing {filteredData.length} of {data.length} records
            </div>
          )}
        </div>
      </div>

      {/* ADD MODAL */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>Add Bank Name</span>
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
                  id="bank_name_text"
                  placeholder="Enter bank name"
                  value={value}
                  onChange={handleInputChange}
                  onKeyDown={(e) => handleKeyDown(e, "add")}
                  autoFocus
                  disabled={saving}
                  className={errors.bank_name ? "error" : ""}
                />
                {errors.bank_name && (
                  <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{errors.bank_name}</span>
                )}
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleSave}
                  disabled={saving || !value.trim() || errors.bank_name}
                >
                  {saving ? "SAVING..." : "ADD"}
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

      {/* UPDATE MODAL */}
      {showUpdateModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>Update Bank Name</span>
              <FaTimes
                className={styles.close}
                onClick={() => {
                  handleUpdateModalClose();
                  removeErrorMsg();
                }}
                title="Close"
                disabled={saving}
              />
            </div>

            <div className={styles.modalBody}>
              <div className="input-field">
                <input
                  type="text"
                  id="bank_name_text1"
                  placeholder="Enter bank name"
                  value={updateValue}
                  onChange={handleUpdateInputChange}
                  onKeyDown={(e) => handleKeyDown(e, "update")}
                  autoFocus
                  disabled={saving}
                  className={updateErrors.bank_name ? "error" : ""}
                />
                <input type="hidden" id="bank_name_old_text" value={updateOldValue} />
                {updateErrors.bank_name && (
                  <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{updateErrors.bank_name}</span>
                )}
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleUpdate}
                  disabled={saving || !updateValue.trim() || updateErrors.bank_name}
                >
                  {saving ? "UPDATING..." : "UPDATE"}
                </button>
                <button
                  className={styles.cancelBtn}
                  onClick={() => {
                    handleUpdateModalClose();
                    removeErrorMsg();
                  }}
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

      {/* Loading Overlay */}
      {saving && (
        <div className={styles.pageLoader}>
          <div className="preloader-wrapper big active">
            <div className="spinner-layer spinner-blue-only">
              <div className="circle-clipper left">
                <div className="circle"></div>
              </div>
              <div className="gap-patch">
                <div className="circle"></div>
              </div>
              <div className="circle-clipper right">
                <div className="circle"></div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}