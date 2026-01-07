import React, { useEffect, useState } from "react";
import styles from "./LAFileType.module.css";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";

export default function LAFileType() {
	const [data, setData] = useState([]);
	const [search, setSearch] = useState("");

	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");
	const [value, setValue] = useState("");
	const [oldValue, setOldValue] = useState("");

	/* ================= LOAD ================= */
	const fetchLAFileTypes = async () => {
		try {
			const res = await fetch(`${API_BASE_URL}/la-file-type`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({})
			});

			const json = await res.json();
			if (json.status !== "SUCCESS") return;

			const fileTypeList =
				json.laFileTypeDetails?.dList1 || [];

			const countList =
				json.laFileTypeDetails?.countList || [];

			// Map counts → { "Sale Deed": 3 }
			const countMap = {};
			countList.forEach(c => {
				countMap[c.la_file_type] = Number(c.count || 0);
			});

			// Merge file type + count
			const merged = fileTypeList.map(f => ({
				la_file_type: f.la_file_type,
				count: countMap[f.la_file_type] || 0
			}));

			setData(merged);
		} catch (err) {
			console.error("Failed to load LA File Type", err);
		}
	};

	useEffect(() => {
		fetchLAFileTypes();
	}, []);

	/* ================= FILTER ================= */
	const filteredData = data.filter(item =>
		item.la_file_type
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
		setValue(item.la_file_type);
		setOldValue(item.la_file_type);
		setShowModal(true);
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		if (!value.trim()) return;

		try {
			const url =
				mode === "add"
					? "/add-la-file-type"
					: "/update-la-file-type";

			const payload =
				mode === "add"
					? { la_file_type: value.trim() }
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
				fetchLAFileTypes();
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

		if (!window.confirm(`Are you sure you want to delete "${item.la_file_type}"?`))
			return;

		try {
			const res = await fetch(`${API_BASE_URL}/delete-la-file-type`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ la_file_type: item.la_file_type })
			});

			const json = await res.json();

			if (json.status === "SUCCESS") {
				alert(json.message || "Deleted successfully");
				fetchLAFileTypes();
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
					<h2 className="center-align">File Type</h2>
				</div>

				<div className="innerPage">
					<div className={styles.topActions}>
						<button className="btn btn-primary" onClick={handleAddClick}>
							+ Add File Type
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
									<th>File Type</th>
									<th>LA Files</th>
									<th className={styles.actionCol}>Action</th>
								</tr>
							</thead>
							<tbody>
								{filteredData.map((item, index) => {
									const canDelete = !item.count || item.count === 0;

									return (
										<tr key={index}>
											<td>{item.la_file_type}</td>
											<td>{item.count}</td>

											<td className={styles.actionCol}>
												<div className={styles.actionButtons}>
													{/* EDIT → always visible */}
													<button
														className={styles.editBtn}
														onClick={() => handleEditClick(item, index)}
														title="Edit"
													>
														<FaEdit />
													</button>

													{/* DELETE → only if no dependency */}
													{canDelete && (
														<button
															className={styles.deleteBtn}
															onClick={() => handleDelete(item)}
															title="Delete"
														>
															<FaTrash />
														</button>
													)}
												</div>
											</td>
										</tr>
									);
								})}
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
								{mode === "add" ? "Add File Type" : "Update File Type"}
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
								placeholder="File Type"
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
