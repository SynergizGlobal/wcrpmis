import React, { useEffect, useState } from "react";
import api from "../../../../api/axiosInstance";
import AsyncSelect from "react-select/async";
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

  const [form, setForm] = useState({
    category: "",
    project: "",
    contract: "",
    letterNo: "",
    letterDate: "",
    to: "",
    subject: "",
  });

  const [file, setFile] = useState(null);

  useEffect(() => {
  api.get("/projects/get-project-name")
    .then(r => setProjects(r.data || []));

  api.get("/contract/get-contract-name")
    .then(r => setContracts(r.data || []));
}, []);


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
      "Reference Number": r.referenceNumber || "",
      Category: r.category || "",
      "Letter No": r.letterNumber || "",
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

  useEffect(() => {
    fetchCorrespondence();
  }, []);

  const handleChange = (e) => {
    setForm(prev => ({
      ...prev,
      [e.target.name]: e.target.value
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
                  <select onChange={handleChange} name="category">
                    <option>Select Category</option>
                    <option value="Contract">Contract</option>
                    <option value="Technical">Technical</option>
                    <option value="Commercial">Commercial</option>
                    <option value="Legal">Legal</option>
                    <option value="Administrative">Administrative</option>
                  </select>
                </div>

              <div className="form-field">
                <label>Project Name *</label>
                <select  name="project" onChange={handleChange}>
                  <option>Select Project</option>
                  {projects.map(p => <option key={p.id} value={p.name}>{p.name}</option>)}
                </select>
              </div>
              
              <div className="form-field">
                <label>Contract Name *</label>
                <select name="contract" onChange={handleChange}>
                  <option>Select Contract</option>
                  {contracts.map(c => <option key={c.id} value={c.name}>{c.name}</option>)}
                </select>
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
                <label>Subject *</label>
                <textarea name="subject" onChange={handleChange} />
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
    </>
  );
}
