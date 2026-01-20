import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";
import { API_BASE_URL } from "../../config";

export default function IssuesPendingDashboard() {
  const { saveRouteTitle } = usePageTitle();
  const location = useLocation();
  const navigate = useNavigate();

  const [projectId, setProjectId] = useState(null);
  const [iframeSrc, setIframeSrc] = useState("");

  /* ---------- PROJECT ID (PATH + SESSION) ---------- */
  useEffect(() => {
    const segments = location.pathname.split("/").filter(Boolean);
    const last = segments[segments.length - 1];

    const id =
      last && last !== "issues-pending-dashboard"
        ? last
        : sessionStorage.getItem("projectId");

    if (id) {
      setProjectId(id);
      sessionStorage.setItem("projectId", id);
    }

    saveRouteTitle(
      location.pathname,
      id ? `Issues Pending - ${id}` : "Issues Pending"
    );
  }, [location.pathname, saveRouteTitle]);

  /* ---------- LOAD TABLEAU IFRAME ---------- */
  useEffect(() => {
    if (!projectId) return;

    const loadIframe = async () => {
      const url = await fetchTrustedTicketUrl(
        "issues-pending-dashboard",
        "/views/pending_issue/PendingIssues?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no"
      );
      setIframeSrc(url);
    };

    loadIframe();
  }, [projectId]);

  /* ---------- TRUSTED TICKET ---------- */
  const fetchTrustedTicketUrl = async (path, urllink) => {
    try {
      const res = await fetch(`${API_BASE_URL}/tableau/ticket`, {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
        body: new URLSearchParams({
          username: "SynTrack",
          client_ip: "10.48.192.7",
        }),
      });

      const ticket = await res.text();
      return `http://115.124.125.227:8000/trusted/${ticket}/${urllink}?projectId=${projectId}`;
    } catch (err) {
      console.error("Trusted ticket error:", err);
      return "";
    }
  };

  return (
    <Dashboard>
      <div style={{ marginTop: 20, height: "80vh", position: "relative" }}>
        
        {/* BACK BUTTON */}
        <button
          onClick={() => navigate(`/Works?project_id=${projectId}`)}
          style={{
            position: "absolute",
            top: 0,
            right: 10,
            zIndex: 9999,
            padding: "6px 12px",
            borderRadius: 4,
            border: "none",
            background: "#007bff",
            color: "#fff",
            cursor: "pointer",
          }}
        >
          Back
        </button>


        {/* IFRAME */}
        {iframeSrc && (
          <iframe
            title="Issues Pending Dashboard"
            src={iframeSrc}
            style={{
              marginTop: 25,
              width: "100%",
              height: "100%",
              borderRadius: 10,
            }}
            frameBorder="0"
            allowFullScreen
          />
        )}
      </div>
    </Dashboard>
  );
}
