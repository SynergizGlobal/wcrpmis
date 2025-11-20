import React, { useContext, useState, useEffect, useCallback } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import styles from "./DesignDrawing.module.css";
import { CirclePlus } from "lucide-react";
import { LuCloudDownload, LuUpload, LuDownload } from "react-icons/lu";
import { FaSearch } from "react-icons/fa";
import api from "../../../api/axiosInstance";
import { Outlet, useNavigate, useLocation } from "react-router-dom";
import { API_BASE_URL } from "../../../config";
import { MdEditNote } from 'react-icons/md';

export default function DesignDrawing() {
	const location = useLocation();
	const navigate = useNavigate();
	const { refresh } = useContext(RefreshContext);

	// DESIGN list (backend-paginated)
	const [designs, setDesigns] = useState([]);
	const [designTotalRows, setDesignTotalRows] = useState(0);
	const [designPage, setDesignPage] = useState(1);
	const [designPerPage, setDesignPerPage] = useState(10);
	const [designLoading, setDesignLoading] = useState(false);
	const [designSearch, setDesignSearch] = useState("");

	// UPLOADED FILES (client-side)
	const [uploadedFile, setUploadedFile] = useState([]);
	const [uploadedSearch, setUploadedSearch] = useState("");
	const [uploadedPage, setUploadedPage] = useState(1);
	const [uploadedPerPage, setUploadedPerPage] = useState(10);

	const [filters, setFilters] = useState({
		contract: "",
		structureType: "",
		drawingType: "",
	});

	const [filterOptions, setFilterOptions] = useState({
	  contract: [],
	  structureType: [],
	  drawingType: [],
	});

	const isDesignDrawingForm = location.pathname.endsWith("/add-design-form");

	// Helper to compute total pages
	const designTotalPages = Math.max(1, Math.ceil(designTotalRows / designPerPage));
	const uploadedFiltered = uploadedFile.filter((uf) => {
		if (!uploadedSearch) return true;
		const s = uploadedSearch.toLowerCase();
		// search a few fields
		return (
			(uf.filePath || "").toString().toLowerCase().includes(s) ||
			(uf.status || "").toString().toLowerCase().includes(s) ||
			(uf.remarks || "").toString().toLowerCase().includes(s) ||
			(uf.uploaded_by_user_id_fk || "").toString().toLowerCase().includes(s)
		);
	});
	const uploadedTotalRows = uploadedFiltered.length;
	const uploadedTotalPages = Math.max(1, Math.ceil(uploadedTotalRows / uploadedPerPage));
	const uploadedPageSlice = uploadedFiltered.slice(
		(uploadedPage - 1) * uploadedPerPage,
		uploadedPage * uploadedPerPage
	);

	useEffect(() => {
	  const fetchFilterData = async () => {
	    const endpoints = {
	      contract: `${API_BASE_URL}/design/ajax/getContractListFilterInDesign`,
	      structureType: `${API_BASE_URL}/design/ajax/getStructureListFilterInDesign`,
	      drawingType: `${API_BASE_URL}/design/ajax/getDrawingTypeListFilterInDesign`,
	    };

	    for (const [key, url] of Object.entries(endpoints)) {
	      try {
	        const resp = await fetch(url, {
	          method: "POST",
	          headers: { "Content-Type": "application/json" },
	          body: JSON.stringify({}),
	        });

	        const result = await resp.json();
	        const data = Array.isArray(result)
	          ? result
	          : result.data || result.list || [];

	        setFilterOptions((prev) => ({
	          ...prev,
	          [key]: data.map((item) => ({
	            value:
	              item.contract_id ??
	              item.structure_type_fk ??
	              item.drawing_type_fk ??
	              "",
	            label:
	              item.contract_id ??
	              item.structure_type_fk ??
	              item.drawing_type_fk ??
	              "Unknown",
	          })),
	        }));
	      } catch (err) {
	        console.error("Error fetching filter options for", key, err);
	      }
	    }
	  };

	  fetchFilterData();
	}, [refresh, location]);


	const fetchDesigns = useCallback(
	  async (
	    pageNumber = designPage,
	    pageSize = designPerPage,
	    searchText = designSearch,
	    currentFilters = filters
	  ) => {
	    setDesignLoading(true);
	    try {
	      const iDisplayStart = (pageNumber - 1) * pageSize;
	      const iDisplayLength = pageSize;

	      const params = new URLSearchParams({
	        iDisplayStart,
	        iDisplayLength,
	        sSearch: searchText || "",

	        contract_id_fk: currentFilters.contract || "",
	        structure_type_fk: currentFilters.structureType || "",
	        drawing_type_fk: currentFilters.drawingType || "",
	      });

	      const res = await api.post(
	        `${API_BASE_URL}/design/ajax/getDesignsList?${params.toString()}`,
	        {},
	        { withCredentials: true }
	      );

	      const d = res.data || {};
	      const total =
	        Number(d.iTotalDisplayRecords ?? d.iTotalRecords ?? d.total ?? 0) || 0;
	      const aaData = d.aaData ?? d.data ?? [];

	      setDesigns(Array.isArray(aaData) ? aaData : []);
	      setDesignTotalRows(total);

	      if ((pageNumber - 1) * pageSize >= total && total > 0) {
	        const newPage = Math.max(1, Math.ceil(total / pageSize));
	        setDesignPage(newPage);
	      }
	    } catch (err) {
	      console.error("Error fetching designs list:", err);
	      setDesigns([]);
	      setDesignTotalRows(0);
	    } finally {
	      setDesignLoading(false);
	    }
	  },
	  [designPage, designPerPage, designSearch, filters, refresh, location]
	);


	useEffect(() => {
		fetchDesigns(designPage, designPerPage, designSearch, filters);
	}, [designPage, designPerPage, designSearch, filters, refresh, location]);

	const fetchUploadedFile = useCallback(async () => {
		try {
			const res = await api.post(
				`${API_BASE_URL}/design/ajax/getDesignUploadsList`,
				{},
				{ withCredentials: true }
			);
			const data = res.data ?? [];
			setUploadedFile(Array.isArray(data) ? data : data.aaData ?? []);
			setUploadedPage(1);
		} catch (err) {
			console.error("Error fetching uploaded files:", err);
			setUploadedFile([]);
		}
	}, [refresh, location]);

	useEffect(() => {
		fetchUploadedFile();
	}, [fetchUploadedFile, refresh, location]);

	const clearAllFiltersAndSearch = () => {
		setFilters({ contract: "", structureType: "", drawingType: "" });
		setDesignSearch("");
		setUploadedSearch("");
		setDesignPage(1);
		setUploadedPage(1);
	};

	const handleFilterChange = (name, value) => {
		setFilters((prev) => ({ ...prev, [name]: value }));
		setDesignPage(1); // reset to first page when filter changes
	};

	const handleAdd = () => navigate("add-design-form");

	
	
	const handleEdit = (designId) => {
	  navigate("add-design-form", { state: { design_id: designId } });
	};

	
	
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

	return (
		<div className={styles.container}>
			{/* Top Bar */}
			{!isDesignDrawingForm && (
				<div className="pageHeading">
					<h2>Update Design & Drawing</h2>
					<div className="rightBtns">
						<button className="btn-2 transparent-btn">
							<LuDownload size={16} />
						</button>
						<button className="btn btn-primary">
							<LuUpload size={16} /> Upload
						</button>
						<button className="btn btn-primary" onClick={handleAdd}>
							<CirclePlus size={16} /> Add
						</button>
						<button className="btn btn-primary">
							<LuCloudDownload size={16} /> Export
						</button>
					</div>
				</div>
			)}

			{/* Filters */}
			{!isDesignDrawingForm && (
				<div className="innerPage">
					<div className={styles.filterRow}>
						{Object.keys(filters).map((key) => {
							const options = filterOptions[key] || [];
							// key mapping to friendly label
							const labelMap = {
								contract: "Contract",
								structureType: "Structure Type",
								drawingType: "Drawing Type",
							};
							return (
								<div className="filterOptions" key={key} style={{ minWidth: 160 }}>
									<Select
										options={[{ value: "", label: `Select ${labelMap[key] || key}` }, ...options]}
										classNamePrefix="react-select"
										value={
											options.find((opt) => opt.value === filters[key]) || {
												value: "",
												label: `Select ${labelMap[key] || key}`,
											}
										}
										onChange={(selectedOption) =>
											handleFilterChange(key, selectedOption?.value || "")
										}
										placeholder={`Select ${labelMap[key] || key}`}
										isSearchable
										styles={{
											control: (base) => ({
												...base,
												minHeight: "32px",
												borderColor: "#ced4da",
												boxShadow: "none",
												"&:hover": { borderColor: "#86b7fe" },
											}),
											dropdownIndicator: (base) => ({ ...base, padding: "2px 6px" }),
											valueContainer: (base) => ({ ...base, padding: "0 6px" }),
											menu: (base) => ({ ...base, zIndex: 9999 }),
										}}
									/>
								</div>
							);
						})}

						<div style={{ display: "flex", alignItems: "center", gap: 8 }}>
							<button
								className="btn btn-2 btn-primary"
								type="button"
								onClick={clearAllFiltersAndSearch}
							>
								Clear Filter
							</button>
						</div>
					</div>

					{/* DESIGN Table */}
					<div className={`dataTable ${styles.tableWrapper}`} style={{ marginTop: 12 }}>
						<div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>

							<div className="showEntriesCount">
								<label>Show </label>
								<select
									value={designPerPage}
									onChange={(e) => {
										setDesignPerPage(Number(e.target.value));
										setDesignPage(1);
									}}
								>
									{[5, 10, 20, 50, 100].map((size) => (
										<option key={size} value={size}>
											{size}
										</option>
									))}
								</select>
								<span> entries</span>
							</div>

							<div style={{ display: "flex", gap: 8, alignItems: "center" }}>


								<div className="searchRow form-field" style={{ display: "flex", alignItems: "center" }}>
									<input
										type="text"
										className="form-control"
										placeholder="Search designs..."
										value={designSearch}
										onChange={(e) => {
											setDesignSearch(e.target.value);
											// trigger fetch if empty or length > 2 (as before)
											if (e.target.value.length === 0 || e.target.value.length > 2) {
												setDesignPage(1);
											}
										}}
										onKeyDown={(e) => {
											if (e.key === "Enter") {
												setDesignPage(1);
											}
										}}
										style={{ width: 220 }}
									/>
									<button
										className="transparent-btn search-btn"
										onClick={() => setDesignPage(1)}
										title="Search"
										style={{ marginLeft: 6 }}
									>
										<FaSearch size="14" />
									</button>
								</div>
							</div>
						</div>

						<table className={styles.designTable} style={{ width: "100%", marginTop: 8 }}>
							<thead>
								<tr>
									<th>PMIS Drawing No.</th>
									<th>Structure Type</th>
									<th>Structure</th>
									<th>Title</th>
									<th>Drawing Type</th>
									<th>Drawing No</th>
									<th>Last Update</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
								{designLoading ? (
									<tr>
										<td colSpan="8" style={{ textAlign: "center" }}>
											Loading...
										</td>
									</tr>
								) : designs.length > 0 ? (
									designs.map((dd, index) => (
										<tr key={dd.design_seq_id ?? index}>
											<td>{dd.design_seq_id}</td>
											<td>{dd.structure_type_fk}</td>
											<td>{dd.structure_id_fk}</td>
											<td>{dd.drawing_title}</td>
											<td>{dd.drawing_type_fk}</td>
											<td>{dd.mrvc_drawing_no}</td>
											<td>{dd.modified_date}</td>
											<td>
												<button
													className="btn btn-sm btn-outline-primary"
													onClick={() => handleEdit(dd.design_id)}
												>
													<MdEditNote
														size="22"
													/>
												</button>
											</td>
										</tr>
									))
								) : (
									<tr>
										<td colSpan="8" style={{ textAlign: "center" }}>
											No records found
										</td>
									</tr>
								)}
							</tbody>
						</table>

						{/* Pagination Row (design) */}
						<div className="paginationRow" style={{ marginTop: 12 }}>
							<div className="entryCount">
								Showing{" "}
								{designTotalRows === 0
									? 0
									: Math.min((designPage - 1) * designPerPage + 1, designTotalRows)}{" "}
								– {Math.min(designPage * designPerPage, designTotalRows)} of {designTotalRows} entries
							</div>
							<div className="pagination">
								{renderPageButtons(designPage, designTotalPages, setDesignPage)}
							</div>
						</div>
					</div>

					<br />
					<br />

					{/* UPLOADED Design Data table (frontend pagination) */}
					{!isDesignDrawingForm && (
						<div className="pageHeading">
							<h2>Uploaded Design Data</h2>
							<div className="rightBtns">
								<button className="btn-2 transparent-btn hidden">
									<LuDownload size={16} />
								</button>
							</div>
						</div>
					)}

					<div className={`dataTable ${styles.tableWrapper}`} style={{ marginTop: 8 }}>
						<div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
							<div className="showEntriesCount">
								<label>Show </label>
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
								<span> entries</span>
							</div>

							<div style={{ display: "flex", gap: 8, alignItems: "center" }}>
								<div className="searchRow form-field" style={{ display: "flex", alignItems: "center" }}>
									<input
										type="text"
										className="form-control"
										placeholder="Search uploaded files..."
										value={uploadedSearch}
										onChange={(e) => {
											setUploadedSearch(e.target.value);
											setUploadedPage(1);
										}}
										onKeyDown={(e) => {
											if (e.key === "Enter") setUploadedPage(1);
										}}
										style={{ width: 220 }}
									/>
									<button
										className="transparent-btn search-btn"
										onClick={() => setUploadedPage(1)}
										title="Search"
										style={{ marginLeft: 6 }}
									>
										<FaSearch size="14" />
									</button>
								</div>
							</div>
						</div>

						<table className={styles.uploadedDesignTable} style={{ width: "100%", marginTop: 8 }}>
							<thead>
								<tr>
									<th>Uploaded File</th>
									<th>Status</th>
									<th>Remarks</th>
									<th>Uploaded by</th>
									<th>Uploaded On</th>
								</tr>
							</thead>
							<tbody>
								{uploadedPageSlice.length > 0 ? (
									uploadedPageSlice.map((uf, index) => (
										<tr key={uf.id ?? index}>
											<td>{uf.uploaded_file}</td>
											<td>{uf.status}</td>
											<td>{uf.remarks}</td>
											<td>{uf.uploaded_by_user_id_fk}</td>
											<td>{uf.uploaded_on}</td>
										</tr>
									))
								) : (
									<tr>
										<td colSpan="8" style={{ textAlign: "center" }}>
											No records found
										</td>
									</tr>
								)}
							</tbody>
						</table>

						{/* Pagination Row (uploaded files) */}
						<div className="paginationRow" style={{ marginTop: 12 }}>
							<div className="entryCount">
								Showing{" "}
								{uploadedTotalRows === 0
									? 0
									: Math.min((uploadedPage - 1) * uploadedPerPage + 1, uploadedTotalRows)}{" "}
								– {Math.min(uploadedPage * uploadedPerPage, uploadedTotalRows)} of{" "}
								{uploadedTotalRows} entries
							</div>
							<div className="pagination">
								{renderPageButtons(uploadedPage, uploadedTotalPages, setUploadedPage)}
							</div>
						</div>
					</div>
				</div>
			)}

			<Outlet />
		</div>
	);
}
