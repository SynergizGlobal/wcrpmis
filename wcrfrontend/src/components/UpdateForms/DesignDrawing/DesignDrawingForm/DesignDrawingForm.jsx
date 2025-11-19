import React, { useEffect } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation, Outlet } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from './DesignDrawingForm.module.css';
import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
import { RiAttachment2 } from 'react-icons/ri';


export default function DesignDrawingForm() {

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
      approving_railway: "",
      structure_type_fk: "",
      structure_id_fk: "",
      component: "",
      contract_id_fk: "",
      prepared_by_id_fk: "",
      consultant_contract_id_fk: "",
      proof_consultant_contract_id_fk: "",
      threepvc: "",
      drawing_type_fk: "",
      approval_authority_fk: "",
      required_date: "",
      gfc_released: "",
      drawing_title: "",
      contractor_drawing_no: "",
      mrvc_drawing_no: "",
      division_drawing_no: "",
      hq_drawing_no: "",
      revisionDetails: [
        { 
          revisions: "", 
          drawing_nos: "", 
          correspondence_letter_nos: "", 
          revision_dates: "", 
          revision_status_fks: "", 
          remarkss: "", 
          uploadFileNames: "", 
          current: "", 
        }
      ],
      remarks: "",
    },
  });

  const { fields: revisionDetailsFields, append: appendRevisionDetails, remove: removeRevisionDetails } = useFieldArray({
    control,
    name: "revisionDetails",
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
            {isEdit ? "Edit Design & Drawing" : "Add Design & Drawing"}
          </h2>
        </div>
        <div className="innerPage">
          <form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">

          <h6 className="d-flex justify-content-center mt-1 mb-2">Work Details</h6>

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
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
                {errors.project_id_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Approving Railway <span className="red">*</span></label>
                <Controller
                  name="approving_railway"
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
                {errors.approving_railway && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Structure Type <span className="red">*</span></label>
                <Controller
                  name="structure_type_fk "
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
                {errors.structure_type_fk && <span className="red">Required</span>}
              </div>
            
              <div className="form-field">
                <label>Structure <span className="red">*</span></label>
                <Controller
                  name="structure_id_fk"
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
                <label>Component</label>
                <input {...register("component")} type="text" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>Contract</label>
                <Controller
                  name="contract_id_fk"
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
                <label>Prepared By</label>
                <Controller
                  name="prepared_by_id_fk"
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
                <label>Consultant</label>
                <input {...register("consultant_contract_id_fk")} type="number" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>Proof Consultant </label>
                <input {...register("proof_consultant_contract_id_fk")} type="date" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>3PVC </label>
                <input {...register("threepvc")} type="date" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Drawing Type  <span className="red">*</span></label>
                <Controller
                  name="drawing_type_fk"
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
                {errors.drawing_type_fk && <span className="red">Required</span>}
              </div>
              <div className="form-field">
                <label>Approval Authority <span className="red">*</span></label>
                <Controller
                  name="approval_authority_fk"
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
                {errors.approval_authority_fk && <span className="red">Required</span>}
              </div>

              <div className="form-field">
                <label>Required Date</label>
                <input {...register("required_date")} type="date" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>GFC Release Date</label>
                <input {...register("actual_completion_cost")} type="date" placeholder="Enter Value" />
              </div>
            </div>

            <h6 className="d-flex justify-content-center mt-1 mb-2">Drawing Details</h6>

            <div className="form-row">
              <div className="form-field">
                <label>Drawing Title <span className="red">*</span></label>
                <textarea 
                {...register("drawing_title")}
                onChange={(e) => setValue("drawing_title", e.target.value)}
                name="drawing_title"
                rules={{ required: true }}
                maxLength={200}
                rows="3"
                ></textarea>
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watch("drawing_title")?.length || 0}/200
                  {errors.drawing_title && <span className="red">Required</span>}
                </div>
              </div>
            </div>

            <div className="form-row">
              <div className="form-field">
                <label>Agency Drawing No</label>
                <input {...register("contractor_drawing_no")} type="text" placeholder="Enter Value" />
              </div>
              <div className="form-field">
                <label>MRVC Drawing No</label>
                <input {...register("mrvc_drawing_no")} type="text" placeholder="Enter Value" />
              </div>
              <div className="form-field">
                <label>Divisional Drawing No</label>
                <input {...register("division_drawing_no")} type="text" placeholder="Enter Value" />
              </div>
              <div className="form-field">
                <label>HQ Drawing No</label>
                <input {...register("hq_drawing_no")} type="text" placeholder="Enter Value" />
              </div>

            </div>

            {/* Completion Costs */}
            <div className="row mt-1 mb-2">
              <h6 className="d-flex justify-content-center mt-1 mb-2">Revision Details</h6>

              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "150px" }}>Revision No.</th>
                      <th style={{ width: "150px" }}>Drawing No. <span className="red">*</span></th>
                      <th style={{ width: "150px" }}>Correspondence Letter No. <span className="red">*</span></th>
                      <th style={{ width: "150px" }}>Revision Date <span className="red">*</span></th>
                      <th style={{ width: "150px" }}>Revision Status <span className="red">*</span></th>
                      <th style={{ width: "180px" }}>Remarks</th>
                      <th style={{ width: "180px" }}>Upload File <span className="red">*</span></th>
                      <th style={{ width: "100px" }}>Current</th>
                      <th style={{ width: "100px" }}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {revisionDetailsFields.length > 0 ? (
                      revisionDetailsFields.map((item, index) => (
                        <tr key={item.id}>
                          <td>
                            <input
                              type="text"
                              disabled={isEdit} 
                              {...register(`revisionDetails.${index}.revisions`)}
                              className="form-control"
                            />
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(`revisionDetails.${index}.drawing_nos`, { required: "required" })}
                              className="form-control"
                            />
                            {errors.revisionDetails?.[index]?.drawing_nos && (
                              <span className="red">
                                {errors.revisionDetails[index].drawing_nos.message}
                              </span>
                            )}
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(`revisionDetails.${index}.correspondence_letter_nos`, { required: "required" })}
                              className="form-control"
                            />
                            {errors.revisionDetails?.[index]?.correspondence_letter_nos && (
                              <span className="red">
                                {errors.revisionDetails[index].correspondence_letter_nos.message}
                              </span>
                            )}
                          </td>
                          <td>
                            <input
                              type="date"
                              {...register(`revisionDetails.${index}.revision_dates`, { required: "required" })}
                              className="form-control"
                            />
                            {errors.revisionDetails?.[index]?.revision_dates && (
                              <span className="red">
                                {errors.revisionDetails[index].revision_dates.message}
                              </span>
                            )}
                          </td>
                           <td>
                            <Controller
                                name={`revisionDetails.${index}.revision_status_fks`}
                                rules={{ required: "required" }}
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
                            {errors.revisionDetails?.[index]?.revision_status_fks && (
                              <span className="red">
                                {errors.revisionDetails[index].revision_status_fks.message}
                              </span>
                            )}
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(`revisionDetails.${index}.remarkss`)}
                              className="form-control"
                            />
                          </td>
                          <td>
                            <div className={styles["file-upload-wrapper"]}>
                              <label htmlFor={`file-${index}`} className={styles["file-upload-label-icon"]}>
                                <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
                              </label>
                              <input
                              id={`file-${index}`}
                                type="file"
                                {...register(`revisionDetails.${index}.uploadFiles`, { required: "required" })}
                                className={styles["file-upload-input"]}
                              />
                              {watch(`revisionDetails.${index}.uploadFiles`)?.[0]?.name && (() => {
                                const fullName = watch(`revisionDetails.${index}.uploadFiles`)[0].name;

                                const dotIndex = fullName.lastIndexOf(".");
                                const ext = dotIndex !== -1 ? fullName.slice(dotIndex) : "";
                                const base = dotIndex !== -1 ? fullName.slice(0, dotIndex) : fullName;

                                const maxStart = 10;
                                const maxEnd = 6;

                                const trimmedBase =
                                  base.length > maxStart + maxEnd
                                    ? `${base.slice(0, maxStart)}...${base.slice(-maxEnd)}`
                                    : base;

                                return (
                                  <p style={{
                                    marginTop: "6px",
                                    fontSize: "0.9rem",
                                    color: "#475569",
                                    display: "block",
                                    maxWidth: "180px",
                                    whiteSpace: "nowrap",
                                    overflow: "hidden",
                                    textOverflow: "ellipsis",
                                    direction: "rtl", /* ✅ shows extension always */
                                    textAlign: "left"
                                  }}
                                    title={fullName}  // ✅ tooltip full name
                                  >
                                    {`${trimmedBase}${ext}`}
                                  </p>
                                );
                              })()}


                              {errors.revisionDetails?.[index]?.uploadFiles && (
                                <span className="red">
                                  {errors.revisionDetails[index].uploadFiles.message}
                                </span>
                              )}
                            </div>
                          </td>
                          <td>
                            <input
                              type="checkbox"
                              {...register(`revisionDetails.${index}.current`)}
                              className="form-control"
                            />
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeRevisionDetails(index)}
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
                    appendRevisionDetails({ 
                      revisions: "", 
                      drawing_nos: "", 
                      correspondence_letter_nos: "", 
                      revision_dates: "", 
                      revision_status_fks: "", 
                      remarkss: "", 
                      uploadFileNames: "", 
                      current: "", 
                    })
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
                <label>Drawing Title <span className="red">*</span></label>
                <textarea 
                {...register("remarks")}
                onChange={(e) => setValue("remarks", e.target.value)}
                name="remarks"
                rules={{ required: true }}
                maxLength={200}
                rows="3"
                ></textarea>
                <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                  {watch("remarks")?.length || 0}/200
                  {errors.remarks && <span className="red">Required</span>}
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