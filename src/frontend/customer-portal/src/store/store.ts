/**
 * @fileoverview Redux store configuration for Lakeside Mutual Customer Portal
 * Configures the application state management using Redux Toolkit
 * @module store
 */

import { configureStore } from '@reduxjs/toolkit';
import { TypedUseSelectorHook, useDispatch, useSelector } from 'react-redux';

// Import slice reducers (to be created)
import authSlice from './slices/authSlice';
import claimsSlice from './slices/claimsSlice';
import policiesSlice from './slices/policiesSlice';
import uiSlice from './slices/uiSlice';

/**
 * Configure the Redux store with all application slices
 * Uses Redux Toolkit for simplified store setup and development tools
 */
export const store = configureStore({
  reducer: {
    auth: authSlice,
    policies: policiesSlice,
    claims: claimsSlice,
    ui: uiSlice,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: {
        // Ignore these action types for serialization checks
        ignoredActions: ['persist/PERSIST', 'persist/REHYDRATE'],
      },
    }),
  devTools: process.env.NODE_ENV !== 'production',
});

// Infer the `RootState` and `AppDispatch` types from the store itself
export type TRootState = ReturnType<typeof store.getState>;
export type TAppDispatch = typeof store.dispatch;

// Use throughout your app instead of plain `useDispatch` and `useSelector`
export const useAppDispatch = () => useDispatch<AppDispatch>();
export const useAppSelector: TypedUseSelectorHook<RootState> = useSelector;

export default store;
