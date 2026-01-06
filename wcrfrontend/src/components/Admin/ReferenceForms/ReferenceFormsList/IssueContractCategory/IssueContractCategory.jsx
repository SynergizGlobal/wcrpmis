import React, { useEffect, useState } from "react";
import { API_BASE_URL } from "../../../../../config.js";
import { FaEdit, FaTrash, FaPlus, FaTimes } from "react-icons/fa";
import styles from "./IssueContractCategory.module.css";
import Swal from "sweetalert2";

export default function IssueContractCategory() {
	const [rows, setRows] = useState([]);
	const [contractTypes, setContractTypes] = useState([]);
	const [issueCategories, setIssueCategories] = useState([]);

	const [showModal, setShowModal] = useState(false);
	const [mode, setMode] = useState("add");
	const [search, setSearch] = useState("");

	const [issueCategory, setIssueCategory] = useState("");
	const [issueCategoryOld, setIssueCategoryOld] = useState("");
	const [contractRows, setContractRows] = useState([""]);
	
	/* ================= FETCH ================= */
	const fetchData = async () => {
		const res = await fetch(`${API_BASE_URL}/issue-contract-category`, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify({})
		});

		const json = await res.json();

		if (json.status === "success") {
			setRows(
				(json.issueContractCategory || []).map((item, idx) => ({
					id: idx + 1,
					issueCategory: item.issue_category_fk,
					contractCategories: item.contract_category_fk
						? item.contract_category_fk.split(",")
						: []
				}))
			);

			setIssueCategories(
				(json.issueCategoryDetails || []).map(i => ({
					value: i.issue_category_fk,
					label: i.issue_category_fk
				}))
			);

			setContractTypes(
				(json.contractTypeDetails || []).map(c => ({
					value: c.contract_category_fk,
					label: c.contract_category_fk
				}))
			);
		}
	};

	useEffect(() => {
		fetchData();
	}, []);
	
	const filteredRows = rows.filter(r =>
	  r.issueCategory.toLowerCase().includes(search.toLowerCase()) ||
	  r.contractCategories.join(", ").toLowerCase().includes(search.toLowerCase())
	);
	

	/* ================= ROW HANDLERS ================= */
	const addContractRow = () => setContractRows([...contractRows, ""]);
	const removeContractRow = i =>
		setContractRows(contractRows.filter((_, idx) => idx !== i));

	const updateContractRow = (i, val) => {
		const updated = [...contractRows];
		updated[i] = val;
		setContractRows(updated);
	};

	/* ================= ADD ================= */
	const handleAdd = () => {
		setMode("add");
		setIssueCategory("");
		setContractRows([""]);
		setShowModal(true);
	};

	/* ================= EDIT ================= */
	const handleEdit = row => {
		setMode("edit");
		setIssueCategory(row.issueCategory);
		setIssueCategoryOld(row.issueCategory);
		setContractRows(row.contractCategories.length ? row.contractCategories : [""]);
		setShowModal(true);
	};

	const handleSave = async () => {
		if (!issueCategory || contractRows.length === 0) {
			alert("All fields required");
			return;
		}

		// ✅ CLEAN DATA
		const cleanedContracts = contractRows
			.map(v => v.trim())
			.filter(v => v && v !== "_");

		if (cleanedContracts.length === 0) {
			alert("Please select valid Contract Categories");
			return;
		}

		// 🔥 CRITICAL FIX
		const jspContractValue =
			cleanedContracts.length === 1
				? cleanedContracts[0]
				: cleanedContracts.join(",");

		const formData = new FormData();

		if (mode === "add") {
			formData.append("issue_category_fk", issueCategory);
			formData.append("contract_category_fk", jspContractValue);
		} else {
			formData.append("issue_category_fk_old", issueCategoryOld);
			formData.append("issue_category_fk_new", issueCategory);
			formData.append("contract_category_fk_new", jspContractValue);
			formData.append("value_old", jspContractValue);
		}

		await fetch(
			`${API_BASE_URL}/${mode === "add"
				? "add-issue-contract-category"
				: "update-issue-contract-category"
			}`,
			{ method: "POST", body: formData }
		);

		setShowModal(false);
		fetchData();
	};



	/* ================= DELETE ================= */
	const handleDelete = row => {
		Swal.fire({
			title: "Are you sure?",
			text: "You will be deleting the entire Issue Contract Category mapping!",
			icon: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			cancelButtonColor: "#3085d6",
			confirmButtonText: "Yes, delete it!",
			cancelButtonText: "No, cancel",
			reverseButtons: true
		}).then(async result => {
			if (result.isConfirmed) {
				const formData = new FormData();
				formData.append("issue_category_fk", row.issueCategory);

				await fetch(`${API_BASE_URL}/delete-issue-contract-category`, {
					method: "POST",
					body: formData
				});

				Swal.fire(
					"Deleted!",
					"Issue Contract Category has been deleted.",
					"success"
				);

				fetchData();
			} else if (result.dismiss === Swal.DismissReason.cancel) {
				Swal.fire(
					"Cancelled",
					"Record is safe 🙂",
					"error"
				);
			}
		});
	};


	return (
		<div className={styles.container}>
			<div className="card">
				<div className="formHeading">
					<h2 className="center-align">Issue Contract Category</h2>
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
									<th>Issue Category</th>
									<th>Contract Category</th>
									<th className={styles.actionCol}>Action</th>
								</tr>
							</thead>
							<tbody>
							  {filteredRows.map(r => (
							    <tr key={r.id}>
							      {/* ISSUE CATEGORY */}
							      <td>{r.issueCategory}</td>

							      {/* CONTRACT CATEGORIES → vertical like Responsible Executives */}
							      <td>
							        {Array.isArray(r.contractCategories) &&
							        r.contractCategories.length > 0 ? (
							          r.contractCategories.map((cat, idx) => (
							            <div key={idx}>
							              &#9656; {cat}
							            </div>
							          ))
							        ) : (
							          <span>-</span>
							        )}
							      </td>

							      {/* ACTIONS */}
							      <td className={styles.actionCol}>
							        <button
							          className={styles.editBtn}
							          onClick={() => handleEdit(r)}
							          title="Edit"
							        >
							          <FaEdit />
							        </button>

							        <button
							          className={styles.deleteBtn}
							          onClick={() => handleDelete(r)}
							          title="Delete"
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
							Showing {filteredRows.length} of {filteredRows.length} entries
						</div>
					</div>
				</div>

				{/* ================= MODAL ================= */}
				{showModal && (
					<div className={styles.modalOverlay}>
						<div className={styles.modal}>
							<div className={styles.modalHeader}>
								<h4>{mode === "add" ? "Add" : "Update"} Contract Category</h4>
								<span className={styles.close} onClick={() => setShowModal(false)}>
									✕
								</span>
							</div>

							<div className={styles.modalBody}>
								<div className={styles.row}>
									<div className={styles.col}>
										<p className={styles.searchableLabel}>Category</p>
										<select
											className={styles.select}
											value={issueCategory}
											onChange={e => setIssueCategory(e.target.value)}
										>
											<option value="">Select</option>
											{issueCategories.map(i => (
												<option key={i.value} value={i.value}>
													{i.label}
												</option>
											))}
										</select>
									</div>

									<div className={`${styles.col} ${styles.boxGrey}`}>
										{contractRows.map((v, i) => (
											<div key={i} className={styles.contractRow}>
												<select
													className={styles.select}
													value={v}
													onChange={e => updateContractRow(i, e.target.value)}
												>
													<option value="">Select Contract</option>
													{contractTypes.map(c => (
														<option key={c.value} value={c.value}>
															{c.label}
														</option>
													))}
												</select>

												{contractRows.length > 1 && (
													<button
														className={styles.removeBtn}
														onClick={() => removeContractRow(i)}
													>
														<FaTimes />
													</button>
												)}
											</div>
										))}

										<button className="btn btn-primary" onClick={addContractRow}>
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
