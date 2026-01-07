import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle } from "react-icons/fa";
import "react-toastify/dist/ReactToastify.css";
import styles from "./ContractFileType.module.css";
import { API_BASE_URL } from "../../../../../config";
import Swal from "sweetalert2";

const API_ENDPOINTS = {
  GET: "/contract-file-type",
  ADD: "/add-contract-file-type",
  UPDATE: "/update-contract-file-type",
  DELETE: "/delete-contract-file-type"
};

export default function ContractFileType() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [updateValue, setUpdateValue] = useState("");
  const [oldValue, setOldValue] = useState("");
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState({});
  const [updateErrors, setUpdateErrors] = useState({});

  /* ================= FETCH DATA ================= */
  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.GET}`, {
        method: "GET"
      });

      const json = await response.json();
      console.log("API Response:", json); // Debug log

      // Try different response structures based on your JSP
      let list = [];
      
      if (json?.contractFileType) {
        // If response has contractFileType array
        list = json.contractFileType.map((item, index) => ({
          id: index,
          contract_file_type: item.contract_file_type
        }));
      } else if (json?.contractFileTypeDetails?.dlist1) {
        // If response has nested structure
        list = json.contractFileTypeDetails.dlist1.map((item, index) => ({
          id: index,
          contract_file_type: item.contract_file_type || item.value
        }));
      } else if (json?.dlist1) {
        // If response has direct dlist1
        list = json.dlist1.map((item, index) => ({
          id: index,
          contract_file_type: item.contract_file_type || item.value
        }));
      }

      setData(list.filter(i => i.contract_file_type));
    } catch (error) {
      console.error("Fetch error:", error);
      setData([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* ================= VALIDATION ================= */
  const validateInput = (inputValue, isUpdate = false, currentOldValue = "") => {
    const errors = {};
    const trimmedValue = inputValue.trim();
    
    if (!trimmedValue) {
      errors.value = "Contract File Type is required";
      return errors;
    }
    
    if (isUpdate) {
      const exists = data.some((item) => 
        item.contract_file_type.toLowerCase() !== currentOldValue.toLowerCase() &&
        item.contract_file_type.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        errors.value = `${trimmedValue} already exists`;
      }
    } else {
      const exists = data.some(item => 
        item.contract_file_type.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        errors.value = `${trimmedValue} already exists`;
      }
    }
    
    return errors;
  };

  /* ================= HANDLE INPUT CHANGE ================= */
  const handleInputChange = (e) => {
    const newValue = e.target.value;
    setValue(newValue);
    
    if (errors.value) {
      setErrors({});
    }
  };

  const handleUpdateInputChange = (e) => {
    const newValue = e.target.value;
    setUpdateValue(newValue);
    
    if (updateErrors.value) {
      setUpdateErrors({});
    }
  };

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setValue("");
    setErrors({});
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item) => {
    setUpdateValue(item.contract_file_type);
    setOldValue(item.contract_file_type);
    setUpdateErrors({});
    setShowUpdateModal(true);
  };

  /* ================= DELETE ================= */
  const handleDeleteClick = async (item) => {
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
          contract_file_type: item.contract_file_type
        })
      });

      const result = await response.json();
      console.log("Delete response:", result); // Debug log

      if (result.status !== "success" && !result.success) {
        throw new Error(result.message || "Delete failed");
      }

      setData(prev => prev.filter((i) => i.contract_file_type !== item.contract_file_type));
      Swal.fire('Deleted!', 'Record has been deleted', 'success');
    } catch (error) {
      console.error(error);
      Swal.fire('Error', 'Failed to delete contract file type', 'error');
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

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.ADD}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          contract_file_type: value.trim()
        })
      });

      const result = await response.json();
      console.log("Add response:", result); // Debug log

      if (result.status !== "success" && !result.success) {
        throw new Error(result.message || "Operation failed");
      }

      // Add to UI
      const newItem = { 
        id: Date.now(), 
        contract_file_type: value.trim() 
      };
      setData(prev => [newItem, ...prev]);

      setShowModal(false);
      setValue("");
      setErrors({});
      
      Swal.fire(
        'Success!',
        'Contract File Type added successfully',
        'success'
      );
    } catch (error) {
      console.error(error);
      Swal.fire('Error', 'Failed to add contract file type', 'error');
    } finally {
      setSaving(false);
    }
  };

  /* ================= UPDATE ================= */
  const handleUpdate = async () => {
    const validationErrors = validateInput(updateValue, true, oldValue);
    
    if (Object.keys(validationErrors).length > 0) {
      setUpdateErrors(validationErrors);
      return;
    }

    try {
      setSaving(true);

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.UPDATE}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          value_old: oldValue,
          value_new: updateValue.trim()
        })
      });

      const result = await response.json();
      console.log("Update response:", result); // Debug log

      if (result.status !== "success" && !result.success) {
        throw new Error(result.message || "Operation failed");
      }

      // Update in UI
      setData(prev =>
        prev.map((item) =>
          item.contract_file_type === oldValue
            ? { ...item, contract_file_type: updateValue.trim() }
            : item
        )
      );

      setShowUpdateModal(false);
      setUpdateValue("");
      setOldValue("");
      setUpdateErrors({});
      
      Swal.fire(
        'Success!',
        'Contract File Type updated successfully',
        'success'
      );
    } catch (error) {
      console.error(error);
      Swal.fire('Error', 'Failed to update contract file type', 'error');
    } finally {
      setSaving(false);
    }
  };

  /* ================= FILTER DATA ================= */
  const filteredData = data.filter((item) =>
    item.contract_file_type?.toLowerCase().includes(search.toLowerCase())
  );

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
      setOldValue("");
      setUpdateErrors({});
    }
  };

  /* ================= KEY HANDLERS ================= */
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleSave();
    } else if (e.key === 'Escape') {
      handleModalClose();
    }
  };

  const handleUpdateKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleUpdate();
    } else if (e.key === 'Escape') {
      handleUpdateModalClose();
    }
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Contract File Type</h2>
        </div>

        <div className="innerPage">
          <div className={styles.topActions}>
            <button
              className="btn btn-primary"
              onClick={handleAddClick}
              disabled={loading}
            >
              <FaPlusCircle /> &nbsp; Add Contract File Type
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search contract file types..."
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

          <div className="dataTable">
            {loading ? (
              <div className="center-align p-20">Loading contract file types...</div>
            ) : (
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Contract File Type</th>
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
                            value={item.contract_file_type} 
                          />
                          {item.contract_file_type}
                        </td>
                        <td className={styles.actionCol}>
                          <div className={styles.actionButtons}>
                            <button
                              className={styles.editBtn}
                              onClick={() => handleEditClick(item)}
                              title="Edit"
                            >
                              <FaEdit />
                            </button>
                            <button
                              className={styles.deleteBtn}
                              onClick={() => handleDeleteClick(item)}
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
                      <td colSpan="2" className="center-align p-20">
                        {search ? "No matching results found" : "No contract file types available"}
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>

      {/* ADD MODAL */}
      {showModal && (
        <div className={styles.modalOverlay}>
          <div className={styles.modal}>
            <div className={styles.modalHeader}>
              <span>Add Contract File Type</span>
              <FaTimes
                className={styles.close}
                onClick={handleModalClose}
                title="Close"
              />
            </div>

            <div className={styles.modalBody}>
              <input
                type="text"
                id="contract_file_type"
                placeholder="Enter contract file type"
                value={value}
                onChange={handleInputChange}
                onKeyDown={handleKeyDown}
                autoFocus
                disabled={saving}
              />
              {errors.value && (
                <span style={{color: 'red', fontSize: '12px', display: 'block', marginTop: '5px'}}>
                  {errors.value}
                </span>
              )}

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleSave}
                  disabled={saving || !value.trim()}
                >
                  {saving ? "ADDING..." : "ADD"}
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
              <span>Update Contract File Type</span>
              <FaTimes
                className={styles.close}
                onClick={handleUpdateModalClose}
                title="Close"
              />
            </div>

            <div className={styles.modalBody}>
              <input 
                type="hidden" 
                id="value_old" 
                value={oldValue} 
              />
              <input
                type="text"
                id="value_new"
                placeholder="Enter contract file type"
                value={updateValue}
                onChange={handleUpdateInputChange}
                onKeyDown={handleUpdateKeyDown}
                autoFocus
                disabled={saving}
              />
              {updateErrors.value && (
                <span style={{color: 'red', fontSize: '12px', display: 'block', marginTop: '5px'}}>
                  {updateErrors.value}
                </span>
              )}

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleUpdate}
                  disabled={saving || !updateValue.trim()}
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
    </div>
  );
}