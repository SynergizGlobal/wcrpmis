import React, { useEffect, useState } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import { Outlet } from 'react-router-dom';
import styles from './LandAcquisitionForm.module.css';
import { NavLink } from "react-router-dom";

import { API_BASE_URL } from "../../../../config";

import { MdOutlineDeleteSweep } from 'react-icons/md';
import { BiListPlus } from 'react-icons/bi';

export default function LandAcquisitionForm() {

    const navigate = useNavigate();
    const { state } = useLocation(); // passed when editing
    const row = state?.data || null;
    const isEdit = !!row;

    const [projectOptions, setProjectOptions] = useState([]);
    const [statusOptions, setStatusOptions] = useState([]);
    const [typeOptions, setTypeOptions] = useState([]);
    const [allSubCategories, setAllSubCategories] = useState([]);
    const [subCategoryOptions, setSubCategoryOptions] = useState([]);
    const [fileTypeOptions, setfileTypeOptions] = useState([]);

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
          total_area_to_be_acquired: "",
          area_of_plot: "",
          latitude: "",
          longitude: "",
          propsal_submission_date_to_collector: "",
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

      

      useEffect(() => {
          if (isEdit && state?.data) {
            Object.entries(state.data).forEach(([key, value]) => setValue(key, value));
          }
        }, [state, setValue, isEdit]);

    useEffect(() => {

  const loadDropdowns = async () => {
    try {
      const [projectsRes, statusRes, typesRes, subCatsRes, fileTypes] = await Promise.all([
          axios.post(`${API_BASE_URL}/land-acquisition/form/ajax/getProjectsList`, {}, { withCredentials: true }),
          axios.get(`${API_BASE_URL}/land-acquisition/form/ajax/getLaLandStatus`, { withCredentials: true }),
          axios.post(`${API_BASE_URL}/land-acquisition/form/ajax/getLandsListForLAForm`, {}, { withCredentials: true }),
          axios.post(`${API_BASE_URL}/land-acquisition/form/ajax/getSubCategorysListForLAForm`, {}, { withCredentials: true }),
          axios.get(`${API_BASE_URL}/land-acquisition/form/ajax/getLaFileType`, { withCredentials: true }),
        ]);

       const projects = (projectsRes.data || []).map(p =>
        // ({ value: p.project_id_fk, label: `${p.project_id_fk} - ${p.project_name}` })
          ({ value: p.project_id_fk, label: p.project_name })
        );

        const statuses = (statusRes.data || [])
          .filter(s => s.la_land_status)
          .map(s => formatOption(s.la_land_status));

        const types = (typesRes.data || [])
          .filter(t => t.type_of_land)
          .map(t => formatOption(t.type_of_land));

          const fileTypeses = (fileTypes.data || [])
          .filter(f => f.la_file_typess)
          .map(f => formatOption(f.la_file_typess));

          

        // Keep *raw* list so we can filter client-side by type_of_land if API provides it
        const subCatsRaw = (subCatsRes.data || []);
        setAllSubCategories(subCatsRaw);

        setProjectOptions(projects);
        setStatusOptions(statuses);
        setTypeOptions(types);
        setfileTypeOptions(fileTypeses);

      if (isEdit && row) {
          // Plain fields

          Object.keys(row).forEach(k => row[k] !== null && setValue(k, row[k]));

          const plainKeys = [
            "survey_number", "special_feature", "area_acquired", "village",
            "taluka", "dy_slr", "sdo", "collector",
            "area_to_be_acquired", "area_of_plot", "latitude",
            "longitude", "proposal_submission_date_to_collector",
            "jm_fee_amount", "chainage_from", "chainage_to",
            "jm_fee_letter_received_date", "jm_fee_paid_date",
            "jm_start_date", "jm_completion_date", "jm_sheet_date_to_sdo",
            "jm_approval", "jm_remarks",
          ];
          // plainKeys.forEach(k => (row[k] ?? row[k] === 0) && setValue(k, row[k]));

          // Project
            const p = projectsRes.data.find(x => x.project_id_fk === row.project_id_fk);
  if (p) setValue("project_id_fk", { value: p.project_id_fk, label: p.project_name });

          // Status
          const s = statusRes.data.find(x => x.la_land_status === row.la_land_status_fk);
  if (s) setValue("la_land_status_fk", { value: s.la_land_status, label: s.la_land_status });


          // Type + SubCategory (needs filtering)
          const t = typesRes.data.find(x => x.type_of_land === row.type_of_land);
  if (t) setValue("type_of_land", { value: t.type_of_land, label: t.type_of_land });

          const f = fileTypes.data.find(x => x.la_file_typess === row.la_file_typess);
  if (f) setValue("la_file_typess", { value: f.la_file_typess, label: f.la_file_typess });

          // Build filtered sub-category list for the current type
            const filtered = subCatsRaw.filter(x => x.type_of_land === row.type_of_land);
  setSubCategoryOptions(filtered.map(x => ({ value: x.sub_category_of_land, label: x.sub_category_of_land })));

            const sc = filtered.find(x => x.sub_category_of_land === row.sub_category_of_land);
  if (sc) setValue("sub_category_of_land", { value: sc.sub_category_of_land, label: sc.sub_category_of_land });
  
        } else {
          // Add mode: ensure selects are blank
          setValue("project_id_fk", null);
          setValue("la_land_status_fk", null);
          setValue("type_of_land", null);
          setValue("sub_category_of_land", null);
          setValue("la_file_typess", null);
          setSubCategoryOptions([]); // nothing until a type is chosen
        }
      } catch (err) {
        console.error("Dropdown load failed", err);
      }
    };

    loadDropdowns();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

   const filterSubCategories = (rawList, typeValue) => {
  if (!rawList) return [];

  return rawList
    .filter(sc => sc.sub_category_of_land)
    .filter(sc =>
      !typeValue || !sc.type_of_land || sc.type_of_land === typeValue
    )
    .map(sc => ({
      value: sc.sub_category_of_land,
      label: sc.sub_category_of_land
    }));
};

  const selectedType = watch("type_of_land"); // {value,label} or null
  useEffect(() => {
  const typeValue = watch("type_of_land")?.value;
  const filtered = filterSubCategories(allSubCategories, typeValue);
  setSubCategoryOptions(filtered);

  const currentSub = watch("sub_category_of_land");
  if (currentSub && !filtered.find(f => f.value === currentSub.value)) {
    setValue("sub_category_of_land", null);
  }
}, [watch("type_of_land"), allSubCategories]);

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
                            value={field.value || null}
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
                          value={field.value || null}
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
                    <input {...register("area_acquired")} type="text" placeholder="Enter Value"/>
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
                            {...register("jm_approval", { required: "Please select JM Approval" })}
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
                                                name="la_file_typess"
                                                control={control}
                                                render={({ field }) => (
                                                  <Select
                                                    {...field}
                                                    classNamePrefix="react-select"
                                                    options={fileTypeOptions}
                                                    placeholder="Select Sub Category"
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
                                              <input
                                                type="file"
                                                {...register(`attachments.${index}.laFiles`)}
                                                className="form-control"
                                              />
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
                                      appendAttachments({ la_file_typess: "", laDocumentNames: "", laFiles: "" })
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
                          placeholder="Enter remarks for JM approval"
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