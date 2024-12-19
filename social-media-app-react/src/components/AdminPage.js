import React, { useEffect, useState } from "react";
import {
  fetchAllUsers,
  createUserByAdmin,
  removeUser,
  activateUser,
  rejectUser,
} from "../api";
import "./AdminPage.css";
import logo from "../assets/logo.png";

const AdminPage = () => {
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);
  const [formData, setFormData] = useState({
    password: "",
    email: "",
    securityQuestion: "",
    securityAnswer: "",
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({ ...formData, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    try {
      await createUserByAdmin(formData);
      setSuccess(true);
      setFormData({
        password: "",
        email: "",
        securityQuestion: "",
        securityAnswer: "",
      });
      fetchUsers(); // Refresh users list after creating a user
    } catch (err) {
      setError("Error creating user profile");
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (user) => {
    try {
      await removeUser(user);
      setUsers(users.filter((u) => u.email !== user.email));
    } catch (error) {
      console.error("Error removing user:", error.message);
    }
  };

  const handleActivation = async (user) => {
    try {
      await activateUser(user.email);
      fetchUsers(); // Refresh users list after activating a user
    } catch (error) {
      console.error("Error activating user:", error.message);
    }
  };

  const handleRejection = async (user) => {
    try {
      await rejectUser(user.email);
      setUsers(users.filter((u) => u.email !== user.email));
    } catch (error) {
      console.error("Error rejecting user:", error.message);
    }
  };

  const fetchUsers = async () => {
    try {
      const users = await fetchAllUsers();
      setUsers(users);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchUsers();
  }, []);

  return (
    <div>
      <img src={logo} alt="Logo" className="logo" />
      <h2 className="title">Admin Page</h2>
      <div className="admin-page">
        <h2>Pending Users</h2>
        <table>
          <thead>
            <tr>
              <th>Email</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {users
              .filter((user) => user.role === "PENDING")
              .map((user) => (
                <tr key={user.email}>
                  <td>{user.email}</td>
                  <td>
                    <button onClick={() => handleActivation(user)}>
                      Activate User
                    </button>
                    <button onClick={() => handleRejection(user)}>
                      Reject User
                    </button>
                  </td>
                </tr>
              ))}
          </tbody>
        </table>

        <h2>Active Users</h2>
        <table>
          <thead>
            <tr>
              <th>Email</th>
              <th>Role</th>
              <th>Status</th>
              <th>Interests</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            {users
              .filter((user) => user.role !== "PENDING")
              .map((user) => (
                <tr key={user.email}>
                  <td>{user.email}</td>
                  <td>{user.role}</td>
                  <td>{user.status}</td>
                  <td>{user.interests}</td>
                  <td>
                    {user.role !== "ADMIN" && (
                      <button onClick={() => handleDelete(user)}>
                        Delete User
                      </button>
                    )}
                  </td>
                </tr>
              ))}
          </tbody>
        </table>
        <h2>Register a New User</h2>
        <p>
          Note: When registering a new user, email and password must meet
          requirements.
        </p>
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
            {loading ? "Creating User..." : "Create User"}
          </button>
        </form>
      </div>
    </div>
  );
};

export default AdminPage;
