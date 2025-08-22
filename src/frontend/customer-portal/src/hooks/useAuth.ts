/**
 * @fileoverview Authentication hook for Lakeside Mutual Customer Portal
 * Manages user authentication state and operations
 * @module useAuth
 */

import { useState, useEffect } from 'react';

/**
 * User interface representing authenticated customer
 */
interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  customerId: string;
}

/**
 * Authentication hook return type
 */
interface UseAuthReturn {
  /** Current authenticated user */
  user: User | null;
  /** Whether user is authenticated */
  isAuthenticated: boolean;
  /** Whether authentication check is in progress */
  isLoading: boolean;
  /** Authentication error message */
  error: string | null;
  /** Login function */
  login: (email: string, password: string) => Promise<void>;
  /** Logout function */
  logout: () => void;
}

/**
 * Custom hook for managing authentication state
 * Provides login, logout, and authentication status checking
 *
 * @hook
 * @returns {UseAuthReturn} Authentication state and methods
 *
 * @example
 * const { user, isAuthenticated, login, logout, isLoading, error } = useAuth();
 *
 * // Check if user is logged in
 * if (isAuthenticated) {
 *   console.log('User is logged in:', user.email);
 * }
 *
 * // Login user
 * await login('user@example.com', 'password');
 */
export const useAuth = (): UseAuthReturn => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  /**
   * Check if user is already authenticated on mount
   */
  useEffect(() => {
    const checkAuthStatus = async () => {
      try {
        const token = localStorage.getItem('auth_token');
        if (token) {
          // TODO: Validate token with backend
          // For now, use mock user data
          setUser({
            id: '1',
            email: 'demo@lakesidemutual.com',
            firstName: 'John',
            lastName: 'Doe',
            customerId: 'CUST-001',
          });
        }
      } catch (err) {
        console.error('Auth check failed:', err);
        localStorage.removeItem('auth_token');
      } finally {
        setIsLoading(false);
      }
    };

    checkAuthStatus();
  }, []);

  /**
   * Login user with email and password
   * @param email - User email
   * @param password - User password
   */
  const login = async (email: string, password: string): Promise<void> => {
    setIsLoading(true);
    setError(null);

    try {
      // TODO: Replace with actual API call
      if (email === 'demo@lakesidemutual.com' && password === 'demo123') {
        const mockUser: User = {
          id: '1',
          email: email,
          firstName: 'John',
          lastName: 'Doe',
          customerId: 'CUST-001',
        };

        // Simulate API delay
        await new Promise((resolve) => setTimeout(resolve, 1000));

        // Store mock token
        localStorage.setItem('auth_token', 'mock_jwt_token');
        setUser(mockUser);
      } else {
        throw new Error('Invalid email or password');
      }
    } catch (err) {
      const errorMessage = err instanceof Error ? err.message : 'Login failed';
      setError(errorMessage);
      throw err;
    } finally {
      setIsLoading(false);
    }
  };

  /**
   * Logout current user
   */
  const logout = (): void => {
    localStorage.removeItem('auth_token');
    setUser(null);
    setError(null);
  };

  return {
    user,
    isAuthenticated: !!user,
    isLoading,
    error,
    login,
    logout,
  };
};
