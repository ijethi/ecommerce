import React from "react";
import { Link } from "react-router-dom";
import { useUser } from "../contexts/UserContext";
import "./Navbar.css";

const Navbar = () => {
  const { user, setUser } = useUser();

  const handleLogout = () => {
    setUser(null);
  };

  return (
    <nav className="nav-bar">
      <ul>
        <li>
          <Link to="/home">Home</Link>
        </li>
        <li>
          <Link to="/profile">Profile</Link>
        </li>
        <li>
          <Link to="/friends">Friends</Link>
        </li>
        <li>
          <Link to="/friend-requests">Friend Requests</Link>
        </li>
        {user && user.role === "ADMIN" && (
          <li>
            <Link to="/admin">Admin</Link>
          </li>
        )}
        {user && (
          <li>
            <button onClick={handleLogout}>Logout</button>
          </li>
        )}
      </ul>
    </nav>
  );
};

export default Navbar;
