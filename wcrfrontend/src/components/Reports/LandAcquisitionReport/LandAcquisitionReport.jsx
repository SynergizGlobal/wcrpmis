import React, { useEffect, useState, useContext } from "react";
import { RefreshContext } from "../../../context/RefreshContext";
import Select from "react-select";
import { Outlet, useLocation } from 'react-router-dom';
import styles from './LandAcquisitionReport.module.css'
import api from "../../../api/axiosInstance";
import { API_BASE_URL } from "../../../config";

export default function LandAcquisitionReport() {
	const { refresh } = useContext(RefreshContext);
	const location = useLocation();

	const [search, setSearch] = useState("");
	const [page, setPage] = useState(1);

	const [selectedProject, setSelectedProject] = useState(null);
	const [selectedType, setSelectedType] = useState(null);
	const [selectedSubCategory, setSelectedSubCategory] = useState(null);

	const [projectOptions, setProjectOptions] = useState([]);
	const [typeOfLandOptions, setTypeOfLandOptions] = useState([]);
	const [subCategoryOptions, setSubCategoryOptions] = useState([]);

	const [filters, setFilters] = useState({
		project: "",
		typeOfLand: "",
		subCategory: "",
	});

	const handleClearFilters = () => {
		setFilters({
			project: "",
			typeOfLand: "",
			subCategory: "",
		});
		setSearch("");
		setPage(1);
	};

	/* -------------------- DROPDOWN OPTIONS -------------------- */
	useEffect(() => {
		loadDropdowns();
	}, []);

	const loadDropdowns = async () => {
		try {

			const projectRes = await api.get(
				`${API_BASE_URL}/api/land-report/project-list`,
				{ withCredentials: true }
			);

			const typeRes = await api.get(
				`${API_BASE_URL}/api/land-report/type-list`,
				{ withCredentials: true }
			);

			const subCatRes = await api.get(
				`${API_BASE_URL}/api/land-report/sub-category-list`,
				{ withCredentials: true }
			);

			// 🔥 Convert API data → react-select format

			const projectOpts = projectRes.data.map(item => ({
				value: item.project_id,
				label: item.project_name
			}));

			const typeOpts = typeRes.data.map(item => ({
				value: item.category_fk,
				label: item.category_fk
			}));

			const subCatOpts = subCatRes.data.map(item => ({
				value: item.la_sub_category_fk,
				label: item.la_sub_category
			}));
			console.log("Project Response:", projectRes.data);
			console.log("Typelist Response:", typeRes.data);
			console.log("Sub-Category Response:", subCatRes.data);

			setProjectOptions(projectOpts);
			setTypeOfLandOptions(typeOpts);
			setSubCategoryOptions(subCatOpts);

		} catch (err) {
			console.error("Error loading LA dropdowns", err);
		}
	};


	const handleGenerateReport = async () => {
		if (!filters.project) {
			console.log("Selected Project:", selectedProject);
			alert("Please select project");
			return;
		}

		try {
			const response = await api.post(
				"/api/land-report/generate",
				{
					project_id_fk: filters.project || null,
					category_fk: filters.typeOfLand || null,
					la_sub_category_fk: filters.subCategory || null,
				},
				{
					responseType: "blob",
					withCredentials: true,
				}
			);

			const blob = new Blob([response.data], {
				type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
			});

			const url = window.URL.createObjectURL(blob);
			const link = document.createElement("a");
			link.href = url;

			link.setAttribute(
				"download",
				`Land_Acquisition_Report_${new Date().toISOString()}.xlsx`
			);

			document.body.appendChild(link);
			link.click();
			link.remove();

		} catch (error) {
			console.error("Generate error:", error);
			alert("Failed to generate report");
		}
	};

	return (
		<div className={styles.container}>
			{/* Top Bar */}
			<div className="pageHeading">
				<h2>Land Acquisition Report</h2>
				<div className="rightBtns">
					&nbsp;
				</div>
			</div>

			<div className="innerPage">

				{/* ✅ ROW 1 */}
				<div className={styles.filterRow}>
					<div className={styles.inputField}>
						<label className={styles.label}>Project</label>
						<Select
							options={projectOptions}
							value={projectOptions.find((o) => o.value === filters.project)}
							onChange={(opt) =>
								setFilters({ ...filters, project: opt.value })
							}
							placeholder="Select Project"
							className={styles.filterOptions}
						/>
					</div>

					<div className={styles.inputField}>
						<label className={styles.label}>Type Of Land</label>
						<Select
							options={typeOfLandOptions}
							value={typeOfLandOptions.find((o) => o.value === filters.typeOfLand)}
							onChange={(opt) =>
								setFilters({ ...filters, typeOfLand: opt.value })
							}
							placeholder="Select Type Of Land"
							className={styles.filterOptions}
						/>
					</div>

					<div className={styles.inputField}>
						<label className={styles.label}>Sub Category</label>
						<Select
							options={subCategoryOptions}
							value={subCategoryOptions.find((o) => o.value === filters.subCategory)}
							onChange={(opt) =>
								setFilters({ ...filters, subCategory: opt.value })
							}
							placeholder="Select Sub Category"
							className={styles.filterOptions}
						/>
					</div>
				</div>

				{/* ✅ ROW 2 */}
				<div className={styles.buttonRow}>
					<button
						className="btn btn-2 btn-primary"
						onClick={handleClearFilters}
					>
						Clear Filter
					</button>

					<button
						className="btn btn-2 btn-primary"
						onClick={handleGenerateReport}
					>
						Generate Report
					</button>
				</div>

			</div>
			<Outlet />
		</div>
	);
}