import React, { useEffect, useState } from "react";
import Select from "react-select";
import { useForm, useFieldArray, Controller } from "react-hook-form";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from "./DashboardForm.module.css";
import { API_BASE_URL } from "../../../../config";

export default function DashboardForm() {
  const navigate = useNavigate();
  const { state } = useLocation();

  const isEdit = Boolean(state?.dashboardData);
  const existing = state?.dashboardData || {};
  
  const [moduleOptions, setModuleOptions] = useState([]);
  const [roleOptions, setRoleOptions] = useState([]);
  const [typeOptions, setTypeOptions] = useState([]);
  const [userOptions, setUserOptions] = useState([]);
  const [folderOptions, setFolderOptions] = useState([]);

  
  const {
    control,
    handleSubmit,
    setValue,
    watch,
    register
  } = useForm({
    defaultValues: {
      dashboard: "",
      module: "",
      dashboardType: "",
      folder: "",
      work: "",
      priority: "",
      status: "",
	  mobileView: "",
      userDetails: [
        { userRole: [], userType: [], user: [] }
      ]
    }
  });

  const { fields, append, remove } = useFieldArray({
    control,
    name: "userDetails"
  });

  // ==============================
  // LOAD EDIT MODE DATA
  useEffect(() => {
    if (!isEdit) return;

    // Select dropdowns
	setValue("dashboard_name", existing.dashboard_name);

    setValue("module", {
      label: existing.module_name_fk,
      value: existing.module_name_fk
    });

    setValue("dashboardType", {
      label: existing.dashboard_type_fk,
      value: existing.dashboard_type_fk?.toLowerCase()
    });

    setValue("priority", existing.priority);
	setValue("folder", existing.parent_dashboard_id_sr_fk
	  ? {
	      value: existing.parent_dashboard_id_sr_fk,
	      label: existing.folder
	    }
	  : null
	);
    setValue("work", existing.dashboard_url);  
    setValue("status", {
      label: existing.soft_delete_status_fk,
      value: existing.soft_delete_status_fk
    });

	setValue("mobileView", existing.mobile_view ?? "");
    // USER DETAILS (MULTI SELECT)
    setValue("userDetails", [
      {
        userRole: (existing.user_role_access?.split(",") || [])
          .map(v => ({ label: v, value: v })),

        userType: (existing.user_type_access?.split(",") || [])
          .map(v => ({ label: v, value: v })),

        user: (existing.user_access?.split(",") || [])
          .map(v => ({ label: v, value: v }))
      }
    ]);

  }, [isEdit, existing, setValue]);


  useEffect(() => {

    // Load Dashboard Names
	api.get(`${API_BASE_URL}/api/dashboard/dashboards`)
	  .then((res) => {
	    setFolderOptions(
	      res.data.map(d => ({
	        value: d.dashboard_id,   
	        label: d.dashboard_name
	      }))
	    );
	  })
	  .catch(err => console.error("Folder fetch error:", err));

    // Load Modules
    api.get(`${API_BASE_URL}/api/dashboard/modules`)
      .then((res) => {
        setModuleOptions(
          res.data.map(m => ({
            value: m,
            label: m
          }))
        );
      })
      .catch(err => console.error("Module fetch error:", err));
	  
	  // USER ROLES
	   api.get(`${API_BASE_URL}/api/dashboard/roles`)
	     .then((res) => {
	       setRoleOptions(
	         res.data.map(r => ({
	           value: r.user_role_name,
	           label: r.user_role_name
	         }))
	       );
	     });

	   // USER TYPES
	   api.get(`${API_BASE_URL}/api/dashboard/types`)
	     .then((res) => {
	       setTypeOptions(
	         res.data.map(t => ({
	           value: t.user_type_fk,
	           label: t.user_type_fk
	         }))
	       );
	     });

	   // USERS LIST
	   api.get(`${API_BASE_URL}/api/dashboard/users`)
	     .then((res) => {
	       setUserOptions(
	         res.data.map(u => ({
				value: u.user_id, 
				label: `${u.user_id} - ${u.user_name}`
	         }))
	       );
	     });


  }, []);


  // ==============================
  // SUBMIT
  // ==============================
  const onSubmit = async (data) => {
    try {
     
	  const loggedInUserId = localStorage.getItem("userId");

      const payload = {
        dashboard_id: isEdit ? existing.dashboard_id : null,
		dashboard_name: data.dashboard_name || "",
        module_name_fk: data.module?.value || "",
        dashboard_type_fk: data.dashboardType?.value || "",
        priority: data.priority || null,
		mobile_view: data.mobileView || null,
        work_id_fk: data.work || null,
	    parent_dashboard_id_sr_fk: data.folder?.value || null,
        soft_delete_status_fk: data.status?.value || "Inactive",

        // USER ACCESS
        user_role_access: data.userDetails?.[0]?.userRole?.map(r => r.value).join(",") || "",
        user_type_access: data.userDetails?.[0]?.userType?.map(t => t.value).join(",") || "",
        user_access: data.userDetails?.[0]?.user?.map(u => u.value).join(",") || "",

		 modified_by_user_id_fk: loggedInUserId,
		 published_by_user_id_fk: loggedInUserId,
      };

      console.log("FINAL PAYLOAD SENT:", payload);

      // --- API CALL ---
      if (isEdit) {
		await api.put(`${API_BASE_URL}/api/dashboard/update/${existing.dashboard_id}`, payload);
        alert("Dashboard updated successfully!");
      } else {
        await api.post(`${API_BASE_URL}/api/dashboard/save`, payload);
        alert("Dashboard created successfully!");
      }

      navigate("/admin/access-dashboards");

    } catch (err) {
      console.error("SAVE/UPDATE ERROR:", err);
      alert("Error occurred while saving/updating dashboard");
    }
  };


  return (
    <div className={`${styles.container} container-padding`}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align ps-relative">
            Update Dashboard
          </h2>
        </div>

        <div className="innerPage">
          <form onSubmit={handleSubmit(onSubmit)}>

            {/* TOP ROW */}
			<div className="form-row">
			<div className="form-field">
			  <label>Dashboard :</label>
			  <input
			    type="text"
			    {...register("dashboard_name")}
			    placeholder="Enter value"
			    className="form-control"
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
				      />
				    )}
				  />
			    </div>
				  <div className="form-field">
				    <label>Dashboard Type :</label>
					<Controller
					  name="dashboardType"
					  control={control}
					  render={({ field }) => (
					    <Select
					      {...field}
					      options={[
					        { label: "Module", value: "module" },
					        { label: "Project", value: "project" }
					      ]}
					      placeholder="Select Dashboard Type"
						  classNamePrefix="react-select"
					    />
					  )}
					/>
				  </div>
              </div>

            {/* ROW 2 */}
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
			      />
			    )}
			  />
			</div>

			    <div className="form-field">
			      <label>Work :</label>
			      <input  classNamePrefix="react-select" {...register("work")} />
			    </div>
				
				<div className="form-field">
				    <label>Folder :</label>
				    <input  classNamePrefix="react-select" {...register("folder")} />
				  </div>
				</div>

            {/* ROW 3 */}
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
				      options={[
				        { label: "Active", value: "active" },
				        { label: "InActive", value: "inactive" }
				      ]}
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