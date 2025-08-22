/**
 * @fileoverview UI slice for Redux store
 * Manages UI state in the application
 * @module uiSlice
 */

import { createSlice, PayloadAction } from '@reduxjs/toolkit';

/**
 * UI state interface
 */
interface UIState {
  sidebarOpen: boolean;
  theme: 'light' | 'dark';
  notifications: string[];
}

/**
 * Initial state for UI
 */
const initialState: UIState = {
  sidebarOpen: false,
  theme: 'light',
  notifications: [],
};

/**
 * UI slice
 */
const uiSlice = createSlice({
  name: 'ui',
  initialState,
  reducers: {
    toggleSidebar: (state) => {
      state.sidebarOpen = !state.sidebarOpen;
    },
    setTheme: (state, action: PayloadAction<'light' | 'dark'>) => {
      state.theme = action.payload;
    },
    addNotification: (state, action: PayloadAction<string>) => {
      state.notifications.push(action.payload);
    },
    removeNotification: (state, action: PayloadAction<number>) => {
      state.notifications.splice(action.payload, 1);
    },
  },
});

export const { toggleSidebar, setTheme, addNotification, removeNotification } = uiSlice.actions;
export default uiSlice.reducer;
