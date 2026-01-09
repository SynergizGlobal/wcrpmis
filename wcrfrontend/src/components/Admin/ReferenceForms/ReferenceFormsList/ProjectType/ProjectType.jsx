import React, { useState, useEffect, useCallback } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle, FaSearch } from "react-icons/fa";
import styles from "./ProjectType.module.css";
import { API_BASE_URL } from "../../../../../config";
import Swal from "sweetalert2";

const API_ENDPOINTS = {
  GET: "/project-types",
  ADD: "/add-project-type",
  UPDATE: "/update-project-type",
  DELETE: "/delete-project-type"
};

export default function ProjectType() {
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [value, setValue] = useState("");
  const [editValue, setEditValue] = useState("");
  const [editOldValue, setEditOldValue] = useState("");
  const [mode, setMode] = useState("add");
  const [editIndex, setEditIndex] = useState(null);
  const [saving, setSaving] = useState(false);
  const [errors, setErrors] = useState({});
  const [editErrors, setEditErrors] = useState({});
  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [search, setSearch] = useState("");

  /* ================= FETCH DATA ================= */
  const fetchData = useCallback(async () => {
    try {
      setLoading(true);

      const response = await fetch(
        `${API_BASE_URL}${API_ENDPOINTS.GET}`,
        {
          method: "POST",
          headers: { "Content-Type": "application/json" }
        }
      );

      if (!response.ok) {
        throw new Error("Failed to fetch");
      }

      const json = await response.json(); // ✅ JSON

      console.log("API Response:", json);

      const projectTypeList = json.map((item, index) => ({
        id: item.projectTypeId || index + 1,
        project_type: item.projectType || item.project_type,
      }));

      setData(projectTypeList);
      setErrorMessage("");
    } catch (error) {
      console.error(error);
      setErrorMessage("Failed to load project types");
      setData([]);
    } finally {
      setLoading(false);
    }
  }, []);


  useEffect(() => {
    fetchData();
  }, [fetchData]);

  /* ================= VALIDATION ================= */
  const validateInput = (inputValue) => {
    const newErrors = {};
    const trimmedValue = inputValue.trim();
    
    if (!trimmedValue) {
      newErrors.project_type = "Project Type is required";
      return newErrors;
    }
    
    // Check for duplicates (like JSP's doValidate function)
    if (mode === "add") {
      const exists = data.some(item => 
        item.project_type.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        newErrors.project_type = `${trimmedValue} already exists`;
      }
    }
    
    return newErrors;
  };

  const validateEditInput = (inputValue) => {
    const newErrors = {};
    const trimmedValue = inputValue.trim();
    
    if (!trimmedValue) {
      newErrors.value_new = "Project Type is required";
      return newErrors;
    }
    
    // Check for duplicates excluding current item (like JSP's doValidateUpdate)
    if (mode === "edit") {
      const exists = data.some((item, index) => 
        index !== editIndex && 
        item.project_type.toLowerCase() === trimmedValue.toLowerCase()
      );
      if (exists) {
        newErrors.value_new = `${trimmedValue} already exists`;
      }
    }
    
    return newErrors;
  };

  /* ================= HANDLE INPUT CHANGE ================= */
  const handleInputChange = (e) => {
    const newValue = e.target.value;
    setValue(newValue);
    
    if (errors.project_type) {
      setErrors({});
    }
  };

  const handleEditInputChange = (e) => {
    const newValue = e.target.value;
    setEditValue(newValue);
    
    if (editErrors.value_new) {
      setEditErrors({});
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
  const handleEditClick = (index) => {
    const item = data[index];
    setMode("edit");
    setEditValue(item.project_type);
    setEditOldValue(item.project_type);
    setEditIndex(index);
    setEditErrors({});
    setShowUpdateModal(true);
  };

  /* ================= DELETE ================= */
  const handleDeleteClick = async (index) => {
    const item = data[index];

    const result = await Swal.fire({
      title: "Are you sure?",
      text: "This record will be deleted!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#DD6B55",
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "No, cancel"
    });

    if (!result.isConfirmed) return;

    try {
      const formData = new FormData();
      formData.append("project_type", item.project_type);

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.DELETE}`, {
        method: "POST",
        body: formData
      });

      const json = await response.json();
      console.log("Delete response:", json);

      if (!response.ok) {
        throw new Error(json.message || "Delete failed");
      }

      // Update UI
      setData(prev => prev.filter((_, i) => i !== index));

      Swal.fire("Deleted!", json.message, "success");

    } catch (error) {
      console.error(error);
      Swal.fire("Error", error.message || "Failed to delete project type", "error");
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

      const formData = new FormData();
      formData.append("project_type", value.trim());

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.ADD}`, {
        method: "POST",
        body: formData
      });

      const json = await response.json();
      console.log("Add response:", json);

      if (!response.ok) {
        throw new Error(json.message || "Failed to add project type");
      }

      // Refresh list
      await fetchData();

      setShowModal(false);
      setValue("");
      setErrors({});
      setSuccessMessage(json.message);

    } catch (error) {
      console.error(error);
      setErrorMessage(error.message || "Failed to add project type");
    } finally {
      setSaving(false);
    }
  };

  /* ================= SAVE (UPDATE) ================= */
  const handleUpdate = async () => {
    const validationErrors = validateEditInput(editValue);

    if (Object.keys(validationErrors).length > 0) {
      setEditErrors(validationErrors);
      return;
    }

    try {
      setSaving(true);

      const formData = new FormData();
      formData.append("value_new", editValue.trim());
      formData.append("value_old", editOldValue.trim());

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.UPDATE}`, {
        method: "POST",
        body: formData
      });

      const json = await response.json();
      console.log("Update response:", json);

      if (!response.ok) {
        throw new Error(json.message || "Failed to update project type");
      }

      await fetchData();

      setShowUpdateModal(false);
      setEditValue("");
      setEditOldValue("");
      setEditErrors({});
      setSuccessMessage(json.message);

    } catch (error) {
      console.error(error);
      setErrorMessage(error.message || "Failed to update project type");
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
      setEditValue("");
      setEditOldValue("");
      setEditErrors({});
    }
  };

  /* ================= KEY HANDLER ================= */
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      if (mode === "add") {
        handleSave();
      } else {
        handleUpdate();
      }
    } else if (e.key === 'Escape') {
      if (mode === "add") {
        handleModalClose();
      } else {
        handleUpdateModalClose();
      }
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
    item.project_type?.toLowerCase().includes(search.toLowerCase())
  );

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Project Type</h2>
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
              <FaPlusCircle /> &nbsp; Add Project Type
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search project types..."
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
                    <th>Project Type</th>
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
                            value={item.project_type} 
                          />
                          {item.project_type}
                        </td>
                        <td className={styles.actionCol}>
                          <div className={styles.actionButtons}>
                            <button
                              className={styles.editBtn}
                              onClick={() => handleEditClick(index)}
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
                      <td colSpan={2} className="center-align p-20">
                        {search ? "No matching results found" : "No project types available"}
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
              <span>Add Project Type</span>
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
                  id="project_type"
                  name="project_type"
                  placeholder="Enter project type"
                  value={value}
                  onChange={handleInputChange}
                  onKeyDown={handleKeyDown}
                  autoFocus
                  disabled={saving}
                  className={errors.project_type ? "error" : ""}
                />
                <label htmlFor="project_type">Project Type</label>
                {errors.project_type && (
                  <span id="project_typeError" className="error-msg" style={{color: 'red', fontSize: '12px'}}>
                    {errors.project_type}
                  </span>
                )}
              </div>

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
              <span>Update Project Type</span>
              <FaTimes
                className={styles.close}
                onClick={handleUpdateModalClose}
                title="Close"
                disabled={saving}
              />
            </div>

            <div className={styles.modalBody}>
              <input type="hidden" id="value_old" value={editOldValue} />
              
              <div className="input-field">
                <input
                  type="text"
                  id="value_new"
                  name="value_new"
                  placeholder="Enter project type"
                  value={editValue}
                  onChange={handleEditInputChange}
                  onKeyDown={handleKeyDown}
                  autoFocus
                  disabled={saving}
                  className={editErrors.value_new ? "error" : ""}
                />
                <label htmlFor="value_new">Project Type</label>
                {editErrors.value_new && (
                  <span id="value_newError" className="error-msg" style={{color: 'red', fontSize: '12px'}}>
                    {editErrors.value_new}
                  </span>
                )}
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleUpdate}
                  disabled={saving || !editValue.trim()}
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