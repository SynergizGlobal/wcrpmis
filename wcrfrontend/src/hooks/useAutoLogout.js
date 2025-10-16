import { useEffect } from "react";

export default function useAutoLogout(timeoutMinutes = 25) {
  useEffect(() => {
    let logoutTimer;

    const resetTimer = () => {
      clearTimeout(logoutTimer);
      logoutTimer = setTimeout(logoutUser, timeoutMinutes * 60 * 1000);
    };

    const logoutUser = () => {
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      alert("Session expired. Please log in again.");
      window.location.href = "/wcrpmis/";
    };

    // Events that reset the inactivity timer
    const events = ["mousemove", "keydown", "click", "scroll"];
    events.forEach((event) => window.addEventListener(event, resetTimer));

    resetTimer(); // Start timer initially

    return () => {
      clearTimeout(logoutTimer);
      events.forEach((event) => window.removeEventListener(event, resetTimer));
    };
  }, [timeoutMinutes]);
}
