import React, { useState } from "react";
import { Link } from "react-router-dom";
import { adminSignUp } from "../api";
import "./AdminSignUp.css";
import logo from "../assets/logo.png";

function AdminSignUp() {
  const [formData, setFormData] = useState({
    password: "",
    email: "",
    securityQuestion: "",
    securityAnswer: "",
    secretKey: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await adminSignUp(formData);
      setSuccess(true);
      setFormData({
        password: "",
        email: "",
        securityQuestion: "",
        securityAnswer: "",
        secretKey: "",
      });
    } catch (err) {
      setError("Error creating admin profile");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="admin-sign-up">
      <img src={logo} alt="Logo" className="logo" />
      <h2>Admin Sign Up</h2>
      {error && <p className="error">{error}</p>}
      {success && <p className="success">Admin created successfully!</p>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Security Question:</label>
          <input
            type="text"
            name="securityQuestion"
            value={formData.securityQuestion}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Security Answer:</label>
          <input
            type="text"
            name="securityAnswer"
            value={formData.securityAnswer}
            onChange={handleChange}
            required
          />
        </div>

        <div className="form-group">
          <label>Secret Key:</label>
          <input
            type="text"
            name="secretKey"
            value={formData.secretKey}
            onChange={handleChange}
            required
          />
        </div>

        <button type="submit" disabled={loading}>
          {loading ? "Signing Up..." : "Sign Up"}
        </button>
      </form>
      <div className="links">
        <p>
          Already have an account? <Link to="/">Log in</Link>
        </p>
        <p>
          Don't have an account? <Link to="/signup">Sign up</Link>
        </p>
      </div>
    </div>
  );
}

export default AdminSignUp;
