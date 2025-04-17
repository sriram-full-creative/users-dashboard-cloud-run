import React, { useState, useCallback, useEffect } from 'react';
import './App.css';

// Custom JsonViewer component with syntax highlighting and collapsible sections
const JsonViewer = ({ data, darkMode }) => {
  const [expandedSections, setExpandedSections] = useState({});

  // Initialize all sections as expanded by default
  useEffect(() => {
    const initExpandedSections = (obj, parentPath = '') => {
      const expanded = {};

      if (obj && typeof obj === 'object') {
        // Set current path as expanded
        if (parentPath) {
          expanded[parentPath] = true;
        }

        // Recursively handle nested objects and arrays
        Object.keys(obj).forEach(key => {
          const currentPath = parentPath ? `${parentPath}.${key}` : key;
          const value = obj[key];

          if (value && typeof value === 'object') {
            // Mark this path as expanded
            expanded[currentPath] = true;

            // Merge with results from deeper levels
            Object.assign(expanded, initExpandedSections(value, currentPath));
          }
        });
      }

      return expanded;
    };

    setExpandedSections(initExpandedSections(data, 'root'));
  }, [data]);

  const formatValue = (value, key, path) => {
    if (value === null) return <span className="json-null">null</span>;
    if (typeof value === 'boolean') return <span className="json-boolean">{String(value)}</span>;
    if (typeof value === 'number') return <span className="json-number">{value}</span>;
    if (typeof value === 'string') return <span className="json-string">"{value}"</span>;

    // For objects and arrays
    const isArray = Array.isArray(value);
    const isEmpty = isArray ? value.length === 0 : Object.keys(value).length === 0;
    const currentPath = path ? `${path}.${key}` : key;
    const isExpanded = expandedSections[currentPath];

    // Get count info
    const count = isArray ? value.length : Object.keys(value).length;
    const countInfo = isArray ? `[${count}]` : `{${count}}`;

    const toggleExpand = () => {
      setExpandedSections(prev => ({ ...prev, [currentPath]: !prev[currentPath] }));
    };

    if (isEmpty) {
      return <span className="json-empty">{isArray ? '[]' : '{}'}</span>;
    }

    return (
      <div className="json-complex">
        <span
          className="json-toggler"
          onClick={toggleExpand}
        >
          {isExpanded ? '▼' : '►'} {isArray ? '[' : '{'}
          {!isExpanded && <span className="json-count">{countInfo}</span>}
        </span>

        {isExpanded && (
          <div className="json-children">
            {isArray ? (
              value.map((item, index) => (
                <div className="json-item" key={index}>
                  <span className="json-index">{index}:</span> {formatValue(item, index, currentPath)}
                </div>
              ))
            ) : (
              Object.entries(value).map(([k, v]) => (
                <div className="json-item" key={k}>
                  <span className="json-key">"{k}":</span> {formatValue(v, k, currentPath)}
                </div>
              ))
            )}
          </div>
        )}

        <span>{isArray ? ']' : '}'}</span>
      </div>
    );
  };

  return (
    <div className={`json-viewer ${darkMode ? 'dark-mode' : ''}`}>
      {formatValue(data, 'root', '')}
    </div>
  );
};

function App() {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [cache, setCache] = useState({});
  const [darkMode, setDarkMode] = useState(true); // Set dark mode as default
  const [isFirstFetch, setIsFirstFetch] = useState(true); // Track if it's first fetch

  // Replace with your actual API endpoint
  const API_URL = 'http://sriram-spring-boot-backend-401553303388.us-central1.run.app/api/v1/users';

  const fetchData = useCallback(async (forceRefresh = false) => {
    // Check if we have cached data that's less than 60 seconds old
    const now = Date.now();
    // Only use cache if it's not a force refresh and the cache is recent
    if (!forceRefresh && cache.data && now - cache.timestamp < 60000) {
      setData(cache.data);
      setIsFirstFetch(false); // No longer first fetch after getting data
      return;
    }

    try {
      setLoading(true);
      setError(null);

      // Create an AbortController for timeout handling
      const controller = new AbortController();
      const timeoutId = setTimeout(() => controller.abort(), 10000); // 10-second timeout

      // Add timestamp as query parameter for cache busting instead of using headers
      const url = forceRefresh
        ? `${API_URL}${API_URL.includes('?') ? '&' : '?'}_t=${Date.now()}`
        : API_URL;

      const response = await fetch(url, {
        signal: controller.signal
        // Removed the problematic headers that were causing CORS issues
      });

      clearTimeout(timeoutId);

      if (!response.ok) {
        if (response.status === 404) {
          throw new Error('Resource not found. The API endpoint may be incorrect.');
        } else if (response.status >= 500) {
          throw new Error('Server error. Please try again later.');
        } else {
          throw new Error(`HTTP error! Status: ${response.status}`);
        }
      }

      const result = await response.json();
      setData(result);
      setIsFirstFetch(false); // No longer first fetch after getting data

      // Cache the result
      setCache({ data: result, timestamp: Date.now() });
    } catch (err) {
      if (err.name === 'AbortError') {
        setError('Request timed out. Please try again.');
      } else if (err.message.includes('fetch')) {
        setError('Network error. Please check your connection.');
      } else {
        setError(`Error: ${err.message}`);
      }
      setData(null);
    } finally {
      setLoading(false);
    }
  }, [cache, API_URL]);

  // Apply dark mode to body element on component mount and when darkMode changes
  useEffect(() => {
    if (darkMode) {
      document.body.classList.add('dark-mode');
    } else {
      document.body.classList.remove('dark-mode');
    }
  }, [darkMode]);

  const toggleDarkMode = () => {
    setDarkMode(prev => !prev);
  };

  return (
    <div className={`app-container ${darkMode ? 'dark-mode' : ''}`}>
      <header>
        <div className="header-content">
          <h1>Users Dashboard</h1>
          <div className="theme-toggle">
            <label className="switch">
              <input
                type="checkbox"
                checked={darkMode}
                onChange={toggleDarkMode}
              />
              <span className="slider round"></span>
            </label>
            <span className="toggle-label">{darkMode ? 'Dark Mode' : 'Light Mode'}</span>
          </div>
        </div>
      </header>

      <main>
        <div className="action-panel">
          <button
            className="fetch-button"
            onClick={() => fetchData(isFirstFetch ? false : true)}
            disabled={loading}
          >
            {loading ? 'Loading...' : isFirstFetch ? 'View Users List' : 'Refresh List'}
          </button>
        </div>

        {loading && <p className="loading">Fetching data...</p>}

        {error && (
          <div className="error-container">
            <h2>Error</h2>
            <p>{error}</p>
          </div>
        )}

        {data && !loading && (
          <div className="data-container">
            <JsonViewer data={data} darkMode={darkMode} />
          </div>
        )}
      </main>
    </div>
  );
}

export default App;