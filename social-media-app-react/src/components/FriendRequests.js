import React, { useEffect, useState } from "react";
import {
  fetchFriendRequests,
  acceptFriendRequest,
  rejectFriendRequest,
} from "../api";
import "./FriendRequests.css";
import logo from "../assets/logo.png";

const FriendRequests = ({ email }) => {
  const [friendRequests, setFriendRequests] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchRequests = async () => {
      try {
        const requests = await fetchFriendRequests(email);
        console.log("Fetched friend requests:", requests);
        setFriendRequests(requests);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchRequests();
  }, [email]);

  const handleAccept = async (requestId) => {
    console.log("Handling accept for requestId:", requestId);
    try {
      await acceptFriendRequest(requestId);
      alert("Friend request accepted successfully!");
      setFriendRequests(friendRequests.filter((req) => req.id !== requestId));
    } catch (err) {
      alert("Error accepting friend request: " + err.message);
    }
  };

  const handleReject = async (requestId) => {
    console.log("Handling reject for requestId:", requestId);
    try {
      await rejectFriendRequest(requestId);
      alert("Friend request rejected successfully!");
      setFriendRequests(friendRequests.filter((req) => req.id !== requestId));
    } catch (err) {
      alert("Error rejecting friend request: " + err.message);
    }
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="friend-requests">
      <img src={logo} alt="Logo" className="logo" />
      <h3>Friend Requests</h3>
      {friendRequests.length === 0 ? (
        <p>No friend requests found.</p>
      ) : (
        <ul>
          {friendRequests.map((request) => (
            <li key={request.userEmail}>
              {request.userEmail} wants to be friends with you.
              <button onClick={() => handleAccept(request.id)}>Accept</button>
              <button onClick={() => handleReject(request.id)}>Reject</button>
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default FriendRequests;
