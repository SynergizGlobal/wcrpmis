import React, { useState, useEffect } from "react";
import { useForm, Controller, useFieldArray } from "react-hook-form";
import Select from "react-select";
import styles from './P6NewData.module.css';
import { CirclePlus } from "lucide-react";
import { LuCloudDownload } from "react-icons/lu";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom"; 
import { API_BASE_URL } from "../../../config";

import { RiAttachment2 } from 'react-icons/ri';

export default function P6NewData() {
  const location = useLocation();
  const navigate = useNavigate();
  const [projects, setProjects] = useState([]);
  const [filters, setFilters] = useState({
    contract: "",
    dataType: "",
    status: "",
  });

  const {
      register,
      control,
      handleSubmit,
      setValue,
      watch,
      formState: { errors },
    } = useForm({
      defaultValues: {
        project_id_fk: "",
        contract_id_fk: "",
        data_date: "",
        p6dataFile: "",
        p6dataFile2: "",
        p6dataFile3: "",
      },
    });



  const handleFilterChange = (e) => {
    const { name, value } = e.target;
    setFilters({ ...filters, [name]: value });
  };

  const handleBaselineDownload = () => {
    const fileUrl = "/files/landacquisition/P6BaselineFile.xlsx"; // or any URL
    const fileName = "P6BaselineFile.xlsx";

    const link = document.createElement("a");
    link.href = fileUrl;
    link.setAttribute("download", fileName);
    document.body.appendChild(link);
    link.click();
    link.remove();
  };

  const handleRevisedBaselineDownload = () =>{
    const fileUrl = "/files/landacquisition/P6RevisedFile.xlsx"; // or any URL
    const fileName = "P6RevisedFile.xlsx";

    const link = document.createElement("a");
    link.href = fileUrl;
    link.setAttribute("download", fileName);
    document.body.appendChild(link);
    link.click();
    link.remove();
  }

  const handleUpdateDownload = () =>{
    const fileUrl = "/files/landacquisition/P6UpdateFile.xlsx"; // or any URL
    const fileName = "P6UpdateFile.xlsx";

    const link = document.createElement("a");
    link.href = fileUrl;
    link.setAttribute("download", fileName);
    document.body.appendChild(link);
    link.click();
    link.remove();
  }

  return (
    <div className={styles.container}>
      {/* Top Bar */}
  
      <div className="pageHeading">
        <h2>P6 New Data</h2>
        <div  className="rightBtns">
          {/* <button className="btn btn-primary" onClick={handleAdd}>
            <CirclePlus size={16} /> Add
          </button>
          <button className="btn btn-primary">
            <LuCloudDownload size={16} /> Export
          </button> */}
          &nbsp;
        </div>
      </div>

          <div className={styles.p6FormCards}>
            <div className={styles.p6FormCardsInner}>

              <div className={styles.p6Card}>
                <div className={styles.p6CardHeading}>
                  <h3>Baseline</h3>
                </div>
                <div className={styles.p6CardBody}>
                  <form encType="multipart/form-data">
                    <div className="form-row flex-2">
                      <div className="form-field">
                          <label>Project <span className="red">*</span></label>
                          <Controller
                            name="project_id_fk"
                            control={control}
                            render={({ field }) => (
                              <Select
                                {...field}
                                classNamePrefix="react-select"
                                placeholder="Select Value"
                                isSearchable
                              />
                            )}
                          />
                        </div>
                        <div className="form-field">
                          <label>Contract</label>
                          <Controller
                            name="contract_id_fk"
                            rules={{ required: true }}
                            control={control}
                            render={({ field }) => (
                              <Select
                                {...field}
                                classNamePrefix="react-select"
                                placeholder="Select Date"
                                isSearchable
                              />
                            )}
                          />
                          {errors.contract_id_fk && <span className="text-danger">Required</span>}
                        </div>
                        <div className="form-field">
                          <label>Data Date <span className="red">*</span></label>
                          <input {...register("data_date", {required: true})} type="date" placeholder="Select Date" />
                          {errors.project_name && <span className="text-danger">Required</span>}
                        </div>
                        <div className="form-field">
                          <div className={`${styles["file-upload-wrapper"]} ${styles.fileUpload}`}>
                            <label htmlFor={`Cardfile-1`}  className={styles["file-upload-label-icon"]}>
                              <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
                              Upload P6 Export File <span className="red">*</span> 
                            </label>
                            <input
                              id={`Cardfile-1`}
                              type="file"
                              {...register("p6dataFile")}
                              className={styles["file-upload-input"]}
                            />
                            {(
                              watch("p6dataFile")?.[0]?.name ||
                              watch("p6dataFileNames")
                            ) && (
                              <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                {watch("p6dataFile")?.[0]?.name ||
                                watch("p6dataFileNames")}
                              </p>
                            )}
                          </div>
                        </div>
                    </div>
                  </form>
                  <div className="form-post-buttons">
                    <button type="submit" className="btn btn-primary">
                      Upload
                    </button> 
                  </div>
                <p>Note : Please make sure the uploading P6 data file will be in the given format. Click <button className={styles.btnNormal} onClick={handleBaselineDownload}>here</button> for the file format</p>
                </div>
              </div>

              <div className={styles.p6Card}>
                <div className={styles.p6CardHeading}>
                  <h3>Revised Baseline</h3>
                </div>
                <div className={styles.p6CardBody}>
                  <form encType="multipart/form-data">
                    <div className="form-row flex-2">
                      <div className="form-field">
                          <label>Project <span className="red">*</span></label>
                          <Controller
                            name="project_id_fk"
                            control={control}
                            render={({ field }) => (
                              <Select
                                {...field}
                                classNamePrefix="react-select"
                                placeholder="Select Value"
                                isSearchable
                              />
                            )}
                          />
                        </div>
                        <div className="form-field">
                          <label>Contract</label>
                          <Controller
                            name="contract_id_fk"
                            rules={{ required: true }}
                            control={control}
                            render={({ field }) => (
                              <Select
                                {...field}
                                classNamePrefix="react-select"
                                placeholder="Select Date"
                                isSearchable
                              />
                            )}
                          />
                          {errors.contract_id_fk && <span className="text-danger">Required</span>}
                        </div>
                        <div className="form-field">
                          <label>Data Date <span className="red">*</span></label>
                          <input {...register("data_date", {required: true})} type="date" placeholder="Select Date" />
                          {errors.project_name && <span className="text-danger">Required</span>}
                        </div>
                        <div className="form-field">
                          <div className={`${styles["file-upload-wrapper"]} ${styles.fileUpload}`}>
                            <label htmlFor={`Cardfile-2`}  className={styles["file-upload-label-icon"]}>
                              <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
                              Upload P6 Export File <span className="red">*</span>
                            </label>
                            <input
                              id={`Cardfile-2`}
                              type="file"
                              {...register("p6dataFile2")}
                              className={styles["file-upload-input"]}
                            />
                            {(
                              watch("p6dataFile2")?.[0]?.name ||
                              watch("p6dataFile2Names")
                            ) && (
                              <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                {watch("p6dataFile2")?.[0]?.name ||
                                watch("p6dataFile2Names")}
                              </p>
                            )}
                          </div>
                        </div>
                    </div>
                  </form>
                  <div className="form-post-buttons">
                    <button type="submit" className="btn btn-primary">
                      Update
                    </button> 
                  </div>
                <p>Note : Please make sure the uploading P6 data file will be in the given format. Click <button className={styles.btnNormal} onClick={handleRevisedBaselineDownload}>here</button> for the file format</p>
                </div>
              </div>

              <div className={styles.p6Card}>
                <div className={styles.p6CardHeading}>
                  <h3>Update</h3>
                </div>
                <div className={styles.p6CardBody}>
                  <form encType="multipart/form-data">
                    <div className="form-row flex-2">
                      <div className="form-field">
                          <label>Project <span className="red">*</span></label>
                          <Controller
                            name="project_id_fk"
                            control={control}
                            render={({ field }) => (
                              <Select
                                {...field}
                                classNamePrefix="react-select"
                                placeholder="Select Value"
                                isSearchable
                              />
                            )}
                          />
                        </div>
                        <div className="form-field">
                          <label>Contract</label>
                          <Controller
                            name="contract_id_fk"
                            rules={{ required: true }}
                            control={control}
                            render={({ field }) => (
                              <Select
                                {...field}
                                classNamePrefix="react-select"
                                placeholder="Select value"
                                isSearchable
                              />
                            )}
                          />
                          {errors.contract_id_fk && <span className="text-danger">Required</span>}
                        </div>
                        <div className="form-field">
                          <label>Data Date <span className="red">*</span></label>
                          <input {...register("data_date", {required: true})} type="date" placeholder="Select Date" />
                          {errors.project_name && <span className="text-danger">Required</span>}
                        </div>
                        <div className="form-field">
                          <div className={`${styles["file-upload-wrapper"]} ${styles.fileUpload}`}>
                            <label htmlFor={`Cardfile-3`}  className={styles["file-upload-label-icon"]}>
                              <RiAttachment2 size={20} style={{ marginRight: "6px" }} />
                              Upload File
                            </label>
                            <input
                              id={`Cardfile-3`}
                              type="file"
                              {...register("p6dataFile3")}
                              className={styles["file-upload-input"]}
                            />
                            {(
                              watch("p6dataFile3")?.[0]?.name ||
                              watch("p6dataFile3Names")
                            ) && (
                              <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
                                {watch("p6dataFile3")?.[0]?.name ||
                                watch("p6dataFile3Names")}
                              </p>
                            )}
                          </div>
                        </div>
                    </div>
                  </form>
                  <div className="form-post-buttons">
                    <button type="submit" className="btn btn-primary">
                      Upload
                    </button> 
                  </div>
                <p>Note : Please make sure the uploading P6 data file will be in the given format. Click <button className={styles.btnNormal} onClick={handleUpdateDownload}>here</button> for the file format</p>
                </div>
              </div>

            </div>
          </div>
    
      
      {/* Filters */}
     
        
        <div className="innerPage">
          <br />
          <div className="d-flex justify-content-center align-items-center">
            <h3>P6 DATA HISTORY</h3>
          </div>
          
          <div className="filterRow">
            {Object.keys(filters).map((key) => {
              const options = [
                { value: "", label: `Select ${key}` },
                { value: "demo1", label: `${key} 1` },
                { value: "demo2", label: `${key} 2` },
              ];

              return (
                <div className="filterOptions" key={key}>
                  <Select
                    options={options}
                    value={options.find((opt) => opt.value === filters[key])}
                    onChange={(selectedOption) =>
                      handleFilterChange({
                        target: { name: key, value: selectedOption.value },
                      })
                    }
                    placeholder={`Select ${key}`}
                    isSearchable
                    styles={{
                      control: (base) => ({
                        ...base,
                        minHeight: "32px",
                        borderColor: "#ced4da",
                        boxShadow: "none",
                        "&:hover": { borderColor: "#86b7fe" },
                      }),
                      dropdownIndicator: (base) => ({
                        ...base,
                        padding: "2px 6px",
                      }),
                      valueContainer: (base) => ({
                        ...base,
                        padding: "0 6px",
                      }),
                      menu: (base) => ({
                        ...base,
                        zIndex: 9999, // prevents clipping inside modals or cards
                      }),
                    }}
                  />
                </div>
              );
            })}
            <button className="btn btn-2 btn-primary">Clear Filters</button>
          </div>
          <div className={`dataTable ${styles.tableWrapper}`}>
            <table className={styles.projectTable}>
              <thead>
                <tr>
                  <th>Contract ID</th>
                  <th>Data Type</th>
                  <th>Data Date</th>
                  <th>Status</th>
                  <th>Uploaded File</th>
                  <th>Uploaded By</th>
                  <th>Uploaded Date</th>
                </tr>
              </thead>
              <tbody>
                {projects.length > 0 ? (
                  projects.map((proj, index) => (
                    <tr key={index}>
                      <td>{proj.projectId}</td>
                      <td>{proj.projectName}</td>
                      <td>{proj.projectStatus}</td>
                      <td>{proj.sanctionedYear}</td>
                      <td>{proj.division}</td>
                      <td>{proj.section}</td>
                      <td>{proj.remarks}</td>
                    </tr>
                  ))
                ) : (
                  <tr>
                    <td colSpan="8" style={{ textAlign: "center" }}>
                      No records found
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
    
      <Outlet />
    </div>
  );
}