// src/api/axiosInstance.js
import axios from "axios";
import { queryClient } from "../queryClient";

let getCache = {};

const api = axios.create({
  baseURL: process.env.REACT_APP_API_URL,
  withCredentials: true,
});

// -----------------------------------------
// Prevent browser caching GET requests
// -----------------------------------------
api.interceptors.request.use((config) => {
  if (config.method === "get") {
    config.headers["Cache-Control"] = "no-cache";
    config.headers["Pragma"] = "no-cache";
    config.headers["Expires"] = "0";

    config.params = {
      ...(config.params || {}),
      _t: Date.now(),
    };
  }
  return config;
});

// -----------------------------------------
// Auto-refresh trigger for POST/PUT/DELETE
// -----------------------------------------
api.interceptors.response.use(
  (response) => {
    const method = response.config?.method?.toLowerCase();
    const mutating = ["post", "put", "delete", "patch"];

    if (mutating.includes(method)) {
      // 🔥 GLOBAL REFRESH OF ALL FRONTEND DATA\
      getCache = {};
      queryClient.invalidateQueries();
    }

    return response;
  },

  (error) => {
    if (error.response?.status === 401) {
      localStorage.removeItem("token");
      window.location.href = "/wcrpmis/";
    }
    return Promise.reject(error);
  }
);

export default api;
