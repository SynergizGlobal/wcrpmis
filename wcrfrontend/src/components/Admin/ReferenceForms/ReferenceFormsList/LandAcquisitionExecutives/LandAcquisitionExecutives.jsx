import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaPlus, FaTimes } from "react-icons/fa";
import styles from "./LandAcquisitionExecutives.module.css";

export default function LandAcquisitionExecutives() {
	const [rows, setRows] = useState([]);
	const [projects, setProjects] = useState([]);
	const [users, setUsers] = useState([]);
	const [search, setSearch] = useState("");

	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");

	const [selectedProject, setSelectedProject] = useState("");
	const [executiveRows, setExecutiveRows] = useState([""]);
	const [editRow, setEditRow] = useState(null);

	/* ================= LOAD PAGE DATA ================= */
	const fetchLandExecutives = async () => {
		try {
			const res = await fetch(`${API_BASE_URL}/land-executives`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({})
			});

			const json = await res.json();
			if (json.status !== "SUCCESS") return;

			setRows(json.executivesDetails || []);
			setProjects(json.projectDetails || []);
			setUsers(json.usersDetails || []);
		} catch (e) {
			console.error("Failed to load data", e);
		}
	};

	useEffect(() => {
		fetchLandExecutives();
	}, []);

	/* ================= SEARCH ================= */
	const filteredData = rows.filter(item => {
		const term = search.toLowerCase();
		return (
			(item.project_name || "").toLowerCase().includes(term) ||
			(item.user_name || "").toLowerCase().includes(term)
		);
	});

	/* ================= ROW HANDLERS ================= */
	const addExecutiveRow = () =>
		setExecutiveRows(prev => [...prev, ""]);

	const removeExecutiveRow = index =>
		setExecutiveRows(prev => prev.filter((_, i) => i !== index));

	const updateExecutiveRow = (index, value) => {
		const updated = [...executiveRows];
		updated[index] = value;
		setExecutiveRows(updated);
	};

	/* ================= ADD ================= */
	const handleAdd = () => {
		setMode("add");
		setSelectedProject("");
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
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		const cleanedExecutives = [...new Set(executiveRows.filter(Boolean))];

		if (!selectedProject || cleanedExecutives.length === 0) {
			alert("Please select Project and at least one Executive");
			return;
		}

		// ✅ DAO EXPECTS ARRAYS
		const payload = {
			project_id_fks: [selectedProject],
			executive_user_id_fks: [cleanedExecutives.join(",")]
		};

		if (mode === "edit") {
			payload.project_id_fk_old = editRow.project_id_fk;
		}

		const url =
			mode === "add"
				? "/add-land-executives"
				: "/update-land-executives";

		try {
			const res = await fetch(`${API_BASE_URL}${url}`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(payload)
			});

			const json = await res.json();

			if (json.status === "SUCCESS") {
				alert(json.message || "Saved successfully");
				setShowModal(false);
				fetchLandExecutives();
			} else {
				alert(json.message || "Operation failed");
			}
		} catch (err) {
			console.error("Save failed", err);
			alert("Server error. Please try again.");
		}
	};


	/* ================= JSX ================= */
	return (
		<div className={styles.container}>
			<div className="card">
				<div className="formHeading">
					<h2 className="center-align">Land Acquisition Executives</h2>
				</div>

				<div className="innerPage">
					<div className={styles.topActions}>

						<button className="btn btn-primary" onClick={handleAdd}>
							+ Add Executives
						</button>
					</div>

					<div className={styles.searchBox}>
						<input
							placeholder="Search"
							value={search}
							onChange={e => setSearch(e.target.value)}
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
								{filteredData.map((r, i) => (
									<tr key={i}>
										<td>{r.project_name}</td>
										<td>
											{r.user_name
												? r.user_name.split(",").map((n, idx) => (
													<div key={idx}>▶ {n.trim()}</div>
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
						Showing {filteredData.length} of {filteredData.length} entries
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
							<span
								className={styles.close}
								onClick={() => setShowModal(false)}
							>
								✕
							</span>
						</div>

						<div className={styles.modalBody}>
							<div className={styles.row}>
								{/* LEFT COLUMN : PROJECT */}
								<div className={styles.col}>
									<p className={styles.searchableLabel}>
										Project
									</p>
									<select
										className={styles.select}
										value={selectedProject}
										onChange={e => {
											setSelectedProject(e.target.value);
											setExecutiveRows([""]);
										}}
									>
										<option value="">Select Project</option>
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

								{/* RIGHT COLUMN : EXECUTIVES */}
								<div className={`${styles.col} ${styles.boxGrey}`}>
									{executiveRows.map((v, i) => (
										<div key={i} className={styles.contractRow}>
											<select
												className={styles.select}
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

											{executiveRows.length > 1 && (
												<button
													className={styles.removeBtn}
													onClick={() =>
														removeExecutiveRow(i)
													}
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

							{/* ACTIONS */}
							<div className={styles.modalActions}>
								<button
									className="btn btn-primary"
									onClick={handleSave}
								>
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
