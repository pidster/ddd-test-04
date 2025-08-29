/**
 * @fileoverview Loading spinner component for Lakeside Mutual Customer Portal
 * Provides consistent loading state visualization across the application
 * @module LoadingSpinner
 */

import { Box, CircularProgress, Typography } from '@mui/material';
import React from 'react';

/**
 * Props for the LoadingSpinner component
 */
interface ILoadingSpinnerProps {
  /** Loading message to display */
  message?: string;
  /** Size of the spinner */
  size?: number;
  /** Whether to show the component inline or centered */
  variant?: 'inline' | 'centered';
}

/**
 * Loading spinner component that provides visual feedback during async operations
 * Can be used inline within components or as a full-page loading indicator
 *
 * @component
 * @param {LoadingSpinnerProps} props - Component props
 * @returns {JSX.Element} Loading spinner with optional message
 *
 * @example
 * // Basic usage
 * <LoadingSpinner />
 *
 * @example
 * // With custom message and size
 * <LoadingSpinner
 *   message="Loading policies..."
 *   size={50}
 *   variant="centered"
 * />
 */
const LoadingSpinner: React.FC<LoadingSpinnerProps> = ({
  message = 'Loading...',
  size = 40,
  variant = 'centered',
}) => {
  const content = (
    <>
      <CircularProgress size={size} />
      {message && (
        <Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>
          {message}
        </Typography>
      )}
    </>
  );

  if (variant === 'inline') {
    return (
      <Box
        sx={{
          display: 'flex',
          alignItems: 'center',
          justifyContent: 'center',
          gap: 2,
          p: 2,
        }}
      >
        {content}
      </Box>
    );
  }

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        justifyContent: 'center',
        minHeight: '50vh',
        textAlign: 'center',
      }}
    >
      {content}
    </Box>
  );
};

export default LoadingSpinner;
