import React from "react";
import DmsTable from "../DmsTable/DmsTable";

export default function DmsDocuments() {
  return (
    <DmsTable
      columns={[
        "File Type","File Number","File Name","Revision No",
        "Status","Project Name","Contract Name","Folder",
        "Sub-Folder","Created By","Date Uploaded",
        "Revision Date","Department","View / Download"
      ]}
      mockData={[
        {
          "File Type": "Drawing",
          "File Number": "DWG-001",
          "File Name": "Bridge Layout",
          "Revision No": "R1",
          Status: "Approved",
          "Project Name": "WCR Project",
          "Contract Name": "Contract A",
          Folder: "Design",
          "Sub-Folder": "Bridge",
          "Created By": "Engineer",
          "Date Uploaded": "10-02-2025",
          "Revision Date": "11-02-2025",
          Department: "Engineering",
          "View / Download": "View"
        }
      ]}
    />
  );
}
