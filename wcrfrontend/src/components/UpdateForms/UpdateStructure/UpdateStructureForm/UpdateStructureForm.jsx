
import React, { useEffect, useState } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import { Outlet } from 'react-router-dom';
import styles from './UpdateStructureForm.module.css';
import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
import { RiAttachment2 } from 'react-icons/ri';

export default function UpdateStructureForm() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const isEdit = Boolean(state?.updateStructureform || state?.structure_id);
  const structureId = state?.structure_id || state?.updateStructureform?.structure_id;
  
  const [dropdownData, setDropdownData] = useState({
    projectsList: [],
    structuresList: [],
    contractsList: [],
    responsiblePeopleList: [],
    workStatusList: [],
    executionStatusList: [],
    fileType: [],
    departmentsList: [],
    unitsList: [],
    loading: true,
    error: null
  });

  const [structureData, setStructureData] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [isPrefilled, setIsPrefilled] = useState(false);

  const {
    register,
    control,
    handleSubmit,
    setValue,
    watch,
    reset,
    formState: { errors },
  } = useForm({
    defaultValues: {
      project_id_fk: "",
      structure_type_fk: "",
      structure_name: "",
      structure: "",
      work_status_fk: "",

      contractExecutionExecutives: [{
        contracts_id_fk: "",
        excecutives: "",
      }],

      target_date: "",
      estimated_cost: "",
      estimated_cost_unit: "", 
      remarks: "",
      latitude: "",
      longitude: "",
      construction_start_date: "",
      revised_completion: "",

      structureDetails: [{
        structure_details: "",
        structure_values: "",
      }],
      
      documents: [{ 
        structure_file_types: "", 
        structureDocumentNames: "", 
        structureFiles: "" 
      }],
    },
  });

  const { fields: contractExecutionExecutivesFields, append: appendContractExecutionExecutives, remove: removeContractExecutionExecutives } = useFieldArray({
    control,
    name: "contractExecutionExecutives",
  });

  const { fields: structureDetailsFields, append: appendStructureDetails, remove: removeStructureDetails } = useFieldArray({
    control,
    name: "structureDetails",
  });

  const { fields: documentsFields, append: appendDocuments, remove: removeDocuments } = useFieldArray({
    control,
    name: "documents",
  });

  // Helper function to prefill form with structure data
  const prefillForm = (apiData) => {
    if (!apiData || isPrefilled) return;
    
    console.log("API Data for prefilling:", apiData);
    
    // Check if we have structure data
    const hasStructureData = apiData.structuresListDetails && apiData.structuresListDetails.length > 0;
    const hasContractData = apiData.structureDetailsTypes && (Array.isArray(apiData.structureDetailsTypes) || apiData.structureDetailsTypes.contracts_id_fk);
    const hasStructureDetails = apiData.structureDetailsLocations && Array.isArray(apiData.structureDetailsLocations);
    
    console.log("Data check:", {
      hasStructureData,
      hasContractData,
      hasStructureDetails,
      structuresListDetails: apiData.structuresListDetails,
      structureDetailsTypes: apiData.structureDetailsTypes,
      structureDetailsLocations: apiData.structureDetailsLocations
    });
    
    // Prepare the form data object
    const formData = {};
    
    // 1. Fill basic fields from structuresListDetails
    if (hasStructureData) {
      const structureDetail = apiData.structuresListDetails[0];
      console.log("Structure detail:", structureDetail);
      
      const basicFields = [
        { key: 'project_id_fk', value: structureDetail.project_id_fk },
        { key: 'structure_type_fk', value: structureDetail.structure_type_fk },
        { key: 'structure_name', value: structureDetail.structure_name },
        { key: 'structure', value: structureDetail.structure },
        { key: 'work_status_fk', value: structureDetail.work_status_fk },
        { key: 'target_date', value: structureDetail.target_date },
        { key: 'estimated_cost', value: structureDetail.estimated_cost },
        { key: 'estimated_cost_unit', value: structureDetail.estimated_cost_unit },
        { key: 'remarks', value: structureDetail.remarks },
        { key: 'latitude', value: structureDetail.latitude },
        { key: 'longitude', value: structureDetail.longitude },
        { key: 'construction_start_date', value: structureDetail.construction_start_date },
        { key: 'revised_completion', value: structureDetail.revised_completion }
      ];
      
      basicFields.forEach(({ key, value }) => {
        if (value !== null && value !== undefined && value !== '') {
          // Format dates for date inputs
          if (key.includes('_date') || key.includes('date')) {
            if (value && typeof value === 'string' && value.includes('/')) {
              // If date is in "dd/MM/yyyy" format, convert to "yyyy-MM-dd"
              const [day, month, year] = value.split('/');
              if (day && month && year) {
                formData[key] = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
              }
            } else if (value) {
              // If already in correct format or timestamp
              const date = new Date(value);
              if (!isNaN(date.getTime())) {
                formData[key] = date.toISOString().split('T')[0];
              } else {
                formData[key] = value;
              }
            }
          } else {
            formData[key] = value;
          }
        }
      });
    }
    
    // 2. Handle contractExecutionExecutives from structureDetailsTypes
    let contractExecutionExecutives = [];
    if (hasContractData) {
      console.log("Processing contract data:", apiData.structureDetailsTypes);
      
      if (Array.isArray(apiData.structureDetailsTypes)) {
        contractExecutionExecutives = apiData.structureDetailsTypes.map(exec => ({
          contracts_id_fk: exec.contracts_id_fk,
          excecutives: exec.excecutives ? exec.excecutives.split(',') : []
        }));
      } else {
        // Handle single object case
        const exec = apiData.structureDetailsTypes;
        contractExecutionExecutives = [{
          contracts_id_fk: exec.contracts_id_fk,
          excecutives: exec.excecutives ? exec.excecutives.split(',') : []
        }];
      }
    }
    
    // 3. Handle structureDetails from structureDetailsLocations
    let structureDetails = [];
    if (hasStructureDetails) {
      console.log("Processing structure details:", apiData.structureDetailsLocations);
      
      structureDetails = apiData.structureDetailsLocations.map(detail => ({
        structure_details: detail.structure_details || '',
        structure_values: detail.structure_values || ''
      }));
    }
    
    // 4. Prepare the complete form data
    const completeFormData = {
      ...formData,
      contractExecutionExecutives: contractExecutionExecutives.length > 0 
        ? contractExecutionExecutives 
        : [{ contracts_id_fk: "", excecutives: "" }],
      structureDetails: structureDetails.length > 0 
        ? structureDetails 
        : [{ structure_details: "", structure_values: "" }],
      documents: [{ structure_file_types: "", structureDocumentNames: "", structureFiles: "" }]
    };
    
    console.log("Complete form data to reset:", completeFormData);
    
    // Use reset to set all form values at once
    reset(completeFormData);
    setIsPrefilled(true);
    
    // Also set the arrays for field arrays
    if (contractExecutionExecutives.length > 0) {
      // Clear existing and add new
      removeContractExecutionExecutives();
      contractExecutionExecutives.forEach(exec => {
        appendContractExecutionExecutives(exec);
      });
    }
    
    if (structureDetails.length > 0) {
      // Clear existing and add new
      removeStructureDetails();
      structureDetails.forEach(detail => {
        appendStructureDetails(detail);
      });
    }
  };

  // Fetch dropdown data and structure data
  useEffect(() => {
    const fetchData = async () => {
		console.log("useEffect triggered");
		console.log("isEdit:", isEdit);
		console.log("structureId:", structureId);

      try {
        setDropdownData(prev => ({ ...prev, loading: true }));
        setIsPrefilled(false);
        
        // Prepare params for API call
        const params = {};
        
        // Always include structure_id if available (for edit mode)
        if (isEdit && structureId) {
          params.structure_id = structureId;
          console.log(`Fetching data for Structure ID: ${structureId}`);
        }
        
        // Fetch dropdown data with parameters
        const dropdownResponse = await api.get(`${API_BASE_URL}/get-structure-form`, {
          params: params
        });
        
        console.log("Full API Response:", dropdownResponse.data);
        
        // Check what data we received
        const hasStructureData = dropdownResponse.data.structuresListDetails && 
                               dropdownResponse.data.structuresListDetails.length > 0;
        
        // Set dropdown data
        setDropdownData({
          projectsList: dropdownResponse.data.projectsList || [],
          structuresList: dropdownResponse.data.structuresList || [],
          contractsList: dropdownResponse.data.contractsList || [],
          responsiblePeopleList: dropdownResponse.data.responsiblePeopleList || [],
          workStatusList: dropdownResponse.data.workStatusList || [],
          executionStatusList: dropdownResponse.data.executionStatusList || [
            "Not Started",
            "In Progress",
            "On Hold",
            "Commissioned",
            "Completed",
            "Dropped"
          ],
          fileType: dropdownResponse.data.fileType || [],
          departmentsList: dropdownResponse.data.departmentsList || [],
          unitsList: dropdownResponse.data.unitsList || [],
          loading: false,
          error: dropdownResponse.data.error || null
        });
        
        // Store structure data
        setStructureData(dropdownResponse.data);
        
        // Prefill form with API data (for both create and edit)
        // Wait for dropdowns to be ready, then prefill
        setTimeout(() => {
          if (hasStructureData || dropdownResponse.data.structureDetailsTypes || dropdownResponse.data.structureDetailsLocations) {
            console.log("Prefilling form with API data");
            prefillForm(dropdownResponse.data);
          } else if (isEdit && structureId) {
            console.log("No structure data found for ID:", structureId);
            alert("No structure data found for the provided ID");
          }
        }, 500);
        
      } catch (err) {
        console.error("Error fetching data:", err);
        setDropdownData(prev => ({
          ...prev,
          loading: false,
          error: err.message
        }));
      }
    };

    fetchData();
  }, [isEdit, structureId]);

  // Format data for Select components
  const formatProjects = dropdownData.projectsList.map(item => ({
    value: item.project_id_fk,
    label: item.project_name
  }));

  const formatStructures = dropdownData.structuresList.map(item => ({
    value: item.structure_type,
    label: item.structure_type
  }));

  const formatContracts = dropdownData.contractsList.map(item => ({
    value: item.contract_id_fk,
    label: `${item.contract_short_name || item.contract_name} (${item.project_name})`
  }));

  // Updated formatResponsiblePeople to show designation first, then user_name
  const formatResponsiblePeople = dropdownData.responsiblePeopleList.map(item => ({
    value: item.user_id,
    label: `${item.designation} - ${item.user_name}`
  }));

  const formatWorkStatus = dropdownData.executionStatusList.map(status => ({
    value: status,
    label: status
  }));

  const formatFileTypes = dropdownData.fileType.map(item => ({
    value: item.value || item.id,
    label: item.label || item.name
  }));

  // Format units for Estimated Cost dropdown
  const formatUnits = dropdownData.unitsList.map(item => ({
    value: item.unit_id || item.value || item,
    label: item.unit || item.label || item
  }));

  // Helper function to format date for backend
  const formatDateForBackend = (dateString) => {
    if (!dateString) return null;
    
    try {
      const date = new Date(dateString);
      if (isNaN(date.getTime())) return dateString;
      
      // Format as "dd/MM/yyyy" for backend
      const day = String(date.getDate()).padStart(2, '0');
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const year = date.getFullYear();
      return `${day}/${month}/${year}`;
    } catch (error) {
      console.error("Error formatting date:", error);
      return dateString;
    }
  };

  // Submit Handler
  const onSubmit = async (data) => {
    setIsSubmitting(true);
    
    try {
      // Format dates for backend
      const formattedData = {
        ...data,
        target_date: formatDateForBackend(data.target_date),
        construction_start_date: formatDateForBackend(data.construction_start_date),
        revised_completion: formatDateForBackend(data.revised_completion),
        
        // Format contractExecutionExecutives
        contractExecutionExecutives: data.contractExecutionExecutives.map(item => ({
          contracts_id_fk: item.contracts_id_fk?.value || item.contracts_id_fk,
          excecutives: Array.isArray(item.excecutives) 
            ? item.excecutives.map(exec => exec.value || exec).join(',')
            : item.excecutives?.value || item.excecutives
        })),
        
        // Format structureDetails
        structureDetails: data.structureDetails.filter(item => 
          item.structure_details || item.structure_values
        ),
        
        // Format documents (handle file uploads separately if needed)
        documents: data.documents.filter(item => 
          item.structure_file_types || item.structureDocumentNames
        ).map(item => ({
          structure_file_types: item.structure_file_types?.value || item.structure_file_types,
          structureDocumentNames: item.structureDocumentNames
        })),
        
        // Convert Select values to strings
        project_id_fk: data.project_id_fk?.value || data.project_id_fk,
        structure_type_fk: data.structure_type_fk?.value || data.structure_type_fk,
        work_status_fk: data.work_status_fk?.value || data.work_status_fk,
        estimated_cost_unit: data.estimated_cost_unit?.value || data.estimated_cost_unit,
        
        // Add structure_id if editing
        ...(isEdit && structureId && {
          structure_id: structureId
        })
      };

      console.log("Submitting data:", formattedData);

      // Use the update-structure-form endpoint for both create and update
      const response = await api.post(`${API_BASE_URL}/update-structure-form`, formattedData);
      
      if (response.data.success) {
        alert("✅ Structure saved successfully!");
        navigate("/wcrpmis/updateforms/structure-form");	
      } else {
        alert(`❌ ${response.data.message || "Failed to save structure"}`);
      }
    } catch (err) {
      console.error("❌ Error saving structure:", err);
      alert(err.response?.data?.error || err.response?.data?.message || "Error saving structure");
    } finally {
      setIsSubmitting(false);
    }
  };

  // Handle file uploads separately if needed
  const handleFileUpload = async (file, index) => {
    const formData = new FormData();
    formData.append('file', file);
    
    try {
      const response = await api.post(`${API_BASE_URL}/upload-structure-document`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        }
      });
      
      if (response.data.success) {
        setValue(`documents.${index}.structureFiles`, response.data.filePath);
        return response.data.filePath;
      }
    } catch (error) {
      console.error("File upload error:", error);
    }
    return null;
  };

  // Watch form values
  const watchRemarks = watch("remarks");
  const watchLatitude = watch("latitude") || "";
  const watchLongitude = watch("longitude") || "";

  if (dropdownData.loading) {
    return (
      <div className={`${styles.container} container-padding`}>
        <div className="card">
          <div className="text-center p-4">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
            <p className="mt-2">Loading form data...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className={`${styles.container} container-padding`}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            {isEdit ? "Update Structure Form" : "Create Structure Form"}
          </h2>
          {isEdit && structureId && (
            <p className="text-center text-muted">Editing Structure ID: {structureId}</p>
          )}
        </div>
        <div className="innerPage">
          <form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">
            {/* Row 1 */}
            <div className="form-row">
              <div className="form-field">
                <label>Project<span className="red">*</span></label>
                <Controller
                  name="project_id_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={formatProjects}
                      placeholder={dropdownData.loading ? "Loading..." : "Select Project"}
                      isSearchable
                      isLoading={dropdownData.loading}
                    />
                  )}
                />
                {errors.project_id_fk && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label> Structure Type <span className="red">*</span></label>
                <Controller
                  name="structure_type_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={formatStructures}
                      placeholder={dropdownData.loading ? "Loading..." : "Select Structure Type"}
                      isSearchable
                      isLoading={dropdownData.loading}
                    />
                  )}
                />
                {errors.structure_type_fk && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label>Structure Name <span className="red">*</span></label>
                <input {...register("structure_name", { required: true })} type="text" placeholder="Enter Value"/>
                {errors.structure_name && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label>Structure ID <span className="red">*</span></label>
                <input {...register("structure", { required: true })} type="text" placeholder="Enter Value"/>
                {errors.structure && <span className="text-danger">Required</span>}
              </div>
            
              <div className="form-field">
                <label>Work Status <span className="red">*</span></label>
                <Controller
                  name="work_status_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      classNamePrefix="react-select"
                      {...field}
                      options={formatWorkStatus}
                      placeholder={dropdownData.loading ? "Loading..." : "Select Work Status"}
                      isSearchable
                      isLoading={dropdownData.loading}
                    />
                  )}
                />
                {errors.work_status_fk && <span className="text-danger">Required</span>}
              </div>
            </div>

            {/* Contract Execution Executives */}
            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">Contract - Execution Executives</h3>

              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "40%" }}>Contract <span className="red">*</span></th>
                      <th style={{ width: "45%" }}>Responsible Executives <span className="red">*</span></th>
                      <th style={{ width: "15%" }}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {contractExecutionExecutivesFields.length > 0 ? (
                      contractExecutionExecutivesFields.map((item, index) => (
                        <tr key={item.id}>
                          <td>
                            <Controller
                              name={`contractExecutionExecutives.${index}.contracts_id_fk`}
                              control={control}
                              rules={{ required: "Required" }}
                              render={({ field }) => (
                                <Select
                                  {...field}
                                  classNamePrefix="react-select"
                                  options={formatContracts}
                                  placeholder="Select Contract"
                                  isSearchable
                                  isClearable
                                  isLoading={dropdownData.loading}
                                />
                              )}
                            />
                            {errors.contractExecutionExecutives?.[index]?.contracts_id_fk && (
                              <span className="red">
                                Required
                              </span>
                            )}
                          </td>
                          <td>
                            <Controller
                              name={`contractExecutionExecutives.${index}.excecutives`}
                              control={control}
                              rules={{ required: "Please select executives" }}
                              render={({ field }) => (
                                <Select
                                  {...field}
                                  options={formatResponsiblePeople}
                                  isMulti
                                  placeholder="Select Executives"
                                  classNamePrefix="react-select"
                                  isLoading={dropdownData.loading}
                                />
                              )}
                            />
                            {errors.contractExecutionExecutives?.[index]?.excecutives && (
                              <span className="red">
                                Required
                              </span>
                            )}
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeContractExecutionExecutives(index)}
                              disabled={isSubmitting}
                            >
                              <MdOutlineDeleteSweep size="26" />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="3" className="text-center text-muted">
                          No contract executives added yet.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>

              <div className="d-flex align-center justify-content-center mt-1">
                <button
                  type="button"
                  className="btn-2 btn-green"
                  onClick={() =>
                    appendContractExecutionExecutives({ 
                      contracts_id_fk: "", 
                      excecutives: "" 
                    })
                  }
                  disabled={isSubmitting}
                >
                  <BiListPlus size="24" />
                </button>
              </div>
            </div>
          
            {/* Basic Information - Reordered as per your image */}
            <div className="form-row">
              {/* Original Target Date */}
              <div className="form-field">
                <label>Original Target Date</label>
                <input 
                  {...register("target_date")} 
                  type="date" 
                  placeholder="Enter Value" 
                  disabled={isSubmitting}
                />
              </div>
            
              {/* Estimated Cost with Unit Select */}
              <div className="form-field">
                <label>Estimated Cost</label>
                <div className={styles["cost-with-unit"]}>
                  <input 
                    {...register("estimated_cost")} 
                    type="number" 
                    placeholder="Enter Value" 
                    disabled={isSubmitting}
                    className={styles["cost-input"]}
                  />
                  <Controller
                    name="estimated_cost_unit"
                    control={control}
                    render={({ field }) => (
                      <Select
                        {...field}
                        classNamePrefix="react-select"
                        options={formatUnits}
                        placeholder="Unit"
                        isSearchable
                        isClearable
                        isLoading={dropdownData.loading}
                        className={styles["unit-select"]}
                        isDisabled={isSubmitting}
                      />
                    )}
                  />
                </div>
              </div>

              {/* Remarks - Full width */}
              <div className="form-field" style={{ flex: "2 1 100%" }}>
                <label>Remarks</label>
                <textarea 
                  {...register("remarks")}
                  name="remarks"
                  maxLength={1000}
                  rows="3"
                  placeholder="Enter remarks..."
                  disabled={isSubmitting}
                ></textarea>
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watchRemarks?.length || 0}/1000
                </div>
              </div>
            </div>

            {/* Latitude & Longitude */}
            <div className="form-row">
              <div className="form-field">
                <label>Latitude</label>
                <input 
                  {...register("latitude")} 
                  type="number" 
                  step="any" 
                  placeholder="Enter Latitude" 
                  disabled={isSubmitting}
                  maxLength={15}
                />
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watchLatitude.length}/15
                </div>
              </div>

              <div className="form-field">
                <label>Longitude</label>
                <input 
                  {...register("longitude")} 
                  type="number" 
                  step="any" 
                  placeholder="Enter Longitude" 
                  disabled={isSubmitting}
                  maxLength={15}
                />
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watchLongitude.length}/15
                </div>
              </div>

              {/* Construction Start Date */}
              <div className="form-field">
                <label>Construction Start Date <span className="red">*</span></label>
                <input 
                  {...register("construction_start_date", { required: true })} 
                  type="date" 
                  placeholder="Enter Value" 
                  disabled={isSubmitting}
                />
                {errors.construction_start_date && <span className="text-danger">Required</span>}
              </div>

              {/* Target Completion Date */}
              <div className="form-field">
                <label>Target Completion Date <span className="red">*</span></label>
                <input 
                  {...register("revised_completion", { required: true })} 
                  type="date" 
                  placeholder="Enter Value" 
                  disabled={isSubmitting}
                />
                {errors.revised_completion && <span className="text-danger">Required</span>}
              </div>
            </div>

            {/* Structure Details */}
            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">Structure Details</h3>

              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "45%" }}>Structure Detail</th>
                      <th style={{ width: "40%" }}>Value</th>
                      <th style={{ width: "15%" }}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {structureDetailsFields.length > 0 ? (
                      structureDetailsFields.map((item, index) => (
                        <tr key={item.id}>
                          <td>
                            <input
                              type="text"
                              {...register(`structureDetails.${index}.structure_details`)}
                              className="form-control"
                              placeholder="Enter detail description"
                              disabled={isSubmitting}
                            />
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(`structureDetails.${index}.structure_values`)}
                              className="form-control"
                              placeholder="Enter value"
                              disabled={isSubmitting}
                            />
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeStructureDetails(index)}
                              disabled={isSubmitting}
                            >
                              <MdOutlineDeleteSweep size="26" />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="3" className="text-center text-muted">
                          No structure details added yet.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>

              <div className="d-flex align-center justify-content-center mt-1">
                <button
                  type="button"
                  className="btn-2 btn-green"
                  onClick={() =>
                    appendStructureDetails({ 
                      structure_details: "", 
                      structure_values: "" 
                    })
                  }
                  disabled={isSubmitting}
                >
                  <BiListPlus size="24" />
                </button>
              </div>
            </div>

            {/* Documents */}
            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">Documents</h3>

              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "25%" }}>File Type</th>
                      <th style={{ width: "35%" }}>Name</th>
                      <th style={{ width: "25%" }}>Attachment</th>
                      <th style={{ width: "15%" }}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {documentsFields.length > 0 ? (
                      documentsFields.map((item, index) => (
                        <tr key={item.id}>
                          <td>
                            <Controller
                              name={`documents.${index}.structure_file_types`}
                              control={control}
                              render={({ field }) => (
                                <Select
                                  {...field}
                                  classNamePrefix="react-select"
                                  options={formatFileTypes}
                                  placeholder="Select File Type"
                                  isSearchable
                                  isClearable
                                  isLoading={dropdownData.loading}
                                  isDisabled={isSubmitting}
                                />
                              )}
                            />
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(`documents.${index}.structureDocumentNames`)}
                              className="form-control"
                              placeholder="File Name"
                              disabled={isSubmitting}
                            />
                          </td>
                          <td>
                            <div className={styles["file-upload-wrapper"]}>
                              <label 
                                htmlFor={`file-${index}`} 
                                className={styles["file-upload-label-icon"]}
                                style={isSubmitting ? { opacity: 0.6, cursor: 'not-allowed' } : {}}>
                                <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
                                {isSubmitting ? 'Uploading...' : 'Upload File'}
                              </label>
                              <input
                                id={`file-${index}`}
                                type="file"
                                {...register(`documents.${index}.structureFiles`)}
                                className={styles["file-upload-input"]}
                                disabled={isSubmitting}
                              />
                              {(watch(`documents.${index}.structureFiles`)?.[0]?.name) && (
                                <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                  {watch(`documents.${index}.structureFiles`)?.[0]?.name}
                                </p>
                              )}
                            </div>
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeDocuments(index)}
                              disabled={isSubmitting}
                            >
                              <MdOutlineDeleteSweep size="26" />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="4" className="text-center text-muted">
                          No documents added yet.
                        </td>
                      </tr>
                    )}
                  </tbody>
                </table>
              </div>

              <div className="d-flex align-center justify-content-center mt-1">
                <button
                  type="button"
                  className="btn-2 btn-green"
                  onClick={() =>
                    appendDocuments({ 
                      structure_file_types: "", 
                      structureDocumentNames: "", 
                      structureFiles: "" 
                    })
                  }
                  disabled={isSubmitting}
                >
                  <BiListPlus size="24" />
                </button>
              </div>
            </div>

            {/* Buttons */}
            <div className="form-post-buttons">
              <button 
                type="submit" 
                className="btn btn-primary"
                disabled={isSubmitting || dropdownData.loading}
              >
                {isSubmitting ? (
                  <>
                    <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                    {isEdit ? "Updating..." : "Saving..."}
                  </>
                ) : (
                  isEdit ? "Update" : "Save"
                )}
              </button>
              <button
                type="button"
                className="btn btn-white"
                onClick={() => navigate(-1)}
                disabled={isSubmitting}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}
