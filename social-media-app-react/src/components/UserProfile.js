import React, { useState, useEffect } from "react";
import {
  fetchUserProfile,
  updateInterests,
  updateStatus,
  updateTag,
} from "../api";
import { useUser } from "../contexts/UserContext";
import "./UserProfile.css";
import logo from "../assets/logo.png";

function UserProfile() {
  const { user } = useUser();
  const [userData, setUserData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editableFields, setEditableFields] = useState({});

  useEffect(() => {
    if (user.email) {
      fetchUserProfile(user.email)
        .then((data) => {
          setUserData(data);
          setLoading(false);
        })
        .catch((error) => {
          console.error("Error fetching user profile:", error);
          setLoading(false);
        });
    }
  }, [user.email]);

  const handleEdit = (field) => {
    setEditableFields({ ...editableFields, [field]: true });
  };

  const handleSave = async (field, value) => {
    try {
      if (field === "interests") {
        await updateInterests(user.email, value);
      } else if (field === "status") {
        await updateStatus(user.email, value);
      } else if (field === "tag") {
        await updateTag(user.email, value);
      }
      setEditableFields({ ...editableFields, [field]: false });
      fetchUserProfile(user.email).then((data) => setUserData(data));
    } catch (error) {
      console.error(`Error updating ${field}:`, error);
    }
  };

  const handleBack = (field) => {
    setEditableFields({ ...editableFields, [field]: false });
  };

  const statusOptions = ["", "Online", "Away", "Busy"];
  const tagOptions = [
    "",
    "First Year",
    "2nd Year",
    "3rd Year",
    "4th Year",
    "Alumni",
  ];

  if (loading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="user-profile">
      <img src={logo} alt="Logo" className="logo" />
      <div className="title">
        <h2>User Profile</h2>
        <p>Make changes to your profile here!</p>
      </div>
      <div>
        <p>Email: {userData?.email || "No email available"}</p>
        <p>
          Personal Interests:{" "}
          {editableFields.interests ? (
            <>
              <textarea
                value={userData?.interests || ""}
                onChange={(e) =>
                  setUserData({ ...userData, interests: e.target.value })
                }
              />
              <button
                onClick={() => handleSave("interests", userData?.interests)}
              >
                Save
              </button>
              <button onClick={() => handleBack("interests")}>Back</button>
            </>
          ) : (
            <>
              <span>{userData?.interests || "No interests available"}</span>
              <button onClick={() => handleEdit("interests")}>Edit</button>
            </>
          )}
        </p>
        <p>
          Status:{" "}
          {editableFields.status ? (
            <>
              <select
                value={userData?.status || ""}
                onChange={(e) =>
                  setUserData({ ...userData, status: e.target.value })
                }
              >
                {statusOptions.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
              </select>
              <button onClick={() => handleSave("status", userData?.status)}>
                Save
              </button>
              <button onClick={() => handleBack("status")}>Back</button>
            </>
          ) : (
            <>
              <span>{userData?.status || "No status available"}</span>
              <button onClick={() => handleEdit("status")}>Edit</button>
            </>
          )}
        </p>
        <p>
          Tag:{" "}
          {editableFields.tag ? (
            <>
              <select
                value={userData?.tag || ""}
                onChange={(e) =>
                  setUserData({ ...userData, tag: e.target.value })
                }
              >
                {tagOptions.map((option) => (
                  <option key={option} value={option}>
                    {option}
                  </option>
                ))}
              </select>
              <button onClick={() => handleSave("tag", userData?.tag)}>
                Save
              </button>
              <button onClick={() => handleBack("tag")}>Back</button>
            </>
          ) : (
            <>
              <span>{userData?.tag || "No tag available"}</span>
              <button onClick={() => handleEdit("tag")}>Edit</button>
            </>
          )}
        </p>
      </div>
    </div>
  );
}

export default UserProfile;
