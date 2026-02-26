import React, { useEffect, useState, useRef } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from "./ProjectForm.module.css";
import { API_BASE_URL } from "../../../../config";
import { MdOutlineDeleteSweep, MdRailwayAlert } from 'react-icons/md';
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
	
	const railwayZoneOptions = railwayZones.map(zone => ({
		value: zone.railway_id,
		label: zone.railway_id
	}));
	
		// React Hook Form setup
	const {
			register,
			control,
			handleSubmit,
			setValue,
			formState: { errors },
			watch
		} = useForm({
			shouldFocusError: true,
			defaultValues: {
			    project_name: "",

			    project_status: null,
			    project_type_id: null,
			    railway_zone: null,
			    division_id: null,
			    section_id: null,

			    plan_head_number: "",
			    sanctioned_amount: "",
			    sanctioned_commissioning_date: "",

			    pink_book_item_numbers: "",
			    actual_completion_cost: "",
			    actual_completion_date: "",
			    benefits: "",
			    remarks: "",
				proposed_length: "",
			

				// Completion Costs table
				completionCosts: [
					{ date: "", estimatedCost: "", revisedDate: "" }
				],

				// Commission Length Chainage table
				commissionedLength: [
					{ fromChainage: "", toChainage: "", commissionedLength: "" }
				],
				
				pinkBooks: [
				   { financial_year: "", railway: "", pb_item_no: "" }
				]
				
			},
		});
		
		
