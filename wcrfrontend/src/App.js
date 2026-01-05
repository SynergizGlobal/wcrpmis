import IssueFileType from "./components/Admin/ReferenceForms/ReferenceFormsList/IssueFileType/IssueFileType";
import IssuePriority from "./components/Admin/ReferenceForms/ReferenceFormsList/IssuePriority/IssuePriority";
import IssueOtherOrganisation from "./components/Admin/ReferenceForms/ReferenceFormsList/IssueOtherOrganisation/IssueOtherOrganisation";
import IssueStatus from "./components/Admin/ReferenceForms/ReferenceFormsList/IssueStatus/IssueStatus";

import ApprovalAuthority from "./components/Admin/ReferenceForms/ReferenceFormsList/ApprovalAuthority/ApprovalAuthority";
import PurposeOfSubmission from "./components/Admin/ReferenceForms/ReferenceFormsList/PurposeOfSubmission/PurposeOfSubmission";
import DesignExecutives from "./components/Admin/ReferenceForms/ReferenceFormsList/DesignExecutives/DesignExecutives";
import DesignFileType from "./components/Admin/ReferenceForms/ReferenceFormsList/DesignFileType/DesignFileType";
import SubmittedBy from "./components/Admin/ReferenceForms/ReferenceFormsList/SubmittedBy/SubmittedBy";
import Stage from "./components/Admin/ReferenceForms/ReferenceFormsList/Stage/Stage";
import AsBuiltStatus from "./components/Admin/ReferenceForms/ReferenceFormsList/AsBuiltStatus/AsBuiltStatus";
import DrawingType from "./components/Admin/ReferenceForms/ReferenceFormsList/DrawingType/DrawingType";

