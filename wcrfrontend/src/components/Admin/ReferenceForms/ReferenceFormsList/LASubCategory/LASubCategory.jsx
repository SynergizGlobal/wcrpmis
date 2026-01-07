import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash, FaPlus, FaTimes } from "react-icons/fa";
import styles from "./LASubCategory.module.css";
import Swal from "sweetalert2";

export default function LASubCategory() {
	const [rows, setRows] = useState([]);
	const [categories, setCategories] = useState([]);

	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");
	const [search, setSearch] = useState("");

	const [category, setCategory] = useState("");
	const [categoryOld, setCategoryOld] = useState("");
	const [subRows, setSubRows] = useState([""]);

	/* ================= FETCH ================= */
	const fetchData = async () => {
		try {
			const res = await fetch(`${API_BASE_URL}/la-sub-category`, {
				method: "GET"
			});
			const json = await res.json();
			if (!json.status) return;

			/* CATEGORY DROPDOWN */
			setCategories(
				(json.LACategorysList || []).map(c => ({
					value: c.la_category,
					label: c.la_category
				}))
			);

			/* TABLE ROWS */
			const rowsData =
				(json.landAcquisitionSubCategoryDetails?.dList1 || []).map(
					(item, idx) => ({
						id: idx + 1,
						la_category_fk: item.la_category_fk,
						la_sub_category: item.la_sub_category
							? item.la_sub_category.split(",")
							: []
					})
				);

			setRows(rowsData);
		} catch (err) {
			console.error("Failed to load LA Sub Category", err);
		}
	};

	useEffect(() => {
		fetchData();
	}, []);

	/* ================= FILTER ================= */
	const filteredRows = rows.filter(r =>
		r.la_category_fk
			.toLowerCase()
			.includes(search.toLowerCase())
	);

	/* ================= ROW HELPERS ================= */
	const addSubRow = () => setSubRows([...subRows, ""]);
	const removeSubRow = i =>
		setSubRows(subRows.filter((_, idx) => idx !== i));

	const updateSubRow = (i, val) => {
		const updated = [...subRows];
		updated[i] = val;
		setSubRows(updated);
	};

	/* ================= ADD ================= */
	const handleAdd = () => {
		setMode("add");
		setCategory("");
		setSubRows([""]);
		setShowModal(true);
	};

	/* ================= EDIT ================= */
	const handleEdit = row => {
		setMode("edit");
		setCategory(row.la_category_fk);
		setCategoryOld(row.la_category_fk);
		setSubRows(row.la_sub_category.length ? row.la_sub_category : [""]);
		setShowModal(true);
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		if (!category) {
			Swal.fire("Error", "Category is required", "error");
			return;
		}

		const cleanedSubs = subRows
			.map(v => v.trim())
			.filter(v => v);

		if (cleanedSubs.length === 0) {
			Swal.fire("Error", "At least one Sub Category required", "error");
			return;
		}

		const payload =
			mode === "add"
				? {
						la_category_fk: category,
						la_sub_category: cleanedSubs.join(",")
				  }
				: {
						la_category_fk_old: categoryOld,
						la_category_fk_new: category,
						la_sub_category_new: cleanedSubs.join(","),
						value_old: cleanedSubs.join(",")
				  };

		try {
			await fetch(
				`${API_BASE_URL}/${
					mode === "add"
						? "add-la-sub-category"
						: "update-la-sub-category"
				}`,
				{
					method: "POST",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify(payload)
				}
			);

			setShowModal(false);
			fetchData();
		} catch (err) {
			console.error("Save failed", err);
			Swal.fire("Error", "Server error", "error");
		}
	};

	/* ================= DELETE ================= */
	const handleDelete = row => {
		Swal.fire({
			title: "Are you sure?",
			text: "This will delete all sub categories under this category",
			icon: "warning",
			showCancelButton: true,
			confirmButtonText: "Yes, delete",
			cancelButtonText: "Cancel"
		}).then(async result => {
			if (result.isConfirmed) {
				await fetch(`${API_BASE_URL}/delete-la-sub-category`, {
					method: "POST",
					headers: { "Content-Type": "application/json" },
					body: JSON.stringify({
						la_category_fk: row.la_category_fk
					})
				});
				fetchData();
			}
		});
	};

	/* ================= JSX ================= */
	return (
		<div className={styles.container}>
			<div className="card">
				<div className="formHeading">
					<h2 className="center-align">LA Sub Category</h2>
				</div>

				<div className="innerPage">
					<div className={styles.topActions}>
						<button className="btn btn-primary" onClick={handleAdd}>
							+ Add
						</button>
					</div>

					<div className={styles.searchBox}>
						<input
							type="text"
							placeholder="Search Category"
							value={search}
							onChange={e => setSearch(e.target.value)}
						/>
					</div>

					<div className={`dataTable ${styles.tableWrapper}`}>
						<table className={styles.table}>
							<thead>
								<tr>
									<th>Category</th>
									<th>Sub Categories</th>
									<th className={styles.actionCol}>Action</th>
								</tr>
							</thead>
							<tbody>
								{filteredRows.map(r => (
									<tr key={r.id}>
										<td>{r.la_category_fk}</td>
										<td>
											{r.la_sub_category.map((s, i) => (
												<div key={i}>▸ {s}</div>
											))}
										</td>
										<td className={styles.actionCol}>
											<button
												className={styles.editBtn}
												onClick={() => handleEdit(r)}
											>
												<FaEdit />
											</button>
											<button
												className={styles.deleteBtn}
												onClick={() => handleDelete(r)}
											>
												<FaTrash />
											</button>
										</td>
									</tr>
								))}

								{filteredRows.length === 0 && (
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
					Showing {filteredRows.length} entries of {filteredRows.length} entries
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
									? "Add LA Sub Category"
									: "Update LA Sub Category"}
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
								{/* LEFT COLUMN : CATEGORY */}
								<div className={styles.col}>
									<p className={styles.searchableLabel}>
										Category
									</p>
									<select
										className={styles.select}
										value={category}
										onChange={e => setCategory(e.target.value)}
									>
										<option value="">Select</option>
										{categories.map(c => (
											<option key={c.value} value={c.value}>
												{c.label}
											</option>
										))}
									</select>
								</div>

								{/* RIGHT COLUMN : SUB CATEGORIES */}
								<div className={`${styles.col} ${styles.boxGrey}`}>
									{subRows.map((v, i) => (
										<div key={i} className={styles.contractRow}>
											<input
												type="text"
												className={styles.select}
												value={v}
												onChange={e =>
													updateSubRow(i, e.target.value)
												}
												placeholder="Sub Category"
											/>

											{subRows.length > 1 && (
												<button
													className={styles.removeBtn}
													onClick={() => removeSubRow(i)}
												>
													<FaTimes />
												</button>
											)}
										</div>
									))}

									<button
										className="btn btn-primary"
										onClick={addSubRow}
									>
										<FaPlus /> Add
									</button>
								</div>
							</div>

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