/*// pink book fiels	
	const { fields: pinkBookFields, append: appendPinkBook, remove: removePinkBook } =
	    useFieldArray({
	        control,
	        name: "pinkBooks",
	    });
		*/
		

		const { fields: costFields, append: appendCost, remove: removeCost, replace } =
		    useFieldArray({
		        control,
		        name: "completionCosts",
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

	useEffect(() => {
	    if (isEdit && state?.project) {

	        const project = state.project;

	        setValue("project_name", project.project_name ?? "");
	        setValue("plan_head_number", project.plan_head_number ?? "");
	        setValue("sanctioned_amount", project.sanctioned_amount ?? "");
	        setValue("sanctioned_commissioning_date", project.sanctioned_commissioning_date ?? "");
	        setValue("actual_completion_cost", project.actual_completion_cost ?? "");
	        setValue("actual_completion_date", project.actual_completion_date ?? "");
	        setValue("benefits", project.benefits ?? "");
	        setValue("remarks", project.remarks ?? "");

	        setValue("project_status", project.project_status ?? null);
	        setValue("project_type_id", project.project_type_id_fk ?? null);
	        setValue("railway_zone", project.railway_zone ?? null);
	        setValue("division_id", project.division_id ?? null);
	        setValue("section_id", project.section_id ?? null);
	        setValue("sanctioned_year", project.sanctioned_year ?? null);
	        setValue("proposed_length", project.proposed_length ?? null);
	        setValue("structure_details", project.structure_details ?? null);
	        setValue("from_chainage", project.from_chainage ?? null);
	        setValue("to_chainage", project.to_chainage ?? null);
	        setValue("pb_item_number", project.pb_item_number ?? null);

			const normalize = (val) =>
			    Array.isArray(val) ? val :
			    typeof val === "string" ? val.split(",") :
			    [];

			const dates = normalize(project.completion_dates);
			const costs = normalize(project.estimated_completion_costs);
			const revised = normalize(project.revised_completion_dates);

			if (dates.length > 0) {
			    replace(
			        dates.map((d, i) => ({
			            date: d || "",
			            estimatedCost: costs[i] || "",
			            revisedDate: revised[i] || ""
			        }))
			    );
			}
	    }
	}, [isEdit, state, setValue, replace]);



	const benefitsValue = watch("benefits") || "";
	const remarksValue = watch("remarks") || "";

	const fetchDivisions = async (railwayZone) => {

		const res = await api.post(
			`${API_BASE_URL}/projects/api/get-divisions`,
			null,
			{
				params: { railwayZone },
				withCredentials: true
			}
		);
		setDivisions(res.data.divisionsList);
	};

	const selectedZone = watch("railway_zone");

	useEffect(() => {
		if (!selectedZone) return;

		fetchDivisions(selectedZone);

	}, [selectedZone]);







	const onSubmit = async (formData) => {
	    try {

	        const payload = {

	            project_id: isEdit ? state.project.project_id : undefined,

	            project_name: formData.project_name || null,
	            plan_head_number: formData.plan_head_number || null,
	            project_status: formData.project_status || null,
	            project_type_id: formData.project_type_id || null,
	            railway_zone: formData.railway_zone || null,
				pb_item_number: formData.pb_item_number || null,
				

	            sanctioned_year: formData.sanctioned_year || null,

	            sanctioned_amount: formData.sanctioned_amount
	                ? Number(formData.sanctioned_amount)
	                : null,

	            sanctioned_commissioning_date:
	                formData.sanctioned_commissioning_date || null,

	            actual_completion_cost: formData.actual_completion_cost
	                ? Number(formData.actual_completion_cost)
	                : null,

	            actual_completion_date:
	                formData.actual_completion_date || null,

	            benefits: formData.benefits || null,
	            remarks: formData.remarks || null,

	            division_id: formData.division_id || null,
	            section_id: formData.section_id || null,
				proposed_length:formData.proposed_length || null,

	            structure_details: formData.structure_details || null,

	            from_chainage: formData.from_chainage
	                ? Number(formData.from_chainage)
	                : null,

	            to_chainage: formData.to_chainage
	                ? Number(formData.to_chainage)
	                : null,

	            completion_dates: (formData.completionCosts || []).map(
	                c => c.date || null
	            ),

	            estimated_completion_costs: (formData.completionCosts || []).map(
	                c => c.estimatedCost ? Number(c.estimatedCost) : null
	            ),

	            revised_completion_dates: (formData.completionCosts || []).map(
	                c => c.revisedDate || null
	            ),

	            /* ✅ Pink Book → Convert to ARRAYS */
	            financial_years: (formData.pinkBooks || []).map(
	                p => p.financial_year || null
	            ),

	            railways: (formData.pinkBooks || []).map(
	                p => p.railway || null
	            ),

	            pink_book_item_numbers: (formData.pinkBooks || []).map(
	                p => p.pb_item_no || null
	            ),
	        };

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

				if (isEdit) {
					alert("Project updated Successfully.");
				}
				else {
					alert("Project Added Successfully.");
				}
				navigate("/updateforms/project");
			}
			else{
				alert("failed to add or update!");
			}
			

	    } catch (err) {

	        console.error(err);

	        let errorMsg = "Error saving project.";

	        if (err.response?.data?.message) {
	            errorMsg = err.response.data.message;
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
								    rules={{ required: "Project Status is required" }}
								    render={({ field }) => (
								        <Select
										{...field}
										inputRef={field.ref}
								            classNamePrefix="react-select"
								            options={projectStatusOptions}
								            placeholder="Select"
								            isSearchable
								            value={projectStatusOptions.find(o => o.value === field.value) || null}
								            onChange={o => field.onChange(o?.value)}
								        />
								    )}
								/>

								{errors.project_status && (
								    <span className="red">{errors.project_status.message}</span>
								)}
							</div>

							{/* Project Type */}
							<div className="form-field">
								<label>Project Type <span className="red">*</span> </label>
								<Controller
								    name="project_type_id"
								    control={control}
								    rules={{ required: "Project Type is required" }}
								    render={({ field }) => (
								        <Select
										{...field}
										inputRef={field.ref}
								            classNamePrefix="react-select"
								            options={projectTypeOptions}
								            placeholder="Select"
								            isSearchable
								            value={projectTypeOptions.find(o => o.value === field.value) || null}
								            onChange={o => field.onChange(o?.value)}
								        />
								    )}
								/>
								{errors.project_type_id && (
								    <span className="red">{errors.project_type_id.message}</span>
								)}
							</div>

							<div className="form-field">
								<label>Railway Zone <span className="red">*</span> </label>

								<Controller
								    name="railway_zone"
								    control={control}
								    rules={{ required: "Railway Zone is required" }}
								    render={({ field }) => (
								        <Select
										{...field}
										inputRef={field.ref}
								            classNamePrefix="react-select"
								            options={railwayZoneOptions}
								            placeholder="Select"
								            isSearchable
								            value={railwayZoneOptions.find(o => o.value === field.value) || null}
								            onChange={o => field.onChange(o?.value)}
								        />
								    )}
								/>

								{errors.railway_zone && (
								    <span className="red">{errors.railway_zone.message}</span>
								)}
							</div>

							{/* Plan Head Number */}
							<div className="form-field">
								<label>Plan Head Number <span className="red">*</span> </label>
								<input
								    {...register("plan_head_number", {
								        required: "Plan Head Number is required"
								    })}
								    type="text"
								/>

								{errors.plan_head_number && (
								    <span className="red">{errors.plan_head_number.message}</span>
								)}
							</div>

							<div className="form-field">
							    <label>Sanctioned Year <span className="red">*</span></label>

							    <Controller
							        name="sanctioned_year"
							        control={control}
							        rules={{ required: "Sanctioned Year is required" }}
							        render={({ field }) => {

							            const options = yearList.map(y => ({
							                value: y.financial_year,
							                label: y.financial_year
							            }));

							            return (
							                <Select
											{...field}
											inputRef={field.ref}
							                    classNamePrefix="react-select"
							                    options={options}
							                    placeholder="Select"
							                    isSearchable
							                    value={options.find(o => o.value === field.value) || null}
							                    onChange={o => field.onChange(o ? o.value : null)}
							                />
							            );
							        }}
							    />

							    {errors.financial_year && (
							        <span className="red">{errors.financial_year.message}</span>
							    )}
							</div>
							
							<div className="form-field">
							    <label>Sanctioned Amount <span className="red">*</span></label>

							    <input
							        type="number"
							        placeholder="Enter Value"
							        {...register("sanctioned_amount", {
							            required: "Sanctioned Amount is required"
							        })}
							    />

							    {errors.sanctioned_amount && (
							        <span className="red">{errors.sanctioned_amount.message}</span>
							    )}
							</div>
							
							<div className="form-field">
							    <label>Sanctioned Commissioning Date <span className="red">*</span></label>

							    <input
							        type="date"
							        {...register("sanctioned_commissioning_date", {
							            required: "Commissioning Date is required"
							        })}
							    />

							    {errors.sanctioned_commissioning_date && (
							        <span className="red">
							            {errors.sanctioned_commissioning_date.message}
							        </span>
							    )}
							</div>
							

							<div className="form-field">
							    <label>Division <span className="red">*</span></label>

							    <Controller
							        name="division_id"
							        control={control}
							        rules={{ required: "Division is required" }}
							        render={({ field }) => (
							            <Select
										{...field}
										inputRef={field.ref}
							                classNamePrefix="react-select"
							                placeholder="Select Division"
							                options={divisions.map(div => ({
							                    value: div.division_id,
							                    label: div.division_name
							                }))}
							                isSearchable
							                value={
							                    divisions
							                        .map(div => ({
							                            value: div.division_id,
							                            label: div.division_name
							                        }))
							                        .find(o => o.value === field.value) || null
							                }
							                onChange={option => field.onChange(option?.value)}
							            />
							        )}
							    />

							    {errors.division_id && (
							        <span className="red">{errors.division_id.message}</span>
							    )}
							</div>
							
							<div className="form-field">
							    <label>Section <span className="red">*</span></label>

							    <Controller
							        name="section_id"
							        control={control}
							        rules={{ required: "Section is required" }}
							        render={({ field }) => (
							            <Select
										{...field}
										inputRef={field.ref}
							                classNamePrefix="react-select"
							                placeholder="Select Section"
							                options={sections.map(sec => ({
							                    value: sec.section_id,
							                    label: sec.section_name
							                }))}
							                isSearchable
							                value={
							                    sections
							                        .map(sec => ({
							                            value: sec.section_id,
							                            label: sec.section_name
							                        }))
							                        .find(o => o.value === field.value) || null
							                }
							                onChange={option => field.onChange(option?.value)}
							            />
							        )}
							    />

							    {errors.section_id && (
							        <span className="red">{errors.section_id.message}</span>
							    )}
							</div>

							{/* PB Item No */}
							<div className="form-field">
								<label>PB Item No </label>
								<input {...register("pb_item_number")} type="text" placeholder="Enter Value" />
							</div>

							{/* Actual Completion Cost */}
							<div className="form-field">
								<label>Actual Completion Cost </label>
								<input {...register("actual_completion_cost")} type="text" placeholder="Enter Value" />
							</div>


							{/* Actual Completion Date */}
							<div className="form-field">
								<label>Actual Completion Date</label>
								<input
									{...register("actual_completion_date")}
									type="date"
									placeholder="Select Date"
								/>
							</div>

							{/* Proposed Length */}
							<div className="form-field">
								<label>Proposed Length (km)</label>
								<input
									{...register("proposed_length")}
									type="text"
									placeholder="Enter Proposed Length"
								/>
							</div>


						</div>

						<div>

							{/* Benefits */}
							<div className="form-field">
								<label>Benefits</label>
								<textarea
									{...register("benefits")}
									maxLength={1000}
									placeholder="Enter Benefits"
								/>
								<div className="char-counter"> {benefitsValue.length}/1000</div>
							</div>

							{/* Remarks */}
							<div className="form-field">
								<label>Remarks</label>
								<textarea
									{...register("remarks")}
									maxLength={1000}
									placeholder="Enter Remarks"
								/>
								<div className="char-counter"> {remarksValue.length}/1000</div>
							</div>
						</div>

						{/* Commissioned Length */}

						<div className="row mt-1 mb-2">


							<div className={`dataTable ${styles.tableWrapper}`}>
								<h3 className="mb-1 d-flex align-center justify-content-center">Structure Details</h3>

								<table className="table user-table">
									<thead>
										<tr>
										    <th>Structure Details</th>
											<th>From Chainage (m)</th>
											<th>To Chainage (m)</th>
											{/*<th>Action</th>*/}
										</tr>
									</thead>
									<tbody>
										<td>
											<input
												type="text"
												{...register("structure_details")}
												placeholder="Enter Structure Details"
											/>
										</td>
										<td>
											<input
												type="number"
												step="0.01"
												{...register("from_chainage")}
												placeholder="From"
												className="form-control"
											/>
										</td>

										<td>
											<input
												type="number"
												step="0.01"
												{...register("to_chainage")}
												placeholder="To"
											/>
										</td>
									</tbody>
								</table>
							</div>

							{/*<div className="d-flex align-center justify-content-center mt-1">
								<button type="button" className="btn-2 btn-green" onClick={() => appendCommissionedLength({ fromChainage: "", toChainage: "", commissionedLength: "" })}>
									<BiListPlus size="24" />
								</button>
							</div>*/}

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
						
						{/*isEdit && (
						    <div className="row mt-2">

						        <h3 className="d-flex justify-content-center">Pink Book Details</h3>

						        <div className={`dataTable ${styles.tableWrapper}`}>
						            <table className="table user-table">
						                <thead>
						                    <tr>
						                        <th>Financial Year</th>
						                        <th>Railway</th>
						                        <th>PB Item No</th>
						                        <th>Action</th>
						                    </tr>
						                </thead>

						                <tbody>

						                    {pinkBookFields.length > 0 ? (
						                        pinkBookFields.map((row, index) => (
						                            <tr key={row.id}>

						                                { Financial Year }
						                                <td>
						                                    <Controller
						                                        name={`pinkBooks.${index}.financial_year`}
						                                        control={control}
						                                        render={({ field }) => (
						                                            <Select
						                                                classNamePrefix="react-select"
						                                                options={yearList.map(y => ({
						                                                    value: y.financial_year,
						                                                    label: y.financial_year
						                                                }))}
						                                                placeholder="Select"
						                                                value={
						                                                    yearList
						                                                        .map(y => ({
						                                                            value: y.financial_year,
						                                                            label: y.financial_year
						                                                        }))
						                                                        .find(o => o.value === field.value) || null
						                                                }
						                                                onChange={o => field.onChange(o?.value)}
						                                                isSearchable
						                                            />
						                                        )}
						                                    />
						                                </td>

						                                { Railway }
						                                <td>
						                                    <select
						                                        {...register(`pinkBooks.${index}.railway`)}
						                                        className="form-control"
						                                    >
						                                        <option value="">Select</option>
						                                        <option value="WR">WR</option>
						                                        <option value="CR">CR</option>
						                                    </select>
						                                </td>

						                                { PB Item }
						                                <td>
						                                    <input
						                                        {...register(`pinkBooks.${index}.pb_item_no`)}
						                                        type="text"
						                                        placeholder="PB Item No"
						                                    />
						                                </td>

						                                { Remove }
						                                <td>
						                                    <button
						                                        type="button"
						                                        className="btn btn-outline-danger"
						                                        onClick={() => removePinkBook(index)}
						                                    >
						                                        Remove
						                                    </button>
						                                </td>

						                            </tr>
						                        ))
						                    ) : (
						                        <tr>
						                            <td colSpan="4" className="text-center text-muted">
						                                No Pink Book rows added.
						                            </td>
						                        </tr>
						                    )}

						                </tbody>
						            </table>
						        </div>

						        { Add Row Button }
						        <div className="d-flex justify-content-center mt-1">
						            <button
						                type="button"
						                className="btn-2 btn-green"
						                onClick={() =>
						                    appendPinkBook({
						                        financial_year: "",
						                        railway: "",
						                        pb_item_no: ""
						                    })
						                }
						            >
						                Add Row
						            </button>
						        </div>

						    </div>
						)*/}
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
