/**
 * @fileoverview Auth slice for Redux store
 * Manages authentication state in the application
 * @module authSlice
 */

import { createSlice, PayloadAction } from '@reduxjs/toolkit';

/**
 * User interface for authenticated customer
 */
interface IUser {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  customerId: string;
}

/**
 * Authentication state interface
 */
interface IAuthState {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  error: string | null;
}

/**
 * Initial state for authentication
 */
const initialState: AuthState = {
  user: null,
  isAuthenticated: false,
  isLoading: false,
  error: null,
};

/**
 * Auth slice containing authentication reducers and actions
 */
const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    /**
     * Set loading state for authentication operations
     */
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },

    /**
     * Set authentication error
     */
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },

    /**
     * Set authenticated user
     */
    setUser: (state, action: PayloadAction<User>) => {
      state.user = action.payload;
      state.isAuthenticated = true;
      state.error = null;
    },

    /**
     * Clear user authentication
     */
    clearUser: (state) => {
      state.user = null;
      state.isAuthenticated = false;
      state.error = null;
    },

    /**
     * Reset auth state to initial values
     */
    resetAuthState: () => initialState,
  },
});

// Export actions
export const { setLoading, setError, setUser, clearUser, resetAuthState } = authSlice.actions;

// Export reducer
export default authSlice.reducer;
