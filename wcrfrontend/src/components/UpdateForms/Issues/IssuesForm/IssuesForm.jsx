import React, { useEffect } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from './IssuesForm.module.css';
import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
import { RiAttachment2 } from 'react-icons/ri';

export default function IssuesForm() {

    const navigate = useNavigate();
    const { state } = useLocation(); // passed when editing
    const isEdit = Boolean(state?.Issue);

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
      contract_id_fk: "",
      structure: "",
      component: "",
      category_fk: "",
      title: "",
      priority_fk: "",
      description: "",
      corrective_measure: "",
      date: "",
      location: "",
      zonal_railway_fk: "",
      reported_by: "",

      documentsTable: [{ issue_file_types: "", issueFiles: "" }],
    }
  });

  const { fields: documentsTableFields, append: appendDocumentsTable, remove: removeDocumentsTable } = useFieldArray({
    control,
    name: "documentsTable",
  });

  // Prefill form if editing
  useEffect(() => {
    if (isEdit && state.Issue) {
      Object.entries(state.Issue).forEach(([key, value]) => {
        if (key !== "specilaization") {
          setValue(key, value);
        }
      });

      // Fix react-select value when editing
      if (state.Issue.specilaization) {
        setValue("specilaization", {
          value: state.Issue.specilaization,
          label: state.Issue.specilaization
        });
      }
    }
  }, [isEdit, setValue, state]);

  // Submit
  const onSubmit = async (data) => {
    try {
      const payload = {
        ...data,
        specilaization: data.specilaization?.value || data.specilaization
      };

      if (!isEdit) {
        // CREATE
        await api.post(`${API_BASE_URL}/Issues`, payload);
      } else {
        // UPDATE
        const updatedData = { ...payload };
        delete updatedData.IssueId;

        await api.put(
          `${API_BASE_URL}/Issues/${state.Issue.IssueId}`,
          updatedData
        );
      }

      alert("Issue details saved successfully!");
      navigate("/updateforms/Issue");

    } catch (err) {
      console.error("Error saving Issue:", err);
      alert("Error saving Issue details");
    }
  };


  return (
      <div className={`${styles.container} container-padding`}>
        <div className="card">
          <div className="formHeading">
            <h2 className="center-align ps-relative">
              {isEdit ? "Edit Issue" : "Add Issue"}
            </h2>
          </div>

          <div className="innerPage">
            <form onSubmit={handleSubmit(onSubmit)}>

              {/* PAN + Specialization */}
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
                          placeholder="Select value"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
                </div>

                <div className="form-field">
                  <label>Contract <span className="red">*</span></label>
                    <Controller
                      name="contract_id_fk"
                      control={control}
                      rules={{ required: true}}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select value"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
                    {errors.contract_id_fk && (
                      <span className="red">{errors.contract_id_fk.message}</span>
                    )}
                </div>
                <div className="form-field">
                  <label>Structure</label>
                    <Controller
                      name="structure"
                      control={control}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select value"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
                </div>
                <div className="form-field">
                  <label>Component</label>
                    <Controller
                      name="component"
                      control={control}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select value"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
                </div>
                <div className="form-field">
                  <label>Issue Category <span className="red">*</span></label>
                    <Controller
                      name="category_fk"
                      control={control}
                      rules={{ required: true}}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select value"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
                    {errors.category_fk && (
                      <span className="red">{errors.category_fk.message}</span>
                    )}
                </div>
                <div className="form-field">
                  <label>Short Description <span className="red">*</span></label>
                    <Controller
                      name="title"
                      control={control}
                      rules={{ required: true}}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select value"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
                    {errors.title && (
                      <span className="red">{errors.title.message}</span>
                    )}
                </div>
                <div className="form-field">
                  <label>Issue Priority <span className="red">*</span></label>
                    <Controller
                      name="priority_fk"
                      control={control}
                      rules={{ required: true}}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select value"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
                    {errors.priority_fk && (
                      <span className="red">{errors.priority_fk.message}</span>
                    )}
                </div>

              </div>

              
              <div className="form-row">
                <div className="form-field">
                  <label>Description of Issue </label>
                  <textarea 
                    {...register("description")}
                    onChange={(e) => setValue("description", e.target.value)}
                    name="description"
                    maxLength={200}
                    rows="3"
                    ></textarea>
                  <div style={{ textAlign: "right" }}>
                    {watch("description")?.length || 0}/1000
                  </div>
                </div>
              </div>

              <div className="form-row">
                <div className="form-field">
                  <label>Action Taken</label>
                  <textarea
                    {...register("corrective_measure")}
                    onChange={(e) => setValue("corrective_measure", e.target.value)}
                    name="corrective_measure"
                    rows="3"
                    maxLength={1000}
                  ></textarea>
                  <div style={{ textAlign: "right" }}>
                    {watch("corrective_measure")?.length || 0}/1000
                  </div>
                </div>
              </div>


              <div className="form-row">
                <div className="form-field">
                  <label>Deadline for Issue Resolution</label>
                  <input {...register("date")} type="date" placeholder="Select Date"/>
                </div>

                <div className="form-field">
                  <label>Location/Station/KM </label>
                  <input {...register("location")} type="text" placeholder="Select Date"/>
                </div>

                <div className="form-field">
                  <label>Responsible Organization (Pending with) <span className="red">*</span></label>
                    <Controller
                      name="zonal_railway_fk"
                      control={control}
                      rules={{ required: true}}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select value"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
                    {errors.zonal_railway_fk && (
                      <span className="red">{errors.zonal_railway_fk.message}</span>
                    )}
                </div>
                <div className="form-field">
                  <label>Reported by <span className="red">*</span> </label>
                  <input rules={{ required: "Required" }} {...register("reported_by")} type="text" placeholder="Select Date" />
                  {errors.reported_by && (
                    <p className="error-text">{errors.reported_by.message}</p>
                  )}
                </div>

              </div>
              

              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "15%" }}>File Type </th>
                      <th style={{ width: "15%" }}>Attachment</th>
                      <th style={{ width: "15%" }}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {documentsTableFields.length > 0 ? (
                      documentsTableFields.map((item, index) => (
                        <tr key={item.id}>
                            <td>
                            <Controller
                              name={`documentsTableFields.${index}.issue_file_types`}
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
                            <div className={styles["file-upload-wrapper"]}>
                              <label htmlFor={`file-${index}`} className={styles["file-upload-label-icon"]}>
                                <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
                                Upload File
                              </label>
                              <input
                                id={`file-${index}`}
                                type="file"
                                {...register(`documentsTableFields.${index}.issueFiles`)}
                                className={styles["file-upload-input"]}
                              />
                              {watch(`documentsTableFields.${index}.issueFiles`)?.[0]?.name && (
                                <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                  Selected: {watch(`documentsTableFields.${index}.issueFiles`)[0].name}
                                </p>
                              )}

                            </div>

                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeDocumentsTable(index)}
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
                          No rows added yet.
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
                    appendDocumentsTable({ issue_file_types: "", issueFiles: "" })
                  }
                >
                  <BiListPlus
                    size="24"
                  />
                </button>
              </div>

              {/* Buttons */}
              <div className="form-post-buttons">
                <button type="submit" className="btn btn-primary">
                  {isEdit ? "UPDATE" : "ADD"}
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