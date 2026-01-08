import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle, FaSearch } from "react-icons/fa";
import styles from "./Department.module.css";
import { API_BASE_URL } from "../../../../../config";
import Swal from "sweetalert2";

const API_ENDPOINTS = {
  GET: "/department",
  ADD: "/add-department",
  UPDATE: "/update-department",
  DELETE: "/delete-department"
};

export default function Department() {
  const [data, setData] = useState([]);
  const [tablesList, setTablesList] = useState([]);
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
  const [editId, setEditId] = useState(null);
  const [editIndex, setEditIndex] = useState(null);
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState({});
  const [editErrors, setEditErrors] = useState({});

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

      const departmentList = json?.departmentDetails?.dList1?.map((item) => ({
        id: item.id || Date.now() + Math.random(),
        department: item.department,
        department_name: item.department_name,
        contract_id_code: item.contract_id_code,
        counts: json?.departmentDetails?.countList?.filter(count => count.department === item.department) || []
      })) || [];

      const tables = json?.departmentDetails?.tablesList?.map(item => ({
        tName: item.tName,
        captiliszedTableName: item.tName ? item.tName.replace(/_/g, ' ') : ''
      })) || [];

      setData(departmentList.filter((i) => i.department));
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
  const validateInput = () => {
    const newErrors = {};
    const trimmedDept = formData.department.trim();
    const trimmedName = formData.department_name.trim();
    const trimmedCode = formData.contract_id_code.trim();
    
    if (!trimmedDept) newErrors.department = "Department ID is required";
    if (!trimmedName) newErrors.department_name = "Department Name is required";
    if (!trimmedCode) newErrors.contract_id_code = "Department Code is required";
    
    if (mode === "add") {
      const exists = data.some(item => 
        item.department.toLowerCase() === trimmedDept.toLowerCase() ||
        item.department_name.toLowerCase() === trimmedName.toLowerCase() ||
        item.contract_id_code.toLowerCase() === trimmedCode.toLowerCase()
      );
      
      if (exists) {
        const deptExists = data.some(item => item.department.toLowerCase() === trimmedDept.toLowerCase());
        const nameExists = data.some(item => item.department_name.toLowerCase() === trimmedName.toLowerCase());
        const codeExists = data.some(item => item.contract_id_code.toLowerCase() === trimmedCode.toLowerCase());
        
        if (deptExists && nameExists && codeExists) {
          newErrors.all = `"${trimmedDept}" & "${trimmedName}" & "${trimmedCode}" already exist`;
        } else {
          if (deptExists) newErrors.department = `"${trimmedDept}" already exists`;
          if (nameExists) newErrors.department_name = `"${trimmedName}" already exists`;
          if (codeExists) newErrors.contract_id_code = `"${trimmedCode}" already exists`;
        }
      }
    } else if (mode === "edit") {
      const exists = data.some((item, index) => 
        index !== editIndex && (
          item.department.toLowerCase() === trimmedDept.toLowerCase() ||
          item.department_name.toLowerCase() === trimmedName.toLowerCase() ||
          item.contract_id_code.toLowerCase() === trimmedCode.toLowerCase()
        )
      );
      
      if (exists) {
        const deptExists = data.some((item, index) => 
          index !== editIndex && item.department.toLowerCase() === trimmedDept.toLowerCase()
        );
        const nameExists = data.some((item, index) => 
          index !== editIndex && item.department_name.toLowerCase() === trimmedName.toLowerCase()
        );
        const codeExists = data.some((item, index) => 
          index !== editIndex && item.contract_id_code.toLowerCase() === trimmedCode.toLowerCase()
        );
        
        if (deptExists && nameExists && codeExists) {
          newErrors.all = `"${trimmedDept}" & "${trimmedName}" & "${trimmedCode}" already exist`;
        } else {
          if (deptExists) newErrors.department = `"${trimmedDept}" already exists`;
          if (nameExists) newErrors.department_name = `"${trimmedName}" already exists`;
          if (codeExists) newErrors.contract_id_code = `"${trimmedCode}" already exists`;
        }
      }
    }
    
    return newErrors;
  };

  const validateEditInput = () => {
    const newErrors = {};
    const trimmedDept = editFormData.department_new.trim();
    const trimmedName = editFormData.department_name_new.trim();
    const trimmedCode = editFormData.department_code_new.trim();
    
    if (!trimmedDept) newErrors.department_new = "Department ID is required";
    if (!trimmedName) newErrors.department_name_new = "Department Name is required";
    if (!trimmedCode) newErrors.department_code_new = "Department Code is required";
    
    const exists = data.some((item, index) => 
      index !== editIndex && (
        item.department.toLowerCase() === trimmedDept.toLowerCase() ||
        item.department_name.toLowerCase() === trimmedName.toLowerCase() ||
        item.contract_id_code.toLowerCase() === trimmedCode.toLowerCase()
      )
    );
    
    if (exists) {
      const deptExists = data.some((item, index) => 
        index !== editIndex && item.department.toLowerCase() === trimmedDept.toLowerCase()
      );
      const nameExists = data.some((item, index) => 
        index !== editIndex && item.department_name.toLowerCase() === trimmedName.toLowerCase()
      );
      const codeExists = data.some((item, index) => 
        index !== editIndex && item.contract_id_code.toLowerCase() === trimmedCode.toLowerCase()
      );
      
      if (deptExists && nameExists && codeExists) {
        newErrors.all = `"${trimmedDept}" & "${trimmedName}" & "${trimmedCode}" already exist`;
      } else {
        if (deptExists) newErrors.department_new = `"${trimmedDept}" already exists`;
        if (nameExists) newErrors.department_name_new = `"${trimmedName}" already exists`;
        if (codeExists) newErrors.department_code_new = `"${trimmedCode}" already exists`;
      }
    }
    
    return newErrors;
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
    if (item.counts && item.counts.length > 0) {
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

        setData((prev) => [
          {
            id: result.id || Date.now() + Math.random(),
            department: formData.department.trim(),
            department_name: formData.department_name.trim(),
            contract_id_code: formData.contract_id_code.trim(),
            counts: []
          },
          ...prev
        ]);

        setShowModal(false);
        setFormData({ department: "", department_name: "", contract_id_code: "" });
        setErrors({});
        
        Swal.fire('Success!', 'Department added successfully', 'success');
      } catch (error) {
        console.error(error);
        Swal.fire('Error', 'Failed to add department', 'error');
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

        setData((prev) =>
          prev.map((item, index) =>
            index === editIndex
              ? { 
                  ...item, 
                  department: editFormData.department_new.trim(),
                  department_name: editFormData.department_name_new.trim(),
                  contract_id_code: editFormData.department_code_new.trim()
                }
              : item
          )
        );

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
        
        Swal.fire('Success!', 'Department updated successfully', 'success');
      } catch (error) {
        console.error(error);
        Swal.fire('Error', 'Failed to update department', 'error');
      } finally {
        setSaving(false);
      }
    }
  };

  /* ================= FILTER DATA ================= */
  const filteredData = data.filter((item) =>
    item.department?.toLowerCase().includes(search.toLowerCase()) ||
    item.department_name?.toLowerCase().includes(search.toLowerCase()) ||
    item.contract_id_code?.toLowerCase().includes(search.toLowerCase())
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
  const handleKeyDown = (e, formType) => {
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
          <h2 className="center-align">Department Management</h2>
        </div>

        <div className="innerPage">
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
              <div className="center-align p-20">Loading departments...</div>
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
                              onClick={() => handleDeleteClick(item, index)}
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
                      onKeyDown={(e) => handleKeyDown(e, 'add')}
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
                      onKeyDown={(e) => handleKeyDown(e, 'add')}
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
                      onKeyDown={(e) => handleKeyDown(e, 'add')}
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
                      onKeyDown={(e) => handleKeyDown(e, 'edit')}
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
                      onKeyDown={(e) => handleKeyDown(e, 'edit')}
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
                      onKeyDown={(e) => handleKeyDown(e, 'edit')}
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