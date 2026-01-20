import React, { useEffect, useState } from "react";
import { useLocation, useNavigate } from "react-router-dom";
import Dashboard from "../Dashboard/Dashboard";
import { usePageTitle } from "../../context/PageTitleContext";
import { API_BASE_URL } from "../../config";

export default function UtilityShiftingStripchart() {
  const { saveRouteTitle } = usePageTitle();
  const location = useLocation();
  const navigate = useNavigate();

  const [projectId, setProjectId] = useState(null);
  const [iframeSrc, setIframeSrc] = useState("");

  /* ---------- PROJECT ID (PATH + SESSION) ---------- */
  useEffect(() => {
    const parts = location.pathname.split("/").filter(Boolean);
    const last = parts[parts.length - 1];

    const id =
      last && last !== "utility-shifting-details"
        ? last
        : sessionStorage.getItem("projectId");

    if (id) {
      setProjectId(id);
      sessionStorage.setItem("projectId", id);
    }

    saveRouteTitle(
      location.pathname,
	  id ? `Utility Shifting Map - ${id}` : "Utility Shifting Map"
    );
  }, [location.pathname, saveRouteTitle]);

  /* ---------- LOAD IFRAME ---------- */
  useEffect(() => {
    if (!projectId) return;

    const load = async () => {
      const url = await fetchTrustedTicketUrl(
        "utility-shifting-details",
        "/views/UtilityShiftingMap_17667292216510/Dashboard1?:showAppBanner=false&:display_count=n&:showVizHome=n&:origin=viz_share_link&:toolbar=no&:tabs=no"
      );
      setIframeSrc(url);
    };

    load();
  }, [projectId]);

  /* ---------- TRUSTED TICKET ---------- */
  const fetchTrustedTicketUrl = async (path, urllink) => {
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
      return `http://115.124.125.227:8000/trusted/${ticket}/${urllink}?projectId=${projectId}`;
    } catch (e) {
      console.error(e);
      return "";
    }
  };

  return (
    <Dashboard>
      <div style={{ height: "80vh", position: "relative", marginTop: 20 }}>
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
            title="Utility Shifting Details"
            style={{ width: "100%", height: "100%" }}
            frameBorder="0"
          />
        )}
      </div>
    </Dashboard>
  );
}
