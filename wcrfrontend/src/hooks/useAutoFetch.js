import { useEffect, useState, useContext } from "react";
import { AutoRefreshContext } from "../context/AutoRefreshContext";
import api from "../api/axiosInstance";

export default function useAutoFetch(url, deps = []) {
  const { refreshFlag } = useContext(AutoRefreshContext);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(true);

  const fetchNow = async () => {
    setLoading(true);
    try {
      const res = await api.get(url);
      setData(res.data);
    } catch (err) {
      console.error("Error auto-fetching:", err);
    }
    setLoading(false);
  };

  useEffect(() => {
    fetchNow(); // initial load
  }, deps);

  useEffect(() => {
    fetchNow(); // auto refresh when POST/PUT/DELETE happens
  }, [refreshFlag]);

  return { data, loading, refetch: fetchNow };
}
