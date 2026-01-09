import React, { useState, useEffect, useCallback } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle, FaSearch } from "react-icons/fa";
import styles from "./Department.module.css";
import { API_BASE_URL } from "../../../../../config";
import Swal from "sweetalert2";

const API_ENDPOINTS = {
  GET: "/get-department",
  ADD: "/add-department",
  UPDATE: "/update-department",
  DELETE: "/delete-department"
};

export default function Department() {
  const [data, setData] = useState([]);
  const [tablesList, setTablesList] = useState([]);
  const [deletableData, setDeletableData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [search, setSearch] = useState("");
  const [formData, setFormData] = useState({
    department: "",
    department_name: "",
    contract_id_code: ""
  });
  const [editFormData, setEditFormData] = useState({
    department_new: "",
    department_name_new: "",
    department_code_new: "",
    department_old: "",
    department_name_old: "",
    department_code_old: ""
  });
  const [mode, setMode] = useState("add");
  const [editIndex, setEditIndex] = useState(null);
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState({});
  const [editErrors, setEditErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  /* ================= FETCH DATA ================= */
  const fetchData = useCallback(async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.GET}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({})
      });

      const json = await response.json();

      console.log("API Response:", json); // DEBUG: Check the actual response structure

      // Process data like JSP does
      const departmentList = json?.departmentDetails?.dList1?.map((item) => ({
        id: item.id || Date.now() + Math.random(),
        department: item.department,
        department_name: item.department_name,
        contract_id_code: item.contract_id_code,
        counts: json?.departmentDetails?.countList?.filter(count => count.department === item.department) || []
      })) || [];

      // Store deletable data separately (like dList in JSP)
      const deletableList = json?.departmentDetails?.dList?.map(item => ({
        department: item.department,
      })) || [];

      // FIXED: Handle nested table names like in JSP
      const tables = [];
      const tablesFromApi = json?.departmentDetails?.tablesList || [];
      
      tablesFromApi.forEach(tObj => {
        // Check if tObj.tName is an array (like in JSP) or a string
        if (Array.isArray(tObj.tName)) {
          tObj.tName.forEach(tableName => {
            tables.push({
              tName: tableName,
              captiliszedTableName: tableName ? tableName.replace(/_/g, ' ') : ''
            });
          });
        } else if (tObj.tName) {
          // If it's a string, treat it as single item
          tables.push({
            tName: tObj.tName,
            captiliszedTableName: tObj.tName ? tObj.tName.replace(/_/g, ' ') : ''
          });
        }
      });

      setData(departmentList.filter((i) => i.department));
      setDeletableData(deletableList);
      setTablesList(tables);
      
      // Clear messages after fetch
      setSuccessMessage("");
      setErrorMessage("");
    } catch (error) {
      console.error("Fetch error:", error);
      setData([]);
      setTablesList([]);
      setDeletableData([]);
      setErrorMessage("Failed to load departments");
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    fetchData();
  }, [fetchData]);

  /* ================= VALIDATION ================= */
  const validateInput = () => {
    const newErrors = {};
    const trimmedDept = formData.department.trim();
    const trimmedName = formData.department_name.trim();
    const trimmedCode = formData.contract_id_code.trim();
    
    // Basic validation
    if (!trimmedDept) newErrors.department = "Department ID is required";
    if (!trimmedName) newErrors.department_name = "Department Name is required";
    if (!trimmedCode) newErrors.contract_id_code = "Department Code is required";
    
    // Duplicate check (like JSP's doValidate function)
    if (mode === "add") {
      const existingDept = data.find(item => 
        item.department.toLowerCase() === trimmedDept.toLowerCase()
      );
      const existingName = data.find(item => 
        item.department_name.toLowerCase() === trimmedName.toLowerCase()
      );
      const existingCode = data.find(item => 
        item.contract_id_code.toLowerCase() === trimmedCode.toLowerCase()
      );
      
      if (existingDept && existingName && existingCode) {
        newErrors.all = `"${trimmedDept}" & "${trimmedName}" & "${trimmedCode}" already exist`;
      } else {
        if (existingDept) newErrors.department = `"${trimmedDept}" already exists`;
        if (existingName) newErrors.department_name = `"${trimmedName}" already exists`;
        if (existingCode) newErrors.contract_id_code = `"${trimmedCode}" already exists`;
      }
    }
    
    return newErrors;
  };

  const validateEditInput = () => {
    const newErrors = {};
    const trimmedDept = editFormData.department_new.trim();
    const trimmedName = editFormData.department_name_new.trim();
    const trimmedCode = editFormData.department_code_new.trim();
    
    // Basic validation
    if (!trimmedDept) newErrors.department_new = "Department ID is required";
    if (!trimmedName) newErrors.department_name_new = "Department Name is required";
    if (!trimmedCode) newErrors.department_code_new = "Department Code is required";
    
    // Duplicate check excluding current item (like JSP's doValidateUpdate)
    const existingDept = data.find((item, index) => 
      index !== editIndex && 
      item.department.toLowerCase() === trimmedDept.toLowerCase()
    );
    const existingName = data.find((item, index) => 
      index !== editIndex && 
      item.department_name.toLowerCase() === trimmedName.toLowerCase()
    );
    const existingCode = data.find((item, index) => 
      index !== editIndex && 
      item.contract_id_code.toLowerCase() === trimmedCode.toLowerCase()
    );
    
    if (existingDept && existingName && existingCode) {
      newErrors.all = `"${trimmedDept}" & "${trimmedName}" & "${trimmedCode}" already exist`;
    } else {
      if (existingDept) newErrors.department_new = `"${trimmedDept}" already exists`;
      if (existingName) newErrors.department_name_new = `"${trimmedName}" already exists`;
      if (existingCode) newErrors.department_code_new = `"${trimmedCode}" already exists`;
    }
    
    return newErrors;
  };

  /* ================= CHECK IF DEPARTMENT CAN BE DELETED ================= */
  const canDeleteDepartment = (department) => {
    // Check if department exists in deletableData (like JSP does)
    const isDeletable = deletableData.some(item => item.department === department);
    return isDeletable; // In JSP, if it's in dList, it CAN be deleted (shows delete button)
  };

  /* ================= CHECK IF DEPARTMENT CAN BE EDITED ================= */
  const canEditDepartment = (item) => {
    // In JSP, edit button is always shown but checks counts when clicked
    // So we'll show the button but check in handleEditClick
    return true;
  };

  /* ================= HANDLE INPUT CHANGE ================= */
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    if (errors[name]) {
      setErrors(prev => ({ ...prev, [name]: "" }));
    }
    if (errors.all) {
      setErrors(prev => ({ ...prev, all: "" }));
    }
  };

  const handleEditInputChange = (e) => {
    const { name, value } = e.target;
    setEditFormData(prev => ({
      ...prev,
      [name]: value
    }));
    
    if (editErrors[name]) {
      setEditErrors(prev => ({ ...prev, [name]: "" }));
    }
    if (editErrors.all) {
      setEditErrors(prev => ({ ...prev, all: "" }));
    }
  };

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setMode("add");
    setFormData({
      department: "",
      department_name: "",
      contract_id_code: ""
    });
    setErrors({});
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item, index) => {
    // Check if department has counts in any table (like JSP does)
    const hasCounts = item.counts && item.counts.length > 0;
    if (hasCounts) {
      setShowErrorModal(true);
      return;
    }

    setMode("edit");
    setEditFormData({
      department_new: item.department,
      department_name_new: item.department_name,
      department_code_new: item.contract_id_code,
      department_old: item.department,
      department_name_old: item.department_name,
      department_code_old: item.contract_id_code
    });
    setEditIndex(index);
    setEditErrors({});
    setShowModal(true);
  };

  /* ================= DELETE ================= */
  const handleDeleteClick = async (item, index) => {
    // Check if department can be deleted (exists in dList)
    if (!canDeleteDepartment(item.department)) {
      return; // No delete button should be shown anyway
    }

    // Check if it has counts
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
          department: item.department
        })
      });

      const result = await response.json();

      if (!result.success) {
        throw new Error(result.message);
      }

      setData((prev) => prev.filter((_, i) => i !== index));
      Swal.fire('Deleted!', 'Department has been deleted', 'success');
    } catch (error) {
      console.error(error);
      Swal.fire('Error', 'Failed to delete department', 'error');
    }
  };

  /* ================= SAVE ================= */
  const handleSave = async () => {
    if (mode === "add") {
      const validationErrors = validateInput();
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
            department: formData.department.trim(),
            department_name: formData.department_name.trim(),
            contract_id_code: formData.contract_id_code.trim()
          })
        });

        const result = await response.json();

        if (!result.success) {
          throw new Error(result.message);
        }

        // Refresh data to get updated counts
        await fetchData();
        
        setShowModal(false);
        setFormData({ department: "", department_name: "", contract_id_code: "" });
        setErrors({});
        
        setSuccessMessage("Department added successfully");
      } catch (error) {
        console.error(error);
        setErrorMessage("Failed to add department");
      } finally {
        setSaving(false);
      }
    } else {
      const validationErrors = validateEditInput();
      if (Object.keys(validationErrors).length > 0) {
        setEditErrors(validationErrors);
        return;
      }

      try {
        setSaving(true);
        const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.UPDATE}`, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            department_old: editFormData.department_old,
            department_new: editFormData.department_new.trim(),
            department_name_old: editFormData.department_name_old,
            department_name_new: editFormData.department_name_new.trim(),
            department_code_old: editFormData.department_code_old,
            department_code_new: editFormData.department_code_new.trim()
          })
        });

        const result = await response.json();

        if (!result.success) {
          throw new Error(result.message);
        }

        // Refresh data to get updated counts
        await fetchData();
        
        setShowModal(false);
        setEditFormData({
          department_new: "",
          department_name_new: "",
          department_code_new: "",
          department_old: "",
          department_name_old: "",
          department_code_old: ""
        });
        setEditErrors({});
        
        setSuccessMessage("Department updated successfully");
      } catch (error) {
        console.error(error);
        setErrorMessage("Failed to update department");
      } finally {
        setSaving(false);
      }
    }
  };

  /* ================= MODAL CLOSE ================= */
  const handleModalClose = () => {
    if (!saving) {
      setShowModal(false);
      setFormData({ department: "", department_name: "", contract_id_code: "" });
      setEditFormData({
        department_new: "",
        department_name_new: "",
        department_code_new: "",
        department_old: "",
        department_name_old: "",
        department_code_old: ""
      });
      setErrors({});
      setEditErrors({});
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

  /* ================= GET COUNT FOR TABLE ================= */
  const getCountForTable = (item, tableName) => {
    if (!item.counts || !Array.isArray(item.counts)) return null;
    const countObj = item.counts.find(count => count.tName === tableName);
    return countObj ? `(${countObj.count})` : null;
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
    item.department?.toLowerCase().includes(search.toLowerCase()) ||
    item.department_name?.toLowerCase().includes(search.toLowerCase()) ||
    item.contract_id_code?.toLowerCase().includes(search.toLowerCase())
  );

  /* ================= SHOW DELETE BUTTON ================= */
  const showDeleteButton = (department) => {
    // Only show delete button if department is in deletableData (dList)
    return canDeleteDepartment(department);
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Department</h2>
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
              <FaPlusCircle /> &nbsp; Add Department
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search departments..."
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
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Department ID</th>
                    <th>Department Name</th>
                    <th>Department Code</th>
                    {tablesList.map((table, index) => (
                      <th key={index}>{table.captiliszedTableName}</th>
                    ))}
                    <th className={`${styles.actionCol} ${styles.fixedActionCol}`}>Actions</th>
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
                            value={item.department} 
                          />
                          {item.department}
                        </td>
                        <td>
                          <input 
                            type="hidden" 
                            className={styles.findLengths1} 
                            value={item.department_name} 
                          />
                          {item.department_name}
                        </td>
                        <td>
                          <input 
                            type="hidden" 
                            className={styles.findLengths2} 
                            value={item.contract_id_code} 
                          />
                          {item.contract_id_code}
                        </td>
                        {tablesList.map((table, tIndex) => (
                          <td key={tIndex}>
                            {getCountForTable(item, table.tName)}
                          </td>
                        ))}
                        <td className={`${styles.actionCol} ${styles.fixedActionCol}`}>
                          <div className={styles.actionButtons}>
                            <button
                              className={styles.editBtn}
                              onClick={() => handleEditClick(item, index)}
                              title="Edit"
                            >
                              <FaEdit />
                            </button>
                            {showDeleteButton(item.department) && (
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
                      <td colSpan={tablesList.length + 4} className="center-align p-20">
                        {search ? "No matching results found" : "No departments available"}
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
                  ? "Add New Department"
                  : "Update Department"}
              </span>
              <FaTimes
                className={styles.close}
                onClick={handleModalClose}
                title="Close"
                disabled={saving}
              />
            </div>

            <div className={styles.modalBody}>
              {mode === "add" ? (
                <>
                  <div className="input-field">
                    <input
                      type="text"
                      name="department"
                      placeholder="Enter department ID"
                      value={formData.department}
                      onChange={handleInputChange}
                      onKeyDown={handleKeyDown}
                      autoFocus
                      disabled={saving}
                      className={errors.department ? "error" : ""}
                    />
                    {errors.department && (
                      <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{errors.department}</span>
                    )}
                  </div>
                  
                  <div className="input-field">
                    <input
                      type="text"
                      name="department_name"
                      placeholder="Enter department name"
                      value={formData.department_name}
                      onChange={handleInputChange}
                      onKeyDown={handleKeyDown}
                      disabled={saving}
                      className={errors.department_name ? "error" : ""}
                    />
                    {errors.department_name && (
                      <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{errors.department_name}</span>
                    )}
                  </div>
                  
                  <div className="input-field">
                    <input
                      type="text"
                      name="contract_id_code"
                      placeholder="Enter department code"
                      value={formData.contract_id_code}
                      onChange={handleInputChange}
                      onKeyDown={handleKeyDown}
                      disabled={saving}
                      className={errors.contract_id_code ? "error" : ""}
                    />
                    {errors.contract_id_code && (
                      <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{errors.contract_id_code}</span>
                    )}
                  </div>
                  
                  {errors.all && (
                    <div className="center-align">
                      <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{errors.all}</span>
                    </div>
                  )}
                </>
              ) : (
                <>
                  <input type="hidden" name="department_old" value={editFormData.department_old} />
                  <input type="hidden" name="department_name_old" value={editFormData.department_name_old} />
                  <input type="hidden" name="department_code_old" value={editFormData.department_code_old} />
                  
                  <div className="input-field">
                    <input
                      type="text"
                      name="department_new"
                      placeholder="Enter department ID"
                      value={editFormData.department_new}
                      onChange={handleEditInputChange}
                      onKeyDown={handleKeyDown}
                      autoFocus
                      disabled={saving}
                      className={editErrors.department_new ? "error" : ""}
                    />
                    {editErrors.department_new && (
                      <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{editErrors.department_new}</span>
                    )}
                  </div>
                  
                  <div className="input-field">
                    <input
                      type="text"
                      name="department_name_new"
                      placeholder="Enter department name"
                      value={editFormData.department_name_new}
                      onChange={handleEditInputChange}
                      onKeyDown={handleKeyDown}
                      disabled={saving}
                      className={editErrors.department_name_new ? "error" : ""}
                    />
                    {editErrors.department_name_new && (
                      <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{editErrors.department_name_new}</span>
                    )}
                  </div>
                  
                  <div className="input-field">
                    <input
                      type="text"
                      name="department_code_new"
                      placeholder="Enter department code"
                      value={editFormData.department_code_new}
                      onChange={handleEditInputChange}
                      onKeyDown={handleKeyDown}
                      disabled={saving}
                      className={editErrors.department_code_new ? "error" : ""}
                    />
                    {editErrors.department_code_new && (
                      <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{editErrors.department_code_new}</span>
                    )}
                  </div>
                  
                  {editErrors.all && (
                    <div className="center-align">
                      <span className="error-msg" style={{color: 'red', fontSize: '12px'}}>{editErrors.all}</span>
                    </div>
                  )}
                </>
              )}

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleSave}
                  disabled={saving}
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
                  Department cannot be edited or deleted when in use by other Data sets
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