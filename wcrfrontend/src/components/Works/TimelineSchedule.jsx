import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";
import { API_BASE_URL } from "../../config";

export default function TimelineSchedule() {
  const { saveRouteTitle } = usePageTitle();
  const location = useLocation();
  const navigate = useNavigate();

  const [projectId, setProjectId] = useState(null);
  const [iframeSrc, setIframeSrc] = useState("");

  /* ---------------- PROJECT ID (PATH + SESSION) ---------------- */
  useEffect(() => {
    const segments = location.pathname.split("/").filter(Boolean);
    const last = segments[segments.length - 1];

    let id =
      last && last !== "timeline-schedule"
        ? last
        : sessionStorage.getItem("projectId");

    if (id) {
      setProjectId(id);
      sessionStorage.setItem("projectId", id);
    }

    saveRouteTitle(
      location.pathname,
      id ? `Timeline Schedule - ${id}` : "Timeline Schedule"
    );
  }, [location.pathname, saveRouteTitle]);

  /* ---------------- TRUSTED TICKET (REUSED) ---------------- */
  useEffect(() => {
    if (!projectId) return;

    const loadIframe = async () => {
      const url = await fetchTrustedTicketUrl(
        "timeline-schedule",
        "/views/Timelineshedule-WORKS_17667291338900/Contractleveldash?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no"
      );
      setIframeSrc(url);
    };

    loadIframe();
  }, [projectId]);

  /* ---------------- YOUR EXISTING FUNCTION ---------------- */
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

      const params = [];
      if (projectId) params.push(`projectId=${projectId}`);

      const joiner = urllink.includes("?") ? "&" : "?";
      const queryParams = params.length ? joiner + params.join("&") : "";

      return `http://115.124.125.227:8000/trusted/${ticket}/${urllink}${queryParams}`;
    } catch (err) {
      console.error("Error fetching trusted ticket:", err);
      return "";
    }
  };

  return (
    <Dashboard>
      <div style={{ marginTop: "20px", height: "80vh", position: "relative" }}>
        
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
            title="Timeline Schedule"
            src={iframeSrc}
            style={{
              marginTop: 25,
              width: "100%",
              height: "100%",
              borderRadius: 10
            }}
            frameBorder="0"
            allowFullScreen
          />
        )}
      </div>
    </Dashboard>
  );
}
