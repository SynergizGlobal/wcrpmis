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
	const { state } = useLocation();
	const contractId = state?.contractId || null;
	const isEdit = state?.isEdit || false;

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
	const [contractDetails, setContractDetails] = useState(null);
	const [departmentExecutivesMap, setDepartmentExecutivesMap] = useState({});

	const {
		register,
		control,
		handleSubmit,
		setValue,
		getValues,
		watch,
		reset,
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
			contract_ifas_code: "",
			bg_required: "",
			insurance_required: "",
			milestone_requried: "",
			revision_requried: "",
			contractors_key_requried: "",

			executives: [{ department_fks: "", responsible_people_id_fks: [] }],
			tenderBidRevisions: [{ revisionno: "R1", revision_estimated_cost: "", revision_planned_date_of_award: "", revision_planned_date_of_completion: "", notice_inviting_tender: "", tender_bid_opening_date: "", technical_eval_approval: "", financial_eval_approval: "", tender_bid_remarks: "" }],
			bgDetailsList: [{ bg_type_fks: "", issuing_banks: "", bg_numbers: "", bg_values: "", bg_unit: "", bg_dates: "", bg_valid_uptos: "", release_dates: "" }],
			insuranceRequired: [{ insurance_type_fks: "", issuing_agencys: "", agency_addresss: "", insurance_numbers: "", insurance_values: "", insurance_unit: "", insurence_valid_uptos: "", insuranceStatus: "" }],
			milestoneRequired: [{ milestone_ids: "K-1", milestone_names: "", milestone_dates: "", actual_dates: "", revisions: "", mile_remarks: "" }],
			revisionRequired: [{ revision_numbers: "R1", revised_amounts: "", revision_unit: "", revision_amounts_statuss: "", revised_docs: "", revision_statuss: "", approvalbybankstatus: "" }],
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
			revision_unit: "",
			revision_amounts_statuss: "",
			revised_docs: "",
			revision_statuss: "",
			approvalbybankstatus: ""
		});
	};

	// Handle removing revision required (prevent deletion of R1)
	const handleRemoveRevisionRequired = (index) => {
		const revisionNumber = getValues(`revisionRequired.${index}.revision_numbers`);
		if (revisionNumber === "R1") {
			alert("Cannot delete the first revision (R1).");
			return;
		}
		removeRevisionRequired(index);
	};

	// ===== ✅ UPDATED HELPER FUNCTIONS TO MATCH JSP FIELD NAMES =====

	const buildExecutivesArray = (departmentList) => {
		console.log("Building executives array from departmentList:", departmentList);
		if (!departmentList || !Array.isArray(departmentList) || departmentList.length === 0) {
			return [{ department_fks: "", responsible_people_id_fks: [] }];
		}

		return departmentList.map(dept => {
			// Get selected executives from executivesList (matching JSP logic)
			const selectedExecutives = dept.executivesList || [];
			const selectedUserIds = selectedExecutives.map(exec =>
				exec.executive_user_id_fk || exec.hod_user_id_fk
			).filter(id => id && id !== "");

			console.log(`Dept ${dept.department_fk}: selectedUserIds =`, selectedUserIds);

			return {
				department_fks: dept.department_fk || dept.department_id_fk || "",
				responsible_people_id_fks: selectedUserIds
			};
		});
	};

	const buildTenderBidRevisionsArray = (revisions) => {
		console.log("Building tender bid revisions from:", revisions);
		if (!revisions || !Array.isArray(revisions) || revisions.length === 0) {
			return [{
				revisionno: "R1",
				revision_estimated_cost: "",
				revision_planned_date_of_award: "",
				revision_planned_date_of_completion: "",
				notice_inviting_tender: "",
				tender_bid_opening_date: "",
				technical_eval_approval: "",
				financial_eval_approval: "",
				tender_bid_remarks: ""
			}];
		}

		return revisions.map((rev, index) => {
			// Match JSP field names exactly (camelCase from API)
			const revisionData = {
				revisionno: rev.revisionnumber || rev.revision_number || `R${index + 1}`,
				revision_estimated_cost: rev.revisionestimatedcost || rev.revision_estimated_cost || "",
				revision_planned_date_of_award: formatDateForInput(rev.revisionplanneddateofaward || rev.revision_planned_date_of_award),
				revision_planned_date_of_completion: formatDateForInput(rev.revisionplanneddateofcompletion || rev.revision_planned_date_of_completion),
				notice_inviting_tender: rev.noticeinvitingtender || rev.notice_inviting_tender || "",
				tender_bid_opening_date: formatDateForInput(rev.tenderbidopeningdate || rev.tender_bid_opening_date),
				technical_eval_approval: formatDateForInput(rev.technicalevalapproval || rev.technical_eval_approval),
				financial_eval_approval: formatDateForInput(rev.financialevalapproval || rev.financial_eval_approval),
				tender_bid_remarks: rev.tenderbidremarks || rev.tender_bid_remarks || ""
			};

			console.log(`Revision ${index}:`, revisionData);
			return revisionData;
		});
	};

	const buildBgDetailsArray = (bgList) => {
		console.log("Building BG details from:", bgList);
		if (!bgList || !Array.isArray(bgList) || bgList.length === 0) {
			return [{
				bg_type_fks: "",
				issuing_banks: "",
				bg_numbers: "",
				bg_values: "",
				bg_unit: "",
				bg_dates: "",
				bg_valid_uptos: "",
				release_dates: ""
			}];
		}
		return bgList.map(bg => ({
			bg_type_fks: bg.bg_type_fk || bg.bgtypefk || "",
			issuing_banks: bg.issuing_bank || bg.issuingbank || "",
			bg_numbers: bg.bg_number || bg.bgnumber || "",
			bg_values: bg.bg_value || bg.bgvalue || "",
			bg_unit: bg.bg_unit || bg.bgunit || "",
			bg_dates: formatDateForInput(bg.bg_date || bg.bgdate),
			bg_valid_uptos: formatDateForInput(bg.bg_valid_upto || bg.bgvalidupto),
			release_dates: formatDateForInput(bg.release_date || bg.releasedate)
		}));
	};

	const buildInsuranceArray = (insuranceList) => {
		console.log("Building insurance details from:", insuranceList);
		if (!insuranceList || !Array.isArray(insuranceList) || insuranceList.length === 0) {
			return [{
				insurance_type_fks: "",
				issuing_agencys: "",
				agency_addresss: "",
				insurance_numbers: "",
				insurance_values: "",
				insurance_unit: "",
				insurence_valid_uptos: "",
				insuranceStatus: ""
			}];
		}
		return insuranceList.map(ins => ({
			insurance_type_fks: ins.insurance_type_fk || ins.insurancetypefk || "",
			issuing_agencys: ins.issuing_agency || ins.issuingagency || "",
			agency_addresss: ins.agency_address || ins.agencyaddress || "",
			insurance_numbers: ins.insurance_number || ins.insurancenumber || "",
			insurance_values: ins.insurance_value || ins.insurancevalue || "",
			insurance_unit: ins.insurance_unit || ins.insuranceunit || "",
			insurence_valid_uptos: formatDateForInput(ins.insurence_valid_upto || ins.insurencevalidupto),
			insuranceStatus: ins.insurance_status === "Yes" || ins.insurancestatus === "Yes"
		}));
	};

	const buildMilestonesArray = (milestones) => {
		console.log("Building milestones from:", milestones);
		if (!milestones || !Array.isArray(milestones) || milestones.length === 0) {
			return [{
				milestone_ids: "K-1",
				milestone_names: "",
				milestone_dates: "",
				actual_dates: "",
				revisions: "",
				mile_remarks: ""
			}];
		}
		return milestones.map((mile, index) => ({
			milestone_ids: mile.milestone_id || mile.milestoneid || `K-${index + 1}`,
			milestone_names: mile.milestone_name || mile.milestonename || "",
			milestone_dates: formatDateForInput(mile.milestone_date || mile.milestonedate),
			actual_dates: formatDateForInput(mile.actual_date || mile.actualdate),
			revisions: mile.revision || "",
			mile_remarks: mile.remarks || mile.mileremarks || ""
		}));
	};

	const buildRevisionArray = (revisions) => {
		console.log("Building revision required from:", revisions);
		if (!revisions || !Array.isArray(revisions) || revisions.length === 0) {
			return [{
				revision_numbers: "R1",
				revised_amounts: "",
				revision_unit: "",
				revision_amounts_statuss: "",
				revised_docs: "",
				revision_statuss: "",
				approvalbybankstatus: ""
			}];
		}
		return revisions.map((rev, index) => ({
			revision_numbers: rev.revision_number || rev.revisionnumber || `R${index + 1}`,
			revised_amounts: rev.revised_amount || rev.revisedamount || "",
			revision_unit: rev.revised_amount_unit || rev.revisedamountunit || "",
			revised_docs: formatDateForInput(rev.revised_doc || rev.reviseddoc),
			revision_statuss: rev.revision_status === "Yes" || rev.revisionstatus === "Yes",
			revision_amounts_statuss: rev.revision_amounts_status === "Yes" || rev.revisionamountsstatus === "Yes",
			approvalbybankstatus: rev.approval_by_bank === "Yes" || rev.approvalbybank === "Yes"
		}));
	};

	const buildKeyPersonnelArray = (personnel) => {
		console.log("Building key personnel from:", personnel);
		if (!personnel || !Array.isArray(personnel) || personnel.length === 0) {
			return [{
				contractKeyPersonnelNames: "",
				contractKeyPersonnelDesignations: "",
				contractKeyPersonnelMobileNos: "",
				contractKeyPersonnelEmailIds: ""
			}];
		}
		return personnel.map(person => ({
			contractKeyPersonnelNames: person.name || person.contractkeypersonnelname || "",
			contractKeyPersonnelDesignations: person.designation || person.contractkeypersonneldesignation || "",
			contractKeyPersonnelMobileNos: person.mobile_no || person.contractkeypersonnelmobileno || "",
			contractKeyPersonnelEmailIds: person.email_id || person.contractkeypersonnelemailid || ""
		}));
	};

	const buildDocumentsArray = (documents) => {
		console.log("Building documents from:", documents);
		if (!documents || !Array.isArray(documents) || documents.length === 0) {
			return [{
				contract_file_types: "",
				contractDocumentNames: "",
				contractDocumentFiles: ""
			}];
		}

		return documents.map(doc => ({
			contractDocumentNames: doc.name || doc.contractdocumentname || "",
			// JSP uses contract_file_type_fk for selection, but stores as contract_file_type
			contract_file_types: doc.contract_file_type_fk || doc.contract_file_type || doc.file_type || doc.contractfiletype || "",
			contractDocumentFiles: "" // Files can't be pre-populated for security reasons
			// Note: Existing files are shown as download links
		}));
	};

	// ===== ✅ OPTIMIZED DATE FORMATTER =====
	const formatDateForInput = (dateString) => {
		if (!dateString) return "";

		try {
			let date;
			if (typeof dateString === 'string') {
				// If it's already in YYYY-MM-DD format
				if (dateString.match(/^\d{4}-\d{2}-\d{2}$/)) {
					return dateString;
				}
				// Parse DD-MM-YYYY format (from JSP)
				if (dateString.match(/^\d{2}-\d{2}-\d{4}$/)) {
					const [day, month, year] = dateString.split('-');
					date = new Date(`${year}-${month}-${day}`);
				} else {
					date = new Date(dateString);
				}
			} else if (dateString instanceof Date) {
				date = dateString;
			} else {
				return "";
			}

			// Check if date is valid
			if (isNaN(date.getTime())) {
				return "";
			}

			return date.toISOString().split('T')[0];
		} catch (error) {
			console.error("Error formatting date:", error);
			return "";
		}
	};

	// ===== ✅ UPDATED FORM POPULATION FUNCTION =====
	const populateFormWithContractData = (contractData) => {
		console.log("🔍 Populating form with contract data:", contractData);
		console.log("🔍 Department List structure:", contractData.departmentList);
		console.log("🔍 Contract Revisions:", contractData.contract_revisions);
		console.log("🔍 Contract Documents:", contractData.contractDocuments);
		console.time('Form Population');

		try {
			// First, build department executives map for dropdown options
			const deptExecMap = {};
			if (contractData.departmentList && Array.isArray(contractData.departmentList)) {
				contractData.departmentList.forEach(dept => {
					if (dept.department_fk && dept.responsiblePersonsList) {
						deptExecMap[dept.department_fk] = dept.responsiblePersonsList.map(person => ({
							value: person.hod_user_id_fk,
							label: `${person.designation || ''} - ${person.user_name || ''}`.trim()
						}));
					}
				});
				setDepartmentExecutivesMap(deptExecMap);
				console.log("✅ Department executives map built:", deptExecMap);
			}

			// ✅ Build complete form object
			const formValues = {
				// Basic fields - match JSP field names
				project_id_fk: contractData.project_id_fk || contractData.projectidfk || "",
				contract_status: contractData.contract_status || contractData.contractstatus || "",
				hod_user_id_fk: contractData.hod_user_id_fk || contractData.hoduseridfk || "",
				dy_hod_user_id_fk: contractData.dy_hod_user_id_fk || contractData.dyhoduseridfk || "",
				contract_department: contractData.department_fk || contractData.departmentfk || contractData.contract_department || "",
				contract_short_name: contractData.contract_short_name || contractData.contractshortname || "",
				bank_funded: contractData.bank_funded || contractData.bankfunded || "",
				bank_name: contractData.bank_name || contractData.bankname || "",
				type_of_review: contractData.type_of_review || contractData.typeofreview || "",
				contract_name: contractData.contract_name || contractData.contractname || "",
				contract_type_fk: contractData.contract_type_fk || contractData.contracttypefk || "",
				contractor_id_fk: contractData.contractor_id_fk || contractData.contractoridfk || "",
				contract_ifas_code: contractData.contract_ifas_code || contractData.contractifascode || "",
				scope_of_contract: contractData.scope_of_contract || contractData.scopeofcontract || "",
				loa_letter_number: contractData.loa_letter_number || contractData.loaletternumber || "",
				loa_date: contractData.loa_date ? formatDateForInput(contractData.loa_date) : "",
				ca_no: contractData.ca_no || contractData.cano || "",
				ca_date: contractData.ca_date ? formatDateForInput(contractData.ca_date) : "",
				date_of_start: contractData.date_of_start ? formatDateForInput(contractData.date_of_start) : "",
				doc: contractData.doc ? formatDateForInput(contractData.doc) : "",
				target_doc: contractData.target_doc ? formatDateForInput(contractData.target_doc) : "",
				awarded_cost: contractData.awarded_cost || contractData.awardedcost || "",
				awarded_cost_units: contractData.awarded_cost_units || contractData.awardedcostunits || "",
				estimated_cost: contractData.estimated_cost || contractData.estimatedcost || "",
				estimated_cost_units: contractData.estimated_cost_units || contractData.estimatedcostunits || "",
				planned_date_of_award: contractData.planned_date_of_award ? formatDateForInput(contractData.planned_date_of_award) : "",
				planned_date_of_completion: contractData.planned_date_of_completion ? formatDateForInput(contractData.planned_date_of_completion) : "",
				contract_notice_inviting_tender: contractData.contract_notice_inviting_tender ? formatDateForInput(contractData.contract_notice_inviting_tender) : "",
				tender_opening_date: contractData.tender_opening_date ? formatDateForInput(contractData.tender_opening_date) : "",
				technical_eval_submission: contractData.technical_eval_submission ? formatDateForInput(contractData.technical_eval_submission) : "",
				financial_eval_submission: contractData.financial_eval_submission ? formatDateForInput(contractData.financial_eval_submission) : "",
				remarks: contractData.remarks || "",
				contract_status_fk: contractData.contract_status_fk || contractData.contractstatusfk || "",
				bg_required: (contractData.bg_required || contractData.bgrequired || "no").toLowerCase(),
				insurance_required: (contractData.insurance_required || contractData.insurancerequired || "no").toLowerCase(),
				milestone_requried: (contractData.milestone_requried || contractData.milestonerequried || "no").toLowerCase(),
				revision_requried: (contractData.revision_requried || contractData.revisionrequried || "no").toLowerCase(),
				contractors_key_requried: (contractData.contractors_key_requried || contractData.contractorskeyrequried || "no").toLowerCase(),

				// ✅ Use helper functions to build array fields
				executives: buildExecutivesArray(contractData.departmentList || []),
				tenderBidRevisions: buildTenderBidRevisionsArray(contractData.contract_revisions || []),
				bgDetailsList: buildBgDetailsArray(contractData.bankGauranree || contractData.bankGaurantee || []),
				insuranceRequired: buildInsuranceArray(contractData.insurence || contractData.insurance || []),
				milestoneRequired: buildMilestonesArray(contractData.milestones || []),
				revisionRequired: buildRevisionArray(contractData.contract_revision || []),
				contractorsKeyRequried: buildKeyPersonnelArray(contractData.contractKeyPersonnels || []),
				documentsTable: buildDocumentsArray(contractData.contractDocuments || [])
			};

			// ✅ ONE operation instead of 100+ setValue calls
			reset(formValues);

			console.timeEnd('Form Population');
			console.log("✅ Form populated successfully");
			console.log("✅ Executives array:", formValues.executives);
			console.log("✅ Tender Bid Revisions:", formValues.tenderBidRevisions);
			console.log("✅ Documents:", formValues.documentsTable);
		} catch (error) {
			console.error("❌ Error populating form:", error);
		}
	};

	// Fetch dropdown data on component mount
	useEffect(() => {
		const fetchData = async () => {
			try {
				setFormLoading(true);
				let response;

				if (isEdit && contractId) {
					// Create FormData for edit mode
					const formData = new FormData();
					formData.append('contract_id', contractId);

					response = await api.post(`${API_BASE_URL}/contract/get-contractt`, formData, {
						headers: {
							'Content-Type': 'multipart/form-data'
						},
						withCredentials: true
					});

					// Store contract details
					if (response.data.success && response.data.contractDeatils) {
						setContractDetails(response.data.contractDeatils);
						console.log("✅ Contract details loaded:", response.data.contractDeatils);
						console.log("✅ Department List from API:", response.data.contractDeatils.departmentList);
						console.log("✅ Contract Revisions from API:", response.data.contractDeatils.contract_revisions);
						console.log("✅ Documents from API:", response.data.contractDeatils.contractDocuments);

						// Populate the form immediately with the data
						populateFormWithContractData(response.data.contractDeatils);
					}
				} else {
					// For ADD mode: Use /add-contract-form endpoint
					response = await api.post(`${API_BASE_URL}/contract/add-contract-form`, {}, {
						withCredentials: true
					});
				}

				if (response.data.success || response.data.contractDeatils) {
					console.log("API Response keys:", Object.keys(response.data));

					// Transform and set dropdown options
					const projects = response.data.projectsList || [];
					const projectOpts = projects.map(project => ({
						value: project.project_id,
						label: `${project.project_id} - ${project.project_name}`
					}));
					setProjectOptions(projectOpts);

					// HOD: designation and user_name
					const hodList = response.data.hodList || [];
					const hodOpts = hodList.map(hod => ({
						value: hod.user_id,
						label: `${hod.designation} - ${hod.user_name}`
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
					const contractors = response.data.contractors || response.data.contractor || [];
					const contractorOpts = contractors.map(contractor => ({
						value: contractor.contractor_id_fk,
						label: contractor.contractor_name
					}));
					setContractorOptions(contractorOpts);

					// Bank Guarantee Type
					const bgTypes = response.data.bankGuaranteeType || [];
					const bgTypeOpts = bgTypes.map(type => ({
						value: type.bg_type_fk,
						label: type.bg_type_fk
					}));
					setBgTypeOptions(bgTypeOpts);
					console.log("BG OPTIONS STATE:", bgTypeOptions);
					// Insurance Type
					const insuranceTypes = response.data.insurance_type || [];
					const insuranceTypeOpts = insuranceTypes.map(type => ({
						value:  type.insurance_type,
						label: type.insurance_type
					}));
					setInsuranceTypeOptions(insuranceTypeOpts);

					// Contract Status
					const contractStatuses = response.data.contract_Statustype || [];
					const contractStatusOpts = contractStatuses.map(status => ({
						value: status.contract_status_fk,
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

					// Unit Options
					const units = response.data.unitsList || [];
					const unitOpts = units.map(unit => ({
						value: unit.id || unit.value,
						label: unit.unit || unit.label
					}));
					setUnitOptions(unitOpts);

					const fileTypes = response.data.contractFileTypeList || [];
					const fileTypeOpts = fileTypes.map(fileType => ({
						value: fileType.contract_file_type,
						label: fileType.contract_file_type
					}));
					setFileTypeOptions(fileTypeOpts);

					console.log("Data loaded successfully", isEdit ? "(Edit Mode)" : "(Add Mode)");
				} else {
					console.error("Failed to load data:", response.data.message);
				}
			} catch (error) {
				console.error("Error fetching data:", error);
				alert("Failed to load form data. Please try again.");
			} finally {
				setFormLoading(false);
			}
		};

		fetchData();
	}, [isEdit, contractId]);

	const fetchExecutivesByDepartment = async (departmentId) => {
		try {
			setLoadingExecutives(true);

			const response = await api.post(`${API_BASE_URL}/contract/ajax/getExecutivesListForContractForm`, {
				department_fk: departmentId
			});

			if (response.data && Array.isArray(response.data)) {
				const executiveOpts = response.data.map(person => ({
					value: person.hod_user_id_fk,
					label: `${person.designation || ''} - ${person.user_name || ''}`.trim()
				}));

				setExecutiveOptions(prev => ({
					...prev,
					[departmentId]: executiveOpts
				}));

				console.log(`✅ Fetched executives for dept ${departmentId}:`, executiveOpts);
			}
		} catch (error) {
			console.error("Error fetching executives:", error);
		} finally {
			setLoadingExecutives(false);
		}
	};

	const onSubmit = async (data, saveForEdit = false) => {
		if (saveForEdit) setSavingForEdit(true);
		else setLoading(true);


		const hasAnyRevisionField = (r) => {
			return (
				(r?.revision_estimated_cost && String(r.revision_estimated_cost).trim() !== "") ||
				(r?.revision_planned_date_of_award && String(r.revision_planned_date_of_award).trim() !== "") ||
				(r?.revision_planned_date_of_completion && String(r.revision_planned_date_of_completion).trim() !== "") ||
				(r?.notice_inviting_tender && String(r.notice_inviting_tender).trim() !== "") ||
				(r?.tender_bid_opening_date && String(r.tender_bid_opening_date).trim() !== "") ||
				(r?.technical_eval_approval && String(r.technical_eval_approval).trim() !== "") ||
				(r?.financial_eval_approval && String(r.financial_eval_approval).trim() !== "") ||
				(r?.tender_bid_remarks && String(r.tender_bid_remarks).trim() !== "")
			);
		};

		try {
			const toNumberOrNull = (value) => {
			   if (value === "" || value === undefined || value === null) return null;
			   return Number(value);
			};
			
			const formData = new FormData();

			const payload = {
				/* ===============================
				   BASIC FIELDS
				   =============================== */
				project_id_fk: data?.project_id_fk,
				hod_user_id_fk: data?.hod_user_id_fk,
				dy_hod_user_id_fk: data?.dy_hod_user_id_fk ? data?.dy_hod_user_id_fk : null,

				contract_type_fk: data?.contract_type_fk ? data.contract_type_fk : null,
				contractor_id_fk: data?.contractor_id_fk ? data?.contractor_id_fk : null,
				contract_status_fk: data?.contract_status_fk ? data?.contract_status_fk : null,

				contract_ifas_code: data?.contract_ifas_code,
				contract_status: data?.contract_status,
				contract_department: data?.contract_department,

				contract_short_name: data?.contract_short_name,
				contract_name: data?.contract_name,

				bank_funded: data?.bank_funded || "No",
				bank_name: data?.bank_name,
				type_of_review: data?.type_of_review,

				scope_of_contract: data?.scope_of_contract,
				loa_letter_number: data?.loa_letter_number,
				loa_date: data?.loa_date,
				ca_no: data?.ca_no,
				ca_date: data?.ca_date,

				date_of_start: data?.date_of_start,
				doc: data?.doc,

				awarded_cost: toNumberOrNull(data?.awarded_cost),
				awarded_cost_units: toNumberOrNull(data?.awarded_cost_units),

				estimated_cost: toNumberOrNull(data?.estimated_cost),
				estimated_cost_units: toNumberOrNull(data?.estimated_cost_units),

				planned_date_of_award: data?.planned_date_of_award,
				planned_date_of_completion: data?.planned_date_of_completion,

				tender_opening_date: data?.tender_opening_date,
				technical_eval_submission: data?.technical_eval_submission,
				financial_eval_submission: data?.financial_eval_submission,

				remarks: data?.remarks,

				status: "Active",
				is_contract_closure_initiated: "No",

				/* ===============================
				   EXECUTIVES
				   =============================== */
				department_fks: [],
				responsible_people_id_fks: [],
				filecounts: [],

				/* ===============================
				   TENDER BID REVISIONS
				   =============================== */
				revisionno: [],
				revision_estimated_cost: [],
				revision_planned_date_of_award: [],
				revision_planned_date_of_completion: [],
				notice_inviting_tender: [],
				tender_bid_opening_date: [],
				technical_eval_approval: [],
				financial_eval_approval: [],
				tender_bid_remarks: [],

				/* ===============================
				   BANK GUARANTEE
				   =============================== */
				bg_type_fks: [],
				issuing_banks: [],
				bg_numbers: [],
				bg_values: [],
				bg_valid_uptos: [],
				bg_dates: [],
				release_dates: [],
				bg_value_unitss: [],

				/* ===============================
				   INSURANCE
				   =============================== */
				insurance_type_fks: [],
				issuing_agencys: [],
				agency_addresss: [],
				insurance_numbers: [],
				insurance_values: [],
				insurence_valid_uptos: [],
				insuranceStatus: [],
				insurance_value_unitss: [],

				/* ===============================
				   MILESTONES
				   =============================== */
				milestone_ids: [],
				milestone_names: [],
				milestone_dates: [],
				actual_dates: [],
				revisions: [],
				mile_remarks: [],

				/* ===============================
				   CONTRACT REVISION
				   =============================== */
				revision_numbers: [],
				revised_amounts: [],
				revised_docs: [],
				revision_statuss: [],
				revised_amount_unitss: [],
				revision_amounts_statuss: [],
				approval_by_bank: [],

				/* ===============================
				   CONTRACTOR KEY PERSONNEL
				   =============================== */
				contractKeyPersonnelNames: [],
				contractKeyPersonnelDesignations: [],
				contractKeyPersonnelMobileNos: [],
				contractKeyPersonnelEmailIds: [],

				/* ===============================
				  Documents (Matching JSP structure)
				 =============================== */
				contractDocumentNames: [],
				contractDocumentFileNames: [],
				contract_file_types: [],
				contract_file_ids: []
			};

			// Add contract_id for update (if in edit mode)
			if (isEdit && contractId) {
				payload.contract_id = contractId;
				// Only in edit
								(data.bgDetailsList || []).forEach((bg) => {
									if (!bg.bg_type_fks) return;

									payload.bg_type_fks.push(bg.bg_type_fks);
									payload.issuing_banks.push(bg.issuing_banks);
									payload.bg_numbers.push(bg.bg_numbers);
									payload.bg_values.push(bg.bg_values);
									payload.bg_valid_uptos.push(bg.bg_valid_uptos);
									payload.bg_dates.push(bg.bg_dates);
									payload.release_dates.push(bg.release_dates);
									payload.bg_value_unitss.push(bg.bg_unit);
								});

								//Only in edit
								(data.insuranceRequired || []).forEach((ins) => {
									if (!ins.insurance_type_fks) return;

									payload.insurance_type_fks.push(ins.insurance_type_fks);
									payload.issuing_agencys.push(ins.issuing_agencys);
									payload.agency_addresss.push(ins.agency_addresss);
									payload.insurance_numbers.push(ins.insurance_numbers);
									payload.insurance_values.push(ins.insurance_values);
									payload.insurence_valid_uptos.push(ins.insurence_valid_uptos);
									payload.insuranceStatus.push(ins.insuranceStatus ? "Yes" : "No");
									payload.insurance_value_unitss.push(ins.insurance_unit);
								});

								(data.milestoneRequired || []).forEach((mile) => {
									if (!mile.milestone_ids) return;

									payload.milestone_ids.push(mile.milestone_ids);
									payload.milestone_names.push(mile.milestone_names);
									payload.milestone_dates.push(mile.milestone_dates);
									payload.actual_dates.push(mile.actual_dates);
									payload.revisions.push(mile.revisions);
									payload.mile_remarks.push(mile.mile_remarks);
								});

								(data.revisionRequired || []).forEach((rev) => {
									if (!rev.revision_numbers) return;

									payload.revision_numbers.push(rev.revision_numbers);
									payload.revised_amounts.push(rev.revised_amounts);
									payload.revised_docs.push(rev.revised_docs);
									payload.revision_statuss.push(rev.revision_statuss ? "Yes" : "No");
									payload.revised_amount_unitss.push(rev.revision_unit);
									payload.revision_amounts_statuss.push(rev.revision_amounts_statuss ? "Yes" : "No");
									payload.approval_by_bank.push(rev.approvalbybankstatus ? "Yes" : "No");
								});

								(data.contractorsKeyRequried || []).forEach((person) => {
									if (!person.contractKeyPersonnelNames) return;

									payload.contractKeyPersonnelNames.push(person.contractKeyPersonnelNames);
									payload.contractKeyPersonnelDesignations.push(person.contractKeyPersonnelDesignations);
									payload.contractKeyPersonnelMobileNos.push(person.contractKeyPersonnelMobileNos);
									payload.contractKeyPersonnelEmailIds.push(person.contractKeyPersonnelEmailIds);
								});
			}

			(data.executives || []).forEach(row => {
				if (row.department_fks && row.responsible_people_id_fks?.length) {
					payload.department_fks.push(row.department_fks);
					payload.filecounts.push(String(row.responsible_people_id_fks.length));
					payload.responsible_people_id_fks.push(...row.responsible_people_id_fks);
				}
			});

			(data.tenderBidRevisions || []).forEach((r) => {
				if (!hasAnyRevisionField(r)) return;

				payload.revisionno.push(r.revisionno);
				payload.revision_estimated_cost.push(r.revision_estimated_cost);
				payload.revision_planned_date_of_award.push(r.revision_planned_date_of_award);
				payload.revision_planned_date_of_completion.push(r.revision_planned_date_of_completion);
				payload.notice_inviting_tender.push(r.notice_inviting_tender);
				payload.tender_bid_opening_date.push(r.tender_bid_opening_date);
				payload.technical_eval_approval.push(r.technical_eval_approval);
				payload.financial_eval_approval.push(r.financial_eval_approval);
				payload.tender_bid_remarks.push(r.tender_bid_remarks);
			});


			// Both in Add and Update

			// Handle documents - matching JSP structure
			if (data.documentsTable && data.documentsTable.length > 0) {
				data.documentsTable.forEach((doc, index) => {
					if (doc.contract_file_types && doc.contractDocumentNames) {
						payload.contractDocumentNames.push(doc.contractDocumentNames);
						payload.contract_file_types.push(doc.contract_file_types);

						// Check if this is an existing document
						const existingFileId = contractDetails?.contractDocuments?.[index]?.contract_file_id;
						const existingFileName = contractDetails?.contractDocuments?.[index]?.attachment;

						if (existingFileId) {
							payload.contract_file_ids.push(existingFileId);
						} else {
							payload.contract_file_ids.push("");
						}

						const fileInput = doc.contractDocumentFiles;

						if (fileInput && fileInput.length > 0 && fileInput[0] instanceof File) {
							// New file uploaded
							const file = fileInput[0];
							payload.contractDocumentFileNames.push(file.name);
							formData.append("contractDocumentFiles[]", file);
						} else if (fileInput && fileInput instanceof File) {
							// Single file uploaded
							payload.contractDocumentFileNames.push(fileInput.name);
							formData.append("contractDocumentFiles[]", fileInput);
						} else if (existingFileName) {
							// Keep existing file name if no new file uploaded
							payload.contractDocumentFileNames.push(existingFileName);
						} else {
							// No file
							payload.contractDocumentFileNames.push("");
						}
					}
				});
			}

			formData.append("payload", new Blob([JSON.stringify(payload)], {
				type: "application/json"
			}));

			/* ===============================
			   SUBMIT
			   =============================== */
			const url = isEdit
				? `${API_BASE_URL}/contract/update-contract`
				: `${API_BASE_URL}/contract/add-contract`;

			const response = await api.post(url, formData, {
				headers: {
					"Content-Type": "multipart/form-data"
				},
				withCredentials: true
			});

			if (response?.data?.success) {
				if (saveForEdit) {
					alert("Contract saved successfully! You can continue editing.");
				} else {
					alert(isEdit ? "Contract updated successfully!" : "Contract added successfully!");
					if (!saveForEdit) {
						navigate(-1);
					}
				}
			} else {
				throw new Error(response?.data?.message || "Failed to save contract");
			}
		} catch (error) {
			console.error("Submit error:", error);
			alert(error?.response?.data?.message || error.message || "Failed to save contract. Please try again.");
		} finally {
			if (saveForEdit) setSavingForEdit(false);
			else setLoading(false);
		}
	};

	const handleCancel = () => {
		if (window.confirm("Are you sure you want to cancel? Any unsaved changes will be lost.")) {
			navigate(-1);
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

	// Updated tabs array to conditionally show tabs based on edit mode
	const tabs = [
		{ id: "managers", label: "Contract Managers" },
		{ id: "executives", label: "Executives" },
		{ id: "details", label: "Contract Details" },
		{ id: "closure", label: "Contract Closure" },
		...(isEdit ? [
			{ id: "bank", label: "Bank Guarantee" },
			{ id: "insurance", label: "Insurance" },
			{ id: "milestone", label: "Milestone" },
			{ id: "personnel", label: "Contractor's Key Personnel" },
		] : []),
		{ id: "documents", label: "Documents" },
	];

	const scrollToSection = (id) => {
		setActiveTab(id);
		sectionRefs[id].current.scrollIntoView({ behavior: "smooth", block: "start" });
	};

	useEffect(() => {
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
	}, [formLoading]);

	// Debug effect to check form data
	useEffect(() => {
		if (isEdit && !formLoading) {
			console.log("Debug - Executives form data:", watch("executives"));
			console.log("Debug - Tender Bid Revisions:", watch("tenderBidRevisions"));
			console.log("Debug - Documents:", watch("documentsTable"));
			console.log("Debug - Department executives map:", departmentExecutivesMap);
		}
	}, [isEdit, formLoading, watch, departmentExecutivesMap]);

	if (formLoading) {
		return (
			<div className="d-flex justify-content-center align-items-center" style={{ height: "400px" }}>
				<div className="spinner-border text-primary" role="status">
					<span className="visually-hidden">
						{isEdit ? "Loading contract data..." : "Loading form..."}
					</span>
				</div>
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
							{isEdit && contractId && ` (ID: ${contractId})`}
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

								{/* Contract Awarded field - Only shown in ADD mode, not in EDIT mode */}
								{!isEdit && (
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
								)}

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
											executiveFields.map((item, index) => {
												const selectedDept = watch(`executives.${index}.department_fks`);
												const selectedExecIds = watch(`executives.${index}.responsible_people_id_fks`) || [];

												// Get executives for this department - check multiple sources
												let deptExecutives = [];

												// First, check departmentExecutivesMap (from API response)
												if (departmentExecutivesMap[selectedDept]) {
													deptExecutives = departmentExecutivesMap[selectedDept];
												}
												// Then check executiveOptions (from manual fetch)
												else if (executiveOptions[selectedDept]) {
													deptExecutives = executiveOptions[selectedDept];
												}

												console.log(`Row ${index}: Dept=${selectedDept}, SelectedIds=${selectedExecIds}, Executives=${deptExecutives.length}`);

												return (
													<tr key={item.id}>
														{/* ================= Department ================= */}
														<td>
															<Controller
																name={`executives.${index}.department_fks`}
																control={control}
																rules={{ required: "Department is required" }}
																render={({ field }) => (
																	<Select
																		classNamePrefix="react-select"
																		options={departmentOptions}
																		placeholder="Select Department"
																		isSearchable
																		isClearable
																		value={departmentOptions.find(opt => opt.value === field.value) || null}
																		onChange={(opt) => {
																			const deptVal = opt?.value || "";
																			field.onChange(deptVal);
																			setValue(`executives.${index}.responsible_people_id_fks`, []);
																			if (deptVal) fetchExecutivesByDepartment(deptVal);
																		}}
																	/>
																)}
															/>

															{errors.executives?.[index]?.department_fks && (
																<span className="red">
																	{errors.executives[index].department_fks.message}
																</span>
															)}
														</td>

														{/* ================= Executives Multi ================= */}
														<td>
															<Controller
																name={`executives.${index}.responsible_people_id_fks`}
																control={control}
																rules={{ required: "Please select executives" }}
																render={({ field }) => (
																	<Select
																		classNamePrefix="react-select"
																		options={deptExecutives}
																		isMulti
																		placeholder={!selectedDept ? "Select Department first" :
																			deptExecutives.length === 0 ? "Loading executives..." : "Select Executives"}
																		isDisabled={!selectedDept || deptExecutives.length === 0}

																		// Set selected values - matching JSP logic
																		value={deptExecutives.filter(opt =>
																			Array.isArray(field.value) ? field.value.includes(opt.value) : false
																		)}

																		onChange={(selected) => {
																			const selectedValues = selected ? selected.map(opt => opt.value) : [];
																			field.onChange(selectedValues);
																		}}
																	/>
																)}
															/>

															{errors.executives?.[index]?.responsible_people_id_fks && (
																<span className="red">
																	{errors.executives[index].responsible_people_id_fks.message}
																</span>
															)}
														</td>

														{/* ================= Delete ================= */}
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
												);
											})
										) : (
											<tr>
												<td colSpan="3" className="text-center text-muted">
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
										appendExecutive({ department_fks: "", responsible_people_id_fks: [] })
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
										<p className="red">{errors.contract_short_name.message}</p>
									)}
								</div>
							</div>

							<div className="form-row">
								<div className="form-field">
									<label>Contract Name<span className="red">*</span></label>
									<textarea
										{...register("contract_name", { required: "Contract name is required" })}
										maxLength={1000}
										rows="3"
										placeholder="Enter contract name"
									></textarea>
									<div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
										{watch("contract_name")?.length || 0}/1000
										{errors.contract_name && <span className="red">{errors.contract_name.message}</span>}
									</div>
								</div>
							</div>

							{/* OPTION 3: Always show in edit mode, conditionally in add mode */}
							{(isEdit || contractStatus === "yes") && (
								<>
									<div className="form-row">
										<div className="form-field">
											<label>Contract Type {isEdit && <span className="red">*</span>}</label>
											<Controller
												name="contract_type_fk"
												control={control}
												rules={isEdit ? { required: "Contract Type is required" } : {}}
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

										{/* Contractor Name Field */}
										<div className="form-field">
											<label>Contractor Name {isEdit && <span className="red">*</span>}</label>
											<Controller
												name="contractor_id_fk"
												control={control}
												rules={isEdit ? { required: "Contractor Name is required" } : {}}
												render={({ field }) => (
													<Select
														{...field}
														classNamePrefix="react-select"
														options={contractorOptions}
														placeholder="Select Contractor"
														isSearchable
														isClearable
														value={contractorOptions.find(opt => opt.value === field.value) || null}
														onChange={(opt) => field.onChange(opt?.value || "")}
													/>
												)}
											/>
											{errors.contractor_id_fk && (
												<p className="error-text">{errors.contractor_id_fk.message}</p>
											)}
										</div>

										{/* Contract (IFAS Code) Field */}
										<div className="form-field">
											<label>Contract (IFAS Code)</label>
											<input
												type="text"
												{...register("contract_ifas_code", {
													maxLength: {
														value: 50,
														message: "IFAS Code cannot exceed 50 characters"
													}
												})}
												placeholder="Enter IFAS Code"
											/>
											<div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
												{watch("contract_ifas_code")?.length || 0}/50
											</div>
											{errors.contract_ifas_code && (
												<p className="error-text">{errors.contract_ifas_code.message}</p>
											)}
										</div>
									</div>

									<div className="form-row">
										<div className="form-field">
											<label>Scope of Contract</label>
											<textarea
												{...register("scope_of_contract")}
												maxLength={1000}
												rows="3"
												placeholder="Enter scope of contract"
											></textarea>
											<div style={{ fontSize: "12px", color: "#555", textAlign: "right" }}>
												{watch("scope_of_contract")?.length || 0}/1000
											</div>
										</div>
									</div>

									<div className="form-row">
										<div className="form-field">
											<label>LOA Letter No {isEdit && <span className="red">*</span>} </label>
											<input
												{...register("loa_letter_number", isEdit ? { required: "LOA Letter No is required" } : {})}
												type="text"
												placeholder="Enter LOA Letter No"
											/>
											{errors.loa_letter_number && (
												<p className="error-text">{errors.loa_letter_number.message}</p>
											)}
										</div>
										<div className="form-field">
											<label>LOA Date {isEdit && <span className="red">*</span>} </label>
											<input
												{...register("loa_date", isEdit ? { required: "LOA Date is required" } : {})}
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
								{isEdit && (
									<>
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
											<div className="d-flex align-items-center gap-2">
												<input
													{...register("awarded_cost", { required: "Awarded cost is required" })}
													type="number"
													placeholder="Enter Value"
													className="flex-grow-1"
													style={{ minWidth: "300px" }}
												/>
												<Controller
													name="awarded_cost_units"
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
											{errors.awarded_cost && (
												<p className="error-text">{errors.awarded_cost.message}</p>
											)}
										</div>

										<div className="form-field">
											<label>Target DOC </label>
											<input {...register("target_doc")} type="date" />
										</div>
									</>)}

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

						{/* Tender Bid Revisions Section */}
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

						{/* Bank Guarantee Section (Edit mode only) */}
						{isEdit && (
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
													appendBgDetailsList({ bg_type_fks: "", issuing_banks: "", bg_numbers: "", bg_values: "", bg_unit: "", bg_dates: "", bg_valid_uptos: "", release_dates: "" })
												}
											>
												<BiListPlus size="24" />
											</button>
										</div>
									</>
								)}
							</div>
						)}

						{/* Insurance Section (Edit mode only) */}
						{isEdit && (
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
							</div>
						)}

						{/* Milestone Section (Edit mode only) */}
						{isEdit && (
							<div ref={sectionRefs.milestone} className={styles.formSection}>
								<h6 className="d-flex justify-content-center mt-1 mb-2">Milestone Details</h6>

								{/* milestone */}
								<div className="d-flex gap-30 align-center justify-content-center">
									<label>Milestone Required ?</label>
									<div className="d-flex gap-20" style={{ padding: "10px" }}>
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
									<div className="d-flex gap-20" style={{ padding: "10px" }}>
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
																	<input {...register(`revisionRequired.${index}.revision_statuss`)} type="checkbox" />
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
						)}

						{/* Contractor's Key Personnel Section (Edit mode only) */}
						{isEdit && (
							<div ref={sectionRefs.personnel} className={styles.formSection}>
								<h6 className="d-flex justify-content-center mt-1 mb-2">Contractor's Key Personnel</h6>

								<div className="d-flex gap-30 align-center justify-content-center">
									<label>Contractor's Key Required ?</label>
									<div className="d-flex gap-20" style={{ padding: "10px" }}>
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
						)}

						{/* Documents section - Always shown */}
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
											documentsTableFields.map((item, index) => {
												// Check if this is an existing document (matching JSP logic)
												const existingDoc = contractDetails?.contractDocuments?.[index];
												const existingFileName = existingDoc?.attachment;
												const fileId = existingDoc?.contract_file_id;

												return (
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
															{/* Show existing file download link if available (matching JSP) */}
															{existingFileName && (
																<div className="mb-2">
																	<div className="d-flex align-items-center">
																		<a
																			href={`${API_BASE_URL}/contract-files/${existingFileName}`}
																			className="btn btn-sm btn-outline-primary me-2"
																			download
																			target="_blank"
																			rel="noopener noreferrer"
																		>
																			<i className="fa fa-download me-1"></i>
																			Download
																		</a>
																		<span className="text-muted small">{existingFileName}</span>
																	</div>
																	<input
																		type="hidden"
																		name={`existingFileName_${index}`}
																		value={existingFileName}
																	/>
																	{fileId && (
																		<input
																			type="hidden"
																			name={`fileId_${index}`}
																			value={fileId}
																		/>
																	)}
																</div>
															)}

															{/* File upload for new/replacement files */}
															<div className={styles["file-upload-wrapper"]}>
																<label htmlFor={`file-${index}`} className={styles["file-upload-label-icon"]}>
																	<RiAttachment2 size={20} style={{ marginRight: "6px" }} />
																	{existingFileName ? "Replace File" : "Upload File"}
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
												);
											})
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

							{/* Only show SAVE & EDIT button in ADD mode, not in EDIT mode */}
							{!isEdit && (
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
							)}

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