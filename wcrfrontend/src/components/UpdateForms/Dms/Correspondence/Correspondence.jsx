import React, { useEffect, useState } from "react";
import api from "../../../../api/axiosInstance";
import AsyncSelect from "react-select/async";
import Select from "react-select";
import DmsTable from "../DmsTable/DmsTable";
import Drafts from "./Drafts";
import styles from "./Correspondence.module.css";
import { API_BASE_URL } from "../../../../config";

export default function Correspondence() {

  const [showUpload, setShowUpload] = useState(false);
  const [showDrafts, setShowDrafts] = useState(false);
  const [tableData, setTableData] = useState([]);
  const [loading, setLoading] = useState(false);
  const [toUser, setToUser] = useState(null);
  const [ccUsers, setCcUsers] = useState([]);
  const [userOptions, setUserOptions] = useState([]);
  const [projects, setProjects] = useState([]);
  const [contracts, setContracts] = useState([]);
  const [departments, setDepartments] = useState([]);
  const [showDetails, setShowDetails] = useState(false);
  const [selectedLetter, setSelectedLetter] = useState(null);
  const [detailsData, setDetailsData] = useState(null);
  const [showDetailsModal, setShowDetailsModal] = useState(false);

  const [form, setForm] = useState({
    category: "",
    project: "",
    contract: "",
    letterNo: "",
    letterDate: "",
    to: "",
    subject: "",
    referenceLetters: "",
    keyInformation: "",
    requiredResponse: "",
    letterDueDate: "",
    currentStatus: "",
    department: "",
  });

  const [file, setFile] = useState(null);

  useEffect(() => {
  api.get("/projects/get-project-name")
    .then(r => setProjects(r.data || []));

  api.get("/contract/get-contract-name")
    .then(r => setContracts(r.data || []));


  api.get("/api/departments/get")
      .then(r => setDepartments(r.data || []));
  }, []);

  const projectOptions = projects.map(p => ({
    value: p.name,
    label: p.name
  }));

  const contractOptions = contracts.map(c => ({
    value: c.name,
    label: c.name
  }));

  const departmentOptions = departments.map(d => ({
    value: d.name,
    label: d.name
  }));


  const fetchCorrespondence = async () => {
  try {
    setLoading(true);
    
     const firstRes = await api.post("/api/correspondence/filter-data", {
      draw: 1,
      start: 0,
      length: 10,
      columnFilters: {}
    });

    const total = firstRes.data?.recordsTotal || 10;

    
    const res = await api.post("/api/correspondence/filter-data", {
      draw: 2,
      start: 0,
      length: total,
      columnFilters: {}
    });

    const rows = res.data?.data || [];

    const mapped = rows.map((r) => ({
      // correspondenceId: r.correspondenceId,
      "Reference Number": r.referenceNumber || "",
      Category: r.category || "",
      "Letter No": (
        <span
          style={{
            color: "#0d6efd",
            cursor: "pointer",
            textDecoration: "underline"
          }}
          onClick={() => openLetterDetails(r.correspondenceId)}
        >
          {r.letterNumber}
        </span>
      ),
      From: r.from || "",
      To: r.to || "",
      Subject: r.subject || "",
      "Required Response": r.requiredResponse || "",
      "Due Date": r.dueDate || "",
      Project: r.projectName || "",
      Contract: r.contractName || "",
      Status: r.currentStatus || "",
      Department: r.department || "",
      Attachment: r.attachment || "",	
      Type: r.type || "",
    }));

    setTableData(mapped);

  } catch (error) {
    console.error("Fetch error:", error);
  } finally {
    setLoading(false);
  }
};

const openLetterDetails = async (correspondenceId) => {
  try {

    if (!correspondenceId) {
      console.error("Missing correspondenceId");
      return;
    }

    setShowDetailsModal(true);

    const res = await api.get(
      `/api/correspondence/view/${correspondenceId}`
    );

    setDetailsData(res.data);

  } catch (err) {
    console.error("Letter details error", err);
  }
};

  useEffect(() => {
    fetchCorrespondence();
  }, []);

  const handleChange = (e) => {
    setForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value
    }));
  };

  const handleSelectChange = (name, option) => {
  setForm(prev => ({
    ...prev,
    [name]: option ? option.value : ""
  }));
};

  const handleSend = async () => {
  try {
    const fd = new FormData();

    fd.append("dto", JSON.stringify({
      ...form,
      to: toUser?.value,
      ccRecipients: ccUsers.map(x => x.value),
      action: "send"
    }));

    if (file) {
      fd.append("document", file);
    }

    await api.post("/api/correspondence/uploadLetter", fd);

    fetchCorrespondence();
    setShowUpload(false);

  } catch (e) {
    console.error(e);
  }
};


  const handleDraft = async () => {
    try {
      const formData = new FormData();

      Object.keys(form).forEach(key => {
        formData.append(key, form[key]);
      });

      formData.append("status", "DRAFT");

      if (file) {
        formData.append("file", file);
      }

      await api.post(
        "/api/correspondence/uploadLetter",
        formData,
        {
          headers: { "Content-Type": "multipart/form-data" }
        }
      );

      alert("Draft saved");
      setShowUpload(false);

    } catch (error) {
      console.error("Draft error:", error);
      alert("Failed to save draft");
    }
  };

  const searchUsers = async (inputValue) => {
  try {
    const res = await api.get("/users/search", {
      params: { query: inputValue || "" }
    });

    return (res.data || []).map((u) => ({
      value: u.userId,
      label: `${u.userName} (${u.emailId})`,
    }));

  } catch (err) {
    console.error(err);
    return [];
  }
};

  return (
    <>
      <div className={styles.actionBar}>
        <button className="btn-2 btn-primary" onClick={() => setShowUpload(true)}>Upload Letter</button>
        <button className="btn-2 btn-secondary" onClick={() => setShowDrafts(true)}>Drafts</button>
      </div>

      {!showDrafts && (
        <DmsTable
          loading={loading}
          columns={[
            "Reference Number","Category","Letter No","From","To","Subject",
            "Required Response","Due Date","Project",
            "Contract","Status","Department","Attachment","Type"
          ]}
          mockData={tableData}
        />
      )}

      
      {showDrafts && <Drafts onBack={() => setShowDrafts(false)} />}

      
      {showUpload && (
        <div className={styles.overlay}>
          <div className={styles.modal}>
            <div className={styles.header}>
              <h3>Upload Letter</h3>
              <span onClick={() => setShowUpload(false)}>×</span>
            </div>

            <div className={styles.body}>
              <div className="form-row">
                <div className="form-field">
                  <label>Category *</label>
                  <Select
                    options={[
                      { value: "Contract", label: "Contract" },
                      { value: "Technical", label: "Technical" },
                      { value: "Commercial", label: "Commercial" },
                      { value: "Legal", label: "Legal" },
                      { value: "Administrative", label: "Administrative" }
                    ]}
                    placeholder="Select Category"
                    onChange={(opt) => handleSelectChange("category", opt)}
                  />
                </div>

              <div className="form-field">
                <label>Project Name *</label>
                {/* <select  name="project" onChange={handleChange}>
                  <option>Select Project</option>
                  {projects.map(p => <option key={p.id} value={p.name}>{p.name}</option>)}
                </select> */}

                <Select
                  options={projectOptions}
                  placeholder="Search Project..."
                  isClearable
                  onChange={(opt) => handleSelectChange("project", opt)}
                />
              </div>
              
              <div className="form-field">
                <label>Contract Name *</label>
                {/* <select name="contract" onChange={handleChange}>
                  <option>Select Contract</option>
                  {contracts.map(c => <option key={c.id} value={c.name}>{c.name}</option>)}
                </select> */}
                <Select
                  options={contractOptions}
                  placeholder="Search Contract..."
                  isClearable
                  onChange={(opt) => handleSelectChange("contract", opt)}
                />
              </div>

              <div className="form-field">
                <label>Letter Number *</label>
                <input name="letterNo" onChange={handleChange} placeholder="Enter letter number" />
              </div>

              <div className="form-field">
                <label>Letter Date *</label>
                <input type="date" name="letterDate" onChange={handleChange} />
              </div>

              <div className="form-field">
                <label>To *</label>
                <AsyncSelect
                  placeholder="Search recipient..."
                  loadOptions={searchUsers}
                  onChange={setToUser}
                  isClearable
                  cacheOptions
                  defaultOptions={true} 
                />
              </div>

              <div className="form-field">
                <label>CC</label>
                <AsyncSelect
                  isMulti
                  placeholder="Search CC recipients..."
                  defaultOptions={true} 
                  loadOptions={searchUsers}
                  onChange={setCcUsers}
                  value={ccUsers}
                  closeMenuOnSelect={false}
                  cacheOptions
                />
              </div>

              <div className="form-field">
                <label>Reference Letters: </label>
                <textarea name="subject" onChange={handleChange} />
              </div>


              <div className="form-field">
                <label>Subject *</label>
                <textarea name="subject" onChange={handleChange} />
              </div>

              <div className="form-field">
                <label>Key Information *</label>
                <textarea name="keyInformation" onChange={handleChange} />
              </div>

              <div className="form-field">
                <label>Required Response *</label>
                <select name="requiredResponse" onChange={handleChange}>
                  <option>Select Required Response</option>
                  <option value="Yes">Yes</option>
                  <option value="No">No</option>
                </select>
              </div>

              <div className="form-field">
                <label>Due Date *</label>
                <input type="date" name="letterDueDate" onChange={handleChange} />
              </div>

              <div className="form-field">
                <label>Status *</label>
                <select name="currentStatus" onChange={handleChange}>
                  <option>Select Status</option>
                  <option value="approved">Approved</option>
                  <option value="closed">Closed</option>
                  
                </select>
              </div>

              <div className="form-field">
                <label>Department *</label>
                {/* <select name="department" onChange={handleChange}>
                  <option>Select Department</option>
                  {departments.map(d => (
                    <option key={d.id} value={d.name}>{d.name}</option>
                  ))}
                </select> */}

                <Select
                  options={departmentOptions}
                  placeholder="Search Department..."
                  isClearable
                  onChange={(opt) => handleSelectChange("department", opt)}
                />
              </div>



              <div className="form-field">
                <label>Attachment *</label>
                <input
                    type="file"
                    onChange={(e) => setFile(e.target.files[0])}
                  />
              </div>

            </div>
              
          </div>

            <div className={styles.footer}>
              <button className="btn btn-primary" onClick={handleSend}>Send</button>
              <button className="btn btn-white" onClick={handleDraft}>Save as Draft</button>
              <button
                className="btn btn-red"
                onClick={() => setShowUpload(false)}
              >
                Cancel
              </button>
            </div>
          </div>
        </div>
      )}

      {showDetailsModal && detailsData && (
        <div className={styles.overlay}>
          <div className={styles.modal} style={{ maxWidth: "900px" }}>

            <div className={styles.header}>
              <h3>Letter Details</h3>
              <span onClick={() => setShowDetailsModal(false)}>×</span>
            </div>

            <div className={styles.body}>
              <div className="form-row">

                <div className="form-field">
                  <label>Category</label>
                  <input value={detailsData.category || ""} readOnly />
                </div>

                <div className="form-field">
                  <label>Letter No</label>
                  <input value={detailsData.letterNumber || ""} readOnly />
                </div>

                <div className="form-field">
                  <label>Letter Date</label>
                  <input value={detailsData.letterDate || ""} readOnly />
                </div>

                <div className="form-field">
                  <label>From</label>
                  <input value={detailsData.from || ""} readOnly />
                </div>

                <div className="form-field">
                  <label>To</label>
                  <input value={detailsData.to || ""} readOnly />
                </div>

                <div className="form-field">
                  <label>CC</label>
                  <input
                    value={(detailsData.ccRecipients || []).join(", ")}
                    readOnly
                  />
                </div>

                <div className="form-field full">
                  <label>Subject</label>
                  <textarea value={detailsData.subject || ""} readOnly />
                </div>

                <div className="form-field full">
                  <label>Key Information</label>
                  <textarea
                    value={detailsData.keyInformation || ""}
                    readOnly
                  />
                </div>

                <div className="form-field">
                  <label>Required Response</label>
                  <input
                    value={detailsData.requiredResponse || ""}
                    readOnly
                  />
                </div>

                <div className="form-field">
                  <label>Due Date</label>
                  <input value={detailsData.dueDate || ""} readOnly />
                </div>

                <div className="form-field">
                  <label>Department</label>
                  <input value={detailsData.department || ""} readOnly />
                </div>

              </div>
            </div>

            <div className={styles.footer}>
              <button
                className="btn btn-red"
                onClick={() => setShowDetailsModal(false)}
              >
                Close
              </button>
            </div>

          </div>
        </div>
      )}
    </>
  );
}
