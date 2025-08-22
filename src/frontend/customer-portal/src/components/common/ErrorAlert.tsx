/**
 * @fileoverview Error alert component for Lakeside Mutual Customer Portal
 * Provides consistent error message display across the application
 * @module ErrorAlert
 */

import { Refresh as RefreshIcon } from '@mui/icons-material';
import { Alert, AlertTitle, Box, Button } from '@mui/material';
import React from 'react';

/**
 * Props for the ErrorAlert component
 */
interface ErrorAlertProps {
  /** Error message to display */
  message: string;
  /** Detailed error information (optional) */
  error?: Error | string;
  /** Callback function for retry action */
  onRetry?: () => void;
  /** Severity level of the error */
  severity?: 'error' | 'warning' | 'info';
  /** Whether to show detailed error information */
  showDetails?: boolean;
}

/**
 * Error alert component that displays error messages with optional retry functionality
 * Provides consistent error handling UI across the customer portal
 *
 * @component
 * @param {ErrorAlertProps} props - Component props
 * @returns {JSX.Element} Error alert with message and optional actions
 *
 * @example
 * // Basic error alert
 * <ErrorAlert message="Failed to load data" />
 *
 * @example
 * // Error alert with retry functionality
 * <ErrorAlert
 *   message="Network error occurred"
 *   onRetry={handleRetry}
 *   error={networkError}
 *   showDetails={true}
 * />
 */
const ErrorAlert: React.FC<ErrorAlertProps> = ({
  message,
  error,
  onRetry,
  severity = 'error',
  showDetails = false,
}) => {
  const errorDetails = error instanceof Error ? error.message : String(error);

  return (
    <Box sx={{ my: 2 }}>
      <Alert
        severity={severity}
        action={
          onRetry && (
            <Button color="inherit" size="small" onClick={onRetry} startIcon={<RefreshIcon />}>
              Retry
            </Button>
          )
        }
      >
        <AlertTitle>
          {severity === 'error' ? 'Error' : severity === 'warning' ? 'Warning' : 'Information'}
        </AlertTitle>
        {message}

        {showDetails && errorDetails && (
          <Box sx={{ mt: 1, fontSize: '0.875rem', opacity: 0.8 }}>Details: {errorDetails}</Box>
        )}
      </Alert>
    </Box>
  );
};

export default ErrorAlert;
