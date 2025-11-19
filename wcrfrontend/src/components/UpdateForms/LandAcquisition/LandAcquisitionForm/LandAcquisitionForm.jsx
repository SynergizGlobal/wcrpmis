import React, { useEffect, useState } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import { Outlet } from 'react-router-dom';
import styles from './LandAcquisitionForm.module.css';
import { NavLink } from "react-router-dom";

import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';
import { RiAttachment2 } from 'react-icons/ri';

export default function LandAcquisitionForm() {

    const navigate = useNavigate();
    const { state } = useLocation(); // passed when editing
    const row = state?.data ?? null;
    const laId = state?.la_id ?? row?.la_id ?? null;
    const isEdit = Boolean((row && Object.keys(row).length > 0) || laId);

    const [projectOptions, setProjectOptions] = useState([]);
    const [statusOptions, setStatusOptions] = useState([]);
    const [typeOptions, setTypeOptions] = useState([]);
    const [allSubCategories, setAllSubCategories] = useState([]);
    const [subCategoryOptions, setSubCategoryOptions] = useState([]);
    const [subsReady, setSubsReady] = useState(false);
    const [fileTypeOptions, setfileTypeOptions] = useState([]);
    const [loading, setLoading] = useState(false);

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
          project_id_fk: null,
          la_land_status_fk: null,
          type_of_land: null,
          sub_category_of_land: null,
          survey_number: "",
          special_feature: "",
          area_acquired: "",
          village: "",
          taluka: "",
          dy_slr: "",
          sdo: "",
          collector: "",
          area_to_be_acquired: "",
          area_of_plot: "",
          latitude: "",
          longitude: "",
          proposal_submission_date_to_collector: "",
          jm_fee_amount: "",
          chainage_from: "",
          chainage_to: "",
          jm_fee_letter_received_date: "",
          jm_fee_paid_date: "",
          jm_start_date: "",
          jm_completion_date: "",
          jm_sheet_date_to_sdo: "",
          jm_approval: "",
          jm_remarks: "",
          private_land_process: "",
          

          // private(indian railway act)

          private_ira_collector: "",
          submission_of_proposal_to_GM: "",
          approval_of_GM: "",
          draft_letter_to_con_for_approval_rp: "",
          date_of_approval_of_construction_rp: "",
          date_of_uploading_of_gazette_notification_rp: "",
          publication_in_gazette_rp: "",
          date_of_proposal_to_DC_for_nomination: "",
          date_of_nomination_of_competenta_authority: "",
          draft_letter_to_con_for_approval_ca: "",
          date_of_approval_of_construction_ca: "",
          date_of_uploading_of_gazette_notification_ca: "",
          publication_in_gazette_ca: "",
          date_of_submission_of_draft_notification_to_CALA: "",
          approval_of_CALA_20a: "",
          draft_letter_to_con_for_approval_20a: "",
          date_of_approval_of_construction_20a: "",
          date_of_uploading_of_gazette_notification_20a: "",
          publication_in_gazette_20a: "",
          publication_in_2_local_news_papers_20a: "",
          pasting_of_notification_in_villages_20a: "",
          receipt_of_grievances: "",
          disposal_of_grievances: "",
          date_of_submission_of_draft_notification_to_CALA_20e: "",
          approval_of_CALA_20e: "",
          draft_letter_to_con_for_approval_20e: "",
          date_of_approval_of_construction_20e: "",
          date_of_uploading_of_gazette_notification_20e: "",
          publication_in_gazette_20e: "",
          publication_of_notice_in_2_local_news_papers_20e: "",
          date_of_submission_of_draft_notification_to_CALA_20f: "",
          approval_of_CALA_20f: "",
          draft_letter_to_con_for_approval_20f: "",
          date_of_approval_of_construction_20f: "",
          date_of_uploading_of_gazette_notification_20f: "",
          publication_in_gazette_20f: "",
          publication_of_notice_in_2_local_news_papers_20f: "",

          // Private - IRA & DPM

          name_of_the_owner: "",
          basic_rate: "",
          agriculture_tree_nos: "",
          agriculture_tree_rate: "",
          forest_tree_nos: "",
          forest_tree_rate: "",
          consent_from_owner: "",
          legal_search_report: "",
          date_of_registration: "",
          date_of_possession: "",
          forest_tree_survey: "",
          forest_tree_valuation: "",
          horticulture_tree_survey: "",
          horticulture_tree_valuation: "",
          structure_survey: "",
          structure_valuation: "",
          borewell_survey: "",
          borewell_valuation: "",
          date_of_rfp_to_adtp: "",
          date_of_rate_fixation_of_land: "",
          sdo_demand_for_payment: "",
          date_of_approval_for_payment: "",
          payment_amount: "",
          private_payment_date: "",
          hundred_percent_Solatium: "",
          extra_25_percent: "",
          land_compensation: "",
          agriculture_tree_compensation: "",
          total_rate_divide_m2: "",
          forest_tree_compensation: "",
          structure_compensation: "",
          borewell_compensation: "",
          total_compensation: "",

          attachments: [{ la_file_typess: "", laDocumentNames: "", laFiles: "" }],

          remarks: "",
          issues: "",
        },
      });

       const jmApproval = watch("jm_approval");
       const privateLandProcess = watch("private_land_process")

      const formatOption = (value, label = value) => ({ value, label });

        const { fields: attachmentsFields, append: appendAttachments, remove: removeAttachments } = useFieldArray({
          control,
          name: "attachments",
        });

      

      // useEffect(() => {
      //     if (isEdit && state?.data) {
      //       Object.entries(state.data).forEach(([key, value]) => setValue(key, value));
      //     }
      //   }, [state, setValue, isEdit]);

    useEffect(() => {
  const loadDropdowns = async () => {
    try {
      const [projectsRes, statusRes, typesRes, subCatsRes, fileTypesRes] = await Promise.all([
        api.post(`${API_BASE_URL}/land-acquisition/form/ajax/getProjectsList`, {}, { withCredentials: true }),
        api.get(`${API_BASE_URL}/land-acquisition/form/ajax/getLaLandStatus`, { withCredentials: true }),
        api.post(`${API_BASE_URL}/land-acquisition/form/ajax/getLandsListForLAForm`, {}, { withCredentials: true }),
        api.post(
            `${API_BASE_URL}/land-acquisition/form/ajax/getSubCategorysListForLAForm`,
            { type_of_land: selectedType?.value || "" },
            { withCredentials: true }
          ),
        api.get(`${API_BASE_URL}/land-acquisition/form/ajax/getLaFileType`, { withCredentials: true }),
      ]);

      const formatOption = (value, label = value) => ({ value, label });
      const projects = (projectsRes.data || []).map(p => ({
        value: p.project_id,
        label: `${p.project_id} - ${p.project_name || p.work_code || ""}`.trim(),
      }));
      const statuses = (statusRes.data || []).map(s => formatOption(s.la_land_status));
      const types = (typesRes.data || []).map(t => formatOption(t.type_of_land));
      const subCatsRaw = Array.isArray(subCatsRes.data) ? subCatsRes.data : [];
      const safeSubCats = subCatsRaw.filter(sc =>
        sc &&
        typeof sc === "object" &&
        Object.keys(sc).length > 0 &&
        "sub_category_of_land" in sc &&
        "type_of_land" in sc &&
        sc.sub_category_of_land !== null &&
        sc.type_of_land !== null
        );
        setAllSubCategories(safeSubCats);

      console.log("🧩 Safe SubCategories:", safeSubCats);


      const fileTypes = (Array.isArray(fileTypesRes.data) ? fileTypesRes.data : fileTypesRes.data?.data || [])
        .filter(ft => ft.la_file_type)
        .map(ft => formatOption(ft.la_file_type));

      setProjectOptions(projects);
      setStatusOptions(statuses);
      setTypeOptions(types);
      setAllSubCategories(safeSubCats);
      setfileTypeOptions(fileTypes.length ? fileTypes : [{ value: "Land Document", label: "Land Document" }]);

      
      if (isEdit && row) {
        console.log("🧠 Raw edit row data:", row);

        setValue("project_id_fk", projects.find(p => p.value === dataObject.project_id_fk) || null);

      const isArrayOfPairs = Array.isArray(row) && row.every((item) => Array.isArray(item) && item.length === 2);
      const dataObject = isArrayOfPairs ? Object.fromEntries(row) : row;

      console.log("📦 Converted row object:", dataObject);

        // Map backend keys to form fields
        const mapping = {
          survey_number: row.survey_number || row.la_survey_number,
          special_feature: row.special_feature || row.la_special_feature,
          area_acquired: row.area_acquired || row.la_area_acquired,
          village: row.village || row.la_village,
          taluka: row.taluka || row.la_taluka,
          dy_slr: row.dy_slr || row.la_dy_slr,
          sdo: row.sdo || row.la_sdo,
          collector: row.collector || row.la_collector,
          total_area_to_be_acquired: row.total_area_to_be_acquired || row.la_total_area_to_be_acquired,
          area_of_plot: row.area_of_plot || row.la_area_of_plot,
          latitude: row.latitude || row.la_latitude,
          longitude: row.longitude || row.la_longitude,
          proposal_submission_date_to_collector: row.proposal_submission_date_to_collector || row.la_proposal_submission_date_to_collector,
          jm_fee_amount: row.jm_fee_amount || row.la_jm_fee_amount,
          chainage_from: row.chainage_from || row.la_chainage_from,
          chainage_to: row.chainage_to || row.la_chainage_to,
          jm_fee_letter_received_date: row.jm_fee_letter_received_date || row.la_jm_fee_letter_received_date,
          jm_fee_paid_date: row.jm_fee_paid_date || row.la_jm_fee_paid_date,
          jm_start_date: row.jm_start_date || row.la_jm_start_date,
          jm_completion_date: row.jm_completion_date || row.la_jm_completion_date,
          jm_sheet_date_to_sdo: row.jm_sheet_date_to_sdo || row.la_jm_sheet_date_to_sdo,
          jm_remarks: row.jm_remarks || row.la_jm_remarks,
        };

        // ✅ Fill all fields automatically
        Object.entries(dataObject).forEach(([key, value]) => {
          if (value !== undefined && value !== null && value !== "") {
            setValue(key, value);
          }
        });

        // ✅ Handle dropdowns properly
        const proj = projectOptions.find((p) => p.value === dataObject.project_id_fk);
        if (proj) setValue("project_id_fk", proj);

        const status = statusOptions.find((s) => s.value === dataObject.la_land_status_fk);
        if (status) setValue("la_land_status_fk", status);

        const type = typeOptions.find((t) => t.value === dataObject.type_of_land);
        if (type) setValue("type_of_land", type);

       const subFiltered = Array.isArray(allSubCategories)
        ? allSubCategories.filter(
            (sc) =>
              sc &&
              typeof sc === "object" &&
              sc.sub_category_of_land &&
              sc.type_of_land &&
              sc.type_of_land === (dataObject.type_of_land || dataObject.la_type_of_land)
          )
        : [];

        const subOptions = subFiltered.map((sc) => ({
          value: sc.id,
          label: sc.sub_category_of_land,
        }));
        setSubCategoryOptions(subOptions);

        const sub = subOptions.find((sc) =>
          [dataObject.sub_category_of_land, dataObject.la_sub_category_of_land].includes(sc.value)
        );
        if (sub) setValue("sub_category_of_land", sub);

        console.log("✅ Form values after prefill:", getValues());
      }
    } catch (err) {
      console.error("Dropdown loading failed:", err);
    }
  };

  loadDropdowns();
}, [isEdit, row, setValue]);

