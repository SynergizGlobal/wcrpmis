import React, { useEffect, useState } from "react";
import api from "../../../../api/axiosInstance";
import DmsTable from "../DmsTable/DmsTable";

export default function Drafts({ onBack }) {

  const [drafts, setDrafts] = useState([]);

  const fetchDrafts = async () => {
    try {

      const res = await api.get("/api/documents/drafts");

      const rows = Array.isArray(res.data) ? res.data : [];

      rows = Array.isArray(rows) ? rows : [];

      console.log("ROWS:", rows);

      const mapped = rows.map(d => ({
        sendTo: d.sendTo || "",
        subject: d.sendSubject || "",
        reason: d.sendReason || "",
        responseExpected: d.responseExpected || "",
        targetDate: d.targetResponseDate || "",
        attachment: d.attachmentName || "",
        created: d.createdAt || d.createdDate || ""
      }));

      console.log("MAPPED:", mapped);

      console.log("API DATA:", res.data);

      setDrafts(mapped);

    } catch(err){
      console.error(err);
    }
  };

  useEffect(()=>{
    fetchDrafts();
  },[]);

  return (
    <>
      {/* <div style={{marginBottom:"10px"}}>
        <button className="btn btn-secondary" onClick={onBack}>
          Documents
        </button>
      </div> */}

      <DmsTable
        columns={[
          "sendTo",
          "subject",
          "reason",
          "responseExpected",
          "targetDate",
          "attachment",
          "created"
        ]}
        mockData={drafts}
      />
    </>
  );
}