import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";
import { API_BASE_URL } from "../../config";

export default function ProgressWeightages() {
  const { saveRouteTitle } = usePageTitle();
  const location = useLocation();
  const navigate = useNavigate();

  const [projectId, setProjectId] = useState(null);
  const [iframeSrc, setIframeSrc] = useState("");

  /* ---------- GET PROJECT ID (PATH + SESSION) ---------- */
  useEffect(() => {
    const segments = location.pathname.split("/").filter(Boolean);
    const last = segments[segments.length - 1];

    const id =
      last && last !== "progress-weightages"
        ? last
        : sessionStorage.getItem("projectId");

    if (id) {
      setProjectId(id);
      sessionStorage.setItem("projectId", id);
    }

    saveRouteTitle(
      location.pathname,
      id ? `Progress Weightages - ${id}` : "Progress Weightages"
    );
  }, [location.pathname, saveRouteTitle]);

  /* ---------- LOAD TABLEAU DASHBOARD ---------- */
  useEffect(() => {
    if (!projectId) return;

    const loadDashboard = async () => {
      const url = await fetchTrustedTicketUrl(
        "progress-weightages",
        "/views/ProjectWeightage/Dashboard1?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no"
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

        {/* ---------- IFRAME ---------- */}
        {iframeSrc && (
          <iframe
            title="Progress Weightages"
            src={iframeSrc}
            style={{
              width: "100%",
              height: "100%",
              borderRadius: "10px",
            }}
            frameBorder="0"
            allowFullScreen
          />
        )}
      </div>
    </Dashboard>
  );
}