useEffect(() => {
  const fetchLandAcquisitionById = async () => {
    try {
      const laIdLocal = laId;
      if (!laIdLocal) return;

      // 1️⃣ Fetch record details first
      const res = await api.post(
        `${API_BASE_URL}/land-acquisition/form/ajax/getLandAcquisitionForm`,
        { la_id: laIdLocal },
        { withCredentials: true }
      );

      const record = res.data;
      
      if (Array.isArray(record.laFilesList) && record.laFilesList.length > 0) {
        removeAttachments();

        // Map backend data
        const mappedAttachments = record.laFilesList.map((f) => ({
          la_file_typess: { value: f.la_file_type_fk, label: f.la_file_type_fk },
          laDocumentNames: f.name || "",
          laDocumentFileNames: f.attachment || "",
          laFiles: "", 
        }));

        mappedAttachments.forEach((att) => appendAttachments(att));
      }


      if (!record) {
        console.warn("⚠️ No record returned for la_id:", laIdLocal);
        return;
      }

      // 2️⃣ If subcategories not yet loaded, load them for this type
      if (Array.isArray(allSubCategories) && allSubCategories.length === 0 && record.type_of_land) {
        const subRes = await api.post(
          `${API_BASE_URL}/land-acquisition/form/ajax/getSubCategorysListForLAForm`,
          { type_of_land: record.type_of_land },
          { withCredentials: true }
        );
        setAllSubCategories(subRes.data || []);
      }

      // 3️⃣ Set form field values
      Object.entries(record).forEach(([key, value]) => {
        if (value !== null && value !== undefined && value !== "") {
          setValue(key, value);
        }
      });

      // 4️⃣ Map dropdowns
      if (Array.isArray(projectOptions) && projectOptions.length) {
        const proj = projectOptions.find(p => p.value === record.project_id_fk);
        if (proj) setValue("project_id_fk", proj);
      }

      if (Array.isArray(statusOptions) && statusOptions.length) {
        const status = statusOptions.find(s => s.value === record.la_land_status_fk);
        if (status) setValue("la_land_status_fk", status);
      }

      if (Array.isArray(typeOptions) && typeOptions.length) {
        const type = typeOptions.find(t => t.value === record.type_of_land);
        if (type) setValue("type_of_land", type);
      }

      // 5️⃣ Populate subcategories properly with ID
      if (Array.isArray(allSubCategories) && record.type_of_land) {
        let filteredSubs = allSubCategories.filter(
          sc => sc && sc.type_of_land === record.type_of_land
        );

        // 🔄 Load dynamically if empty
        if (filteredSubs.length === 0) {
          const subRes = await api.post(
            `${API_BASE_URL}/land-acquisition/form/ajax/getSubCategorysListForLAForm`,
            { type_of_land: record.type_of_land },
            { withCredentials: true }
          );
          filteredSubs = subRes.data || [];
          setAllSubCategories(filteredSubs);
        }

        const subOptions = filteredSubs.map(sc => ({
          value: sc.id, // ✅ pass table row ID
          label: sc.sub_category_of_land,
        }));

        setSubCategoryOptions(subOptions);

        // ✅ find by ID first (matches backend FK)
        const selectedSub =
          subOptions.find(opt => opt.value === record.la_sub_category_fk) ||
          subOptions.find(opt => opt.label === record.sub_category_of_land);

        if (selectedSub) {
          setValue("sub_category_of_land", selectedSub);
          setSubsReady(true);
        } else if (record.sub_category_of_land) {
          setValue("sub_category_of_land", {
            value: record.la_sub_category_fk || record.sub_category_of_land,
            label: record.sub_category_of_land,
          });
        }
      }


      console.log("✅ Record loaded successfully:", record);
    } catch (err) {
      console.error("❌ Failed to fetch land acquisition record:", err);
    }
  };
  if (getValues("attachments")?.length > 1 && isEdit) {
    console.log("⚠️ Attachments already loaded, skipping duplicate append.");
    return;
  }
  // ✅ Execute once dropdowns are ready
  if (isEdit && laId && projectOptions.length > 0 && typeOptions.length > 0) {
    fetchLandAcquisitionById();
  }
}, [isEdit, laId, projectOptions.length, typeOptions.length]);

   const filterSubCategories = (rawList, typeValue) => {
      if (!rawList || !Array.isArray(rawList) || rawList.length === 0) return [];

      try {
        return rawList
          .filter((sc) => {
            // skip null / malformed items
            if (!sc || typeof sc !== "object") return false;

            // check required keys
            if (!("type_of_land" in sc) && !("sub_category_of_land" in sc)) return false;

            // filter by type if selected
            if (typeValue && sc.type_of_land && sc.type_of_land !== typeValue) return false;

            return true;
          })
          .map((sc) => ({
            value: sc.id ?? sc.value ?? "",
            label: sc.sub_category_of_land ?? sc.label ?? "",
          }));
      } catch (err) {
        console.error("⚠️ filterSubCategories failed:", err, rawList);
        return [];
      }
    };



  const selectedType = watch("type_of_land");
  useEffect(() => {
  // Only fetch subcategories when user selects a type in ADD mode
  if (!isEdit && selectedType?.value) {
    const fetchSubCategories = async () => {
      try {
        const res = await api.post(
          `${API_BASE_URL}/land-acquisition/form/ajax/getSubCategorysListForLAForm`,
          { type_of_land: selectedType.value },
          { withCredentials: true }
        );

        if (Array.isArray(res.data) && res.data.length > 0) {
          const formatted = res.data
            .filter(sc => sc && sc.id && sc.sub_category_of_land)
            .map(sc => ({
              value: sc.id,
              label: sc.sub_category_of_land,
            }));

          setAllSubCategories(res.data);
          setSubCategoryOptions(formatted);
          console.log("✅ Loaded subcategories for type:", selectedType.value, formatted);
        } else {
          setSubCategoryOptions([]);
          console.warn("⚠️ No subcategories found for type:", selectedType.value);
        }
      } catch (err) {
        console.error("❌ Failed to load subcategories for selected type:", err);
      }
    };

    fetchSubCategories();
  }
}, [selectedType, isEdit]);

