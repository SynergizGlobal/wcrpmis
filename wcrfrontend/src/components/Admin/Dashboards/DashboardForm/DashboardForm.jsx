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
  
  const [dashboardOptions, setDashboardOptions] = useState([]);
  const [moduleOptions, setModuleOptions] = useState([]);
  const [dashboardTypeOptions, setDashboardTypeOptions] = useState([]);
  const [roleOptions, setRoleOptions] = useState([]);
  const [typeOptions, setTypeOptions] = useState([]);
  const [userOptions, setUserOptions] = useState([]);
  
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
  // ==============================
  useEffect(() => {
    if (isEdit) {
      Object.keys(existing).forEach((key) => {
        if (key !== "userDetails") setValue(key, existing[key]);
      });

      if (existing.userDetails) {
        setValue("userDetails", existing.userDetails);
      }
    }
  }, [isEdit, existing, setValue]);

  useEffect(() => {

    // Load Dashboard Names
    api.get(`${API_BASE_URL}/api/dashboard/dashboards`)
      .then((res) => {
        setDashboardOptions(
          res.data.map(d => ({
            value: d.dashboard_id,
            label: d.dashboard_name
          }))
        );
      })
      .catch(err => console.error("Dashboard fetch error:", err));

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
      const payload = {
        ...data,
        userDetails: data.userDetails.map((u) => ({
          userRole: u.userRole.map((x) => x.value),
          userType: u.userType.map((x) => x.value),
          user: u.user.map((x) => x.value)
        }))
      };

     await api.post(`${API_BASE_URL}/dashboards/saveOrUpdate`, payload);

      alert("Dashboard saved!");
      navigate("/updateforms/Dashboards");

    } catch (err) {
      console.error("SAVE ERROR:", err);
      alert("Error saving dashboard");
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
				<Controller
				  name="dashboard"
				  control={control}
				  render={({ field }) => (
				    <Select
				      {...field}
				      options={dashboardOptions}
				      placeholder="Select Dashboard"
					  classNamePrefix="react-select"
				    />
				  )}
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
			    <input classNamePrefix="react-select" {...register("folder")} />
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
				<input type="number" classNamePrefix="react-select" {...register("priority")}/>
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
			        <label><input type="radio" value="Yes" {...register("mobile")} /> Yes</label>
			        <label><input type="radio" value="No" {...register("mobile")} /> No</label>
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

			        {/* USER ROLE (MULTI SELECT) */}
			        <td>
			          <Controller
			            name={`users.${index}.role`}
			            control={control}
			            render={({ field }) => (
			              <Select
			                {...field}
			                isMulti
			                classNamePrefix="react-select"
			                options={roleOptions}
			                placeholder="Select Roles"
			              />
			            )}
			          />
			        </td>

			        {/* USER TYPE (MULTI SELECT) */}
			        <td>
			          <Controller
			            name={`users.${index}.type`}
			            control={control}
			            render={({ field }) => (
			              <Select
			                {...field}
			                isMulti
			                 classNamePrefix="react-select"
			                options={typeOptions}
			                placeholder="Select User Types"
			              />
			            )}
			          />
			        </td>

			        {/* USER (MULTI SELECT) */}
			        <td>
			          <Controller
			            name={`users.${index}.user`}
			            control={control}
			            render={({ field }) => (
			              <Select
			                {...field}
			                isMulti
			                 classNamePrefix="react-select"
			                options={userOptions}
			                placeholder="Select Users"
			              />
			            )}
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