import axios from "axios";

const BASE_URL = "http://localhost:8080/api/users";

export const fetchUserProfile = async (email) => {
  try {
    const response = await axios.get(`${BASE_URL}/profile/${email}`);
    return response.data;
  } catch (error) {
    throw new Error(
      error.response.data.message || "Error fetching user profile"
    );
  }
};

export const updateStatus = async (email, newStatus) => {
  try {
    await axios.put(`${BASE_URL}/profile/status/${email}`, newStatus, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  } catch (error) {
    throw new Error(error.response.data.message || "Error updating status");
  }
};

export const updateInterests = async (email, newInterests) => {
  try {
    await axios.put(`${BASE_URL}/profile/interests/${email}`, newInterests, {
      headers: {
        "Content-Type": "application/json",
      },
    });
  } catch (error) {
    throw new Error(error.response.data.message || "Error updating interests");
  }
};

export const updateTag = async (email, tag) => {
  try {
    const response = await axios.put(`${BASE_URL}/profile/tag/${email}`, {
      tag,
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.message || "Error updating tag");
  }
};

export const createUser = async (userData) => {
  try {
    const response = await axios.post(`${BASE_URL}/signup`, userData, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error creating user profile:", error);
    throw error;
  }
};

export const signInUser = async (credentials) => {
  try {
    const response = await axios.post(`${BASE_URL}/login`, credentials, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error signing in:", error);
    throw error;
  }
};

export const fetchRecommendedFriends = async (email) => {
  try {
    const response = await axios.get(`${BASE_URL}/recommendations/friends`, {
      params: { email },
    });
    return response.data;
  } catch (error) {
    console.error("Error fetching recommended friends:", error);
    throw new Error(
      error.response.data.message || "Error fetching recommended friends"
    );
  }
};

export const resetPassword = async (formData) => {
  try {
    const response = await axios.post(`${BASE_URL}/reset-password`, formData, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error resetting password:", error);
    throw new Error(error.response.data.message || "Error resetting password");
  }
};

export const sendFriendRequest = async (friendRequest) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/send-friend-request`,
      friendRequest,
      {
        headers: {
          "Content-Type": "application/json",
        },
      }
    );
    return response.data;
  } catch (error) {
    console.error("Error sending friend request:", error);
    throw new Error("Failed to send friend request");
  }
};

export const fetchFriendRequests = async (email) => {
  try {
    const response = await axios.get(`${BASE_URL}/friend-requests/${email}`);
    return response.data;
  } catch (error) {
    throw new Error(
      error.response.data.message || "Error fetching friend requests"
    );
  }
};

export const fetchPosts = async (email) => {
  try {
    const response = await axios.get(`${BASE_URL}/posts`, {
      params: { email },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.message || "Error fetching posts");
  }
};

export const addPost = async (post) => {
  try {
    const response = await axios.post(`${BASE_URL}/posts`, post);
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.message || "Error adding post");
  }
};

export const acceptFriendRequest = async (requestId) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/accept-friend-request/${requestId}`
    );
    return response.data;
  } catch (error) {
    throw new Error(
      error.response.data.message || "Error accepting friend request"
    );
  }
};

export const fetchFriendsList = async (email) => {
  try {
    const response = await axios.get(`${BASE_URL}/friends/${email}`);
    return response.data;
  } catch (error) {
    throw new Error(
      error.response.data.message || "Error fetching friends list"
    );
  }
};

export const deleteFriend = async (email, friendEmail) => {
  try {
    const response = await axios.delete(
      `${BASE_URL}/delete-friend/${email}/${friendEmail}`
    );
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.message || "Error deleting friend");
  }
};
export const adminSignUp = async (adminData) => {
  try {
    const response = await axios.post(`${BASE_URL}/admin-signup`, adminData, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.message || "Error signing up as admin");
  }
};
export const fetchAllUsers = async () => {
  try {
    const response = await axios.get(`${BASE_URL}/all`);
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.message || "Error fetching users list");
  }
};
export const removeUser = async (user) => {
  try {
    const response = await axios.delete(`${BASE_URL}/remove`, {
      headers: {
        "Content-Type": "application/json",
      },
      data: user,
    });
    return response;
  } catch (error) {
    throw new Error(error.response?.data?.message || "Error removing user");
  }
};

export const createUserByAdmin = async (userData) => {
  try {
    const response = await axios.post(`${BASE_URL}/add-user`, userData, {
      headers: {
        "Content-Type": "application/json",
      },
    });
    if (response.status === 201) {
      return response.data;
    } else {
      throw new Error("Unexpected response status");
    }
  } catch (error) {
    throw new Error(error.response?.data?.message || "Error creating user");
  }
};

export const searchUsers = async (query) => {
  try {
    const response = await axios.get(`${BASE_URL}/search`, {
      params: { query },
    });
    return response.data;
  } catch (error) {
    console.error("Error searching users:", error);
    throw new Error(error.response.data.message || "Error searching users");
  }
};
export const activateUser = async (email) => {
  try {
    const response = await axios.put(`${BASE_URL}/activate/${email}`);
    return response.data;
  } catch (error) {
    throw new Error(error.response.data.message || "Error activating user");
  }
};
export const rejectFriendRequest = async (requestId) => {
  try {
    const response = await axios.post(
      `${BASE_URL}/reject-friend-request/${requestId}`
    );
    return response.data;
  } catch (error) {
    throw new Error(
      error.response.data.message || "Error rejecting friend request"
    );
  }
};
export const rejectUser = async (email) => {
  try {
    await axios.delete(`${BASE_URL}/reject/${email}`);
  } catch (error) {
    throw new Error(error.response.data.message || "Error rejecting user");
  }
};