useEffect(() => {
  if (!Array.isArray(allSubCategories) || allSubCategories.some(sc => sc === null)) {
    console.warn("⚠️ Invalid subcategory data:", allSubCategories);
    setSubCategoryOptions([]);
    return;
  }

  const typeValue = selectedType?.value || selectedType || "";
  const filtered = Array.isArray(allSubCategories)
    ? filterSubCategories(allSubCategories, typeValue)
    : [];

  setSubCategoryOptions(filtered);

  const currentSub = watch("sub_category_of_land");
    if (!subsReady) {
    setSubCategoryOptions(filtered);
    setSubsReady(true);
  } else {
    // Only update options; don't clear current selection if still valid
    setSubCategoryOptions(filtered);
    const currentSub = watch("sub_category_of_land");
    if (
      currentSub &&
      !filtered.find((f) => f.value === (currentSub?.value || currentSub))
    ) {
      console.log("⚠️ Keeping existing subcategory:", currentSub);
    }
  }

}, [selectedType, allSubCategories]);


useEffect(() => {
  console.log("🐛 allSubCategories data from API:", allSubCategories);
}, [allSubCategories]);


const onSubmit = async (data) => {
  try {
    setLoading(true);

    const sanitizeDate = (val) => (val ? val : null);

    const sanitizeNumber = (val) => {
      if (val === null || val === undefined || val === "") return null;
      const num = Number(val);
      return isNaN(num) ? null : num;
    };

    const payload = {
        ...data,
        id: data.sub_category_of_land?.value, 
        la_id: row?.la_id || data.la_id, 
        project_id_fk: data.project_id_fk?.value || data.project_id_fk,
        la_land_status_fk: data.la_land_status_fk?.value || data.la_land_status_fk,
        type_of_land: data.type_of_land?.value || data.type_of_land,
        la_sub_category_fk: data.la_sub_category_fk, 
        sub_category_of_land:
          data.sub_category_of_land?.label || data.sub_category_of_land?.value || data.sub_category_of_land,

        // numeric/date cleaning
        area_acquired: sanitizeNumber(data.area_acquired),
        area_to_be_acquired: sanitizeNumber(data.area_to_be_acquired),
        area_of_plot: sanitizeNumber(data.area_of_plot),
        jm_fee_amount: sanitizeNumber(data.jm_fee_amount),
        chainage_from: sanitizeNumber(data.chainage_from),
        chainage_to: sanitizeNumber(data.chainage_to),
        latitude: sanitizeNumber(data.latitude),
        longitude: sanitizeNumber(data.longitude),

        proposal_submission_date_to_collector: sanitizeDate(data.proposal_submission_date_to_collector),
        jm_fee_letter_received_date: sanitizeDate(data.jm_fee_letter_received_date),
        jm_fee_paid_date: sanitizeDate(data.jm_fee_paid_date),
        jm_start_date: sanitizeDate(data.jm_start_date),
        jm_completion_date: sanitizeDate(data.jm_completion_date),
        jm_sheet_date_to_sdo: sanitizeDate(data.jm_sheet_date_to_sdo),
      };

      const formData = new FormData();

    // append simple fields
    Object.entries(payload).forEach(([key, val]) => {
      if (val !== undefined && val !== null && val !== "") {
        formData.append(key, val);
      }
    });

    // ✅ append attachments like old JSP names
    if (Array.isArray(data.attachments)) {
      data.attachments.forEach((att, index) => {
        const file = att.laFiles?.[0] || null;
        const fileType = att.la_file_typess?.value || att.la_file_typess || "";
        const docName = att.laDocumentNames || "";
        const existingFileName = att.laDocumentFileNames || "";

        if (file) {
          // new file selected
          formData.append("laFiles", file);
        } else {
          // no new file — preserve old one
          formData.append("laFiles", new File([], ""));
        }

        formData.append("la_file_typess", fileType);
        formData.append("laDocumentNames", docName);
        formData.append("laDocumentFileNames", existingFileName);
      });
    }

    console.log("📦 Final FormData being sent:");
    for (let [key, val] of formData.entries()) {
      if (key === "laFilesList") formData.delete(key);
      console.log(`➡️ ${key}:`, val);
    }

    const endpoint = isEdit
      ? `${API_BASE_URL}/land-acquisition/update-land-acquisition`
      : `${API_BASE_URL}/land-acquisition/add-land-acquisition`;

    const res = await api.post(endpoint, formData, {
      withCredentials: true,
      headers: { "Content-Type": "multipart/form-data" },
    });

    console.log("📥 Response:", res.data);
    if (res.data === true) {
      alert(`✅ Land Acquisition ${isEdit ? "updated" : "added"} successfully!`);
      navigate("/updateforms/land-acquisition");
    } else {
      alert("⚠️ Save failed — backend returned false or error.");
    }
  } catch (err) {
    console.error("❌ Error during submission:", err);
    alert("❌ Save failed. Check console.");
  } finally {
    setLoading(false);
  }
};

