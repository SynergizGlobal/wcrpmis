import React, { useState } from 'react';
import styles from './Login.module.css';

import logo from "../../assets/images/wcr-logo.png";

export default function Login() {
  const [formData, setFormData] = useState({
    username: "",
    password: "",
    rememberMe: false,
  });

  const handleChange = (e) =>{
    const { name, type, value, checked} = e.target;
    setFormData({
      ...formData,
      [name]: type === "checkbox" ? checked : value,
    });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("login Date:", formData)
  }

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
                <div className="formField align-center">
                  <button type="submit" className="btn btn-primary">
                    LOGIN
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