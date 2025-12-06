import React, { useEffect, useState } from "react";
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
    const { state } = useLocation(); 
	
	const userName = localStorage.getItem("userName")?.toLowerCase();



	
	const issueId = state?.issue_id;
    const isEdit = Boolean(issueId);


	const [projectOptions, setProjectOptions] = useState([]);
	const [contractOptions, setContractOptions] = useState([]);
	const [structureOptions, setStructureOptions] = useState([]);
	const [componentOptions, setComponentOptions] = useState([]);
	const [issueCategoryOptions, setIssueCategoryOptions] = useState([]);
	const [shortDescriptionOptions, setShortDescriptionOptions] = useState([]);
	const [issuePriorityOptions, setIssuePriorityOptions] = useState([]);
	const [responsibleOrganisationOptions, setResponsibleOrganisationOptions] = useState([]);
	const [issueFileTypesOptions, setIssueFileTypesOptions] = useState([]);
	const [issueStatusOptions, setIssueStatusOptions] = useState([]);
	const [issueResponsiblePersonOptions, setIssueResponsiblePersonOptions] = useState([]);


	const {
		register,
		control,
		handleSubmit,
		setValue,
		watch,
		reset,
		replace,
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
			modified_by: "",
			modified_date: "",
			location: "",
			other_organization: "",
			reported_by: "",
			status_fk: "",
			other_org_resposible_person_name: "",
			other_org_resposible_person_designation: "",

			documentsTable: [
				{ issue_file_types: "", issueFiles: null, fileName: "" }
			],
			modificationTable: [],

		}
	});


	const {
	  fields: documentsTableFields,
	  append: appendDocumentsTable,
	  remove: removeDocumentsTable,
	  replace: replaceDocumentsTable
	} = useFieldArray({ control, name: "documentsTable" });

	const {
	  fields: modificationTableFields,
	  replace: replaceModificationTable
	} = useFieldArray({ control, name: "modificationTable" });



  useEffect(() => {
    fetch(`${API_BASE_URL}/issue/ajax/form/add-issue-form`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
	  body: JSON.stringify({ issue_id: issueId }),
	  credentials: "include"  
    })
      .then(res => res.json())
      .then(data => {
        // 1. Projects Dropdown
        setProjectOptions(
          data.projectsList?.map(p => ({
            value: p.project_id_fk,
            label: `${p.project_name}`
          })) || []
        );

        // 2. Contracts Dropdown
        setContractOptions(
          data.contractsList?.map(c => ({
            value: c.contract_id_fk,
            label: `${c.contract_short_name} `
          })) || []
        );

		
		setStructureOptions(
		      data.structures?.map(s => ({
		        value: s.structure,
		        label: s.structure
		      })) || []
		    );

        // 4. Issue Priority List
        setIssuePriorityOptions(
          data.issuesPriorityList?.map(i => ({
            value: i.priority,
            label: i.priority
          })) || []
        );

        // 5. Issue Category List
        setIssueCategoryOptions(
          data.issuesCategoryList?.map(i => ({
            value: i.category,
            label: i.category
          })) || []
        );

        // 6. Titles List (short descriptions)
        setShortDescriptionOptions(
          data.issueTitlesList?.map(i => ({
            value: i.short_description,
            label: i.short_description
          })) || []
        );



        // 12. Other Organizations
        setResponsibleOrganisationOptions(
          data.otherOrganizations?.map(o => ({
            value: o.other_organization,
            label: o.other_organization
          })) || []
        );

        // 13. File Types
        setIssueFileTypesOptions(
          data.issueFileTypes?.map(t => ({
            value: t.issue_file_type,
            label: t.issue_file_type
          })) || []
        );
		
		setIssueStatusOptions(
		  data.issuesStatusList?.map(t => ({
		    value: t.status,
		    label: t.status
		  })) || []
		);

        // 14. ResponsiblePersonList
		
		setIssueResponsiblePersonOptions(
		  data.responsiblePersonList?.map(t => ({
		    value: t.responsible_person_user_id,
		    label: t.responsible_person+ "-" + t.responsible_person_designation 
		  }))
		);



        // 15. Components
        setComponentOptions(
          data.components?.map(c => ({
            value: c.component,
            label: c.component
          })) || []
        );
		
		  if (Array.isArray(data.actionTakens) && data.actionTakens.length > 0) {
			  const cleaned = data.actionTakens.map((f) => ({
				  modified_by: f.user_name || "",
				  modified_date: f.created_date || "",
				  comment: f.comment || "",
			  }));
			  replaceModificationTable(cleaned);
		  }

      })
      .catch(err => console.error("Failed to load Issue form data:", err));
  }, []);

  
  useEffect(() => {
    if (userName) {
      setValue("reported_by", userName);
    }
  }, [userName, setValue]);
  
  
  
  const [issueDetails, setissueDetails] = useState(null);

  
  useEffect(() => {
  	if (!isEdit || !issueId) return;

  	fetch(`${API_BASE_URL}/issue/ajax/form/get-issue/issue`, {
  		method: "POST",
  		headers: { "Content-Type": "application/json" },
  		body: JSON.stringify({ issue_id: issueId }),
		credentials:"include",
  	})
  		.then((res) => res.json())
  		.then((data) => {
  			console.log("Full Design Details:", data);
  			setissueDetails(data);
  		});
  }, [issueId,setValue, isEdit]);
  
  
  useEffect(() => {
    if (!isEdit || !issueDetails) return;

    const optionsReady =
      projectOptions.length &&
      contractOptions.length &&
      structureOptions.length &&
      componentOptions.length &&
      issueCategoryOptions.length &&
      issuePriorityOptions.length &&
      responsibleOrganisationOptions.length &&
      issueFileTypesOptions.length;

    if (!optionsReady) return;

    const d = issueDetails;

    // ---------- Helper: match select option ----------
    const findOption = (options, value) => {
      if (!value) return null;
      const v = String(value).trim().toUpperCase();
      return (
        options.find(
          (o) => String(o.value).trim().toUpperCase() === v
        ) || { value, label: value }
      );
    };

    // ---------- Normalize date ----------
	const normalizeDate = (raw) => {
	  if (!raw) return "";

	  // if format is DD-MM-YYYY
	  if (/^\d{2}-\d{2}-\d{4}$/.test(raw)) {
	    const [dd, mm, yyyy] = raw.split("-");
	    return `${yyyy}-${mm}-${dd}`;
	  }

	  // if format is full timestamp (2025-12-03T00:00:00)
	  if (/^\d{4}-\d{2}-\d{2}/.test(raw)) {
	    return raw.substring(0, 10);
	  }

	  return "";
	};


    // ---------- Prepare values ----------
    const values = {
      issue_id: d.issue_id || "",
      title: d.title || "",
      description: d.description || "",
      corrective_measure: d.corrective_measure || "",
      date: normalizeDate(d.date),
	  modified_by:d.modified_by || "",
	  modified_date: normalizeDate(d.modified_date),
      location: d.location || "",
      reported_by: d.reported_by || "",
      other_organization: findOption(responsibleOrganisationOptions, d.other_organization),

      // Select dropdown fields
      project_id_fk: findOption(projectOptions, d.project_id_fk),
      contract_id_fk: findOption(contractOptions, d.contract_id_fk),
      structure: findOption(structureOptions, d.structure),
      component: findOption(componentOptions, d.component),
      category_fk: findOption(issueCategoryOptions, d.category_fk),
      priority_fk: findOption(issuePriorityOptions, d.priority_fk),
	  status_fk: findOption(issueStatusOptions, d.status_fk),
	  other_org_resposible_person_name: d.other_org_resposible_person_name || "",
	  other_org_resposible_person_designation: d.other_org_resposible_person_designation || "",
	  assigned_date: normalizeDate(d.assigned_date) || "",
	  resolved_date: normalizeDate(d.resolved_date) || "",
	  responsible_person:d.responsible_person || "",
	  
	  
	  
	  
    };

    // Reset entire form with normalized values
    reset(values);

    // ================
    // FILE TABLE PREFILL
    // ================
	if (Array.isArray(d.issueFilesList) && d.issueFilesList.length > 0) {
	  const cleaned = d.issueFilesList.map((f) => ({
	    issue_file_types: {
	      value: f.issue_file_type_fk,
	      label: f.issue_file_type_fk,
	    },
	    issueFiles: null,                    
	    fileName: f.file_name || "",
	    issue_file_id: f.issue_file_id || ""
	  }));

	  replaceDocumentsTable(cleaned);
	}



  }, [
    isEdit,
    issueDetails,
    projectOptions,
    contractOptions,
    structureOptions,
    componentOptions,
    issueCategoryOptions,
    issuePriorityOptions,
    responsibleOrganisationOptions,
    issueFileTypesOptions,
    reset,
    replaceDocumentsTable,
	replaceModificationTable 
  ]);





  const onSubmit = async (data) => {
    try {
      const formData = new FormData();

      // ===== MUST send issue_id for UPDATE =====
      if (isEdit) {
        formData.append("issue_id", issueId);
		formData.append("status_fk", data.status_fk?.value || data.status_fk);
      }

      // ==== Normal fields ====
      formData.append("contract_id_fk", data.contract_id_fk?.value || data.contract_id_fk);
      formData.append("project_id_fk", data.project_id_fk?.value || data.project_id_fk);
      formData.append("structure", data.structure?.value || data.structure);
      formData.append("component", data.component?.value || data.component);
      formData.append("category_fk", data.category_fk?.value || data.category_fk);
      formData.append("title", data.title?.value || data.title);
      formData.append("priority_fk", data.priority_fk?.value || data.priority_fk);
      formData.append("description", data.description || "");
      formData.append("corrective_measure", data.corrective_measure || "");
      formData.append("date", data.date || "");
      formData.append("location", data.location || "");
	  formData.append(
	    "other_organization",
	    data.other_organization?.value ?? ""
	  );
	  
	  formData.append("other_org_resposible_person_name", data.other_org_resposible_person_name || "");
	  
	  formData.append("other_org_resposible_person_designation", data.other_org_resposible_person_designation || "");

      formData.append("reported_by", data.reported_by || "");
	  
	  formData.append("assigned_date", data.assigned_date || "");
	  formData.append("resolved_date", data.resolved_date || "");
	  formData.append("responsible_person", data.responsible_person || "");
	  
	  
	  

	  data.documentsTable.forEach((row) => {
	    const file = row.issueFiles?.[0] ?? null;

	    // safe type extract
	    const type =
	      row.issue_file_types?.value ??
	      row.issue_file_types ??
	      "";

	    // safe file id extract
	    const fileId =
	      row.issue_file_id?.value ??
	      row.issue_file_id ??
	      "";
		  
		  const fileName =
		    row.fileName?.value ??
		    row.fileName ??
		    "";


	    if (file) {
	      // new file
		  formData.append("issue_file_types", type);
	      formData.append("issueFiles", file);
	      formData.append("issueFileNames", file.name);
	      formData.append("issue_file_ids", fileId);
	    } else {
	      // no new file (keep old id if exists)
	      const empty = new Blob([], { type: "application/octet-stream" });
		  formData.append("issue_file_types", type);
	      formData.append("issueFiles", empty);
	      formData.append("issueFileNames", fileName);
	      formData.append("issue_file_ids", fileId); 
	    }
	  });



	  const url = isEdit
	        ? `${API_BASE_URL}/issue/update-issue`
	        : `${API_BASE_URL}/issue/add-issue`;

	      const response = await fetch(url, {
	        method: "POST",
	        body: formData,     
	        credentials: "include"
	      });
		  
		  const  msg = await response.text();

	      if (!response.ok) {
	        alert("Failed to Save Issue!");
	        return;
	      }

	      alert(isEdit ? msg : "Issue Added Successfully!");
	      navigate("/updateforms/issues");

    } catch (err) {
      console.error("SAVE ERROR:", err);
      alert("Error saving issue");
    }
  };

  const otherOrg = watch("other_organization");

  const showResponsibleDetails =
    otherOrg && (otherOrg.value || otherOrg) !== "";


  return (
      <div className={`${styles.container} container-padding`}>
        <div className="card">
          <div className="formHeading">
            <h2 className="center-align ps-relative">
              {isEdit ? "Edit Issue" : "Add Issue"}
            </h2>
          </div>

          <div className="innerPage">
            <form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">

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
						  options={projectOptions}
                          classNamePrefix="react-select"
                          placeholder="Select value"
                          isSearchable
                          isClearable
                          onChange={(selected) => field.onChange(selected)}
                        />
                      )}
                    />
                </div>
				
				<div>
				  <input type="hidden" {...register("department_id_fk")} />
				  <input type="hidden" {...register("hod")} />
				  <input type="hidden" {...register("dy_hod")} />
				  <input type="hidden" {...register("contract_type")} />
				</div>
	

				<div className="form-field">
				  <label>Contract <span className="red">*</span></label>

				  <Controller
				    name="contract_id_fk"
				    control={control}
				    rules={{ required: "Contract is required" }}
				    render={({ field }) => (
				      <Select
				        {...field}
				        options={contractOptions}
				        classNamePrefix="react-select"
				        placeholder="Select Contract"
				        isSearchable
				        isClearable

				        /** Ensure correct display */
				        value={contractOptions.find(
				          opt => opt.value === field.value?.value || opt.value === field.value
				        ) || null}

				        /** Update hidden fields automatically */
				        onChange={(selected) => {
				          field.onChange(selected);  // store selected full object

				          setValue("department_id_fk", selected?.department_fk || "");
				          setValue("hod", selected?.hod || "");
				          setValue("dy_hod", selected?.dyhod || "");
				          setValue("contract_type", selected?.contract_type || "");
				        }}
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
						  options={structureOptions}
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
						  options={componentOptions}
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
                      rules={{ required: "Category is required."}}
                      render={({ field }) => (
                        <Select
                          {...field}
						  options={issueCategoryOptions}
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
				    rules={{ required: "Short description Required." }}
				    render={({ field }) => {
				      const selectedOption =
				        shortDescriptionOptions.find(opt => opt.value === field.value) || null;

				      return (
				        <Select
				          {...field}
				          options={shortDescriptionOptions}
				          classNamePrefix="react-select"
				          placeholder="Select value"
				          isSearchable
				          isClearable
				          value={selectedOption}            
				          onChange={(opt) => field.onChange(opt?.value || "")} 
				        />
				      );
				    }}
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
                      rules={{ required: "Issue Priority Required."}}
                      render={({ field }) => (
                        <Select
                          {...field}
						  options={issuePriorityOptions}
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
					  
					  {isEdit && modificationTableFields.length > 0 && (
					  			    <div className="table-responsive dataTable">
					  			      <table className="table table-bordered align-middle">
					  			        <thead className="table-light">
					  			          <tr>
					  			            <th style={{ width: "15%" }}>Modified By</th>
					  			            <th style={{ width: "15%" }}>Modified Date</th>
					  			            <th style={{ width: "15%" }}>Action / Comments</th>
					  			          </tr>
					  			        </thead>

					  			        <tbody>
					  			          {modificationTableFields.length > 0 ? (
					  			            modificationTableFields.map((item, index) => (
					  			              <tr key={index}>
					  			                <td>{item.modified_by}</td>
					  			                <td>{item.modified_date}</td>
					  			                <td>{item.comment}</td>
					  			              </tr>
					  			            ))
					  			          ) : (
					  			            <tr>
					  			              <td colSpan="3" className="text-center text-muted">
					  			                Not yet modified
					  			              </td>
					  			            </tr>
					  			          )}
					  			        </tbody>
					  			      </table>
					  			    </div>
					  			  )}
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
							  <input {...register("date")} type="date" placeholder="Select Date" />
						  </div>

						  <div className="form-field">
							  <label>Location/Station/KM </label>
							  <input {...register("location")} type="text" />
						  </div>

						  <div className="form-field">
							  <label>
								  Responsible Organization (Pending with) <span className="red">*</span>
							  </label>

							  <Controller
								  name="other_organization"
								  control={control}
								  rules={{ required: "Responsible Organization is required" }}
								  render={({ field }) => (
									  <Select
										  {...field}
										  options={responsibleOrganisationOptions}
										  classNamePrefix="react-select"
										  placeholder="Select value"
										  isSearchable
										  isClearable
										  value={responsibleOrganisationOptions.find(
											  (opt) =>
												  opt.value === field.value?.value ||
												  opt.value === field.value
										  )}
										  onChange={(selected) => field.onChange(selected)}
									  />
								  )}
							  />

							  {errors.other_organization && (
								  <span className="red">{errors.other_organization.message}</span>
							  )}
						  </div>

					  </div>


					  {watch("other_organization") && (
						  <div className="form-row">
							  <div className="form-field">
								  <label>Responsible Person Name: </label>
								  <input
									  {...register("other_org_resposible_person_name")}
									  type="text"
								  />
							  </div>

							  <div className="form-field">
								  <label>Responsible Person Designation: </label>
								  <input
									  {...register("other_org_resposible_person_designation")}
									  type="text"
								  />
							  </div>
						  </div>
					  )}

					  <div className="form-row">

						  <div className="form-field">
							  <label>Reported by <span className="red">*</span> </label>

							  <input
								  type="text"
								  rules={{ required: "Reported by is required" }}
								  {...register("reported_by")}
								  defaultValue={userName}
								  className="form-control"
							  />

							  {errors.reported_by && (
								  <p className="error-text">{errors.reported_by.message}</p>
							  )}
						  </div>

						  {isEdit && (
							  <div className="form-field">
								  <label>Issue Status: </label>
								  <Controller
									  name="status_fk"
									  control={control}
									  render={({ field }) => (
										  <Select
											  {...field}
											  options={issueStatusOptions}
											  classNamePrefix="react-select"
											  placeholder="Select value"
											  isSearchable
											  isClearable
											  onChange={(selected) => field.onChange(selected)}
										  />
									  )}
								  />
							  </div>
						  )}
					  </div>
					  
					  {isEdit && ((watch("status_fk")?.value === "Assigned") || (watch("status_fk")?.value === "Closed")) && (
					    <div className="form-row">

					      <div className="form-field">
					        <label>Assigned Date: </label>
					        <input {...register("assigned_date")} type="date" placeholder="Select Date" />
					      </div>

						  <div className="form-field">
						    <label>Responsible Person from WCR: </label>
						    <Controller
						      name="responsible_person"
						      control={control}
						      render={({ field }) => (
						        <Select
						          options={issueResponsiblePersonOptions}
						          classNamePrefix="react-select"
						          placeholder="Select value"
						          isSearchable
						          isClearable

						          // ⬇️ Display selected option correctly
						          value={issueResponsiblePersonOptions.find(
						            (option) => option.value === field.value
						          )}

						          // ⬇️ Store only primitive value
						          onChange={(selected) => field.onChange(selected?.value || "")}
						        />
						      )}
						    />
						  </div>


							  {watch("status_fk")?.value === "Closed" && (
								  <div className="form-field">
									  <label>Resolved Date: </label>
					          <input {...register("resolved_date")} type="date" placeholder="Select Date" />
					        </div>
					      )}
					      
					    </div>
					  )}




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
													  name={`documentsTable.${index}.issue_file_types`}
													  control={control}
													  render={({ field }) => (
														  <Select
															  {...field}
															  options={issueFileTypesOptions}
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

											  {/* FILE CELL */}
											  <td>
												  <div className={styles["file-upload-wrapper"]}>

													  {/* ======= UPLOAD BUTTON ======= */}
													  <label
														  htmlFor={`file-${index}`}
														  className={styles["file-upload-label-icon"]}
													  >
														  <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
														  Upload File
													  </label>

													  <input
														  id={`file-${index}`}
														  type="file"
														  {...register(`documentsTable.${index}.issueFiles`)}
														  className={styles["file-upload-input"]}
													  />

													  {/* ======= NEW FILE SELECTED ======= */}
													  {watch(`documentsTable.${index}.issueFiles`)?.[0]?.name && (
														  <p className={styles["file-name"]}>
															  Selected: {watch(`documentsTable.${index}.issueFiles`)[0].name}
														  </p>
													  )}

													  {/* ======= EXISTING FILE NAME (PREFILL MODE) ======= */}
													  {!watch(`documentsTable.${index}.issueFiles`)?.[0]?.name &&
														  item.fileName && (
															  <p className={styles["file-name-existing"]}>
																  {item.fileName}
															  </p>
														  )}

												  </div>
											  </td>

											  {/* ACTION BUTTON */}
											  <td className="text-center d-flex align-center justify-content-center">
												  <button
													  type="button"
													  className="btn btn-outline-danger"
													  onClick={() => removeDocumentsTable(index)}
												  >
													  <MdOutlineDeleteSweep size={26} />
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