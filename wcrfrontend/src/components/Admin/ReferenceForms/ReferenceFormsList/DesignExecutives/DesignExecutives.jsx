import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaPlus, FaTimes } from "react-icons/fa";
import styles from "./DesignExecutives.module.css";

export default function DesignExecutives() {
	const [rows, setRows] = useState([]);
	const [projects, setProjects] = useState([]);
	const [users, setUsers] = useState([]);
	const [search, setSearch] = useState("");

	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");

	const [selectedProject, setSelectedProject] = useState("");
	const [executiveRows, setExecutiveRows] = useState([""]);
	const [editRow, setEditRow] = useState(null);

	/* ================= PAGE LOAD ================= */
	const fetchDesignExecutives = async () => {
		try {
			const res = await fetch(`${API_BASE_URL}/design-executives`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({})
			});

			const json = await res.json();
			if (json.status !== "success") return;

			setRows(json.executivesDetails || []);
			setProjects(json.projectDetails || []);
		} catch (e) {
			console.error("Failed to load page data", e);
		}
	};

	useEffect(() => {
		fetchDesignExecutives();
	}, []);

	/* ================= SEARCH ================= */
	const filteredData = rows.filter(item => {
		const term = search.toLowerCase();
		return (
			(item.project_name || "").toLowerCase().includes(term) ||
			(item.user_name || "").toLowerCase().includes(term)
		);
	});

	/* ================= LOAD USERS (PROJECT BASED) ================= */
	const loadProjectUsers = async projectId => {
		if (!projectId) {
			setUsers([]);
			return;
		}

		try {
			const res = await fetch(
				`${API_BASE_URL}/ajax/getProjectWiseDesignResponsibleUsers`,
				{
					method: "POST",
					credentials: "include",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify({ project_id_fk: projectId })
				}
			);

			const text = await res.text();
			if (!text) {
				setUsers([]);
				return;
			}

			const data = JSON.parse(text);

			const cleaned = Array.isArray(data)
				? data.filter(u => u.user_id && u.user_name)
				: [];

			setUsers(cleaned);
		} catch (err) {
			console.error("Failed to load users", err);
			setUsers([]);
		}
	};

	/* ================= EXECUTIVE ROW HANDLERS ================= */
	const addExecutiveRow = () => {
		setExecutiveRows([...executiveRows, ""]);
	};

	const removeExecutiveRow = index => {
		setExecutiveRows(executiveRows.filter((_, i) => i !== index));
	};

	const updateExecutiveRow = (index, value) => {
		const updated = [...executiveRows];
		updated[index] = value;
		setExecutiveRows(updated);
	};

	/* ================= ADD ================= */
	const handleAdd = () => {
		setMode("add");
		setSelectedProject("");
		setUsers([]);
		setExecutiveRows([""]);
		setEditRow(null);
		setShowModal(true);
	};

	/* ================= EDIT ================= */
	const handleEdit = row => {
		setMode("edit");
		setSelectedProject(row.project_id_fk);
		setExecutiveRows(
			row.user_id ? row.user_id.split(",").map(v => v.trim()) : [""]
		);
		setEditRow(row);
		setShowModal(true);

		loadProjectUsers(row.project_id_fk);
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		const cleanedExecutives = [
			...new Set(executiveRows.filter(v => v))
		];

		if (!selectedProject || cleanedExecutives.length === 0) {
			alert("Please select Project and Executives");
			return;
		}

		const formData = new FormData();
		formData.append("project_id_fks", selectedProject);
		formData.append(
			"executive_user_id_fks",
			cleanedExecutives.join(",")
		);

		if (mode === "edit") {
			formData.append("project_id_fk_old", editRow.project_id_fk);
		}

		const url =
			mode === "add"
				? "/add-design-executives"
				: "/update-design-executives";

		await fetch(`${API_BASE_URL}${url}`, {
			method: "POST",
			body: formData
		});

		setShowModal(false);
		fetchDesignExecutives();
	};

	return (
		<div className={styles.container}>
			<div className="card">
				<div className="formHeading">
					<h2 className="center-align">Design Executives</h2>
				</div>

				<div className="innerPage">
					<div className={styles.topActions}>
						<button className="btn btn-primary" onClick={handleAdd}>
							+ Add Executives
						</button>
					</div>

					{/* SEARCH */}
					<div className={styles.searchBox}>
						<input
							type="text"
							placeholder="Search"
							value={search}
							onChange={e => setSearch(e.target.value)}
						/>
						{search && (
							<span
								className={styles.clear}
								onClick={() => setSearch("")}
							>
								✕
							</span>
						)}
					</div>

					{/* ===== TABLE ===== */}
					<div className={`dataTable ${styles.tableWrapper}`}>
						<table className={styles.table}>
							<thead>
								<tr>
									<th>Project</th>
									<th>Executives</th>
									<th>Action</th>
								</tr>
							</thead>

							<tbody>
								{filteredData.map((r, i) => (
									<tr key={i}>
										<td>{r.project_name}</td>
										<td>
											{r.user_name
												? r.user_name.split(",").map((n, idx) => (
													<div key={idx}>&#9656; {n.trim()}</div>
												))
												: "-"}
										</td>
										<td>
											<button
												className={styles.editBtn}
												onClick={() => handleEdit(r)}
											>
												<FaEdit />
											</button>
										</td>
									</tr>
								))}

								{filteredData.length === 0 && (
									<tr>
										<td colSpan={3} className="center-align">
											No records found
										</td>
									</tr>
								)}
							</tbody>
						</table>
					</div>

					<div className={styles.footerText}>
						Showing {filteredData.length} of {rows.length} entries
					</div>
				</div>
			</div>

			{/* ================= MODAL ================= */}
			{showModal && (
				<div className={styles.modalOverlay}>
					<div className={styles.modal}>
						<div className={styles.modalHeader}>
							<span>
								{mode === "add" ? "Add Executives" : "Update Executives"}
							</span>
							<span
								className={styles.close}
								onClick={() => setShowModal(false)}
							>
								✕
							</span>
						</div>

						<div className={styles.modalBody}>
							<div className={styles.row}>
								{/* PROJECT */}
								<div className={styles.col}>
									<p className={styles.searchableLabel}>Project</p>
									<select
										className={styles.select}
										value={selectedProject}
										onChange={e => {
											setSelectedProject(e.target.value);
											setExecutiveRows([""]);
											loadProjectUsers(e.target.value);
										}}
									>
										<option value="">Select Project</option>
										{projects.map(p => (
											<option key={p.project_id_fk} value={p.project_id_fk}>
												{p.project_id_fk} - {p.project_name}
											</option>
										))}
									</select>
								</div>

								{/* EXECUTIVES */}
								<div className={`${styles.col} ${styles.boxGrey}`}>
									<p className={styles.searchableLabel}>
										Responsible Executives
									</p>

									{executiveRows.map((v, i) => (
										<div key={i} className={styles.contractRow}>
											<select
												className={styles.select}
												value={v}
												disabled={!selectedProject}
												onChange={e =>
													updateExecutiveRow(i, e.target.value)
												}
											>
												<option value="">
													{selectedProject
														? "Select Executive"
														: "Select Project First"}
												</option>

												{users.map(u => {
													const alreadySelected =
														executiveRows.includes(u.user_id) &&
														u.user_id !== v;

													return (
														<option
															key={u.user_id}
															value={u.user_id}
															disabled={alreadySelected}
														>
															{u.designation} - {u.user_name}
														</option>
													);
												})}
											</select>

											{executiveRows.length > 1 && (
												<button
													className={styles.removeBtn}
													onClick={() => removeExecutiveRow(i)}
												>
													<FaTimes />
												</button>
											)}
										</div>
									))}

									<button
										className="btn btn-primary"
										onClick={addExecutiveRow}
									>
										<FaPlus /> Add
									</button>
								</div>
							</div>

							<div className={styles.modalActions}>
								<button className="btn btn-primary" onClick={handleSave}>
									{mode === "add" ? "ADD" : "UPDATE"}
								</button>
								<button
									className="btn btn-white"
									onClick={() => setShowModal(false)}
								>
									CANCEL
								</button>
							</div>
						</div>
					</div>
				</div>
			)}
		</div>
	);
}
