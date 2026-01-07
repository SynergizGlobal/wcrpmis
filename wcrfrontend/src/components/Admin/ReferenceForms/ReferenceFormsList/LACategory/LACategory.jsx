import React, { useEffect, useState } from "react";
import styles from "./LACategory.module.css";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";

export default function LACategory() {
	const [data, setData] = useState([]);
	const [search, setSearch] = useState("");

	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");
	const [value, setValue] = useState("");
	const [oldValue, setOldValue] = useState("");

	/* ================= LOAD LA CATEGORY ================= */
	const fetchLACategory = async () => {
		try {
			const res = await fetch(`${API_BASE_URL}/la-category`, {
				method: "GET"
			});

			const json = await res.json();
			if (!json.status) return;

			// Main category list
			const list = json.LACategoryList || [];

			// Count list from backend
			const countList =
				json.landAcquisitionCategoryDetails?.countList || [];

			/*
			  countMap structure:
			  {
				"Category A": {
				  landIdentification: 55,
				  subCategory: 21
				}
			  }
			*/
			const countMap = {};

			countList.forEach(c => {
				const category = c.la_category;
				const table = c.tName;
				const count = Number(c.count || 0);

				if (!countMap[category]) {
					countMap[category] = {
						landIdentification: 0,
						subCategory: 0
					};
				}

				if (table === "la_land_identification") {
					countMap[category].landIdentification = count;
				}

				if (table === "la_sub_category") {
					countMap[category].subCategory = count;
				}
			});

			// Merge category + split counts
			const merged = list.map(c => ({
				la_category: c.la_category,
				landIdentification:
					countMap[c.la_category]?.landIdentification || 0,
				subCategory:
					countMap[c.la_category]?.subCategory || 0
			}));

			setData(merged);
		} catch (err) {
			console.error("Failed to load LA Category", err);
		}
	};

	useEffect(() => {
		fetchLACategory();
	}, []);

	/* ================= FILTER ================= */
	const filteredData = data.filter(item =>
		item.la_category
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
		setValue(item.la_category);
		setOldValue(item.la_category);
		setShowModal(true);
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		if (!value.trim()) return;

		const url =
			mode === "add"
				? "/add-la-category"
				: "/update-la-category";

		const payload =
			mode === "add"
				? { la_category: value.trim() }
				: {
					value_old: oldValue,
					value_new: value.trim()
				};

		try {
			const res = await fetch(`${API_BASE_URL}${url}`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify(payload)
			});

			const json = await res.json();

			if (json.status) {
				alert(json.message || "Operation successful");
				setShowModal(false);
				fetchLACategory();
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
		if (item.count > 0) return;

		if (
			!window.confirm(
				`Are you sure you want to delete "${item.la_category}"?`
			)
		)
			return;

		try {
			const res = await fetch(`${API_BASE_URL}/delete-la-category`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({
					la_category: item.la_category
				})
			});

			const json = await res.json();

			if (json.status) {
				alert(json.message || "Deleted successfully");
				fetchLACategory();
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
					<h2 className="center-align">
						Land Acquisition Category
					</h2>
				</div>

				<div className="innerPage">
					<div className={styles.topActions}>
						<button
							className="btn btn-primary"
							onClick={handleAddClick}
						>
							+ Add Category
						</button>
					</div>

					<div className={styles.searchBox}>
						<input
							type="text"
							placeholder="Search"
							value={search}
							onChange={e => setSearch(e.target.value)}
						/>
					</div>

					<div className={`dataTable ${styles.tableWrapper}`}>
						<table className={styles.table}>
							<thead>
								<tr>
									<th>Category</th>
									<th className="center-align">Land Identification</th>
									<th className="center-align">Sub Category</th>
									<th className={styles.actionCol}>Action</th>
								</tr>
							</thead>

							<tbody>
								{filteredData.map((item, index) => {
									const canDelete =
										item.landIdentification === 0 &&
										item.subCategory === 0;

									return (
										<tr key={index}>
											<td>{item.la_category}</td>

											<td className="center-align">
												{item.landIdentification > 0
													? `(${item.landIdentification})`
													: ""}
											</td>

											<td className="center-align">
												{item.subCategory > 0
													? `(${item.subCategory})`
													: ""}
											</td>

											<td className={styles.actionCol}>
												<button
													className={styles.editBtn}
													onClick={() => handleEditClick(item)}
													title="Edit"
												>
													<FaEdit />
												</button>

												{canDelete && (
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
									);
								})}


								{filteredData.length === 0 && (
									<tr>
										<td colSpan={4} className="center-align">
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
								{mode === "add"
									? "Add Category"
									: "Update Category"}
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
								placeholder="Category"
								value={value}
								onChange={e =>
									setValue(e.target.value)
								}
							/>

							<div className={styles.modalActions}>
								<button
									className="btn btn-primary"
									onClick={handleSave}
								>
									{mode === "add" ? "ADD" : "UPDATE"}
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
