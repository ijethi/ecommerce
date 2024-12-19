import React, { useEffect, useState } from "react";
import { fetchGroupsList } from "../api";
import SearchBar from "./SearchBar";
import "./GroupsList.css";

const GroupsList = () => {
  const [groups, setGroups] = useState([]);
  const [filteredGroups, setFilteredGroups] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    const fetchGroups = async () => {
      try {
        const groupsList = await fetchGroupsList();
        setGroups(groupsList);
        setFilteredGroups(groupsList);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchGroups();
  }, []);

  useEffect(() => {
    setFilteredGroups(
      groups.filter((group) =>
        group.name.toLowerCase().includes(searchTerm.toLowerCase())
      )
    );
  }, [searchTerm, groups]);

  if (loading) return <div className="loading">Loading...</div>;
  if (error) return <div className="error">Error: {error}</div>;

  return (
    <div className="groups-list">
      <h3>Groups List</h3>
      <SearchBar
        searchTerm={searchTerm}
        setSearchTerm={setSearchTerm}
        placeholder="Search groups..."
      />
      <ul>
        {filteredGroups.map((group) => (
          <li key={group.id}>
            {group.name} - {group.description}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default GroupsList;
