import axios from "axios";

axios.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.warn("Session expired, logging out...");
      localStorage.removeItem("token");
      localStorage.removeItem("user");
      window.location.href = "/wcrpmis/";
    }
    return Promise.reject(error);
  }
);