import UtilityShiftingExecutives from "./components/Admin/ReferenceForms/ReferenceFormsList/UtilityShiftingExecutives/UtilityShiftingExecutives";
import UtilityShiftingFileTyp from "./components/Admin/ReferenceForms/ReferenceFormsList/UtilityShiftingFileTyp/UtilityShiftingFileTyp";
import UtilityTypes from "./components/Admin/ReferenceForms/ReferenceFormsList/UtilityTypes/UtilityTypes";
import UtilityStatus from "./components/Admin/ReferenceForms/ReferenceFormsList/UtilityStatus/UtilityStatus";
import UtilityRequirementStage from "./components/Admin/ReferenceForms/ReferenceFormsList/UtilityRequirementStage/UtilityRequirementStage";
import UtilityExecutionAgency from "./components/Admin/ReferenceForms/ReferenceFormsList/UtilityExecutionAgency/UtilityExecutionAgency";
import UtilityCategory from "./components/Admin/ReferenceForms/ReferenceFormsList/UtilityCategory/UtilityCategory";
import BankGuaranteeType from "./components/Admin/ReferenceForms/ReferenceFormsList/BankGuaranteeType/BankGuaranteeType";
import ReferenceForms from "./components/Admin/ReferenceForms/ReferenceForms";
import DashboardForm from "./components/Admin/Dashboards/DashboardForm/DashboardForm";
import Dashboards from "./components/Admin/Dashboards/Dashboards";
import UserForm from "./components/Admin/Users/UserForm/UserForm";
import Users from "./components/Admin/Users/Users";
import AddStructureForm from "./components/UpdateForms/AddStructure/AddStructureForm/AddStructureForm";
import AddStructure from "./components/UpdateForms/AddStructure/AddStructure";
import IssuesForm from "./components/UpdateForms/Issues/IssuesForm/IssuesForm";
import Issues from "./components/UpdateForms/Issues/Issues";
import ValidateData from "./components/UpdateForms/ValidateData/ValidateData";
import ModifyActuals from "./components/UpdateForms/ModifyActuals/ModifyActuals";
import NewActivitiesUpdate from "./components/UpdateForms/NewActivitiesUpdate/NewActivitiesUpdate";
import P6NewData from "./components/UpdateForms/P6NewData/P6NewData";
import ContractorForm from "./components/UpdateForms/Contractor/ContractorForm/ContractorForm";
import Contractor from "./components/UpdateForms/Contractor/Contractor";
import UpdateStructureForm from "./components/UpdateForms/UpdateStructure/UpdateStructureForm/UpdateStructureForm";
import UpdateStructure from "./components/UpdateForms/UpdateStructure/UpdateStructure";
import UtilityShiftingForm from "./components/UpdateForms/UtilityShifting/UtilityShiftingForm/UtilityShiftingForm";
import UtilityShifting from "./components/UpdateForms/UtilityShifting/UtilityShifting";
import ContractForm from "./components/UpdateForms/Contract/ContractForm/ContractForm";
import Contract from "./components/UpdateForms/Contract/Contract";
import DesignDrawingForm from "./components/UpdateForms/DesignDrawing/DesignDrawingForm/DesignDrawingForm";
import DesignDrawing from "./components/UpdateForms/DesignDrawing/DesignDrawing";
import GanttBarChart from "./components/Charts/GanttBarChart/GanttBarChart";
import LandAcquisitionForm from "./components/UpdateForms/LandAcquisition/LandAcquisitionForm/LandAcquisitionForm";
import LandAcquisition from "./components/UpdateForms/LandAcquisition/LandAcquisition";
import Works from "./components/Works/Works";
import Work from "./components/UpdateForms/Work/Work";
import WorkForm from "./components/UpdateForms/Work/WorkForm/WorkForm";
import Project from "./components/UpdateForms/Project/Project";
import QuickLinks from "./components/QuickLinks/QuickLinks";
import Documents from "./components/Documents/Documents";
import Reports from "./components/Reports/Reports";
import Modules from "./components/Modules/Modules";
import Admin from "./components/Admin/Admin";
import UpdateForms from "./components/UpdateForms/UpdateForms";
import Footer from "./components/Footer/Footer";
import Sidebar from "./components/Sidebar/Sidebar";
import Header from "./components/Header/Header";
import About from "./components/About/About";
import Dashboard from "./components/Dashboard/Dashboard";
import { PageTitleProvider } from "./context/PageTitleContext";
import React, { useEffect, useState } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { API_BASE_URL } from "./config";
import "./App.css";
import Layout from "./components/Layout/Layout";
import Login from "./components/Login/Login";
import Home from "./components/Home/Home";
import ProjectForm from "./components/UpdateForms/Project/ProjectForm/ProjectForm";
import { QueryClientProvider } from "@tanstack/react-query";
import { queryClient } from "./queryClient";
function App() {
	const [message, setMessage] = useState("Loading...");
	const isAuthenticated = localStorage.getItem("token");
	useEffect(() => {
		fetch(`${API_BASE_URL}/api/test`).then(res => {
			if (!res.ok) throw new Error("Network error");
			return res.text();
		}).then(data => setMessage(data)).catch(() => setMessage("❌ Could not connect to backend"));
	}, []);
	return <PageTitleProvider>
		<BrowserRouter basename="/wcrpmis">
			<QueryClientProvider client={queryClient}>
				<Routes>
					<Route path="/" element={isAuthenticated ? <Navigate to="/home" /> : <Login />} />

					<Route path="/" element={isAuthenticated ? <Layout /> : <Navigate to="/" />}>
						<Route path="home" element={<Home />} />
						<Route path="footer" element={<Footer />} />
						<Route path="sidebar" element={<Sidebar />} />
						<Route path="header" element={<Header />} />
						<Route path="about" element={<About />} />
						<Route path="dashboard" element={<Dashboard />} />

						<Route path="updateforms" element={<UpdateForms />}>
							<Route path="project" element={<Project />}>
								<Route path="projectform" element={<ProjectForm />} />
							</Route>

							<Route path="work" element={<Work />}>
								<Route path="workform" element={<WorkForm />} />
							</Route>

							<Route path="land-acquisition" element={<LandAcquisition />}>
								<Route path="landacquisitionform" element={<LandAcquisitionForm />} />
							</Route>

							<Route path="design" element={<DesignDrawing />}>
								<Route path="add-design-form" element={<DesignDrawingForm />} />
							</Route>

							<Route path="ganttbarchart" element={<GanttBarChart />} />

							<Route path="contract" element={<Contract />}>
								<Route path="add-contract-form" element={<ContractForm />} />
							</Route>

							<Route path="utilityshifting" element={<UtilityShifting />}>
								<Route path="add-utility-shifting" element={<UtilityShiftingForm />} />
							</Route>

							<Route path="structure-form" element={<UpdateStructure />}>
								<Route path="get-structure-form" element={<UpdateStructureForm />} />
							</Route>

							<Route path="contractor" element={<Contractor />}>
								<Route path="add-contractor-form" element={<ContractorForm />} />
							</Route>

							<Route path="structure" element={<AddStructure />}>
								<Route path="addstructureform" element={<AddStructureForm />} />
							</Route>

							<Route path="p6-new-data" element={<P6NewData />} />
							<Route path="new-activities-update" element={<NewActivitiesUpdate />} />
							<Route path="modify-actuals" element={<ModifyActuals />} />
							<Route path="progress-approval-page" element={<ValidateData />} />
							<Route path="issues" element={<Issues />}>
								<Route path="issuesform" element={<IssuesForm />} />
							</Route>
						</Route>






						<Route path="works" element={<Works />} />
						<Route path="admin" element={<Admin />}>
							<Route path="users" element={<Users />}>
								<Route path="userform" element={<UserForm />} />
							</Route>
							<Route path="access-dashboards" element={<Dashboards />}>
								<Route path="dashboardform" element={<DashboardForm />} />
							</Route>
							<Route path="reference-forms" element={<ReferenceForms />} />
						</Route>
						<Route path="bank-guarantee-type" element={<BankGuaranteeType />} />
						<Route path="utility-category" element={<UtilityCategory />} />
						<Route path="utility-execution-agency" element={<UtilityExecutionAgency />} />
						<Route path="utility-requirement-stage" element={<UtilityRequirementStage />} />
						<Route path="utility-status" element={<UtilityStatus />} />
						<Route path="utility-types" element={<UtilityTypes />} />
						<Route path="utility-shifting-filetyp" element={<UtilityShiftingFileTyp />} />
						<Route path="utility-shifting-executives" element={<UtilityShiftingExecutives />} />
						<Route path="as-built-status" element={<AsBuiltStatus />} />
						<Route path="drawing-type" element={<DrawingType />} />
						<Route path="approval-authority" element={<ApprovalAuthority />} />
						<Route path="stage" element={<Stage />} />
						<Route path="submitted-by" element={<SubmittedBy />} />
						<Route path="design-file-type" element={<DesignFileType />} />
						<Route path="purpose-of-submission" element={<PurposeOfSubmission />} />
						<Route path="design-executives" element={<DesignExecutives />} />
						<Route path="issue-file-type" element={<IssueFileType />} />
						<Route path="issue-priority" element={<IssuePriority />} />
						<Route path="issue-other-organisation" element={<IssueOtherOrganisation />} />
						<Route path="issue-status" element={<IssueStatus />} />
						<Route path="modules" element={<Modules />} />
						<Route path="reports" element={<Reports />} />
						<Route path="documents" element={<Documents />} />
						<Route path="quicklinks" element={<QuickLinks />} />

					</Route>

					<Route path="*" element={<div style={{
						textAlign: "center",
						marginTop: "20%"
					}}>
						<h2>{message}</h2>
					</div>} />
				</Routes>
			</QueryClientProvider>
		</BrowserRouter>
	</PageTitleProvider>;
}
export default App;
