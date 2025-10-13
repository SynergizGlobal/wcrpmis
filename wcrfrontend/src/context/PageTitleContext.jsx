import React, { createContext, useState, useContext } from "react";

// Create a context
const PageTitleContext = createContext();



// Custom hook for easy access
export function usePageTitle() {
  return useContext(PageTitleContext);
}

// Provider component
export function PageTitleProvider({ children }) {
  const [pageTitle, setPageTitle] = useState("<>Western Central Railways</>");
  const [routeTitles, setRouteTitles] = useState({});

  const saveRouteTitle = (path, title) => {
    setRouteTitles((prev) => ({ ...prev, [path]: title }));
    setPageTitle(title);
  };

  return (
    <PageTitleContext.Provider value={{ pageTitle, setPageTitle, routeTitles, saveRouteTitle}}>
      {children}
    </PageTitleContext.Provider>
  );
}
