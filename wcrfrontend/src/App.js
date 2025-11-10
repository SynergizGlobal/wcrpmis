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
function App() {
  const [message, setMessage] = useState("Loading...");
  const isAuthenticated = localStorage.getItem("token");
  useEffect(() => {
    fetch(`${API_BASE_URL}/api/test`).then(res => {
      if (!res.ok) throw new Error("Network error");
      return res.text();
    }).then(data => setMessage(data)).catch(err => {
      console.error("Error fetching backend:", err);
      setMessage("❌ Could not connect to backend");
    });
  }, []);
  return <PageTitleProvider>
    <BrowserRouter basename="/wcrpmis">
      <Routes>
        {/* Public route */}
        <Route path="/" element={isAuthenticated ? <Navigate to="/home" /> : <Login />} />

        {/* Protected routes inside Layout */}
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
            <Route path="contract" element={<Contract />} >
              <Route path="add-contract-form" element={<ContractForm />} />
            </Route>
          </Route>
          <Route path="works" element={<Works />} />
          <Route path="admin" element={<Admin />} />
          <Route path="modules" element={<Modules />} />
          
          <Route path="reports" element={<Reports />} />
          
          <Route path="documents" element={<Documents />} />
          
          <Route path="quicklinks" element={<QuickLinks />} />

        
          {/* Add more nested pages here later */}
        </Route>

        {/* Fallback route */}
        <Route path="*" element={<div style={{
          textAlign: "center",
          marginTop: "20%"
        }}>
              <h2>{message}</h2>
            </div>} />
      
      
          
      </Routes>
    </BrowserRouter>
    </PageTitleProvider>;
}
export default App;
