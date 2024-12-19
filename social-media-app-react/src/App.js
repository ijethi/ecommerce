import React from "react";
import { BrowserRouter as Router, Route, Routes } from "react-router-dom";
import SignIn from "./components/SignIn";
import SignUp from "./components/SignUp";
import ForgotPassword from "./components/ForgotPassword";
import UserProfile from "./components/UserProfile";
import FriendRequests from "./components/FriendRequests";
import FriendsList from "./components/FriendList";
import LandingPage from "./components/LandingPage";
import NavBar from "./components/Navbar";
import { UserProvider, useUser } from "./contexts/UserContext";
import "./App.css";
import AdminSignUp from "./components/AdminSignUp";
import AdminPage from "./components/AdminPage";
import FriendProfile from "./components/FriendProfile";

function App() {
  return (
    <UserProvider>
      <Router>
        <div className="app">
          <NavBar />
          <main>
            <Routes>
              <Route path="/" element={<SignIn />} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/admin-signup" element={<AdminSignUp />} />
              <Route path="/forgot-password" element={<ForgotPassword />} />
              <Route
                path="/home"
                element={<PrivateRoute component={LandingPage} />}
              />
              <Route
                path="/profile"
                element={<PrivateRoute component={UserProfile} />}
              />
              <Route
                path="/friend-requests"
                element={<PrivateRoute component={FriendRequests} />}
              />
              <Route
                path="/friends"
                element={<PrivateRoute component={FriendsList} />}
              />
              <Route
                path="/admin"
                element={<PrivateRoute component={AdminPage} />}
              />
              <Route path="/user/:email" element={<PrivateRoute component={FriendProfile} />} />
            </Routes>
          </main>
        </div>
      </Router>
    </UserProvider>
  );
}

const PrivateRoute = ({ component: Component }) => {
  const { user } = useUser();
  return user ? <Component email={user.email} /> : <SignIn />;
};

export default App;
