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
  const [referenceLetters, setReferenceLetters] = useState([]);
  const [statuses, setStatuses] = useState([]);

  const [form, setForm] = useState({
    category: "",
    projectName: "",
    contractName: "",
    letterNumber: "",
    letterDate: "",
    to: "",
    subject: "",
    // referenceLetters: "",
    keyInformation: "",
    requiredResponse: "",
    dueDate: "",
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

  api.get("/api/statuses/get")
  .then(r => setStatuses(r.data || []));

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
    value: d.id,
    label: d.name
  }));

  const formatDate = (v) => {
    if (!v) return "";
    if (Array.isArray(v)) {
      // [2024, 3, 11] → "11-03-2024"
      const [year, month, day] = v;
      return `${String(day).padStart(2, "0")}-${String(month).padStart(2, "0")}-${year}`;
    }
    // "2024-03-11" or ISO string
    const d = new Date(v);
    if (isNaN(d)) return v;
    return `${String(d.getDate()).padStart(2, "0")}-${String(d.getMonth() + 1).padStart(2, "0")}-${d.getFullYear()}`;
  };
  
  
  const fetchCorrespondence = async (isDraft = false) => {
  try {
    setLoading(true);

    let filters = {};

     if (isDraft) {
      // Only drafts
        filters = {
          "-1": ["draft", "Save as Draft"]
        };
      } else {
        // Only sent letters
        filters = {
          "-1": ["send", "Send"]
        };
      }
    
     const firstRes = await api.post("/api/correspondence/filter-data", {
      draw: 1,
      start: 0,
      length: 10,
      columnFilters: filters
    });

    const total = firstRes.data?.recordsTotal || 10;

    
    const res = await api.post("/api/correspondence/filter-data", {
      draw: 2,
      start: 0,
      length: total,
      columnFilters: filters
    });

    const rows = res.data?.data || [];

    const mapped = rows.map((r) => ({
    
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
      "Due Date": formatDate(r.dueDate) || "",
      Project: r.projectName || "",
      Contract: r.contractName || "",
      Status: r.currentStatus || "",
      Department: r.department || "",
      Attachment: r.attachment || "",	
      Type: r.type || "",
    }));

      mapped.sort((a, b) => {
        if (a.Type === "Incoming" && b.Type === "Outgoing") return -1;
        if (a.Type === "Outgoing" && b.Type === "Incoming") return 1;
        return 0;
      });

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

const searchReferenceLetters = async (inputValue) => {
  try {
    const res = await api.get("/api/correspondence/getReferenceLetters", {
      params: { query: inputValue || "" }
    });

    return (res.data || []).map((item) => ({
      value: item,
      label: item
    }));
  } catch (err) {
    console.error(err);
    return [];
  }
};

useEffect(() => {
  fetchCorrespondence(showDrafts);
}, [showDrafts]);

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

    const dto = {
      ...form,
      department: form.department ? Number(form.department) : null,
      currentStatus: form.currentStatus ? Number(form.currentStatus) : null,
      to: toUser?.value,
      cc: ccUsers?.map(x => x.value) || [],
      referenceLetters: referenceLetters?.map(x => x.value) || [],
      projectName: form.projectName,
      contractName: form.contractName,
      action: "Send"
    };

    fd.append("dto", JSON.stringify(dto));

    if (file) {
      fd.append("document", file);
    }

    await api.post("/api/correspondence/uploadLetter", fd);

    fetchCorrespondence();
    setShowUpload(false);

  } catch (error) {
    console.error(error);

    const message =
      error.response?.data || "Failed to save draft";

    alert(message);
  }
};

  const handleDraft = async () => {
  try {
    const fd = new FormData();

    const dto = {
      ...form,
      department: form.department ? Number(form.department) : null,
      currentStatus: form.currentStatus ? Number(form.currentStatus) : null,
      to: toUser?.value || "",
      cc: ccUsers?.map(x => x.value) || [],
      referenceLetters: referenceLetters?.map(x => x.value) || [],
      projectName: form.projectName,
      contractName: form.contractName,
      action: "Save as Draft"
    };

    console.log("TO value:", toUser);
    console.log("DTO to:", toUser?.value);

    fd.append("dto", JSON.stringify(dto));

    if (file) {
      fd.append("document", file);
    }

    await api.post("/api/correspondence/uploadLetter", fd);

    alert("Draft saved");
    setShowUpload(false);
    fetchCorrespondence();

  } catch (error) {
    console.error(error);

    const message =
      error.response?.data || "Failed to save draft";

    alert(message);
  }
};

  const searchUsers = async (inputValue) => {
  try {
    const res = await api.get("/users/search", {
      params: { query: inputValue || "" }
    });

    return (res.data || []).map((u) => ({
      value: u.userName,
      label: `${u.userName} (${u.emailId})`,
    }));

  } catch (err) {
    console.error(err);
    return [];
  }
};

const handleEditDraft = async (id) => {
  const res = await api.get(`/api/correspondence/view/${id}`);
  const data = res.data;

  setReferenceLetters(
    (data.refLetters || []).map(r => ({
      value: r,
      label: r
    }))
  );

  setForm({
    correspondenceId: id,
    category: data.category || "",
    projectName: data.projectName || "",
    contractName: data.contractName || "",
    letterNumber: data.letterNumber || "",
    letterDate: data.letterDate || "",
    subject: data.subject || "",
    keyInformation: data.keyInformation || "",
    requiredResponse: data.requiredResponse || "",
    dueDate: data.dueDate || "",
    currentStatus: data.currentStatus || "",
    department: data.department || ""
  });

  setShowUpload(true);
};

