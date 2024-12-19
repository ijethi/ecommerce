import React, { useState } from "react";
import { Link } from "react-router-dom";
import { createUser } from "../api";
import "./SignUp.css";
import logo from "../assets/logo.png";

function SignUp() {
  const [formData, setFormData] = useState({
    password: "",
    email: "",
    securityQuestion: "",
    securityAnswer: "",
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
      await createUser(formData);
      setSuccess(true);
      setFormData({
        password: "",
        email: "",
        securityQuestion: "",
        securityAnswer: "",
      });
    } catch (err) {
      setError("Error creating user profile");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="sign-up">
      <img src={logo} alt="Logo" className="logo" />
      <h2>Sign Up</h2>
      {error && <p className="error">{error}</p>}
      {success && <p className="success">User created successfully!</p>}
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

        <button type="submit" disabled={loading}>
          {loading ? "Signing Up..." : "Sign Up"}
        </button>
      </form>
      <div className="links">
        <p>
          Already have an account? <Link to="/">Log in</Link>
        </p>
        <p>
          Want to sign up as an admin?{" "}
          <Link to="/admin-signup">Admin Sign Up</Link>
        </p>
      </div>
    </div>
  );
}

export default SignUp;