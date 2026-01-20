import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { API_BASE_URL } from "../../config";

export default function TimelineSchedule() {
  const navigate = useNavigate();

  const [iframeSrc, setIframeSrc] = useState("");
  const [loading, setLoading] = useState(true);
  const projectId = sessionStorage.getItem("projectId");

  /* ---------- LOAD IFRAME ---------- */
  useEffect(() => {
    const load = async () => {
      setLoading(true);
      const url = await fetchTrustedTicketUrl(
        "/views/FortnightlyMeeting/FortnightlyPlanP6FN?:iid=1"
      );
      setIframeSrc(url);
    };
    load();
  }, []);

  /* ---------- TRUSTED TICKET ---------- */
  const fetchTrustedTicketUrl = async (view) => {
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
    } catch (e) {
      console.error(e);
      return "";
    }
  };

  return (
    <div style={{ height: "100vh", position: "relative" }}>
      {/* BACK BUTTON */}
      <button
        onClick={() => navigate("/modules")}
        style={{
          position: "absolute",
          top: 10,
          right: 20,
          zIndex: 9999,
        }}
      >
        Back
      </button>

      {loading && (
        <div style={{ textAlign: "center", marginTop: "20%" }}>
          Loading...
        </div>
      )}

      {iframeSrc && (
        <iframe
          src={iframeSrc}
          title="Fortnight Meeting"
          style={{ width: "100%", height: "100%", border: "none" }}
          onLoad={() => setLoading(false)}
        />
      )}
    </div>
  );
}
	