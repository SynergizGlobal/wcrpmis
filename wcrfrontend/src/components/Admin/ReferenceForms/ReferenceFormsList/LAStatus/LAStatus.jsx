import React, { useEffect, useState } from "react";
import styles from "./LAStatus.module.css";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";

export default function LAStatus() {
	const [data, setData] = useState([]);
	const [search, setSearch] = useState("");

	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");
	const [value, setValue] = useState("");
	const [oldValue, setOldValue] = useState("");

	/* ================= LOAD ================= */
	const fetchLALandStatus = async () => {
		try {
			const res = await fetch(`${API_BASE_URL}/la-status`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({})
			});

			const json = await res.json();
			if (!json.status) return;
			setData(json.laStatusList || []);
		} catch (err) {
			console.error("Failed to load LA Status", err);
			alert("Failed to load data");
		}
	};

	useEffect(() => {
		fetchLALandStatus();
	}, []);

	/* ================= FILTER ================= */
	const filteredData = data.filter(item =>
		item.status?.toLowerCase().includes(search.toLowerCase())
	);

	/* ================= ADD ================= */
	const handleAddClick = () => {
		setMode("add");
		setValue("");
		setOldValue("");
		setShowModal(true);
	};

	/* ================= EDIT ================= */
	const handleEditClick = item => {
		setMode("edit");
		setValue(item.status);
		setOldValue(item.status);
		setShowModal(true);
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		if (!value.trim()) {
			alert("Status is required");
			return;
		}

		try {
			const url =
				mode === "add"
					? "/add-la-status"
					: "/update-la-status";

			const payload =
				mode === "add"
					? { status: value.trim() }
					: {
						status_old: oldValue,
						status_new: value.trim()
					};

			const res = await fetch(`${API_BASE_URL}${url}`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(payload)
			});

			const json = await res.json();

			if (json.status) {
				alert(json.message || "Operation successful");
				setShowModal(false);
				fetchLALandStatus();
			} else {
				alert(json.error || "Operation failed");
			}
		} catch (err) {
			console.error("Save failed", err);
			alert("Server error. Try again.");
		}
	};

	/* ================= DELETE ================= */
	const handleDelete = async item => {
		if (
			!window.confirm(`Are you sure you want to delete "${item.status}"?`)
		)
			return;

		try {
			const res = await fetch(`${API_BASE_URL}/delete-la-status`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ status: item.status })
			});

			const json = await res.json();

			if (json.status) {
				alert(json.message || "Deleted successfully");
				fetchLALandStatus();
			} else {
				alert(json.error || "Delete failed");
			}
		} catch (err) {
			console.error("Delete failed", err);
			alert("Server error. Try again.");
		}
	};

	/* ================= JSX ================= */
	return (
		<div className={styles.container}>
			<div className="card">
				<div className="formHeading">
					<h2 className="center-align">Status</h2>
				</div>

				<div className="innerPage">
					<div className={styles.topActions}>
						<button className="btn btn-primary" onClick={handleAddClick}>
							+ Add Status
						</button>
					</div>

					<div className={styles.searchBox}>
						<input
							type="text"
							placeholder="Search"
							value={search}
							onChange={e => setSearch(e.target.value)}
						/>
						{search && (
							<span className={styles.clear} onClick={() => setSearch("")}>
								✕
							</span>
						)}
					</div>
					<div className={`dataTable ${styles.tableWrapper}`}>
						<table className={styles.table}>
							<thead>
								<tr>
									<th>Status</th>
									<th className={styles.actionCol}>Action</th>
								</tr>
							</thead>
							<tbody>
								{filteredData.map((item, index) => (
									<tr key={index}>
										<td>{item.status}</td>
										<td className={styles.actionCol}>
											<button
												className={styles.editBtn}
												onClick={() => handleEditClick(item)}
											>
												<FaEdit />
											</button>
											<button
												className={styles.deleteBtn}
												onClick={() => handleDelete(item)}
											>
												<FaTrash />
											</button>
										</td>
									</tr>
								))}

								{filteredData.length === 0 && (
									<tr>
										<td colSpan="2" className="center-align">
											No records found
										</td>
									</tr>
								)}
							</tbody>
						</table>
					</div>
				</div>
				<div className={styles.footerText}>
					<spam>Showing {filteredData.length} entries of {filteredData.length} entries </spam>
				</div>
			</div>

			{/* ================= MODAL ================= */}
			{showModal && (
				<div className={styles.modalOverlay}>
					<div className={styles.modal}>
						<div className={styles.modalHeader}>
							<h4>{mode === "add" ? "Add Status" : "Update Status"}</h4>
							<span className={styles.close} onClick={() => setShowModal(false)}>
								✕
							</span>
						</div>

						<div className={styles.modalBody}>
							<input
								type="text"
								placeholder="Status"
								value={value}
								onChange={e => setValue(e.target.value)}
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
