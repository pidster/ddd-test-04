/**
 * @fileoverview Main App component for Lakeside Mutual Customer Portal
 * Handles routing and layout for the customer-facing application
 * @module App
 */

import { Box, Container } from '@mui/material';
import React from 'react';
import { Routes, Route, Navigate } from 'react-router-dom';

import LoadingSpinner from './components/common/LoadingSpinner';
import Footer from './components/layout/Footer';
import Header from './components/layout/Header';
import { useAuth } from './hooks/useAuth';
import ClaimsPage from './pages/ClaimsPage';
import Dashboard from './pages/Dashboard';
import LoginPage from './pages/LoginPage';
import NotFoundPage from './pages/NotFoundPage';
import PoliciesPage from './pages/PoliciesPage';
import ProfilePage from './pages/ProfilePage';

/**
 * Main application component that provides routing and layout
 * Handles authentication state and renders appropriate content
 *
 * @component
 * @returns {JSX.Element} The main application layout with routing
 */
const App: React.FC = () => {
  const { isAuthenticated, isLoading } = useAuth();

  // Show loading spinner while checking authentication
  if (isLoading) {
    return <LoadingSpinner />;
  }

  // If user is not authenticated, show login page
  if (!isAuthenticated) {
    return <LoginPage />;
  }

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
      }}
    >
      <Header />

      <Container
        component="main"
        maxWidth="lg"
        sx={{
          flex: 1,
          py: 3,
        }}
      >
        <Routes>
          {/* Default redirect to dashboard */}
          <Route path="/" element={<Navigate to="/dashboard" replace />} />

          {/* Main application routes */}
          <Route path="/dashboard" element={<Dashboard />} />
          <Route path="/policies" element={<PoliciesPage />} />
          <Route path="/claims" element={<ClaimsPage />} />
          <Route path="/profile" element={<ProfilePage />} />

          {/* 404 page for unmatched routes */}
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
      </Container>

      <Footer />
    </Box>
  );
};

export default App;
