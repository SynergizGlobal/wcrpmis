import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle } from "react-icons/fa";
import "react-toastify/dist/ReactToastify.css";
import styles from "./InsuranceType.module.css";
import { API_BASE_URL } from "../../../../../config";
import Swal from "sweetalert2";

const API_ENDPOINTS = {
  GET: "/insurance-type",
  ADD: "/add-insurance-type",
  UPDATE: "/update-insurance-type",
  DELETE: "/delete-insurance-type"
};

export default function InsuranceType() {
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

      const insuranceTypeList = json?.insuranceTypesDetails?.dList1?.map((item, index) => ({
        id: index,
        insurance_type: item.insurance_type,
        counts: json?.insuranceTypesDetails?.countList?.filter(count => count.insurance_type === item.insurance_type) || []
      })) || [];

      const tables = json?.insuranceTypesDetails?.tablesList?.map(item => ({
        tName: item.tName,
        captiliszedTableName: item.captiliszedTableName
      })) || [];

      setData(insuranceTypeList.filter((i) => i.insurance_type));
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
      setErrorMessage("Failed to load insurance types");
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
      errors.insurance_type = "Insurance Type is required";
      return errors;
    }
    
    if (isUpdate) {
      const exists = data.some(item => 
        item.insurance_type.toLowerCase() === trimmedValue.toLowerCase() && 
        item.insurance_type.toLowerCase() !== oldValue.toLowerCase()
      );
      if (exists) {
        errors.insurance_type = `${trimmedValue} already exists`;
      }
    } else {
      const exists = data.some(item => 
        item.insurance_type.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        errors.insurance_type = `${trimmedValue} already exists`;
      }
    }
    
    return errors;
  };

  /* ================= HANDLE INPUT CHANGE ================= */
  const handleInputChange = (e) => {
    const newValue = e.target.value;
    setValue(newValue);
    
    if (errors.insurance_type) {
      const validationErrors = validateInput(newValue);
      setErrors(validationErrors);
    }
  };

  const handleUpdateInputChange = (e) => {
    const newValue = e.target.value;
    setUpdateValue(newValue);
    
    if (updateErrors.insurance_type) {
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

    setUpdateValue(item.insurance_type);
    setUpdateOldValue(item.insurance_type);
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
      formData.append("insurance_type", item.insurance_type);

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
      Swal.fire('Error', 'Failed to delete insurance type', 'error');
      setErrorMessage("Failed to delete insurance type");
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
        insurance_type: value.trim()
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
          { id: data.length, insurance_type: value.trim(), counts: [] },
          ...prev
        ]);
        setShowModal(false);
        setValue("");
        Swal.fire("Success!", "Insurance Type added successfully", "success");
      } else {
        throw new Error(result.message);
      }
    } catch (e) {
      Swal.fire("Error", "Failed to add insurance type", "error");
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
        value_old: updateOldValue,
        value_new: updateValue.trim()
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
              ? { ...item, insurance_type: updateValue.trim() }
              : item
          )
        );

        setShowUpdateModal(false);
        setUpdateValue("");
        setUpdateOldValue("");

        Swal.fire("Success!", "Insurance Type updated successfully", "success");
      } else {
        throw new Error(result.message);
      }
    } catch (err) {
      Swal.fire("Error", "Failed to update insurance type", "error");
    } finally {
      setSaving(false);
    }
  };


  /* ================= FILTER DATA ================= */
  const filteredData = data.filter((item) =>
    item.insurance_type?.toLowerCase().includes(search.toLowerCase())
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

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Insurance Type</h2>
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
              <FaPlusCircle /> &nbsp; Add Insurance Type
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search insurance types..."
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
                <div>Loading insurance types...</div>
              </div>
            ) : (
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Insurance Type</th>
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
                            value={item.insurance_type} 
                          />
                          {item.insurance_type}
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
                        {search ? "No matching results found" : "No insurance types available"}
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
              <span>Add Insurance Type</span>
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
                  id="insurance_type_text"
                  placeholder="Enter insurance type"
                  value={value}
                  onChange={handleInputChange}
                  onKeyDown={(e) => handleKeyDown(e, "add")}
                  autoFocus
                  disabled={saving}
                  className={errors.insurance_type ? "error" : ""}
                />
                {errors.insurance_type && (
                  <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{errors.insurance_type}</span>
                )}
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleSave}
                  disabled={saving || !value.trim() || errors.insurance_type}
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
              <span>Update Insurance Type</span>
              <FaTimes
                className={styles.close}
                onClick={handleUpdateModalClose}
                title="Close"
                disabled={saving}
              />
            </div>

            <div className={styles.modalBody}>
              <div className="input-field">
                <input
                  type="text"
                  id="value_new"
                  placeholder="Enter insurance type"
                  value={updateValue}
                  onChange={handleUpdateInputChange}
                  onKeyDown={(e) => handleKeyDown(e, "update")}
                  autoFocus
                  disabled={saving}
                  className={updateErrors.insurance_type ? "error" : ""}
                />
                <input type="hidden" id="value_old" value={updateOldValue} />
                {updateErrors.insurance_type && (
                  <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{updateErrors.insurance_type}</span>
                )}
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleUpdate}
                  disabled={saving || !updateValue.trim() || updateErrors.insurance_type}
                >
                  {saving ? "UPDATING..." : "UPDATE"}
                </button>
                <button
                  className={styles.cancelBtn}
                  onClick={handleUpdateModalClose}
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