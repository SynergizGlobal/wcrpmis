import React, { useState, useEffect } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from "./UserForm.module.css";
import { API_BASE_URL } from "../../../../config";

import { RiAttachment2 } from 'react-icons/ri';

export default function UserForm() {
    
    const navigate = useNavigate();
    const { state } = useLocation(); 
    const isEdit = Boolean(state?.project);

    const {
        register,
        control,
        handleSubmit,
        watch,
        setValue,
        formState: { errors },
      } = useForm({
        defaultValues: {
          user_name: "",
          designation: "",
          department_fk: "",
          user_type_fk: "",
          reporting_to_id_srfk: "",
          user_role_name_fk: "",
          email_id: "",
          mobile_number: "",
          personal_contact_number: "",
          landline: "",
          extension: "",
          pmis_key_fk: "",
          actual_completion_date: "",
          fileName: "",
        },
      });

  // MODULES with submenus (this should come from DB later)
  const MODULES = {
    Contracts: ["Contract Master", "Agreement Details", "Milestones"],
    Design: ["Design Drawings", "BOMS", "Specifications"],
    "Land Acquisition": ["Village List", "Compensation", "Survey"],
    "Utility Shifting": ["Waterline", "Powerline", "OFC"],
    Works: ["Earthwork", "Bridges", "Track", "Utilities"],
  };

  // Checkbox state: which modules are enabled
  const [enabledModules, setEnabledModules] = useState([]);

  // Submenu selections for each module
  const [moduleAccess, setModuleAccess] = useState({});

  // Toggle module checkbox
  const toggleModule = (moduleName) => {
    const isEnabled = enabledModules.includes(moduleName);

    if (isEnabled) {
      // remove access
      setEnabledModules(enabledModules.filter((m) => m !== moduleName));

      // also remove its submenu selections
      const copy = { ...moduleAccess };
      delete copy[moduleName];
      setModuleAccess(copy);
    } else {
      setEnabledModules([...enabledModules, moduleName]);
    }
  };

  // Handle multi-select submenu choice
  const handleSubMenuChange = (moduleName, selectedOptions) => {
    setModuleAccess({
      ...moduleAccess,
      [moduleName]: selectedOptions, // array of selected submenu values
    });
  };

    useEffect(() => {
      if (isEdit && state?.project) {
        Object.entries(state.project).forEach(([key, value]) => setValue(key, value));
      }
    }, [state, setValue, isEdit]);
  
    const onSubmit = async (data) => {
    try {
      const endpoint = isEdit
        ? `${API_BASE_URL}/projects/${state.project.projectId}`
        : `${API_BASE_URL}/projects`;

      if (isEdit) {
        await api.put(endpoint, data);
      } else {
        await api.post(endpoint, data);
      }

      alert("✅ Project saved successfully!");
      navigate("/wcrpmis/updateforms/project");
    } catch (err) {
      console.error("❌ Error saving project:", err);
      alert("Error saving project");
    }
  };

  return (
    <div className={styles.container}>

      <div className="card">
            <div className="formHeading">
              <h2 className="center-align ps-relative">
                {isEdit ? "Update Contract" : "Add Contract"}
              </h2>
            </div>
            <div className="innerPage">
              <form onSubmit={handleSubmit(onSubmit)}>
                <div className="form-row">
                  <div className="form-field">
                    <label>Name <span className="red">*</span></label>
                    <input {...register("user_name", { required: true })} type="text" placeholder="Enter Value"/>
                    {errors.user_name && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>Designation <span className="red">*</span></label>
                    <input {...register("designation", { required: true })} type="text" placeholder="Enter Value"/>
                    {errors.designation && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>Department <span className="red">*</span></label>
                    <Controller
                      name="department_fk"
                      control={control}
                      rules={{ required: true }}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select Value"
                          isSearchable
                        />
                      )}
                    />
                    {errors.department_fk && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>User Type <span className="red">*</span></label>
                    <Controller
                      name="user_type_fk"
                      control={control}
                      rules={{ required: true }}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select Value"
                          isSearchable
                        />
                      )}
                    />
                    {errors.user_type_fk && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>Reporting To <span className="red">*</span></label>
                    <Controller
                      name="reporting_to_id_srfk"
                      control={control}
                      rules={{ required: true }}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select Value"
                          isSearchable
                        />
                      )}
                    />
                    {errors.reporting_to_id_srfk && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>User Role <span className="red">*</span></label>
                    <Controller
                      name="user_role_name_fk"
                      control={control}
                      rules={{ required: true }}
                      render={({ field }) => (
                        <Select
                          {...field}
                          classNamePrefix="react-select"
                          placeholder="Select Value"
                          isSearchable
                        />
                      )}
                    />
                    {errors.user_role_name_fk && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>Email ID <span className="red">*</span></label>
                    <input {...register("email_id", { required: true })} type="email" placeholder="Enter Value"/>
                    {errors.email_id && <span className="text-danger">Required</span>}
                  </div>
                  <div className="form-field">
                    <label>Mobile Number </label>
                    <input {...register("mobile_number")} type="number" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Personal Contact Number </label>
                    <input {...register("personal_contact_number")} type="number" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Landline</label>
                    <input {...register("landline")} type="number" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>Extension</label>
                    <input {...register("extension")} type="number" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <label>PMIS KEY</label>
                    <input {...register("pmis_key")} type="text" placeholder="Enter Value"/>
                  </div>
                  <div className="form-field">
                    <div className={styles["file-upload-wrapper"]}>
                      <label htmlFor={`file-user-image`} className={styles["file-upload-label-icon"]}>
                        <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
                        User Image
                      </label>
                      <input
                        id={`file-user-image`}
                        type="file"
                        {...register("fileName")}
                        className={styles["file-upload-input"]}
                      />
                      {watch("fileName")?.[0]?.name && (
                        <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                          Selected: {watch("fileName")[0].name}
                        </p>
                      )}
                    </div>
                  </div>

                </div>
                {/* ============================ MODULE CHECKBOXES ============================ */}
                <div className={styles.moduleCheckboxRow}>
                  {Object.keys(MODULES).map((module) => (
                    <label key={module} className={styles.moduleCheckbox}>
                      <input
                        type="checkbox"
                        checked={enabledModules.includes(module)}
                        onChange={() => toggleModule(module)}
                      />
                      {module}
                    </label>
                  ))}
                </div>
                  <br />
                {/* ============================ MODULE TABLES ============================ */}
                <div className={styles.tablesContainer}>
                  {enabledModules.map((module) => (
                    <div className={styles.permissionCard} key={module}>
                      <h3>{module} Permission</h3>
                      <div className={styles.cardInput}>
                        <Select
                          isMulti
                          options={MODULES[module].map((s) => ({ value: s, label: s }))}
                          value={moduleAccess[module] || []}
                          onChange={(selected) => handleSubMenuChange(module, selected)}
                          placeholder="Select sub menus (leave empty for full access)"
                        />
                      </div>

                      {/* <p className={styles.note}>
                        {moduleAccess[module]?.length
                          ? "✔ User will only see selected sub-menus."
                          : "✔ No selection → user gets FULL access to this module."}
                      </p> */}
                    </div>
                  ))}
                </div>
              </form>
            </div>
      </div>

      {/* ============================ SAVE BUTTON ============================ */}
      <div className="form-post-buttons">
        <button className="btn btn-primary" onClick={() => alert("Save logic pending")}>
          SAVE
        </button>
        <button className="btn btn-secondary">CANCEL</button>
      </div>
    </div>
  );
}
