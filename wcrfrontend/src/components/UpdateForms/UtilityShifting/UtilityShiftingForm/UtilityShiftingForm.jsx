// UtilityShiftingForm.jsx
import React, { useEffect, useState } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation, Outlet } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from './UtilityShiftingForm.module.css';

import { API_BASE_URL } from "../../../../config";
import { MdAttachment, MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
import { RiAttachment2 } from 'react-icons/ri';

export default function UtilityShiftingForm() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const isEdit = Boolean(state?.design);

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
      progressDetails: [
        {
          progress_dates: "",
          progress_of_works: "",
        }
      ],
      attachments: [
        {
          attachment_file_types: "",
          attachmentNames: "",
          utilityShiftingFiles: ""
        }
      ],
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

  // Fetch dropdown data - SINGLE API for both add and edit
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

  // Prefill form if editing - ONLY after dropdown data is loaded
  useEffect(() => {
    if (isEdit && state?.design && Object.keys(dropdownData).length > 0) {
      const design = state.design;
      
      // Define select field mappings for proper prefill
      const selectFieldMappings = {
        'project_id_fk': { 
          data: dropdownData.projectsList, 
          valueKey: 'project_id_fk', 
          labelKey: 'project_name',
          formatLabel: (item) => `${item.project_id_fk} - ${item.project_name}`
        },
        'execution_agency_fk': { 
          data: dropdownData.utilityExecutionAgencyList, 
          valueKey: 'execution_agency_fk', 
          labelKey: 'execution_agency_fk' 
        },
        'hod_user_id_fk': { 
          data: dropdownData.utilityHODList, 
          valueKey: 'hod_user_id_fk', 
          labelKey: 'user_name' 
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

      // Prefill select fields
      Object.entries(selectFieldMappings).forEach(([field, config]) => {
        if (design[field]) {
          const option = config.data?.find(item => {
            const itemValue = item[config.valueKey];
            const designValue = design[field];
            return itemValue === designValue || 
                   itemValue?.toString() === designValue?.toString() ||
                   (typeof designValue === 'object' && designValue.value && itemValue === designValue.value);
          });
          
          if (option) {
            const label = config.formatLabel ? config.formatLabel(option) : (option[config.labelKey] || String(option[config.valueKey]));
            setValue(field, {
              value: option[config.valueKey],
              label: label
            });
          }
        }
      });

      // Prefill simple fields
      const simpleFields = [
        'utility_description', 'location_name', 'custodian', 'identification',
        'reference_number', 'executed_by', 'chainage', 'latitude', 'longitude',
        'affected_structures', 'planned_completion_date', 'scope', 'completed',
        'start_date', 'shifting_completion_date', 'remarks'
      ];

      simpleFields.forEach(field => {
        if (design[field] !== undefined && design[field] !== null) {
          setValue(field, design[field]);
        }
      });

      // Prefill progress details if available
      if (design.progressDetails && Array.isArray(design.progressDetails)) {
        design.progressDetails.forEach((progress, index) => {
          setValue(`progressDetails.${index}.progress_dates`, progress.progress_dates);
          setValue(`progressDetails.${index}.progress_of_works`, progress.progress_of_works);
        });
      }

      // Prefill attachments if available
      if (design.attachments && Array.isArray(design.attachments)) {
        design.attachments.forEach((attachment, index) => {
          setValue(`attachments.${index}.attachmentNames`, attachment.attachmentNames);
          // For file types, you might need to find the corresponding option
          if (attachment.attachment_file_types) {
            const fileTypeOption = dropdownData.utilityshiftingfiletypeList?.find(
              item => item.utility_shifting_file_type === attachment.attachment_file_types
            );
            if (fileTypeOption) {
              setValue(`attachments.${index}.attachment_file_types`, {
                value: fileTypeOption.utility_shifting_file_type,
                label: fileTypeOption.utility_shifting_file_type
              });
            }
          }
        });
      }
    }
  }, [state, setValue, isEdit, dropdownData]);

  // Helper function to convert array to Select options
  const convertToOptions = (dataArray = [], valueKey = "id", labelKey = "name", formatLabel = null) => {
    if (!Array.isArray(dataArray)) return [];
    return dataArray.map(item => ({
      value: item[valueKey],
      label: formatLabel ? formatLabel(item) : `${item[valueKey]} - ${item[labelKey] || item.project_name || item.contract_name || item.utility_description || String(item[valueKey])}`
    }));
  };

  // Submit Handler
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
        if (value && value.value) {
          formData.append(key, value.value);
        } else if (value) {
          formData.append(key, value);
        }
      });

      // Add progress details as arrays (backend expects progress_dates[] and progress_of_works[])
      if (data.progressDetails && Array.isArray(data.progressDetails)) {
        data.progressDetails.forEach((item, index) => {
          if (item.progress_dates) {
            formData.append(`progress_dates`, item.progress_dates);
          }
          if (item.progress_of_works) {
            formData.append(`progress_of_works`, item.progress_of_works);
          }
        });
      }

      // Add attachments
      if (data.attachments && Array.isArray(data.attachments)) {
        data.attachments.forEach((item, index) => {
          if (item.attachment_file_types && item.attachment_file_types.value) {
            formData.append(`attachment_file_types`, item.attachment_file_types.value);
          }
          
          if (item.attachmentNames) {
            formData.append(`attachmentNames`, item.attachmentNames);
          }

          // Handle actual file upload
          if (item.utilityShiftingFiles && item.utilityShiftingFiles.length > 0) {
            formData.append(`utilityShiftingFiles`, item.utilityShiftingFiles[0]);
          }
        });
      }

      // Add work_code (CRITICAL - get this from project or other source)
      if (data.project_id_fk && data.project_id_fk.value) {
        // Extract work_code from project ID or fetch it
        const workCode = data.project_id_fk.value.split('-')[0]; // Adjust based on your project ID format
        formData.append('work_code', workCode);
      }

      // Add utility_shifting_id for edit mode
      if (isEdit && state?.design?.utility_shifting_id) {
        formData.append('utility_shifting_id', state.design.utility_shifting_id);
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

  if (loading && !isEdit) {
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
                        label: item.user_name
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

            {/* Attachments Section */}
            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">Attachments</h3>

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
                    {attachmentsFields.length > 0 ? (
                      attachmentsFields.map((item, index) => (
                        <tr key={item.id}>
                          <td>
                            <Controller
                              name={`attachments.${index}.attachment_file_types`}
                              control={control}
                              render={({ field }) => (
                                <Select
                                  {...field}
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
                                  value={field.value}
                                />
                              )}
                            />
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(`attachments.${index}.attachmentNames`)}
                              className="form-control"
                              placeholder="File Name"
                            />
                          </td>
                          <td>
                            <div className={styles["file-upload-wrapper"]}>
                              <label htmlFor={`file-${index}`} className={styles["file-upload-label-icon"]}>
                                <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
                                Upload File
                              </label>
                              <input
                                id={`file-${index}`}
                                type="file"
                                {...register(`attachments.${index}.utilityShiftingFiles`)}
                                className={styles["file-upload-input"]}
                              />
                              {watch(`attachments.${index}.utilityShiftingFiles`)?.[0]?.name && (
                                <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                  Selected: {watch(`attachments.${index}.utilityShiftingFiles`)[0].name}
                                </p>
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
                      utilityShiftingFiles: ""
                    })
                  }
                >
                  <BiListPlus size="24" />
                </button>
              </div>
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