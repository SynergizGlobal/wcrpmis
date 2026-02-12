import React, { useState } from "react";
import DmsTable from "../DmsTable/DmsTable";
import Drafts from "./Drafts";
import styles from "./Correspondence.module.css";

export default function Correspondence() {

  const [showUpload, setShowUpload] = useState(false);
  const [showDrafts, setShowDrafts] = useState(false);

  return (
    <>
      {/* ACTION BAR */}
      <div className={styles.actionBar}>
        <button className="btn-2 btn-primary" onClick={() => setShowUpload(true)}>Upload Letter</button>
        <button className="btn-2 btn-secondary" onClick={() => setShowDrafts(true)}>Drafts</button>
      </div>

      {/* MAIN TABLE */}
      {!showDrafts && (
        <DmsTable
          columns={[
            "Reference Number","Category","Letter No","From","To","Subject",
            "Required Response","Due Date","Project",
            "Contract","Status","Department","Attachment","Type"
          ]}
          mockData={[
            {
              "Reference Number": "WCR/Over Bridge Con/001",
              Category: "Technical",
              "Letter No": "LTR-001",
              From: "MRVC",
              To: "Contractor",
              Subject: "Design Approval",
              "Required Response": "Yes",
              "Due Date": "12-02-2025",
              Project: "WCR Project",
              Contract: "Contract A",
              Status: "Open",
              Department: "Engineering",
              Attachment: "View",
              Type: "Incoming"
            }
          ]}
        />
      )}

      {/* DRAFTS */}
      {showDrafts && <Drafts onBack={() => setShowDrafts(false)} />}

      {/* UPLOAD LETTER MODAL */}
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
                  <select><option>Select Category</option></select>
                </div>

              <div className="form-field">
                <label>Project Name *</label>
                <select><option>MUTP IIIA</option></select>
              </div>
              
              <div className="form-field">
                <label>Contract Name *</label>
                <select>
                  <option>224 Construction of Important Bridges</option>
                </select>
              </div>

              <div className="form-field">
                <label>Letter Number *</label>
                <input placeholder="Enter letter number" />
              </div>

              <div className="form-field">
                <label>Letter Date *</label>
                <input type="date" />
              </div>

              <div className="form-field">
                <label>To *</label>
                <input placeholder="Recipient" />
              </div>

              <div className="form-field">
                <label>Subject *</label>
                <textarea placeholder="Enter subject" />
              </div>

              <div className="form-field">
                <label>Attachment *</label>
                <input type="file" />
              </div>

            </div>
              
          </div>

            <div className={styles.footer}>
              <button className="btn btn-primary">Send</button>
              <button className="btn btn-white">Save as Draft</button>
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
