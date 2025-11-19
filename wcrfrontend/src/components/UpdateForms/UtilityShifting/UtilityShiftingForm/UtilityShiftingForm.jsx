import React, { useEffect } from "react";
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
    const { state } = useLocation(); // passed when editing
    const isEdit = Boolean(state?.design);

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
          utilityShiftingFiles: "",
        }
      ],

      remarks: "",
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

  // Prefill form if editing
  useEffect(() => {
    if (isEdit && state?.design) {
      Object.entries(state.design).forEach(([key, value]) => setValue(key, value));
    }
  }, [state, setValue, isEdit]);

  // Submit Handler
  const onSubmit = async (data) => {
    try {
      const endpoint = isEdit
        ? `${API_BASE_URL}/design/ajax/form/get-design/design/${state.design.projectId}`
        : `${API_BASE_URL}/design/ajax/form/add-design-form`;

      if (isEdit) {
        await api.put(endpoint, data);
      } else {
        await api.post(endpoint, data);
      }

      alert("✅ Design saved successfully!");
      navigate("/wcrpmis/updateforms/design");
    } catch (err) {
      console.error("❌ Error saving design:", err);
      alert("Error saving design");
    }
  };

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
                      classNamePrefix="react-select"
                      placeholder="Select Value"
                      isSearchable
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
                      classNamePrefix="react-select"
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
                {errors.execution_agency_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>HOD <span className="red">*</span></label>
                <Controller
                  name="hod_user_id_fk "
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder="Select Value"
                      isSearchable
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
                    classNamePrefix="react-select"
                      {...field}
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
                {errors.structure_id_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Utility Description <span className="red">*</span></label>
                <input {...register("utility_description")} type="text" rules={{ required: true }} placeholder="Enter Value" />
                {errors.utility_description && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Location Name</label>
                <input {...register("location_name")} type="text" maxLength={50} placeholder="Enter Value" />
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watch("location_name")?.length || 0}/50
                </div>
              </div>

              <div className="form-field">
                <label>Custodian</label>
                <input {...register("custodian")} type="number" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>Identification Date </label>
                <input {...register("identification")} type="date" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Reference Number </label>
                <input {...register("reference_number")} type="text" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Executed by </label>
                <input {...register("executed_by")} type="text" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>Chainage </label>
                <input {...register("chainage")} type="text" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>latitude </label>
                <input {...register("latitude")} type="text" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Longitude </label>
                <input {...register("longitude")} type="text" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label> Impacted Contract <span className="red">*</span></label>
                <Controller
                  name="impacted_contract_id_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder="Select Date"
                      isSearchable
                    />
                  )}
                />
                {errors.impacted_contract_id_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label> Requirement stage <span className="red">*</span></label>
                <Controller
                  name="requirement_stage_fk"
                  rules={{ required: true }}
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder="Select Date"
                      isSearchable
                    />
                  )}
                />
                {errors.requirement_stage_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>  Impacted Element</label>
                <Controller
                  name="impacted_element"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder="Select Date"
                      isSearchable
                    />
                  )}
                />
              </div>
            
              <div className="form-field">
                <label>Affected Structures</label>
                <input {...register("affected_structures")} type="text" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>Target Date</label>
                <input {...register("planned_completion_date")} type="date" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Scope </label>
                <input {...register("scope")} maxLength={25} type="number" placeholder="Select Date" />
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watch("scope")?.length || 0}/25
                </div>
              </div>

              <div className="form-field">
                <label>Completed  </label>
                <input {...register("completed")} maxLength={25} type="number" placeholder="Select Date" />
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watch("completed")?.length || 0}/25
                </div>
              </div>

              <div className="form-field">
                <label> Unit</label>
                <Controller
                  name="unit_fk"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
              </div>

               <div className="form-field">
                <label>Start Date </label>
                <input {...register("start_date")} type="date" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label> Status</label>
                <Controller
                  name="shifting_status_fk"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
              </div>

              <div className="form-field">
                <label>Completion Date </label>
                <input {...register("shifting_completion_date")} type="date" placeholder="Enter Value" />
              </div>
            </div>

          
            <div className="row mt-1 mb-2">
              <h6 className="d-flex justify-content-center mt-1 mb-2">Progress Details</h6>

              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "150px" }}>Progress Date </th>
                      <th style={{ width: "150px" }}>Progress of Work </th>
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
                              disabled={isEdit} 
                              {...register(`progressDetailsFields.${index}.progress_dates`)}
                              className="form-control"
                            />
                          </td>
                          <td>
                            <textarea 
                              {...register(`progressDetailsFields.${index}.progress_of_works`)}
                              onChange={(e) => setValue("remarks", e.target.value)}
                              name="remarks"
                              rules={{ required: true }}
                              style={{ width: "100%" }}
                              maxLength={1000}
                              rows="3"
                              ></textarea>
                              <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                                {watch(`progressDetailsFields.${index}.progress_of_works`)?.length || 0}/1000
                              </div>
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeProgressDetails(index)}
                            >
                              <MdOutlineDeleteSweep
                               size="26"
                              />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="4" className="text-center text-muted">
                          No completion cost rows added yet.
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
                  <BiListPlus
                    size="24"
                  />
                </button>
              </div>
            </div>

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
                              name={`attachmentsFields.${index}.attachment_file_types`}
                              control={control}
                              render={({ field }) => (
                                <Select
                                  {...field}
                                  classNamePrefix="react-select"
                                  placeholder="Select File Type"
                                  isSearchable
                                  isClearable
                                  value={field.value}
                                  onChange={(opt) => field.onChange(opt)}
                                />
                              )}
                            />

                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(`attachmentsFields.${index}.attachmentNames`)}
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
                                {...register(`attachmentsFields.${index}.utilityShiftingFiles`)}
                                className={styles["file-upload-input"]}
                              />
                              {watch(`attachmentsFields.${index}.utilityShiftingFiles`)?.[0]?.name && (
                                <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                  Selected: {watch(`attachmentsFields.${index}.utilityShiftingFiles`)[0].name}
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
                              <MdOutlineDeleteSweep
                                size="26"
                              />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td colSpan="4" className="text-center text-muted">
                          No completion cost rows added yet.
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
                    appendAttachments({ attachment_file_types: "", attachmentNames: "", utilityShiftingFiles: ""})
                  }
                >
                  <BiListPlus
                    size="24"
                  />
                </button>
              </div>
            </div>

            <div className="form-row">
              <div className="form-field">
                <label>Remarks</label>
                <textarea 
                {...register("remarks")}
                onChange={(e) => setValue("remarks", e.target.value)}
                name="remarks"
                maxLength={1000}
                rows="3"
                ></textarea>
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watch("remarks")?.length || 0}/1000
                </div>
              </div>
            </div>


            {/* Buttons */}
            <div className="form-post-buttons">
              <button type="submit" className="btn btn-primary">
                {isEdit ? "Update" : "Save"}
              </button>
              <button
                type="button"
                className="btn btn-white"
                onClick={() => navigate(-1)}
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