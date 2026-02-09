import React, { useEffect, useState } from "react";
import Select from "react-select";
import { useForm, useFieldArray, Controller } from "react-hook-form";
import { useNavigate, useLocation } from "react-router-dom";
import api from "../../../../api/axiosInstance";
import styles from './ReportsAccessForm.module.css';
import { API_BASE_URL } from "../../../../config";

export default function ReportsForm() {
  const navigate = useNavigate();
  const { state } = useLocation();

  const isEdit = Boolean(state?.formId);

  const [moduleOptions, setModuleOptions] = useState([]);
  const [roleOptions, setRoleOptions] = useState([]);
  const [typeOptions, setTypeOptions] = useState([]);
  const [userOptions, setUserOptions] = useState([]);
  const [folderOptions, setFolderOptions] = useState([]);
  const [statusOptions, setStatusOptions] = useState([]);
  const [loading, setLoading] = useState(false);

  const { control, handleSubmit, setValue, register, reset } = useForm({
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
  
  // Load dropdown options
  useEffect(() => {
    const loadDropdowns = async () => {
      try {
        const res = await api.post(
          `${API_BASE_URL}/report-access/ajax/form/get-access-report`,
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
  
  // Load status options
  useEffect(() => {
    const fetchStatusList = async () => {
      const res = await api.post(
        `${API_BASE_URL}/report-access/ajax/getStatusFilterListInReport`,  
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

  // Fetch form details for editing
  useEffect(() => {
    if (!isEdit || !state.formId) return;

    const fetchFormDetails = async () => {
      try {
        setLoading(true);
        const res = await api.post(
          `${API_BASE_URL}/report-access/ajax/form/get-access-report/details`,
          { form_id: state.formId }
        );

        // Extract the data from reportDetails property
        const d = res.data?.reportDetails || res.data;
        
        if (!d) {
          console.error("No data received from API");
          alert("No data found for this report");
          return;
        }

        console.log("Fetched form details:", d);

        // Reset form with fetched data
        reset({
          form_name: d.form_name || "",
          priority: d.priority || "",
          mobileView: d.display_in_mobile || "No",
          status: d.soft_delete_status_fk ? { 
            label: d.soft_delete_status_fk, 
            value: d.soft_delete_status_fk 
          } : "",
          module: d.module_name_fk ? { 
            label: d.module_name_fk, 
            value: d.module_name_fk 
          } : "",
          folder: d.parent_form_id_sr_fk && d.folder_name ? { 
            label: d.folder_name, 
            value: d.parent_form_id_sr_fk 
          } : "",
          userDetails: [{
            userRole: [],
            userType: [],
            user: []
          }]
        });

        // Set user role access (from accessPermissions array)
        if (d.accessPermissions && Array.isArray(d.accessPermissions)) {
          // Filter for user_role access_type
          const userRoles = d.accessPermissions
            .filter(perm => perm.access_type === "user_role")
            .map(perm => ({ label: perm.access_value, value: perm.access_value }));
          
          setValue("userDetails.0.userRole", userRoles);
        } else if (d.user_role_access) {
          // Fallback to the string if accessPermissions doesn't exist
          const userRoles = d.user_role_access
            .split(",")
            .map(v => v.trim())
            .filter(v => v)
            .map(v => ({ label: v, value: v }));
          
          setValue("userDetails.0.userRole", userRoles);
        }

        // Set user type access
        if (d.user_type_access) {
          const userTypes = d.user_type_access
            .split(",")
            .map(v => v.trim())
            .filter(v => v)
            .map(v => ({ label: v, value: v }));
          
          setValue("userDetails.0.userType", userTypes);
        }

        // Set user access
        if (d.user_access) {
          const userAccess = d.user_access
            .split(",")
            .map(id => {
              const trimmedId = id.trim();
              const u = userOptions.find(x => x.value === trimmedId);
              return u || { label: trimmedId, value: trimmedId };
            });
          
          setValue("userDetails.0.user", userAccess);
        }

      } catch (error) {
        console.error("Error fetching form details:", error);
        alert("Failed to load report details");
      } finally {
        setLoading(false);
      }
    };

    fetchFormDetails();
  }, [isEdit, state?.formId, userOptions, setValue, reset]);

  // Submit handler
  const onSubmit = async (data) => {
    try {
      setLoading(true);
      
      // Extract user roles from form data
      let userRoleAccess = "";
      if (data.userDetails?.[0]?.userRole?.length > 0) {
        userRoleAccess = data.userDetails[0].userRole.map(r => r.value).join(",");
      }

      // Prepare payload
      const payload = {
        form_id: state.formId,
        priority: data.priority || "",
        soft_delete_status_fk: data.status?.value || "",
        display_in_mobile: data.mobileView || "No",
        user_role_access: userRoleAccess,
        user_type_access: data.userDetails?.[0]?.userType?.map(t => t.value).join(",") || "",
        user_access: data.userDetails?.[0]?.user?.map(u => u.value).join(",") || ""
      };

      console.log("FINAL PAYLOAD:", payload);

      // Convert to x-www-form-urlencoded
      const formData = new URLSearchParams();
      Object.entries(payload).forEach(([key, value]) => {
        if (value !== undefined && value !== null) {
          formData.append(key, value);
        }
      });

      // Update report
      await api.post(
        `${API_BASE_URL}/report-access/update-access-report`,
        formData,
        {
          headers: {
            "Content-Type": "application/x-www-form-urlencoded"
          }
        }
      );

      alert("Report updated successfully!");
      navigate("/admin/access-reports");

    } catch (err) {
      console.error("Update error:", err);
      console.error("Error response:", err.response?.data);
      alert(`Error: ${err.response?.data?.message || "Failed to update report"}`);
    } finally {
      setLoading(false);
    }
  };

  if (loading && isEdit) {
    return (
      <div className={styles.container}>
        <div className="card">
          <div className="formHeading">
            <h2 className="center-align">Loading Report Details...</h2>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className={`${styles.container} container-padding`}>
      <div className="card">
        <div className="formHeading">
          <h2 className="center-align ps-relative">
               Update Report
          </h2>
        </div>

        <div className="innerPage">
          <form onSubmit={handleSubmit(onSubmit)}>

            {/* TOP ROW */}
            <div className="form-row">
              <div className="form-field">
                <label>Report Name :</label>
                <input
                  type="text"
                  {...register("form_name")}
                  placeholder="Report Name"
                  className="form-control"
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

            {/* SECOND ROW */}
            <div className="form-row">
              <div className="form-field">
                <label>Priority :</label>
                <input 
                  type="number" 
                  {...register("priority")}
                  placeholder="Enter priority"
                  className="form-control"
                />
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
                  <label>
                    <input 
                      type="radio" 
                      value="Yes" 
                      {...register("mobileView")} 
                    /> Yes
                  </label>
                  <label>
                    <input 
                      type="radio" 
                      value="No" 
                      {...register("mobileView")} 
                    /> No
                  </label>
                </div>
              </div>
            </div>

            {/* USER DETAILS TABLE */}
            <h6 className="d-flex justify-content-center mt-3 mb-2">User Details</h6>		
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
                          render={({ field }) => (
                            <Select 
                              {...field} 
                              isMulti 
                              options={roleOptions} 
                              placeholder="Select Roles"
                              classNamePrefix="react-select"
                            />
                          )}
                        />
                      </td>
                      <td>
                        <Controller
                          name={`userDetails.${index}.userType`}
                          control={control}
                          render={({ field }) => (
                            <Select 
                              {...field} 
                              isMulti 
                              options={typeOptions} 
                              placeholder="Select User Types"
                              classNamePrefix="react-select"
                            />
                          )}
                        />
                      </td>
                      <td>
                        <Controller
                          name={`userDetails.${index}.user`}
                          control={control}
                          render={({ field }) => (
                            <Select 
                              {...field} 
                              isMulti 
                              options={userOptions} 
                              placeholder="Select Users"
                              classNamePrefix="react-select"
                            />
                          )}
                        />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>

            {/* BUTTONS */}
            <div className="form-post-buttons">
              <button 
                type="submit" 
                className="btn btn-primary"
                disabled={loading}
              >
                {loading ? "Updating..." : "Update"}
              </button>

              <button
                type="button"
                className="btn btn-white"
                onClick={() => navigate(-1)}
                disabled={loading}
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
