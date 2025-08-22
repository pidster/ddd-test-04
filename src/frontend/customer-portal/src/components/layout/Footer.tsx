/**
 * @fileoverview Footer component for Lakeside Mutual Customer Portal
 * Provides company information and helpful links
 * @module Footer
 */

import { Box, Typography, Container, Link, Grid, Divider } from '@mui/material';
import React from 'react';

/**
 * Footer component that provides company information and links
 * Displayed at the bottom of all pages in the customer portal
 *
 * @component
 * @returns {JSX.Element} Footer with company info and links
 */
const Footer: React.FC = () => {
  const currentYear = new Date().getFullYear();

  return (
    <Box
      component="footer"
      sx={{
        mt: 'auto',
        bgcolor: 'background.paper',
        borderTop: 1,
        borderColor: 'divider',
        py: 3,
      }}
    >
      <Container maxWidth="lg">
        <Grid container spacing={4}>
          {/* Company Info */}
          <Grid item xs={12} md={6}>
            <Typography variant="h6" gutterBottom>
              Lakeside Mutual
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Your trusted insurance partner since 1965. Protecting what matters most with
              comprehensive coverage and exceptional customer service.
            </Typography>
          </Grid>

          {/* Quick Links */}
          <Grid item xs={12} md={3}>
            <Typography variant="h6" gutterBottom>
              Quick Links
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Link href="#" variant="body2" color="text.secondary">
                Get a Quote
              </Link>
              <Link href="#" variant="body2" color="text.secondary">
                File a Claim
              </Link>
              <Link href="#" variant="body2" color="text.secondary">
                Find an Agent
              </Link>
              <Link href="#" variant="body2" color="text.secondary">
                Pay Bill
              </Link>
            </Box>
          </Grid>

          {/* Support */}
          <Grid item xs={12} md={3}>
            <Typography variant="h6" gutterBottom>
              Support
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1 }}>
              <Link href="#" variant="body2" color="text.secondary">
                Help Center
              </Link>
              <Link href="#" variant="body2" color="text.secondary">
                Contact Us
              </Link>
              <Link href="#" variant="body2" color="text.secondary">
                Privacy Policy
              </Link>
              <Link href="#" variant="body2" color="text.secondary">
                Terms of Service
              </Link>
            </Box>
          </Grid>
        </Grid>

        <Divider sx={{ my: 3 }} />

        {/* Copyright */}
        <Box
          sx={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            flexWrap: 'wrap',
            gap: 2,
          }}
        >
          <Typography variant="body2" color="text.secondary">
            © {currentYear} Lakeside Mutual Insurance Company. All rights reserved.
          </Typography>

          <Box sx={{ display: 'flex', gap: 2 }}>
            <Typography variant="body2" color="text.secondary">
              24/7 Claims Hotline: 1-800-CLAIMS
            </Typography>
          </Box>
        </Box>
      </Container>
    </Box>
  );
};

export default Footer;
