import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";
import { API_BASE_URL } from "../../config";

export default function ContractsOverviewInProgress() {
  const { saveRouteTitle } = usePageTitle();
  const location = useLocation();
  const navigate = useNavigate();

  const [projectId, setProjectId] = useState(null);
  const [iframeSrc, setIframeSrc] = useState("");

  /* -------- projectId (path + session) -------- */
  useEffect(() => {
    const parts = location.pathname.split("/").filter(Boolean);
    const last = parts[parts.length - 1];

    const id =
      last && last !== "contracts-overview-completed"
        ? last
        : sessionStorage.getItem("projectId");

    if (id) {
      setProjectId(id);
      sessionStorage.setItem("projectId", id);
    }

    saveRouteTitle(
      location.pathname,
      id ? `Contracts Completed - ${id}` : "Contracts Completed"
    );
  }, [location.pathname, saveRouteTitle]);

  /* -------- load tableau -------- */
  useEffect(() => {
    if (!projectId) return;

    const load = async () => {
      const url = await fetchTrustedTicketUrl(
        "contracts-overview-completed",
        "/views/ContractCompleted_17665485543350/CompletedContracts?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no"
      );
      setIframeSrc(url);
    };

    load();
  }, [projectId]);

  /* -------- trusted ticket -------- */
  const fetchTrustedTicketUrl = async (_, view) => {
    try {
      const res = await fetch(`${API_BASE_URL}/tableau/ticket`, {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({
          username: "SynTrack",
          client_ip: "10.48.192.7",
        }),
      });

      const ticket = await res.text();
      return `http://115.124.125.227:8000/trusted/${ticket}/${view}?projectId=${projectId}`;
    } catch {
      return "";
    }
  };

  return (
    <Dashboard>
      <div style={{ height: "80vh", marginTop: 20, position: "relative" }}>
	  <button
	    onClick={() => navigate(`/Works?project_id=${projectId}`)}
	    style={{
	      position: "absolute",
	      top: 0,
	      right: 10,
	      zIndex: 9999,
	      padding: "6px 12px",
	      borderRadius: "4px",
	      border: "none",
	      background: "#007bff",
	      color: "#fff",
	      cursor: "pointer",
	    }}
	  >
	    Back
	  </button>


        {iframeSrc && (
          <iframe
            src={iframeSrc}
            title="Contracts Overview"
            style={{ width: "100%", height: "100%" }}
            frameBorder="0"
          />
        )}
      </div>
    </Dashboard>
  );
}
