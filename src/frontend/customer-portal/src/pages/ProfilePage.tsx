/**
 * @fileoverview Profile page for Lakeside Mutual Customer Portal
 * Allows customers to view and edit their personal information
 * @module ProfilePage
 */

import { Person as PersonIcon, Edit as EditIcon, Save as SaveIcon } from '@mui/icons-material';
import {
  Box,
  Typography,
  Paper,
  Grid,
  TextField,
  Button,
  Avatar,
  Stack,
  Divider,
} from '@mui/material';
import React from 'react';

/**
 * Profile page component that displays and allows editing of customer information
 * Manages personal details, contact information, and account preferences
 *
 * @component
 * @returns {JSX.Element} Profile page with customer information form
 */
const ProfilePage: React.FC = () => {
  // Mock data - replace with actual customer data from hooks
  const mockCustomer = {
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
    phone: '+1 (555) 123-4567',
    dateOfBirth: '1985-06-15',
    address: {
      street: '123 Main Street',
      city: 'Springfield',
      state: 'IL',
      zipCode: '62701',
      country: 'United States',
    },
    memberSince: '2020-03-15',
  };

  return (
    <Box>
      {/* Page Header */}
      <Typography variant="h4" component="h1" gutterBottom>
        My Profile
      </Typography>
      <Typography variant="subtitle1" color="text.secondary" sx={{ mb: 4 }}>
        Manage your personal information and account settings
      </Typography>

      <Grid container spacing={3}>
        {/* Profile Overview Card */}
        <Grid item xs={12} md={4}>
          <Paper sx={{ p: 3, textAlign: 'center' }}>
            <Avatar
              sx={{
                width: 100,
                height: 100,
                mx: 'auto',
                mb: 2,
                bgcolor: 'primary.main',
                fontSize: '2rem',
              }}
            >
              {mockCustomer.firstName[0]}
              {mockCustomer.lastName[0]}
            </Avatar>

            <Typography variant="h5" gutterBottom>
              {mockCustomer.firstName} {mockCustomer.lastName}
            </Typography>

            <Typography variant="body2" color="text.secondary" gutterBottom>
              {mockCustomer.email}
            </Typography>

            <Typography variant="body2" color="text.secondary">
              Member since {new Date(mockCustomer.memberSince).toLocaleDateString()}
            </Typography>

            <Button variant="outlined" startIcon={<EditIcon />} sx={{ mt: 2 }} fullWidth>
              Edit Profile Picture
            </Button>
          </Paper>
        </Grid>

        {/* Profile Information Form */}
        <Grid item xs={12} md={8}>
          <Paper sx={{ p: 3 }}>
            <Stack spacing={3}>
              {/* Personal Information Section */}
              <Box>
                <Typography variant="h6" gutterBottom>
                  Personal Information
                </Typography>
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="First Name"
                      value={mockCustomer.firstName}
                      variant="outlined"
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Last Name"
                      value={mockCustomer.lastName}
                      variant="outlined"
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Email Address"
                      value={mockCustomer.email}
                      variant="outlined"
                      type="email"
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Phone Number"
                      value={mockCustomer.phone}
                      variant="outlined"
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Date of Birth"
                      value={mockCustomer.dateOfBirth}
                      variant="outlined"
                      type="date"
                      InputLabelProps={{ shrink: true }}
                    />
                  </Grid>
                </Grid>
              </Box>

              <Divider />

              {/* Address Information Section */}
              <Box>
                <Typography variant="h6" gutterBottom>
                  Address Information
                </Typography>
                <Grid container spacing={2}>
                  <Grid item xs={12}>
                    <TextField
                      fullWidth
                      label="Street Address"
                      value={mockCustomer.address.street}
                      variant="outlined"
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="City"
                      value={mockCustomer.address.city}
                      variant="outlined"
                    />
                  </Grid>
                  <Grid item xs={12} sm={3}>
                    <TextField
                      fullWidth
                      label="State"
                      value={mockCustomer.address.state}
                      variant="outlined"
                    />
                  </Grid>
                  <Grid item xs={12} sm={3}>
                    <TextField
                      fullWidth
                      label="ZIP Code"
                      value={mockCustomer.address.zipCode}
                      variant="outlined"
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      fullWidth
                      label="Country"
                      value={mockCustomer.address.country}
                      variant="outlined"
                    />
                  </Grid>
                </Grid>
              </Box>

              <Divider />

              {/* Action Buttons */}
              <Stack direction="row" spacing={2} justifyContent="flex-end">
                <Button variant="outlined" size="large">
                  Cancel
                </Button>
                <Button variant="contained" startIcon={<SaveIcon />} size="large">
                  Save Changes
                </Button>
              </Stack>
            </Stack>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default ProfilePage;
