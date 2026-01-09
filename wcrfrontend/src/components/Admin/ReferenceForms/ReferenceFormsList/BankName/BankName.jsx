import React, { useState, useEffect, useCallback } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle, FaSearch } from "react-icons/fa";
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
  const [deletableData, setDeletableData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [search, setSearch] = useState("");
  const [value, setValue] = useState("");
  const [updateValue, setUpdateValue] = useState("");
  const [updateOldValue, setUpdateOldValue] = useState("");
  const [editIndex, setEditIndex] = useState(null);
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState({});
  const [updateErrors, setUpdateErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  /* ================= FETCH DATA ================= */
  /* ================= FETCH DATA ================= */
  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.GET}`, {
        method: "GET",
        headers: { "Content-Type": "application/json" }
      });

      const json = await response.json();
      console.log("API Response:", json); // Debug log

      if (json.success) {
        // Process the JSON data directly
        const bankNameList = json?.bankNameList || [];
        const bankNameDetails = json?.bankNameDetails || {};
        
        // Get bankNameList1 from bankNameDetails (like in JSP)
        const bankNameList1 = bankNameDetails?.bankNameList1 || [];
        const countList = bankNameDetails?.countList || [];
        const tablesList = bankNameDetails?.tablesList || [];
        const deletableBankNames = bankNameDetails?.bankNameList || [];
        
        // Process bank names
        const processedData = bankNameList1.map((item, index) => ({
          id: index + 1,
          bank_name: item.bank_name || "",
          counts: countList.filter(count => count.bank_name_fk === item.bank_name) || []
        }));

        // Process tables list
        const processedTables = tablesList.map(item => ({
          tName: item.tName || "",
          captiliszedTableName: item.captiliszedTableName || item.tName || ""
        }));

        // Process deletable data
        const processedDeletable = deletableBankNames.map(item => ({
          bank_name: item.bank_name
        }));

        setData(processedData);
        setTablesList(processedTables);
        setDeletableData(processedDeletable);
        
        // Clear messages
        setSuccessMessage("");
        setErrorMessage("");
        
      } else {
        setErrorMessage(json.message || "Failed to load bank names");
      }
      
    } catch (error) {
      console.error("Fetch error:", error);
      setData([]);
      setTablesList([]);
      setDeletableData([]);
      setErrorMessage("Failed to load bank names");
    } finally {
      setLoading(false);
    }
  }, []);
  useEffect(() => {
    fetchData();
  }, [fetchData]);

  /* ================= VALIDATION ================= */
  const validateInput = (inputValue, isUpdate = false, oldValue = "") => {
    const newErrors = {};
    const trimmedValue = inputValue.trim();
    
    if (!trimmedValue) {
      newErrors.bank_name = "Bank Name is required";
      return newErrors;
    }
    
    // Check for duplicates (like JSP's doValidate function)
    if (isUpdate) {
      // For update, exclude the current value
      const exists = data.some((item, index) => 
        index !== editIndex && 
        item.bank_name.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        newErrors.bank_name = `${trimmedValue} already exists`;
      }
    } else {
      // For add, check all values
      const exists = data.some(item => 
        item.bank_name.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        newErrors.bank_name = `${trimmedValue} already exists`;
      }
    }
    
    return newErrors;
  };

  /* ================= CHECK IF CAN DELETE ================= */
  const canDeleteBankName = (bankName) => {
    return deletableData.some(item => item.bank_name === bankName);
  };

  /* ================= CHECK IF CAN EDIT ================= */
  const canEditBankName = (item) => {
    // Check if has counts in any table
    const hasCounts = item.counts && item.counts.length > 0;
    return !hasCounts;
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
    setValue("");
    setErrors({});
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item, index) => {
    // Check if can be edited
    if (!canEditBankName(item)) {
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
  const handleDeleteClick = async (item, index) => {
    // Check if can be deleted
    if (!canDeleteBankName(item.bank_name)) {
      return;
    }

    // Check if has counts
    if (!canEditBankName(item)) {
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
      // Submit form like JSP does
      const formData = new FormData();
      formData.append("bank_name", item.bank_name);

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.DELETE}`, {
        method: "POST",
        body: formData
      });

      const text = await response.text();
      console.log("Delete response:", text);

      if (response.ok) {
        // Remove item from local state
        setData(prev => prev.filter((_, i) => i !== index));
        setDeletableData(prev => prev.filter(d => d.bank_name !== item.bank_name));
        
        Swal.fire('Deleted!', 'Record has been deleted', 'success');
        setSuccessMessage("Record deleted successfully");
        setTimeout(() => setSuccessMessage(""), 3000);
      } else {
        throw new Error('Failed to delete bank name');
      }
    } catch (error) {
      console.error(error);
      Swal.fire('Error', 'Failed to delete bank name', 'error');
      setErrorMessage("Failed to delete bank name");
      setTimeout(() => setErrorMessage(""), 3000);
    }
  };

  /* ================= SAVE (ADD) ================= */
  const handleSave = async () => {
    const validationErrors = validateInput(value);
    
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    try {
      setSaving(true);
      
      // Submit form like JSP does
      const formData = new FormData();
      formData.append("bank_name", value.trim());

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.ADD}`, {
        method: "POST",
        body: formData
      });

      const text = await response.text();
      console.log("Add response:", text);

      if (response.ok) {
        // Refresh data
        await fetchData();
        
        setShowModal(false);
        setValue("");
        setErrors({});
        
        // Extract success message
        const parser = new DOMParser();
        const htmlDoc = parser.parseFromString(text, 'text/html');
        const successDiv = htmlDoc.querySelector('.close-message');
        if (successDiv) {
          setSuccessMessage(successDiv.textContent);
        } else {
          setSuccessMessage("Bank name added successfully");
        }
        setTimeout(() => setSuccessMessage(""), 3000);
      } else {
        throw new Error('Failed to add bank name');
      }
    } catch (error) {
      console.error(error);
      setErrorMessage("Failed to add bank name");
      setTimeout(() => setErrorMessage(""), 3000);
    } finally {
      setSaving(false);
    }
  };

  /* ================= SAVE (UPDATE) ================= */
  const handleUpdate = async () => {
    const validationErrors = validateInput(updateValue, true, updateOldValue);
    
    if (Object.keys(validationErrors).length > 0) {
      setUpdateErrors(validationErrors);
      return;
    }

    try {
      setSaving(true);
      
      // Submit form like JSP does
      const formData = new FormData();
      formData.append("bank_name_new", updateValue.trim());
      formData.append("bank_name_old", updateOldValue.trim());

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.UPDATE}`, {
        method: "POST",
        body: formData
      });

      const text = await response.text();
      console.log("Update response:", text);

      if (response.ok) {
        // Refresh data
        await fetchData();
        
        setShowUpdateModal(false);
        setUpdateValue("");
        setUpdateOldValue("");
        setUpdateErrors({});
        
        // Extract success message
        const parser = new DOMParser();
        const htmlDoc = parser.parseFromString(text, 'text/html');
        const successDiv = htmlDoc.querySelector('.close-message');
        if (successDiv) {
          setSuccessMessage(successDiv.textContent);
        } else {
          setSuccessMessage("Bank name updated successfully");
        }
        setTimeout(() => setSuccessMessage(""), 3000);
      } else {
        throw new Error('Failed to update bank name');
      }
    } catch (error) {
      console.error(error);
      setErrorMessage("Failed to update bank name");
      setTimeout(() => setErrorMessage(""), 3000);
    } finally {
      setSaving(false);
    }
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

  const removeErrorMsg = () => {
    setUpdateErrors({});
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

  /* ================= RENDER MESSAGES ================= */
  const renderMessages = () => {
    if (successMessage) {
      return (
        <div className="center-align m-1 close-message" style={{ color: 'green' }}>
          {successMessage}
        </div>
      );
    }
    if (errorMessage) {
      return (
        <div className="center-align m-1 close-message" style={{ color: 'red' }}>
          {errorMessage}
        </div>
      );
    }
    return null;
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

  /* ================= SHOW DELETE BUTTON ================= */
  const showDeleteButton = (bank_name) => {
    return canDeleteBankName(bank_name);
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Bank Name</h2>
        </div>

        <div className="innerPage">
          {/* Display success/error messages */}
          {renderMessages()}

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
            {search ? (
              <span
                className={styles.clear}
                onClick={() => setSearch("")}
              >
                <FaTimes />
              </span>
            ) : (
              <span className={styles.searchIcon}>
                <FaSearch />
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
                            {showDeleteButton(item.bank_name) && (
                              <button
                                className={styles.deleteBtn}
                                onClick={() => handleDeleteClick(item, index)}
                                title="Delete"
                              >
                                <FaTrash />
                              </button>
                            )}
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
                  name="bank_name"
                  placeholder="Enter bank name"
                  value={value}
                  onChange={handleInputChange}
                  onKeyDown={(e) => handleKeyDown(e, "add")}
                  autoFocus
                  disabled={saving}
                  className={errors.bank_name ? "error" : ""}
                />
                <label htmlFor="bank_name_text">Bank Name</label>
                {errors.bank_name && (
                  <span id="bank_nameError" className="error-msg" style={{color: 'red', fontSize: '12px'}}>
                    {errors.bank_name}
                  </span>
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
                  name="bank_name_new"
                  placeholder="Enter bank name"
                  value={updateValue}
                  onChange={handleUpdateInputChange}
                  onKeyDown={(e) => handleKeyDown(e, "update")}
                  autoFocus
                  disabled={saving}
                  className={updateErrors.bank_name ? "error" : ""}
                />
                <input type="hidden" id="bank_name_old_text" name="bank_name_old" value={updateOldValue} />
                <label htmlFor="bank_name_text1">Bank Name</label>
                {updateErrors.bank_name && (
                  <span id="bank_name_text1Error" className="error-msg" style={{color: 'red', fontSize: '12px'}}>
                    {updateErrors.bank_name}
                  </span>
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