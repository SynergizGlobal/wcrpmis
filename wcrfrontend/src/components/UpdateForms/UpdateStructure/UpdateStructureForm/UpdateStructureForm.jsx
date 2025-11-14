import React, { useEffect } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import { Outlet } from 'react-router-dom';
import styles from './UpdateStructureForm.module.css';
import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
import { RiAttachment2 } from 'react-icons/ri';

export default function UpdateStructureForm() {

    const navigate = useNavigate();
    const { state } = useLocation(); // passed when editing
    const isEdit = Boolean(state?.updateStructureform);const {
    register,
    control,
    handleSubmit,
    setValue,
    watch,
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

  // Prefill form if editing
  useEffect(() => {
    if (isEdit && state?.updateStructureform) {
      Object.entries(state.updateStructureform).forEach(([key, value]) => setValue(key, value));
    }
  }, [state, setValue, isEdit]);

  // Sample static lists (replace with API if available)
  const projectTypes = [
    { value: "1", label: "Construction" },
    { value: "2", label: "Maintenance" },
  ];

  const railwayZones = [
    { value: "WCR", label: "West Central Railway" },
    { value: "ECR", label: "East Central Railway" },
  ];

  const yearList = [
    { value: "2023-24", label: "2023-24" },
    { value: "2024-25", label: "2024-25" },
  ];

  const divisions = [
    { value: "Bhopal", label: "Bhopal" },
    { value: "Jabalpur", label: "Jabalpur" },
  ];

  const sections = [
    { value: "Section1", label: "Section 1" },
    { value: "Section2", label: "Section 2" },
  ];

  // Submit Handler
  const onSubmit = async (data) => {
    try {
      const endpoint = isEdit
        ? `${API_BASE_URL}/structure-form/${state.updateStructureform.structureId}`
        : `${API_BASE_URL}/structure-form`;

      if (isEdit) {
        await axios.put(endpoint, data);
      } else {
        await axios.post(endpoint, data);
      }

      alert("✅ Project saved successfully!");
      navigate("/wcrpmis/updateforms/structure-form");
    } catch (err) {
      console.error("❌ Error saving project:", err);
      alert("Error saving project");
    }
  };

  return (
    <div className={`${styles.container} container-padding`}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            Update Structure Form
          </h2>
        </div>
        <div className="innerPage">
          <form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">
            {/* <input type="hidden" {...register("structure_id")} /> */}
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
                {errors.project_id_fk && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label> Structure Type <span className="red">*</span></label>
                <Controller
                  name="structure_type_fk"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={projectTypes}
                      placeholder="Select Value"
                      isSearchable
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
                  render={({ field }) => (
                    <Select
                    classNamePrefix="react-select"
                      {...field}
                      options={[
                        { value: "Not Started", label: "Not Started" },
                        { value: "In Progress", label: "In Progress" },
                        { value: "On Hold", label: "On Hold" },
                        { value: "Commissioned", label: "Commissioned" },
                        { value: "Dropped", label: "Dropped" },
                      ]}
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
              </div>
            </div>

            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">Contract - Execution Executives</h3>

              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "40%" }}>Contract <span className="red">*</span></th>
                      <th style={{ width: "45%" }}>Estimated Completion Cost</th>
                      <th style={{ width: "15%" }}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {contractExecutionExecutivesFields.length > 0 ? (
                      contractExecutionExecutivesFields.map((item, index) => (
                        <tr key={item.id}>
                          <td>
                            <Controller
                              name={(`contractExecutionExecutivesFields.${index}.contracts_id_fk`)}
                              control={control}
                              rules={{ required: "Required" }}
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
                            {errors.contractExecutionExecutivesFields?.[index]?.contracts_id_fk && (
                                <span className="red">
                                  {errors.contractExecutionExecutivesFields[index].contracts_id_fk.message}
                                </span>
                              )}
                          </td>
                          <td>
                            <Controller
                              name={(`contractExecutionExecutivesFields.${index}.excecutives`)}
                              control={control}
                              rules={{ required: "Please select executives" }}
                              render={({ field }) => (
                                <Select
                                  {...field}
                                  // options={executiveOptions}
                                  isMulti
                                  placeholder="Select Executives"
                                  classNamePrefix="react-select"
                                  onChange={(selected) => field.onChange(selected)}
                                  value={field.value}
                                />
                              )}
                            />
                            {errors.contractExecutionExecutivesFields?.[index]?.excecutives && (
                                <span className="red">
                                  {errors.contractExecutionExecutivesFields[index].excecutives.message}
                                </span>
                              )}
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeContractExecutionExecutives(index)}
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
                    appendContractExecutionExecutives({ contracts_id_fk: "", excecutives: "", })
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
                <label>Original Target Date</label>
                <input {...register("target_date")} type="date" placeholder="Enter Value" />
              </div>
            
              <div className="form-field rupee-field">
                <label>Estimated Cost</label>
                <input {...register("estimated_cost")} type="number" placeholder="Enter Value" />
              </div>

              <div className="form-row w-100">
                <div className="form-field flex-100">
                  <label>Remarks</label>
                    <textarea 
                      {...register("remarks")}
                      onChange={(e) => setValue("remarks", e.target.value)}
                      name="remarks"
                      rules={{ required: true }}
                      maxLength={1000}
                      rows="3"
                      ></textarea>
                      <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                        {watch("remarks")?.length || 0}/1000
                        {errors.remarks && <span className="red">Required</span>}
                      </div>
                  </div>  
                </div>

              <div className="form-field">
                <label>Latitude</label>
                <input {...register("latitude")} type="number" step="any" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Longitude</label>
                <input {...register("longitude")} type="number" step="any" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Construction Start Date <span className="red">*</span></label>
                <input {...register("construction_start_date")} type="date" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>Target completion Date <span className="red">*</span></label>
                <input {...register("revised_completion")} type="date" placeholder="Enter Value" />
              </div>
            </div>

            {/* Completion Costs */}
            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">Completion Costs</h3>

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
                              {...register(`structureDetailsFields.${index}.structure_details`)}
                              className="form-control"
                            />
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(`structureDetailsFields.${index}.structure_values`)}
                              className="form-control"
                              placeholder="Estimated Cost"
                            />
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeStructureDetails(index)}
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
                    appendStructureDetails({ structure_details: "", structure_values: "", })
                  }
                >
                  <BiListPlus
                    size="24"
                  />
                </button>
              </div>
            </div>

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
                              name={`documentsFields.${index}.structure_file_types`}
                              control={control}
                              render={({ field }) => (
                                <Select
                                  {...field}
                                  classNamePrefix="react-select"
                                  // options={fileTypeOptions}
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
                              {...register(`documentsFields.${index}.structureDocumentNames`)}
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
                                {...register(`documentsFields.${index}.structureFiles`)}
                                className={styles["file-upload-input"]}
                              />
                              {(
                                watch(`documentsFields.${index}.structureFiles`)?.[0]?.name ||
                                watch(`documentsFields.${index}.structureDocumentFileNames`)
                              ) && (
                                <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                  {watch(`documentsFields.${index}.structureFiles`)?.[0]?.name ||
                                  watch(`documentsFields.${index}.structureDocumentFileNames`)}
                                </p>
                              )}
                            </div>

                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeDocuments(index)}
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
                    appendDocuments({ la_file_typess: null, laDocumentNames: "", laFiles: "" })
                  }
                >
                  <BiListPlus
                    size="24"
                  />
                </button>
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
    </div>
  );
}