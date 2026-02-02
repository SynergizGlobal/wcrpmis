import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaPlus, FaTimes } from "react-icons/fa";
import styles from "./UtilityShiftingExecutives.module.css";

export default function UtilityShiftingExecutives() {
	/* ================= STATE ================= */
	const [rows, setRows] = useState([]);
	const [projects, setProjects] = useState([]);
	const [users, setUsers] = useState([]);
	const [search, setSearch] = useState("");
	const [loading, setLoading] = useState(false);
	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");

	const [selectedProject, setSelectedProject] = useState("");
	const [executiveRows, setExecutiveRows] = useState([""]);
	const [editRow, setEditRow] = useState(null);

	/* ================= LOAD DATA ================= */
	const fetchLandExecutives = async () => {
		setLoading(true);
		try {
			const res = await fetch(
				`${API_BASE_URL}/utility-shifting-executivess`,
				{
					method: "GET",
					headers: { "Content-Type": "application/json" },
					credentials: "include"
				}
			);
			if (!res.ok) {
				throw new Error(`HTTP ${res.status}`);
			}

			const json = await res.json();

			if (json.status !== "SUCCESS") return;

			setRows(json.executivesDetails || []);
			setProjects(json.projectDetails || []);
			setUsers(json.usersDetails || []);
		} catch (err) {
			console.error("Failed to fetch executives", err);
		}
		finally {
			setLoading(false);
		}
	};

	useEffect(() => {
		fetchLandExecutives();
	}, []);

	/* ================= SEARCH ================= */
	const filteredData = rows.filter(r => {
		const term = search.toLowerCase();
		return (
			(r.project_name || "").toLowerCase().includes(term) ||
			(r.user_name || "").toLowerCase().includes(term)
		);
	});

	/* ================= HANDLERS ================= */
	const addExecutiveRow = () =>
		setExecutiveRows(prev => [...prev, ""]);

	const removeExecutiveRow = index =>
		setExecutiveRows(prev => prev.filter((_, i) => i !== index));

	const updateExecutiveRow = (index, value) => {
		const updated = [...executiveRows];
		updated[index] = value;
		setExecutiveRows(updated);
	};

	const handleAdd = () => {
		setMode("add");
		setSelectedProject("");
		setExecutiveRows([""]);
		setEditRow(null);
		setShowModal(true);
	};

	const handleEdit = row => {
		setMode("edit");
		setSelectedProject(row.project_id_fk);
		setExecutiveRows(
			row.user_id
				? row.user_id.split(",").map(v => v.trim())
				: [""]
		);
		setEditRow(row);
		setShowModal(true);
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		const executives = [...new Set(executiveRows.filter(Boolean))];

		if (!selectedProject || executives.length === 0) {
			alert("Please select project and executive(s)");
			return;
		}

		const payload = {
			project_id_fks: [selectedProject],
			executive_user_id_fks: [executives.join(",")]
		};

		if (mode === "edit") {
			payload.project_id_fk_old = editRow.project_id_fk;
		}

		const url =
			mode === "add"
				? "/add-utility-shifting-executives"
				: "/update-utility-shifting-executives";

		try {
			const res = await fetch(`${API_BASE_URL}${url}`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				credentials: "include",
				body: JSON.stringify(payload)
			});

			const json = await res.json();

			if (json.status === "SUCCESS") {
				alert(json.message);
				setShowModal(false);
				fetchLandExecutives();
			} else {
				alert(json.message || "Operation failed");
			}
		} catch (err) {
			console.error("Save failed", err);
			alert("Server error");
		}
	};

	/* ================= JSX ================= */
	return (
		<div className={styles.container}>
			<div className="card">
				<div className="formHeading">
					<h2 className="center-align">
						Utility Shifting Executives
					</h2>
				</div>

				<div className="innerPage">
					<div className={styles.topActions}>
						<button
							className="btn btn-primary"
							onClick={handleAdd}
						>
							+ Add Executives
						</button>
					</div>

					<div className={styles.searchBox}>
						<input
							placeholder="Search"
							value={search}
							onChange={e =>
								setSearch(e.target.value)
							}
						/>
					</div>
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
								{loading ?
									<tr>
										<td colSpan={3} align="center">Processing...</td>
									</tr>
									: filteredData.length === 0 && (
										<tr>
											<td colSpan="3" align="center">
												No records found
											</td>
										</tr>
									)}

								{filteredData.map((r, i) => (
									<tr key={i}>
										<td>{r.project_name}</td>
										<td>
											{r.user_name
												?.split(",")
												.map((n, idx) => (
													<div key={idx}>
														▶ {n.trim()}
													</div>
												))}
										</td>
										<td>
											<button
												className={
													styles.editBtn
												}
												onClick={() =>
													handleEdit(r)
												}
											>
												<FaEdit />
											</button>
										</td>
									</tr>
								))}
							</tbody>
						</table>
					</div>
					<div className={styles.footerText}>
						Showing 1 to {filteredData.length} of {filteredData.length} entries
					</div>
				</div>
			</div>

			{/* ================= MODAL ================= */}
			{showModal && (
				<div className={styles.modalOverlay}>
					<div className={styles.modal}>
						<div className={styles.modalHeader}>

							<h4>
								{mode === "add"
									? "Add Executives"
									: "Update Executives"}
							</h4>
						</div>
						<div className={styles.modalBody}>
							<div className={styles.row}>
								<div className={styles.col}>
									<p className={styles.searchableLabel}>
										Project
									</p>
									<select
										value={selectedProject}
										onChange={e => {
											setSelectedProject(
												e.target.value
											);
											setExecutiveRows([""]);
										}}
									>
										<option value="">
											Select Project
										</option>
										{projects.map(p => (
											<option
												key={p.project_id_fk}
												value={p.project_id_fk} 
											>
												{p.project_id_fk} - {p.project_name}
											</option>
										))}
									</select>
								</div>
								<div className={`${styles.col} ${styles.boxGrey}`}>

									{executiveRows.map((v, i) => (
										<div key={i} className={styles.contractRow}>
											<select
												value={v}
												disabled={!selectedProject}
												onChange={e =>
													updateExecutiveRow(
														i,
														e.target.value
													)
												}
											>
												<option value="">
													Select Executive
												</option>
												{users.map(u => (
													<option
														key={u.user_id}
														value={u.user_id}
														disabled={
															executiveRows.includes(
																u.user_id
															) &&
															u.user_id !== v
														}
													>
														{u.designation} - {u.user_name}
													</option>
												))}
											</select>

											{executiveRows.length >
												1 && (
													<button
														className={styles.removeBtn}

														onClick={() =>
															removeExecutiveRow(
																i
															)
														}
													>
														<FaTimes />
													</button>
												)}
										</div>
									))}

									<button
										className="btn btn-primary"
										onClick={addExecutiveRow}>
										<FaPlus /> Add
									</button>
								</div>
							</div>

							<div className={styles.modalActions}>
								<button
									className="btn btn-primary"
									onClick={handleSave}
								>
									{mode === "add"
										? "ADD"
										: "UPDATE"}
								</button>
								<button
									className="btn btn-white"

									onClick={() =>
										setShowModal(false)
									}
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