console.log("🗂️ Editing Row Data:", row);
console.log("🧾 laId:", laId, "isEdit:", isEdit);

// safe logging (no crash)
console.log("🏷️ SubCategory:", row?.sub_category_of_land ?? "(no subcategory)");
console.log("📍 Coordinates:", row?.latitude ?? "(no lat)", row?.longitude ?? "(no lng)");
// console.log("🏷️ SubCategory:", row?.sub_category_of_land ?? "(no row)");
// console.log("📍 Coordinates:", row?.latitude ?? "(lat missing)", row?.longitude ?? "(lng missing)");

  
useEffect(() => {
  console.log("📋 Current Form Mode:", isEdit ? "EDIT" : "ADD");
  if (isEdit) console.log("🗂️ Editing Existing Record:", row);
}, [isEdit, row]);

if (!Array.isArray(allSubCategories)) {
  return <div>Loading subcategories...</div>;
}


  return (
    <div className={`${styles.container} container-padding`}>
          <div className="card">
            <div className="formHeading">
              <h2 className="center-align ps-relative">
                {isEdit ? "Edit Land Acquisition" : "Add Land Acquisition"}
              </h2>
            </div>
            <div className="innerPage">
              <form onSubmit={handleSubmit(onSubmit)} encType="multipart/form-data">
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
                      <p className="error-text">{errors.project_id_fk.message}</p>
                    )}
                  </div>
                  <div className="form-field">
                    <label>Land Status <span className="red">*</span></label>
                    <Controller
                      name="la_land_status_fk"
                      control={control}
                      rules={{ required: true }}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          options={statusOptions}
                          placeholder="Select Status"
                          isSearchable
                          isClearable
                          value={field.value}
                          onChange={(opt) => field.onChange(opt)}
                        />
                      )}
                    />
                    {errors.la_land_status_fk && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>Survey Number</label>
                    <input {...register("survey_number")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Special Feature</label>
                    <input {...register("special_feature")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Type of Land <span className="red">*</span></label>
                    <Controller
                      name="type_of_land"
                      control={control}
                      rules={{ required: true }}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          options={typeOptions}
                          placeholder="Select Type"
                          isSearchable
                          isClearable
                          value={field.value}
                          onChange={(opt) => field.onChange(opt)}
                        />
                      )}
                    />
                    {errors.type_of_land && (
                      <span className="text-danger">Required</span>
                    )}
                  </div>
                  <div className="form-field">
                    <label>Sub Category of Land <span className="red">*</span></label>
                    <Controller
                      name="sub_category_of_land"
                      control={control}
                      rules={{ required: true }}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          options={subCategoryOptions}
                          placeholder="Select Sub Category"
                          isSearchable
                          isClearable
                          value={field.value}
                          onChange={(opt) => field.onChange(opt)}
                          noOptionsMessage={() =>
                            selectedType ? "No sub category for selected type" : "Select a Type first"
                          }
                        />
                      )}
                    />
                    {errors.sub_category_of_land && (
                      <span className="text-danger">Required</span>
                    )}
                  </div>
                  <div className="form-field">
                    <label>Acquired Area (Ha) <span className="red">*</span></label>
                    <input {...register("area_acquired")} type="text" rules={{ required: true }} placeholder="Enter Value"/>
                    {errors.area_acquired && (
                      <span className="text-danger">Required</span>
                    )}
                  </div>
                  <div className="form-field">
                    <label>Village <span className="red">*</span></label>
                    <input {...register("village")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label> Taluka  <span className="red">*</span></label>
                    <input {...register("taluka")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label> Dy SLR </label>
                    <input {...register("dy_slr")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>SDO </label>
                    <input {...register("sdo")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Collector </label>
                    <input {...register("collector")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label> Total Area to be Acquired (Ha)  <span className="red">*</span></label>
                    <input {...register("area_to_be_acquired")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Area of Plot (Ha) </label>
                    <input {...register("area_of_plot")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Latitude </label>
                    <input {...register("latitude")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Longitude </label>
                    <input {...register("longitude")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Proposal submission Date to collector </label>
                    <input {...register("proposal_submission_date_to_collector")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field rupee-field">
                    <label>JM Fee Amount </label>
                    <input {...register("jm_fee_amount")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Chainage From <span className="red">*</span> </label>
                    <input {...register("chainage_from")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Chainage To <span className="red">*</span> </label>
                    <input {...register("chainage_to")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>JM Fee Letter received Date </label>
                    <input {...register("jm_fee_letter_received_date")} type="date" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>JM Fee Paid Date </label>
                    <input {...register("jm_fee_paid_date")} type="date" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>JM Start Date </label>
                    <input {...register("jm_start_date")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>JM Completion Date </label>
                    <input {...register("jm_completion_date")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>JM Sheet Date to SDO </label>
                    <input {...register("jm_sheet_date_to_sdo")} type="text" placeholder="Enter Value"/>
                  </div>
                </div>
                <div className="form-row">
                  <div className="d-flex justify-content-center w-100 mt-2 mb-1">
                    <div className="form-field">
                      <label>JM Approval</label>
                    </div>
                    <div className="form-field">
                      <div className="d-flex gap-20">
                        <label>
                          <input
                            type="radio"
                            value="accept"
                            {...register("jm_approval")}
                          />
                          Accept
                        </label>

                        <label>
                          <input
                            type="radio"
                            value="reject"
                            {...register("jm_approval")}
                          />
                          Reject
                        </label>
                      </div>
                    </div>
                  </div>
                </div>
                  <div className="form-row">

                  {jmApproval === "accept" && (
                    <div className="jm-approved-section w-100">
                      <h3 className="d-flex align-center justify-content-center mt-1 mb-1 w-100">Private Land Details</h3>

                      <div className="form-row">
                        <div className="d-flex w-100 align-center justify-content-center">
                          <div className="w-100 mb-1">
                            <div className="d-flex justify-content-center gap-20 w-100">
                              <label>
                                <input
                                  type="radio"
                                  value="PrivateIRA"
                                  {...register("private_land_process")}
                                />
                                Private (Indian Railway Act)
                              </label>

                              <label>
                                <input
                                  type="radio"
                                  value="PrivateDPM"
                                  {...register("private_land_process")}
                                />
                                Private - IRA & DPM
                              </label>
                            </div>
                          </div>
                        </div>
                      </div>

                      {privateLandProcess === "PrivateIRA" && (
                        <>
                        <div className="form-row">
                          <div className="form-field">
                            <label>Collector</label>
                            <input
                              type="text"
                              {...register("private_ira_collector")}
                              placeholder="Enter Value"
                            />
                          </div>
                        </div>
                        <h6 className="d-flex justify-content-center mt-1 mb-2">Declaration of Special Railway project</h6>

                        <div className="form-row">
                          <div className="form-field">
                            <label>Submission of Proposal to GM.(date)</label>
                            <input
                              type="date"
                              {...register("submission_of_proposal_to_GM")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Approval of GM</label>
                            <input
                              type="date"
                              {...register("approval_of_GM")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Draft Letter to CE/Con for Approval</label>
                            <input
                              type="date"
                              {...register("draft_letter_to_con_for_approval_rp")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Date of Approval of CE/Construction</label>
                            <input
                              type="date"
                              {...register("draft_letter_to_con_for_approval_rp")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Date of Uploading of Gazette notification</label>
                            <input
                              type="date"
                              {...register("date_of_uploading_of_gazette_notification_rp")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Publication in gazette</label>
                            <input
                              type="date"
                              {...register("publication_in_gazette_rp")}
                            />
                          </div>
                        </div>

                          <h6 className="d-flex justify-content-center mt-1 mb-2">Nomination of competent Authority</h6>

                          <div className="form-row">
                            <div className="form-field">
                              <label>Date of Proposal to DC for nomination</label>
                              <input
                                type="date"
                                {...register("date_of_proposal_to_DC_for_nomination")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Date of Nomination of competent Authority</label>
                              <input
                                type="date"
                                {...register("date_of_nomination_of_competenta_authority")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Draft Letter to CE/Con for Approval</label>
                              <input
                                type="date"
                                {...register("draft_letter_to_con_for_approval_ca")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Date of Approval of CE/Construction</label>
                              <input
                                type="date"
                                {...register("date_of_approval_of_construction_ca")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Date of Uploading of Gazette notification</label>
                              <input
                                type="date"
                                {...register("date_of_uploading_of_gazette_notification_ca")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Publication in gazette</label>
                              <input
                                type="date"
                                {...register("publication_in_gazette_ca")}
                              />
                            </div>

                          </div>

                          <h6 className="d-flex justify-content-center mt-1 mb-2">Publication of notification under 20 A</h6>

                          <div className="form-row">
                            <div className="form-field">
                              <label>Date of Submission of draft notification to CALA</label>
                              <input
                                type="date"
                                {...register("date_of_submission_of_draft_notification_to_CALA")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Approval of CALA</label>
                              <input
                                type="date"
                                {...register("approval_of_CALA_20a")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Draft Letter to CE/Con for Approval</label>
                              <input
                                type="date"
                                {...register("draft_letter_to_con_for_approval_20a")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Date of Approval of CE/Construction</label>
                              <input
                                type="date"
                                {...register("date_of_approval_of_construction_20a")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Date of Uploading of Gazette notification</label>
                              <input
                                type="date"
                                {...register("date_of_uploading_of_gazette_notification_20a")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Publication in gazette</label>
                              <input
                                type="date"
                                {...register("publication_in_gazette_20a")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Publication in 2 Local Newspapers</label>
                              <input
                                type="date"
                                {...register("publication_in_2_local_news_papers_20a")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Pasting of notification in villages</label>
                              <input
                                type="date"
                                {...register("pasting_of_notification_in_villages_20a")}
                              />
                            </div>

                          </div>

                          <h6 className="d-flex justify-content-center mt-1 mb-2">Grievances Redressal</h6>

                          <div className="form-row">
                            <div className="form-field">
                              <label>Receipt of Grievances</label>
                              <input
                                type="date"
                                {...register("receipt_of_grievances")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Disposal of Grievances</label>
                              <input
                                type="date"
                                {...register("disposal_of_grievances")}
                              />
                            </div>
                          
                          </div>

                          <div className="d-flex align-center justify-content-center w-100 mb-1 mt-1">
                            <NavLink 
                              to="/add-issue-form"
                              className="btn btn-primary"
                            >
                                Raise An Issue
                            </NavLink>
                          </div>

                         <h6 className="d-flex justify-content-center mt-1 mb-2">Acquisition notice under 20E</h6>

                          <div className="form-row">
                            <div className="form-field">
                              <label>Date of Submission of draft notification to CALA</label>
                              <input
                                type="date"
                                {...register("date_of_submission_of_draft_notification_to_CALA_20e")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Approval of CALA</label>
                              <input
                                type="date"
                                {...register("approval_of_CALA_20e")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Draft Letter to CE/Con for Approval</label>
                              <input
                                type="date"
                                {...register("draft_letter_to_con_for_approval_20e")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Date of Approval of CE/Construction</label>
                              <input
                                type="date"
                                {...register("date_of_approval_of_construction_20e")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Date of Uploading of Gazette notification</label>
                              <input
                                type="date"
                                {...register("date_of_uploading_of_gazette_notification_20e")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Publication in gazette</label>
                              <input
                                type="date"
                                {...register("publication_in_gazette_20e")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Publication of notice in 2 Local News papers</label>
                              <input
                                type="date"
                                {...register("publication_of_notice_in_2_local_news_papers_20e")}
                              />
                            </div>
                          
                          </div>

                           <h6 className="d-flex justify-content-center mt-1 mb-2">Acquisition notice under 20F</h6>

                          <div className="form-row">
                            <div className="form-field">
                              <label>Date of Submission of draft notification to CALA</label>
                              <input
                                type="date"
                                {...register("date_of_submission_of_draft_notification_to_CALA_20f")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Approval of CALA</label>
                              <input
                                type="date"
                                {...register("approval_of_CALA_20f")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Draft Letter to CE/Con for Approval</label>
                              <input
                                type="date"
                                {...register("draft_letter_to_con_for_approval_20f")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Date of Approval of CE/Construction</label>
                              <input
                                type="date"
                                {...register("date_of_approval_of_construction_20f")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Date of Uploading of Gazette notification</label>
                              <input
                                type="date"
                                {...register("date_of_uploading_of_gazette_notification_20f")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Publication in gazette</label>
                              <input
                                type="date"
                                {...register("publication_in_gazette_20f")}
                              />
                            </div>
                            <div className="form-field">
                              <label>Publication of notice in 2 Local News papers</label>
                              <input
                                type="date"
                                {...register("publication_of_notice_in_2_local_news_papers_20f")}
                              />
                            </div>

                          </div>
                          
                      </>
                    )}

                    {privateLandProcess === "PrivateDPM" && (
                        <>
                        <div className="form-row">
                          <div className="form-field">
                            <label>Name of Owner</label>
                            <input
                              type="text"
                              {...register("private_name_of_owner")}
                              placeholder="Enter Value"
                            />
                          </div>
                          <div className="form-field rupee-field">
                            <label>Basic Rate</label>
                            <input
                              type="text"
                              {...register("private_basic_rate")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Agriculture tree nos</label>
                            <input
                              type="date"
                              {...register("agriculture_tree_nos")}
                            />
                          </div>
                          <div className="form-field rupee-field">
                            <label>Agriculture tree rate</label>
                            <input
                              type="text"
                              {...register("private_agri_tree_rate")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Forest tree nos</label>
                            <input
                              type="date"
                              {...register("forest_tree_nos")}
                            />
                          </div>
                          <div className="form-field rupee-field">
                            <label>Forest tree rate</label>
                            <input
                              type="text"
                              {...register("forest_tree_rate")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Consent from Owner</label>
                            <input
                              type="date"
                              {...register("consent_from_owner")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Legal Search Report</label>
                            <input
                              type="date"
                              {...register("legal_search_report")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Date of Registration</label>
                            <input
                              type="date"
                              {...register("date_of_registration")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Date of Possession</label>
                            <input
                              type="date"
                              {...register("date_of_possession")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Forest Tree Survey</label>
                            <input
                              type="date"
                              {...register("forest_tree_survey")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Forest Tree Valuation</label>
                            <input
                              type="date"
                              {...register("forest_tree_valuation")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Horticulture Tree Survey</label>
                            <input
                              type="date"
                              {...register("horticulture_tree_survey")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Horticulture Tree Valuation</label>
                            <input
                              type="date"
                              {...register("horticulture_tree_valuation")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Structure Survey</label>
                            <input
                              type="date"
                              {...register("structure_survey")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Structure Valuation</label>
                            <input
                              type="date"
                              {...register("structure_valuation")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Borewell Survey</label>
                            <input
                              type="date"
                              {...register("borewell_survey")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Borewell Valuation</label>
                            <input
                              type="date"
                              {...register("borewell_valuation")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Date of RFP to ADTP</label>
                            <input
                              type="date"
                              {...register("date_of_rfp_to_adtp")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Date of Rate Fixation of Land</label>
                            <input
                              type="date"
                              {...register("date_of_rate_fixation_of_land")}
                            />
                          </div>
                          <div className="form-field">
                            <label>SDO demand for payment</label>
                            <input
                              type="date"
                              {...register("sdo_demand_for_payment")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Date of Approval for Payment</label>
                            <input
                              type="date"
                              {...register("date_of_approval_for_payment")}
                            />
                          </div>
                          <div className="form-field rupee-field">
                            <label>Payment Amount</label>
                            <input
                              type="text"
                              {...register("payment_amount")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Payment Date</label>
                            <input
                              type="date"
                              {...register("private_payment_date")}
                            />
                          </div>
                          <div className="form-field">
                            <label>100 Percent Solatium</label>
                            <input
                              type="text"
                              {...register("hundred_percent_Solatium")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Extra 25 Percent</label>
                            <input
                              type="text"
                              {...register("extra_25_percent")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Land Compensation</label>
                            <input
                              type="number"
                              {...register("land_compensation")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Agriculture Tree Compensation</label>
                            <input
                              type="number"
                              {...register("agriculture_tree_compensation")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Total Rate Divide M2</label>
                            <input
                              type="number"
                              {...register("total_rate_divide_m2")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Forest Tree Compensation</label>
                            <input
                              type="number"
                              {...register("forest_tree_compensation")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Structure Compensation</label>
                            <input
                              type="number"
                              {...register("structure_compensation")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Borewell Compensation</label>
                            <input
                              type="number"
                              {...register("borewell_compensation")}
                            />
                          </div>
                          <div className="form-field">
                            <label>Total Compensation</label>
                            <input
                              type="date"
                              {...register("total_compensation")}
                            />
                          </div>

                        </div>
                      </>
                    )}
                    </div>
                  )}
                  </div>

                  <div className="row mt-1 mb-2">
                                <h3 className="mb-1 d-flex align-center justify-content-center">Attachments</h3>
                  
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
                                      {attachmentsFields.length > 0 ? (
                                        attachmentsFields.map((item, index) => (
                                          <tr key={item.id}>
                                            <td>
                                              <Controller
                                                name={`attachments.${index}.la_file_typess`}
                                                control={control}
                                                render={({ field }) => (
                                                  <Select
                                                    {...field}
                                                    classNamePrefix="react-select"
                                                    options={fileTypeOptions}
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
                                                {...register(`attachments.${index}.laDocumentNames`)}
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
                                                  {...register(`attachments.${index}.laFiles`)}
                                                  className={styles["file-upload-input"]}
                                                />
                                                {(
                                                  watch(`attachments.${index}.laFiles`)?.[0]?.name ||
                                                  watch(`attachments.${index}.laDocumentFileNames`)
                                                ) && (
                                                  <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                                    {watch(`attachments.${index}.laFiles`)?.[0]?.name ||
                                                    watch(`attachments.${index}.laDocumentFileNames`)}
                                                  </p>
                                                )}
                                              </div>

                                            </td>
                                            <td className="text-center d-flex align-center justify-content-center">
                                              <button
                                                type="button"
                                                className="btn btn-outline-danger"
                                                onClick={() => removeAttachments(index)}
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
                                      appendAttachments({ la_file_typess: null, laDocumentNames: "", laFiles: "" })
                                    }
                                  >
                                    <BiListPlus
                                      size="24"
                                    />
                                  </button>
                                </div>
                              </div>

                  <div className="form-row w-100">
                    <div className="form-field flex-100">
                      <label>JM Remarks</label>
                        <textarea
                          type="text"
                          {...register("jm_remarks")}
                          className="form-control"
                        >
                        </textarea>
                      </div>  
                    </div>

                    <div className="form-row w-100">
                      <div className="form-field flex-100">
                        <label>JM Sheet Date to SDO</label>
                        <textarea
                          type="date"
                          {...register("jm_sheet_date_to_sdo")}
                          className="form-control"
                        >
                        </textarea>
                      </div>
                    </div>

                <div className="form-post-buttons">
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? "Saving..." : isEdit ? "Update" : "Save"}
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