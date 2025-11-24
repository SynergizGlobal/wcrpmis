	import React, { useContext, useEffect, useState} from "react";
	import { RefreshContext } from "../../../../context/RefreshContext";
	import { useForm, useFieldArray, Controller } from "react-hook-form";
	import Select from "react-select";
	import { useNavigate, useLocation } from "react-router-dom";
	import api from "../../../../api/axiosInstance";
	import styles from "./AddStructureForm.module.css";
	import { API_BASE_URL } from "../../../../config";
	
	export default function StructureForm() {
		 const location = useLocation();
	  const navigate = useNavigate();
	  const { state } = useLocation();
	  const structureData = state?.structureData || null;
	  const isEdit = Boolean(structureData);
	  const [allTypes, setAllTypes] = useState([]);
	  const [expandedIndex, setExpandedIndex] = useState(null);

	    const { refresh } = useContext(RefreshContext);
	  
	  const [projectOptions, setProjectOptions] = useState([]);
	
	
	  const { control, handleSubmit, watch, setValue } = useForm({
		defaultValues: {
		      project: "",
		      structureTypes: [
		        {
		          type: "",
		          rows: [{ structureId: "", structureName: "" }]
		        }
		      ]
		    }
	  });
	  
	  const { fields: typeFields, append: addType, remove: removeType } =
	    useFieldArray({
	      control,
	      name: "structureTypes"
	    });
	
	  const project = watch("project");
	  
	  useEffect(() => {
	    if (isEdit && structureData) {
	
	      setValue("project", {
	        value: structureData.projectId,
	        label: `${structureData.projectId} - ${structureData.projectName}`,
	      });
	
	      setValue("structureTypes", structureData.structureTypes);
	    }
	  }, [isEdit, structureData, refresh, location]);
	
	  useEffect(() => {
	    if (state?.typeOptions) {
	      setAllTypes(state.typeOptions);
	    }
	  }, [state, refresh, location]);
	  
	  useEffect(() => {
	    api.get(`${API_BASE_URL}/structures/types`)
	      .then(res => {
	        const options = res.data.map(t => ({ label: t, value: t }));
	        setAllTypes(options);
	      })
	      .catch(err => console.error("Error loading types:", err));
	  }, [refresh, location]);
	  
	  
	  useEffect(() => {
	    api.get(`${API_BASE_URL}/projects/api/getProjects`)
	      .then(res => {
	        const options = res.data.map(p => ({
	          value: p.project_id,
	          label: `${p.project_id} - ${p.project_name}`
	        }));
	        setProjectOptions(options);
	      })
	      .catch(err => console.error("Project Load Error:", err));
	  }, [refresh, location]);
	
	  const onSubmit = async (data) => {
	    try {
	      const payload = {
	        project: data.project.value,
	        structureTypes: data.structureTypes.map(t => ({
	          type: t.type?.value || t.type,       // <-- FIX HERE
	          rows: t.rows
	        }))
	      };

	      await api.post(`${API_BASE_URL}/structures/saveOrUpdate`, payload);

	      alert("Saved successfully!");
	      navigate("/updateforms/Structure");

	    } catch (err) {
	      console.error("SAVE ERROR:", err);
	      alert("Error saving structure");
	    }
	  };
	
	
	   return (
		<div className={`${styles.container} container-padding`}>
	   <div className="card">
	     <div className="formHeading">
	       <h2 className="center-align ps-relative">
	         {isEdit ? "Update Structure" : "Add Structure"}
	       </h2>
	     </div>
				 
	       <div className="innerPage">
	        <form onSubmit={handleSubmit(onSubmit)}>
	          
	          {/* PROJECT */}
	          <div className="form-row">
	            <div className="form-field">
	              <label>
	                Project <span className="red">*</span>
	              </label>
	
	              <Controller
	                name="project"
	                control={control}
	                rules={{ required: true }}
	                render={({ field }) => (
						<Select
						  {...field}
						  placeholder="Select Project"
						  options={projectOptions}
						/>
	                )}
	              />
	            </div>
	          </div>
	
	          {/* SHOW STRUCTURE TYPES ONLY AFTER PROJECT SELECT */}
	          {project && (
	            <div className={styles.typeBox}>
	              {typeFields.map((type, typeIndex) => (
	                <div key={type.id} className={styles.typeRow}>
	                  
	                  {/* STRUCTURE TYPE DROPDOWN */}
	                  <label>Structure Type :</label>
					  <div className="d-flex w-100 gap-20 align-center">
	
					  {isEdit && type.type !== "" ? (
					    <input
					      type="text"
					      value={type.type}
					      readOnly
						  onClick={() => setExpandedIndex(typeIndex)}
					      className={`w-100 ${styles.readonlyInput}`}
					       style={{ background: "#f5f5f5", fontWeight: "600", cursor: "pointer" }}
					    />
					  ) : (
					    // NEW structure type → show dropdown
						<Controller
						  name={`structureTypes.${typeIndex}.type`}
						  control={control}
						  render={({ field }) => (
						    <Select
						      {...field}
						      options={allTypes}
						      placeholder="Select Structure Type"
						      onChange={(val) => {
						        field.onChange(val);
						        setExpandedIndex(typeIndex);   // show table after selecting
						      }}
						    />
						  )}
						/>
					  )}
					   
		                {/* DELETE STRUCTURE TYPE BUTTON */}
						<button
						  type="button"
						  className={styles.deleteBtn}
						  onClick={async () => {
						    if (isEdit) {
						      const confirmDelete = window.confirm(
						        `Delete entire Structure Type (${type.type})?`
						      );

						      if (!confirmDelete) return;

						      await api.delete(`${API_BASE_URL}/structures/type`, {
						        params: {
						          projectId: structureData.projectId,
						          type: type.type
						        }
						      });
						    }

						    removeType(typeIndex);
						  }}
						>
						  X
						</button>
					  </div>	                  
	
	                  {/* SHOW NESTED ROWS ONLY WHEN STRUCTURE TYPE SELECTED */}
					  {expandedIndex === typeIndex && (
					      <NestedRows control={control} typeIndex={typeIndex} />
					  )}
	                </div>
	              ))}
	
	              {/* ADD STRUCTURE TYPE */}
				  <div className={styles.mainRowAddBtn}>
		              <button
		                type="button"
		                className={styles.addTypeBtn}
		                onClick={() =>
		                  addType({
		                    type: "",
		                    rows: [{ structureId: "", structureName: "" }],
		                  })
		                }
		              >
		                +
		              </button>
					</div>
	            </div>
	          )}
	
	          {/* FORM BUTTONS */}
	          <div className="form-post-buttons">
	            <button className="btn btn-primary" type="submit">
	               {isEdit ? "UPDATE" : "ADD"}
	            </button>
	            <button className="btn btn-white" type="button"
				onClick={() => navigate("/updateforms/AddStructure")}>
	              CANCEL
	            </button>
	          </div>
	        </form>
	      </div>
		  </div>
		  </div>
	    );
	  }
	
	
	  /* ----------------------------------
	    NESTED ROWS COMPONENT (MUST BE OUTSIDE)
	  ---------------------------------- */
	  function NestedRows({ control, typeIndex }) {
	    const { fields, append, remove } = useFieldArray({
	      control,
	      name: `structureTypes.${typeIndex}.rows`,
	    });
	
	    return (
	      <div className={styles.rowBox}>
	        <table className={styles.table}>
	          <thead>
	            <tr>
	              <th>Structure Id</th>
	              <th>Structure Name</th>
	              <th>Action</th>
	            </tr>
	          </thead>
	
	          <tbody>
	            {fields.map((row, rowIndex) => (
	              <tr key={row.id}>
	                <td>
	                  <input
	                    {...control.register(
	                      `structureTypes.${typeIndex}.rows.${rowIndex}.structureId`
	                    )}
	                    placeholder="Structure Id"
	                  />
	                </td>
	
	                <td>
	                  <input
	                    {...control.register(
	                      `structureTypes.${typeIndex}.rows.${rowIndex}.structureName`
	                    )}
	                    placeholder="Structure Name"
	                  />
	                </td>
	
	                <td>
					<button
					  type="button"
					  className={styles.deleteBtn}
					  onClick={async () => {
					    const rowData = fields[rowIndex];
	
					    if (rowData.structureId) {
					      // backend delete
					      await api.delete(`${API_BASE_URL}/structures/${rowData.structureId}`);
					    }
	
					    remove(rowIndex);
					  }}
					>
					  X
					</button>
	                </td>
	              </tr>
	            ))}
	          </tbody>
	        </table>
			<br />
			<div className={styles.mainRowAddBtn}>
				<button
		            type="button"
		            className={styles.addBtn}
		            onClick={() =>
		              append({ structureId: "", structureName: "" })
		            }
		          >
		            +
		          </button>
			</div>
	      </div>
	    );
	  }
