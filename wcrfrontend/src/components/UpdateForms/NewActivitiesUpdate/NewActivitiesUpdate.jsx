import React, { useEffect, useState } from "react";
import Select from "react-select";
import styles from "./NewActivitiesUpdate.module.css";
import { API_BASE_URL } from "../../../config";
import api from "../../../api/axiosInstance";

export default function NewActivitiesUpdate() {

	// ---------- LIST STATES ----------
	const [projectsList, setProjectsList] = useState([]);
	const [contractsList, setContractsList] = useState([]);
	const [structureTypeList, setStructureTypesList] = useState([]);
	const [structureList, setStructureList] = useState([]);
	const [componentList, setComponentList] = useState([]);
	const [elementsList, setElementsList] = useState([]);
	const [activitiesList, setActivityList] = useState([]);

	// ---------- SELECTED STATES ----------
	const [selectedProject, setSelectedProject] = useState(null);
	const [selectedContract, setSelectedContract] = useState(null);
	const [selectedStructureType, setSelectedStructureType] = useState(null);
	const [selectedStructure, setSelectedStructure] = useState(null);
	const [selectedComponent, setSelectedComponent] = useState(null);
	const [selectedElement, setSelectedElement] = useState(null);

	// ---------- FORM STATES ----------
	const [progressDate, setProgressDate] = useState("");
	const [remarks, setRemarks] = useState("");

	//-------------Rrrors=-----------
	const [errors, setErrors] = useState({});
	
	const [latestData,setLatestData] = useState([]);
	
	const [latesDataLoading, setLatesDataLoading] = useState(false);
	


	// ---------- INITIAL LOAD ----------
	useEffect(() => {
		api.post(`${API_BASE_URL}/newActivitiesUpdate`, {}, { withCredentials: true })
			.then(res => {
				setProjectsList((res.data.projectsList || []).map(p => ({ value: p.project_id, label: p.project_name })));
				setContractsList((res.data.contractsList || []).map(c => ({ value: c.contract_id, label: c.contract_short_name })));
			});
	}, []);


	useEffect(() => {

		setLatesDataLoading(true);
		try {
			api.post(`${API_BASE_URL}/ajax/getLatestRowData`, {}, { withCredentials: true })
				.then(res => {
					setLatestData((res.data || []));
				});
		}
		catch (err) {
			console.log("Error Occuered!" + err);
		}
		finally {
			setLatesDataLoading(false);
		}

	}, []);
	

	const fetchLatestData = async () => {
		try {
			api.post(`${API_BASE_URL}/ajax/getLatestRowData`, {}, { withCredentials: true })
				.then(res => {
					setLatestData((res.data || []));
				});
		}
		catch (err) {
			console.log("Error Occuered!" + err);
		}
		finally {
			setLatesDataLoading(false);
		}
	};

	// ---------- STRUCTURE TYPES ----------
	useEffect(() => {
		if (!selectedContract) return setStructureTypesList([]);
		api.post(`${API_BASE_URL}/ajax/getStructureTypesInActivitiesUpdate`,
			{ contract_id_fk: selectedContract.value },
			{ withCredentials: true }
		).then(res => {
			setStructureTypesList(res.data.map(s => ({ value: s.structure_type, label: s.structure_type })));
		});
	}, [selectedContract]);

	// ---------- STRUCTURES ----------
	useEffect(() => {
		if (!selectedStructureType) return setStructureList([]);
		api.post(`${API_BASE_URL}/ajax/getNewActivitiesUpdateStructures`,
			{ contract_id_fk: selectedContract.value, structure_type_fk: selectedStructureType.value },
			{ withCredentials: true }
		).then(res => {
			setStructureList(res.data.map(s => ({ value: s.strip_chart_structure_id_fk, label: s.strip_chart_structure_id_fk })));
		});
	}, [selectedStructureType]);

	// ---------- COMPONENTS ----------
	useEffect(() => {
		if (!selectedStructure) return setComponentList([]);
		api.post(`${API_BASE_URL}/ajax/getNewActivitiesUpdateComponentsList`,
			{
				contract_id_fk: selectedContract.value,
				strip_chart_structure_id_fk: selectedStructure.value,
				structure_type_fk: selectedStructureType.value
			},
			{ withCredentials: true }
		).then(res => {
			setComponentList(res.data.map(c => ({ value: c.strip_chart_component, label: c.strip_chart_component })));
		});
	}, [selectedStructure]);

	// ---------- ELEMENTS ----------
	useEffect(() => {
		if (!selectedComponent) return setElementsList([]);
		api.post(`${API_BASE_URL}/ajax/getNewActivitiesUpdateComponentIdsList`,
			{
				contract_id_fk: selectedContract.value,
				strip_chart_structure_id_fk: selectedStructure.value,
				strip_chart_component: selectedComponent.value,
				structure_type_fk: selectedStructureType.value
			},
			{ withCredentials: true }
		).then(res => {
			setElementsList(res.data.map(e => ({ value: e.strip_chart_component_id, label: e.strip_chart_component_id })));
		});
	}, [selectedComponent]);

	// ---------- ACTIVITIES ----------
	useEffect(() => {
		// Parent filters must exist
		if (!selectedContract || !selectedStructure || !selectedStructureType) {
			setActivityList([]);
			return;
		}

		// At least ONE of these must be selected
		if (!selectedComponent && !selectedElement) {
			setActivityList([]);
			return;
		}

		const fetchActivities = async () => {
			try {
				if(!selectedComponent){
					setActivityList([]);
					return;
				}
				const res = await api.post(
					`${API_BASE_URL}/ajax/getNewActivitiesfiltersList`,
					{
						contract_id_fk: selectedContract.value,
						strip_chart_structure_id_fk: selectedStructure.value,
						structure_type_fk: selectedStructureType.value,
						strip_chart_component: selectedComponent?.value || null,
						strip_chart_component_id: selectedElement?.value || null
					},
					{ withCredentials: true }
				);

				setActivityList(res.data || []);
			} catch (err) {
				console.error("Failed to load activities", err);
				setActivityList([]);
			}
		};

		fetchActivities();

	}, [
		selectedContract,
		selectedStructure,
		selectedStructureType,
		selectedComponent,
		selectedElement
	]);
	
	
	const fetchActivities = async () => {
		try {
			const res = await api.post(
				`${API_BASE_URL}/ajax/getNewActivitiesfiltersList`,
				{
					contract_id_fk: selectedContract.value,
					strip_chart_structure_id_fk: selectedStructure.value,
					structure_type_fk: selectedStructureType.value,
					strip_chart_component: selectedComponent?.value || null,
					strip_chart_component_id: selectedElement?.value || null
				},
				{ withCredentials: true }
			);

			setActivityList(res.data || []);
		} catch (err) {
			console.error("Failed to load activities", err);
			setActivityList([]);
		}
	};


	// ---------- HANDLERS ----------
	const handleActualChange = (index, value) => {
		const updated = [...activitiesList];
		updated[index].actual = value;
		setActivityList(updated);
	};

	const formatDateForBackend = (date) => {
		if (!date) return null;

		const d = new Date(date);
		if (isNaN(d.getTime())) return null;

		const day = String(d.getDate()).padStart(2, "0");
		const year = d.getFullYear();
		const monthName = d.toLocaleString("en-US", { month: "long" });

		return `${day}-${monthName}-${year}`;
	};


	const handleUpdate = async () => {
		const newErrors = {};

		if (!selectedContract) newErrors.contract_id_fk = true;
		if (!selectedStructureType) newErrors.structure_type_fk = true;
		if (!selectedComponent) newErrors.strip_chart_component = true;

		setErrors(newErrors);

		if (Object.keys(newErrors).length > 0) return;

		try {
			const activity_ids = activitiesList.map(a => a.activity_id);
			const actualScopes = activitiesList.map(a => (a.actual ? a.actual.toString() : ""));
			const totalScopes = activitiesList.map(a => (a.scope ? a.scope.toString() : "0"));
			const completedScopes = activitiesList.map(a => (a.completed ? a.completed.toString() : "0"));

		const res =	await api.post(`${API_BASE_URL}/update-new-activities-bulk`, {
				contract_id_fk: selectedContract?.value,
				structure_type_fk: selectedStructureType?.value,
				strip_chart_structure_id_fk: selectedStructure?.value,
				strip_chart_component: selectedComponent?.value,
				strip_chart_component_id: selectedElement?.value,
				progress_date: formatDateForBackend(progressDate),
				remarks,

				activity_ids: activity_ids,
				actualScopes: actualScopes,
				totalScopes: totalScopes,
				completedScopes: completedScopes,

				scope: totalScopes.join(",")

			}, { withCredentials: true });
			
			if (res.data.success) {
			  alert(res.data.success);
			  fetchActivities();
			  fetchLatestData();
			} else if (res.data.error) {
			  alert(res.data.error);
			} else {
			  alert("Unexpected response from server");
			}		} catch (err) {
			console.error(err);
			alert("Update failed");
		}
	};


	const handleExport = async () => {
		const res = await api.post(`${API_BASE_URL}/exportActivitiesbyContract`, {
			contract_id_fk: selectedContract?.value,
			structure_type_fk: selectedStructureType?.value,
			strip_chart_structure_id_fk: selectedStructure?.value,
			progress_date: formatDateForBackend(progressDate),
		}, { responseType: "blob", withCredentials: true });

		const url = window.URL.createObjectURL(new Blob([res.data]));
		const link = document.createElement("a");
		link.href = url;
		link.download = "Activities.xlsx";
		link.click();
	};



	const handleResetButton = () => {
		setSelectedProject(null);
		setSelectedContract(null);
		setSelectedStructureType(null);
		setSelectedStructure(null);
		setSelectedComponent(null);
		setSelectedElement(null);
		setActivityList([]);
	};

	const [showModal, setShowModal] = useState(false);
	const [selectedFile, setSelectedFile] = useState(null);
	const [loading, setLoading] = useState(false);

	//  Handle file selection
	const handleFileChange = (e) => {
		setSelectedFile(e.target.files[0]);
	};

	const closeModal = () => {
		setShowModal(false);
		setSelectedFile(null);
	};



	const handleUploadSubmit = async (e) => {
		e.preventDefault();

		if (!selectedFile) return alert("Select a file!");

		const formData = new FormData();
		formData.append("stripChartFile", selectedFile);

		try {
			setLoading(true);
			await api.post(`${API_BASE_URL}/upload-new-activities`, formData, {
				headers: { "Content-Type": "multipart/form-data" },
				withCredentials: true
			});
			alert("Upload successful!");
			closeModal();
			window.location.reload();
		} catch (err) {
			alert("Upload failed.");
			console.error(err);
			window.location.reload();
		} finally {
			setLoading(false);
		}
	};
	return (
		<div className={styles.container}>
		  <div className="pageHeading">
		  <h2>New Activities Update</h2>
		  <div  className="rightBtns md-none">
		    <span>&nbsp;</span>
		  </div>
		</div>

			<div className={styles.layoutWrapper}>
				{/* LEFT PANEL */}
				<div className={styles.leftSection}>

					<div className="form-row">
						<div className="form-field">
							<Select placeholder="Project" value={selectedProject} options={projectsList} onChange={setSelectedProject} isClearable />
						</div>
						<div className="form-field">
							<Select placeholder="Contract"
								value={selectedContract}
								options={contractsList}
								onChange={(val) => {
									setSelectedContract(val);
									setErrors(prev => ({ ...prev, contract_id_fk: false }));
									setSelectedStructureType(null);
									setSelectedStructure(null);
									setSelectedComponent(null);
									setSelectedElement(null);
								}}
								isClearable />
								<div>
							{errors.contract_id_fk && <span className="red">Required*</span>}
							</div>

						</div>
					</div>

					<div className="form-row flex-4">
						<div className="form-field">
							<Select
								placeholder="Structure Type"
								value={selectedStructureType}
								options={structureTypeList}
								onChange={(val) => {
									setSelectedStructureType(val);
									setErrors(prev => ({ ...prev, structure_type_fk: false }));
									setSelectedStructure(null);
									setSelectedComponent(null);
									setSelectedElement(null);
								}}
								isClearable
								isDisabled={!selectedContract}
							/>
							{errors.structure_type_fk && <span className="red">Required*</span>}
						</div>

						<div className="form-field">

							<Select placeholder="Structure"
								value={selectedStructure}
								options={structureList}
								onChange={(val) => {
									setSelectedStructure(val);
									setSelectedComponent(null);
									setSelectedElement(null);
								}}
								isClearable
								isDisabled={!selectedStructureType} />
						</div>
						<div className="form-field">
							<Select placeholder="Component"
								value={selectedComponent}
								options={componentList}
								onChange={(val) => {
									setSelectedComponent(val);
									setErrors(prev => ({ ...prev, strip_chart_component: false }));
									setSelectedElement(null);
								}}
								isDisabled={!selectedStructure}
								isClearable />
							{errors.strip_chart_component && <span className="red">Required*</span>}

						</div>
						<div className="form-field"><Select placeholder="Element" value={selectedElement} options={elementsList} onChange={setSelectedElement} isDisabled={!selectedComponent} isClearable /></div>

						<div className="form-field">
							<label>Progress Date *</label>
							<input type="date" value={progressDate} onChange={e => setProgressDate(e.target.value)} />
						</div>

						<div className="form-field">
							<label>Remarks</label>
							<input type="text" value={remarks} onChange={e => setRemarks(e.target.value)} />
						</div>

						<button className="btn btn-primary" onClick={() => setShowModal(true)}>Attach Photo</button>
						<button className="btn btn-primary" onClick={handleUpdate}>Update</button>
					</div>

					<div className="form-post-buttons">
						<button className="btn btn-primary" onClick={handleUpdate}>Update</button>
						<button className="btn btn-secondary" onClick={handleResetButton}>Reset</button>
						<button className="btn btn-primary" onClick={handleExport}>Export</button>
						<button className="btn btn-secondary" onClick={() => setShowModal(true)}>Upload</button>
					</div>
				</div>
				{/* ---------------- RIGHT PANEL ---------------- */}
				<div className={styles.rightSection}>
				  <h4>Latest Updated Structure → Component</h4>
				  <div className={styles.latestBox}>
				    {latestData.map((item, i) => (
				      <p key={i}>
				        <a href="#">
				          {item.structure} → {item.strip_chart_component}
				        </a>
				      </p>
				    ))}
				  </div>
				</div>
			</div>

			{activitiesList.length > 0 && (
				<div className={`dataTable ${styles.tableWrapper}`}>
					<table className={styles.projectTable}>
						<thead>
							<tr>
								<th>Task Code</th><th>Activity</th><th>Scope</th><th>Validation Pending</th><th>Completed</th><th>Actual</th>
							</tr>
						</thead>
						<tbody>
							{activitiesList.map((a, i) => (
								<tr key={i}>
									<td>{a.p6_task_code}</td>
									<td>{a.strip_chart_activity_name}</td>
									<td>{a.scope}</td>
									<td>{a.validation_pending}</td>
									<td>{a.completed}</td>
									<td>
										<input type="number" step="0.001" min="0"
											value={a.actual || ""}
											onChange={(e) => handleActualChange(i, e.target.value)}
										/>
									</td>
								</tr>
							))}
						</tbody>
					</table>
				</div>
			)}



			{showModal && (
				<div
					className="modal-overlay"
					style={{
						position: "fixed",
						inset: 0,
						background: "rgba(0,0,0,0.5)",
						display: "flex",
						alignItems: "center",
						justifyContent: "center",
						zIndex: 9999,
					}}
				>
					<div
						className="modal-content"
						style={{
							background: "#fff",
							borderRadius: "10px",
							width: "420px",
							maxWidth: "90%",
							padding: "1.5rem",
							boxShadow: "0 5px 15px rgba(0,0,0,0.3)",
						}}
					>
						<h3 className="text-center mb-2">Upload New Activities</h3>

						<form onSubmit={handleUploadSubmit} encType="multipart/form-data">
							<div className="form-group mb-3 center-align">
								<label className="form-label fw-bold mb-2">Attachment</label>
								<input
									type="file"
									id="designFile"
									name="designFile"
									//	  									accept=".xls, .xlsx"
									onChange={handleFileChange}
									required
									className="form-control"
								/>
								{selectedFile && (
									<p style={{ marginTop: "10px", color: "#475569" }}>
										Selected: {selectedFile.name}
									</p>
								)}
							</div>

							<div
								className="modal-actions"
								style={{
									display: "flex",
									justifyContent: "space-evenly",
									marginTop: "1rem",
								}}
							>
								<button
									type="submit"
									className="btn btn-primary"
									style={{ width: "48%" }}
									disabled={loading}
								>
									{loading ? "Uploading..." : "Update"}
								</button>

								<button
									type="button"
									className="btn btn-white"
									style={{ width: "48%" }}
									onClick={closeModal}
								>
									Cancel
								</button>
							</div>
						</form>
					</div>
				</div>
			)}

		</div>
	);
}
