import React, { useEffect, useState, useCallback, useContext } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation, Outlet } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from './UtilityShiftingForm.module.css';

import { API_BASE_URL } from "../../../../config";
import { MdOutlineDeleteSweep } from "react-icons/md";
import { BiListPlus } from "react-icons/bi";
import { RiAttachment2 } from "react-icons/ri";
import { RefreshContext } from "../../../../context/RefreshContext";

export default function UtilityShiftingForm() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const { setRefresh } = useContext(RefreshContext);
  const isEdit = Boolean(state?.design);
  const [isPrefilled, setIsPrefilled] = useState(false);

  const [dropdownData, setDropdownData] = useState({
    projectsList: [],
    contractsList: [],
    utilityTypeList: [],
    utilityCategoryList: [],
    utilityExecutionAgencyList: [],
    impactedContractList: [],
    requirementStageList: [],
    unitList: [],
    utilityshiftingfiletypeList: [],
    statusList: [],
    utilityHODList: [],
    impactedContractsList: [],
    reqStageList: [],
    impactedElementList: []
  });

  const [loading, setLoading] = useState(false);
  const [formLoading, setFormLoading] = useState(false);
  const [editData, setEditData] = useState(null);

  const {
    register,
    control,
    handleSubmit,
    setValue,
    watch,
    formState: { errors },
  } = useForm({
    defaultValues: {
      project_id_fk: "",
      execution_agency_fk: "",
      hod_user_id_fk: "",
      utility_type_fk: "",
      utility_description: "",
      location_name: "",
      custodian: "",
      identification: "",
      reference_number: "",
      executed_by: "",
      chainage: "",
      latitude: "",
      longitude: "",
      impacted_contract_id_fk: "",
      requirement_stage_fk: "",
      impacted_element: "",
      affected_structures: "",
      planned_completion_date: "",
      scope: "",
      completed: "",
      unit_fk: "",
      start_date: "",
      shifting_status_fk: "",
      shifting_completion_date: "",
      progressDetails: [],
      attachments: [],
      remarks: ""
    },
  });

  const { fields: progressDetailsFields, append: appendProgressDetails, remove: removeProgressDetails } = useFieldArray({
    control,
    name: "progressDetails",
  });

  const { fields: attachmentsFields, append: appendAttachments, remove: removeAttachments } = useFieldArray({
    control,
    name: "attachments",
  });

  // Helper function to convert dd-MM-yyyy to yyyy-MM-dd
  const convertToHTMLDate = useCallback((dateString) => {
    if (!dateString) return "";
    const parts = dateString.split('-');
    if (parts.length === 3) {
      return `${parts[2]}-${parts[1]}-${parts[0]}`; // yyyy-MM-dd
    }
    return dateString;
  }, []);

  // Fetch dropdown data
  useEffect(() => {
    const fetchDropdownData = async () => {
      try {
        setLoading(true);
        const response = await api.post(
          `${API_BASE_URL}/utility-shifting/ajax/form/add-utility-shifting`, 
          {}
        );
        setDropdownData(response.data || {});
      } catch (err) {
        console.error("❌ Error fetching dropdown data:", err);
        alert("Error loading form data");
      } finally {
        setLoading(false);
      }
    };

    fetchDropdownData();
  }, []);

  // Fetch edit data when in edit mode
  useEffect(() => {
    const fetchEditData = async () => {
      if (isEdit && state?.design?.utility_shifting_id && !editData) {
        try {
          setLoading(true);
          const response = await api.post(
            `${API_BASE_URL}/utility-shifting/ajax/form/get-utility-shifting/get-utility-shifting`,
            {
              utility_shifting_id: state.design.utility_shifting_id
            }
          );
          setEditData(response.data);
          console.log("Edit data structure:", response.data);
        } catch (err) {
          console.error("❌ Error fetching edit data:", err);
          alert("Error loading edit data");
        } finally {
          setLoading(false);
        }
      }
    };

    fetchEditData();
  }, [isEdit, state, editData]);

  // Prefill form with edit data - ONLY ONCE when both editData and dropdownData are available
  useEffect(() => {
    if (isEdit && editData && Object.keys(dropdownData).length > 0 && !isPrefilled) {
      prefillFormData(editData);
      setIsPrefilled(true);
    }
  }, [editData, dropdownData, isEdit, isPrefilled]);

  // Improved helper to prefilling select fields
  const prefillSelectField = useCallback((fieldName, config, design) => {
    if (!config || !config.data || design === undefined || design === null) return;

    const { data, valueKey, labelKey, formatLabel, getDesignLabel } = config;

    console.log(`🔍 Prefilling ${fieldName}:`, { 
      designValue: design[fieldName], 
      valueKeyValue: design[valueKey],
      availableOptions: data 
    });

    // Get design value - try multiple possible field names
    let designValue = design[fieldName] ?? design[valueKey];
    
    // Special handling for project_id_fk - check common alternative field names
    if (fieldName === 'project_id_fk' && !designValue) {
      designValue = design.project_id || design.projectId || design.project_id_fk;
    }

    if (designValue === undefined || designValue === null) {
      console.log(`❌ No value found for ${fieldName}`);
      return;
    }

    console.log(`🎯 Looking for match for ${fieldName} with value:`, designValue);

    // Find option by matching valueKey
    const option = data?.find(item => {
      const itemVal = item?.[valueKey];
      try {
        if (itemVal !== undefined && designValue !== undefined && 
            itemVal?.toString() === designValue?.toString()) {
          console.log(`✅ Found match for ${fieldName}:`, item);
          return true;
        }
      } catch (e) {
        console.log(`❌ Error comparing values for ${fieldName}:`, e);
      }
      return false;
    });

    if (option) {
      const labelCandidate = getDesignLabel ? getDesignLabel(design) 
                            : (formatLabel ? formatLabel(option) 
                            : (option[labelKey] ?? String(option[valueKey])));

      // Ensure label is a primitive string/number
      const label = (labelCandidate && typeof labelCandidate === 'object')
        ? String(labelCandidate[labelKey] ?? labelCandidate.value ?? '')
        : String(labelCandidate ?? '');

      const value = option[valueKey];
      setValue(fieldName, { value, label });
      console.log(`✅ Successfully prefilled ${fieldName}:`, { value, label });
    } else {
      console.warn(`❌ No option found for ${fieldName} with value:`, designValue);
      console.log('Available options:', data);
      
      // Fallback: create option from design data
      let fallbackLabel = null;
      if (getDesignLabel) {
        try { fallbackLabel = getDesignLabel(design); } catch (e) { fallbackLabel = null; }
      }
      if (!fallbackLabel && labelKey && design && design[labelKey]) fallbackLabel = design[labelKey];
      if (!fallbackLabel && designValue !== undefined) fallbackLabel = designValue;

      const safeLabel = (fallbackLabel && typeof fallbackLabel === 'object')
        ? String(fallbackLabel[labelKey] ?? fallbackLabel.value ?? '')
        : String(fallbackLabel ?? '');

      const safeValue = (designValue !== undefined && typeof designValue !== 'object') 
        ? designValue 
        : (design && design[valueKey] ? design[valueKey] : '');

      if (safeValue && safeLabel) {
        setValue(fieldName, {
          value: safeValue,
          label: safeLabel
        });
        console.log(`🔄 Used fallback for ${fieldName}:`, { value: safeValue, label: safeLabel });
      }
    }
  }, [setValue]);

  // Prefill form data function
  const prefillFormData = useCallback((design) => {
    if (!design) return;

    console.log("Prefilling form with data:", design);
    console.log("Available HOD options:", dropdownData.utilityHODList);

    // Define select field mappings for proper prefill
    const selectFieldMappings = {
      'project_id_fk': { 
        data: dropdownData.projectsList, 
        valueKey: 'project_id_fk', 
        labelKey: 'project_name',
        formatLabel: (item) => `${item.project_id_fk} - ${item.project_name}`,
        getDesignLabel: (d) => {
          // Try to get project name from various possible fields
          const projectName = d.project_name || d.projectName || d.project_name_fk;
          const projectId = d.project_id_fk || d.project_id || d.projectId;
          
          if (projectName && projectId) {
            return `${projectId} - ${projectName}`;
          } else if (projectName) {
            return projectName;
          } else if (projectId) {
            return String(projectId);
          }
          return '';
        }
      },
      'execution_agency_fk': { 
        data: dropdownData.utilityExecutionAgencyList, 
        valueKey: 'execution_agency_fk', 
        labelKey: 'execution_agency_fk' 
      },
      'hod_user_id_fk': { 
        data: dropdownData.utilityHODList || [], 
        valueKey: 'hod_user_id_fk', 
        labelKey: 'user_name',
        getDesignLabel: (d) => d.user_name || d.hod_user_id_fk
      },
      'utility_type_fk': { 
        data: dropdownData.utilityTypeList, 
        valueKey: 'utility_type_fk', 
        labelKey: 'utility_type_fk' 
      },
      'impacted_contract_id_fk': { 
        data: dropdownData.impactedContractsList, 
        valueKey: 'contract_id_fk', 
        labelKey: 'contract_short_name',
        formatLabel: (item) => `${item.contract_id_fk} - ${item.contract_short_name}`
      },
      'requirement_stage_fk': { 
        data: dropdownData.reqStageList, 
        valueKey: 'requirement_stage_fk', 
        labelKey: 'requirement_stage_name' 
      },
      'unit_fk': { 
        data: dropdownData.unitList, 
        valueKey: 'unit_fk', 
        labelKey: 'unit_fk' 
      },
      'shifting_status_fk': { 
        data: dropdownData.statusList, 
        valueKey: 'shifting_status_fk', 
        labelKey: 'shifting_status_fk' 
      },
      'impacted_element': { 
        data: dropdownData.impactedElementList, 
        valueKey: 'impacted_element', 
        labelKey: 'impacted_element'
      }
    };

    // Debug project data specifically
    console.log("🔍 PROJECT DEBUG:", {
      designProjectIdFk: design.project_id_fk,
      designProjectId: design.project_id,
      designProjectName: design.project_name,
      designAllKeys: Object.keys(design),
      availableProjects: dropdownData.projectsList?.map(p => ({ 
        id: p.project_id_fk, 
        name: p.project_name 
      }))
    });

    // Prefill select fields using robust helper
    Object.entries(selectFieldMappings).forEach(([field, config]) => {
      const designValue = design[field] ?? design[config.valueKey];
      if (designValue !== undefined && designValue !== null) {
        prefillSelectField(field, config, design);
      }
    });

    // Manual project prefill as a fallback
    if (design.project_id_fk || design.project_id) {
      const projectId = design.project_id_fk || design.project_id;
      const projectName = design.project_name || '';
      
      const projectOption = dropdownData.projectsList?.find(
        project => project.project_id_fk?.toString() === projectId?.toString()
      );
      
      if (projectOption) {
        setValue('project_id_fk', {
          value: projectOption.project_id_fk,
          label: `${projectOption.project_id_fk} - ${projectOption.project_name}`
        });
        console.log("✅ Manually prefilled project:", {
          value: projectOption.project_id_fk,
          label: `${projectOption.project_id_fk} - ${projectOption.project_name}`
        });
      } else if (projectId) {
        setValue('project_id_fk', {
          value: projectId,
          label: projectName ? `${projectId} - ${projectName}` : String(projectId)
        });
        console.log("🔄 Used project fallback:", {
          value: projectId,
          label: projectName ? `${projectId} - ${projectName}` : String(projectId)
        });
      }
    }

    // Prefill simple fields with date conversion
    const simpleFields = [
      'utility_description', 'location_name', 'custodian', 'identification',
      'reference_number', 'executed_by', 'chainage', 'latitude', 'longitude',
      'affected_structures', 'planned_completion_date', 'scope', 'completed',
      'start_date', 'shifting_completion_date', 'remarks'
    ];

    simpleFields.forEach(field => {
      if (design[field] !== undefined && design[field] !== null) {
        if (field.includes('date') || field === 'identification') {
          setValue(field, convertToHTMLDate(design[field]));
        } else {
          setValue(field, design[field]);
        }
      }
    });

    // Prefill progress details if available
    if (design.utilityShiftingProgressDetailsList && Array.isArray(design.utilityShiftingProgressDetailsList)) {
      setTimeout(() => {
        progressDetailsFields.forEach((_, index) => {
          removeProgressDetails(index);
        });
        
        design.utilityShiftingProgressDetailsList.forEach((progress) => {
          appendProgressDetails({
            progress_dates: convertToHTMLDate(progress.progress_date),
            progress_of_works: progress.progress_of_work
          });
        });
      }, 100);
    }

    // Prefill attachments if available (uses exact key utility_shifting_file_type)
    if (design.utilityShiftingFilesList && Array.isArray(design.utilityShiftingFilesList)) {
      setTimeout(() => {
        attachmentsFields.forEach((_, index) => removeAttachments(index));

        design.utilityShiftingFilesList.forEach((attachment) => {
          const apiFileType = attachment.utility_shifting_file_type;

          const fileTypeOption = dropdownData.utilityshiftingfiletypeList?.find(
            (item) =>
              item.utility_shifting_file_type?.toString() === apiFileType?.toString()
          );

          const selectOption = fileTypeOption
            ? {
                value: fileTypeOption.utility_shifting_file_type,
                label: fileTypeOption.utility_shifting_file_type,
              }
            : apiFileType
            ? { value: apiFileType, label: apiFileType }
            : "";

          // 🔥 include attachmentFileNames (server-stored file name)
          appendAttachments({
            attachment_file_types: selectOption,
            attachmentNames: attachment.name || "",
            attachmentFileNames: attachment.attachment || attachment.uploaded_file || "",
            utilityShiftingFiles: "", // file already uploaded
          });
        });
      }, 100);
    }
  }, [
    dropdownData,
    setValue,
    convertToHTMLDate,
    removeProgressDetails,
    appendProgressDetails,
    progressDetailsFields,
    removeAttachments,
    appendAttachments,
    attachmentsFields,
    prefillSelectField
  ]);

  // Helper function to convert array to Select options
  const convertToOptions = useCallback((dataArray = [], valueKey = "id", labelKey = "name", formatLabel = null) => {
    if (!Array.isArray(dataArray)) return [];
    return dataArray.map(item => ({
      value: item[valueKey],
      label: formatLabel ? formatLabel(item) : `${item[valueKey]} - ${item[labelKey] || item.project_name || item.contract_name || item.utility_description || String(item[valueKey])}`
    }));
  }, []);

  // Submit Handler - FIXED FOR UPDATE AND FILE ATTACHMENTS
  const onSubmit = async (data) => {
    try {
      setFormLoading(true);

      // Create FormData for file uploads
      const formData = new FormData();

      // Add simple fields
      const simpleFields = [
        'utility_description', 'location_name', 'custodian', 'identification',
        'reference_number', 'executed_by', 'chainage', 'latitude', 'longitude',
        'affected_structures', 'planned_completion_date', 'scope', 'completed',
        'start_date', 'shifting_completion_date', 'remarks'
      ];

      simpleFields.forEach(field => {
        if (data[field] !== undefined && data[field] !== null && data[field] !== '') {
          formData.append(field, data[field]);
        }
      });

      // Add Select fields (convert to values)
      const selectFields = {
        'project_id_fk': data.project_id_fk,
        'execution_agency_fk': data.execution_agency_fk,
        'hod_user_id_fk': data.hod_user_id_fk,
        'utility_type_fk': data.utility_type_fk,
        'impacted_contract_id_fk': data.impacted_contract_id_fk,
        'requirement_stage_fk': data.requirement_stage_fk,
        'unit_fk': data.unit_fk,
        'shifting_status_fk': data.shifting_status_fk,
        'impacted_element': data.impacted_element
      };

      Object.entries(selectFields).forEach(([key, value]) => {
        // Only append if value exists and has a valid value
        if (value && value.value !== undefined && value.value !== null && value.value !== '') {
          formData.append(key, value.value);
        }
        // Special handling for shifting_status_fk - don't send if empty
        else if (key === 'shifting_status_fk') {
          // Don't append anything if status is empty
          console.log('Skipping empty shifting_status_fk');
        }
        else if (value !== undefined && value !== null && value !== '') {
          formData.append(key, value);
        }
      });

      // Add progress details as arrays
      if (data.progressDetails && Array.isArray(data.progressDetails)) {
        data.progressDetails.forEach((item) => {
          if (item.progress_dates) {
            formData.append(`progress_dates`, item.progress_dates);
          }
          if (item.progress_of_works) {
            formData.append(`progress_of_works`, item.progress_of_works);
          }
        });
      }

      // 🔥 FIX: Handle attachments properly with all required arrays
      if (data.attachments && Array.isArray(data.attachments)) {
        const attachmentFileTypes = [];
        const attachmentNames = [];
        const attachmentFileNames = [];

        data.attachments.forEach((item, index) => {
          // Collect data for arrays - ensure we always have values
          if (item.attachment_file_types && item.attachment_file_types.value) {
            attachmentFileTypes.push(item.attachment_file_types.value);
          } else {
            attachmentFileTypes.push('');
          }

          if (item.attachmentNames) {
            attachmentNames.push(item.attachmentNames);
          } else {
            attachmentNames.push('');
          }

          // For attachmentFileNames - use actual file name if available, otherwise use attachmentNames
          if (item.utilityShiftingFiles && item.utilityShiftingFiles.length > 0) {
            attachmentFileNames.push(item.utilityShiftingFiles[0].name);
          } else if (item.attachmentNames) {
            attachmentFileNames.push(item.attachmentNames);
          } else {
            attachmentFileNames.push('');
          }

          // Handle actual file upload
          if (item.utilityShiftingFiles && item.utilityShiftingFiles.length > 0) {
            formData.append(`utilityShiftingFiles`, item.utilityShiftingFiles[0]);
          }
        });

        // 🔥 CRITICAL: Append all three arrays that backend expects
        attachmentFileTypes.forEach((type) => {
          formData.append('attachment_file_types', type || '');
        });

        attachmentNames.forEach((name) => {
          formData.append('attachmentNames', name || '');
        });

        attachmentFileNames.forEach((fileName) => {
          formData.append('attachmentFileNames', fileName || '');
        });

        console.log("📎 Attachment Arrays Sent:", {
          fileTypes: attachmentFileTypes,
          names: attachmentNames,
          fileNames: attachmentFileNames,
          lengths: {
            fileTypes: attachmentFileTypes.length,
            names: attachmentNames.length,
            fileNames: attachmentFileNames.length
          }
        });
      }

      // Add work_code
      if (data.project_id_fk && data.project_id_fk.value) {
        const workCode = data.project_id_fk.value.split('-')[0];
        formData.append('work_code', workCode);
      }

      // 🔥 CRITICAL FIX: Send both IDs for update
      if (isEdit && editData) {
        // Send the primary key ID (for WHERE clause in UPDATE)
        formData.append('id', editData.id);
        
        // Send the business ID (for progress, files, messages)
        formData.append('utility_shifting_id', editData.utility_shifting_id);
        
        console.log("🆔 Sending IDs for update:", {
          id: editData.id,
          utility_shifting_id: editData.utility_shifting_id
        });
      }

      console.log("FormData entries:");
      for (let pair of formData.entries()) {
        console.log(pair[0] + ': ', pair[1]);
      }

      const endpoint = isEdit 
        ? `${API_BASE_URL}/utility-shifting/updateUtilityShifting`
        : `${API_BASE_URL}/utility-shifting/addUtilityShifting`;

      const response = await api.post(endpoint, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        withCredentials: true
      });

      if (response.data) {
        alert(`✅ Utility Shifting ${isEdit ? 'updated' : 'saved'} successfully!`);
        
        // 🔥 TRIGGER REFRESH HERE
        if (setRefresh) {
          console.log("🔄 Triggering refresh for Utility Shifting list...");
          setRefresh(prev => !prev); // Toggle refresh state
        }
        
        navigate("/updateforms/utilityshifting");
      } else {
        alert(`❌ Failed to ${isEdit ? 'update' : 'save'} Utility Shifting`);
      }
    } catch (err) {
      console.error(`❌ Error ${isEdit ? 'updating' : 'saving'} utility shifting:`, err);
      console.error("Error response:", err.response);
      alert(`Error ${isEdit ? 'updating' : 'saving'} utility shifting: ${err.response?.data?.message || err.message}`);
    } finally {
      setFormLoading(false);
    }
  };
  
  if (loading) {
    return <div className={styles.container}>Loading form data...</div>;
  }

  return (
    <div className={styles.container}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            {isEdit ? "Edit Utility Shifting" : "Add Utility Shifting"}
          </h2>
        </div>
        <div className="innerPage">
          <form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">
            {/* Row 1 */}
            <div className="form-row">
              <div className="form-field">
                <label>Project</label>
                <Controller
                  name="project_id_fk"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={convertToOptions(
                        dropdownData.projectsList, 
                        "project_id_fk", 
                        "project_name",
                        (item) => `${item.project_id_fk} - ${item.project_name}`
                      )}
                      classNamePrefix="react-select"
                      placeholder="Select Project"
                      isSearchable
                      isLoading={loading}
                      isDisabled={isEdit}
                    />
                  )}
                />
              </div>

              <div className="form-field">
                <label>Execution Agency <span className="red">*</span></label>
                <Controller
                  name="execution_agency_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={dropdownData.utilityExecutionAgencyList?.map(item => ({
                        value: item.execution_agency_fk,
                        label: item.execution_agency_fk
                      })) || []}
                      classNamePrefix="react-select"
                      placeholder="Select Execution Agency"
                      isSearchable
                      isLoading={loading}
                    />
                  )}
                />
                {errors.execution_agency_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>HOD <span className="red">*</span></label>
                <Controller
                  name="hod_user_id_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={dropdownData.utilityHODList?.map(item => ({
                        value: item.hod_user_id_fk,
                        label: item.user_name || `User ${item.hod_user_id_fk}`
                      })) || []}
                      classNamePrefix="react-select"
                      placeholder="Select HOD"
                      isSearchable
                      isLoading={loading}
                    />
                  )}
                />
                {errors.hod_user_id_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Utility Type <span className="red">*</span></label>
                <Controller
                  name="utility_type_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={dropdownData.utilityTypeList?.map(item => ({
                        value: item.utility_type_fk,
                        label: item.utility_type_fk  
                      })) || []}
                      classNamePrefix="react-select"
                      placeholder="Select Utility Type"
                      isSearchable
                      isLoading={loading}
                    />
                  )}
                />
                {errors.utility_type_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Utility Description <span className="red">*</span></label>
                <input
                  {...register("utility_description", { required: true })}
                  type="text"
                  placeholder="Enter Utility Description"
                />
                {errors.utility_description && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Location Name</label>
                <input {...register("location_name")} type="text" maxLength={50} placeholder="Enter Location Name" />
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watch("location_name")?.length || 0}/50
                </div>
              </div>

              <div className="form-field">
                <label>Custodian</label>
                <input {...register("custodian")} type="text" placeholder="Enter Custodian" />
              </div>

              <div className="form-field">
                <label>Identification Date</label>
                <input {...register("identification")} type="date" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Reference Number</label>
                <input {...register("reference_number")} type="text" placeholder="Enter Reference Number" />
              </div>

              <div className="form-field">
                <label>Executed by</label>
                <input {...register("executed_by")} type="text" placeholder="Enter Executed By" />
              </div>

              <div className="form-field">
                <label>Chainage</label>
                <input {...register("chainage")} type="text" placeholder="Enter Chainage" />
              </div>

              <div className="form-field">
                <label>Latitude</label>
                <input {...register("latitude")} type="text" placeholder="Enter Latitude" />
              </div>

              <div className="form-field">
                <label>Longitude</label>
                <input {...register("longitude")} type="text" placeholder="Enter Longitude" />
              </div>

              <div className="form-field">
                <label>Impacted Contract <span className="red">*</span></label>
                <Controller
                  name="impacted_contract_id_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={convertToOptions(
                        dropdownData.impactedContractsList,
                        "contract_id_fk",
                        "contract_short_name",
                        (item) => `${item.contract_id_fk} - ${item.contract_short_name}`
                      )}
                      classNamePrefix="react-select"
                      placeholder="Select Impacted Contract"
                      isSearchable
                      isLoading={loading}
                    />
                  )}
                />
                {errors.impacted_contract_id_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Requirement Stage <span className="red">*</span></label>
                <Controller
                  name="requirement_stage_fk"
                  rules={{ required: true }}
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={dropdownData.reqStageList?.map(item => ({
                        value: item.requirement_stage_fk || item.id,
                        label: item.requirement_stage_name || item.name || String(item.requirement_stage_fk || item.id)
                      })) || []}
                      classNamePrefix="react-select"
                      placeholder="Select Requirement Stage"
                      isSearchable
                      isLoading={loading}
                    />
                  )}
                />
                {errors.requirement_stage_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Impacted Element</label>
                <Controller
                  name="impacted_element"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={dropdownData.impactedElementList?.map(item => ({
                        value: item.impacted_element,
                        label: item.impacted_element
                      })) || []}
                      classNamePrefix="react-select"
                      placeholder="Select Impacted Element"
                      isSearchable
                      isLoading={loading}
                    />
                  )}
                />
              </div>

              <div className="form-field">
                <label>Affected Structures</label>
                <input {...register("affected_structures")} type="text" placeholder="Enter Affected Structures" />
              </div>

              <div className="form-field">
                <label>Target Date</label>
                <input {...register("planned_completion_date")} type="date" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Scope</label>
                <input {...register("scope")} maxLength={25} type="number" placeholder="Enter Scope" />
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {(watch("scope") && String(watch("scope")).length) || 0}/25
                </div>
              </div>

              <div className="form-field">
                <label>Completed</label>
                <input {...register("completed")} maxLength={25} type="number" placeholder="Enter Completed" />
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {(watch("completed") && String(watch("completed")).length) || 0}/25
                </div>
              </div>

              <div className="form-field">
                <label>Unit</label>
                <Controller
                  name="unit_fk"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={dropdownData.unitList?.map(unit => ({
                        value: unit.unit_fk,
                        label: unit.unit_fk
                      })) || []}
                      classNamePrefix="react-select"
                      placeholder="Select Unit"
                      isSearchable
                      isLoading={loading}
                    />
                  )}
                />
              </div>

              <div className="form-field">
                <label>Start Date</label>
                <input {...register("start_date")} type="date" placeholder="Select Start Date" />
              </div>

              <div className="form-field">
                <label>Status</label>
                <Controller
                  name="shifting_status_fk"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      options={dropdownData.statusList?.map(status => ({
                        value: status.shifting_status_fk,
                        label: status.shifting_status_fk
                      })) || []}
                      classNamePrefix="react-select"
                      placeholder="Select Status"
                      isSearchable
                      isLoading={loading}
                    />
                  )}
                />
              </div>

              <div className="form-field">
                <label>Completion Date</label>
                <input {...register("shifting_completion_date")} type="date" placeholder="Select Completion Date" />
              </div>    
            </div>

            {/* Progress Details Section */}
            <div className="row mt-1 mb-2">
              <h6 className="d-flex justify-content-center mt-1 mb-2">Progress Details</h6>
              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "150px" }}>Progress Date</th>
                      <th style={{ width: "150px" }}>Progress of Work</th>
                      <th style={{ width: "100px" }}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {progressDetailsFields.length > 0 ? (
                      progressDetailsFields.map((item, index) => (
                        <tr key={item.id}>
                          <td>
                            <input
                              type="date"
                              {...register(`progressDetails.${index}.progress_dates`)}
                              className="form-control"
                            />
                          </td>
                          <td>
                            <textarea
                              {...register(`progressDetails.${index}.progress_of_works`)}
                              style={{ width: "100%" }}
                              maxLength={1000}
                              rows="3"
                              placeholder="Enter progress details"
                            ></textarea>
                            <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                              {watch(`progressDetails.${index}.progress_of_works`)?.length || 0}/1000
                            </div>
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeProgressDetails(index)}
                            >
                              <MdOutlineDeleteSweep size="26" />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="3" className="text-center text-muted">
                          No progress details added yet.
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
                    appendProgressDetails({
                      progress_dates: "",
                      progress_of_works: "",
                    })
                  }
                >
                  <BiListPlus size="24" />
                </button>
              </div>
            </div>

            {/* Attachments Section - ALL FIELDS MANDATORY */}
            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">Attachments <span className="red">*</span></h3>
              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "25%" }}>File Type <span className="red">*</span></th>
                      <th style={{ width: "35%" }}>Name <span className="red">*</span></th>
                      <th style={{ width: "25%" }}>Attachment <span className="red">*</span></th>
                      <th style={{ width: "15%" }}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {attachmentsFields.length > 0 ? (
                      attachmentsFields.map((item, index) => (
                        <tr key={item.id}>
                          <td>
                            <Controller
                              name={`attachments.${index}.attachment_file_types`}
                              control={control}
                              rules={{ 
                                required: "File type is required",
                                validate: value => value && value.value ? true : "File type is required"
                              }}
                              render={({ field, fieldState }) => {
                                const normalizedValue = field.value && typeof field.value === 'object'
                                  ? field.value
                                  : (field.value ? { value: field.value, label: String(field.value) } : null);

                                return (
                                  <div>
                                    <Select
                                      {...field}
                                      value={normalizedValue}
                                      options={dropdownData.utilityshiftingfiletypeList?.map(item => ({
                                        value: item.utility_shifting_file_type,
                                        label: item.utility_shifting_file_type
                                      })) || []}
                                      classNamePrefix="react-select"
                                      placeholder="Select File Type"
                                      isSearchable
                                      isClearable
                                      onChange={(selectedOption) => {
                                        field.onChange(selectedOption);
                                      }}
                                      styles={{
                                        control: (base) => ({
                                          ...base,
                                          borderColor: fieldState.error ? '#dc3545' : base.borderColor,
                                          '&:hover': {
                                            borderColor: fieldState.error ? '#dc3545' : base.borderColor
                                          }
                                        })
                                      }}
                                    />
                                    {fieldState.error && (
                                      <span className="red" style={{ fontSize: "12px" }}>
                                        {fieldState.error.message}
                                      </span>
                                    )}
                                  </div>
                                );
                              }}
                            />
                          </td>
                          <td>
                            <div>
                              <input
                                type="text"
                                {...register(`attachments.${index}.attachmentNames`, { 
                                  required: "File name is required" 
                                })}
                                className="form-control"
                                placeholder="Enter File Name"
                                style={{
                                  borderColor: errors.attachments?.[index]?.attachmentNames ? '#dc3545' : undefined
                                }}
                              />
                              {errors.attachments?.[index]?.attachmentNames && (
                                <span className="red" style={{ fontSize: "12px" }}>
                                  {errors.attachments[index].attachmentNames.message}
                                </span>
                              )}
                              {/* Hidden field for server-side file name (attachmentFileNames) */}
                              <input
                                type="hidden"
                                {...register(`attachments.${index}.attachmentFileNames`)}
                              />
                            </div>
                          </td>
                          <td>
                            <div>
                              <div className={styles["file-upload-wrapper"]}>
                                <label htmlFor={`file-${index}`} className={styles["file-upload-label-icon"]}>
                                  <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
                                  Upload File
                                </label>
                                <input
                                  id={`file-${index}`}
                                  type="file"
                                  {...register(`attachments.${index}.utilityShiftingFiles`, { 
                                    required: isEdit && !watch(`attachments.${index}.utilityShiftingFiles`)?.[0] && 
                                             !editData?.utilityShiftingFilesList?.[index] 
                                             ? "File is required" 
                                             : !isEdit 
                                               ? "File is required" 
                                               : false,
                                    validate: (files) => {
                                      // In edit mode, if there's already a file on server, no need to upload new one
                                      if (isEdit && editData?.utilityShiftingFilesList?.[index]) {
                                        return true;
                                      }
                                      // Otherwise, file is required
                                      return files && files.length > 0 || "File is required";
                                    }
                                  })}
                                  className={styles["file-upload-input"]}
                                />
                                {watch(`attachments.${index}.utilityShiftingFiles`)?.[0]?.name && (
                                  <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                    Selected: {watch(`attachments.${index}.utilityShiftingFiles`)[0].name}
                                  </p>
                                )}
                                {isEdit && !watch(`attachments.${index}.utilityShiftingFiles`)?.[0] && editData?.utilityShiftingFilesList?.[index] && (
                                  <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569", fontStyle: "italic" }}>
                                    File already uploaded: {editData.utilityShiftingFilesList[index].attachment || editData.utilityShiftingFilesList[index].uploaded_file || ''}
                                  </p>
                                )}
                              </div>
                              {errors.attachments?.[index]?.utilityShiftingFiles && (
                                <span className="red" style={{ fontSize: "12px" }}>
                                  {errors.attachments[index].utilityShiftingFiles.message}
                                </span>
                              )}
                            </div>
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeAttachments(index)}
                            >
                              <MdOutlineDeleteSweep size="26" />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="4" className="text-center text-muted">
                          No attachments added yet.
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
                    appendAttachments({
                      attachment_file_types: "",
                      attachmentNames: "",
                      utilityShiftingFiles: "",
                      attachmentFileNames: ""
                    })
                  }
                >
                  <BiListPlus size="24" />
                </button>
              </div>
              
              {/* Global attachment validation message */}
              {attachmentsFields.length === 0 && (
                <div className="text-center mt-2">
                  <span className="red">At least one attachment is required</span>
                </div>
              )}
            </div>

            <div className="form-row">
              <div className="form-field">
                <label>Remarks</label>
                <textarea
                  {...register("remarks")}
                  maxLength={1000}
                  rows="3"
                  placeholder="Enter remarks"
                ></textarea>
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watch("remarks")?.length || 0}/1000
                </div>
              </div>
            </div>

            {/* Buttons */}
            <div className="form-post-buttons">
              <button type="submit" className="btn btn-primary" disabled={formLoading}>
                {formLoading ? "Saving..." : (isEdit ? "Update" : "Save")}
              </button>
              <button
                type="button"
                className="btn btn-white"
                onClick={() => navigate(-1)}
                disabled={formLoading}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
      <Outlet />
    </div>
  );
}