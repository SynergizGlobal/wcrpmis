import React, { useEffect, useState } from "react";
import api from "../../../../api/axiosInstance";
import DmsTable from "../DmsTable/DmsTable";

export default function Drafts({ onBack }) {

  const [drafts, setDrafts] = useState([]);

  const fetchDrafts = async () => {
    try {
      const res = await api.post("/api/documents/drafts",{
        draw:1,
        start:0,
        length:10
      });

      const rows = res.data.data || [];

      const mapped = rows.map(d => ({
        "Send To": d.sendTo,
        "Subject": d.sendSubject,
        "Reason": d.sendReason,
        "Response Expected": d.responseExpected,
        "Target Date": d.targetResponseDate,
        "Attachment": d.attachmentName,
        "Created": new Date(d.createdAt).toLocaleString()
      }));

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
          "Send To",
          "Subject",
          "Reason",
          "Response Expected",
          "Target Date",
          "Attachment",
          "Created"
        ]}
        mockData={drafts}
      />
    </>
  );
}