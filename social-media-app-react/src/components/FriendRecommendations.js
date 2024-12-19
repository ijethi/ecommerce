import React, { useEffect, useState } from "react";
import { fetchRecommendedFriends, sendFriendRequest } from "../api";
import { useUser } from "../contexts/UserContext";
import "./FriendRecommendation.css";

const FriendRecommendations = () => {
  const { user } = useUser();
  const [recommendedFriends, setRecommendedFriends] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchFriends = async () => {
      try {
        const friends = await fetchRecommendedFriends(user.email);
        setRecommendedFriends(friends);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchFriends();
  }, [user.email]);

  const handleAddFriend = async (friendEmail) => {
    try {
      await sendFriendRequest({ userEmail: user.email, friendEmail });
      alert("Friend request sent successfully!");
    } catch (err) {
      alert("Error sending friend request: " + err.message);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="friend-recommendations">
      <h3>Recommended Friends</h3>
      <p>Make new connections!</p>
      <ul>
        {recommendedFriends.map((friend) => (
          <li key={friend.id}>
            {friend.email}
            <button onClick={() => handleAddFriend(friend.email)}>
              Add Friend
            </button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default FriendRecommendations;
