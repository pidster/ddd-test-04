/**
 * @fileoverview Entry point for Lakeside Mutual Customer Portal
 * Main React application bootstrap file that sets up the app with providers
 * @module CustomerPortalIndex
 */

import { ThemeProvider, CssBaseline } from '@mui/material';
import React from 'react';
import { createRoot } from 'react-dom/client';
import { ErrorBoundary } from 'react-error-boundary';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';

import App from './App';
import ErrorFallback from './components/common/ErrorFallback';
import { store } from './store/store';
import { theme } from './theme/theme';
import './index.css';

/**
 * Error handler for the error boundary
 * @param error - The error that occurred
 * @param errorInfo - Additional error information
 */
function onError(error: Error, errorInfo: { componentStack: string }) {
  console.error('Customer Portal Error:', error, errorInfo);
  // TODO: Send error to monitoring service
}

/**
 * Initialize and render the Customer Portal React application
 * Sets up all necessary providers and context for the application
 */
function initializeApp() {
  const container = document.getElementById('root');

  if (!container) {
    throw new Error('Failed to find the root element');
  }

  const root = createRoot(container);

  root.render(
    <React.StrictMode>
      <ErrorBoundary
        FallbackComponent={ErrorFallback}
        onError={onError}
        onReset={() => window.location.reload()}
      >
        <Provider store={store}>
          <BrowserRouter>
            <ThemeProvider theme={theme}>
              <CssBaseline />
              <App />
            </ThemeProvider>
          </BrowserRouter>
        </Provider>
      </ErrorBoundary>
    </React.StrictMode>
  );
}

// Initialize the application
initializeApp();
