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

  // State for dropdown options
  const [projectOptions, setProjectOptions] = useState([]);
  const [hodOptions, setHodOptions] = useState([]);
  const [dyHodOptions, setDyHodOptions] = useState([]);
  const [departmentOptions, setDepartmentOptions] = useState([]);
  const [contractTypeOptions, setContractTypeOptions] = useState([]);
  const [contractorOptions, setContractorOptions] = useState([]);
  const [executiveOptions, setExecutiveOptions] = useState({});
  const [bgTypeOptions, setBgTypeOptions] = useState([]);
  const [insuranceTypeOptions, setInsuranceTypeOptions] = useState([]);
  const [contractStatusOptions, setContractStatusOptions] = useState([]);
  const [bankNameOptions, setBankNameOptions] = useState([]);
  const [unitOptions, setUnitOptions] = useState([]); 
  const [fileTypeOptions, setFileTypeOptions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [savingForEdit, setSavingForEdit] = useState(false);
  const [formLoading, setFormLoading] = useState(true);
  const [loadingExecutives, setLoadingExecutives] = useState(false);
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
      estimated_cost_units: "Rs", // Changed to match DB field name

      executives: [{ department_fks: "", responsible_people_id_fks: "" }],
      tenderBidRevisions: [{ revisionno: "R1", revision_estimated_cost: "", revision_planned_date_of_award: "", revision_planned_date_of_completion: "", notice_inviting_tender: "", tender_bid_opening_date: "", technical_eval_approval: "", financial_eval_approval: "", tender_bid_remarks: "" }],
      bgDetailsList: [{ bg_type_fks: "", issuing_banks: "", bg_numbers: "", bg_values: "", bg_unit: "Rs", bg_dates: "", bg_valid_uptos: "", release_dates: "" }],
      insuranceRequired: [{ insurance_type_fks: "", issuing_agencys: "", agency_addresss: "", insurance_numbers: "", insurance_values: "", insurance_unit: "Rs", insurence_valid_uptos: "", insuranceStatus: "" }],
      milestoneRequired: [{ milestone_ids: "K-1", milestone_names: "", milestone_dates: "", actual_dates: "", revisions: "", mile_remarks: "" }],
      revisionRequired: [{ revision_numbers: "R1", revised_amounts: "", revision_unit: "Rs", revision_amounts_statuss: "", revised_docs: "", revision_statuss: "", approvalbybankstatus: "" }],
      contractorsKeyRequried: [{ contractKeyPersonnelNames: "", contractKeyPersonnelDesignations: "", contractKeyPersonnelMobileNos: "", contractKeyPersonnelEmailIds: "" }],
      documentsTable: [{ contract_file_types: "", contractDocumentNames: "", contractDocumentFiles: "" }],
    }
  });

  const bankFunded = watch("bank_funded");
  const contractStatus = watch("contract_status");
  const bgDetails = watch("bg_required");
  const insuranceRequiredbutton = watch("insurance_required");
  const milestoneRequiredButton = watch("milestone_requried");
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

  // Generate revision number based on index (for tender bid revisions)
  const generateRevisionNumber = (index) => {
    return `R${index + 1}`;
  };

  // Generate milestone ID based on index
  const generateMilestoneId = (index) => {
    return `K-${index + 1}`;
  };

  // Generate revision number for revision required section
  const generateRevisionRequiredNumber = (index) => {
    return `R${index + 1}`;
  };

  // Handle adding new tender bid revision
  const handleAppendTenderBidRevision = () => {
    const nextRevisionNumber = generateRevisionNumber(tenderBidRevisionsFields.length);
    appendTenderBidRevisions({
      revisionno: nextRevisionNumber,
      revision_estimated_cost: "",
      revision_planned_date_of_award: "",
      revision_planned_date_of_completion: "",
      notice_inviting_tender: "",
      tender_bid_opening_date: "",
      technical_eval_approval: "",
      financial_eval_approval: "",
      tender_bid_remarks: ""
    });
  };

  // Handle removing tender bid revision (prevent deletion of R1)
  const handleRemoveTenderBidRevision = (index) => {
    // Check if this is the first revision (R1)
    const revisionNumber = getValues(`tenderBidRevisions.${index}.revisionno`);
    if (revisionNumber === "R1") {
      alert("Cannot delete the first revision (R1).");
      return;
    }
    removeTenderBidRevisions(index);
  };

  // Handle adding new milestone
  const handleAppendMilestone = () => {
    const nextMilestoneId = generateMilestoneId(milestoneRequiredFields.length);
    appendMilestoneRequired({
      milestone_ids: nextMilestoneId,
      milestone_names: "",
      milestone_dates: "",
      actual_dates: "",
      revisions: "",
      mile_remarks: ""
    });
  };

  // Handle removing milestone (prevent deletion of K-1)
  const handleRemoveMilestone = (index) => {
    // Check if this is the first milestone (K-1)
    const milestoneId = getValues(`milestoneRequired.${index}.milestone_ids`);
    if (milestoneId === "K-1") {
      alert("Cannot delete the first milestone (K-1).");
      return;
    }
    removeMilestoneRequired(index);
  };

  // Handle adding new revision required
  const handleAppendRevisionRequired = () => {
    const nextRevisionNumber = generateRevisionRequiredNumber(revisionRequiredFields.length);
    appendRevisionRequired({
      revision_numbers: nextRevisionNumber,
      revised_amounts: "",
      revision_unit: "Rs",
      revision_amounts_statuss: "",
      revised_docs: "",
      revision_statuss: "",
      approvalbybankstatus: ""
    });
  };

  // Handle removing revision required (prevent deletion of R1)
  const handleRemoveRevisionRequired = (index) => {
    // Check if this is the first revision (R1)
    const revisionNumber = getValues(`revisionRequired.${index}.revision_numbers`);
    if (revisionNumber === "R1") {
      alert("Cannot delete the first revision (R1).");
      return;
    }
    removeRevisionRequired(index);
  };

  // Fetch dropdown data on component mount
  useEffect(() => {
    const fetchDropdownData = async () => {
      try {
        setFormLoading(true);
        const response = await api.post(`${API_BASE_URL}/contract/add-contract-form`, {});

        if (response.data.success) {
          // Transform and set dropdown options
          
          // Projects: project_id and project_name
          const projects = response.data.projectsList || [];
          const projectOpts = projects.map(project => ({
            value: project.project_id,
            label:`${project.project_id} - ${project.project_name}`
          }));
          setProjectOptions(projectOpts);

          // HOD: designation and user_name
          const hodList = response.data.hodList || [];
          const hodOpts = hodList.map(hod => ({
               value: hod.user_id || hod.id || hod.designation + "_" + hod.user_name,
            label:`${hod.designation} - ${hod.user_name}`
          }));
          setHodOptions(hodOpts);

          // Dy HOD: designation and user_name
          const dyHodList = response.data.dyHodList || [];
          const dyHodOpts = dyHodList.map(dyHod => ({
            value: dyHod.dy_hod_user_id_fk,
            label: `${dyHod.designation} - ${dyHod.user_name}`
          }));
          setDyHodOptions(dyHodOpts);

          // Contract Department: department_name
          const departments = response.data.departmentList || [];
          const deptOpts = departments.map(dept => ({
            value: dept.department_fk,
            label: dept.department_name
          }));
          setDepartmentOptions(deptOpts);

          // Contract Type
          const contractTypes = response.data.contract_type || [];
          const contractTypeOpts = contractTypes.map(type => ({
            value: type.contract_type_fk,
            label: type.contract_type_fk
          }));
          setContractTypeOptions(contractTypeOpts);

          // Contractors
          const contractors = response.data.contractors || [];
          const contractorOpts = contractors.map(contractor => ({
            value: contractor.id || contractor.value,
            label: contractor.name || contractor.label
          }));
          setContractorOptions(contractorOpts);

          // Responsible People (Executives)
		  const responsiblePeople = response.data.responsiblePeopleList || [];
		  const executiveOpts = responsiblePeople.map(person => ({
		    value: person.id || person.value, // This should be a unique ID
		    label: `${person.designation || ''} - ${person.user_name || person.name || person.label}`.trim()
		  }));
		  setExecutiveOptions(executiveOpts);
          // Bank Guarantee Type
          const bgTypes = response.data.bankGuaranteeType || [];
          const bgTypeOpts = bgTypes.map(type => ({
            value: type.id || type.bg_type_fk,
            label: type.bg_type_fk
          }));
          setBgTypeOptions(bgTypeOpts);

          // Insurance Type
          const insuranceTypes = response.data.insurance_type || [];
          const insuranceTypeOpts = insuranceTypes.map(type => ({
            value: type.id || type.insurance_type,
            label: type.insurance_type
          }));
          setInsuranceTypeOptions(insuranceTypeOpts);

          // Contract Status
          const contractStatuses = response.data.contract_Statustype || [];
          const contractStatusOpts = contractStatuses.map(status => ({
			 value:  status.contract_status_fk,
			  label: status.contract_status_fk 
			}));
          setContractStatusOptions(contractStatusOpts);

          // Bank Names
          const bankNames = response.data.bankNameList || [];
          const bankNameOpts = bankNames.map(bank => ({
            value: bank.id || bank.value || bank.bank_name,
            label: bank.bank_name || bank.label || bank.name
          }));
          setBankNameOptions(bankNameOpts);

          // Unit Options - Added this section
          // If API returns unit options, use them, otherwise use default ones
          const units = response.data.unitsList || [];
          let unitOpts = units.map(unit => ({
              value: unit.id || unit.value,
              label: unit.unit || unit.label
            }));
          
          // Check if "Rs" exists in unit options
          const hasRs = unitOpts.some(unit => 
            unit.value === "Rs" || unit.label === "Rs" || 
            unit.value === "₹" || unit.label === "₹" ||
            unit.value === "INR" || unit.label === "INR"
          );
          
          // If "Rs" doesn't exist, add it as the first option
          if (!hasRs) {
            unitOpts = [
              { value: "Rs", label: "Rs" },
              ...unitOpts
            ];
          }
          
          setUnitOptions(unitOpts);
		  
		  const fileTypes = response.data.contractFileTypeList || [];
		  const fileTypeOpts = fileTypes.map(fileType => ({
		    value: fileType.contract_file_type, // Use the type as value
		    label: fileType.contract_file_type  // Use the type as
		  }));
		  setFileTypeOptions(fileTypeOpts);

          console.log("Dropdown data loaded successfully");
        } else {
          console.error("Failed to load dropdown data:", response.data.message);
        }
      } catch (error) {
        console.error("Error fetching dropdown data:", error);
      } finally {
        setFormLoading(false);
      }
    };

    fetchDropdownData();
  }, []);
  const fetchExecutivesByDepartment = async (departmentId) => {
    try {
      setLoadingExecutives(true);
      
      const response = await api.post(`${API_BASE_URL}/contract/ajax/getExecutivesListForContractForm`, {
        department_fk: departmentId // Send department ID to filter
      });
      
      if (response.data && Array.isArray(response.data)) {
        const executiveOpts = response.data.map(person => ({
          value: person.user_id || person.id || `${person.designation}_${person.user_name}`,
          label: `${person.designation || ''} - ${person.user_name || person.name || ''}`.trim()
        }));
        
        // Store executives for this department
        setExecutiveOptions(prev => ({
          ...prev,
          [departmentId]: executiveOpts
        }));
      }
    } catch (error) {
      console.error("Error fetching executives:", error);
    } finally {
      setLoadingExecutives(false);
    }
  };
  // Load edit data if in edit mode
  useEffect(() => {
    if (isEdit && row) {
      // Set form values from row data
      Object.keys(row).forEach(key => {
        if (row[key] !== undefined && row[key] !== null) {
          setValue(key, row[key]);
        }
      });

      // Handle array fields if present in row data
      if (row.executives && Array.isArray(row.executives)) {
        row.executives.forEach((executive, index) => {
          setValue(`executives.${index}`, executive);
        });
      }

      // Similarly handle other array fields as needed
    }
  }, [isEdit, row, setValue]);

  const onSubmit = async (data, saveForEdit = false) => {
    if (saveForEdit) setSavingForEdit(true);
    else setLoading(true);

    // ✅ helper: return number or null
    const toIntOrNull = (v) => {
      if (v === undefined || v === null || v === "") return null;
      const n = Number(v);
      return Number.isFinite(n) ? n : null;
    };

    // ✅ helper: clean string
    const toStr = (v) => (v === undefined || v === null ? "" : String(v));

    try {
      const payload = {
        // ✅ FK fields should be numbers
        project_id_fk: toIntOrNull(data?.project_id_fk),
        hod_user_id_fk: toIntOrNull(data?.hod_user_id_fk),
        dy_hod_user_id_fk: toIntOrNull(data?.dy_hod_user_id_fk),
        contract_type_fk: toIntOrNull(data?.contract_type_fk),
        contractor_id_fk: toIntOrNull(data?.contractor_id_fk),

        // strings
        contract_status: toStr(data?.contract_status || "No"),
        contract_department: toStr(data?.contract_department),
        contract_short_name: toStr(data?.contract_short_name),
        bank_funded: toStr(data?.bank_funded || "No"),
        bank_name: toStr(data?.bank_name),
        type_of_review: toStr(data?.type_of_review),
        contract_name: toStr(data?.contract_name),

        scope_of_contract: toStr(data?.scope_of_contract),
        loa_letter_number: toStr(data?.loa_letter_number),
        loa_date: toStr(data?.loa_date),
        ca_no: toStr(data?.ca_no),
        ca_date: toStr(data?.ca_date),
        date_of_start: toStr(data?.date_of_start),
        doc: toStr(data?.doc),

        awarded_cost: toStr(data?.awarded_cost),
        awarded_cost_units: "Rs",
        estimated_cost: toStr(data?.estimated_cost),
        estimated_cost_units: toStr(data?.estimated_cost_units || "Rs"),

        planned_date_of_award: toStr(data?.planned_date_of_award),
        planned_date_of_completion: toStr(data?.planned_date_of_completion),

        tender_opening_date: toStr(data?.tender_opening_date),
        technical_eval_submission: toStr(data?.technical_eval_submission),
        financial_eval_submission: toStr(data?.financial_eval_submission),
        remarks: toStr(data?.remarks),

        status: "Active",
        is_contract_closure_initiated: "No",

        // ============================
        // ✅ Lists - make *_fk numeric
        // ============================

        bankGauranree: (data?.bgDetailsList || [])
          .filter(x => toIntOrNull(x?.bg_type_fks) !== null) // ✅ only valid rows
          .map(x => ({
            bg_type_fk: toIntOrNull(x?.bg_type_fks),
            issuing_bank: toStr(x?.issuing_banks),
            bg_number: toStr(x?.bg_numbers),
            bg_value: toStr(x?.bg_values),
            bg_value_units: toStr(x?.bg_unit || "Rs"),
            bg_date: toStr(x?.bg_dates),
            bg_valid_upto: toStr(x?.bg_valid_uptos),
            release_date: toStr(x?.release_dates)
          })),

        insurence: (data?.insuranceRequired || [])
          .filter(x => toIntOrNull(x?.insurance_type_fks) !== null)
          .map(x => ({
            insurance_type_fk: toIntOrNull(x?.insurance_type_fks),
            issuing_agency: toStr(x?.issuing_agencys),
            agency_address: toStr(x?.agency_addresss),
            insurance_number: toStr(x?.insurance_numbers),
            insurance_value: toStr(x?.insurance_values),
            insurance_value_units: toStr(x?.insurance_unit || "Rs"),
            insurence_valid_upto: toStr(x?.insurence_valid_uptos),
            released_fk: x?.insuranceStatus ? "Yes" : "No"
          })),

        milestones: (data?.milestoneRequired || [])
          .filter(x => toStr(x?.milestone_names).trim() !== "")
          .map(x => ({
            milestone_id: toIntOrNull(x?.milestone_ids),
            milestone_name: toStr(x?.milestone_names),
            milestone_date: toStr(x?.milestone_dates),
            actual_date: toStr(x?.actual_dates),
            revision: toStr(x?.revisions),
            mile_remark: toStr(x?.mile_remarks),
            status: "Active"
          })),

        contract_revision: (data?.revisionRequired || [])
          .filter(x => toStr(x?.revision_numbers).trim() !== "")
          .map(x => ({
            revision_number: toStr(x?.revision_numbers),
            revised_amount: toStr(x?.revised_amounts),
            revised_amount_units: toStr(x?.revision_unit || "Rs"),
            revised_doc: toStr(x?.revised_docs),
            revision_amounts_status: x?.revision_amounts_statuss ? "Yes" : "No",
            approval_by_bank: x?.approvalbybankstatus ? "Yes" : "No",
            revision_status: x?.revision_statuss ? "Completed" : "Pending"
          })),

        contractKeyPersonnels: (data?.contractorsKeyRequried || [])
          .filter(x => toStr(x?.contractKeyPersonnelNames).trim() !== "")
          .map(x => ({
            name: toStr(x?.contractKeyPersonnelNames),
            designation: toStr(x?.contractKeyPersonnelDesignations),
            mobile_no: toStr(x?.contractKeyPersonnelMobileNos),
            email_id: toStr(x?.contractKeyPersonnelEmailIds)
          })),

        executivesList: (data?.executives || [])
          .filter(x => toIntOrNull(x?.department_fks) !== null && toIntOrNull(x?.responsible_people_id_fks) !== null)
          .map(x => ({
            department_fk: toIntOrNull(x?.department_fks),
            responsible_people_id_fk: toIntOrNull(x?.responsible_people_id_fks)
          })),

        // ✅ documents: remove blank rows and force contract_file_type_fk numeric
        contractDocuments: (data?.documentsTable || [])
          .filter(d => toStr(d?.contractDocumentNames).trim() !== "" && toIntOrNull(d?.contract_file_types) !== null)
          .map(d => ({
            name: toStr(d?.contractDocumentNames),
            contract_file_type_fk: toIntOrNull(d?.contract_file_types)
          }))
      };

      console.log("✅ Payload:", payload);

      const url = isEdit
        ? `${API_BASE_URL}/contract/update-contract`
        : `${API_BASE_URL}/contract/add-contract`;

      const response = await api.post(url, payload, {
        headers: { "Content-Type": "application/json" },
        withCredentials: true
      });

      if (response.data) {
        if (saveForEdit) alert("Contract saved successfully! You can continue editing.");
        else alert(isEdit ? "Contract updated successfully!" : "Contract added successfully!");
      }
    } catch (error) {
      console.error(error);
      alert(error?.response?.data?.message || error.message || "Failed");
    } finally {
      if (saveForEdit) setSavingForEdit(false);
      else setLoading(false);
    }
  };




  const handleCancel = () => {
    if (window.confirm("Are you sure you want to cancel? Any unsaved changes will be lost.")) {
      navigate(-1); // Go back to previous page
    }
  };

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
    // Don't set up scroll listener while form is loading
    if (formLoading) return;

    const handleScroll = () => {
      let current = "";

      Object.keys(sectionRefs).forEach((key) => {
        const section = sectionRefs[key].current;
        if (section) {
          const top = section.getBoundingClientRect().top;

          if (top <= 150 && top >= -200) {
            current = key;
          }
        }
      });

      if (current) setActiveTab(current);
    };

    window.addEventListener("scroll", handleScroll);
    return () => window.removeEventListener("scroll", handleScroll);
  }, [formLoading]); // Re-run when formLoading changes

  useEffect(() => {
    console.log("📋 Current Form Mode:", isEdit ? "EDIT" : "ADD");
    if (isEdit) console.log("🗂️ Editing Existing Record:", row);
  }, [isEdit, row]);

  if (formLoading) {
    return (
      <div className="d-flex justify-content-center align-items-center" style={{ height: "400px" }}>
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Loading...</span>
        </div>
        <span className="ms-2">Loading form data...</span>
      </div>
    );
  }

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
                        value={projectOptions.find(opt => opt.value === field.value) || null}
                        onChange={(opt) => field.onChange(opt?.value || "")}
                      />
                    )}
                  />
                  {errors.project_id_fk && (
                    <p className="red">Project is required</p>
                  )}
                </div>
                <div className="form-field">
                  <label>Contract Awarded</label>
                  <div className="d-flex gap-20" style={{ padding: "10px" }}>
                    <label>
                      <input
                        type="radio"
                        value="yes"
                        {...register("contract_status", { required: "Please select Contract Awarded status" })}
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
                  {errors.contract_status && (
                    <p className="red">{errors.contract_status.message}</p>
                  )}
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
                        options={hodOptions}
                        placeholder="Select HOD"
                        isSearchable
                        isClearable
                        value={hodOptions.find(opt => opt.value === field.value) || null}
                        onChange={(opt) => field.onChange(opt?.value || "")}
                      />
                    )}
                  />
                  {errors.hod_user_id_fk && (
                    <p className="red">HOD is required</p>
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
                        options={dyHodOptions}
                        placeholder="Select Dy HOD"
                        isSearchable
                        isClearable
                        value={dyHodOptions.find(opt => opt.value === field.value) || null}
                        onChange={(opt) => field.onChange(opt?.value || "")}
                      />
                    )}
                  />
                  {errors.dy_hod_user_id_fk && (
                    <p className="red">Dy HOD is required</p>
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
                        options={departmentOptions}
                        placeholder="Select Department"
                        isSearchable
                        isClearable
                        value={departmentOptions.find(opt => opt.value === field.value) || null}
                        onChange={(opt) => field.onChange(opt?.value || "")}
                      />
                    )}
                  />
                  {errors.contract_department && (
                    <p className="red">Contract Department is required</p>
                  )}
                </div>

                <div className="form-field">
                  <label>Bank Funded</label>
                  <div className="d-flex gap-20" style={{ padding: "10px" }}>
                    <label>
                      <input
                        type="radio"
                        value="yes"
                        {...register("bank_funded")}
                      />
                      Yes
                    </label>
                    <label>
                      <input
                        type="radio"
                        value="no"
                        defaultChecked
                        {...register("bank_funded")}
                      />
                      No
                    </label>
                  </div>
                </div>
                {bankFunded === "yes" && (
                  <>
                    <div className="form-field">
                      <label>Bank Name</label>
                      <Controller
                        name="bank_name"
                        control={control}
                        render={({ field }) => (
                          <Select
                            {...field}
                            classNamePrefix="react-select"
                            options={bankNameOptions}
                            placeholder="Select Bank"
                            isSearchable
                            isClearable
                            value={bankNameOptions.find(opt => opt.value === field.value) || null}
                            onChange={(opt) => field.onChange(opt?.value || "")}
                          />
                        )}
                      />
                    </div>
                    <div className="form-field">
                      <label>Type of Review</label>
                      <input
                        type="text"
                        {...register("type_of_review")}
                        placeholder="Enter Value"
                      />
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
                              rules={{ required: "Department is required" }}
                              render={({ field }) => (
                                <Select
                                  {...field}
                                  classNamePrefix="react-select"
                                  options={departmentOptions}
                                  placeholder="Select Department"
                                  isSearchable
                                  isClearable
                                  value={departmentOptions.find(opt => opt.value === field.value) || null}
								    onChange={(opt) => {
								          field.onChange(opt?.value || "");
								          // Clear responsible people when department changes
								          setValue(`executives.${index}.responsible_people_id_fks`, []);
								          // Fetch executives for selected department
								          if (opt?.value) {
								            fetchExecutivesByDepartment(opt.value);
								          }
								        }}
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
						    render={({ field }) => {
						      const selectedDept = watch(`executives.${index}.department_fks`);
						      const deptExecutives = executiveOptions[selectedDept] || [];
						      
						      return (
						        <Select
						          {...field}
						          options={deptExecutives}
						          isMulti
						          placeholder={loadingExecutives ? "Loading..." : "Select Executives"}
						          classNamePrefix="react-select"
						          isDisabled={!selectedDept || loadingExecutives}
						          onChange={(selected) => {
						            const selectedValues = selected ? selected.map(option => option.value) : [];
						            field.onChange(selectedValues);
						          }}
						          value={deptExecutives.filter(option => 
						            Array.isArray(field.value) 
						              ? field.value.includes(option.value)
						              : false
						          )}
						        />
						      );
						    }}
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
                              <MdOutlineDeleteSweep size="26" />
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
                  <BiListPlus size="24" />
                </button>
              </div>
            </div>

            <div ref={sectionRefs.details} className={styles.formSection}>
              <h6 className="d-flex justify-content-center mt-1 mb-2">Contract Details</h6>
              
              {/* Contract Short Name is now ONLY in Contract Details section */}
              <div className="form-row">
                <div className="form-field">
                  <label>Contract Short Name <span className="red">*</span> </label>
                  <input
                    type="text"
                    maxLength={100}
                    {...register("contract_short_name", { required: "Contract Short Name is required" })}
                    placeholder="Enter Value"
                  />
                  <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                    {watch("contract_short_name")?.length || 0}/100
                  </div>
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
                    maxLength={200}
                    rows="3"
                    placeholder="Enter contract name"
                  ></textarea>
                  <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                    {watch("contract_name")?.length || 0}/200
                    {errors.contract_name && <span className="red">{errors.contract_name.message}</span>}
                  </div>
                </div>
              </div>

              {contractStatus === "yes" && (
                <>
                  <div className="form-row">
                    <div className="form-field">
                      <label>Contract Type <span className="red">*</span></label>
                      <Controller
                        name="contract_type_fk"
                        control={control}
                        rules={{ required: "Contract Type is required" }}
                        render={({ field }) => (
                          <Select
                            {...field}
                            classNamePrefix="react-select"
                            options={contractTypeOptions}
                            placeholder="Select Contract Type"
                            isSearchable
                            isClearable
                            value={contractTypeOptions.find(opt => opt.value === field.value) || null}
                            onChange={(opt) => field.onChange(opt?.value || "")}
                          />
                        )}
                      />
                      {errors.contract_type_fk && (
                        <p className="error-text">{errors.contract_type_fk.message}</p>
                      )}
                    </div>
         
                  </div>

                  {/* Rest of the Contract Details section remains the same */}
                  <div className="form-row">
                    <div className="form-field">
                      <label>Scope of Contract</label>
                      <textarea
                        {...register("scope_of_contract")}
                        maxLength={200}
                        rows="3"
                        placeholder="Enter scope of contract"
                      ></textarea>
                      <div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
                        {watch("scope_of_contract")?.length || 0}/200
                      </div>
                    </div>
                  </div>

                  <div className="form-row">
                    <div className="form-field">
                      <label>LOA Letter No <span className="red">*</span> </label>
                      <input
                        {...register("loa_letter_number", { required: "LOA Letter No is required" })}
                        type="text"
                        placeholder="Enter LOA Letter No"
                      />
                      {errors.loa_letter_number && (
                        <p className="error-text">{errors.loa_letter_number.message}</p>
                      )}
                    </div>
                    <div className="form-field">
                      <label>LOA Date <span className="red">*</span> </label>
                      <input
                        {...register("loa_date", { required: "LOA Date is required" })}
                        type="date"
                      />
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
                  <input {...register("ca_date")} type="date" />
                </div>
                <div className="form-field">
                  <label>Date of Start <span className="red">*</span> </label>
                  <input
                    {...register("date_of_start", { required: "Date of Start is required" })}
                    type="date"
                  />
                  {errors.date_of_start && (
                    <p className="error-text">{errors.date_of_start.message}</p>
                  )}
                </div>
                <div className="form-field">
                  <label>Original DOC <span className="red">*</span> </label>
                  <input
                    {...register("doc", { required: "Original DOC is required" })}
                    type="date"
                  />
                  {errors.doc && (
                    <p className="error-text">{errors.doc.message}</p>
                  )}
                </div>
                <div className="form-field rupee-field">
                  <label>Awarded cost <span className="red">*</span> </label>
                  <input
                    {...register("awarded_cost", { required: "Awarded cost is required" })}
                    type="number"
                    placeholder="Enter Value"
                  />
                  {errors.awarded_cost && (
                    <p className="error-text">{errors.awarded_cost.message}</p>
                  )}
                </div>
                <div className="form-field">
                  <label>Target DOC </label>
                  <input {...register("target_doc")} type="date" />
                </div>
                <div className="form-field">
                  <label>Actual Date of Commissioning <span className="red">*</span> </label>
                  <input
                    {...register("actual_date_of_commissioning", { required: "Actual Date of Commissioning is required" })}
                    type="date"
                  />
                  {errors.actual_date_of_commissioning && (
                    <p className="error-text">{errors.actual_date_of_commissioning.message}</p>
                  )}
                </div>
                <div className="form-field">
                  <label>Status of Work <span className="red">*</span></label>
                  <Controller
                    name="contract_status_fk"
                    control={control}
                    rules={{ required: "Status of Work is required" }}
                    render={({ field }) => (
                      <Select
                        {...field}
                        classNamePrefix="react-select"
                        options={contractStatusOptions}
                        placeholder="Select Status"
                        isSearchable
                        isClearable
                        value={contractStatusOptions.find(opt => opt.value === field.value) || null}
                        onChange={(opt) => field.onChange(opt?.value || "")}
                      />
                    )}
                  />
                  {errors.contract_status_fk && (
                    <p className="error-text">{errors.contract_status_fk.message}</p>
                  )}
                </div>
                <div className="form-field rupee-field">
                  <label>Detailed Estimated cost </label>
                  <div className="d-flex align-items-center gap-2">
                    <input 
                      {...register("estimated_cost")} 
                      type="number" 
                      placeholder="Enter Value" 
                      className="flex-grow-1"
					            style={{ minWidth: "300px" }}
                    />
                    <Controller
                      name="estimated_cost_units"
                      control={control}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          options={unitOptions}
                          placeholder="Unit"
                          isSearchable={false}
                          styles={{
                            container: (base) => ({
                              ...base,
                              minWidth: '120px',
                              width: '120px'
                            })
                          }}
                          value={unitOptions.find(opt => opt.value === field.value) || null}
                          onChange={(opt) => field.onChange(opt?.value || "")}
                        />
                      )}
                    />
                  </div>
                </div>
                <div className="form-field">
                  <label>Planned date of award </label>
                  <input {...register("planned_date_of_award")} type="date" />
                </div>
                <div className="form-field">
                  <label>Planned date of completion </label>
                  <input {...register("planned_date_of_completion")} type="date" />
                </div>
                <div className="form-field">
                  <label>Notice Inviting Tender </label>
                  <input {...register("contract_notice_inviting_tender")} type="date" />
                </div>
                <div className="form-field">
                  <label>Tender Opening Date </label>
                  <input {...register("tender_opening_date")} type="date" />
                </div>
                <div className="form-field">
                  <label>Technical Eval. Submission </label>
                  <input {...register("technical_eval_submission")} type="date" />
                </div>
                <div className="form-field">
                  <label>Financial Eval. Submission </label>
                  <input {...register("financial_eval_submission")} type="date" />
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
                              <input 
                                {...register(`tenderBidRevisions.${index}.revisionno`)} 
                                type="text" 
                                placeholder="Enter value" 
                                value={generateRevisionNumber(index)}
                                readOnly
                                className="readonly-input"
                                style={{ backgroundColor: "#f8f9fa", cursor: "not-allowed" }}
                              />
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
                              onClick={() => handleRemoveTenderBidRevision(index)}
                              disabled={index === 0} // Disable delete for first row (R1)
                              title={index === 0 ? "Cannot delete first revision" : "Delete row"}
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
                  onClick={handleAppendTenderBidRevision}
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
                <div className="d-flex gap-20" style={{ padding: "10px" }}>
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
                      defaultChecked
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
                          <th style={{ width: "20%" }}>BG Type <span className="red">*</span> </th>
                          <th style={{ width: "15%" }}>Issuing Bank </th>
                          <th style={{ width: "15%" }}>BG / FDR Number </th>
                          <th style={{ width: "15%" }}>Amount </th>
                          <th style={{ width: "10%" }}>BG / FDR Date</th>
                          <th style={{ width: "10%" }}>Expiry Date <span className="red">*</span></th>
                          <th style={{ width: "10%" }}>Release Date</th>
                          <th style={{ width: "7%" }}>Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        {bgDetailsListFields.length > 0 ? (
                          bgDetailsListFields.map((item, index) => (
                            <tr key={item.id}>
                              <td>
                                <Controller
                                  name={`bgDetailsList.${index}.bg_type_fks`}
                                  rules={{ required: "BG Type is required" }}
                                  control={control}
                                  render={({ field }) => (
                                    <Select
                                      {...field}
                                      classNamePrefix="react-select"
                                      options={bgTypeOptions}
                                      placeholder="Select BG Type"
                                      isSearchable
                                      value={bgTypeOptions.find(opt => opt.value === field.value) || null}
                                      onChange={(opt) => field.onChange(opt?.value || "")}
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
                                <div className="d-flex align-items-center gap-2">
                                  <input 
                                    {...register(`bgDetailsList.${index}.bg_values`)} 
                                    type="number" 
                                    placeholder="Enter amount" 
                                    className="flex-grow-1"
                                    style={{ minWidth: "120px" }}
                                  />
                                  <Controller
                                    name={`bgDetailsList.${index}.bg_unit`}
                                    control={control}
                                    render={({ field }) => (
                                      <Select
                                        {...field}
                                        classNamePrefix="react-select"
                                        options={unitOptions}
                                        placeholder="Unit"
                                        isSearchable={false}
                                        styles={{
                                          container: (base) => ({
                                            ...base,
                                            minWidth: '80px',
                                            width: '80px'
                                          }),
                                          control: (base) => ({
                                            ...base,
                                            minHeight: '38px',
                                            fontSize: '14px'
                                          })
                                        }}
                                        value={unitOptions.find(opt => opt.value === field.value) || null}
                                        onChange={(opt) => field.onChange(opt?.value || "")}
                                      />
                                    )}
                                  />
                                </div>
                              </td>
                              <td>
                                <input {...register(`bgDetailsList.${index}.bg_dates`)} type="date" />
                              </td>
                              <td>
                                <input {...register(`bgDetailsList.${index}.bg_valid_uptos`)} type="date" />
                              </td>
                              <td>
                                <input {...register(`bgDetailsList.${index}.release_dates`)} type="date" />
                              </td>
                              <td className="text-center d-flex align-center justify-content-center">
                                <button
                                  type="button"
                                  className="btn btn-outline-danger"
                                  onClick={() => removeBgDetailsList(index)}
                                >
                                  <MdOutlineDeleteSweep size="26" />
                                </button>
                              </td>
                            </tr>
                          ))
                        ) : (
                          <tr>
                            <td colSpan="8" className="text-center text-muted">
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
                        appendBgDetailsList({ bg_type_fks: "", issuing_banks: "", bg_numbers: "", bg_values: "", bg_unit: "Rs", bg_dates: "", bg_valid_uptos: "", release_dates: "" })
                      }
                    >
                      <BiListPlus size="24" />
                    </button>
                  </div>
                </>
              )}
            </div>

            <div ref={sectionRefs.insurance} className={styles.formSection}>
              <h6 className="d-flex justify-content-center mt-1 mb-2">Insurance Details</h6>

              <div className="d-flex gap-30 align-center justify-content-center">
                <label>Insurance Required ?</label>
                <div className="d-flex gap-20" style={{ padding: "10px" }}>
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
                      defaultChecked
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
                          <th style={{ width: "20%" }}>Insurance Type <span className="red">*</span> </th>
                          <th style={{ width: "15%" }}>Issuing Agency </th>
                          <th style={{ width: "15%" }}>Agency Address </th>
                          <th style={{ width: "15%" }}>Insurance Number </th>
                          <th style={{ width: "10%" }}>Insurance Value </th>
                          <th style={{ width: "10%" }}>Valid Upto <span className="red">*</span></th>
                          <th style={{ width: "10%" }}>Release</th>
                          <th style={{ width: "7%" }}>Action</th>
                        </tr>
                      </thead>
                      <tbody>
                        {insuranceRequiredFields.length > 0 ? (
                          insuranceRequiredFields.map((item, index) => (
                            <tr key={item.id}>
                              <td>
                                <Controller
                                  name={`insuranceRequired.${index}.insurance_type_fks`}
                                  rules={{ required: "Insurance Type is required" }}
                                  control={control}
                                  render={({ field }) => (
                                    <Select
                                      {...field}
                                      classNamePrefix="react-select"
                                      options={insuranceTypeOptions}
                                      placeholder="Select Insurance Type"
                                      isSearchable
                                      value={insuranceTypeOptions.find(opt => opt.value === field.value) || null}
                                      onChange={(opt) => field.onChange(opt?.value || "")}
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
                                <div className="d-flex align-items-center gap-2">
                                  <input 
                                    {...register(`insuranceRequired.${index}.insurance_values`)} 
                                    type="number" 
                                    placeholder="Enter amount" 
                                    className="flex-grow-1"
                                    style={{ minWidth: "120px" }}
                                  />
                                  <Controller
                                    name={`insuranceRequired.${index}.insurance_unit`}
                                    control={control}
                                    render={({ field }) => (
                                      <Select
                                        {...field}
                                        classNamePrefix="react-select"
                                        options={unitOptions}
                                        placeholder="Unit"
                                        isSearchable={false}
                                        styles={{
                                          container: (base) => ({
                                            ...base,
                                            minWidth: '80px',
                                            width: '80px'
                                          }),
                                          control: (base) => ({
                                            ...base,
                                            minHeight: '38px',
                                            fontSize: '14px'
                                          })
                                        }}
                                        value={unitOptions.find(opt => opt.value === field.value) || null}
                                        onChange={(opt) => field.onChange(opt?.value || "")}
                                      />
                                    )}
                                  />
                                </div>
                              </td>
                              <td>
                                <input {...register(`insuranceRequired.${index}.insurence_valid_uptos`)} type="date" />
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
                                  <MdOutlineDeleteSweep size="26" />
                                </button>
                              </td>
                            </tr>
                          ))
                        ) : (
                          <tr>
                            <td colSpan="8" className="text-center text-muted">
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
                        appendInsuranceRequired({ insurance_type_fks: "", issuing_agencys: "", agency_addresss: "", insurance_numbers: "", insurance_values: "", insurance_unit: "Rs", insurence_valid_uptos: "", insuranceStatus: "" })
                      }
                    >
                      <BiListPlus size="24" />
                    </button>
                  </div>
                </>
              )}
            </div>            <div ref={sectionRefs.milestone} className={styles.formSection}> 
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
                              defaultChecked
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
                                        <input 
                                          {...register(`milestoneRequired.${index}.milestone_ids`)} 
                                          type="text" 
                                          placeholder="Enter value" 
                                          value={generateMilestoneId(index)}
                                          readOnly
                                          className="readonly-input"
                                          style={{ backgroundColor: "#f8f9fa", cursor: "not-allowed" }}
                                        />
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
                                          onClick={() => handleRemoveMilestone(index)}
                                          disabled={index === 0} // Disable delete for first row (K-1)
                                          title={index === 0 ? "Cannot delete first milestone" : "Delete row"}
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
                              onClick={handleAppendMilestone}
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
                              defaultChecked
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
                                        <input 
                                          {...register(`revisionRequired.${index}.revision_numbers`)} 
                                          type="text" 
                                          placeholder="Enter value" 
                                          value={generateRevisionRequiredNumber(index)}
                                          readOnly
                                          className="readonly-input"
                                          style={{ backgroundColor: "#f8f9fa", cursor: "not-allowed" }}
                                        />
                                        {errors.revisionRequired?.[index]?.revision_numbers && (
                                          <span className="red">
                                            {errors.revisionRequired[index].revision_numbers.message}
                                          </span>
                                        )}
                                      </td>
                                      <td className="rupee-field">
                                        <div className="d-flex align-items-center gap-2">
                                          <input 
                                            {...register(`revisionRequired.${index}.revised_amounts`)} 
                                            type="number" 
                                            placeholder="Enter value" 
                                            className="flex-grow-1"
                                            style={{ minWidth: "120px" }}
                                          />
                                          <Controller
                                            name={`revisionRequired.${index}.revision_unit`}
                                            control={control}
                                            render={({ field }) => (
                                              <Select
                                                {...field}
                                                classNamePrefix="react-select"
                                                options={unitOptions}
                                                placeholder="Unit"
                                                isSearchable={false}
                                                styles={{
                                                  container: (base) => ({
                                                    ...base,
                                                    minWidth: '80px',
                                                    width: '80px'
                                                  }),
                                                  control: (base) => ({
                                                    ...base,
                                                    minHeight: '38px',
                                                    fontSize: '14px'
                                                  })
                                                }}
                                                value={unitOptions.find(opt => opt.value === field.value) || null}
                                                onChange={(opt) => field.onChange(opt?.value || "")}
                                              />
                                            )}
                                          />
                                        </div>
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
                                          onClick={() => handleRemoveRevisionRequired(index)}
                                          disabled={index === 0} // Disable delete for first row (R1)
                                          title={index === 0 ? "Cannot delete first revision" : "Delete row"}
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
                              onClick={handleAppendRevisionRequired}
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
                              defaultChecked
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
                                      <td>
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
                                  <th style={{ width: "7%" }}>Action</th>
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
											  options={fileTypeOptions} 
                                              classNamePrefix="react-select"
                                              placeholder="Select File Type"
                                              isSearchable
                                              isClearable
											  value={fileTypeOptions.find(opt => opt.value === field.value) || null}
											  onChange={(opt) => field.onChange(opt?.value || "")}
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
			                   isEdit ? "Update " : "Add "
			                 )}
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
			              type="button"
			                className="btn btn-secondary"
			                onClick={handleCancel}
			                disabled={loading || savingForEdit}
			              >
			                Cancel
							
			             </button>
            </div>
          </div>
        </div>
      </form>
      <Outlet />
    </div>
  );
}