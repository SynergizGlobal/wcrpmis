const getApiUrl = () => {
  const envUrl = process.env.REACT_APP_API_URL;

  if (envUrl) {
    return envUrl;
  }

  if (process.env.NODE_ENV === "production") {
    return "https://syntrackpro.com/wcr";
  } else {
    return "http://localhost:8080";
  }
};

export const API_BASE_URL = getApiUrl();