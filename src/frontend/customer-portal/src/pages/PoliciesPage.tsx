/**
 * @fileoverview Policies page for Lakeside Mutual Customer Portal
 * Displays and manages customer's insurance policies
 * @module PoliciesPage
 */

import { Policy as PolicyIcon, Add as AddIcon, Visibility as ViewIcon } from '@mui/icons-material';
import {
  Box,
  Typography,
  Paper,
  Grid,
  Card,
  CardContent,
  Chip,
  Button,
  Stack,
} from '@mui/material';
import React from 'react';

/**
 * Policies page component that displays customer's insurance policies
 * Shows policy list with status, coverage details, and management actions
 *
 * @component
 * @returns {JSX.Element} Policies page with policy list and management options
 */
const PoliciesPage: React.FC = () => {
  // Mock data - replace with actual data from hooks
  const mockPolicies = [
    {
      id: 'POL-001',
      type: 'Auto Insurance',
      status: 'ACTIVE',
      premium: 1200,
      coverage: '$250,000',
      renewalDate: '2024-12-15',
    },
    {
      id: 'POL-002',
      type: 'Home Insurance',
      status: 'ACTIVE',
      premium: 800,
      coverage: '$500,000',
      renewalDate: '2024-08-30',
    },
    {
      id: 'POL-003',
      type: 'Life Insurance',
      status: 'PENDING',
      premium: 600,
      coverage: '$1,000,000',
      renewalDate: '2025-01-10',
    },
  ];

  /**
   * Gets the appropriate color for policy status chip
   * @param status - Policy status
   * @returns Chip color
   */
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'ACTIVE':
        return 'success';
      case 'PENDING':
        return 'warning';
      case 'EXPIRED':
        return 'error';
      default:
        return 'default';
    }
  };

  return (
    <Box>
      {/* Page Header */}
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" component="h1" gutterBottom>
            My Policies
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            Manage your insurance policies and coverage
          </Typography>
        </Box>
        <Button variant="contained" startIcon={<AddIcon />} size="large">
          Get Quote
        </Button>
      </Box>

      {/* Policies Grid */}
      <Grid container spacing={3}>
        {mockPolicies.map((policy) => (
          <Grid item xs={12} md={6} lg={4} key={policy.id}>
            <Card sx={{ height: '100%' }}>
              <CardContent>
                <Stack spacing={2}>
                  {/* Policy Header */}
                  <Box
                    sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}
                  >
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <PolicyIcon color="primary" />
                      <Typography variant="h6">{policy.type}</Typography>
                    </Box>
                    <Chip
                      label={policy.status}
                      color={getStatusColor(policy.status) as any}
                      size="small"
                    />
                  </Box>

                  {/* Policy Details */}
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Policy Number
                    </Typography>
                    <Typography variant="body1" fontWeight="medium">
                      {policy.id}
                    </Typography>
                  </Box>

                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Coverage Amount
                    </Typography>
                    <Typography variant="h6" color="primary">
                      {policy.coverage}
                    </Typography>
                  </Box>

                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Annual Premium
                    </Typography>
                    <Typography variant="body1" fontWeight="medium">
                      ${policy.premium.toLocaleString()}
                    </Typography>
                  </Box>

                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      Renewal Date
                    </Typography>
                    <Typography variant="body1">
                      {new Date(policy.renewalDate).toLocaleDateString()}
                    </Typography>
                  </Box>

                  {/* Actions */}
                  <Stack direction="row" spacing={1} sx={{ mt: 2 }}>
                    <Button variant="outlined" startIcon={<ViewIcon />} size="small" fullWidth>
                      View Details
                    </Button>
                  </Stack>
                </Stack>
              </CardContent>
            </Card>
          </Grid>
        ))}
      </Grid>

      {/* Empty State */}
      {mockPolicies.length === 0 && (
        <Paper sx={{ p: 6, textAlign: 'center' }}>
          <PolicyIcon sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" gutterBottom>
            No Policies Found
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
            You don't have any insurance policies yet. Get started with a quote today!
          </Typography>
          <Button variant="contained" startIcon={<AddIcon />}>
            Get Your First Quote
          </Button>
        </Paper>
      )}
    </Box>
  );
};

export default PoliciesPage;
