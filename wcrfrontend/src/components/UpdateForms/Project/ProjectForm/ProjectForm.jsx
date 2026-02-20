import React, { useEffect, useState } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from "./ProjectForm.module.css";
import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';

export default function ProjectForm() {
	const navigate = useNavigate();
	const { state } = useLocation(); 
	const isEdit = Boolean(state?.project);

	const [projectTypes, setProjectTypes] = useState([]);
	const [railwayZones, setRailwayZones] = useState([]);
	const [yearList, setYearList] = useState([]);
	const [divisions, setDivisions] = useState([]);
	const [sections, setSections] = useState([]);

	const projectStatusOptions = [
	  { value: "Open", label: "Open" },
	  { value: "Closed", label: "Closed" }
	];
	
	const projectTypeOptions = projectTypes.map(pt => ({
	  value: pt.project_type_id,
	  label: pt.project_type_name
	}));
	
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
			project_status: { value: "", label: "" },
			project_type_id: { value: "", label: "" },
			railway_zone: { value: "", label: "" },
			plan_head_number: "",
			financial_year: { value: "", label: "" },
			sanctioned_amount: "",
			sanctioned_commissioning_date: "",
			division_id: { value: "", label: "" },
			section_id: { value: "", label: "" },
			pink_book_item_numbers: "",
			actual_completion_cost: "",
			actual_completion_date: "",
			benefits: "",
			remarks: "",

			// Completion Costs table
			completionCosts: [
				{ date: "", estimatedCost: "", revisedDate: "" }
			],

			// Commission Length Chainage table
			commissionedLength: [
				{ fromChainage: "", toChainage: "", commissionedLength: "" }
			]
		},
	});

	// Completion Cost Table FieldArray
	const { fields: costFields, append: appendCost, remove: removeCost } =
		useFieldArray({
			control,
			name: "completionCosts",
		});

	// CommissionedLength FieldArray
	const { fields: CommissionedLengthFields, append: appendCommissionedLength, remove: removeCommissionedLength } =
		useFieldArray({
			control,
			name: "commissionedLength",
		});


	// Fetch dropdown data from API
	useEffect(() => {
		const fetchDropdowns = async () => {
			try {
				const [
					typesRes,
					zonesRes,
					yearsRes,
					divisionsRes,
					sectionsRes
				] = await Promise.all([
					api.get(`${API_BASE_URL}/projects/api/projectTypes`, { withCredentials: true }),
					api.get(`${API_BASE_URL}/projects/api/railwayZones`, { withCredentials: true }),
					api.get(`${API_BASE_URL}/projects/api/yearList`, { withCredentials: true }),
					api.get(`${API_BASE_URL}/projects/api/divisions`, { withCredentials: true }),
					api.get(`${API_BASE_URL}/projects/api/sections`, { withCredentials: true }),
				]);

				setProjectTypes(typesRes.data);
				setRailwayZones(zonesRes.data);
				setYearList(yearsRes.data);
				setDivisions(divisionsRes.data);
				setSections(sectionsRes.data);

			} catch (err) {
				console.error("Error fetching dropdowns:", err);
				alert("Error fetching dropdowns: " + err.message);
			}
		};

		fetchDropdowns();
	}, []);

	// Prefill form if editing
	useEffect(() => {
		if (isEdit && state?.project) {
			const project = state.project;

			setValue("project_name", project.project_name ?? "");
			setValue("plan_head_number", project.plan_head_number ?? "");
			setValue("sanctioned_amount", project.sanctioned_amount ?? "");
			setValue("sanctioned_commissioning_date", project.sanctioned_commissioning_date ?? "");
			setValue("pink_book_item_numbers", project.pink_book_item_numbers ?? "");
			setValue("actual_completion_cost", project.actual_completion_cost ?? "");
			setValue("actual_completion_date", project.actual_completion_date ?? "");
			setValue("benefits", project.benefits ?? "");
			setValue("remarks", project.remarks ?? "");
			setValue("project_status", project.project_status ? { value: project.project_status, label: project.project_status } : { value: "", label: "" });
			setValue("project_type_id", project.project_type_id ? { value: project.project_type_id, label: project.project_type_name || project.project_type_id } : { value: "", label: "" });
			setValue("railway_zone", project.railway_zone ? { value: project.railway_zone, label: project.railway_zone_name || project.railway_zone } : { value: "", label: "" });
			setValue("financial_year", project.financial_year ? { value: project.financial_year, label: project.financial_year } : { value: "", label: "" });
			setValue("division_id", project.division_id ? { value: project.division_id, label: project.division_name || project.division_id } : { value: "", label: "" });
			setValue("section_id", project.section_id ? { value: project.section_id, label: project.section_name || project.section_id } : { value: "", label: "" });

			if (project.commissionedlength && project.commissionedlength.length) {
				project.commissionedlength.forEach((com, index) => {
					if (index === 0) {
						setValue(`commissionedlength.${index}.fromChainage`, com.fromChainage ?? "");
						setValue(`commissionedlength.${index}.toChainage`, com.toChainage ?? "");
						setValue(`commissionedlength.${index}.commissionedLength`, com.commissionedLength ?? "");
					} else {
						appendCommissionedLength({
							fromChainage: com.fromChainage ?? "",
							toChainage: com.toChainage ?? "",
							commissionedLength: com.commissionedLength ?? "",
						});
					}
				});
			}
			if (project.completionCosts && project.completionCosts.length) {
				project.completionCosts.forEach((cost, index) => {
					if (index === 0) {
						setValue(`completionCosts.${index}.date`, cost.date ?? "");
						setValue(`completionCosts.${index}.estimatedCost`, cost.estimatedCost ?? "");
						setValue(`completionCosts.${index}.revisedDate`, cost.revisedDate ?? "");
					} else {
						appendCost({
							date: cost.date ?? "",
							estimatedCost: cost.estimatedCost ?? "",
							revisedDate: cost.revisedDate ?? "",
						});
					}
				});
			}
		}
	}, [state, setValue, isEdit, appendCost, appendCommissionedLength]);


	const onSubmit = async (formData) => {
		try {
			const payload = {
				...formData,
				project_id: isEdit ? state.project.project_id : undefined,
				project_status: formData.project_status?.value || "",
				project_type_id: formData.project_type_id?.value || "",
				railway_zone: formData.railway_zone?.value || "",
				financial_year: formData.financial_year?.value || "",
				division_id: formData.division_id?.value || "",
				section_id: formData.section_id?.value || "",
				commissionedLength: formData.commissionedLength.map(com => ({
					fromChainage: com.fromChainage || "",
					toChainage: com.toChainage || "",
					commissionedLength: com.commissionedLength || ""
				})),
				completionCosts: formData.completionCosts.map(cost => ({
					date: cost.date || "",
					estimatedCost: cost.estimatedCost || "",
					revisedDate: cost.revisedDate || ""
				})),

			};

			if (typeof formData.pink_book_item_numbers === "string") {
				if (formData.pink_book_item_numbers.trim() === "") {
					delete payload.pink_book_item_numbers;
				} else {
					payload.pink_book_item_numbers = formData.pink_book_item_numbers.split(",").map(s => s.trim());
				}
			}

			const arrayFields = [
				"pink_book_item_numbers",
				"financial_years",
				"railways",
				"projectFileNames",
				"project_file_types",
				"project_file_ids",
				"created_dates",
				"completion_dates",
				"estimated_completion_costs",
				"revised_completion_dates"
			];


			arrayFields.forEach(f => {
				if (!Array.isArray(payload[f])) {
					delete payload[f];
				}
			});

			console.log("FINAL PAYLOAD:", payload);

			let response;

			if (isEdit) {
				response = await api.put(
					`${API_BASE_URL}/projects/api/updateProject/${state.project.project_id}`,
					payload,
					{ headers: { "Content-Type": "application/json" }, withCredentials: true }
				);
			} else {
				response = await api.post(
					`${API_BASE_URL}/projects/api/addProject`,
					payload,
					{ headers: { "Content-Type": "application/json" }, withCredentials: true }
				);
			}

			if (response.status === 200) {
				navigate("/updateforms/project");
			}
		} catch (err) {

			// Show a readable message
			let errorMsg = "Error saving project. Please try again later.";
			if (err.response?.data) {
				// If backend sends a message field
				if (err.response.data.message) {
					errorMsg = `Error: ${err.response.data.message}`;
				} else {
					// Fallback: stringify object
					errorMsg = `Error: ${JSON.stringify(err.response.data)}`;
				}
			}

			alert(errorMsg);
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

						<div className="form-row">
							{/* Project Name */}
							<div className="form-field">
								<label>Project Name <span className="red">*</span></label>
								<input {...register("project_name", { required: true })} type="text" placeholder="Enter Value" />
								{errors.project_name && <span className="red">This field is required</span>}
							</div>

							{/* Project Status */}
							<div className="form-field">
								<label>Project Status <span className="red">*</span></label>
								<Controller
								  name="project_status"
								  control={control}
								  rules={{ required: true }}
								  render={({ field }) => (
								    <Select
									classNamePrefix="react-select"
								      options={projectStatusOptions}
								      placeholder="Select"
								      isSearchable
								      value={projectStatusOptions.find(o => o.value === field.value) || null}
								      onChange={o => field.onChange(o?.value)}
								    />
								  )}
								/>
								{errors.project_status && <span className="red">This field is required</span>}
							</div>

							{/* Project Type */}
							<div className="form-field">
								<label>Project Type </label>
								<Controller
								  name="project_type_id"
								  control={control}
								  render={({ field }) => (
								    <Select
								      classNamePrefix="react-select"
								      options={projectTypeOptions}
								      placeholder="Select"
								      isSearchable

								      /* ✅ show selected value */
								      value={projectTypeOptions.find(
								        option => option.value === field.value
								      ) || null}

								      /* ✅ store only ID in form */
								      onChange={option => field.onChange(option?.value)}
								    />
								  )}
								/>
							</div>

							<div className="form-field">
							  <label>Railway Zone</label>

							  <Controller
							    name="railway_zone"
							    control={control}
							    render={({ field }) => (
							      <Select
							        classNamePrefix="react-select"
							        options={railwayZones.map(zone => ({
							          value: zone.railway_id,
							          label: zone.railway_id,
							        }))}
							        placeholder="Select"
							        isSearchable
							        value={
							          railwayZones
							            .map(zone => ({
							              value: zone.railway_id,
							              label: zone.railway_id,
							            }))
							            .find(option => option.value === field.value) || null
							        }
							        onChange={option => field.onChange(option?.value)}
							      />
							    )}
							  />
							</div>

							{/* Plan Head Number */}
							<div className="form-field">
								<label>Plan Head Number <span className="red">*</span> </label>
								<input {...register("plan_head_number")} type="text" placeholder="Enter Value" />
								{errors.plan_head_number && <span className="red">This field is required</span>}
							</div>

							{/* Sanctioned Year */}
							<div className="form-field">
							  <label>Sanctioned Year</label>

							  <Controller
							    name="financial_year"
							    control={control}
							    render={({ field }) => (
							      <Select
							        classNamePrefix="react-select"
							        options={yearList.map(year => ({
							          value: year.financial_year_id,
							          label: year.financial_year,
							        }))}
							        placeholder="Select"
							        isSearchable
							        value={
							          yearList
							            .map(year => ({
							              value: year.financial_year_id,
							              label: year.financial_year,
							            }))
							            .find(option => option.value === field.value) || null
							        }
							        onChange={option => field.onChange(option?.value)}
							      />
							    )}
							  />
							</div>

							{/* Sanctioned Amount */}
							<div className="form-field">
								<label>Sanctioned Amount </label>
								<input {...register("sanctioned_amount")} type="number" placeholder="Enter Value" />
							</div>

							{/* Sanctioned Commissioning Date */}
							<div className="form-field">
								<label>Sanctioned Commissioning Date </label>
								<input {...register("sanctioned_commissioning_date")} type="date" placeholder="Select Date" />
							</div>

							{/* Division */}
							<div className="form-field">
								<label>Division </label>
								<Controller
								  name="division_id"
								  control={control}
								  render={({ field }) => (
								    <Select
								      classNamePrefix="react-select"
								      placeholder="Select Division"
								      options={divisions.map(div => ({
								        value: div.division_id,
								        label: div.division_name
								      }))}
								      value={divisions
								        .map(div => ({
								          value: div.division_id,
								          label: div.division_name
								        }))
								        .find(option => option.value === field.value) || null}

								      onChange={selectedOption =>
								        field.onChange(selectedOption ? selectedOption.value : null)
								      }

								      isSearchable
								    />
								  )}
								/>
							</div>

							{/* Section */}
							<div className="form-field">
							  <label>Section</label>

							  <Controller
							    name="section_id"
							    control={control}
							    render={({ field }) => (
							      <Select
							        classNamePrefix="react-select"
							        options={sections.map(sec => ({
							          value: sec.section_id,
							          label: sec.section_name
							        }))}
							        placeholder="Select"
							        isSearchable

							        value={
							          sections
							            .map(sec => ({
							              value: sec.section_id,
							              label: sec.section_name
							            }))
							            .find(option => option.value === field.value) || null
							        }
							        onChange={option => field.onChange(option?.value)}
							      />
							    )}
							  />
							</div>

							{/* PB Item No */}
							<div className="form-field">
								<label>PB Item No </label>
								<input {...register("pink_book_item_numbers")} type="text" placeholder="Enter Value" />
							</div>

							{/* Actual Completion Cost */}
							<div className="form-field">
								<label>Actual Completion Cost </label>
								<input {...register("actual_completion_cost")} type="text" placeholder="Enter Value" />
							</div>
						</div>

						{/* Commissioned Length */}

						<div className="row mt-1 mb-2">


							<div className={`dataTable ${styles.tableWrapper}`}>
							<h3 className="d-flex justify-content-center mt-1 mb-2">Commissioned Length</h3>

								<table className="table user-table">
									<thead>
										<tr>
											<th>S. No.</th>
											<th>From Chainage (m)</th>
											<th>To Chainage (m)</th>
											<th>Completed Length (m)</th>
											<th>Action</th>
										</tr>
									</thead>
									<tbody>
										{CommissionedLengthFields.length > 0 ? (
											
												CommissionedLengthFields.map((row, index) => (
													<tr key={row.id}>

														<td>{index + 1}</td>


														<td>
															<input
																type="number"
																step="0.01"
																{...register(`commissionedLength.${index}.fromChainage`)}
																placeholder="From"
																className="form-control"
															/>
														</td>

														<td>
															<input
																type="number"
																step="0.01"
																{...register(`commissionedLength.${index}.toChainage`)}
																placeholder="To"
															/>
														</td>

														<td>
															<input
																type="number"
																{...register(`commissionedLength.${index}.commisionedLength`)}
																placeholder="Commissioned Length"
															/>
														</td>
														<td><button type="button" className="btn btn-outline-danger" onClick={() => removeCommissionedLength(index)}>
															<MdOutlineDeleteSweep size="26" />
														</button>
														</td>
													</tr>
												))
											
										) :

											(
												<tr>
													<td colSpan="5" className="text-center text-muted">
														No Commission Length rows added yet.
													</td>
												</tr>
											)
										}
									</tbody>
								</table>
							</div>

							<div className="d-flex align-center justify-content-center mt-1">
								<button type="button" className="btn-2 btn-green" onClick={() => appendCommissionedLength({ fromChainage: "", toChainage: "", commissionedLength: "" })}>
									<BiListPlus size="24" />
								</button>
							</div>

						</div>

						{/* Completion Costs Table */}
						<div className="row mt-1 mb-2">
							<h3 className="mb-1 d-flex align-center justify-content-center">Completion Costs</h3>
							<div className="table-responsive dataTable">
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
													<td><input type="date" {...register(`completionCosts.${index}.date`)} className="form-control" /></td>
													<td><input type="text" {...register(`completionCosts.${index}.estimatedCost`)} className="form-control" placeholder="Estimated Cost" /></td>
													<td><input type="date" {...register(`completionCosts.${index}.revisedDate`)} className="form-control" /></td>
													<td className="text-center d-flex align-center justify-content-center">
														<button type="button" className="btn btn-outline-danger" onClick={() => removeCost(index)}>
															<MdOutlineDeleteSweep size="26" />
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
								<button type="button" className="btn-2 btn-green" onClick={() => appendCost({ date: "", estimatedCost: "", revisedDate: "" })}>
									<BiListPlus size="24" />
								</button>
							</div>
						</div>

						{/* Form Buttons */}
						<div className="form-post-buttons">
							<button type="submit" className="btn btn-primary">
								{isEdit ? "Update" : "Save"}
							</button>
							<button type="button" className="btn btn-white" onClick={() => navigate(-1)}>Cancel</button>
						</div>
					</form>
				</div>
			</div>
		</div>
	);
}
