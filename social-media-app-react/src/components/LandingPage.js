import React, { useState, useEffect } from "react";
import { fetchUserProfile, fetchPosts, addPost, fetchFriendsList } from "../api";
import FriendRecommendations from "./FriendRecommendations";
import { useUser } from "../contexts/UserContext";
import "./LandingPage.css";
import logo from "../assets/logo.png";


const LandingPage = () => {
  const { user } = useUser();
  const [userEmail, setUserEmail] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [posts, setPosts] = useState([]);
  const [newPost, setNewPost] = useState("");
  const [friends, setFriends] = useState([]);

  useEffect(() => {
    const fetchProfileAndData = async () => {
      try {
        const profile = await fetchUserProfile(user.email);
        setUserEmail(profile.email);
        const [postList, friendsList] = await Promise.all([
          fetchPosts(user.email),
          fetchFriendsList(user.email)
        ]);
        setPosts(postList);
        setFriends(friendsList);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchProfileAndData();
  }, [user.email]);

  const handleAddPost = async () => {
    try {
      await addPost({ content: newPost, email: userEmail });
      setPosts([...posts, { content: newPost, email: userEmail }]);
      setNewPost("");
    } catch (err) {
      setError(err.message);
    }
  };

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="landing-page">
      <div className="main-content">
        <img src={logo} alt="Logo" className="logo" />
        <h2 className="welcome-text">Welcome to the Dalhousie Social Media Platform!</h2>
        <p className="welcome-text">Use this platform to connect with other students, create posts, and share your interests!</p>
          <div className="post-section">
            <h3>Friends' Posts:</h3>
            <ul>
              {posts
                .filter((post) => friends.some((friend) => friend.email === post.email) || post.email === userEmail)
                .map((post, index) => (
                  <li key={index}>
                    <strong>{post.email}</strong>: {post.content}
                  </li>
                ))}
            </ul>
            <div className="add-post">
              <textarea
                value={newPost}
                onChange={(e) => setNewPost(e.target.value)}
                placeholder="Write a new post..."
              ></textarea>
              <button onClick={handleAddPost}>Add Post</button>
          </div>
        </div>
      </div>
      <div className="sidebar">
        <FriendRecommendations email={userEmail} />
      </div>
    </div>
  );
};

export default LandingPage;
