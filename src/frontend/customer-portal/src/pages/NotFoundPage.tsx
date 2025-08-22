/**
 * @fileoverview 404 Not Found page for Lakeside Mutual Customer Portal
 * Displayed when users navigate to non-existent routes
 * @module NotFoundPage
 */

import { Home as HomeIcon, ArrowBack as BackIcon } from '@mui/icons-material';
import { Box, Typography, Button, Container, Stack } from '@mui/material';
import React from 'react';
import { useNavigate } from 'react-router-dom';

/**
 * 404 Not Found page component
 * Provides navigation options when users reach invalid routes
 *
 * @component
 * @returns {JSX.Element} 404 page with navigation options
 */
const NotFoundPage: React.FC = () => {
  const navigate = useNavigate();

  /**
   * Navigate back to the previous page
   */
  const handleGoBack = () => {
    navigate(-1);
  };

  /**
   * Navigate to the dashboard
   */
  const handleGoHome = () => {
    navigate('/dashboard');
  };

  return (
    <Container maxWidth="md">
      <Box
        sx={{
          minHeight: '60vh',
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          justifyContent: 'center',
          textAlign: 'center',
          py: 8,
        }}
      >
        {/* Error Code */}
        <Typography
          variant="h1"
          sx={{
            fontSize: { xs: '6rem', md: '8rem' },
            fontWeight: 'bold',
            color: 'primary.main',
            lineHeight: 1,
            mb: 2,
          }}
        >
          404
        </Typography>

        {/* Error Message */}
        <Typography variant="h4" component="h1" gutterBottom>
          Page Not Found
        </Typography>

        <Typography variant="body1" color="text.secondary" sx={{ mb: 4, maxWidth: 600 }}>
          We're sorry, but the page you're looking for doesn't exist. It may have been moved,
          deleted, or you entered the wrong URL.
        </Typography>

        {/* Action Buttons */}
        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
          <Button variant="contained" startIcon={<HomeIcon />} onClick={handleGoHome} size="large">
            Go to Dashboard
          </Button>

          <Button variant="outlined" startIcon={<BackIcon />} onClick={handleGoBack} size="large">
            Go Back
          </Button>
        </Stack>

        {/* Additional Help */}
        <Box sx={{ mt: 6 }}>
          <Typography variant="body2" color="text.secondary">
            Need help? Contact our support team at{' '}
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
      </Box>
    </Container>
  );
};

export default NotFoundPage;
