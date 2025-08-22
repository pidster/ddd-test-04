/**
 * @fileoverview Claims page for Lakeside Mutual Customer Portal
 * Displays customer's insurance claims and allows new claim submission
 * @module ClaimsPage
 */

import {
  Assignment as ClaimsIcon,
  Add as AddIcon,
  Visibility as ViewIcon,
  AttachMoney as MoneyIcon,
} from '@mui/icons-material';
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
  LinearProgress,
} from '@mui/material';
import React from 'react';

/**
 * Claims page component that displays customer's insurance claims
 * Shows claim history, status tracking, and new claim submission
 *
 * @component
 * @returns {JSX.Element} Claims page with claim list and submission options
 */
const ClaimsPage: React.FC = () => {
  // Mock data - replace with actual data from hooks
  const mockClaims = [
    {
      id: 'CLM-001',
      type: 'Auto Accident',
      status: 'APPROVED',
      amount: 2500,
      submittedDate: '2024-07-15',
      policyId: 'POL-001',
      progress: 100,
    },
    {
      id: 'CLM-002',
      type: 'Water Damage',
      status: 'UNDER_REVIEW',
      amount: 5000,
      submittedDate: '2024-08-01',
      policyId: 'POL-002',
      progress: 60,
    },
    {
      id: 'CLM-003',
      type: 'Theft',
      status: 'PENDING',
      amount: 1200,
      submittedDate: '2024-08-20',
      policyId: 'POL-002',
      progress: 25,
    },
  ];

  /**
   * Gets the appropriate color for claim status chip
   * @param status - Claim status
   * @returns Chip color
   */
  const getStatusColor = (status: string) => {
    switch (status) {
      case 'APPROVED':
        return 'success';
      case 'UNDER_REVIEW':
        return 'info';
      case 'PENDING':
        return 'warning';
      case 'DENIED':
        return 'error';
      default:
        return 'default';
    }
  };

  /**
   * Gets human-readable status text
   * @param status - Claim status
   * @returns Formatted status text
   */
  const getStatusText = (status: string) => {
    switch (status) {
      case 'UNDER_REVIEW':
        return 'Under Review';
      case 'APPROVED':
        return 'Approved';
      case 'PENDING':
        return 'Pending';
      case 'DENIED':
        return 'Denied';
      default:
        return status;
    }
  };

  return (
    <Box>
      {/* Page Header */}
      <Box sx={{ mb: 4, display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Box>
          <Typography variant="h4" component="h1" gutterBottom>
            My Claims
          </Typography>
          <Typography variant="subtitle1" color="text.secondary">
            Track your insurance claims and submit new ones
          </Typography>
        </Box>
        <Button variant="contained" startIcon={<AddIcon />} size="large">
          Submit New Claim
        </Button>
      </Box>

      {/* Claims Grid */}
      <Grid container spacing={3}>
        {mockClaims.map((claim) => (
          <Grid item xs={12} lg={6} key={claim.id}>
            <Card sx={{ height: '100%' }}>
              <CardContent>
                <Stack spacing={3}>
                  {/* Claim Header */}
                  <Box
                    sx={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between' }}
                  >
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                      <ClaimsIcon color="primary" />
                      <Typography variant="h6">{claim.type}</Typography>
                    </Box>
                    <Chip
                      label={getStatusText(claim.status)}
                      color={getStatusColor(claim.status) as any}
                      size="small"
                    />
                  </Box>

                  {/* Progress Bar */}
                  <Box>
                    <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                      <Typography variant="body2" color="text.secondary">
                        Progress
                      </Typography>
                      <Typography variant="body2" color="text.secondary">
                        {claim.progress}%
                      </Typography>
                    </Box>
                    <LinearProgress
                      variant="determinate"
                      value={claim.progress}
                      sx={{ height: 8, borderRadius: 4 }}
                    />
                  </Box>

                  {/* Claim Details Grid */}
                  <Grid container spacing={2}>
                    <Grid item xs={6}>
                      <Typography variant="body2" color="text.secondary">
                        Claim Number
                      </Typography>
                      <Typography variant="body1" fontWeight="medium">
                        {claim.id}
                      </Typography>
                    </Grid>

                    <Grid item xs={6}>
                      <Typography variant="body2" color="text.secondary">
                        Policy
                      </Typography>
                      <Typography variant="body1" fontWeight="medium">
                        {claim.policyId}
                      </Typography>
                    </Grid>

                    <Grid item xs={6}>
                      <Typography variant="body2" color="text.secondary">
                        Claim Amount
                      </Typography>
                      <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                        <MoneyIcon fontSize="small" color="success" />
                        <Typography variant="h6" color="success.main">
                          ${claim.amount.toLocaleString()}
                        </Typography>
                      </Box>
                    </Grid>

                    <Grid item xs={6}>
                      <Typography variant="body2" color="text.secondary">
                        Submitted
                      </Typography>
                      <Typography variant="body1">
                        {new Date(claim.submittedDate).toLocaleDateString()}
                      </Typography>
                    </Grid>
                  </Grid>

                  {/* Actions */}
                  <Stack direction="row" spacing={1}>
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
      {mockClaims.length === 0 && (
        <Paper sx={{ p: 6, textAlign: 'center' }}>
          <ClaimsIcon sx={{ fontSize: 64, color: 'text.secondary', mb: 2 }} />
          <Typography variant="h6" gutterBottom>
            No Claims Found
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
            You haven't submitted any insurance claims yet. If you need to file a claim, we're here
            to help.
          </Typography>
          <Button variant="contained" startIcon={<AddIcon />}>
            Submit Your First Claim
          </Button>
        </Paper>
      )}
    </Box>
  );
};

export default ClaimsPage;
