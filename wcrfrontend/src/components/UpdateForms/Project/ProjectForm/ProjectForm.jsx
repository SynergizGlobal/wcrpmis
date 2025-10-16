import React, { useEffect } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import styles from "./ProjectForm.module.css";
import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';

export default function ProjectForm() {
  const navigate = useNavigate();
  const { state } = useLocation(); // passed when editing
  const isEdit = Boolean(state?.project);

  // React Hook Form setup
  const {
    register,
    control,
    handleSubmit,
    setValue,
    formState: { errors },
  } = useForm({
    defaultValues: {
      project_name: "",
      project_status: "",
      project_type_id: "",
      railway_zone: "",
      plan_head_number: "",
      financial_years: "",
      sanctioned_amount: "",
      sanctioned_commissioning_date: "",
      division_id: "",
      section_id: "",
      pink_book_item_numbers: "",
      actual_completion_cost: "",
      actual_completion_date: "",
      benefits: "",
      remarks: "",
      completionCosts: [{ date: "", estimatedCost: "", revisedDate: "" }],
    },
  });

  const { fields: costFields, append: appendCost, remove: removeCost } = useFieldArray({
    control,
    name: "completionCosts",
  });

  // Prefill form if editing
  useEffect(() => {
    if (isEdit && state?.project) {
      Object.entries(state.project).forEach(([key, value]) => setValue(key, value));
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
        ? `${API_BASE_URL}/projects/${state.project.projectId}`
        : `${API_BASE_URL}/projects`;

      if (isEdit) {
        await axios.put(endpoint, data);
      } else {
        await axios.post(endpoint, data);
      }

      alert("✅ Project saved successfully!");
      navigate("/wcrpmis/updateforms/project");
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
            {isEdit ? "Edit Project" : "Add Project"}
          </h2>
        </div>
        <div className="innerPage">
          <form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">

            {/* Row 1 */}
            <div className="form-row">
              <div className="form-field">
                <label>Project Name <span className="red">*</span></label>
                <input {...register("project_name", { required: true })} type="text" placeholder="Enter Value"/>
                {errors.project_name && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label>Project Status <span className="red">*</span></label>
                <Controller
                  name="project_status"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={[
                        { value: "Open", label: "Open" },
                        { value: "Closed", label: "Closed" },
                      ]}
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
                {errors.project_status && <span className="text-danger">Required</span>}
              </div>

              <div className="form-field">
                <label>Project Type <span className="red">*</span></label>
                <Controller
                  name="project_type_id"
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
              </div>
            
              <div className="form-field">
                <label>Railway Zone <span className="red">*</span></label>
                <Controller
                  name="railway_zone"
                  control={control}
                  render={({ field }) => (
                    <Select
                    classNamePrefix="react-select"
                      {...field}
                      options={railwayZones}
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
              </div>

              <div className="form-field">
                <label>Plan Head Number <span className="red">*</span></label>
                <input {...register("plan_head_number")} type="text" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>Sanctioned Year <span className="red">*</span></label>
                <Controller
                  name="financial_years"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={yearList}
                      placeholder="Select Date"
                      isSearchable
                    />
                  )}
                />
              </div>
            
              <div className="form-field">
                <label>Sanctioned Amount <span className="red">*</span></label>
                <input {...register("sanctioned_amount")} type="number" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>Sanctioned Commissioning Date <span className="red">*</span></label>
                <input {...register("sanctioned_commissioning_date")} type="date" placeholder="Select Date" />
              </div>

              <div className="form-field">
                <label>Division <span className="red">*</span></label>
                <Controller
                  name="division_id"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={divisions}
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
              </div>
              <div className="form-field">
                <label>Section <span className="red">*</span></label>
                <Controller
                  name="section_id"
                  control={control}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={sections}
                      placeholder="Select Value"
                      isSearchable
                    />
                  )}
                />
              </div>

              <div className="form-field">
                <label>PB Item No <span className="red">*</span></label>
                <input {...register("pink_book_item_numbers")} type="text" placeholder="Enter Value" />
              </div>

              <div className="form-field">
                <label>Actual Completion Cost <span className="red">*</span></label>
                <input {...register("actual_completion_cost")} type="text" placeholder="Enter Value" />
              </div>
            </div>

            {/* Completion Costs */}
            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">Completion Costs</h3>

              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "25%" }}>Date</th>
                      <th style={{ width: "35%" }}>Estimated Completion Cost</th>
                      <th style={{ width: "25%" }}>Revised Completion Date</th>
                      <th style={{ width: "15%" }}>Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    {costFields.length > 0 ? (
                      costFields.map((item, index) => (
                        <tr key={item.id}>
                          <td>
                            <input
                              type="date"
                              {...register(`completionCosts.${index}.date`)}
                              className="form-control"
                            />
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(`completionCosts.${index}.estimatedCost`)}
                              className="form-control"
                              placeholder="Estimated Cost"
                            />
                          </td>
                          <td>
                            <input
                              type="date"
                              {...register(`completionCosts.${index}.revisedDate`)}
                              className="form-control"
                            />
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() => removeCost(index)}
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
                    appendCost({ date: "", estimatedCost: "", revisedDate: "" })
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
