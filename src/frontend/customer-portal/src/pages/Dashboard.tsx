/**
 * @fileoverview Dashboard page for Lakeside Mutual Customer Portal
 * Provides an overview of customer's policies, claims, and important information
 * @module Dashboard
 */

import {
  Policy as PolicyIcon,
  Assignment as ClaimsIcon,
  AccountBalance as AccountIcon,
  Notifications as NotificationsIcon,
  TrendingUp as TrendingUpIcon,
} from '@mui/icons-material';
import {
  Box,
  Grid,
  Paper,
  Typography,
  Card,
  CardContent,
  Button,
  Chip,
  Stack,
} from '@mui/material';
import React from 'react';
import { useNavigate } from 'react-router-dom';

import ErrorAlert from '../components/common/ErrorAlert';
import LoadingSpinner from '../components/common/LoadingSpinner';
import { useClaims } from '../hooks/useClaims';
import { usePolicies } from '../hooks/usePolicies';

/**
 * Dashboard component that displays an overview of customer's insurance information
 * Shows summary cards for policies, claims, notifications, and quick actions
 *
 * @component
 * @returns {JSX.Element} Dashboard page with overview cards and quick actions
 */
const Dashboard: React.FC = () => {
  const navigate = useNavigate();
  const { policies, loading: policiesLoading, error: policiesError } = usePolicies();
  const { claims, loading: claimsLoading, error: claimsError } = useClaims();

  const loading = policiesLoading || claimsLoading;
  const error = policiesError || claimsError;

  if (loading) {
    return <LoadingSpinner />;
  }

  if (error) {
    return <ErrorAlert message="Failed to load dashboard data" error={error} />;
  }

  // Calculate summary statistics
  const activePolicies = policies?.filter((policy) => policy.status === 'ACTIVE').length || 0;
  const pendingClaims = claims?.filter((claim) => claim.status === 'PENDING').length || 0;
  const totalPremium = policies?.reduce((sum, policy) => sum + policy.premium, 0) || 0;

  return (
    <Box>
      {/* Page Header */}
      <Typography variant="h4" component="h1" gutterBottom>
        Dashboard
      </Typography>
      <Typography variant="subtitle1" color="text.secondary" sx={{ mb: 4 }}>
        Welcome back! Here's an overview of your insurance portfolio.
      </Typography>

      {/* Summary Cards */}
      <Grid container spacing={3} sx={{ mb: 4 }}>
        {/* Active Policies Card */}
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={2}>
                <PolicyIcon color="primary" sx={{ fontSize: 40 }} />
                <Box>
                  <Typography variant="h4" component="div">
                    {activePolicies}
                  </Typography>
                  <Typography color="text.secondary">Active Policies</Typography>
                </Box>
              </Stack>
            </CardContent>
          </Card>
        </Grid>

        {/* Pending Claims Card */}
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={2}>
                <ClaimsIcon color="warning" sx={{ fontSize: 40 }} />
                <Box>
                  <Typography variant="h4" component="div">
                    {pendingClaims}
                  </Typography>
                  <Typography color="text.secondary">Pending Claims</Typography>
                </Box>
              </Stack>
            </CardContent>
          </Card>
        </Grid>

        {/* Total Premium Card */}
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={2}>
                <AccountIcon color="success" sx={{ fontSize: 40 }} />
                <Box>
                  <Typography variant="h4" component="div">
                    ${totalPremium.toLocaleString()}
                  </Typography>
                  <Typography color="text.secondary">Annual Premium</Typography>
                </Box>
              </Stack>
            </CardContent>
          </Card>
        </Grid>

        {/* Notifications Card */}
        <Grid item xs={12} sm={6} md={3}>
          <Card>
            <CardContent>
              <Stack direction="row" alignItems="center" spacing={2}>
                <NotificationsIcon color="info" sx={{ fontSize: 40 }} />
                <Box>
                  <Typography variant="h4" component="div">
                    3
                  </Typography>
                  <Typography color="text.secondary">Notifications</Typography>
                </Box>
              </Stack>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      <Grid container spacing={3}>
        {/* Quick Actions */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Quick Actions
            </Typography>
            <Stack spacing={2}>
              <Button
                variant="contained"
                startIcon={<PolicyIcon />}
                onClick={() => navigate('/policies')}
                fullWidth
              >
                View All Policies
              </Button>
              <Button
                variant="outlined"
                startIcon={<ClaimsIcon />}
                onClick={() => navigate('/claims')}
                fullWidth
              >
                Submit New Claim
              </Button>
              <Button
                variant="outlined"
                startIcon={<TrendingUpIcon />}
                onClick={() => navigate('/policies')}
                fullWidth
              >
                Get Policy Quote
              </Button>
            </Stack>
          </Paper>
        </Grid>

        {/* Recent Activity */}
        <Grid item xs={12} md={6}>
          <Paper sx={{ p: 3 }}>
            <Typography variant="h6" gutterBottom>
              Recent Activity
            </Typography>
            <Stack spacing={2}>
              <Box>
                <Typography variant="body2" color="text.secondary">
                  2 days ago
                </Typography>
                <Typography variant="body1">Policy renewal reminder sent</Typography>
                <Chip label="Auto Insurance" size="small" />
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary">
                  1 week ago
                </Typography>
                <Typography variant="body1">Claim approved and processed</Typography>
                <Chip label="Home Insurance" size="small" color="success" />
              </Box>
              <Box>
                <Typography variant="body2" color="text.secondary">
                  2 weeks ago
                </Typography>
                <Typography variant="body1">Premium payment received</Typography>
                <Chip label="Life Insurance" size="small" color="info" />
              </Box>
            </Stack>
          </Paper>
        </Grid>
      </Grid>
    </Box>
  );
};

export default Dashboard;
