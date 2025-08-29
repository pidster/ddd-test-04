/**
 * @fileoverview Policies slice for Redux store
 * Manages policies state in the application
 * @module policiesSlice
 */

import { createSlice, PayloadAction } from '@reduxjs/toolkit';

/**
 * Policy interface
 */
interface IPolicy {
  id: string;
  type: string;
  status: string;
  premium: number;
  coverage: string;
  renewalDate: string;
}

/**
 * Policies state interface
 */
interface IPoliciesState {
  policies: Policy[];
  isLoading: boolean;
  error: string | null;
}

/**
 * Initial state for policies
 */
const initialState: PoliciesState = {
  policies: [],
  isLoading: false,
  error: null,
};

/**
 * Policies slice
 */
const policiesSlice = createSlice({
  name: 'policies',
  initialState,
  reducers: {
    setPolicies: (state, action: PayloadAction<Policy[]>) => {
      state.policies = action.payload;
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
  },
});

export const { setPolicies, setLoading, setError } = policiesSlice.actions;
export default policiesSlice.reducer;
