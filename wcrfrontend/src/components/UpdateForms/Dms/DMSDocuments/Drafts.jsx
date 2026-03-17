import React, { useEffect, useState } from "react";
import DmsTable from "../DmsTable/DmsTable";
import api from "../../../../api/axiosInstance";

export default function Drafts({ onDraftClick  }) {

  const [drafts, setDrafts] = useState([]);

  const fetchDrafts = async () => {
    try {
      const res = await api.get("/api/documents/drafts");

      let rows = res.data;

      if (typeof rows === "string") {
        rows = JSON.parse(rows);
      }

      if (!Array.isArray(rows)) {
        rows = [];
      }

      const mapped = rows.map((d) => {

        console.log("RAW DRAFT:", d);

      let targetDate = "";

      if (Array.isArray(d.targetResponseDate)) {
        const [y, m, day] = d.targetResponseDate;
        targetDate = `${y}-${String(m).padStart(2,"0")}-${String(day).padStart(2,"0")}`;
      } else if (typeof d.targetResponseDate === "string") {
        targetDate = d.targetResponseDate;
      }

      return {
        id: d.id,
        docId: d.docId,
        sendTo: d.sendTo || "",
        subject: d.sendSubject || "",
        reason: d.sendReason || "",
        responseExpected: d.responseExpected || "",
        targetDate: targetDate,
        attachment: d.attachmentName || "",
        created: d.createdAt || ""
      };
    });

      console.log("DRAFT API DATA:", mapped);

      setDrafts(mapped);

    } catch (err) {
      console.error(err);
    }
  };

  useEffect(() => {
    fetchDrafts();
  }, []);

  return (
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
      onRowClick={(row) => onDraftClick(row)}
    />
  );
}