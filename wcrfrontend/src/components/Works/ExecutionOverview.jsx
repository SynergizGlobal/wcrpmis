import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";
import { API_BASE_URL } from "../../config";

export default function ExecutionOverview() {
  const { saveRouteTitle } = usePageTitle();
  const location = useLocation();
  const navigate = useNavigate();

  const [projectId, setProjectId] = useState(null);
  const [iframeSrc, setIframeSrc] = useState("");
  const [loading, setLoading] = useState(true);

  /* ---------- GET PROJECT ID (PATH + SESSION) ---------- */
  useEffect(() => {
    const segments = location.pathname.split("/").filter(Boolean);
    const last = segments[segments.length - 1];

    const id =
      last && last !== "execution-overview"
        ? last
        : sessionStorage.getItem("projectId");

    if (id) {
      setProjectId(id);
      sessionStorage.setItem("projectId", id);
    }

    saveRouteTitle(
      location.pathname,
      id ? `Execution Overview - ${id}` : "Execution Overview"
    );
  }, [location.pathname, saveRouteTitle]);

  /* ---------- LOAD TABLEAU DASHBOARD ---------- */
  useEffect(() => {
    if (!projectId) return;

    const loadDashboard = async () => {
      setLoading(true);
      const url = await fetchTrustedTicketUrl(
        "execution-overview",
        "/views/Civilworks3/Civilworks?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no"
      );
      setIframeSrc(url);
    };

    loadDashboard();
  }, [projectId]);

  /* ---------- TRUSTED TICKET ---------- */
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
    } catch (err) {
      console.error("Trusted ticket error:", err);
      return "";
    }
  };

  return (
    <Dashboard>
      <div style={{ marginTop: 20, height: "80vh", position: "relative" }}>
        
        {/* ---------- BACK BUTTON ---------- */}
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

        {/* ---------- LOADER ---------- */}
        {loading && (
          <div
            style={{
              position: "absolute",
              top: "50%",
              left: "50%",
              transform: "translate(-50%, -50%)",
              zIndex: 999,
              color: "#555",
            }}
          >
            Loading dashboard...
          </div>
        )}

        {/* ---------- IFRAME ---------- */}
        {iframeSrc && (
          <iframe
            title="Execution Overview"
            src={iframeSrc}
            style={{
              width: "100%",
              height: "100%",
              borderRadius: "10px",
              display: loading ? "none" : "block",
            }}
            frameBorder="0"
            allowFullScreen
            onLoad={() => setLoading(false)}
          />
        )}
      </div>
    </Dashboard>
  );
}
