import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { fetchUserProfile } from '../api';
import "./FriendProfile.css";
import logo from "../assets/logo.png";


const FriendProfile = () => {
  const { email } = useParams();
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const userDetails = await fetchUserProfile(email);
        setUser(userDetails);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, [email]);

  if (loading) return <div>Loading...</div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className='friend-profile'>
      <img src={logo} alt="Logo" className="logo" />
      <h2>{user.email}'s Profile</h2>
      <p>Email: {user.email}</p>
      <p>Status: {user.status}</p>
      <p>Interests: {user.interests}</p>
      <p>Tag: {user.tag}</p>
    </div>
  );
};

export default FriendProfile;

