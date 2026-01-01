import React, { useState, useEffect, useCallback, useMemo, useContext } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
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
	const { refresh } = useContext(RefreshContext);

	const [p6DataList, setP6DataList] = useState([]);

	const [listLoading, setListLoading] = useState(false);
	const [searchText, setSearchText] = useState("");
	const [page, setPage] = useState(1);
	const [pageSize, setPageSize] = useState(10);


	const [projectList, setProjectsList] = useState([]);
	
	const [baselineContracts, setBaselineContracts] = useState([]);
	const [revisedContracts, setRevisedContracts] = useState([]);
	const [updateContracts, setUpdateContracts] = useState([]);

	const [baselineFobs, setBaselineFobs] = useState([]);
	const [revisedFobs, setRevisedFobs] = useState([]);
	const [updateFobs, setUpdateFobs] = useState([]);
	

	const [xerErrors, setXerErrors] = useState("");
	const [parsedXerRows, setParsedXerRows] = useState([]);

	const baselineForm = useForm({
	  defaultValues: {
	    baseline: {
	      project_id_fk: "",
	      contract_id_fk: "",
	      fob_id_fk: "",
	      data_date: "",
	      p6dataFile: null,
	    },
	  },
	});
	const revisedForm = useForm({
	  defaultValues: {
	    revised: {
	      project_id_fk: "",
	      contract_id_fk: "",
	      fob_id_fk: "",
	      data_date: "",
	      p6dataFile: null,
	    },
	  },
	});
	const updateForm = useForm({
	  defaultValues: {
	    update: {
	      project_id_fk: "",
	      contract_id_fk: "",
	      fob_id_fk: "",
	      data_date: "",
	      p6dataFile: null,
	    },
	  },
	});


	const {
	  register: baselineRegister,
	  control: baselineControl,
	  handleSubmit: handleBaselineSubmit,
	  setValue: baselineSetValue,
	  watch: baselineWatch,
	  formState: { errors: baselineErrors },
	} = baselineForm;

	const {
	  register: revisedRegister,
	  control: revisedControl,
	  handleSubmit: handleRevisedSubmit,
	  setValue: revisedSetValue,
	  watch: revisedWatch,
	  formState: { errors: revisedErrors },
	} = revisedForm;

	
	const {
	  register: updateRegister,
	  control: updateControl,
	  handleSubmit: handleUpdateSubmit,
	  setValue: updateSetValue,
	  watch: updateWatch,
	  formState: { errors: updateErrors },
	} = updateForm;


	const [filters, setFilters] = useState({
		contract: "",
		dataType: "",
		status: "",
	});

	const [filterOptions, setFilterOptions] = useState({
		contract: [],
		dataType: [],
		status: [],
	});

	const toOptions = (list, valueKey, labelKeys = []) =>
		list.map(item => ({
			value: item[valueKey],
			label: labelKeys
				.map(k => item[k])
				.filter(Boolean)
				.join(" - ")
		}));


	const projectOptions = toOptions(
		projectList,
		"project_id_fk",
		["project_id_fk", "project_name"]
	);

	const baselineContractsOptions = toOptions(
		baselineContracts,
		"contract_id",
		["contract_id", "contract_short_name"]
	);

	const revisedContractsOptions = toOptions(
		revisedContracts,
		"contract_id",
		["contract_id", "contract_short_name"]
	);
	const updateContractsOptions = toOptions(
		updateContracts,
		"contract_id",
		["contract_id", "contract_short_name"]
	);
	
	
	const baselineFobsOptions = toOptions(
		baselineFobs,
		"fob_id",
		["fob_id", "fob_name"]
	);

	const revisedFobsOptions = toOptions(
		revisedFobs,
		"fob_id",
		["fob_id", "fob_name"]
	);
	const updateFobsOptions = toOptions(
		updateFobs,
		"fob_id",
		["fob_id", "fob_name"]
	);



	


	const fetchProjectList = useCallback(async () => {
		try {
			const res = await api.post(
				`${API_BASE_URL}/p6-new-data-new`,
				null, // no body
				{ withCredentials: true }
			);

			console.log("Projects:", res.data.projectsList);
			console.log("Contracts:", res.data.contractsList);

			setProjectsList(
				Array.isArray(res.data.projectsList) ? res.data.projectsList : []
			);

			setBaselineContracts(
				Array.isArray(res.data.contractsList) ? res.data.contractsList : []
			);
			setRevisedContracts(
				Array.isArray(res.data.contractsList) ? res.data.contractsList : []
			);
			setUpdateContracts(
				Array.isArray(res.data.contractsList) ? res.data.contractsList : []
			);

		} catch (err) {
			console.error("Error fetching P6 Data list:", err);
		}
	}, [location, refresh]);




	const fetchContracts = async (projectId, setContracts) => {
	  if (!projectId) {
	    setContracts([]);
	    return;
	  }

	  try {
	    const res = await api.post(
	      `${API_BASE_URL}/ajax/getContractListInP6New`,
	      null,
	      {
	        params: { project_id_fk: projectId },
	        withCredentials: true
	      }
	    );

	    setContracts(Array.isArray(res.data) ? res.data : []);
	  } catch (err) {
	    console.error("Error fetching contracts:", err);
	    setContracts([]);
	  }
	};


	
	
	const fetchFobByContract = async (contractId, setFobs) => {
	  if (!contractId) {
	    setFobs([]);
	    return;
	  }

	  try {
	    const res = await api.post(
	      `${API_BASE_URL}/ajax/getFobListInP6New`,
	      null,
	      {
	        params: { contract_id_fk: contractId },
	        withCredentials: true
	      }
	    );

	    setFobs(Array.isArray(res.data) ? res.data : []);
	  } catch (err) {
	    console.error("Error fetching FOB:", err);
	    setFobs([]);
	  }
	};




	const fetchP6DataList = useCallback(async () => {
		setListLoading(true);

		try {
			const body = {
				contract_id_fk: filters.contract || "",
				upload_type: filters.dataType || "",
				status_fk: filters.status || "",
			};

			const res = await api.post(
				`${API_BASE_URL}/ajax/getP6NewActivityData`,
				body,
				{ withCredentials: true }
			);

			setP6DataList(Array.isArray(res.data) ? res.data : []);
		} catch (err) {
			console.error("Error fetching P6 Data list:", err);
			setP6DataList([]);
		} finally {
			setListLoading(false);
		}
	}, [filters,location, refresh]);



	const filteredData = useMemo(() => {
		if (!searchText) return p6DataList;

		const search = searchText.toLowerCase();

		return p6DataList.filter((row) => {
			return (
				row.contract_id_fk?.toLowerCase().includes(search) ||
				row.upload_type?.toLowerCase().includes(search) ||
				row.data_date?.toLowerCase().includes(search) ||
				row.soft_delete_status_fk?.toLowerCase().includes(search) ||
				row.soft_delete_status_fk?.toLowerCase().includes(search) ||
				row.uploaded_by_user_id_fk?.toLowerCase().includes(search) ||
				row.uploaded_date?.toLowerCase().includes(search)
			);
		});
	}, [p6DataList, searchText]);
	
	const total = filteredData.length;
	const pages = Math.ceil(total / pageSize);

	const paginatedData = useMemo(() => {
		const start = (page - 1) * pageSize;
		return filteredData.slice(start, start + pageSize);
	}, [filteredData, page, pageSize]);


	// 1️⃣ Reset page when filters / tab / page size change
	useEffect(() => {
		setPage(1);
	}, [filters, pageSize]);

	useEffect(() => {
		fetchP6DataList();
		fetchProjectList();
	}, [filters, fetchP6DataList,location,refresh]);

	useEffect(() => {
		setPage(1);
	}, [searchText]);


	const loadContractOptions = async (selected = "") => {
		if (filters.contract !== "") return;

		const params = {
		};

		const res = await api.post(
			`${API_BASE_URL}/ajax/getContractsListFilterInP6New`,
			params
		);

		const data = res.data || [];

		setFilterOptions(prev => ({
			...prev,
			contract: data.map(val => ({
				value: val.contract_id,
				label: val.contract_id + " - " + val.contract_short_name,
			})),
		}));
	};





	const loadDataTypeOptions = async (selected = "") => {
		if (filters.dataType !== "") return;


		const params = {
		};
		const res = await api.post(
			`${API_BASE_URL}/ajax/getUploadTypesFilterInP6New`,
			params
		);

		const data = res.data || [];

		setFilterOptions(prev => ({
			...prev,
			dataType: data.map(val => ({
				value: val.upload_type,
				label: val.upload_type,
			})),
		}));
	};




	const loadStatusListOptions = async (selected = "") => {
		if (filters.status !== "") return;


		const params = {
		};
		const res = await api.post(
			`${API_BASE_URL}/ajax/getStatusListFilterInP6New`,
			params
		);

		const data = res.data || [];

		setFilterOptions(prev => ({
			...prev,
			status: data.map(val => ({
				value: val.soft_delete_status_fk,
				label: val.soft_delete_status_fk,
			})),
		}));
	};


	useEffect(() => {
		loadContractOptions();
		loadDataTypeOptions();
		loadStatusListOptions();
		fetchP6DataList();
	}, [location, refresh]);



	const clearAllFiltersAndSearch = () => {
		setFilters({ contract: "", dataType: "", status: "" });
		setSearchText("");
	};


	const handleFilterChange = async (name, value) => {
		let updated = { ...filters, [name]: value };

		if (name === "contract") {
			updated.dataType = "";
			updated.status = "";
		}

		if (name === "dataType") {
			updated.status = "";
		}

		setFilters(updated);
	};

	useEffect(() => {
		fetchP6DataList();
	}, [filters, fetchP6DataList, location, refresh]);


	const handleBaselineDownload = () => {
		const fileUrl = "/files/p6-new-data/P6BaselineFile.xlsx"; 
		const fileName = "P6BaselineFile.xlsx";

		const link = document.createElement("a");
		link.href = fileUrl;
		link.setAttribute("download", fileName);
		document.body.appendChild(link);
		link.click();
		link.remove();
	};

	const handleRevisedBaselineDownload = () => {
		const fileUrl = "/files/p6-new-data/P6RevisedFile.xlsx"; 
		const fileName = "P6RevisedFile.xlsx";

		const link = document.createElement("a");
		link.href = fileUrl;
		link.setAttribute("download", fileName);
		document.body.appendChild(link);
		link.click();
		link.remove();
	}

	const handleUpdateDownload = () => {
		const fileUrl = "/files/p6-new-data/P6UpdateFile.xlsx";
		const fileName = "P6UpdateFile.xlsx";

		const link = document.createElement("a");
		link.href = fileUrl;
		link.setAttribute("download", fileName);
		document.body.appendChild(link);
		link.click();
		link.remove();
	}

	const renderPageButtons = (page, totalPages, setPageFn) => {
		if (totalPages <= 1) return null;

		const pages = Array.from({ length: totalPages }, (_, i) => i + 1)
			.filter((p) => {
				if (p <= 2 || p > totalPages - 2) return true;
				if (p >= page - 1 && p <= page + 1) return true;
				return false;
			})
			.reduce((acc, p, idx, arr) => {
				if (idx > 0 && p - arr[idx - 1] > 1) acc.push("...");
				acc.push(p);
				return acc;
			}, []);

		return (
			<div style={{ display: "inline-flex", alignItems: "center", gap: 4 }}>
				<button
					disabled={page === 1}
					onClick={() => setPageFn(page - 1)}
					className="pageBtn"
				>
					‹
				</button>

				{pages.map((item, idx) =>
					item === "..." ? (
						<span key={`ellipsis-${idx}`} style={{ padding: "0 6px" }}>
							...
						</span>
					) : (
						<button
							key={`page-${item}`}
							onClick={() => setPageFn(item)}
							className={`pageBtn ${item === page ? "activePage" : ""}`}
						>
							{item}
						</button>
					)
				)}

				<button
					disabled={page === totalPages}
					onClick={() => setPageFn(page + 1)}
					className="pageBtn"
				>
					›
				</button>
			</div>
		);
	};
	
	const isValidDate = (dateStr) => {
	  const regex = /^\d{2}-\d{2}-\d{4}$/;
	  return regex.test(dateStr);
	};


	const processXERFile = (data) => {
	  const lines = data.split("\n");

	  const header =
	    "%F\ttask_id\tproj_id\twbs_id\tclndr_id\tphys_complete_pct\t" +
	    "rev_fdbk_flag\test_wt\tlock_plan_flag\tauto_compute_act_flag\t" +
	    "complete_pct_type\ttask_type\tduration_type\tstatus_code\t" +
	    "task_code\ttask_name\trsrc_id\ttotal_float_hr_cnt\tfree_float_hr_cnt\t" +
	    "remain_drtn_hr_cnt\tact_work_qty\tremain_work_qty\t" +
	    "target_work_qty\ttarget_drtn_hr_cnt\ttarget_equip_qty\t" +
	    "act_equip_qty\tremain_equip_qty\tcstr_date\tact_start_date\t" +
	    "act_end_date\tlate_start_date\tlate_end_date\texpect_end_date\t" +
	    "early_start_date\tearly_end_date\trestart_date\treend_date\t" +
	    "target_start_date\ttarget_end_date\trem_late_start_date\trem_late_end_date\t" +
	    "cstr_type\tpriority_type\tsuspend_date\tresume_date\tfloat_path\t" +
	    "float_path_order\tguid\ttmpl_guid\tcstr_date2\tcstr_type2\t" +
	    "driving_path_flag\tact_this_per_work_qty\tact_this_per_equip_qty\t" +
	    "external_early_start_date\texternal_late_end_date\tcreate_date\t" +
	    "update_date\tcreate_user\tupdate_user\tlocation_id\tcrt_path_num";

	  const headerColumns = header.split("\t");
	  const rows = [];
	  let errorText = "";

	  for (let i = 0; i < lines.length; i++) {
	    const line = lines[i];

	    if (line.includes(header)) continue;

	    if (line.startsWith("%R")) {
	      const columns = line.split("\t");
	      const rowMap = {};

	      headerColumns.forEach((col, idx) => {
	        rowMap[col] = columns[idx] || "";
	      });

	      const dateColumns = [
	        "base_start_date",
	        "base_end_date",
	        "start_date",
	        "end_date"
	      ];

	      dateColumns.forEach((dateCol) => {
	        if (rowMap[dateCol] && !isValidDate(rowMap[dateCol])) {
	          errorText += `Row ${i + 1}: ${dateCol.replace(
	            "_",
	            " "
	          )} should be dd-mm-yyyy format\n`;
	        }
	      });

	      rows.push(rowMap);
	    }

	    if (line.startsWith("%T\tTASKPRED")) break;
	  }

	  setXerErrors(errorText);     
	  setParsedXerRows(rows);     

	  if (!errorText) {
	    console.log("XER validated successfully");
	    submitP6Baseline();        
	  }
	};
	
	
	const submitP6Baseline = async () => {
	  try {
	    const formData = new FormData();

	    formData.append(
	      "project_id_fk",
	      baselineWatch("baseline.project_id_fk")?.value
	    );
	    formData.append(
	      "contract_id_fk",
	      baselineWatch("baseline.contract_id_fk")?.value
	    );
	    formData.append(
	      "data_date",
	      baselineWatch("baseline.data_date")
	    );
		const appendIfPresent = (fd, key, value) => {
		  if (value != null && value !== "") {
		    fd.append(key, value);
		  }
		};

		appendIfPresent(formData, "fob_id_fk", revisedWatch("baseline.fob_id_fk")?.value);





	    const file = baselineWatch("baseline.p6dataFile")?.[0];
	    if (!file) {
	      alert("Please select a File!");
	      return;
	    }
	    formData.append("p6dataFile", file);

	    const res = await api.post(
	      `${API_BASE_URL}/upload-p6-new-data`,
	      formData,
	      { withCredentials: true }   
	    );

	    console.log(res.data);
	    alert(JSON.stringify(res.data).replace("{","").replace("}",""));
		fetchP6DataList();
	  } catch (err) {
	    console.error("Upload failed:", err);
	    alert("Upload failed. Please check server logs.");
	  }
	};
	
	
	const submitP6RevisedBaseline = async () => {
		  try {
		    const formData = new FormData();

		    formData.append(
		      "project_id_fk",
		      revisedWatch("revised.project_id_fk")?.value
		    );
		    formData.append(
		      "contract_id_fk",
		      revisedWatch("revised.contract_id_fk")?.value
		    );
		    formData.append(
		      "data_date",
		      revisedWatch("revised.data_date")
		    );
			formData.append(
			  "fob_id_fk",
			  revisedWatch("revised.fob_id_fk")?.value || ""
			);

		    const file = revisedWatch("revised.p6dataFile")?.[0];
		    if (!file) {
		      alert("Please select a file");
		      return;
		    }
		    formData.append("p6dataFile", file);

		    const res = await api.post(
		      `${API_BASE_URL}/revised-p6-new-activities`,
		      formData,
		      { withCredentials: true }   
		    );

		    console.log(res.data);
		    alert(JSON.stringify(res.data).replace("{","").replace("}",""));
			fetchP6DataList();
		  } catch (err) {
		    console.error("Upload failed:", err);
		    alert("Upload failed. Please check server logs.");
		  }
		};

		
		const submitP6Update = async () => {
			  try {
			    const formData = new FormData();

			    formData.append(
			      "project_id_fk",
			      updateWatch("update.project_id_fk")?.value
			    );
			    formData.append(
			      "contract_id_fk",
			      updateWatch("update.contract_id_fk")?.value
			    );
			    formData.append(
			      "data_date",
			      updateWatch("update.data_date")
			    );
				formData.append(
				  "fob_id_fk",
				  updateWatch("update.fob_id_fk")?.value || ""
				);
			    const file = updateWatch("update.p6dataFile")?.[0];
			    if (!file) {
			      alert("Please select a file");
			      return;
			    }
			    formData.append("p6dataFile", file);

			    const res = await api.post(
			      `${API_BASE_URL}/update-p6-new-activities`,
			      formData,
			      { withCredentials: true }   
			    );

			    console.log(res.data);
				
				
			    alert(JSON.stringify(res.data).replace("{", "").replace("}",""));
				
				fetchP6DataList();

			  } catch (err) {
			    console.error("Upload failed:", err);
			    alert("Upload failed. Please check server logs.");
			  }
			};
			
	

	return (
		<div className={styles.container}>
			{/* Top Bar */}

			<div className="pageHeading">
				<h2>P6 New Data</h2>
				<div className="rightBtns">
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
							<form encType="multipart/form-data" onSubmit={baselineForm.handleSubmit(submitP6Baseline)}>
								<div className="form-row flex-2">
									<div className="form-field">
										<label>Project <span className="red">*</span></label>
										<Controller
										  name="baseline.project_id_fk"
										  control={baselineForm.control}
										  render={({ field }) => (
										    <Select
										      options={projectOptions}
										      value={field.value}
										      onChange={(opt) => {
										        field.onChange(opt);
										        fetchContracts(opt?.value, setBaselineContracts);
										        baselineSetValue("baseline.contract_id_fk", null);
										      }}
										    />
										  )}
										/>

									</div>
									<div className="form-field">
									  <label>
									    Contract <span className="red">*</span>
									  </label>

									  <Controller
									    name="baseline.contract_id_fk"
									    rules={{ required: "This field is required*" }}
									    control={baselineForm.control}
									    render={({ field }) => (
									      <Select
									        options={baselineContractsOptions}
									        placeholder="Select Contract"
									        value={field.value}
									        onChange={(opt) => {
									          field.onChange(opt);
									          fetchFobByContract(opt?.value, setBaselineFobs);
									        }}
									      />
									    )}
									  />

									  {baselineErrors?.baseline?.contract_id_fk && (
									    <span className="red">
									      {baselineErrors.baseline.contract_id_fk.message}
									    </span>
									  )}
									</div>

									{baselineFobs.length > 0 && (
									  <div className="form-field">
									    <label>FOB <span className="red">*</span></label>
										<Controller
										  name="baseline.fob_id_fk"
										  control={baselineForm.control}
										  render={({ field }) => (
										    <Select
										      options={baselineFobsOptions}
										      placeholder="Select FOB"
										      value={field.value} 
										      onChange={(opt) => field.onChange(opt)}
										    />
										  )}
										/>
									  </div>
									)}

									
									<div className="form-field">
									  <label>
									    Data Date <span className="red">*</span>
									  </label>

									  <input
									    type="date"
									    placeholder="Select Date"
									    {...baselineRegister("baseline.data_date", {
									      required: "This field is required*"
									    })}
									  />

									  {baselineErrors?.baseline?.data_date && (
									    <span className="red">
									      {baselineErrors.baseline.data_date.message}
									    </span>
									  )}
									</div>

									<div className="form-field">
										<div className={`${styles["file-upload-wrapper"]} ${styles.fileUpload}`}>
											<label htmlFor={`Cardfile-1`} className={styles["file-upload-label-icon"]}>
												<RiAttachment2 size={20} style={{ marginRight: "6px" }} />
												Upload P6 Export File <span className="red">*</span>
											</label>
											<input
											  id="Cardfile-1"
											  type="file"
											  accept=".xer,.xls,.xlsx"
											  {...baselineRegister("baseline.p6dataFile", {
											    required: "P6 file is required",
											    validate: {
											      validType: (files) => {
											        const file = files?.[0];
											        if (!file) return true;
											        return (
											          /\.(xer|xls|xlsx)$/i.test(file.name) ||
											          "Only .xer, .xls or .xlsx files are allowed"
											        );
											      }
											    }
											  })}
											  className={styles["file-upload-input"]}
											/>

											{baselineWatch("baseline.p6dataFile")?.[0]?.name && (
											  <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
											    {baselineWatch("baseline.p6dataFile")[0].name}
											  </p>
											)}

											{baselineErrors?.baseline?.p6dataFile && (
											  <span className="red">
											    {baselineErrors.baseline.p6dataFile.message}
											  </span>
											)}

										</div>
									</div>
									
								</div>
								<div className="form-post-buttons">
									<button type="submit" className="btn btn-primary">
										Upload
									</button>
								</div>
							</form>
							
							<p>Note : Please make sure the uploading P6 data file will be in the given format. Click <button className={styles.btnNormal} onClick={handleBaselineDownload}>here</button> for the file format</p>
						</div>
					</div>

					<div className={styles.p6Card}>
						<div className={styles.p6CardHeading}>
							<h3>Revised Baseline</h3>
						</div>
						<div className={styles.p6CardBody}>
							<form encType="multipart/form-data" onSubmit={revisedForm.handleSubmit(submitP6RevisedBaseline)}>
								<div className="form-row flex-2">
									<div className="form-field">
										<label>Project <span className="red">*</span></label>
										<Controller
										  name="revised.project_id_fk"
										  control={revisedForm.control}
										  render={({ field }) => (
										    <Select
										      options={projectOptions}
										      value={field.value}
										      onChange={(opt) => {
										        field.onChange(opt);
										        fetchContracts(opt?.value, setRevisedContracts);
										        revisedSetValue("revised.contract_id_fk", null);
										      }}
										    />
										  )}
										/>


									</div>
									<div className="form-field">
									  <label>
									    Contract <span className="red">*</span>
									  </label>

									  <Controller
									    name="revised.contract_id_fk"
									    control={revisedForm.control}
									    rules={{ required: "This field is required." }}
									    render={({ field }) => (
									      <Select
									        options={revisedContractsOptions}
									        placeholder="Select Contract"
									        value={field.value}
									        onChange={(opt) => {
									          field.onChange(opt);
									          fetchFobByContract(opt?.value, setRevisedFobs);
									        }}
									      />
									    )}
									  />

									  {revisedErrors?.revised?.contract_id_fk && (
									    <span className="red">
									      {revisedErrors.revised.contract_id_fk.message}
									    </span>
									  )}
									</div>


									{revisedFobs.length > 0 && (
										<div className="form-field">
											<label>FOB <span className="red">*</span></label>
											<Controller
												name="revised.fob_id_fk"
												control={revisedForm.control}
												render={({ field }) => (
													<Select
														options={revisedFobsOptions}
														placeholder="Select FOB"
														value={field.value}
														onChange={(opt) => field.onChange(opt)}
													/>
												)}
											/>
										</div>
									)}


									<div className="form-field">
									  <label>
									    Data Date <span className="red">*</span>
									  </label>

									  <input
									    type="date"
									    placeholder="Select Date"
									    {...revisedRegister("baseline.data_date", {
									      required: "This field is required"
									    })}
									  />

									  {revisedErrors?.baseline?.data_date && (
									    <span className="red">
									      {revisedErrors.baseline.data_date.message}
									    </span>
									  )}
									</div>

									<div className="form-field">
										<div className={`${styles["file-upload-wrapper"]} ${styles.fileUpload}`}>
											<label htmlFor={`Cardfile-2`} className={styles["file-upload-label-icon"]}>
												<RiAttachment2 size={20} style={{ marginRight: "6px" }} />
												Upload P6 Export File <span className="red">*</span>
											</label>
											<input
											  id="Cardfile-2"
											  type="file"
											  accept=".xer,.xls,.xlsx"
											  onClick={(e) => (e.target.value = null)}
											  {...revisedRegister("revised.p6dataFile", {
											    required: "P6 file is required",
											    validate: {
											      validType: (files) => {
											        const file = files?.[0];
											        if (!file) return true;

											        return (
											          /\.(xer|xls|xlsx)$/i.test(file.name) ||
											          "Only .xer, .xls or .xlsx files are allowed"
											        );
											      }
											    }
											  })}
											  className={styles["file-upload-input"]}
											/>

											{revisedWatch("revised.p6dataFile")?.[0]?.name && (
											  <p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
											    {revisedWatch("revised.p6dataFile")[0].name}
											  </p>
											)}

											{revisedErrors?.revised?.p6dataFile && (
											  <span className="red">
											    {revisedErrors.revised.p6dataFile.message}
											  </span>
											)}

										</div>
									</div>
								</div>
								<div className="form-post-buttons">
									<button type="submit" className="btn btn-primary" >
										Update
									</button>
								</div>
							</form>

							<p>Note : Please make sure the uploading P6 data file will be in the given format. Click <button className={styles.btnNormal} onClick={handleRevisedBaselineDownload}>here</button> for the file format</p>
						</div>
					</div>

					<div className={styles.p6Card}>
						<div className={styles.p6CardHeading}>
							<h3>Update</h3>
						</div>
						<div className={styles.p6CardBody}>
							<form encType="multipart/form-data" onSubmit={updateForm.handleSubmit(submitP6Update)}>
								<div className="form-row flex-2">
									<div className="form-field">
										<label>Project <span className="red">*</span></label>
										<Controller
											name="update.project_id_fk"
											control={updateForm.control}
											render={({ field }) => (
												<Select
													options={projectOptions}
													value={field.value}
													onChange={(opt) => {
														field.onChange(opt);
														fetchContracts(opt?.value, setUpdateContracts);
														updateSetValue("update.contract_id_fk", null);
													}}
												/>
											)}
										/>
									</div>
									<div className="form-field">
									  <label>
									    Contract <span className="red">*</span>
									  </label>

									  <Controller
									    name="update.contract_id_fk"
									    control={updateForm.control}
									    rules={{ required: "This field is required." }}
									    render={({ field }) => (
									      <Select
									        options={updateContractsOptions}
									        placeholder="Select Contract"
									        value={field.value}
									        onChange={(opt) => {
									          field.onChange(opt);
									          fetchFobByContract(opt?.value, setUpdateFobs);
									        }}
									      />
									    )}
									  />

									  {updateErrors?.update?.contract_id_fk && (
									    <span className="red">
									      {updateErrors.update.contract_id_fk.message}
									    </span>
									  )}
									</div>


									{updateFobs.length > 0 && (
										<div className="form-field">
											<label>FOB <span className="red">*</span></label>
											<Controller
												name="update.fob_id_fk"
												control={updateControl}
												render={({ field }) => (
													<Select
														options={updateFobsOptions}
														placeholder="Select FOB"
														value={field.value}
														onChange={(opt) => field.onChange(opt)}
													/>
												)}
											/>
										</div>
									)}

									
									<div className="form-field">
									  <label>
									    Data Date <span className="red">*</span>
									  </label>

									  <input
									    type="date"
									    placeholder="Select Date"
									    {...updateRegister("baseline.data_date", {
									      required: "This field is required"
									    })}
									  />

									  {updateErrors?.baseline?.data_date && (
									    <span className="red">
									      {updateErrors.baseline.data_date.message}
									    </span>
									  )}
									</div>

									
									
									<div className="form-field">
										<div className={`${styles["file-upload-wrapper"]} ${styles.fileUpload}`}>
											<label htmlFor={`Cardfile-3`} className={styles["file-upload-label-icon"]}>
												<RiAttachment2 size={20} style={{ marginRight: "6px" }} />
												Upload File
											</label>
											<input
												id="Cardfile-3"
												type="file"
												accept=".xer,.xls,.xlsx"
												{...updateRegister("update.p6dataFile", {
													required: "P6 file is required",
													validate: {
														validType: (files) => {
															const file = files?.[0];
															if (!file) return true;
															return (
																/\.(xer|xls|xlsx)$/i.test(file.name) ||
																"Only .xer, .xls or .xlsx files are allowed"
															);
														}
													}
												})}
												className={styles["file-upload-input"]}
											/>

											{updateWatch("update.p6dataFile")?.[0]?.name && (
												<p style={{ marginTop: "6px", fontSize: "0.9rem", color: "#475569" }}>
													{updateWatch("update.p6dataFile")[0].name}
												</p>
											)}

											{updateErrors?.update?.p6dataFile && (
												<span className="red">
													{updateErrors.update.p6dataFile.message}
												</span>
											)}
										</div>
									</div>
								</div>
								<div className="form-post-buttons">
									<button type="submit" className="btn btn-primary">
										Upload
									</button>
								</div>
							</form>

							<p>Note : Please make sure the uploading P6 data file will be in the given format. Click <button className={styles.btnNormal} onClick={handleUpdateDownload}>here</button> for the file format</p>
						</div>
					</div>

				</div>
			</div>




			<div className="innerPage">
				<br />
				<div className="d-flex justify-content-center align-items-center">
					<h3>P6 DATA HISTORY</h3>
				</div>

				{/* Filters */}
				<div className={styles.filterRow}>
					{Object.keys(filters).map((key) => {
						const options = filterOptions[key] || [];
						// key mapping to friendly label
						const labelMap = {
							contract: "contract",
							dataType: "dataType",
							status: "status",
						};
						return (
							<div className="filterOptions" key={key} style={{ minWidth: 160 }}>
								<Select
									options={[{ value: "", label: `Select ${labelMap[key] || key}` }, ...options]}
									classNamePrefix="react-select"
									value={
										options.find((opt) => opt.value === filters[key]) || {
											value: "",
											label: `Select ${labelMap[key] || key}`,
										}
									}
									onChange={(selectedOption) =>
										handleFilterChange(key, selectedOption?.value || "")
									}
									placeholder={`Select ${labelMap[key] || key}`}
									isSearchable
									styles={{
										control: (base) => ({
											...base,
											minHeight: "32px",
											borderColor: "#ced4da",
											boxShadow: "none",
											"&:hover": { borderColor: "#86b7fe" },
										}),
										dropdownIndicator: (base) => ({ ...base, padding: "2px 6px" }),
										valueContainer: (base) => ({ ...base, padding: "0 6px" }),
										menu: (base) => ({ ...base, zIndex: 9999 }),
									}}
								/>
							</div>
						);
					})}

					<div style={{ display: "flex", alignItems: "center", gap: 8 }}>
						<button
							className="btn btn-2 btn-primary"
							type="button"
							onClick={clearAllFiltersAndSearch}
						>
							Clear Filter
						</button>
					</div>
				</div>

				{/* Show Entries + Search Row */}
				<div className={styles.tableTopRow} style={{ display: "flex", justifyContent: "space-between", marginTop: "10px" }}>

					{/* Show Entries */}
					<div className="showEntriesCount">
						<label>Show </label>
						<select
							value={pageSize}
							onChange={(e) => {
								setPageSize(Number(e.target.value));
								setPage(1);  // reset to first page
							}}
						>
							{[5, 10, 20, 50, 100].map((size) => (
								<option key={size} value={size}>{size}</option>
							))}
						</select>
						<span> entries</span>
					</div>

					{/* Search */}
					<div className={styles.searchWrapper}>
						<input
							type="text"
							placeholder="Search"
							value={searchText}
							onChange={(e) => {
								setSearchText(e.target.value);
							}}
							className={styles.searchInput}
						/>
						<span className={styles.searchIcon}>🔍</span>
					</div>
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
							{paginatedData.length > 0 ? (
								paginatedData.map((data, index) => (
									<tr key={index}>
										<td>{data.contract_id_fk}</td>
										<td>{data.upload_type}</td>
										<td>{data.data_date}</td>
										<td>{data.soft_delete_status_fk}</td>
										<td>{data.p6_file_path}</td>
										<td>{data.uploaded_by_user_id_fk}</td>
										<td>{data.uploaded_date}</td>
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

				{/* Pagination */}
				<div className="paginationRow">
					<div className="paginationInfo">
						Showing {(page - 1) * pageSize + 1} –{" "}
						{Math.min(page * pageSize, total)} of {total}
					</div>

					<div className="paginationControls">
						{renderPageButtons(page, pages, setPage)}
					</div>
				</div>

			</div>

			<Outlet />
		</div>
	);
}