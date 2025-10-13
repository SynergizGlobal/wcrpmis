import React, { useState } from 'react';
import styles from './Login.module.css';
import { API_BASE_URL } from "../../config";

import logo from "../../assets/images/wcr-logo.png";

export default function Login() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    rememberMe: false,
  });

    const [errorMessage, setErrorMessage] = useState("");
    const [loading, setLoading] = useState(false);

  const handleChange = (e) =>{
    const { name, type, value, checked} = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = async(e) => {
    e.preventDefault();
    setErrorMessage("");
    setLoading(true);

     try{
        const response = await fetch(`${API_BASE_URL}/login`,{
          method: "POST",
          headers: {"Content-Type": "application/json"},
          credentials: "include",
          body: JSON.stringify({
            userId: formData.username,
            password: formData.password,
          })
          
        });

        if(!response.ok){
          const errorData = await response.json();
          throw new Error(errorData.message || "Invalid credentials");
        }
        const data = await response.json();

        localStorage.setItem("token", "SESSION_AUTH");
        localStorage.setItem("user", JSON.stringify(data));

        window.location.href = "/wcrpmis/home";
    }
    catch(error){
      console.error("Login error:", error);
      setErrorMessage(error.message || "Login failed. PLease try again later.")
    }
    finally{
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <div className={styles.loginSection}>
        <div className={styles.loginBox}>
          <div className={styles.loginboxInner}>
            <div className={styles.logo}>
              <img src={logo} alt="Logo" />
              <h5>Login</h5>
            </div>
            <div className={styles.loginForm}>
              <form onSubmit={handleSubmit}>
                <div className="formField">
                  <label>Username: </label>
                  <input 
                  type="text" 
                  name="username"
                  value={formData.username}
                  onChange={handleChange}
                  required
                  />
                </div>
                <div className="formField">
                  <label>Password: </label>
                  <input 
                  type="password" 
                  name="password"
                  value={formData.password}
                  onChange={handleChange}
                  required
                  />
                </div>
                <div className="formField">
                  <div className="checkboxRow">
                    <input 
                    type="checkbox" 
                    name="rememberMe"
                    checked={formData.rememberMe}
                    onChange={handleChange}
                    />
                    <span>Remember Me</span>
                  </div>
                </div>

                {errorMessage && (
                  <div className={styles.error}>{errorMessage}</div>
                )}

                <div className="formField align-center">
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? "Logging in..." : "LOGIN"}
                  </button>
                </div>
                <div className="formField align-center">
                    <a href="#">Forgot Password?</a>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}