/**
 * @fileoverview Error fallback component for Lakeside Mutual Customer Portal
 * Provides error boundary fallback UI when React components crash
 * @module ErrorFallback
 */

import { Error as ErrorIcon, Refresh as RefreshIcon, Home as HomeIcon } from '@mui/icons-material';
import { Box, Typography, Button, Paper, Container, Stack } from '@mui/material';
import React from 'react';
import { FallbackProps } from 'react-error-boundary';

/**
 * Error fallback component displayed when the application encounters an unhandled error
 * Provides options to retry the failed operation or navigate to safety
 *
 * @component
 * @param {FallbackProps} props - Error boundary fallback props
 * @returns {JSX.Element} Error fallback UI with recovery options
 */
const ErrorFallback: React.FC<FallbackProps> = ({ error, resetErrorBoundary }) => {
  /**
   * Navigate to home page and reset error boundary
   */
  const handleGoHome = () => {
    window.location.href = '/dashboard';
    resetErrorBoundary();
  };

  return (
    <Container maxWidth="md">
      <Box
        sx={{
          minHeight: '100vh',
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          py: 4,
        }}
      >
        <Paper
          elevation={3}
          sx={{
            p: 6,
            textAlign: 'center',
            maxWidth: 600,
          }}
        >
          {/* Error Icon */}
          <ErrorIcon
            sx={{
              fontSize: 80,
              color: 'error.main',
              mb: 3,
            }}
          />

          {/* Error Message */}
          <Typography variant="h4" component="h1" gutterBottom>
            Oops! Something went wrong
          </Typography>

          <Typography variant="body1" color="text.secondary" sx={{ mb: 4 }}>
            We're sorry, but something unexpected happened. Our team has been notified and is
            working to fix the issue.
          </Typography>

          {/* Error Details (in development) */}
          {process.env.NODE_ENV === 'development' && error && (
            <Box
              sx={{
                mb: 4,
                p: 2,
                backgroundColor: 'grey.100',
                borderRadius: 1,
                textAlign: 'left',
                overflow: 'auto',
                maxHeight: 200,
              }}
            >
              <Typography variant="caption" color="text.secondary">
                Error Details (Development Mode):
              </Typography>
              <Typography
                variant="body2"
                component="pre"
                sx={{
                  fontSize: '0.75rem',
                  fontFamily: 'monospace',
                  mt: 1,
                }}
              >
                {error.message}
                {error.stack && `\n\n${error.stack}`}
              </Typography>
            </Box>
          )}

          {/* Action Buttons */}
          <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} justifyContent="center">
            <Button
              variant="contained"
              startIcon={<RefreshIcon />}
              onClick={resetErrorBoundary}
              size="large"
            >
              Try Again
            </Button>

            <Button variant="outlined" startIcon={<HomeIcon />} onClick={handleGoHome} size="large">
              Go to Dashboard
            </Button>
          </Stack>

          {/* Support Information */}
          <Box sx={{ mt: 4, pt: 3, borderTop: 1, borderColor: 'divider' }}>
            <Typography variant="body2" color="text.secondary">
              If this problem persists, please contact our support team at{' '}
              <Button
                variant="text"
                size="small"
                href="mailto:support@lakesidemutual.com"
                sx={{ textTransform: 'none', p: 0, minWidth: 'auto' }}
              >
                support@lakesidemutual.com
              </Button>
            </Typography>
          </Box>
        </Paper>
      </Box>
    </Container>
  );
};

export default ErrorFallback;
