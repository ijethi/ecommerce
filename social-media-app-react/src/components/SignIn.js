import React, { useState } from "react";
import { signInUser } from "../api";
import { useNavigate, Link } from "react-router-dom";
import { useUser } from "../contexts/UserContext";
import "./SignIn.css";
import logo from "../assets/logo.png";

function SignIn() {
  const [formData, setFormData] = useState({ email: "", password: "" });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const { setUser } = useUser();
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      const user = await signInUser(formData);
      setUser(user);
      navigate("/home");
    } catch (err) {
      if(err.response.status === 403){
        setError("Your Account is currently pending approval.");
      }else{
        setError("Error signing in");
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="sign-in">
      <img src={logo} alt="Logo" className="logo" />
      <h2>Sign In</h2>
      {error && <p className="error">{error}</p>}
      <form onSubmit={handleSubmit}>
        <div>
          <label>Email:</label>
          <input
            type="email"
            name="email"
            value={formData.email}
            onChange={handleChange}
            required
          />
        </div>
        <div>
          <label>Password:</label>
          <input
            type="password"
            name="password"
            value={formData.password}
            onChange={handleChange}
            required
          />
        </div>
        <button type="submit" disabled={loading}>
          {loading ? "Signing In..." : "Sign In"}
        </button>
      </form>
      <div className="links">
        <p>
          Want to sign up as an admin?{" "}
          <Link to="/admin-signup">Admin Sign Up</Link>
        </p>
        <p>
          Don't have an account? <Link to="/signup">Sign up</Link>
        </p>
        <p>
          <Link to="/forgot-password">Forgot Password?</Link>
        </p>
      </div>
    </div>
  );
}

export default SignIn;
