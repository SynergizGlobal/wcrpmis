import React, { useState, useEffect } from "react";
import { FaEdit, FaTrash, FaTimes, FaPlusCircle } from "react-icons/fa";
import "react-toastify/dist/ReactToastify.css";
import styles from "./ContractType.module.css";
import { API_BASE_URL } from "../../../../../config";
import Swal from "sweetalert2";

const API_ENDPOINTS = {
  GET: "/contract-type",
  ADD: "/add-contract-type",
  UPDATE: "/update-contract-type",
  DELETE: "/delete-contract-type"
};

export default function ContractType() {
  const [contractTypeDetails, setContractTypeDetails] = useState({
    dList1: [],
    dList: [],
    tablesList: [],
    countList: []
  });
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [search, setSearch] = useState("");
  const [contract_type, setContract_type] = useState("");
  const [contract_type_new, setContract_type_new] = useState("");
  const [contract_type_old, setContract_type_old] = useState("");
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [success, setSuccess] = useState("");
  const [flag, setFlag] = useState(false);
  const [updateFlag, setUpdateFlag] = useState(true);
  const [errors, setErrors] = useState({});
  const [updateErrors, setUpdateErrors] = useState({});

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

      if (json?.contractTypeDetails) {
        setContractTypeDetails(json.contractTypeDetails);
      } else {
        console.error("Invalid API response structure");
        setContractTypeDetails({
          dList1: [],
          dList: [],
          tablesList: [],
          countList: []
        });
      }
    } catch (error) {
      console.error("Fetch error:", error);
      setContractTypeDetails({
        dList1: [],
        dList: [],
        tablesList: [],
        countList: []
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  /* ================= VALIDATION FUNCTIONS ================= */
  const doValidate = (value) => {
    const print_value = value;
    const trimmedValue = value.trim();
    const lowerValue = trimmedValue.toLowerCase();
    
    // Get all existing contract_type values
    const existingValues = contractTypeDetails.dList1
      .filter(item => item.contract_type)
      .map(item => item.contract_type.toLowerCase());
    
    let isValid = true;
    let errorMsg = "";
    
    if (existingValues.includes(lowerValue)) {
      errorMsg = `${print_value} already exists`;
      isValid = false;
    }
    
    setErrors({ value: errorMsg });
    setFlag(isValid);
    
    return isValid;
  };

  const doValidateUpdate = (value) => {
    const print_value = value;
    const trimmedValue = value.trim();
    const lowerValue = trimmedValue.toLowerCase();
    
    // Get all existing contract_type values except the old one
    const existingValues = contractTypeDetails.dList1
      .filter(item => item.contract_type && item.contract_type !== contract_type_old)
      .map(item => item.contract_type.toLowerCase());
    
    let isValid = true;
    let errorMsg = "";
    
    if (existingValues.includes(lowerValue)) {
      errorMsg = `${print_value} already exists`;
      isValid = false;
    }
    
    setUpdateErrors({ value: errorMsg });
    setUpdateFlag(isValid);
    
    return isValid;
  };

  const removeErrorMsg = () => {
    setUpdateErrors({});
    setUpdateFlag(true);
  };

  /* ================= ADD ================= */
  const handleAddClick = () => {
    setContract_type("");
    setErrors({});
    setFlag(false);
    setShowModal(true);
  };

  /* ================= EDIT ================= */
  const updateRow = (index) => {
    const item = contractTypeDetails.dList1[index];
    if (item && item.contract_type) {
      // Check if item has counts
      const hasCounts = contractTypeDetails.countList.some(
        count => count.contract_type === item.contract_type
      );
      
      if (hasCounts) {
        Swal.fire({
          title: 'Error',
          text: 'Reference data cannot be edited or deleted when in use by other Data sets',
          icon: 'error',
          confirmButtonText: 'OK'
        });
        return;
      }
      
      setContract_type_old(item.contract_type);
      setContract_type_new(item.contract_type);
      setUpdateErrors({});
      setUpdateFlag(true);
      setShowUpdateModal(true);
    }
  };

  /* ================= DELETE ================= */
  const deleteRow = (val) => {
    // Check if item has counts
    const hasCounts = contractTypeDetails.countList.some(
      count => count.contract_type === val
    );
    
    if (hasCounts) {
      Swal.fire({
        title: 'Error',
        text: 'Reference data cannot be edited or deleted when in use by other Data sets',
        icon: 'error',
        confirmButtonText: 'OK'
      });
      return;
    }
    
    showCancelMessage(val);
  };

  const showCancelMessage = (val) => {
    Swal.fire({
      title: "Are you sure?",
      text: "You will be changing the status of the record!",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#DD6B55",
      confirmButtonText: "Yes, delete it!",
      cancelButtonText: "No, cancel it!"
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          setLoading(true);
          
          // Send as TrainingType object with contract_type property
          const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.DELETE}`, {
            method: "DELETE",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
              contract_type: val
            })
          });

          const result = await response.json();
          console.log("Delete response:", result);
          
          if (result.success) {
            // Update the state to remove the deleted item
            setContractTypeDetails(prev => ({
              ...prev,
              dList1: prev.dList1.filter(item => item.contract_type !== val),
              dList: prev.dList.filter(item => item.contract_type !== val),
              countList: prev.countList.filter(item => item.contract_type !== val)
            }));
            
            Swal.fire('Deleted!', result.message || 'Record has been deleted', 'success');
          } else {
            throw new Error(result.message || "Delete failed");
          }
        } catch (error) {
          console.error(error);
          Swal.fire('Error', 'Failed to delete contract type', 'error');
        } finally {
          setLoading(false);
        }
      } else {
        Swal.fire("Cancelled", "Record is safe :)", "error");
      }
    });
  };

  /* ================= SAVE (ADD) ================= */
  const handleSave = async () => {
    if (!flag) {
      return;
    }

    try {
      setSaving(true);

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.ADD}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          contract_type: contract_type.trim()
        })
      });

      const result = await response.json();
      console.log("Add response:", result);

      if (result.success) {
        const newItem = { 
          contract_type: contract_type.trim()
        };
        
        // Add to the beginning of the list
        setContractTypeDetails(prev => ({
          ...prev,
          dList1: [newItem, ...prev.dList1],
          dList: [newItem, ...prev.dList]
        }));

        setShowModal(false);
        setContract_type("");
        setErrors({});
        setFlag(false);
        
        setSuccess('Contract Type added successfully');
        setTimeout(() => setSuccess(""), 3000);
      } else {
        throw new Error(result.message || "Operation failed");
      }
    } catch (error) {
      console.error(error);
      setError('Failed to add contract type');
      setTimeout(() => setError(""), 3000);
    } finally {
      setSaving(false);
    }
  };

  /* ================= UPDATE ================= */
  const handleUpdate = async () => {
    if (!updateFlag) {
      return;
    }

    try {
      setSaving(true);

      console.log("Sending update request with:", {
        value_old: contract_type_old,
        value_new: contract_type_new.trim()
      });

      // Try different property names based on common patterns
      const requestBody = {
        // Try the current pattern
        value_old: contract_type_old,
        value_new: contract_type_new.trim(),
        
        // Also try alternative property names that might match TrainingType
        contract_type_old: contract_type_old,
        contract_type_new: contract_type_new.trim(),
        
        // Try simple property names
        oldValue: contract_type_old,
        newValue: contract_type_new.trim(),
        
        // Try with underscore
        old_value: contract_type_old,
        new_value: contract_type_new.trim()
      };

      const response = await fetch(`${API_BASE_URL}${API_ENDPOINTS.UPDATE}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestBody)
      });

      const result = await response.json();
      console.log("Update response:", result);

      if (result.success) {
        // Update items in all lists
        const updateItemInArray = (array) => 
          array.map(item => 
            item.contract_type === contract_type_old
              ? { ...item, contract_type: contract_type_new.trim() }
              : item
          );

        setContractTypeDetails(prev => ({
          ...prev,
          dList1: updateItemInArray(prev.dList1),
          dList: updateItemInArray(prev.dList),
          countList: updateItemInArray(prev.countList)
        }));

        setShowUpdateModal(false);
        setContract_type_new("");
        setContract_type_old("");
        setUpdateErrors({});
        setUpdateFlag(true);
        
        setSuccess('Contract Type updated successfully');
        setTimeout(() => setSuccess(""), 3000);
      } else {
        // Show more detailed error
        Swal.fire({
          title: 'Update Failed',
          text: result.message || 'Failed to update contract type',
          icon: 'error',
          confirmButtonText: 'OK'
        });
        throw new Error(result.message || "Operation failed");
      }
    } catch (error) {
      console.error("Update error:", error);
      setError('Failed to update contract type: ' + error.message);
      setTimeout(() => setError(""), 3000);
    } finally {
      setSaving(false);
    }
  };

  /* ================= GET COUNT FOR TABLE CELL ================= */
  const getCountForCell = (contractType, tableName) => {
    const countObj = contractTypeDetails.countList.find(count => 
      count.contract_type === contractType && 
      count.tName === tableName
    );
    
    return countObj ? `(${countObj.count})` : "";
  };

  /* ================= CHECK IF ITEM IS IN DLIST ================= */
  const isInDList = (contractType) => {
    return contractTypeDetails.dList.some(item => 
      item.contract_type === contractType
    );
  };

  /* ================= FILTER DATA ================= */
  const filteredData = contractTypeDetails.dList1.filter((item) =>
    item.contract_type?.toLowerCase().includes(search.toLowerCase())
  );

  /* ================= HANDLE INPUT CHANGE ================= */
  const handleInputChange = (e) => {
    const value = e.target.value;
    setContract_type(value);
    doValidate(value);
  };

  const handleUpdateInputChange = (e) => {
    const value = e.target.value;
    setContract_type_new(value);
    doValidateUpdate(value);
  };

  /* ================= KEY HANDLERS ================= */
  const handleKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleSave();
    } else if (e.key === 'Escape') {
      setShowModal(false);
    }
  };

  const handleUpdateKeyDown = (e) => {
    if (e.key === 'Enter') {
      handleUpdate();
    } else if (e.key === 'Escape') {
      setShowUpdateModal(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align">Contract Type</h2>
        </div>

        <div className="innerPage">
          {/* Success/Error Messages */}
          {success && (
            <div className="center-align m-1 close-message">	
              {success}
            </div>
          )}
          {error && (
            <div className="center-align m-1 close-message">
              {error}
            </div>
          )}

          <div className={styles.topActions}>
            <button
              className="btn btn-primary"
              onClick={handleAddClick}
              disabled={loading}
            >
              <FaPlusCircle /> &nbsp; Add Contract Type
            </button>
          </div>

          <div className={styles.searchBox}>
            <input
              type="text"
              placeholder="Search contract types..."
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
                    <th>Contract Type</th>
                    {contractTypeDetails.tablesList.map((tObj, index) => (
                      <th key={index}>{tObj.captiliszedTableName}</th>
                    ))}
                    <th className={styles.actionCol}>Action</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredData.map((obj, indexs) => (
                    <tr key={indexs}>
                      <td>
                        <input 
                          type="hidden" 
                          id={`contract_typeId${indexs + 1}`} 
                          value={obj.contract_type} 
                          className="findLengths"
                        />
                        {obj.contract_type}
                      </td>
                      {contractTypeDetails.tablesList.map((tObj, index) => (
                        <td key={index}>
                          {getCountForCell(obj.contract_type, tObj.tName)}
                        </td>
                      ))}
                      <td className={styles.actionCol}>
                        <div className={styles.actionButtons}>
                          <button
                            className={styles.editBtn}
                            onClick={() => updateRow(indexs)}
                            title="Edit"
                          >
                            <FaEdit />
                          </button>
                          {isInDList(obj.contract_type) && (
                            <button
                              className={styles.deleteBtn}
                              onClick={() => deleteRow(obj.contract_type)}
                              title="Delete"
                            >
                              <FaTrash />
                            </button>
                          )}
                        </div>
                      </td>
                    </tr>
                  ))}
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
              <span>Add Contract Type</span>
              <FaTimes
                className={styles.close}
                onClick={() => setShowModal(false)}
                title="Close"
              />
            </div>

            <div className={styles.modalBody}>
              <div className="row">
                <div className="input-field col s12 m12">
                  <input 
                    id="contract_type_text" 
                    type="text" 
                    value={contract_type}
                    onChange={handleInputChange}
                    onKeyDown={handleKeyDown}
                    className="validate" 
                    autoFocus
                    disabled={saving}
                    style={{width: "100%", padding: "8px", marginBottom: "20px"}}
                  />
                  <label htmlFor="contract_type_text">Contract Type</label>
                  {errors.value && (
                    <span id="contract_typeError" className={styles.errorMsg}>
                      {errors.value}
                    </span>
                  )}
                </div>
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleSave}
                  disabled={saving || !flag}
                  id="bttn"
                >
                  {saving ? "Adding..." : "Add"}
                </button>
                <button
                  className={styles.cancelBtn}
                  onClick={() => setShowModal(false)}
                  disabled={saving}
                >
                  Cancel
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
              <span>Update Contract Type</span>
              <FaTimes
                className={styles.close}
                onClick={() => {
                  setShowUpdateModal(false);
                  removeErrorMsg();
                }}
                title="Close"
              />
            </div>

            <div className={styles.modalBody}>
              <input 
                id="contract_type_old" 
                type="hidden" 
                value={contract_type_old}
              />
              <div className="row">
                <div className="input-field col s12 m12">
                  <input 
                    id="contract_type_new" 
                    type="text" 
                    value={contract_type_new}
                    onChange={handleUpdateInputChange}
                    onKeyDown={handleUpdateKeyDown}
                    className="validate" 
                    autoFocus
                    disabled={saving}
                    style={{width: "100%", padding: "8px", marginBottom: "20px"}}
                  />
                  <label htmlFor="contract_type_new">Contract Type</label>
                  {updateErrors.value && (
                    <span id="contract_type_newError" className={styles.errorMsg}>
                      {updateErrors.value}
                    </span>
                  )}
                </div>
              </div>

              <div className={styles.modalActions}>
                <button 
                  className={styles.saveBtn}
                  onClick={handleUpdate}
                  disabled={saving || !updateFlag}
                  id="bttnUpdate"
                >
                  {saving ? "Updating..." : "Update"}
                </button>
                <button
                  className={styles.cancelBtn}
                  onClick={() => {
                    setShowUpdateModal(false);
                    removeErrorMsg();
                  }}
                  disabled={saving}
                >
                  Cancel
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}