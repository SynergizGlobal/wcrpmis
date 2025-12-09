import React, { useEffect, useState } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from "./UpdateStructureForm.module.css";
import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from "react-icons/md";
import { BiListPlus } from "react-icons/bi";
import { RiAttachment2 } from "react-icons/ri";

export default function UpdateStructureForm() {
  const navigate = useNavigate();
  const { state } = useLocation();
  const isEdit = Boolean(state?.updateStructureform || state?.structure_id);
  const structureId =
    state?.structure_id || state?.updateStructureform?.structure_id;

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
    error: null,
  });

  const [structureApiData, setStructureApiData] = useState(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

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
      contractExecutionExecutives: [{ contracts_id_fk: "", excecutives: "" }],
      target_date: "",
      estimated_cost: "",
      estimated_cost_unit: "",
      remarks: "",
      latitude: "",
      longitude: "",
      construction_start_date: "",
      revised_completion: "",
      structureDetails: [
        { structure_details: "", structure_values: "" },
      ],
      documents: [
        {
          structure_file_types: "",
          structureDocumentNames: "",
          structureFiles: "",
          attachment: "", // existing file name from backend
        },
      ],
    },
  });

  const {
    fields: contractExecutionExecutivesFields,
    append: appendContractExecutionExecutives,
    remove: removeContractExecutionExecutives,
  } = useFieldArray({ control, name: "contractExecutionExecutives" });

  const {
    fields: structureDetailsFields,
    append: appendStructureDetails,
    remove: removeStructureDetails,
  } = useFieldArray({ control, name: "structureDetails" });

  const {
    fields: documentsFields,
    append: appendDocuments,
    remove: removeDocuments,
  } = useFieldArray({ control, name: "documents" });

  // Helpers: date conversions
  const toInputDate = (value) => {
    if (!value) return "";
    if (typeof value === "string" && value.includes("/")) {
      const [dd, mm, yyyy] = value.split("/");
      if (dd && mm && yyyy) {
        return `${yyyy}-${mm.padStart(2, "0")}-${dd.padStart(2, "0")}`;
      }
      return "";
    }
    const d = new Date(value);
    if (isNaN(d.getTime())) return "";
    return d.toISOString().split("T")[0];
  };

  const formatDateForBackend = (dateString) => {
    if (!dateString) return null;
    try {
      const d = new Date(dateString);
      if (isNaN(d.getTime())) return dateString;
      const day = String(d.getDate()).padStart(2, "0");
      const month = String(d.getMonth() + 1).padStart(2, "0");
      const year = d.getFullYear();
      return `${day}/${month}/${year}`;
    } catch {
      return dateString;
    }
  };

  const findOption = (options, value, labelFallback) => {
    if (value == null && labelFallback == null) return null;
    const v = value != null ? String(value).trim().toUpperCase() : null;
    const l =
      labelFallback != null ? String(labelFallback).trim().toUpperCase() : null;
    return (
      options.find(
        (o) =>
          (v && String(o.value).trim().toUpperCase() === v) ||
          (l && String(o.label).trim().toUpperCase() === l) ||
          (v && String(o.label).trim().toUpperCase() === v) ||
          (l && String(o.value).trim().toUpperCase() === l)
      ) || null
    );
  };

  // Fetch dropdown data & structure (for edit)
  useEffect(() => {
    const fetchData = async () => {
      try {
        setDropdownData((prev) => ({ ...prev, loading: true, error: null }));
        setStructureApiData(null);

        const params = {};
        if (isEdit && structureId) params.structure_id = structureId;

        const res = await api.get(`${API_BASE_URL}/get-structure-form`, {
          params,
        });
        const data = res.data || {};

        setDropdownData({
          projectsList: data.projectsList || [],
          structuresList: data.structuresList || [],
          contractsList: data.contractsList || [],
          responsiblePeopleList: data.responsiblePeopleList || [],
          workStatusList: data.workStatusList || [],
          executionStatusList:
            data.executionStatusList || [
              "Not Started",
              "In Progress",
              "On Hold",
              "Commissioned",
              "Completed",
              "Dropped",
            ],
          fileType: data.fileType || [],
          departmentsList: data.departmentsList || [],
          unitsList: data.unitsList || [],
          loading: false,
          error: null,
        });

        if (isEdit) setStructureApiData(data);
      } catch (err) {
        console.error("Error loading structure form:", err);
        setDropdownData((prev) => ({
          ...prev,
          loading: false,
          error: err.message || "Failed to load data",
        }));
      }
    };

    fetchData();
  }, [isEdit, structureId]);

  // Format select options
  const formatProjects = (dropdownData.projectsList || [])
    .filter(Boolean)
    .map((item) => ({
      value: item.project_id_fk ?? item.project_id ?? item.id,
      label:
        item.project_name ||
        String(item.project_id_fk ?? item.project_id ?? item.id),
    }));

  const formatStructures = (dropdownData.structuresList || [])
    .filter(Boolean)
    .map((item) => ({
      value: item.structure_type_fk || item.structure_type,
      label: item.structure_type || String(item.structure_type_fk || ""),
    }));

  const formatContracts = (dropdownData.contractsList || [])
    .filter(Boolean)
    .map((item) => {
      const value = item.contract_id_fk ?? item.contract_id ?? item.id;
      const label =
        item.contract_short_name ||
        item.contract_name ||
        String(value);
      return { value, label };
    });

  const formatResponsiblePeople = (dropdownData.responsiblePeopleList || [])
    .filter(Boolean)
    .map((item) => ({
      value: item.user_id,
      label: `${item.designation || ""} - ${item.user_name || ""}`,
    }));

  const formatWorkStatus = (dropdownData.executionStatusList || [])
    .filter(Boolean)
    .map((status) => ({
      value: status,
      label: status,
    }));

  const formatFileTypes = (dropdownData.fileType || [])
    .filter(Boolean)
    .map((item) => ({
      value: item.value || item.structure_file_type,
      label: item.value || item.structure_file_type,
    }));

  const formatUnits = (dropdownData.unitsList || [])
    .filter(Boolean)
    .map((item) => {
      if (typeof item === "string") return { value: item, label: item };
      return {
        value: item.unit_id || item.value || item.unit || item,
        label:
          item.unit || item.label || String(item.unit_id || item.value || item),
      };
    });

  // Prefill (for edit)
  useEffect(() => {
    if (!isEdit || dropdownData.loading || !structureApiData) return;

    const rawDetails =
      structureApiData.structuresListDetails ||
      structureApiData.structureDetails ||
      structureApiData.structure;

    let d = null;
    if (Array.isArray(rawDetails)) {
      if (rawDetails.length === 0) return;
      d = rawDetails[0];
    } else if (rawDetails && typeof rawDetails === "object") {
      d = rawDetails;
    } else {
      return;
    }
    if (!d) return;

    const projectOption = findOption(
      formatProjects,
      d.project_id_fk,
      d.project_name
    );
    const structureTypeOption = findOption(
      formatStructures,
      d.structure_type_fk,
      d.structure_type
    );
    const workStatusOption = findOption(
      formatWorkStatus,
      d.work_status_fk,
      d.work_status
    );

    const unitRaw =
      d.unit || d.estimated_cost_unit || d.estimated_cost_units || d.unit_id;
    const costUnitOption = findOption(formatUnits, unitRaw, unitRaw);

    // contractExecutionExecutives prefill
    let execSource =
      d.executivesList ||
      structureApiData.executivesList ||
      structureApiData.structureDetailsTypes ||
      [];

    if (!Array.isArray(execSource)) execSource = [execSource];
    execSource = execSource.filter(Boolean);

    const contractExecutionExecutives =
      execSource.length > 0
        ? execSource.map((exec) => {
            const contractOpt = findOption(
              formatContracts,
              exec.contracts_id_fk,
              exec.contract_short_name
            );

            let execValues = [];

            if (exec.excecutives) {
              const parts = String(exec.excecutives)
                .split(",")
                .map((s) => s.trim())
                .filter(Boolean);

              execValues = parts
                .map(
                  (p) =>
                    findOption(formatResponsiblePeople, p, null) ||
                    findOption(formatResponsiblePeople, null, p)
                )
                .filter(Boolean);
            } else if (exec.responsiblePeopleLists) {
              execValues = exec.responsiblePeopleLists
                .map((p) =>
                  findOption(
                    formatResponsiblePeople,
                    p.responsible_people_id_fk || p.user_id,
                    p.user_name
                  )
                )
                .filter(Boolean);
            }

            return {
              contracts_id_fk: contractOpt,
              excecutives: execValues,
            };
          })
        : [{ contracts_id_fk: "", excecutives: "" }];

    // Structure Details prefill
    let structureDetailsUI = [];
    if (
      d.structureDetailsList1 &&
      Array.isArray(d.structureDetailsList1) &&
      d.structureDetailsList1.length > 0
    ) {
      structureDetailsUI = d.structureDetailsList1.map((sd) => ({
        structure_details: sd.structure_detail || sd.structure_details || "",
        structure_values: sd.structure_value || sd.structure_values || "",
      }));
    } else if (
      structureApiData.structureDetailsList1 &&
      Array.isArray(structureApiData.structureDetailsList1)
    ) {
      structureDetailsUI = structureApiData.structureDetailsList1.map(
        (sd) => ({
          structure_details: sd.structure_detail || sd.structure_details || "",
          structure_values: sd.structure_value || sd.structure_values || "",
        })
      );
    } else {
      const sdArr =
        d.structure_details || structureApiData.structure_details || [];
      const svArr =
        d.structure_values || structureApiData.structure_values || [];
      const maxLen = Math.max(sdArr?.length || 0, svArr?.length || 0);
      for (let i = 0; i < maxLen; i++) {
        structureDetailsUI.push({
          structure_details: (sdArr && sdArr[i]) || "",
          structure_values: (svArr && svArr[i]) || "",
        });
      }
      if (structureDetailsUI.length === 0)
        structureDetailsUI = [
          { structure_details: "", structure_values: "" },
        ];
    }

    // Documents prefill
    let docsSrc = d.documentsList || structureApiData.documentsList || [];
    if (!Array.isArray(docsSrc)) docsSrc = [docsSrc];
    docsSrc = docsSrc.filter(Boolean);

    const documents =
      docsSrc.length > 0
        ? docsSrc.map((doc) => ({
            structure_file_types: findOption(
              formatFileTypes,
              doc.structure_file_type_fk || doc.structure_file_type,
              doc.structure_file_type
            ),
            structureDocumentNames: doc.name || doc.file_name || "",
            structureFiles: "",
            attachment: doc.attachment || doc.file_name || "", // store existing file name
          }))
        : [
            {
              structure_file_types: "",
              structureDocumentNames: "",
              structureFiles: "",
              attachment: "",
            },
          ];

    const values = {
      project_id_fk: projectOption,
      structure_type_fk: structureTypeOption,
      work_status_fk: workStatusOption,
      structure_name: d.structure_name || "",
      structure: d.structure || d.structure_id || "",
      target_date: toInputDate(d.target_date),
      estimated_cost: d.estimated_cost || "",
      estimated_cost_unit: costUnitOption,
      remarks: d.remarks || "",
      latitude: d.latitude || "",
      longitude: d.longitude || "",
      construction_start_date: toInputDate(d.construction_start_date),
      revised_completion: toInputDate(d.revised_completion),
      contractExecutionExecutives,
      structureDetails: structureDetailsUI,
      documents,
    };

    reset(values);
  }, [isEdit, dropdownData.loading, structureApiData, reset]);

  // Submit
  const onSubmit = async (data) => {
    setIsSubmitting(true);
    try {
      const normalizeSingle = (sel) => {
        if (sel == null) return null;
        if (typeof sel === "object" && "value" in sel) return sel.value;
        return sel;
      };
      const normalizeMulti = (sel) => {
        if (!sel) return [];
        if (Array.isArray(sel))
          return sel.map((s) =>
            typeof s === "object" && "value" in s ? s.value : s
          );
        if (typeof sel === "string")
          return sel
            .split(",")
            .map((s) => s.trim())
            .filter(Boolean);
        return [];
      };

      // Contracts & executives arrays
      const contracts_id_fk_arr = [];
      const responsible_people_id_fks_arr = [];

      (data.contractExecutionExecutives || []).forEach((item) => {
        const cVal = normalizeSingle(item.contracts_id_fk);
        const execIds = normalizeMulti(item.excecutives);

        if (cVal != null && cVal !== "") {
          contracts_id_fk_arr.push(String(cVal));
        } else {
          contracts_id_fk_arr.push(null);
        }

        if (execIds.length > 0) {
          responsible_people_id_fks_arr.push(execIds.join(","));
        } else {
          responsible_people_id_fks_arr.push(null);
        }
      });

      // Structure details arrays
      const structure_details_arr = (data.structureDetails || []).map((it) =>
        it && it.structure_details ? String(it.structure_details) : null
      );
      const structure_values_arr = (data.structureDetails || []).map((it) =>
        it && it.structure_values ? String(it.structure_values) : null
      );
      const hasAnyStructureDetail = structure_details_arr.some(
        (x) => x != null && x !== ""
      );
      const hasAnyStructureValue = structure_values_arr.some(
        (x) => x != null && x !== ""
      );

      // Base formatted data (scalar & non-file arrays)
      const formattedData = {
        ...data,
        target_date: formatDateForBackend(data.target_date),
        construction_start_date: formatDateForBackend(
          data.construction_start_date
        ),
        revised_completion: formatDateForBackend(data.revised_completion),

        project_id_fk: data.project_id_fk?.value || data.project_id_fk,
        structure_type_fk:
          data.structure_type_fk?.value || data.structure_type_fk,
        work_status_fk: data.work_status_fk?.value || data.work_status_fk,

        estimated_cost: data.estimated_cost || undefined,
        estimated_cost_unit:
          data.estimated_cost_unit?.value ||
          data.estimated_cost_unit ||
          undefined,
        estimated_cost_units:
          data.estimated_cost_unit?.value ||
          data.estimated_cost_unit ||
          undefined,

        contracts_id_fk:
          contracts_id_fk_arr.length ? contracts_id_fk_arr : undefined,
        responsible_people_id_fks: responsible_people_id_fks_arr.length
          ? responsible_people_id_fks_arr
          : undefined,

        ...(hasAnyStructureDetail
          ? { structure_details: structure_details_arr }
          : {}),
        ...(hasAnyStructureValue
          ? { structure_values: structure_values_arr }
          : {}),

        ...(isEdit && structureId ? { structure_id: structureId } : {}),
      };

      // Build docs arrays (file types, names, file names, file objects)
      const structure_file_types_arr = [];
      const structureDocumentNames_arr = [];
      const structureFileNames_arr = [];
      const structureFiles_arr = [];

      (data.documents || []).forEach((doc) => {
        const typeVal =
          doc.structure_file_types?.value || doc.structure_file_types || "";
        const nameVal = doc.structureDocumentNames || "";
        const existingAttachment = doc.attachment || "";

        let fileObj = null;
        if (doc.structureFiles && doc.structureFiles.length > 0) {
          // FileList from input
          fileObj = doc.structureFiles[0];
        }

        // skip fully empty row
        if (
          !typeVal &&
          !nameVal &&
          !existingAttachment &&
          (!fileObj || !fileObj.name)
        ) {
          return;
        }

        structure_file_types_arr.push(typeVal || "");
        structureDocumentNames_arr.push(nameVal || "");

        if (fileObj && fileObj.name) {
          structureFileNames_arr.push(fileObj.name);
          structureFiles_arr.push(fileObj);
        } else {
          // keep old file name from backend
          structureFileNames_arr.push(existingAttachment || "");
        }
      });

      // Now build FormData
      const formData = new FormData();

      // Simple fields & non-file arrays
      Object.entries(formattedData).forEach(([key, value]) => {
        if (value === undefined || value === null) return;

        if (Array.isArray(value)) {
          value.forEach((v) => {
            formData.append(key, v != null ? v : "");
          });
        } else {
          formData.append(key, value);
        }
      });

      // Append documents arrays
      structure_file_types_arr.forEach((v) =>
        formData.append("structure_file_types", v != null ? v : "")
      );
      structureDocumentNames_arr.forEach((v) =>
        formData.append("structureDocumentNames", v != null ? v : "")
      );
      structureFileNames_arr.forEach((v) =>
        formData.append("structureFileNames", v != null ? v : "")
      );
      structureFiles_arr.forEach((file) => {
        formData.append("structureFiles", file);
      });

      console.log(
        ">>> FINAL FORM DATA (debug, without files content).",
        {
          ...formattedData,
          structure_file_types: structure_file_types_arr,
          structureDocumentNames: structureDocumentNames_arr,
          structureFileNames: structureFileNames_arr,
          structureFiles_count: structureFiles_arr.length,
        }
      );

      // Send multipart/form-data (let axios set headers)
      const res = await api.post(
        `${API_BASE_URL}/update-structure-form`,
        formData
      );

      if (res.data?.success) {
        alert("✅ Structure saved successfully!");
        navigate("wcrpmis/updateforms/structure-form");
      } else {
        alert(
          `❌ ${
            res.data?.message || "Failed to save structure. Please try again."
          }`
        );
      }
    } catch (err) {
      console.error(
        "Error saving structure:",
        err,
        err?.response?.data ?? ""
      );
      alert(
        err?.response?.data?.error ||
          err?.response?.data?.message ||
          "Error saving structure"
      );
    } finally {
      setIsSubmitting(false);
    }
  };

  const watchRemarks = watch("remarks");
  const watchLatitude = watch("latitude") || "";
  const watchLongitude = watch("longitude") || "";

  if (dropdownData.loading) {
    return (
      <div className={`${styles.container} container-padding`}>
        <div className="card">
          <div
            className="innerPage d-flex justify-content-center align-items-center"
            style={{ minHeight: "200px" }}
          >
            <div className="text-center">
              <div className="spinner-border" role="status" />
              <p className="mt-2 mb-0">Loading structure data...</p>
            </div>
          </div>
        </div>
      </div>
    );
  }
  if (dropdownData.error) {
    return (
      <div className={`${styles.container} container-padding`}>
        <div className="card">
          <div className="innerPage text-center">
            <p className="text-danger mb-2">
              Failed to load data: {dropdownData.error}
            </p>
            <button
              className="btn btn-primary"
              type="button"
              onClick={() => window.location.reload()}
            >
              Retry
            </button>
          </div>
        </div>
      </div>
    );
  }

  // Form UI
  return (
    <div className={`${styles.container} container-padding`}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            {isEdit ? "Update Structure Form" : "Create Structure Form"}
          </h2>
          {isEdit && structureId && (
            <p className="text-center text-muted">
              Editing Structure ID: {structureId}
            </p>
          )}
        </div>

        <div className="innerPage">
          <form
            onSubmit={handleSubmit(onSubmit)}
            encType="multipart/form-data"
          >
            {/* Row 1 */}
            <div className="form-row">
              <div className="form-field">
                <label>
                  Project<span className="red">*</span>
                </label>
                <Controller
                  name="project_id_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={formatProjects}
                      placeholder="Select Project"
                      isSearchable
                    />
                  )}
                />
                {errors.project_id_fk && (
                  <span className="text-danger">Required</span>
                )}
              </div>

              <div className="form-field">
                <label>
                  Structure Type <span className="red">*</span>
                </label>
                <Controller
                  name="structure_type_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={formatStructures}
                      placeholder="Select Structure Type"
                      isSearchable
                    />
                  )}
                />
                {errors.structure_type_fk && (
                  <span className="text-danger">Required</span>
                )}
              </div>

              <div className="form-field">
                <label>
                  Structure Name <span className="red">*</span>
                </label>
                <input
                  {...register("structure_name", { required: true })}
                  type="text"
                  placeholder="Enter Value"
                />
                {errors.structure_name && (
                  <span className="text-danger">Required</span>
                )}
              </div>

              <div className="form-field">
                <label>
                  Structure ID <span className="red">*</span>
                </label>
                <input
                  {...register("structure", { required: true })}
                  type="text"
                  placeholder="Enter Value"
                />
                {errors.structure && (
                  <span className="text-danger">Required</span>
                )}
              </div>

              <div className="form-field">
                <label>
                  Work Status <span className="red">*</span>
                </label>
                <Controller
                  name="work_status_fk"
                  control={control}
                  rules={{ required: true }}
                  render={({ field }) => (
                    <Select
                      {...field}
                      classNamePrefix="react-select"
                      options={formatWorkStatus}
                      placeholder="Select Work Status"
                      isSearchable
                    />
                  )}
                />
                {errors.work_status_fk && (
                  <span className="text-danger">Required</span>
                )}
              </div>
            </div>

            {/* Contract - Execution Executives */}
            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">
                Contract - Execution Executives
              </h3>

              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "40%" }}>
                        Contract <span className="red">*</span>
                      </th>
                      <th style={{ width: "45%" }}>
                        Responsible Executives{" "}
                        <span className="red">*</span>
                      </th>
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
                                />
                              )}
                            />
                            {errors.contractExecutionExecutives?.[index]
                              ?.contracts_id_fk && (
                              <span className="red">Required</span>
                            )}
                          </td>
                          <td>
                            <Controller
                              name={`contractExecutionExecutives.${index}.excecutives`}
                              control={control}
                              rules={{
                                required: "Please select executives",
                              }}
                              render={({ field }) => (
                                <Select
                                  {...field}
                                  options={formatResponsiblePeople}
                                  isMulti
                                  placeholder="Select Executives"
                                  classNamePrefix="react-select"
                                />
                              )}
                            />
                            {errors.contractExecutionExecutives?.[index]
                              ?.excecutives && (
                              <span className="red">Required</span>
                            )}
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() =>
                                removeContractExecutionExecutives(index)
                              }
                              disabled={isSubmitting}
                            >
                              <MdOutlineDeleteSweep size="26" />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td
                          colSpan="3"
                          className="text-center text-muted"
                        >
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
                      excecutives: "",
                    })
                  }
                  disabled={isSubmitting}
                >
                  <BiListPlus size="24" />
                </button>
              </div>
            </div>

            {/* Basic info: cost + unit + remarks */}
            <div className="form-row">
              <div className="form-field">
                <label>Original Target Date</label>
                <input
                  {...register("target_date")}
                  type="date"
                  placeholder="Enter Value"
                  disabled={isSubmitting}
                />
              </div>

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
                        className={styles["unit-select"]}
                        isDisabled={isSubmitting}
                      />
                    )}
                  />
                </div>
              </div>

              <div
                className="form-field"
                style={{ flex: "2 1 100%" }}
              >
                <label>Remarks</label>
                <textarea
                  {...register("remarks")}
                  maxLength={1000}
                  rows="3"
                  placeholder="Enter remarks..."
                  disabled={isSubmitting}
                ></textarea>
                <div
                  style={{
                    fontSize: "12px",
                    color: "#555",
                    textAlign: "right",
                  }}
                >
                  {watchRemarks?.length || 0}/1000
                </div>
              </div>
            </div>

            {/* Lat / Long / Dates */}
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
                <div
                  style={{
                    fontSize: "12px",
                    color: "#555",
                    textAlign: "right",
                  }}
                >
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
                <div
                  style={{
                    fontSize: "12px",
                    color: "#555",
                    textAlign: "right",
                  }}
                >
                  {watchLongitude.length}/15
                </div>
              </div>

              <div className="form-field">
                <label>
                  Construction Start Date{" "}
                  <span className="red">*</span>
                </label>
                <input
                  {...register("construction_start_date", {
                    required: true,
                  })}
                  type="date"
                  placeholder="Enter Value"
                  disabled={isSubmitting}
                />
                {errors.construction_start_date && (
                  <span className="text-danger">Required</span>
                )}
              </div>

              <div className="form-field">
                <label>
                  Target Completion Date{" "}
                  <span className="red">*</span>
                </label>
                <input
                  {...register("revised_completion", {
                    required: true,
                  })}
                  type="date"
                  placeholder="Enter Value"
                  disabled={isSubmitting}
                />
                {errors.revised_completion && (
                  <span className="text-danger">Required</span>
                )}
              </div>
            </div>

            {/* Structure Details */}
            <div className="row mt-1 mb-2">
              <h3 className="mb-1 d-flex align-center justify-content-center">
                Structure Details
              </h3>
              <div className="table-responsive dataTable ">
                <table className="table table-bordered align-middle">
                  <thead className="table-light">
                    <tr>
                      <th style={{ width: "45%" }}>
                        Structure Detail
                      </th>
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
                              {...register(
                                `structureDetails.${index}.structure_details`
                              )}
                              className="form-control"
                              placeholder="Enter detail description"
                              disabled={isSubmitting}
                            />
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(
                                `structureDetails.${index}.structure_values`
                              )}
                              className="form-control"
                              placeholder="Enter value"
                              disabled={isSubmitting}
                            />
                          </td>
                          <td className="text-center d-flex align-center justify-content-center">
                            <button
                              type="button"
                              className="btn btn-outline-danger"
                              onClick={() =>
                                removeStructureDetails(index)
                              }
                              disabled={isSubmitting}
                            >
                              <MdOutlineDeleteSweep size="26" />
                            </button>
                          </td>
                        </tr>
                      ))
                    ) : (
                      <tr>
                        <td
                          colSpan="3"
                          className="text-center text-muted"
                        >
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
                      structure_values: "",
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
              <h3 className="mb-1 d-flex align-center justify-content-center">
                Documents
              </h3>

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
                                  isDisabled={isSubmitting}
                                />
                              )}
                            />
                          </td>
                          <td>
                            <input
                              type="text"
                              {...register(
                                `documents.${index}.structureDocumentNames`
                              )}
                              className="form-control"
                              placeholder="File Name"
                              disabled={isSubmitting}
                            />
                            {/* hidden attachment field for existing file name */}
                            <input
                              type="hidden"
                              {...register(
                                `documents.${index}.attachment`
                              )}
                            />
                          </td>
                          <td>
                            <div
                              className={
                                styles["file-upload-wrapper"]
                              }
                            >
                              <label
                                htmlFor={`file-${index}`}
                                className={
                                  styles["file-upload-label-icon"]
                                }
                                style={
                                  isSubmitting
                                    ? {
                                        opacity: 0.6,
                                        cursor: "not-allowed",
                                      }
                                    : {}
                                }
                              >
                                <RiAttachment2
                                  size={20}
                                  style={{ marginRight: "6px" }}
                                />
                                {isSubmitting
                                  ? "Uploading..."
                                  : "Upload File"}
                              </label>
                              <input
                                id={`file-${index}`}
                                type="file"
                                {...register(
                                  `documents.${index}.structureFiles`
                                )}
                                className={
                                  styles["file-upload-input"]
                                }
                                disabled={isSubmitting}
                              />
                              {watch(
                                `documents.${index}.structureFiles`
                              )?.[0]?.name && (
                                <p
                                  style={{
                                    marginTop: "6px",
                                    fontSize: "0.9rem",
                                    color: "#475569",
                                  }}
                                >
                                  {
                                    watch(
                                      `documents.${index}.structureFiles`
                                    )?.[0]?.name
                                  }
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
                        <td
                          colSpan="4"
                          className="text-center text-muted"
                        >
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
                      structureFiles: "",
                      attachment: "",
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
                disabled={isSubmitting}
              >
                {isSubmitting
                  ? isEdit
                    ? "Updating..."
                    : "Saving..."
                  : isEdit
                  ? "Update"
                  : "Save"}
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
