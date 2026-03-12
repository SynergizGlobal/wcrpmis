import React, { useState, useEffect } from "react";
import Select from "react-select";
import styles from "./ModifyActuals.module.css";
import { API_BASE_URL } from "../../../config";

export default function ModifyActuals() {

	const actions = [
		{ label: "Completed Scope / Actual Zero by Task Code", value: "completed" },
		{ label: "Delete activities by task code", value: "deleteTask" },
		{ label: "Delete activities by contract", value: "deleteContract" },
	];

	const [uploadedPage, setUploadedPage] = useState(1);
	const [uploadedPerPage, setUploadedPerPage] = useState(10);

	const [contractsOptions, setContractOptions] = useState([]);
	const [structureOptions, setStructureOptions] = useState([]);

	const [selectedAction, setSelectedAction] = useState(null);
	const [selectedContract, setSelectedContract] = useState(null);
	const [selectedStructure, setSelectedStructure] = useState(null);

	const [tableData, setTableData] = useState([]);
	const [checkedItems, setCheckedItems] = useState([]);

	const [search, setSearch] = useState("");
	const [isDataLoading, setIsDataLoading] = useState(false);

	// RESET FORM
	const handleReset = () => {
		setSelectedAction(null);
		setSelectedAction(null);
		setSelectedContract(null);
		setSelectedStructure(null);
		setStructureOptions([]);
		setTableData([]);
		setCheckedItems([]);
		setSearch("");
	};

	// CONTRACT LIST LOAD
	useEffect(() => {
		fetch(`${API_BASE_URL}/modify-actuals`, {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			credentials: "include",
			body: JSON.stringify({})
		})
			.then(res => res.json())
			.then(data => {

				setContractOptions(
					data.contractsList?.map(c => ({
						value: c.contract_id,
						label: `${c.contract_id}${c.contract_short_name ? " - " + c.contract_short_name : ""}`
					})) || []
				);

			});
	}, []);

	// LOAD STRUCTURES
	useEffect(() => {

		if (!selectedContract) return;

		fetch(`${API_BASE_URL}/ajax/getContractStructures?contract_id_fk=${selectedContract.value}`, {
			method: "GET",
			credentials: "include"
		})
			.then(res => res.json())
			.then(data => {

				setStructureOptions(
					data?.map(c => ({
						value: c.strip_chart_structure_id_fk,
						label: `${c.strip_chart_structure_id_fk}`
					})) || []
				);

			});

	}, [selectedContract]);

	// LOAD TABLE DATA
	useEffect(() => {

		setIsDataLoading(true);

		if (!selectedContract) {
			setIsDataLoading(false);
			return;
		}

		if (selectedAction === "deleteContract" && !selectedStructure) {
			setIsDataLoading(false);
			return;
		}

		const structureId = selectedStructure ? selectedStructure.value : "";

		fetch(`${API_BASE_URL}/ajax/getNewActivitiesfiltersList?contract_id_fk=${selectedContract.value}&strip_chart_structure_id_fk=${structureId}&searchStr=${search}`, {
			method: "GET",
			credentials: "include"
		})
			.then(res => res.json())
			.then(data => {
				setTableData(data || []);
				setUploadedPage(1);
				setIsDataLoading(false);
			})
			.catch(() => setIsDataLoading(false));

	}, [selectedContract, selectedStructure, search, selectedAction]);


	const uploadedTotalRows = tableData.length;
	const uploadedTotalPages = Math.ceil(uploadedTotalRows / uploadedPerPage);

	const uploadedStart = (uploadedPage - 1) * uploadedPerPage;
	const uploadedEnd = uploadedStart + uploadedPerPage;

	const paginatedTableData = tableData.slice(uploadedStart, uploadedEnd);


	const renderPageButtons = (page, totalPages, setPageFn) => {
		if (totalPages <= 1) return null;

		const pages = Array.from({ length: totalPages }, (_, i) => i + 1)
			.filter((p) => {
				if (p <= 2 || p > totalPages - 2) return true;
				if (p >= page - 1 && p <= page + 1) return true;
				return false;
			})
			.reduce((acc, p, idx, arr) => {
				if (idx > 0 && p - arr[idx - 1] > 1) acc.push("...");
				acc.push(p);
				return acc;
			}, []);

		return (
			<>
				<button
					disabled={page === 1}
					onClick={() => setPageFn(page - 1)}
					className="pageBtn"
				>
					‹
				</button>

				{pages.map((item, idx) =>
					item === "..." ? (
						<span key={idx} className="ellipsis">
							...
						</span>
					) : (
						<button
							key={idx}
							onClick={() => setPageFn(item)}
							className={`pageBtn ${item === page ? "activePage" : ""}`}
						>
							{item}
						</button>
					)
				)}

				<button
					disabled={page === totalPages}
					onClick={() => setPageFn(page + 1)}
					className="pageBtn"
				>
					›
				</button>
			</>
		);
	};


	// TABLE VISIBILITY
	const isShowTable =
		(selectedAction === "completed" && selectedContract) ||
		(selectedAction === "deleteTask" && selectedContract) ||
		(selectedAction === "deleteContract" && selectedContract && selectedStructure);

	// CHECKBOX HANDLER
	const handleCheckboxChange = (id) => {
		setCheckedItems((prev) =>
			prev.includes(id)
				? prev.filter((x) => x !== id)
				: [...prev, id]
		);
	};


	const loadTableData = () => {

		setIsDataLoading(true);

		if (!selectedContract) {
			setIsDataLoading(false);
			return;
		}

		if (selectedAction === "deleteContract" && !selectedStructure) {
			setIsDataLoading(false);
			return;
		}

		const structureId = selectedStructure ? selectedStructure.value : "";

		fetch(`${API_BASE_URL}/ajax/getNewActivitiesfiltersList?contract_id_fk=${selectedContract.value}&strip_chart_structure_id_fk=${structureId}&searchStr=${search}`, {
			method: "GET",
			credentials: "include"
		})
			.then(res => res.json())
			.then(data => {
				setTableData(data || []);
				setUploadedPage(1);
				setIsDataLoading(false);
			})
			.catch(() => setIsDataLoading(false));

	};


	useEffect(() => {
		loadTableData();
	}, [selectedContract, selectedStructure, search, selectedAction]);



	const handleUpdate = async () => {

		if (checkedItems.length === 0) {
			alert("Please select at least one activity");
			return;
		}

		const selectedRows = tableData.filter(row =>
			checkedItems.includes(row.activity_id)
		);

		const payload = {
			pending:
				selectedAction === "completed"
					? 1
					: selectedAction === "deleteTask"
						? 2
						: 3,

			contract_id_fk: selectedContract?.value || null,

			strip_chart_structure_id_fk:
				selectedAction === "deleteContract"
					? selectedStructure?.value
					: null,

			p6_task_codes: selectedRows.map(r => r.p6_task_code),
			activity_ids: selectedRows.map(r => r.activity_id),
			totalScopes: selectedRows.map(r => r.scope),
			completedScopes: selectedRows.map(r => r.completed),

			ids: selectedRows.map(() => 1)
		};

		try {

			if (!window.confirm("Are you sure you want to update selected activities?")) {
				return;
			}

			const res = await fetch(`${API_BASE_URL}/update-modify-actuals-bulk`, {
				method: "POST",
				headers: {
					"Content-Type": "application/json"
				},
				credentials: "include",
				body: JSON.stringify(payload)
			});

			const result = await res.text();

			if (res.ok) {

				alert(result);

				setCheckedItems([]);
				loadTableData();
				setUploadedPage(1);

			} else {
				alert("Update failed");
			}

		} catch (error) {
			console.error(error);
			alert("Server error");
		}

	};

	return (

		<div className={styles.container}>

			<div className="pageHeading">
				<h2>Modify Actuals</h2>
				<div className="rightBtns">
					&nbsp;
				</div>
			</div>

			{/* ACTION RADIO */}
			<div className={styles.radioRow}>
				<span><strong>Action</strong></span>

				{actions.map((a, i) => (
					<label key={i} className={styles.radioLabel}>

						<input
							type="radio"
							name="action"
							value={a.value}
							checked={selectedAction === a.value}
							onChange={() => {
								setSelectedAction(a.value);
								setSelectedContract(null);
								setSelectedStructure(null);
								setCheckedItems([]);
								setTableData([]);
							}}
						/>

						{a.label}

					</label>
				))}
			</div>

			{/* FORM FIELDS */}

			<div className={styles.fieldsSection}>

				<div className="form-row">

					{/* CONTRACT */}

					{selectedAction && (

						<div className="form-field">

							<label>Contract *</label>

							<Select
								placeholder="Select"
								isClearable
								value={selectedContract}
								options={contractsOptions}
								onChange={(v) => setSelectedContract(v)}
							/>

						</div>

					)}

					{/* STRUCTURE */}

					{selectedAction === "deleteContract" && (

						<div className="form-field">

							<label>Structure *</label>

							<Select
								placeholder="Select"
								isClearable
								value={selectedStructure}
								options={structureOptions}
								onChange={(v) => setSelectedStructure(v)}
							/>

						</div>

					)}

				</div>

				{/* SEARCH + BUTTONS */}

				{selectedAction && (

					<div className={styles.searchBtns}>

						<div className="form-post-buttons">

							<button
								className="btn btn-primary"
								disabled={checkedItems.length === 0 || !isShowTable}
								style={{
									backgroundColor:
										checkedItems.length === 0 ? "#bcbcbc" : "var(--primary-color)",
									cursor: checkedItems.length === 0 ? "not-allowed" : "pointer",
								}}
								onClick={handleUpdate}
							>
								Update
							</button>

							<button
								className="btn btn-secondary"
								onClick={handleReset}
							>
								Reset
							</button>

						</div>



					</div>

				)}



			</div>

			{/* TABLE */}

			{isShowTable && (




				



					<div className={`dataTable ${styles.tableWrapper}`}>
					
				<div className={styles.tableToolbar}>
						<div className={styles.showEntriesCount}>
							<label>Show</label>

							<select
								value={uploadedPerPage}
								onChange={(e) => {
									setUploadedPerPage(Number(e.target.value));
									setUploadedPage(1);
								}}
							>
								{[5, 10, 20, 50, 100].map((size) => (
									<option key={size} value={size}>
										{size}
									</option>
								))}
							</select>

							<span>entries</span>
						</div>

						<div className={styles.searchBox}>
							<label>Search</label>

							<input
								type="text"
								placeholder="Search.."
								value={search}
								onChange={(e) => setSearch(e.target.value)}
							/>
						</div>

					</div>

						<table className={styles.dataTable}>

							<thead>

								<tr>

									<th></th>
									<th>Task Code</th>
									<th>Activity</th>
									<th>Scope</th>
									<th>Completed</th>

								</tr>

							</thead>

							<tbody>

								{isDataLoading ? (

									<tr>

										<td colSpan="5" style={{ textAlign: "center" }}>
											Loading...
										</td>

									</tr>

								) : paginatedTableData.length > 0 ? (

									paginatedTableData.map((dd, index) => (

										<tr key={dd.activity_id ?? index}>

											<td>

												<input
													type="checkbox"
													checked={checkedItems.includes(dd.activity_id)}
													onChange={() =>
														handleCheckboxChange(dd.activity_id)
													}
												/>

											</td>

											<td>{dd.p6_task_code}</td>

											<td>
												{dd.strip_chart_activity_name} ({dd.unit_fk})
											</td>

											<td>{Number(dd.scope)}</td>

											<td>{Number(dd.completed)}</td>

										</tr>

									))

								) : (

									<tr>

										<td colSpan="5" style={{ textAlign: "center" }}>
											No records found
										</td>

									</tr>

								)}

							</tbody>

						</table>

						<div className="paginationRow" style={{ marginTop: 12 }}>

							<div className="entryCount">
								Showing{" "}
								{uploadedTotalRows === 0
									? 0
									: Math.min(uploadedStart + 1, uploadedTotalRows)}{" "}
								– {Math.min(uploadedEnd, uploadedTotalRows)} of{" "}
								{uploadedTotalRows} entries
							</div>

							<div className="pagination">
								{renderPageButtons(uploadedPage, uploadedTotalPages, setUploadedPage)}
							</div>

						</div>

					</div>


			)}

		</div>

	);
}