import React from "react";
import DmsTable from "../DmsTable/DmsTable";

export default function Correspondence() {
  return (
    <DmsTable
      columns={[
        "Category","Letter No","From","To","Subject",
        "Required Response","Due Date","Project",
        "Contract","Status","Department","Attachment","Type"
      ]}
      mockData={[
        {
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
  );
}
