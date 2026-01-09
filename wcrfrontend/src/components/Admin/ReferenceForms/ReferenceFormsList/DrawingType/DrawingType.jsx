import React, { useEffect, useState } from "react";
import styles from "./DrawingType.module.css";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash } from "react-icons/fa";

export default function DrawingType() {
	const [data, setData] = useState([]);
	const [showModal, setShowModal] = useState(false);
	const [search, setSearch] = useState("");
	const [value, setValue] = useState("");
	const [mode, setMode] = useState("add");
	const [selectedRow, setSelectedRow] = useState(null);
	const [columns, setColumns] = useState([]);

	/* ================= FETCH ================= */
	const fetchDrawingTypes = async () => {
		try {
			const res = await fetch(`${API_BASE_URL}/drawing-type`, {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({})
			});

			const json = await res.json();

			const drawingTypeList = json.drawingTypeList || [];
			const tablesList = json.drawingTypeDetails?.tablesList || [];
			const countList = json.drawingTypeDetails?.countList || [];

			/* ---- DISTINCT FK TABLE NAMES ---- */
			const columnNames = [
				...new Set(
					tablesList
						.map(t => t?.tName)
						.filter(Boolean)
				)
			];

			setColumns(columnNames);

			/* ---- drawing_type | table -> count ---- */
			const countMap = {};
			countList.forEach(c => {
				if (c?.drawing_type && c?.tName) {
					countMap[`${c.drawing_type}|${c.tName}`] = Number(c.count || 0);
				}
			});

			/* ---- MERGE DATA ---- */
			const merged = drawingTypeList.map((d, idx) => {
				const counts = {};
				columnNames.forEach(col => {
					counts[col] = countMap[`${d.drawing_type}|${col}`] || 0;
				});

				return {
					id: idx + 1,
					drawingType: d.drawing_type ?? "",
					counts
				};
			});

			setData(merged);

		} catch (err) {
			console.error("Failed to load Drawing Type", err);
		}
	};


	useEffect(() => {
		fetchDrawingTypes();
	}, []);

	/* ================= FILTER ================= */
	const filteredData = data.filter(item => {
		const term = search.toLowerCase();
		return (
			(item.drawingType || "").toLowerCase().includes(term) ||
			String(item.designCount || "").includes(term)
		);
	});

	/* ================= ADD ================= */
	const handleAddClick = () => {
		setMode("add");
		setValue("");
		setSelectedRow(null);
		setShowModal(true);
	};

	/* ================= EDIT ================= */
	const handleEditClick = row => {
		setMode("edit");
		setValue(row.drawingType);
		setSelectedRow(row); // ✅ FIXED
		setShowModal(true);
	};

	/* ================= SAVE ================= */
	const handleSave = async () => {
		if (!value.trim()) {
			window.alert("Drawing Type is required");
			return;
		}

		const formData = new FormData();

		if (mode === "add") {
			formData.append("drawing_type", value.trim());

			await fetch(`${API_BASE_URL}/add-drawing-type`, {
				method: "POST",
				body: formData
			});

			window.alert("Drawing Type added successfully.");
		} else if (mode === "edit" && selectedRow) {
			formData.append("value_old", selectedRow.drawingType);
			formData.append("value_new", value.trim());

			await fetch(`${API_BASE_URL}/update-drawing-type`, {
				method: "POST",
				body: formData
			});

			window.alert("Drawing Type updated successfully.");
		}

		setShowModal(false);
		setValue("");
		setSelectedRow(null);
		fetchDrawingTypes();
	};

	/* ================= DELETE ================= */
	const handleDelete = async row => {
		if (row.designCount > 0) {
			window.alert("Cannot delete. Drawing Type is in use.");
			return;
		}

		if (!window.confirm(`Delete "${row.drawingType}" ?`)) return;

		const formData = new FormData();
		formData.append("drawing_type", row.drawingType);

		await fetch(`${API_BASE_URL}/delete-drawing-type`, {
			method: "POST",
			body: formData
		});

		window.alert("Drawing Type deleted successfully.");
		fetchDrawingTypes();
	};

	return (
		<div className={styles.container}>
			<div className="card">
				<div className="formHeading">
					<h2 className="center-align">Drawing Type</h2>
				</div>

				<div className="innerPage">
					<div className={styles.topActions}>
						<button className="btn btn-primary" onClick={handleAddClick}>
							+ Add Drawing Type
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
									<th>Drawing Type</th>
									{columns.map(c => (
										<th key={c}>{c.toUpperCase()}</th>
									))}
									<th className={styles.actionCol}>Action</th>
								</tr>
							</thead>
							<tbody>
								{filteredData.map(row => (
									<tr key={row.id}>
										<td>{row.drawingType}</td>
										{columns.map(c => (
											<td key={c}>
												{row.counts[c] ? `(${row.counts[c]})` : ""}
											</td>
										))}
						<td className={styles.actionCol}>
											<button
												className={styles.editBtn}
												onClick={() => handleEditClick(row)}
											>
												<FaEdit />
											</button>
											{row.designCount === 0 && <button
												className={styles.deleteBtn}
												onClick={() => handleDelete(row)}
											>
												<FaTrash />
											</button>
											}
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
						Showing {filteredData.length} of {data.length} entries
					</div>
				</div>
			</div>

			{showModal && (
				<div className={styles.modalOverlay}>
					<div className={styles.modal}>
						<div className={styles.modalHeader}>
							<span>{mode === "add" ? "Add" : "Edit"} Drawing Type</span>
							<span className={styles.close} onClick={() => setShowModal(false)}>
								✕
							</span>
						</div>

						<div className={styles.modalBody}>
							<input
								type="text"
								value={value}
								onChange={e => setValue(e.target.value)}
								placeholder="Drawing Type"
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
