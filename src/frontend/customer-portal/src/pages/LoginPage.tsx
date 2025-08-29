/**
 * @fileoverview Login page for Lakeside Mutual Customer Portal
 * Handles customer authentication and initial access to the portal
 * @module LoginPage
 */

import { LockOutlined as LockIcon } from '@mui/icons-material';
import {
  Box,
  Paper,
  TextField,
  Button,
  Typography,
  Container,
  Alert,
  Link,
  Divider,
  Stack,
} from '@mui/material';
import React, { useState } from 'react';

import LoadingSpinner from '../components/common/LoadingSpinner';
import { useAuth } from '../hooks/useAuth';

/**
 * Form data interface for login credentials
 */
interface ILoginFormData {
  email: string;
  password: string;
}

/**
 * Login page component that handles customer authentication
 * Provides form for email/password login and links to registration/recovery
 *
 * @component
 * @returns {JSX.Element} Login page with authentication form
 */
const LoginPage: React.FC = () => {
  const { login, loading, error } = useAuth();
  const [formData, setFormData] = useState<LoginFormData>({
    email: '',
    password: '',
  });
  const [validationErrors, setValidationErrors] = useState<Partial<LoginFormData>>({});

  /**
   * Validates the login form data
   * @param data - Form data to validate
   * @returns Object containing validation errors
   */
  const validateForm = (data: LoginFormData): Partial<LoginFormData> => {
    const errors: Partial<LoginFormData> = {};

    if (!data.email) {
      errors.email = 'Email is required';
    } else if (!/\S+@\S+\.\S+/.test(data.email)) {
      errors.email = 'Please enter a valid email address';
    }

    if (!data.password) {
      errors.password = 'Password is required';
    } else if (data.password.length < 6) {
      errors.password = 'Password must be at least 6 characters';
    }

    return errors;
  };

  /**
   * Handles form input changes
   * @param field - The field being changed
   * @param value - The new value
   */
  const handleInputChange = (field: keyof LoginFormData, value: string) => {
    setFormData((prev) => ({ ...prev, [field]: value }));

    // Clear validation error for this field
    if (validationErrors[field]) {
      setValidationErrors((prev) => ({ ...prev, [field]: undefined }));
    }
  };

  /**
   * Handles form submission
   * @param event - Form submission event
   */
  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();

    const errors = validateForm(formData);
    if (Object.keys(errors).length > 0) {
      setValidationErrors(errors);
      return;
    }

    try {
      await login(formData.email, formData.password);
    } catch (err) {
      // Error is handled by the useAuth hook
      console.error('Login failed:', err);
    }
  };

  if (loading) {
    return <LoadingSpinner />;
  }

  return (
    <Container component="main" maxWidth="sm">
      <Box
        sx={{
          minHeight: '100vh',
          display: 'flex',
          flexDirection: 'column',
          justifyContent: 'center',
          py: 4,
        }}
      >
        <Paper
          elevation={3}
          sx={{
            p: 4,
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
          }}
        >
          {/* Logo and Header */}
          <Box
            sx={{
              mb: 3,
              display: 'flex',
              flexDirection: 'column',
              alignItems: 'center',
            }}
          >
            <Box
              sx={{
                width: 60,
                height: 60,
                borderRadius: '50%',
                backgroundColor: 'primary.main',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                mb: 2,
              }}
            >
              <LockIcon sx={{ color: 'white', fontSize: 30 }} />
            </Box>
            <Typography component="h1" variant="h4" align="center" gutterBottom>
              Lakeside Mutual
            </Typography>
            <Typography variant="h6" color="text.secondary" align="center">
              Customer Portal
            </Typography>
          </Box>

          {/* Error Alert */}
          {error && (
            <Alert severity="error" sx={{ width: '100%', mb: 2 }}>
              {error}
            </Alert>
          )}

          {/* Login Form */}
          <Box component="form" onSubmit={handleSubmit} sx={{ width: '100%' }}>
            <Stack spacing={3}>
              <TextField
                required
                fullWidth
                id="email"
                label="Email Address"
                name="email"
                autoComplete="email"
                autoFocus
                value={formData.email}
                onChange={(e) => handleInputChange('email', e.target.value)}
                error={!!validationErrors.email}
                helperText={validationErrors.email}
              />

              <TextField
                required
                fullWidth
                name="password"
                label="Password"
                type="password"
                id="password"
                autoComplete="current-password"
                value={formData.password}
                onChange={(e) => handleInputChange('password', e.target.value)}
                error={!!validationErrors.password}
                helperText={validationErrors.password}
              />

              <Button
                type="submit"
                fullWidth
                variant="contained"
                size="large"
                disabled={loading}
                sx={{ mt: 3, mb: 2 }}
              >
                {loading ? 'Signing In...' : 'Sign In'}
              </Button>
            </Stack>
          </Box>

          <Divider sx={{ width: '100%', my: 3 }} />

          {/* Additional Links */}
          <Stack spacing={2} alignItems="center">
            <Link href="#forgot-password" variant="body2">
              Forgot your password?
            </Link>
            <Typography variant="body2" color="text.secondary">
              Don't have an account? <Link href="#register">Contact us to get started</Link>
            </Typography>
          </Stack>

          {/* Demo Credentials */}
          <Box sx={{ mt: 3, p: 2, backgroundColor: 'grey.50', borderRadius: 1, width: '100%' }}>
            <Typography variant="caption" color="text.secondary" align="center" display="block">
              Demo Credentials
            </Typography>
            <Typography variant="body2" align="center">
              Email: demo@lakesidemutual.com
              <br />
              Password: demo123
            </Typography>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default LoginPage;
