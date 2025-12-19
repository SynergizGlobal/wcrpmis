import { useRef, useState, useEffect } from "react";
import { Outlet } from 'react-router-dom';
import styles from './ContractForm.module.css';
import { NavLink } from "react-router-dom";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
import { RiAttachment2 } from 'react-icons/ri';

export default function ContractForm() {

  const [activeTab, setActiveTab] = useState("managers");

  const navigate = useNavigate();
      const { state } = useLocation(); // passed when editing
      const row = state?.data || null;
      const isEdit = !!row;

      const [projectOptions, setProjectOptions] = useState([]);
      const [loading, setLoading] = useState(false);
      const [savingForEdit, setSavingForEdit] = useState(false);

  const {
          register,
          control,
          handleSubmit,
          setValue,
          getValues,
          watch,
          formState: { errors },
        } = useForm({
          defaultValues: {
            project_id_fk: "",
            contract_status: "",
            hod_user_id_fk: "",
            dy_hod_user_id_fk: "",
            contract_department: "",
            contract_short_name: "",
            bank_funded: "",
            bank_name: "",
            type_of_review: "",
            contract_name: "",
            contract_type_fk: "",
            contractor_id_fk: "",
            bg_required: "",
            insurance_required: "",
            milestone_requried: "",
            revision_requried: "",
            contractors_key_requried: "",

            executives: [{ department_fks: "", responsible_people_id_fks: "" }],
            tenderBidRevisions: [{ revisionno: "", revision_estimated_cost: "", revision_planned_date_of_award: "", revision_planned_date_of_completion: "", notice_inviting_tender: "", tender_bid_opening_date: "", technical_eval_approval: "", financial_eval_approval: "", tender_bid_remarks: "" }],
            bgDetailsList : [{ bg_type_fks: "", issuing_banks: "", bg_numbers: "", bg_values: "", bg_dates: "", bg_valid_uptos: "", release_dates: ""  }],
            insuranceRequired: [{ insurance_type_fks: "", issuing_agencys: "", agency_addresss: "", insurance_numbers: "", insurance_values: "", insurence_valid_uptos: "", insuranceStatus: "" }],
            milestoneRequired: [{ milestone_ids: "", milestone_names: "", milestone_dates: "", actual_dates: "", revisions: "", mile_remarks: "" }],
            revisionRequired: [{ revision_numbers: "", revised_amounts: "", revision_amounts_statuss: "", revised_docs: "", revision_statuss: "", approvalbybankstatus: "" }],
            contractorsKeyRequried: [{ contractKeyPersonnelNames: "", contractKeyPersonnelDesignations: "", contractKeyPersonnelMobileNos: "", contractKeyPersonnelEmailIds: "" }],
            documentsTable: [{ contract_file_types: "", contractDocumentNames: "", contractDocumentFiles: "" }],

          }
        });


        const bankFunded = watch("bank_funded");
        const contractStatus = watch("contract_status");
        const bgDetails = watch("bg_required");
        const insuranceRequiredbutton = watch("insurance_required");
        const milestoneRequiredButton= watch("milestone_requried");
        const revisionRequiredButton = watch("revision_requried");
        const contractorsKeyRequriedButton = watch("contractors_key_requried");

          const { fields: executiveFields, append: appendExecutive, remove: removeExecutive } = useFieldArray({
            control,
            name: "executives",
          });

          const { fields: tenderBidRevisionsFields, append: appendTenderBidRevisions, remove: removeTenderBidRevisions } = useFieldArray({
            control,
            name: "tenderBidRevisions",
          });

          const { fields: bgDetailsListFields, append: appendBgDetailsList, remove: removeBgDetailsList } = useFieldArray({
            control,
            name: "bgDetailsList",
          });
          
          const { fields: insuranceRequiredFields, append: appendInsuranceRequired, remove: removeInsuranceRequired } = useFieldArray({
            control,
            name: "insuranceRequired",
          });

          const { fields: milestoneRequiredFields, append: appendMilestoneRequired, remove: removeMilestoneRequired } = useFieldArray({
            control,
            name: "milestoneRequired",
          });

          const { fields: revisionRequiredFields, append: appendRevisionRequired, remove: removeRevisionRequired } = useFieldArray({
            control,
            name: "revisionRequired",
          });

          const { fields: contractorsKeyRequriedFields, append: appendContractorsKeyRequried, remove: removeContractorsKeyRequried } = useFieldArray({
            control,
            name: "contractorsKeyRequried",
          });

          const { fields: documentsTableFields, append: appendDocumentsTable, remove: removeDocumentsTable } = useFieldArray({
            control,
            name: "documentsTable",
          });
        
    const onSubmit = async (data, saveForEdit = false) => {
      if (saveForEdit) {
        setSavingForEdit(true);
      } else {
        setLoading(true);
      }
      
      try {
        const formData = new FormData();
        
        // Append all form fields to FormData
        Object.keys(data).forEach(key => {
          if (key !== 'documentsTable') {
            if (typeof data[key] === 'object' && data[key] !== null) {
              formData.append(key, JSON.stringify(data[key]));
            } else {
              formData.append(key, data[key] || '');
            }
          }
        });

        // Handle file uploads separately
        data.documentsTable?.forEach((doc, index) => {
          if (doc.contractDocumentFiles && doc.contractDocumentFiles[0]) {
            formData.append(`document_${index}`, doc.contractDocumentFiles[0]);
            formData.append(`document_name_${index}`, doc.contractDocumentNames || '');
            formData.append(`document_type_${index}`, doc.contract_file_types || '');
          }
        });

        let response;
        if (isEdit) {
          // Update existing contract
          response = await api.put(`${API_BASE_URL}/contracts/${row.id}`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          });
        } else {
          // Create new contract
          response = await api.post(`${API_BASE_URL}/contracts`, formData, {
            headers: { 'Content-Type': 'multipart/form-data' }
          });
        }
        
        if (saveForEdit) {
          // If saving for edit, stay on the page and show success message
          alert("Contract saved successfully! You can continue editing.");
          // If this was a new contract, we might want to update the URL or form mode
          if (!isEdit && response.data?.id) {
            // Optionally update to edit mode with the new ID
            console.log("New contract created with ID:", response.data.id);
            // You could navigate to edit mode or just refresh the form
          }
        } else {
          // Normal save, go back to previous page
          alert(isEdit ? "Contract updated successfully!" : "Contract added successfully!");
          navigate(-1); // Go back to previous page
        }
      } catch (error) {
        console.error("Error submitting form:", error);
        alert(`Error: ${error.response?.data?.message || error.message}`);
      } finally {
        if (saveForEdit) {
          setSavingForEdit(false);
        } else {
          setLoading(false);
        }
      }
    };

    const handleCancel = () => {
      if (window.confirm("Are you sure you want to cancel? Any unsaved changes will be lost.")) {
        navigate(-1); // Go back to previous page
      }
    };

    const executiveOptions = [
      { value: 1, label: "Executive 1" },
      { value: 2, label: "Executive 2" },
      { value: 3, label: "Executive 3" }
    ];

    const sectionRefs = {
      managers: useRef(null),
      executives: useRef(null),
      details: useRef(null),
      closure: useRef(null),
      bank: useRef(null),
      insurance: useRef(null),
      milestone: useRef(null),
      personnel: useRef(null),
      documents: useRef(null)
    };

      const tabs = [
        { id: "managers", label: "Contract Managers" },
        { id: "executives", label: "Executives" },
        { id: "details", label: "Contract Details" },
        { id: "closure", label: "Contract Closure" },
        { id: "bank", label: "Bank Guarantee" },
        { id: "insurance", label: "Insurance" },
        { id: "milestone", label: "Milestone" },
        { id: "personnel", label: "Contractor's Key Personnel" },
        { id: "documents", label: "Documents" },
      ];

      const scrollToSection = (id) => {
        setActiveTab(id);
        sectionRefs[id].current.scrollIntoView({ behavior: "smooth", block: "start" });
      };

      useEffect(() => {
        const handleScroll = () => {
          let current = "";

          Object.keys(sectionRefs).forEach((key) => {
            const section = sectionRefs[key].current;
            const top = section.getBoundingClientRect().top;

            if (top <= 150 && top >= -200) {
              current = key;
            }
          });

          if (current) setActiveTab(current);
        };

        window.addEventListener("scroll", handleScroll);
        return () => window.removeEventListener("scroll", handleScroll);
      }, []);

        useEffect(() => {
          if (isEdit && state?.project) {
            Object.entries(state.project).forEach(([key, value]) => setValue(key, value));
          }
        }, [state, setValue, isEdit]);

      useEffect(() => {
        console.log("📋 Current Form Mode:", isEdit ? "EDIT" : "ADD");
        if (isEdit) console.log("🗂️ Editing Existing Record:", row);
      }, [isEdit, row]);

  return (
    <div className={`contractForm ${styles.container}`}>
      <form onSubmit={handleSubmit((data) => onSubmit(data, false))}>
        <div className="card">
              <div className="formHeading">
                <h2 className="center-align ps-relative">
                  {isEdit ? "Update Contract" : "Add Contract"}
                </h2>
              </div>
              <div className="innerPage">
                <div className={styles.stickyNav}>
                  {tabs.map(t => (
                    <button
                      key={t.id}
                      type="button"
                      onClick={() => scrollToSection(t.id)}
                      className={`${styles.navBtn} ${activeTab === t.id ? styles.activeTab : ""}`}
                    >
                      {t.label}
                    </button>
                  ))}
                </div>

                <div ref={sectionRefs.managers} className={styles.formSection}> 
                <h6 className="d-flex justify-content-center mt-1 mb-2">Contract Managers</h6>
                
                <div className="form-row">
                    <div className="form-field">
                      <label>Project <span className="red">*</span></label>
                      <Controller
                        name="project_id_fk"
                        control={control}
                        rules={{ required: true }}
                        render={({ field }) => (
                          <Select
                            {...field}
                            classNamePrefix="react-select"
                            options={projectOptions}
                            placeholder="Select Project"
                            isSearchable
                            isClearable
                            value={projectOptions.find(opt => opt.value === field.value?.value) || null}
                            onChange={(opt) => field.onChange(opt)}
                          />
                        )}
                      />

                      {errors.project_id_fk && (
                        <p className="red">{errors.project_id_fk.message}</p>
                      )}
                    </div>
                    <div className="form-field">
                      <label>Contract Awarded</label>
                        <div className="d-flex gap-20" style={{padding: "10px"}}>
                          <label>
                            <input
                              type="radio"
                              value="yes"
                              {...register("contract_status", { required: "Please select JM Approval" })}
                            />
                            Yes
                          </label>

                          <label>
                            <input
                              type="radio"
                              value="no"
                              {...register("contract_status")}
                            />
                            No
                          </label>
                        </div>
                      </div>
                      <div className="form-field">
                      <label>HOD <span className="red">*</span></label>
                      <Controller
                        name="hod_user_id_fk"
                        control={control}
                        rules={{ required: true }}
                        render={({ field }) => (
                          <Select
                            {...field}
                            classNamePrefix="react-select"
                            placeholder="Select Project"
                            isSearchable
                            isClearable
                          />
                        )}
                      />

                      {errors.hod_user_id_fk && (
                        <p className="red">{errors.hod_user_id_fk.message}</p>
                      )}
                    </div>
                    <div className="form-field">
                      <label>Dy HOD <span className="red">*</span></label>
                      <Controller
                        name="dy_hod_user_id_fk"
                        control={control}
                        rules={{ required: true }}
                        render={({ field }) => (
                          <Select
                            {...field}
                            classNamePrefix="react-select"
                            placeholder="Select Project"
                            isSearchable
                            isClearable
                          />
                        )}
                      />

                      {errors.dy_hod_user_id_fk && (
                        <p className="red">{errors.dy_hod_user_id_fk.message}</p>
                      )}
                    </div>
                    <div className="form-field">
                      <label>Contract Department <span className="red">*</span></label>
                      <Controller
                        name="contract_department"
                        control={control}
                        rules={{ required: true }}
                        render={({ field }) => (
                          <Select
                            {...field}
                            classNamePrefix="react-select"
                            placeholder="Select Project"
                            isSearchable
                            isClearable
                          />
                        )}
                      />

                      {errors.contract_department && (
                        <p className="red">{errors.contract_department.message}</p>
                      )}
                    </div>
                    <div className="form-field">
                      <label>Contract Short Name <span className="red">*</span></label>
                      <input
                        type="text"
                        maxLength={100}
                        rules={{ required: true }}
                        {...register("contract_short_name")}
                        placeholder="Enter Value"
                      />
                      <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                        {watch("contract_short_name")?.length || 0}/100
                        {errors.contract_short_name && <span className="red">Required</span>}
                      </div>
                    </div>

                  <div className="form-field">
                    <label>Bank Refunded</label>
                        <div className="d-flex gap-20" style={{padding: "10px"}}>
                          <label>
                            <input
                              type="radio"
                              value="yes"
                              {...register("bank_funded", { required: "Please select JM Approval" })}
                            />
                            Yes
                          </label>

                          <label>
                            <input
                              type="radio"
                              value="no"
                              {...register("bank_funded")}
                            />
                            No
                          </label>
                        </div>
                      </div>
                      {bankFunded === "yes" && (
                          <>
                              <div className="form-field">
                                <label>Bank Name <span className="red">*</span></label>
                                <input
                                  type="text"
                                  rules={{ required: true }}
                                  {...register("bank_name")}
                                  placeholder="Enter Value"
                                />
                                {errors.bank_name && (
                                  <p className="red">{errors.bank_name.message}</p>
                                )}
                              </div>
                              <div className="form-field">
                                <label>Type of Review <span className="red">*</span></label>
                                <input
                                  type="text"
                                  rules={{ required: true }}
                                  {...register("type_of_review")}
                                  placeholder="Enter Value"
                                />
                                {errors.type_of_review && (
                                  <p className="red">{errors.type_of_review.message}</p>
                                )}
                              </div>
                          </>
                      )}
                </div>
                  
                </div>
                <div ref={sectionRefs.executives} className={styles.formSection}> 
                  <h6 className="d-flex justify-content-center mt-1 mb-2">Executives</h6>

                  <div className="table-responsive dataTable ">
                                  <table className="table table-bordered align-middle">
                                    <thead className="table-light">
                                      <tr>
                                        <th style={{ width: "43%" }}>Department <span className="red">*</span></th>
                                        <th style={{ width: "42%" }}>Select Executives <span className="red">*</span></th>
                                        <th style={{ width: "15%" }}>Action</th>
                                      </tr>
                                    </thead>
                                    <tbody>
                                      {executiveFields.length > 0 ? (
                                        executiveFields.map((item, index) => (
                                          <tr key={item.id}>
                                            <td>
                                              <Controller
                                                  name={`executives.${index}.department_fks`}
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
                                                {errors.executives?.[index]?.department_fks && (
                                                <span className="red">{errors.executives[index].department_fks.message}</span>
                                              )}
                                            </td>
                                            <td>
                                              <Controller
                                                name={`executives.${index}.responsible_people_id_fks`}
                                                control={control}
                                                rules={{ required: "Please select executives" }}
                                                render={({ field }) => (
                                                  <Select
                                                    {...field}
                                                    options={executiveOptions}
                                                    isMulti
                                                    placeholder="Select Executives"
                                                    classNamePrefix="react-select"
                                                    onChange={(selected) => field.onChange(selected)}
                                                    value={field.value}
                                                  />
                                                )}
                                              />

                                              {errors.executives?.[index]?.responsible_people_id_fks && (
                                                <span className="red">{errors.executives[index].responsible_people_id_fks.message}</span>
                                              )}
                                            </td>
                                            <td className="text-center d-flex align-center justify-content-center">
                                              <button
                                                type="button"
                                                className="btn btn-outline-danger"
                                                onClick={() => removeExecutive(index)}
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
                                      appendExecutive({ department_fks: "", responsible_people_id_fks: "" })
                                    }
                                  >
                                    <BiListPlus
                                      size="24"
                                    />
                                  </button>
                                </div>

                </div>
                <div ref={sectionRefs.details} className={styles.formSection}>
                  <h6 className="d-flex justify-content-center mt-1 mb-2">Contract Details</h6>
            <div className="form-row">
                    <div className="form-field">
                      <label>Contract Short Name <span className="red">*</span> </label>
                      <input rules={{ required: "Required" }} {...register("contract_short_name")} type="text" placeholder="Enter Value" />
                      {errors.contract_short_name && (
                        <p className="error-text">{errors.contract_short_name.message}</p>
                      )}
                    </div>
                  </div>

                  <div className="form-row">
                    <div className="form-field">
                      <label>Contract Name<span className="red">*</span></label>
                      <textarea 
                      {...register("contract_name", { required: "Contract name is required" })}
                      name="contract_name"
                      maxLength={200}
                      rows="3"
                      ></textarea>
                      <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                        {watch("contract_name")?.length || 0}/200
                        {errors.contract_name && <span className="red">{errors.contract_name.message}</span>}
                      </div>
                    </div>
                  </div>

                  { contractStatus === "yes" && (
                    <>
                      <div className="form-row">
                        <div className="form-field">
                          <label>Contract Type  <span className="red">*</span></label>
                          <Controller
                            name="contract_type_fk"
                            control={control}
                            rules={{ required: true }}
                            render={({ field }) => (
                              <Select
                                {...field}
                                classNamePrefix="react-select"
                                placeholder="Select Project"
                                isSearchable
                                isClearable
                                onChange={(opt) => field.onChange(opt)}
                              />
                            )}
                          />
                          {errors.contract_type_fk && (
                            <p className="error-text">{errors.contract_type_fk.message}</p>
                          )}
                        </div>
                        <div className="form-field">
                          <label>Contractor Name  <span className="red">*</span></label>
                          <Controller
                            name="contractor_id_fk"
                            control={control}
                            rules={{ required: true }}
                            render={({ field }) => (
                              <Select
                                {...field}
                                classNamePrefix="react-select"
                                placeholder="Select Project"
                                isSearchable
                                isClearable
                                onChange={(opt) => field.onChange(opt)}
                              />
                            )}
                          />
                          {errors.contractor_id_fk && (
                            <p className="error-text">{errors.contractor_id_fk.message}</p>
                          )}
                        </div>

                      </div>

                      <div className="form-row">
                        <div className="form-field">
                          <label>Scope of Contract</label>
                          <textarea 
                          {...register("scope_of_contract")}
                          name="scope_of_contract"
                          maxLength={200}
                          rows="3"
                          ></textarea>
                          <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                            {watch("scope_of_contract")?.length || 0}/200
                          </div>
                        </div>
                      </div>

                      <div className="form-row">
                        <div className="form-field">
                          <label>LOA Letter No <span className="red">*</span> </label>
                          <input rules={{ required: "Required" }} {...register("loa_letter_number")} type="text" placeholder="Enter LOA Letter No" />
                          {errors.loa_letter_number && (
                            <p className="error-text">{errors.loa_letter_number.message}</p>
                          )}
                        </div>
                        <div className="form-field">
                          <label>LOA Date <span className="red">*</span> </label>
                          <input rules={{ required: "Required" }} {...register("loa_date")} type="date" placeholder="Select Date" />
                          {errors.loa_date && (
                            <p className="error-text">{errors.loa_date.message}</p>
                          )}
                        </div>
                      </div>
                    </>
                  )}

                    <div className="form-row">
                      <div className="form-field">
                        <label>CA No </label>
                        <input {...register("ca_no")} type="text" placeholder="Enter value" />
                      </div>
                      <div className="form-field">
                        <label>CA Date </label>
                        <input {...register("ca_date")} type="date" placeholder="Enter value" />
                      </div>
                      <div className="form-field">
                        <label>Date of Start  <span className="red">*</span> </label>
                        <input rules={{ required: "Required" }} {...register("date_of_start")} type="date" placeholder="Select Date" />
                        {errors.date_of_start && (
                          <p className="error-text">{errors.date_of_start.message}</p>
                        )}
                      </div>
                      <div className="form-field">
                        <label>Original DOC  <span className="red">*</span> </label>
                        <input rules={{ required: "Required" }} {...register("doc")} type="date" placeholder="Select Date" />
                        {errors.doc && (
                          <p className="error-text">{errors.doc.message}</p>
                        )}
                      </div>
                      <div className="form-field rupee-field">
                        <label>Awarded cost <span className="red">*</span> </label>
                        <input rules={{ required: "Required" }} {...register("awarded_cost")} type="number" placeholder="Enter Value" />
                        {errors.awarded_cost && (
                          <p className="error-text">{errors.awarded_cost.message}</p>
                        )}
                      </div>
                      <div className="form-field">
                        <label>Target DOC </label>
                        <input {...register("target_doc")} type="date" placeholder="Enter value" />
                      </div>
                      <div className="form-field">
                        <label>Actual Date of Commissioning  <span className="red">*</span> </label>
                        <input rules={{ required: "Required" }} {...register("actual_date_of_commissioning")} type="date" placeholder="Select Date" />
                        {errors.actual_date_of_commissioning && (
                          <p className="error-text">{errors.actual_date_of_commissioning.message}</p>
                        )}
                      </div>
                      <div className="form-field">
                        <label>Status of Work  <span className="red">*</span></label>
                        <Controller
                          name="contract_status_fk"
                          control={control}
                          rules={{ required: true }}
                          render={({ field }) => (
                            <Select
                              {...field}
                              classNamePrefix="react-select"
                              placeholder="Select Project"
                              isSearchable
                              isClearable
                              onChange={(opt) => field.onChange(opt)}
                            />
                          )}
                        />
                        {errors.contract_status_fk && (
                          <p className="error-text">{errors.contract_status_fk.message}</p>
                        )}
                      </div>
                      <div className="form-field rupee-field">
                        <label>Detailed Estimated cost </label>
                        <input {...register("estimated_cost")} type="number" placeholder="Enter Value" />
                      </div>
                      <div className="form-field">
                        <label>Planned date of award </label>
                        <input {...register("planned_date_of_award")} type="date" placeholder="Enter value" />
                      </div>
                      <div className="form-field">
                        <label>Planned date of completion </label>
                        <input {...register("planned_date_of_completion")} type="date" placeholder="Enter value" />
                      </div>
                      <div className="form-field">
                        <label>Notice Inviting Tender </label>
                        <input {...register("contract_notice_inviting_tender")} type="date" placeholder="Enter value" />
                      </div>
                      <div className="form-field">
                        <label>Tender Opening Date </label>
                        <input {...register("tender_opening_date")} type="date" placeholder="Enter value" />
                      </div>
                      <div className="form-field">
                        <label>Technical Eval. Submission </label>
                        <input {...register("technical_eval_submission")} type="date" placeholder="Enter value" />
                      </div>
                      <div className="form-field">
                        <label>Financial Eval. Submission </label>
                        <input {...register("financial_eval_submission")} type="date" placeholder="Enter value" />
                      </div>

                    </div>

                </div>
                <div ref={sectionRefs.closure} className={styles.formSection}>
                  <h6 className="d-flex justify-content-center mt-1 mb-2">Tender Bid Revisions</h6>
                  <div className="table-responsive dataTable ">
                    <table className="table table-bordered align-middle">
                      <thead className="table-light">
                        <tr>
                          <th style={{ width: "15%" }}>Revision No </th>
                          <th style={{ width: "15%" }}>Detailed Estimated cost</th>
                          <th style={{ width: "15%" }}>Planned date of award</th>
                          <th style={{ width: "15%" }}>Planned date of completion</th>
                          <th style={{ width: "15%" }}>Notice Inviting Tender</th>
                          <th style={{ width: "15%" }}>Tender Opening Date</th>
                          <th style={{ width: "15%" }}>Tech. Eval. Approval</th>
                          <th style={{ width: "15%" }}>Fin. Eval. Approval</th>
                          <th style={{ width: "42%" }}>Remarks</th>
                          <th style={{ width: "15%" }}>Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        {tenderBidRevisionsFields.length > 0 ? (
                          tenderBidRevisionsFields.map((item, index) => (
                            <tr key={item.id}>
                              <td>
                                  <input {...register(`tenderBidRevisions.${index}.revisionno`)} type="text" placeholder="Enter value" defaultValue={`R${index + 1}`} />
                              </td>
                              <td>
                                <input {...register(`tenderBidRevisions.${index}.revision_estimated_cost`)} type="number" placeholder="Enter value" />
                              </td>
                              <td>
                                <input {...register(`tenderBidRevisions.${index}.revision_planned_date_of_award`)} type="date" placeholder="Enter value" />
                              </td>
                              <td>
                                <input {...register(`tenderBidRevisions.${index}.revision_planned_date_of_completion`)} type="date" placeholder="Enter value" />
                              </td>
                              <td>
                                <input {...register(`tenderBidRevisions.${index}.notice_inviting_tender`)} type="text" placeholder="Enter value" />
                              </td>
                              <td>
                                <input {...register(`tenderBidRevisions.${index}.tender_bid_opening_date`)} type="date" placeholder="Enter value" />
                              </td>
                              <td>
                                <input {...register(`tenderBidRevisions.${index}.technical_eval_approval`)} type="text" placeholder="Enter value" />
                              </td>
                              <td>
                                <input {...register(`tenderBidRevisions.${index}.financial_eval_approval`)} type="text" placeholder="Enter value" />
                              </td>
                              <td>
                                <textarea 
                                    {...register(`tenderBidRevisions.${index}.tender_bid_remarks`)}
                                    name="tender_bid_remarks"
                                    rows="3"
                                    ></textarea>
                              </td>
                              <td className="text-center d-flex align-center justify-content-center">
                                <button
                                  type="button"
                                  className="btn btn-outline-danger"
                                  onClick={() => removeTenderBidRevisions(index)}
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
                        appendTenderBidRevisions({ revisionno: "", revision_estimated_cost: "", revision_planned_date_of_award: "", revision_planned_date_of_completion: "", notice_inviting_tender: "", tender_bid_opening_date: "", technical_eval_approval: "", financial_eval_approval: "", tender_bid_remarks: "" })
                      }
                    >
                      <BiListPlus
                        size="24"
                      />
                    </button>
                  </div>
                  
                  <div className="form-row">
                    <div className="form-field">
                      <label>Remarks </label>
                      <textarea 
                          {...register("remarks")}
                          name="remarks"
                          rows="3"
                          ></textarea>
                    </div>
                  </div>
                   
                </div>
                <div ref={sectionRefs.bank} className={styles.formSection}> 
                  <h6 className="d-flex justify-content-center mt-1 mb-2">Bank Guarantee Details</h6> 
                  
                  <div className="d-flex gap-30 align-center justify-content-center">
                      <label>Bank Guarantee Required ?</label>
                        <div className="d-flex gap-20" style={{padding: "10px"}}>
                          <label>
                            <input
                              type="radio"
                              value="yes"
                              {...register("bg_required")}
                            />
                            Yes
                          </label>

                          <label>
                            <input
                              type="radio"
                              value="no"
                              {...register("bg_required")}
                            />
                            No
                          </label>
                        </div>
                      </div>

                      <br />
                      {bgDetails === "yes" && (
                        <>
                          <div className="table-responsive dataTable ">
                            <table className="table table-bordered align-middle">
                              <thead className="table-light">
                                <tr>
                                  <th style={{ width: "15%" }}>BG Type <span className="red">*</span> </th>
                                  <th style={{ width: "15%" }}>Issuing Bank </th>
                                  <th style={{ width: "15%" }}>BG / FDR Number </th>
                                  <th style={{ width: "15%" }}>Amount </th>
                                  <th style={{ width: "15%" }}>BG / FDR Date</th>
                                  <th style={{ width: "15%" }}>Expiry Date <span className="red">*</span></th>
                                  <th style={{ width: "15%" }}>Release Date</th>
                                  <th style={{ width: "15%" }}>Action</th>
                                </tr>
                              </thead>
                              <tbody>
                                {bgDetailsListFields.length > 0 ? (
                                  bgDetailsListFields.map((item, index) => (
                                    <tr key={item.id}>
                                      <td>
                                        <Controller
                                          name={`bgDetailsList.${index}.bg_type_fks`}
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
                                      {errors.bgDetailsList?.[index]?.bg_type_fks && (
                                        <span className="red">
                                          {errors.bgDetailsList[index].bg_type_fks.message}
                                        </span>
                                      )}
                                      </td>
                                      <td>
                                        <input {...register(`bgDetailsList.${index}.issuing_banks`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`bgDetailsList.${index}.bg_numbers`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td className="rupee-field">
                                        <input {...register(`bgDetailsList.${index}.bg_values`)} type="number" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`bgDetailsList.${index}.bg_dates`)} type="date" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`bgDetailsList.${index}.bg_valid_uptos`)} type="date" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`bgDetailsList.${index}.release_dates`)} type="date" placeholder="Enter value" />
                                      </td>
                                      <td className="text-center d-flex align-center justify-content-center">
                                        <button
                                          type="button"
                                          className="btn btn-outline-danger"
                                          onClick={() => removeBgDetailsList(index)}
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
                                appendBgDetailsList({ bg_type_fks: "", issuing_banks: "", bg_numbers: "", bg_values: "", bg_dates: "", bg_valid_uptos: "", release_dates: "" })
                              }
                            >
                              <BiListPlus
                                size="24"
                              />
                            </button>
                          </div>
                        </>
                      )}

                </div>
                <div ref={sectionRefs.insurance} className={styles.formSection}>
                  <h6 className="d-flex justify-content-center mt-1 mb-2">Insurance Details</h6> 
                  
                  <div className="d-flex gap-30 align-center justify-content-center">
                      <label>Insurance Required ?</label>
                        <div className="d-flex gap-20" style={{padding: "10px"}}>
                          <label>
                            <input
                              type="radio"
                              value="yes"
                              {...register("insurance_required")}
                            />
                            Yes
                          </label>

                          <label>
                            <input
                              type="radio"
                              value="no"
                              {...register("insurance_required")}
                            />
                            No
                          </label>
                        </div>
                      </div>

                      <br />
                      {insuranceRequiredbutton === "yes" && (
                        <>
                          <div className="table-responsive dataTable ">
                            <table className="table table-bordered align-middle">
                              <thead className="table-light">
                                <tr>
                                  <th style={{ width: "15%" }}>Insurance Type  <span className="red">*</span> </th>
                                  <th style={{ width: "15%" }}>Issuing Agency </th>
                                  <th style={{ width: "15%" }}>Agency Address </th>
                                  <th style={{ width: "15%" }}>Insurance Number  </th>
                                  <th style={{ width: "15%" }}>Insurance Value </th>
                                  <th style={{ width: "15%" }}>Valid Upto <span className="red">*</span></th>
                                  <th style={{ width: "15%" }}>Release</th>
                                  <th style={{ width: "15%" }}>Action</th>
                                </tr>
                              </thead>
                              <tbody>
                                {insuranceRequiredFields.length > 0 ? (
                                  insuranceRequiredFields.map((item, index) => (
                                    <tr key={item.id}>
                                      
                                      <td>
                                        <Controller
                                          name={`insuranceRequired.${index}.insurance_type_fks`}
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
                                      {errors.insuranceRequired?.[index]?.insurance_type_fks && (
                                        <span className="red">
                                          {errors.insuranceRequired[index].insurance_type_fks.message}
                                        </span>
                                      )}
                                      </td>
                                      <td>
                                        <input {...register(`insuranceRequired.${index}.issuing_agencys`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`insuranceRequired.${index}.agency_addresss`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`insuranceRequired.${index}.insurance_numbers`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td className="rupee-field">
                                        <input {...register(`insuranceRequired.${index}.insurance_values`)} type="number" placeholder="Enter value" />
                                      </td>
                                      
                                      <td>
                                        <input {...register(`insuranceRequired.${index}.insurence_valid_uptos`)} rules={{ required: "required" }} type="date" placeholder="Enter value" />
                                        {errors.insuranceRequired?.[index]?.insurence_valid_uptos && (
                                        <span className="red">
                                          {errors.insuranceRequired[index].insurence_valid_uptos.message}
                                        </span>
                                      )}
                                      </td>
                                      <td>
                                        <input {...register(`insuranceRequired.${index}.insuranceStatus`)} type="checkbox" />
                                      </td>
                                      <td className="text-center d-flex align-center justify-content-center">
                                        <button
                                          type="button"
                                          className="btn btn-outline-danger"
                                          onClick={() => removeInsuranceRequired(index)}
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
                                appendInsuranceRequired({ insurance_type_fks: "", issuing_agencys: "", agency_addresss: "", insurance_numbers: "", insurance_values: "", insurence_valid_uptos: "", insuranceStatus: "" })
                              }
                            >
                              <BiListPlus
                                size="24"
                              />
                            </button>
                          </div>
                        </>
                      )}

                </div>
                <div ref={sectionRefs.milestone} className={styles.formSection}> 
                  <h6 className="d-flex justify-content-center mt-1 mb-2">Milestone Details</h6> 

                  {/* milestone */}

                  <div className="d-flex gap-30 align-center justify-content-center">
                      <label>Milestone Required ?</label>
                        <div className="d-flex gap-20" style={{padding: "10px"}}>
                          <label>
                            <input
                              type="radio"
                              value="yes"
                              {...register("milestone_requried")}
                            />
                            Yes
                          </label>

                          <label>
                            <input
                              type="radio"
                              value="no"
                              {...register("milestone_requried")}
                            />
                            No
                          </label>
                        </div>
                      </div>

                      <br />
                      {milestoneRequiredButton === "yes" && (
                        <>
                          <div className="table-responsive dataTable ">
                            <table className="table table-bordered align-middle">
                              <thead className="table-light">
                                <tr>
                                  <th style={{ width: "15%" }}>Milestone ID  <span className="red">*</span> </th>
                                  <th style={{ width: "15%" }}>Milestone Name </th>
                                  <th style={{ width: "15%" }}>Milestone Date </th>
                                  <th style={{ width: "15%" }}>Actual Date </th>
                                  <th style={{ width: "15%" }}>Revision</th>
                                  <th style={{ width: "15%" }}>Remarks  <span className="red">*</span></th>
                                  <th style={{ width: "15%" }}>Action</th>
                                </tr>
                              </thead>
                              <tbody>
                                {milestoneRequiredFields.length > 0 ? (
                                  milestoneRequiredFields.map((item, index) => (
                                    <tr key={item.id}>
                                      
                                      <td>
                                        <input {...register(`milestoneRequired.${index}.milestone_ids`)} type="text" placeholder="Enter value" defaultValue={`k-${index + 1}`} />
                                      </td>
                                      <td>
                                        <input {...register(`milestoneRequired.${index}.milestone_names`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`milestoneRequired.${index}.milestone_dates`)} type="date" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`milestoneRequired.${index}.actual_dates`)} type="date" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`milestoneRequired.${index}.revisions`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`milestoneRequired.${index}.mile_remarks`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td className="text-center d-flex align-center justify-content-center">
                                        <button
                                          type="button"
                                          className="btn btn-outline-danger"
                                          onClick={() => removeMilestoneRequired(index)}
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
                                appendMilestoneRequired({ milestone_ids: "", milestone_names: "", milestone_dates: "", actual_dates: "", revisions: "", mile_remarks: "" })
                              }
                            >
                              <BiListPlus
                                size="24"
                              />
                            </button>
                          </div>
                        </>
                      )}

                      {/* revision */}

                      <div className="d-flex gap-30 align-center justify-content-center">
                      <label>Revision Required ?</label>
                        <div className="d-flex gap-20" style={{padding: "10px"}}>
                          <label>
                            <input
                              type="radio"
                              value="yes"
                              {...register("revision_requried")}
                            />
                            Yes
                          </label>

                          <label>
                            <input
                              type="radio"
                              value="no"
                              {...register("revision_requried")}
                            />
                            No
                          </label>
                        </div>
                      </div>

                      <br />
                      {revisionRequiredButton === "yes" && (
                        <>
                          <div className="table-responsive dataTable ">
                            <table className="table table-bordered align-middle">
                              <thead className="table-light">
                                <tr>
                                  <th style={{ width: "15%" }}>Revision Number <span className="red">*</span> </th>
                                  <th style={{ width: "15%" }}>Revised Contract Value </th>
                                  <th style={{ width: "15%" }}>Current </th>
                                  <th style={{ width: "15%" }}>Revised DOC </th>
                                  <th style={{ width: "15%" }}>Current</th>
                                  <th style={{ width: "15%" }}>Approval by Bank </th>
                                  <th style={{ width: "15%" }}>Action</th>
                                </tr>
                              </thead>
                              <tbody>
                                {revisionRequiredFields.length > 0 ? (
                                  revisionRequiredFields.map((item, index) => (
                                    <tr key={item.id}>
                                      <td>
                                      <input {...register(`revisionRequired.${index}.revision_numbers`)} rules={{ required: "required" }} type="text" placeholder="Enter value" defaultValue={`R${index + 1}`} />
                                      {errors.revisionRequired?.[index]?.revision_numbers && (
                                        <span className="red">
                                          {errors.revisionRequired[index].revision_numbers.message}
                                        </span>
                                      )}
                                      </td>
                                      <td className="rupee-field">
                                        <input {...register(`revisionRequired.${index}.revised_amounts`)} type="number" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`revisionRequired.${index}.revision_amounts_statuss`)} type="checkbox" />
                                      </td>
                                      <td>
                                        <input {...register(`revisionRequired.${index}.revised_docs`)} type="date" placeholder="Enter value" />
                                      </td>
                                      
                                      <td>
                                        <input {...register(`revisionRequired.${index}.revision_statuss`)} type="checkbox"/>
                                      </td>
                                      <td>
                                        <input {...register(`revisionRequired.${index}.approvalbybankstatus`)} type="checkbox" />
                                      </td>
                                      <td className="text-center d-flex align-center justify-content-center">
                                        <button
                                          type="button"
                                          className="btn btn-outline-danger"
                                          onClick={() => removeRevisionRequired(index)}
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
                                appendRevisionRequired({ revision_numbers: "", revised_amounts: "", revision_amounts_statuss: "", revised_docs: "", revision_statuss: "", approvalbybankstatus: "" })
                              }
                            >
                              <BiListPlus
                                size="24"
                              />
                            </button>
                          </div>
                        </>
                      )}
                  
                </div>
                <div ref={sectionRefs.personnel} className={styles.formSection}>
                   
                  <h6 className="d-flex justify-content-center mt-1 mb-2">Contractor's Key Personnel</h6> 
                  
                  <div className="d-flex gap-30 align-center justify-content-center">
                      <label>Contractor's Key Required ?</label>
                        <div className="d-flex gap-20" style={{padding: "10px"}}>
                          <label>
                            <input
                              type="radio"
                              value="yes"
                              {...register("contractors_key_requried")}
                            />
                            Yes
                          </label>

                          <label>
                            <input
                              type="radio"
                              value="no"
                              {...register("contractors_key_requried")}
                            />
                            No
                          </label>
                        </div>
                      </div>

                      <br />
                      {contractorsKeyRequriedButton === "yes" && (
                        <>
                          <div className="table-responsive dataTable ">
                            <table className="table table-bordered align-middle">
                              <thead className="table-light">
                                <tr>
                                  <th style={{ width: "15%" }}>Name </th>
                                  <th style={{ width: "15%" }}>Designation </th>
                                  <th style={{ width: "15%" }}>Mobile No</th>
                                  <th style={{ width: "15%" }}>Email ID </th>
                                  <th style={{ width: "15%" }}>Action</th>
                                </tr>
                              </thead>
                              <tbody>
                                {contractorsKeyRequriedFields.length > 0 ? (
                                  contractorsKeyRequriedFields.map((item, index) => (
                                    <tr key={item.id}>
                                      
                                      <td>
                                        <input {...register(`contractorsKeyRequried.${index}.contractKeyPersonnelNames`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`contractorsKeyRequried.${index}.contractKeyPersonnelDesignations`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td>
                                        <input {...register(`contractorsKeyRequried.${index}.contractKeyPersonnelMobileNos`)} type="text" placeholder="Enter value" />
                                      </td>
                                      <td className="rupee-field">
                                        <input {...register(`contractorsKeyRequried.${index}.contractKeyPersonnelEmailIds`)} type="email" placeholder="Enter email" />
                                      </td>
                                      <td className="text-center d-flex align-center justify-content-center">
                                        <button
                                          type="button"
                                          className="btn btn-outline-danger"
                                          onClick={() => removeContractorsKeyRequried(index)}
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
                                appendContractorsKeyRequried({ contractKeyPersonnelNames: "", contractKeyPersonnelDesignations: "", contractKeyPersonnelMobileNos: "", contractKeyPersonnelEmailIds: "" })
                              }
                            >
                              <BiListPlus
                                size="24"
                              />
                            </button>
                          </div>
                        </>
                      )}

                </div>
                <div ref={sectionRefs.documents} className={styles.formSection}>
                   
                  <h6 className="d-flex justify-content-center mt-1 mb-2">Documents</h6> 

                  <div className="table-responsive dataTable ">
                            <table className="table table-bordered align-middle">
                              <thead className="table-light">
                                <tr>
                                  <th style={{ width: "15%" }}>File Type </th>
                                  <th style={{ width: "15%" }}>Name </th>
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
                                          name={`documentsTable.${index}.contract_file_types`}
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
                                          {...register(`documentsTable.${index}.contractDocumentNames`)}
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
                                            {...register(`documentsTable.${index}.contractDocumentFiles`)}
                                            className={styles["file-upload-input"]}
                                          />
                                          {watch(`documentsTable.${index}.contractDocumentFiles`)?.[0]?.name && (
                                            <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                              Selected: {watch(`documentsTable.${index}.contractDocumentFiles`)[0].name}
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
                                appendDocumentsTable({ contract_file_types: "", contractDocumentNames: "", contractDocumentFiles: "" })
                              }
                            >
                              <BiListPlus
                                size="24"
                              />
                            </button>
                          </div>

                </div>

                {/* Action Buttons */}
                <div className="d-flex justify-content-center gap-20 mt-4 mb-4">
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={handleCancel}
                    disabled={loading || savingForEdit}
                  >
                    Cancel
                  </button>
                  <button
                    type="button"
                    className={`bttttnnn ${styles.saveEditButton}`}
                    onClick={handleSubmit((data) => onSubmit(data, true))}
                    disabled={loading || savingForEdit}
                  >
                    {savingForEdit ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                        Saving...
                      </>
                    ) : (
                      "SAVE & EDIT"
                    )}
                  </button>
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={loading || savingForEdit}
                  >
                    {loading ? (
                      <>
                        <span className="spinner-border spinner-border-sm me-2" role="status" aria-hidden="true"></span>
                        {isEdit ? "Updating..." : "Adding..."}
                      </>
                    ) : (
                      isEdit ? "Update Contract" : "Add Contract"
                    )}
                  </button>
                </div>

              </div>
            </div>
        </form>
      <Outlet />
    </div>
  );
}