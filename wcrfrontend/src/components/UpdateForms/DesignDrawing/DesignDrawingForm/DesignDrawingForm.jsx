import React, { useState, useEffect } from "react";
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
	const { state } = useLocation();
	const designId = state?.design_id;    

	const isEdit = Boolean(designId);
	const [projectOptions, setProjectOptions] = useState([]);
	const [contractOptions, setContractOptions] = useState([]);
	const [contractListOptions, setContractListOptions] = useState([]);
	const [preparedByOptions, setPreparedByOptions] = useState([]);
	const [approvingRailwayOptions, setApprovingRailwayOptions] = useState([]);
	const [structureTypeOptions, setStructureTypeOptions] = useState([]);
	const [drawingTypeOptions, setDrawingTypeOptions] = useState([]);
	const [revisionStatusOptions, setRevisionStatusOptions] = useState([]);
	const [approvalAuthorityOptions, setApprovalAuthorityOptions] = useState([]);
	const [structureOptions, setStructureOptions] = useState([]);
	const [stageOptions, setStageOptions] = useState([]);
	const [submittedOptions, setSubmittedOptions] = useState([]);
	const [submissionPurposeOptions, setSubmissionPurposeOptions] = useState([]);
	const [designFileTypeOptions, setDesignFileTypeOptions] = useState([]);
	const [asBuiltStatusOptions, setAsBuiltStatusOptions] = useState([]);

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
	        revisionDetailsFields: [
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

	const {
	  fields: revisionDetailsFields,
	  append: appendRevisionDetails,
	  remove: removeRevisionDetails,
	  replace
	} = useFieldArray({
	  control,
	  name: "revisionDetailsFields",
	});





	useEffect(() => {
		fetch(`${API_BASE_URL}/design/ajax/form/add-design-form`, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({})
		})
			.then(res => res.json())
			.then(data => {

				// Project Dropdown
				setProjectOptions(
					data.projectsList?.map(p => ({
						value: p.project_id,	
						label: `${p.project_id}${p.project_name ? " - " + p.project_name : ""}`,
						work_code: p.work_code  
					})) || []
				);

				// Contract Dropdown
				setContractOptions(
					data.contractsList?.map(c => ({
						value: c.contract_id,
						label: `${c.contract_id}${c.contract_short_name ? " - " + c.contract_short_name : ""}`
					})) || []
				);

				// Contract List Dropdown (Optional field)
				setContractListOptions(
					data.contractList?.map(c => ({
						value: c.contract_id,
						label: c.contract_name
					})) || []
				);

				// Prepared By Dropdown
				setPreparedByOptions(
					data.preparedBy?.map(p => ({
						value: p.prepared_by_id_fk,
						label: p.prepared_by_id_fk
					})) || []
				);


				// Approving Railway
				setApprovingRailwayOptions(
					data.approvingRailway?.map(a => ({
						value: a.railway_id,
						label: a.railway_id
					})) || []
				);

				// Structure Types
				setStructureTypeOptions(
					data.structureTypeList?.map(s => ({
						value: s.structure_type_fk,
						label: s.structure_type_fk
					})) || []
				);

				// Structure List (Structure IDs)
				setStructureOptions(
					data.structureId?.map(s => ({
						value: s.structure,
						label: s.structure
					})) || []
				);

				// Drawing Types
				setDrawingTypeOptions(
					data.drawingTypeList?.map(d => ({
						value: d.drawing_type_fk,
						label: d.drawing_type_fk
					})) || []
				);

				// Revision Statuses
				setRevisionStatusOptions(
				  data.revisionStatuses?.map(r => ({
				    value: r.revision_status,
				    label: r.revision_status
				  })) || []
				);


				// Approval Authorities
				setApprovalAuthorityOptions(
					data.approvalAuthority?.map(a => ({
						value: a.approval_authority_fk,
						label: a.approval_authority_fk
					})) || []
				);

				// Stage
				setStageOptions(
					data.stage?.map(s => ({
						value: s.stage_fk,
						label: s.stage_fk
					})) || []
				);

				// Submitted Status
				setSubmittedOptions(
					data.submitted?.map(s => ({
						value: s.submitted_id,
						label: s.submitted_status
					})) || []
				);

				// Submission Purpose
				setSubmissionPurposeOptions(
					data.submssionpurpose?.map(s => ({
						value: s.purpose_id,
						label: s.purpose_name
					})) || []
				);

				// Design File Types
				setDesignFileTypeOptions(
					data.designFileType?.map(f => ({
						value: f.file_type_id,
						label: f.file_type_name
					})) || []
				);

				// As-Built Status
				setAsBuiltStatusOptions(
					data.asBuiltStatuses?.map(a => ({
						value: a.as_built_status_id,
						label: a.as_built_status
					})) || []
				);

			});
	}, []);


	const [designDetails, setDesignDetails] = useState(null);

	useEffect(() => {
	  if (!isEdit || !designId) return;

	  fetch(`${API_BASE_URL}/design/ajax/form/get-design/designDetails`, {
	    method: "POST",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify({ design_id: designId }),
	  })
	    .then((res) => res.json())
	    .then((data) => {
	      console.log("Full Design Details:", data);   
	      setDesignDetails(data);
	    });
	}, [designId, isEdit]);

	useEffect(() => {
	  if (!isEdit || !designDetails) return;

	  const optionsLoaded =
	    projectOptions.length &&
	    approvingRailwayOptions.length &&
	    structureTypeOptions.length &&
	    structureOptions.length &&
	    drawingTypeOptions.length &&
	    approvalAuthorityOptions.length &&
	    preparedByOptions.length &&
	    contractOptions.length;

	  if (!optionsLoaded) return;

	  const d = designDetails;

	  // Helper to match dropdown values
	  const findOption = (options, value) => {
	    if (!value) return null;
	    const clean = String(value).trim().toUpperCase();
	    return (
	      options.find(o => String(o.value).trim().toUpperCase() === clean) ||
	      { value: clean, label: clean }
	    );
	  };

	  // Normalize dd-MM-yyyy -> yyyy-MM-dd
	  const normalizeDate = (raw) => {
	    if (!raw) return "";
	    if (/^\d{2}-\d{2}-\d{4}$/.test(raw)) {
	      const [dd, mm, yyyy] = raw.split("-");
	      return `${yyyy}-${mm}-${dd}`;
	    }
	    return raw.substring(0, 10);
	  };

	  const values = {
	    component: d.component || "",
	    consultant_contract_id_fk: d.consultant_contract_id_fk || "",
	    proof_consultant_contract_id_fk: d.proof_consultant_contract_id_fk || "",
	    threepvc: d.threepvc || "",
	    drawing_title: d.drawing_title || "",
	    remarks: d.remarks || "",
	    contractor_drawing_no: d.contractor_drawing_no || "",
	    mrvc_drawing_no: d.mrvc_drawing_no || "",
	    division_drawing_no: d.division_drawing_no || "",
	    hq_drawing_no: d.hq_drawing_no || "",

	    required_date: normalizeDate(d.required_date),
	    gfc_released: normalizeDate(d.gfc_released),
	  };

	  // ------------------------
	  // Dropdown Prefill
	  // ------------------------
	  values.project_id_fk = findOption(projectOptions, d.project_id_fk);
	  values.approving_railway = findOption(approvingRailwayOptions, d.approving_railway);
	  values.structure_type_fk = findOption(structureTypeOptions, d.structure_type_fk);
	  values.structure_id_fk = findOption(structureOptions, d.structure_id_fk);
	  values.drawing_type_fk = findOption(drawingTypeOptions, d.drawing_type_fk);
	  values.approval_authority_fk = findOption(approvalAuthorityOptions, d.approval_authority_fk);
	  values.prepared_by_id_fk = findOption(preparedByOptions, d.prepared_by_id_fk);
	  values.contract_id_fk = findOption(contractOptions, d.contract_id_fk);


	  reset(values);

	// Revision Table
	  if (Array.isArray(d.designRevisions) && d.designRevisions.length > 0) {
	    const cleaned = d.designRevisions.map((r, i) => ({
	      revisions: r.revision || `R${i + 1}`,
	      drawing_nos: r.drawing_no || "",
	      correspondence_letter_nos: r.correspondence_letter_no || "",
	      revision_dates: normalizeDate(r.revision_date),
	      revision_status: r.revision_status_fk || "",
	      remarkss: r.remarks || "",
	      uploadFileNames: r.upload_file || "",
	      uploadFiles: null, 
	      current: r.current === "Y" || r.current === true,
	    }));

	    replace(cleaned);
	  }

	}, [
	  isEdit,
	  designDetails,
	  projectOptions,
	  approvingRailwayOptions,
	  structureTypeOptions,
	  structureOptions,
	  drawingTypeOptions,
	  approvalAuthorityOptions,
	  preparedByOptions,
	  contractOptions,
	  reset,
	  replace
	]);



	
	
	const normalizeSelectFields = (data, fields) => {
	  fields.forEach((field) => {
	    if (data[field] && typeof data[field] === "object") {
	      data[field] = data[field]?.value || "";
	    }
	  });
	};



	const onSubmitSave = async (data) => {
	  try {
	    console.log("Saving Design:", data);

	    const formData = new FormData();

	    // Normalize selects
	    normalizeSelectFields(data, [
	      "project_id_fk",
	      "approving_railway",
	      "structure_type_fk",
	      "structure_id_fk",
	      "drawing_type_fk",
	      "approval_authority_fk",
	      "contract_id_fk",
	      "prepared_by_id_fk",
	    ]);
	    const workCodeValue = data.project_id_fk;

	    formData.append("work_code", workCodeValue);

	    console.log("WORK CODE SENT:", workCodeValue);

	    // Append all normal fields except revision rows
	    Object.keys(data).forEach((key) => {
	      if (key !== "revisionDetails" && data[key] != null) {
	        formData.append(key, data[key]);
	      }
	    });

	    // REVISION rows (ADD MODE File REQUIRED)
	    data.revisionDetailsFields.forEach((item) => {
	      formData.append("revisions", item.revisions || "");
	      formData.append("drawing_nos", item.drawing_nos || "");
	      formData.append("correspondence_letter_nos", item.correspondence_letter_nos || "");

	      formData.append(
	        "revision_dates",
	        item.revision_dates
	          ? new Date(item.revision_dates).toISOString().slice(0, 10)
	          : ""
	      );

	      formData.append("revision_status_fks", item.revision_status || "");
	      formData.append("remarkss", item.remarkss || "");
	      formData.append("currents", item.current ? "Y" : "N");

	      // Upload file must be present
	      if (!item.uploadFiles?.length) {
	        alert("Please upload a file for each revision row.");
	        throw new Error("Missing file");
	      }

	      const file = item.uploadFiles[0];
	      formData.append("uploadFiles", file);
	      formData.append("uploadFileNames", file.name);
	    });

	    // API call
	    const response = await fetch(`${API_BASE_URL}/design/add-design`, {
	      method: "POST",
	      body: formData,
	      credentials: "include",
	    });

	    if (!response.ok) {
	      alert("Save Failed!");
	      return;
	    }

	    alert("Design Saved Successfully!");
	    navigate("/updateforms/design");

	  } catch (err) {
	    console.error("Save Error:", err);
	  }
	};

	
	
	const onSubmitUpdate = async (data) => {
	  try {
	    console.log("Updating Design:", data);

	    const formData = new FormData();

	    // Required fields
	    formData.append("design_id", designId);
	    formData.append("design_seq_id", designDetails?.design_seq_id || "");

	    // Normalize select fields
	    normalizeSelectFields(data, [
	      "project_id_fk",
	      "approving_railway",
	      "structure_type_fk",
	      "structure_id_fk",
	      "drawing_type_fk",
	      "approval_authority_fk",
	      "contract_id_fk",
	      "prepared_by_id_fk",
	    ]);

	    // Simple fields
	    Object.keys(data).forEach((key) => {
	      if (key !== "revisionDetails" && key !== "revisionDetailsFields") {
	        if (data[key] != null) formData.append(key, data[key]);
	      }
	    });

	    // ===== EXACT FORMAT THAT BACKEND EXPECTS =====
		data.revisionDetailsFields.forEach((item) => {
		  
		  formData.append("revisions", item.revisions || "");
		  formData.append("drawing_nos", item.drawing_nos || "");
		  formData.append("correspondence_letter_nos", item.correspondence_letter_nos || "");

		  formData.append(
		    "revision_dates",
		    item.revision_dates
		      ? new Date(item.revision_dates).toISOString().slice(0, 10)
		      : ""
		  );

		  formData.append("revision_status_fks", item.revision_status || "");
		  formData.append("remarkss", item.remarkss || "");
		  formData.append("currents", item.current ? "Y" : "N");

		  // ALWAYS append uploadFiles and uploadFileNames
		  if (item.uploadFiles?.length > 0) {
		    const file = item.uploadFiles[0];
		    formData.append("uploadFiles", file);
		    formData.append("uploadFileNames", file.name);
		  } 
		  else {
		    // IMPORTANT — send placeholders to keep arrays aligned
		    formData.append("uploadFiles", new Blob([]), "");  // empty file
		    formData.append("uploadFileNames", item.uploadFileNames || "");
		  }

		});


	    // API call
	    const response = await fetch(`${API_BASE_URL}/design/update-design`, {
	      method: "POST",
	      body: formData,
	      credentials: "include",
	    });

	    if (!response.ok) {
	      alert("Update Failed!");
	      return;
	    }

	    alert("Design Updated Successfully!");
	    navigate("/updateforms/design");

	  } catch (err) {
	    console.error("Update Error:", err);
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
				<form onSubmit={handleSubmit(isEdit ? onSubmitUpdate : onSubmitSave)} 
				      encType="multipart/form-data">

						<h6 className="d-flex justify-content-center mt-1 mb-2">Work Details</h6>

						{/* Row 1 */}
						<div className="form-row">
							{/* ===================== PROJECT ===================== */}
							<div className="form-field">
							  <label>Project <span className="red">*</span></label>

							  <Controller
							    name="project_id_fk"
							    control={control}
							    rules={{ required: true }}
							    render={({ field }) => (
							      <Select
							        {...field}
							        options={projectOptions}
							        classNamePrefix="react-select"
							        placeholder="Select Project"
							        isSearchable
							        onChange={(selected) => {
							          field.onChange(selected);
							        }}
							      />
							    )}
							  />


							  {errors.project_id_fk && <span className="red">Required</span>}
							</div>


							{/* ===================== APPROVING RAILWAY ===================== */}
							<div className="form-field">
								<label>Approving Railway <span className="red">*</span></label>
								<Controller
									name="approving_railway"
									control={control}
									rules={{ required: true }}
									render={({ field }) => (
										<Select
											{...field}
											options={approvingRailwayOptions}
											classNamePrefix="react-select"
											placeholder="Select Value"
											isSearchable
										/>
									)}
								/>
								{errors.approving_railway && <span className="red">Required</span>}
							</div>

							{/* ===================== STRUCTURE TYPE ===================== */}
							<div className="form-field">
								<label>Structure Type <span className="red">*</span></label>
								<Controller
									name="structure_type_fk"
									control={control}
									rules={{ required: true }}
									render={({ field }) => (
										<Select
											{...field}
											options={structureTypeOptions}
											classNamePrefix="react-select"
											placeholder="Select Value"
											isSearchable
										/>
									)}
								/>
								{errors.structure_type_fk && <span className="red">Required</span>}
							</div>

							{/* ===================== STRUCTURE ===================== */}
							<div className="form-field">
								<label>Structure <span className="red">*</span></label>
								<Controller
									name="structure_id_fk"
									control={control}
									rules={{ required: true }}
									render={({ field }) => (
										<Select
											{...field}
											options={structureOptions}
											classNamePrefix="react-select"
											placeholder="Select Value"
											isSearchable
										/>
									)}
								/>
								{errors.structure_id_fk && <span className="red">Required</span>}
							</div>

							{/* ===================== COMPONENT ===================== */}
							<div className="form-field">
							  <label>Component</label>
							  <input
							    {...register("component")}
							    type="text"
							    placeholder="Enter Component"
							  />
							</div>


							{/* ===================== CONTRACT ===================== */}
							<div className="form-field">
								<label>Contract</label>
								<Controller
									name="contract_id_fk"
									control={control}
									render={({ field }) => (
										<Select
											{...field}
											options={contractOptions}
											classNamePrefix="react-select"
											placeholder="Select"
											isSearchable
										/>
									)}
								/>
							</div>

							{/* ===================== PREPARED BY ===================== */}
							<div className="form-field">
								<label>Prepared By</label>
								<Controller
									name="prepared_by_id_fk"
									control={control}
									render={({ field }) => (
										<Select
											{...field}
											options={preparedByOptions}
											classNamePrefix="react-select"
											placeholder="Select"
											isSearchable
										/>
									)}
								/>
							</div>

							{/* ===================== CONSULTANT ===================== */}
							<div className="form-field">
							  <label>Consultant</label>
							  <input
							    {...register("consultant_contract_id_fk")}
							    type="text"
							    placeholder="Enter Consultant"
							  />
							</div>

							{/* ===================== PROOF CONSULTANT ===================== */}
							<div className="form-field">
							  <label>Proof Consultant</label>
							  <input
							    {...register("proof_consultant_contract_id_fk")}
							    type="text"
							    placeholder="Enter Proof Consultant"
							  />
							</div>

							{/* ===================== 3PVC ===================== */}
							<div className="form-field">
							  <label>3PVC</label>
							  <input
							    {...register("threepvc")}
							    type="text"
							    placeholder="Enter 3PVC"
							  />
							</div>



							{/* ===================== DRAWING TYPE ===================== */}
							<div className="form-field">
								<label>Drawing Type <span className="red">*</span></label>
								<Controller
									name="drawing_type_fk"
									control={control}
									rules={{ required: true }}
									render={({ field }) => (
										<Select
											{...field}
											options={drawingTypeOptions}
											classNamePrefix="react-select"
											placeholder="Select Value"
											isSearchable
										/>
									)}
								/>
								{errors.drawing_type_fk && <span className="red">Required</span>}
							</div>

							{/* ===================== APPROVAL AUTHORITY ===================== */}
							<div className="form-field">
								<label>Approval Authority <span className="red">*</span></label>
								<Controller
									name="approval_authority_fk"
									control={control}
									rules={{ required: true }}
									render={({ field }) => (
										<Select
											{...field}
											options={approvalAuthorityOptions}
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
								<input {...register("gfc_released")} type="date" />
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
															{...register(`revisionDetailsFields.${index}.revisions`)}
															className="form-control"
															value={(`R${index + 1}`)} 
														/>
													</td>
													<td>
														<input
															type="text"
															{...register(`revisionDetailsFields.${index}.drawing_nos`, { required: "required" })}
															className="form-control"
														/>
														{errors.revisionDetailsFields?.[index]?.drawing_nos && (
															<span className="red">
																{errors.revisionDetailsFields[index].drawing_nos.message}
															</span>
														)}
													</td>
													<td>
														<input
															type="text"
															{...register(`revisionDetailsFields.${index}.correspondence_letter_nos`, { required: "required" })}
															className="form-control"
														/>
														{errors.revisionDetailsFields?.[index]?.correspondence_letter_nos && (
															<span className="red">
																{errors.revisionDetailsFields[index].correspondence_letter_nos.message}
															</span>
														)}
													</td>
													<td>
														<input
															type="date"
															{...register(`revisionDetailsFields.${index}.revision_dates`, { required: "required" })}
															className="form-control"
														/>
														{errors.revisionDetailsFields?.[index]?.revision_dates && (
															<span className="red">
																{errors.revisionDetailsFields[index].revision_dates.message}
															</span>
														)}
													</td>
													<td>
													  <select
													    {...register(`revisionDetailsFields.${index}.revision_status`, {
													      required: "required",
													    })}
													    className="form-control"
													    defaultValue={item.revision_status || ""}
													  >
													    <option value="">Select Status</option>
													    {revisionStatusOptions.map((opt) => (
													      <option key={opt.value} value={opt.value}>
													        {opt.label}
													      </option>
													    ))}
													  </select>

													  {errors.revisionDetailsFields?.[index]?.revision_status && (
													    <span className="red">
													      {errors.revisionDetailsFields[index].revision_status.message}
													    </span>
													  )}
													</td>




													<td>
														<input
															type="text"
															{...register(`revisionDetailsFields.${index}.remarkss`)}
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
													      {...register(`revisionDetailsFields.${index}.uploadFiles`)}
													      className={styles["file-upload-input"]}
													    />

													    {/* Show NEW filename OR EXISTING filename */}
													    {watch(`revisionDetailsFields.${index}.uploadFiles`)?.[0]?.name ? (
													      <p>{watch(`revisionDetailsFields.${index}.uploadFiles`)[0].name}</p>
													    ) : watch(`revisionDetailsFields.${index}.uploadFileNames`) ? (
													      <p>
													        <a 
													          href={`/wrpmis/DESIGN_REVISION_FILES/${watch(`revisionDetails.${index}.uploadFileNames`)}`} 
													          target="_blank"
													        >
													          {watch(`revisionDetailsFields.${index}.uploadFileNames`)}
													        </a>
													      </p>
													    ) : null}

													  </div>
													</td>

													<td>
														<input
															type="checkbox"
															{...register(`revisionDetailsFields.${index}.current`)}
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
								<label>Remarks: <span className="red">*</span></label>
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