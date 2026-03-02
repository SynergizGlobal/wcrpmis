import React, { useEffect, useState } from "react";
import api from "../../../../api/axiosInstance";
import DmsTable from "../DmsTable/DmsTable";

export default function Drafts({ onBack, onEdit }) {

  const [tableData, setTableData] = useState([]);
  const [loading, setLoading] = useState(false);

  const fetchDrafts = async () => {
    try {
      setLoading(true);

       const filter = {
          "-1": ["draft", "Save as Draft"]
        };

      const firstRes = await api.post("/api/correspondence/filter-data", {
        draw: 1,
        start: 0,
        length: 10,
        columnFilters: filter
      });

      

      const total = firstRes.data?.recordsTotal || 10;

      const res = await api.post("/api/correspondence/filter-data", {
        draw: 2,
        start: 0,
        length: total,
        columnFilters: filter
      });

      console.log(res.data.data);

      const rows = res.data?.data || [];

      const mapped = rows.map((r) => ({
        correspondenceId: r.correspondenceId,
        Category: r.category || "",
        "Letter Code": r.referenceNumber || "",
        "Letter Number": r.letterNumber || "",
        From: r.from || "",
        To: r.to || "",
        Subject: r.subject || "",
        "Required Response": r.requiredResponse || "",
        "Letter Date": r.letterDate || "",
        "Project Name": r.projectName || "",
        "Contract Name": r.contractName || "",
        Status: r.currentStatus || "",
        Department: r.department || "",
        Attachment: r.attachment || "",
        Type: r.type || ""
      }));

      setTableData(mapped);
      setLoading(false);

    } catch (err) {
      console.error("Draft fetch error:", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchDrafts();
  }, []);

  return (
    <>
      {/* <button className="btn btn-white" onClick={onBack} style={{ marginBottom: 10 }}>
        Back
      </button> */}

      <DmsTable
        loading={loading}
        columns={[
          "Category",
          "Letter Code",
          "Letter Number",
          "From",
          "To",
          "Subject",
          "Required Response",
          "Letter Date",
          "Project Name",
          "Contract Name",
          "Status",
          "Department",
          "Attachment",
          "Type"
        ]}
        mockData={tableData}
        onRowClick={(row) => onEdit(row.correspondenceId)}
      />
    </>
  );
}