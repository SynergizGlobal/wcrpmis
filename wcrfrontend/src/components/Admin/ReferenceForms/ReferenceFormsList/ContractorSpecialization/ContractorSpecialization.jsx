import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle } from "react-icons/fa";
import "react-toastify/dist/ReactToastify.css";
import styles from "./ContractorSpecialization.module.css";
import { API_BASE_URL } from "../../../../../config";
import Swal from "sweetalert2";

const API_ENDPOINTS = {
  GET: "/contractor-specialization",
  ADD: "/add-contractor-specialization",
  UPDATE: "/update-contractor-specialization",
  DELETE: "/delete-contractor-specialization"
};

export default function ContractorSpecialization() {
  const [data, setData] = useState([]);
  const [allData, setAllData] = useState([]);
  const [tablesList, setTablesList] = useState([]);
  const [countList, setCountList] = useState([]);
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
  const [showSuccess, setShowSuccess] = useState(false);
  const [showError, setShowError] = useState(false);
  const [message, setMessage] = useState("");

  /* ================= FETCH DATA ================= */
  const fetchData = async () => {
    try {
      setLoading(true);
      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.GET}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json"
        }
      });

      const json = await response.json();
      console.log("Full API Response:", json);

      if (json?.success && json?.contractorSpecializationDetails) {
        const { dList1 = [], dList = [], tablesList = [], countList = [] } = json.contractorSpecializationDetails;
        
        console.log("dList1:", dList1);
        console.log("dList:", dList);
        console.log("tablesList:", tablesList);
        console.log("countList:", countList);

        // Process dList1 for display (main table data)
        const displayData = dList1
          .filter(item => item.contractor_specialization)
          .map((item, index) => ({
            id: index,
            contractor_specialization: item.contractor_specialization
          }));

        // Process dList for delete operations
        const allItems = dList
          .filter(item => item.contractor_specialization)
          .map((item, index) => ({
            id: index + 1000, // Different ID range to avoid conflicts
            contractor_specialization: item.contractor_specialization
          }));

        // Process tablesList for table headers
        const processedTables = tablesList
          .filter(table => table.captiliszedTableName)
          .map((table, index) => ({
            id: index,
            tName: table.tName,
            captiliszedTableName: table.captiliszedTableName
          }));

        // Process countList for count display
        const processedCounts = countList
          .filter(count => count.contractor_specialization && count.tName && count.count)
          .map(count => ({
            contractor_specialization: count.contractor_specialization,
            tName: count.tName,
            count: count.count
          }));

        setData(displayData);
        setAllData(allItems);
        setTablesList(processedTables);
        setCountList(processedCounts);

        console.log("Processed Data:", {
          displayData,
          allItems,
          processedTables,
          processedCounts
        });
      } else {
        console.error("Invalid API response structure");
        setData([]);
        setAllData([]);
        setTablesList([]);
        setCountList([]);
      }
    } catch (error) {
      console.error("Fetch error:", error);
      setData([]);
      setAllData([]);
      setTablesList([]);
      setCountList([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* ================= GET COUNT FOR TABLE CELL ================= */
  const getCountForCell = (contractorSpecialization, tableName) => {
    const countObj = countList.find(count => 
      count.contractor_specialization === contractorSpecialization && 
      count.tName === tableName
    );
    
    return countObj ? `(${countObj.count})` : "";
  };

  /* ================= CHECK IF ITEM HAS COUNTS ================= */
  const hasCounts = (contractorSpecialization) => {
    return countList.some(count => 
      count.contractor_specialization === contractorSpecialization
    );
  };

  /* ================= CHECK IF ITEM IS IN ALLDATA ================= */
  const isInAllData = (contractorSpecialization) => {
    return allData.some(item => 
      item.contractor_specialization === contractorSpecialization
    );
  };

  /* ================= VALIDATION ================= */
  const validateInput = (inputValue, isUpdate = false, currentOldValue = "") => {
    const errors = {};
    const trimmedValue = inputValue.trim();
    
    if (!trimmedValue) {
      errors.value = "Contractor Specialization is required";
      return errors;
    }
    
    const checkData = isUpdate ? 
      data.filter(item => item.contractor_specialization !== currentOldValue) : 
      data;
    
    const exists = checkData.some(item => 
      item.contractor_specialization.toLowerCase() === trimmedValue.toLowerCase()
    );
    
    if (exists) {
      errors.value = `${trimmedValue} already exists`;
    }
    
    return errors;
  };

  const validateOnType = (value, isUpdate = false) => {
    const errors = validateInput(value, isUpdate, oldValue);
    if (isUpdate) {
      setUpdateErrors(errors);
    } else {
      setErrors(errors);
    }
  };

  /* ================= HANDLE INPUT CHANGE ================= */
  const handleInputChange = (e) => {
    const newValue = e.target.value;
    setValue(newValue);
    validateOnType(newValue, false);
  };

  const handleUpdateInputChange = (e) => {
    const newValue = e.target.value;
    setUpdateValue(newValue);
    validateOnType(newValue, true);
  };

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setValue("");
    setErrors({});
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const handleEditClick = (item) => {
    if (hasCounts(item.contractor_specialization)) {
      Swal.fire({
        title: 'Error',
        text: 'Reference data cannot be edited when in use by other Data sets',
        icon: 'error',
        confirmButtonText: 'OK'
      });
      return;
    }

    setUpdateValue(item.contractor_specialization);
    setOldValue(item.contractor_specialization);
    setUpdateErrors({});
    setShowUpdateModal(true);
  };

  /* ================= DELETE ================= */
  const handleDeleteClick = async (item) => {
    if (hasCounts(item.contractor_specialization)) {
      Swal.fire({
        title: 'Error',
        text: 'Reference data cannot be deleted when in use by other Data sets',
        icon: 'error',
        confirmButtonText: 'OK'
      });
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

    if (result.isConfirmed) {
      try {
        const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.DELETE}`, {
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            contractor_specialization: item.contractor_specialization
          })
        });

        const result = await response.json();
        console.log("Delete response:", result);

        if (result.success) {
          setData(prev => prev.filter(i => i.contractor_specialization !== item.contractor_specialization));
          setAllData(prev => prev.filter(i => i.contractor_specialization !== item.contractor_specialization));
          setCountList(prev => prev.filter(i => i.contractor_specialization !== item.contractor_specialization));
          
          Swal.fire('Deleted!', 'Record has been deleted', 'success');
        } else {
          throw new Error(result.message || "Delete failed");
        }
      } catch (error) {
        console.error(error);
        Swal.fire('Error', 'Failed to delete contractor specialization', 'error');
      }
    } else {
      Swal.fire('Cancelled', 'Record is safe :)', 'error');
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
          contractor_specialization: value.trim()
        })
      });

      const result = await response.json();
      console.log("Add response:", result);

      if (result.success) {
        const newItem = { 
          id: Date.now(), 
          contractor_specialization: value.trim()
        };
        
        setData(prev => [newItem, ...prev]);
        setAllData(prev => [newItem, ...prev]);

        setShowModal(false);
        setValue("");
        setErrors({});
        
        setMessage('Contractor Specialization added successfully');
        setShowSuccess(true);
        setTimeout(() => setShowSuccess(false), 3000);
      } else {
        throw new Error(result.message || "Operation failed");
      }
    } catch (error) {
      console.error(error);
      setMessage('Failed to add contractor specialization');
      setShowError(true);
      setTimeout(() => setShowError(false), 3000);
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
          contractor_specialization_old: oldValue,
          contractor_specialization_new: updateValue.trim()
        })
      });

      const result = await response.json();
      console.log("Update response:", result);

      if (result.success) {
        const updateItemInArray = (array) => 
          array.map(item => 
            item.contractor_specialization === oldValue
              ? { ...item, contractor_specialization: updateValue.trim() }
              : item
          );

        setData(prev => updateItemInArray(prev));
        setAllData(prev => updateItemInArray(prev));
        setCountList(prev => 
          prev.map(item => 
            item.contractor_specialization === oldValue
              ? { ...item, contractor_specialization: updateValue.trim() }
              : item
          )
        );

        setShowUpdateModal(false);
        setUpdateValue("");
        setOldValue("");
        setUpdateErrors({});
        
        setMessage('Contractor Specialization updated successfully');
        setShowSuccess(true);
        setTimeout(() => setShowSuccess(false), 3000);
      } else {
        throw new Error(result.message || "Operation failed");
      }
    } catch (error) {
      console.error(error);
      setMessage('Failed to update contractor specialization');
      setShowError(true);
      setTimeout(() => setShowError(false), 3000);
    } finally {
      setSaving(false);
    }
  };

  /* ================= FILTER DATA ================= */
  const filteredData = data.filter((item) =>
    item.contractor_specialization?.toLowerCase().includes(search.toLowerCase())
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

  const removeErrorMsg = () => {
    setUpdateErrors({});
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
          <h2 className="center-align">Contractor Specialization</h2>
        </div>

        <div className="innerPage">
          {/* Success/Error Messages */}
          {showSuccess && (
            <div className="center-align m-1 close-message" style={{color: 'green', padding: '10px', margin: '10px 0'}}>
              {message}
            </div>
          )}
          {showError && (
            <div className="center-align m-1 close-message" style={{color: 'red', padding: '10px', margin: '10px 0'}}>
              {message}
            </div>
          )}

          <div className={styles.topActions}>
            <button
              className="btn btn-primary"
              onClick={handleAddClick}
              disabled={loading}
            >
              <FaPlusCircle /> &nbsp; Add Contractor Specialization
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search contractor specializations..."
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
              <div className="center-align p-20">Loading contractor specializations...</div>
            ) : (
              <table className={styles.table}>
                <thead>
                  <tr>
                    <th>Contractor Specialization</th>
                    {tablesList.map((table) => (
                      <th key={table.id}>{table.captiliszedTableName}</th>
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
                            value={item.contractor_specialization} 
                            data-index={index + 1}
                          />
                          {item.contractor_specialization}
                        </td>
                        {tablesList.map((table) => (
                          <td key={`${item.id}-${table.id}`}>
                            {getCountForCell(item.contractor_specialization, table.tName)}
                          </td>
                        ))}
                        <td className={styles.actionCol}>
                          <div className={styles.actionButtons}>
                            <button
                              className={styles.editBtn}
                              onClick={() => handleEditClick(item)}
                              title="Edit"
                              disabled={hasCounts(item.contractor_specialization)}
                            >
                              <FaEdit />
                            </button>
                            {isInAllData(item.contractor_specialization) && (
                              <button
                                className={styles.deleteBtn}
                                onClick={() => handleDeleteClick(item)}
                                title="Delete"
                                disabled={hasCounts(item.contractor_specialization)}
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
                        {search ? "No matching results found" : "No contractor specializations available"}
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
              <span>Add Contractor Specialization</span>
              <FaTimes
                className={styles.close}
                onClick={handleModalClose}
                title="Close"
              />
            </div>

            <div className={styles.modalBody}>
              <div className={styles.inputContainer}>
                <input
                  type="text"
                  id="contractor_specialization_text"
                  placeholder=" "
                  value={value}
                  onChange={handleInputChange}
                  onKeyDown={handleKeyDown}
                  autoFocus
                  disabled={saving}
                  className={errors.value ? styles.errorInput : ""}
                />
                <label htmlFor="contractor_specialization_text">Contractor Specialization</label>
                {errors.value && (
                  <span id="contractor_specializationError" className={styles.errorMsg}>
                    {errors.value}
                  </span>
                )}
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleSave}
                  disabled={saving || !value.trim() || errors.value}
                  id="bttn"
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
              <span>Update Contractor Specialization</span>
              <FaTimes
                className={styles.close}
                onClick={() => {
                  handleUpdateModalClose();
                  removeErrorMsg();
                }}
                title="Close"
              />
            </div>

            <div className={styles.modalBody}>
              <input 
                type="hidden" 
                id="contractor_specialization_old" 
                value={oldValue} 
              />
              <div className={styles.inputContainer}>
                <input
                  type="text"
                  id="contractor_specialization_new"
                  placeholder=" "
                  value={updateValue}
                  onChange={handleUpdateInputChange}
                  onKeyDown={handleUpdateKeyDown}
                  autoFocus
                  disabled={saving}
                  className={updateErrors.value ? styles.errorInput : ""}
                />
                <label htmlFor="contractor_specialization_new">Contractor Specialization</label>
                {updateErrors.value && (
                  <span id="contractor_specialization_newError" className={styles.errorMsg}>
                    {updateErrors.value}
                  </span>
                )}
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleUpdate}
                  disabled={saving || !updateValue.trim() || updateErrors.value}
                  id="bttnUpdate"
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
    </div>
  );
}