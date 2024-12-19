import React, { useEffect, useState } from "react";
import {
  fetchFriendsList,
  deleteFriend,
  searchUsers,
  sendFriendRequest,
} from "../api";
import SearchBar from "./SearchBar";
import "./FriendsList.css";
import logo from "../assets/logo.png";
import { Link } from "react-router-dom";

const debounce = (func, delay) => {
  let timeoutId;
  return (...args) => {
    if (timeoutId) {
      clearTimeout(timeoutId);
    }
    timeoutId = setTimeout(() => {
      func(...args);
    }, delay);
  };
};

const FriendsList = ({ email }) => {
  const [friends, setFriends] = useState([]);
  const [filteredFriends, setFilteredFriends] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");
  const [noResults, setNoResults] = useState(false);
  const [tagFilter, setTagFilter] = useState("");

  useEffect(() => {
    const fetchFriends = async () => {
      try {
        const friendsList = await fetchFriendsList(email);
        setFriends(friendsList);
        setFilteredFriends(friendsList);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchFriends();
  }, [email]);

  useEffect(() => {
    const performSearch = debounce(async (term, tag, friendsList) => {
      let filteredList = friendsList;

      if (term.trim() !== "") {
        const users = await searchUsers(term);
        filteredList = users.filter(
          (user) =>
            user.email
              .split("@")[0]
              .toLowerCase()
              .startsWith(term.toLowerCase()) && user.email !== email
        );
      }

      if (tag !== "") {
        filteredList = filteredList.filter((user) => user.tag === tag);
      }

      setFilteredFriends(filteredList);
      setNoResults(filteredList.length === 0);
    }, 300);

    performSearch(searchTerm, tagFilter, friends);
  }, [searchTerm, tagFilter, friends, email]);

  const handleDeleteFriend = async (email, friendEmail) => {
    try {
      await deleteFriend(email, friendEmail);
      const updatedFriends = friends.filter(
        (friend) => friend.email !== friendEmail
      );
      setFriends(updatedFriends);
      setFilteredFriends(
        updatedFriends.filter((friend) =>
          friend.email
            .split("@")[0]
            .toLowerCase()
            .includes(searchTerm.toLowerCase())
        )
      );
    } catch (err) {
      alert(err.message);
    }
  };

  const handleSendFriendRequest = async (friendEmail) => {
    try {
      await sendFriendRequest({ userEmail: email, friendEmail });
      alert("Friend request sent successfully!");
    } catch (err) {
      alert("Error sending friend request: " + err.message);
    }
  };

  const handleClearFilter = () => {
    setTagFilter("");
    setSearchTerm("");
    setFilteredFriends(friends);
  };

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="friends-list">
      <img src={logo} alt="Logo" className="logo" />
      <h3>Friends List</h3>
      <SearchBar
        searchTerm={searchTerm}
        setSearchTerm={setSearchTerm}
        placeholder="Search users..."
      />
      <div className="filter-section">
        <select
          value={tagFilter}
          onChange={(e) => setTagFilter(e.target.value)}
        >
          <option value="">All Years</option>
          <option value="First Year">First Year</option>
          <option value="2nd Year">2nd Year</option>
          <option value="3rd Year">3rd Year</option>
          <option value="4th Year">4th Year</option>
          <option value="Alumni">Alumni</option>
        </select>
        <button onClick={handleClearFilter}>Clear Filter</button>
      </div>
      {noResults ? (
        <p>No such user exists</p>
      ) : (
        <ul>
          {filteredFriends.map((user) => (
            <li
              key={user.email}
              className={
                friends.some((friend) => friend.email === user.email)
                  ? ""
                  : "not-friend"
              }
            >
              {friends.some((friend) => friend.email === user.email) ? (
                <>
                  <Link to={`/user/${user.email}`} className="friend-link">
                    {user.email}
                  </Link>
                  <div className="friend-details">
                    <span>Status: {user.status}</span>
                    <button
                      onClick={() => handleDeleteFriend(email, user.email)}
                    >
                      Delete Friend
                    </button>
                  </div>
                </>
              ) : (
                <>
                  <span>{user.email}</span>
                  <button
                    onClick={() => handleSendFriendRequest(user.email)}
                    className="send-request"
                  >
                    Send Friend Request
                  </button>
                </>
              )}
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

export default FriendsList;
