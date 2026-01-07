import React, { useEffect, useState } from "react";
import styles from "./LALandStatus.module.css";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";

export default function LALandStatus() {
	const [data, setData] = useState([]);
	const [search, setSearch] = useState("");

	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");
	const [value, setValue] = useState("");
	const [oldValue, setOldValue] = useState("");

	/* ================= LOAD STATUS + COUNT ================= */
	const fetchLALandStatus = async () => {
		try {
			const res = await fetch(`${API_BASE_URL}/la-land-status`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({})
			});

			const json = await res.json();
			if (json.status !== "SUCCESS") return;

			// ✅ Correct paths
			const statusList =
				json.landAcquisitionStatusDetails?.dList1 || [];

			const countList =
				json.landAcquisitionStatusDetails?.countList || [];

			// Build count map → { status : count }
			const countMap = {};
			countList.forEach(c => {
				countMap[c.la_land_status] = Number(c.count || 0);
			});

			// Merge status + count
			const merged = statusList.map(s => ({
				la_land_status: s.la_land_status,
				count: countMap[s.la_land_status] || 0
			}));

			setData(merged);
		} catch (err) {
			console.error("Failed to load LA Land Status", err);
		}
	};


	useEffect(() => {
		fetchLALandStatus();
	}, []);

	/* ================= FILTER ================= */
	const filteredData = data.filter(item =>
		item.la_land_status
			.toLowerCase()
			.includes(search.toLowerCase())
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
		setValue(item.la_land_status);
		setOldValue(item.la_land_status);
		setShowModal(true);
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		if (!value.trim()) return;

		try {
			const url =
				mode === "add"
					? "/add-la-land-status"
					: "/update-la-land-status";

			const payload =
				mode === "add"
					? { la_land_status: value.trim() }
					: {
							value_old: oldValue,
							value_new: value.trim()
					  };

			const res = await fetch(`${API_BASE_URL}${url}`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(payload)
			});

			const json = await res.json();

			if (json.status === "SUCCESS") {
				alert(json.message || "Operation successful");
				setShowModal(false);
				fetchLALandStatus();
			} else {
				alert(json.message || "Operation failed");
			}
		} catch (err) {
			console.error("Save failed", err);
			alert("Server error. Try again.");
		}
	};

	/* ================= DELETE ================= */
	const handleDelete = async item => {
		if (item.count > 0) return;

		if (
			!window.confirm(
				`Are you sure you want to delete "${item.la_land_status}"?`
			)
		)
			return;

		try {
			const res = await fetch(`${API_BASE_URL}/delete-la-land-status`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({
					la_land_status: item.la_land_status
				})
			});

			const json = await res.json();

			if (json.status === "SUCCESS") {
				alert(json.message || "Deleted successfully");
				fetchLALandStatus();
			} else {
				alert(json.message || "Delete failed");
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
					<h2 className="center-align">Land Status</h2>
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
									<th>LA Files</th>
									<th className={styles.actionCol}>Action</th>
								</tr>
							</thead>
							<tbody>
								{filteredData.map((item, index) => (
									<tr key={index}>
										<td>{item.la_land_status}</td>
										<td className="center-align">
											({item.count})
										</td>
										<td className={styles.actionCol}>
											<button
												className={styles.editBtn}
												onClick={() => handleEditClick(item)}
												title="Edit"
											>
												<FaEdit />
											</button>

											{item.count === 0 && (
												<button
													className={styles.deleteBtn}
													onClick={() => handleDelete(item)}
													title="Delete"
												>
													<FaTrash />
												</button>
											)}
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
					Showing {filteredData.length} entries of {filteredData.length} entries
					</div>
				</div>
			</div>

			{/* ================= MODAL ================= */}
			{showModal && (
				<div className={styles.modalOverlay}>
					<div className={styles.modal}>
						<div className={styles.modalHeader}>
							<span>
								{mode === "add" ? "Add Status" : "Update Status"}
							</span>
							<span
								className={styles.close}
								onClick={() => setShowModal(false)}
							>
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
