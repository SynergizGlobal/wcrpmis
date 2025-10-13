import React from "react";
import Dashboard from "../Dashboard/Dashboard";

export default function withDashboard(WrappedComponent) {
  return function DashboardWrapper(props) {
    return (
      <>
        <Dashboard title={props.title || "Dashboard"} />
        <WrappedComponent {...props} />
      </>
    );
  };
}
