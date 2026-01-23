import React, { useEffect, useState } from "react";
import Select from "react-select";
import { useForm, useFieldArray, Controller } from "react-hook-form";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from './FormsForm.module.css';
import { API_BASE_URL } from "../../../../config";

export default function FormsForm() {
  const navigate = useNavigate();
  const { state } = useLocation();

  const isEdit = Boolean(state?.formId);

  const [moduleOptions, setModuleOptions] = useState([]);
  const [roleOptions, setRoleOptions] = useState([]);
  const [typeOptions, setTypeOptions] = useState([]);
  const [userOptions, setUserOptions] = useState([]);
  const [folderOptions, setFolderOptions] = useState([]);
  const [statusOptions, setStatusOptions] = useState([]);

  const { control, handleSubmit, setValue, register } = useForm({
    defaultValues: {
      form_name: "",
      module: "",
      folder: "",
      priority: "",
      status: "",
      mobileView: "",
      userDetails: [{ userRole: [], userType: [], user: [] }]
    }
  });

  const { fields } = useFieldArray({
    control,
    name: "userDetails"
  });
  
  useEffect(() => {
    const loadDropdowns = async () => {
      try {
        const res = await api.post(
          `${API_BASE_URL}/form-access/ajax/form/add-form-access`,
          {}
        );

        const d = res.data;

        setModuleOptions(d.modulesList.map(m => ({
          label: m.module_name_fk,
          value: m.module_name_fk
        })));

        setFolderOptions(d.foldersList.map(f => ({
          label: f.form_name,
          value: f.form_id
        })));

        setRoleOptions(d.user_roles.map(r => ({
          label: r.access_value_id,
          value: r.access_value_id
        })));

        setTypeOptions(d.user_types.map(t => ({
          label: t.access_value_id,
          value: t.access_value_id
        })));

        setUserOptions(d.users.map(u => ({
          label: `${u.access_value_id} - ${u.access_value_name}`,
          value: u.access_value_id
        })));
      } catch (e) {
        console.error("Dropdown load error", e);
      }
    };

    loadDropdowns();
  }, []);
  
  useEffect(() => {
    const fetchStatusList = async () => {
      const res = await api.post(
        `${API_BASE_URL}/form-access/ajax/getStatusFilterListInForm`,
        {}
      );

      setStatusOptions(
        res.data.map(s => ({
          label: s.soft_delete_status_fk,
          value: s.soft_delete_status_fk
        }))
      );
    };

    fetchStatusList();
  }, []);


  useEffect(() => {
    if (!isEdit) return;

    const fetchFormDetails = async () => {
      const res = await api.post(
        `${API_BASE_URL}/form-access/ajax/form/get-form/details`,
        { form_id: state.formId }
      );

      const d = res.data;

      setValue("form_name", d.form_name);
      setValue("priority", d.priority);
      setValue("mobileView", d.display_in_mobile);
      setValue("status", { label: d.soft_delete_status_fk, value: d.soft_delete_status_fk });

      setValue("module", { label: d.module_name_fk, value: d.module_name_fk }, { shouldDirty: false });

      if (d.parent_form_id_sr_fk) {
        setValue("folder", { label: d.folder_name, value: d.parent_form_id_sr_fk }, { shouldDirty: false });
      }

      setValue(
        "userDetails.0.userRole",
        d.user_role_access
          ? d.user_role_access.split(",").map(v => ({ label: v, value: v }))
          : []
      );

      setValue(
        "userDetails.0.userType",
        d.user_type_access
          ? d.user_type_access.split(",").map(v => ({ label: v, value: v }))
          : []
      );

      setValue(
        "userDetails.0.user",
        d.user_access
          ? d.user_access.split(",").map(id => {
              const u = userOptions.find(x => x.value === id);
              return u || { label: id, value: id };
            })
          : []
      );
    };

    fetchFormDetails();
  }, [isEdit, state?.formId, userOptions, setValue]);

  // ==============================
  // SUBMIT
  // ==============================
  const onSubmit = async (data) => {
    try {
      const payload = {
        form_id: state.formId,
        priority: data.priority,
        soft_delete_status_fk: data.status?.value,
        display_in_mobile: data.mobileView,

        user_role_access:
          data.userDetails?.[0]?.userRole?.map(r => r.value).join(",") || "",

        user_type_access:
          data.userDetails?.[0]?.userType?.map(t => t.value).join(",") || "",

        user_access:
          data.userDetails?.[0]?.user?.map(u => u.value).join(",") || ""
      };

      console.log("FINAL PAYLOAD SENT:", payload);

      // 🔥 IMPORTANT: convert to x-www-form-urlencoded
      const formData = new URLSearchParams();
      Object.entries(payload).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          formData.append(key, value);
        }
      });

      // ✅ ONLY THIS API (Access update)
      await api.post(
        `${API_BASE_URL}/form-access/update-access-form`,
        formData,
        {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          }
        }
      );

      alert("Form updated successfully!");
      navigate("/admin/access-forms");

    } catch (err) {
      console.error("SAVE/UPDATE ERROR:", err);
      alert("Error occurred while updating form");
    }
  };

  return (
    <div className={`${styles.container} container-padding`}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            Update Form
          </h2>
        </div>

        <div className="innerPage">
          <form onSubmit={handleSubmit(onSubmit)}>

            {/* TOP ROW */}
			<div className="form-row">
			<div className="form-field">
			  <label>Form Name :</label>
			  <input
			    type="text"
			    {...register("form_name")}
			    placeholder="Enter value"
			    className="form-control"
				classNamePrefix="react-select"
		        disabled={isEdit}
			  />
			</div>

			  <div className="form-field">
			      <label>Module :</label>
				  <Controller
				    name="module"
				    control={control}
				    render={({ field }) => (
				      <Select
				        {...field}
				        options={moduleOptions}
				        placeholder="Select Module"
				        classNamePrefix="react-select"
						isDisabled={isEdit}
				      />
				    )}
				  />
			    </div>
            <div className="form-row">
			<div className="form-field">
			  <label>Folder :</label>
			  <Controller
			    name="folder"
			    control={control}
			    render={({ field }) => (
			      <Select
			        {...field}
			        options={folderOptions}
			        placeholder="Select Folder"
			        classNamePrefix="react-select"
					isDisabled={isEdit}
			      />
			    )}
			  />
			</div>
			</div>
				</div>

            {/* ROW 2 */}
			<div className="form-row">
			  <div className="form-field">
			    <label>Priority :</label>
				<input type="number" placeholder="Enter value" classNamePrefix="react-select" {...register("priority")}/>
			  </div>
			  
			  <div className="form-field">
			    <label>Status :</label>
			    <Controller
			      name="status"
			      control={control}
			      render={({ field }) => (
			        <Select
			          {...field}
			          options={statusOptions}
			          placeholder="Select Status"
			          classNamePrefix="react-select"
			        />
			      )}
			    />
			  </div>

			    <div className="form-field">
			      <label>Mobile View ?</label>
			      <div className="radio-group">
			        <label><input type="radio" value="Yes" {...register("mobileView")} /> Yes</label>
			        <label><input type="radio" value="No" {...register("mobileView")} /> No</label>
			      </div>
			    </div>
			  </div>

            {/* USER DETAILS TABLE */}
			<h6 className="d-flex justify-content-center mt-1 mb-2">User Details</h6>		
			<div className={`dataTable ${styles.tableWrapper}`}>
			<table className="user-table">
			  <thead>
			    <tr>
			      <th>User Role</th>
			      <th>User Type</th>
			      <th>User</th>
			    </tr>
			  </thead>
			  <tbody>
			  {fields.map((row, index) => (
			    <tr key={row.id}>
			      <td>
			        <Controller
			          name={`userDetails.${index}.userRole`}
			          control={control}
			          render={({ field }) => <Select {...field} isMulti options={roleOptions} />}
			        />
			      </td>
			      <td>
			        <Controller
			          name={`userDetails.${index}.userType`}
			          control={control}
			          render={({ field }) => <Select {...field} isMulti options={typeOptions} />}
			        />
			      </td>
			      <td>
			        <Controller
			          name={`userDetails.${index}.user`}
			          control={control}
			          render={({ field }) => <Select {...field} isMulti options={userOptions} />}
			        />
			      </td>
			    </tr>
			  ))}
			  </tbody>
			</table>
		</div>

			{/* Buttons */}
              <div className="form-post-buttons">
                <button type="submit" className="btn btn-primary">
                 Update
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