const resetForm = () => {
  setForm({
    category: "",
    projectName: "",
    contractName: "",
    letterNumber: "",
    letterDate: "",
    subject: "",
    keyInformation: "",
    requiredResponse: "",
    dueDate: "",
    currentStatus: "",
    department: ""
  });
};

  return (
    <>
      <div className={styles.actionBar}>
        <button
          className="btn-2 btn-primary"
          onClick={() => {
            resetForm();
            setShowUpload(true);
          }}
        >
          Upload Letter
        </button>

        {!showDrafts ? (
          <button
            className="btn-2 btn-secondary"
            onClick={() => setShowDrafts(true)}
          >
            Drafts
          </button>
        ) : (
          <button
            className="btn-2 btn-secondary"
            onClick={() => setShowDrafts(false)}
          >
            Correspondence
          </button>
        )}
      </div>

      {!showDrafts && ( 
		<div className={styles.tableWrapper}>
        <DmsTable
          loading={loading}
          columns={[
            "Reference Number","Category","Letter No","From","To","Subject",
            "Required Response","Due Date","Project",
            "Contract","Status","Department","Attachment","Type"
          ]}
          mockData={tableData}
        />
		</div>
      )}

      
      {showDrafts && (
        <Drafts
          onBack={() => setShowDrafts(false)}
          onEdit={handleEditDraft}
        />
      )}

      
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
                    value={
                      form.category
                        ? { value: form.category, label: form.category }
                        : null
                    }
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
                  value={
                    form.projectName ? { value: form.projectName, label: form.projectName} : null
                  }
                  placeholder="Search Project..."
                  isClearable
                  onChange={(opt) => handleSelectChange("projectName", opt)}
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
                  value={
                    form.contractName ? { value: form.contractName, label: form.contractName} : null
                  }
                  placeholder="Search Contract..."
                  isClearable
                  onChange={(opt) => handleSelectChange("contractName", opt)}
                />
              </div>

              <div className="form-field">
                <label>Letter Number *</label>
                <input name="letterNumber" value={form.letterNumber} onChange={handleChange} placeholder="Enter letter number" />
              </div>

              <div className="form-field">
                <label>Letter Date *</label>
                <input type="date" value={form.letterDate} name="letterDate" onChange={handleChange} />
              </div>

              <div className="form-field">
                <label>To *</label>
                <AsyncSelect
                  placeholder="Search recipient..."
                  loadOptions={searchUsers}
                  value={toUser}
                  onChange={(selected) => {
                    setToUser(selected);
                    setForm(prev => ({
                      ...prev,
                      to: selected ? selected.value : ""
                    }));
                  }}
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
                <label>Reference Letters:</label>
                <AsyncSelect
                  isMulti
                  placeholder="Search reference letters..."
                  defaultOptions
                  cacheOptions
                  loadOptions={searchReferenceLetters}
                  value={referenceLetters}
                  onChange={(selected) => {
                    setReferenceLetters(selected || []);
                  }}
                  closeMenuOnSelect={false}
                />
              </div>


              <div className="form-field">
                <label>Subject *</label>
                <textarea name="subject" value={form.subject} onChange={handleChange} />
              </div>

              <div className="form-field">
                <label>Key Information *</label>
                <textarea name="keyInformation" value={form.keyInformation} onChange={handleChange} />
              </div>

              <div className="form-field">
                <label>Required Response *</label>
                <select name="requiredResponse" value={form.requiredResponse} onChange={handleChange}>
                  <option>Select Required Response</option>
                  <option value="Yes">Yes</option>
                  <option value="No">No</option>
                </select>
              </div>

              <div className="form-field">
                <label>Due Date *</label>
                <input type="date" name="dueDate" value={form.dueDate} onChange={handleChange} />
              </div>

              <div className="form-field">
                <label>Status *</label>
                <Select
                  options={statuses.map(s => ({
                    value: s.id,
                    label: s.name
                  }))}
                  value={
                      statuses
                        .map(s => ({ value: s.id, label: s.name }))
                        .find(option => option.value === form.currentStatus) || null
                    }
                  placeholder="Select Status"
                  isClearable
                  onChange={(opt) =>
                    setForm(prev => ({
                      ...prev,
                      currentStatus: opt ? opt.value : ""
                    }))
                  }
                />
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
                  value={
                    departmentOptions.find(
                      option => option.value === form.department
                    ) || null
                  }
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
                  <input value={formatDate(detailsData.letterDate) || ""} readOnly />
                </div>

                <div className="form-field">
                  <label>From</label>
                  <input value={detailsData.sender || ""} readOnly />
                </div>

                <div className="form-field">
                  <label>To</label>
                  <input value={detailsData.copiedTo  || ""} readOnly />
                </div>

                <div className="form-field">
                  <label>CC</label>
                  <input value={detailsData.ccRecipient || ""} readOnly />
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
                  <input value={formatDate(detailsData.dueDate) || ""} readOnly />
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
