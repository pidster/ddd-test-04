/**
 * @fileoverview Claims slice for Redux store
 * Manages claims state in the application
 * @module claimsSlice
 */

import { createSlice, PayloadAction } from '@reduxjs/toolkit';

/**
 * Claim interface
 */
interface IClaim {
  id: string;
  type: string;
  status: string;
  amount: number;
  submittedDate: string;
  policyId: string;
}

/**
 * Claims state interface
 */
interface IClaimsState {
  claims: Claim[];
  isLoading: boolean;
  error: string | null;
}

/**
 * Initial state for claims
 */
const initialState: ClaimsState = {
  claims: [],
  isLoading: false,
  error: null,
};

/**
 * Claims slice
 */
const claimsSlice = createSlice({
  name: 'claims',
  initialState,
  reducers: {
    setClaims: (state, action: PayloadAction<Claim[]>) => {
      state.claims = action.payload;
    },
    setLoading: (state, action: PayloadAction<boolean>) => {
      state.isLoading = action.payload;
    },
    setError: (state, action: PayloadAction<string | null>) => {
      state.error = action.payload;
    },
  },
});

export const { setClaims, setLoading, setError } = claimsSlice.actions;
export default claimsSlice.reducer;
