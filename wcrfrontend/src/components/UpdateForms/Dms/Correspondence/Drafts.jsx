import React from "react";
import DmsTable from "../DmsTable/DmsTable";

export default function Drafts({ onBack }) {
  return (
    <>
      <button onClick={onBack} style={{ marginBottom: 10 }}>
        Back to Correspondence
      </button>

      <DmsTable
        columns={[
          "Category","Letter No","Subject",
          "Project","Contract","Last Saved"
        ]}
        mockData={[
          {
            Category: "Technical",
            "Letter No": "DRAFT-001",
            Subject: "Kick-off Meeting",
            Project: "MUTP IIIA",
            Contract: "224 Construction of Important Bridges",
            "Last Saved": "09-02-2026"
          }
        ]}
      />
    </>
  );
}
