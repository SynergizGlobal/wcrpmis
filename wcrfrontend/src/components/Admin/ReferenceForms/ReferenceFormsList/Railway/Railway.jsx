import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash, FaPlus, FaTimes } from "react-icons/fa";
import styles from "./Railway.module.css";
import Swal from "sweetalert2";

export default function Railway() {
	const [rows, setRows] = useState([]);
	const [columns, setColumns] = useState([]);
	const [search, setSearch] = useState("");

	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");

	const [railwayId, setRailwayId] = useState("");
	const [railwayName, setRailwayName] = useState("");
	const [railwayIdOld, setRailwayIdOld] = useState("");

	/* ================= FETCH ================= */
	const fetchData = async () => {
		try {
			const res = await fetch(`${API_BASE_URL}/get-railway`, {
				method: "GET",
				credentials: "include"
			});

			const json = await res.json();
			if (json.message !== "success") return;

			const railwayList = json.railwayList || [];
			const tablesList = json.railwayDetails?.tablesList || [];
			const countList = json.railwayDetails?.countList || [];

			/* ---- DISTINCT FK TABLE NAMES ---- */
			const columnNames = [
				...new Set(
					tablesList
						.map(t => t?.tName)
						.filter(Boolean)
				)
			];

			setColumns(columnNames);

			/* ---- railway_id | table -> count ---- */
			const countMap = {};
			countList.forEach(c => {
				if (c?.railway_id && c?.tName) {
					countMap[`${c.railway_id}|${c.tName}`] = Number(c.count || 0);
				}
			});

			/* ---- MERGE DATA ---- */
			const merged = railwayList.map((r, idx) => {
				const counts = {};
				columnNames.forEach(col => {
					counts[col] = countMap[`${r.railway_id}|${col}`] || 0;
				});

				return {
					id: idx + 1,
					railwayId: r.railway_id ?? "",
					railwayName: r.railway_name ?? "",
					counts
				};
			});

			setRows(merged);
		} catch (err) {
			console.error("Fetch failed", err);
		}
	};

	useEffect(() => {
		fetchData();
	}, []);

	/* ================= SEARCH ================= */
	const filteredRows = rows.filter(r =>
		`${r.railwayId} ${r.railwayName}`
			.toLowerCase()
			.includes(search.toLowerCase())
	);

	/* ================= ADD ================= */
	const handleAdd = () => {
		setMode("add");
		setRailwayId("");
		setRailwayName("");
		setRailwayIdOld("");
		setShowModal(true);
	};

	/* ================= EDIT ================= */
	const handleEdit = row => {
		setMode("edit");
		setRailwayId(row.railwayId);
		setRailwayName(row.railwayName);
		setRailwayIdOld(row.railwayId);
		setShowModal(true);
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		if (!railwayId.trim() || !railwayName.trim()) {
			Swal.fire("Validation", "Railway Id & Name are required", "warning");
			return;
		}

		const formData = new FormData();

		try {
			if (mode === "add") {
				formData.append("railway_id", railwayId);
				formData.append("railway_name", railwayName);

				await fetch(`${API_BASE_URL}/add-railway`, {
					method: "POST",
					body: formData
				});
			} else {
				formData.append("value_old", railwayIdOld);
				formData.append("value_new", railwayId);
				formData.append("railway_name_new", railwayName);

				await fetch(`${API_BASE_URL}/update-railway`, {
					method: "POST",
					body: formData
				});
			}

			setShowModal(false);
			fetchData();
			Swal.fire("Success", "Saved successfully", "success");
		} catch (err) {
			Swal.fire("Error", "Something went wrong", "error");
		}
	};

	/* ================= DELETE ================= */
	const handleDelete = row => {
		const hasRefs = Object.values(row.counts).some(v => v > 0);
		if (hasRefs) return;

		Swal.fire({
			title: "Delete Railway?",
			text: "This action cannot be undone",
			icon: "warning",
			showCancelButton: true,
			confirmButtonText: "Yes, Delete"
		}).then(async result => {
			if (result.isConfirmed) {
				const formData = new FormData();
				formData.append("railway_id_val", row.railwayId);

				await fetch(`${API_BASE_URL}/delete-railway`, {
					method: "POST",
					body: formData
				});

				fetchData();
				Swal.fire("Deleted", "Railway deleted successfully", "success");
			}
		});
	};

	return (
		<div className={styles.container}>
			<div className="card">
				<div className="formHeading">
					<h2 className="center-align">Railway</h2>
				</div>

				<div className="innerPage">
					<div className={styles.topActions}>
						<button className="btn btn-primary" onClick={handleAdd}>
							+ Add Railway
						</button>
					</div>

					<div className={styles.searchBox}>
						<input
							placeholder="Search Railway"
							value={search}
							onChange={e => setSearch(e.target.value)}
						/>
					</div>
					<div className={`dataTable ${styles.tableWrapper}`}>
						<table className={styles.table}>
							<thead>
								<tr>
									<th>Railway ID</th>
									<th>Railway Name</th>
									{columns.map(c => (
										<th key={c}>{c.toUpperCase()}</th>
									))}
									<th>Action</th>
								</tr>
							</thead>

							<tbody>
								{filteredRows.map(r => (
									<tr key={r.id}>
										<td>{r.railwayId}</td>
										<td>{r.railwayName}</td>

										{columns.map(c => (
											<td key={c}>
												{r.counts[c] ? `(${r.counts[c]})` : ""}
											</td>
										))}

										<td >
										<div className={styles.actionButtons}>
										<button
										  className={styles.editBtn}
										  onClick={() => handleEdit(r)}
										  title="Edit"
										>
										  <FaEdit />
										</button>


											{!Object.values(r.counts).some(v => v > 0) && (
												<button
												  className={styles.deleteBtn}
												  onClick={() => handleDelete(r)}
												  title="Delete"
												>
												  <FaTrash />
												</button>

											)}
											</div>
										</td>
									</tr>
								))}

								{filteredRows.length === 0 && (
									<tr>
										<td colSpan={columns.length + 3} className="center-align">
											No records found
										</td>
									</tr>
								)}
							</tbody>
						</table>
					</div>
					<div className={styles.footerText}>
						Showing 1 to {filteredRows.length} to {filteredRows.length}
					</div>
				</div>
			</div>
			{showModal && (
				<div className={styles.modalOverlay}>
					<div className={styles.modal}>
						<div className={styles.modalHeader}>
							<span>{mode === "add" ? "Add" : "Update"} Railway</span>
							<span
								className={styles.close}
								onClick={() => setShowModal(false)}
							>
								✕
							</span>
						</div>

						<div className={styles.modalBody}>
							<label>Railway ID</label>
							<input
								value={railwayId}
								onChange={e => setRailwayId(e.target.value)}
								disabled={mode === "edit"}
							/>

							<label>Railway Name</label>
							<input
								value={railwayName}
								onChange={e => setRailwayName(e.target.value)}
							/>

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
