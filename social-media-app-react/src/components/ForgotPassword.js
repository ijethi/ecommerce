import React, { useState } from "react";
import { resetPassword } from "../api";
import "./ForgotPassword.css";
import logo from "../assets/logo.png";


function ForgotPassword() {
  const [formData, setFormData] = useState({
    email: "",
    securityAnswer: "",
    newPassword: "",
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
    setSuccess(false);
    try {
      await resetPassword(formData);
      setSuccess(true);
      setFormData({
        email: "",
        securityAnswer: "",
        newPassword: "",
      });
    } catch (err) {
      setError(
        "Error resetting password. Please ensure the email and security answer are correct."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="forgot-password">
      <img src={logo} alt="Logo" className="logo" />
      <h2>Forgot Password</h2>
      {error && <p className="error">{error}</p>}
      {success && <p className="success">Password reset successfully!</p>}
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
          <label>New Password:</label>
          <input
            type="password"
            name="newPassword"
            value={formData.newPassword}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit" disabled={loading}>
          {loading ? "Resetting Password..." : "Reset Password"}
        </button>
      </form>
    </div>
  );
}

export default ForgotPassword;
