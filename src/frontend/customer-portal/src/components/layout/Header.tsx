/**
 * @fileoverview Header component for Lakeside Mutual Customer Portal
 * Provides navigation and user account access
 * @module Header
 */

import { AccountCircle, Logout, Person, Notifications } from '@mui/icons-material';
import {
  AppBar,
  Toolbar,
  Typography,
  Button,
  IconButton,
  Menu,
  MenuItem,
  Avatar,
  Box,
  Divider,
} from '@mui/material';
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

import { useAuth } from '../../hooks/useAuth';

/**
 * Header component that provides top navigation and user menu
 * Displays company branding, navigation links, and user account access
 *
 * @component
 * @returns {JSX.Element} Header with navigation and user menu
 */
const Header: React.FC = () => {
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const isMenuOpen = Boolean(anchorEl);

  /**
   * Handle opening user menu
   */
  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  /**
   * Handle closing user menu
   */
  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  /**
   * Handle navigation to profile page
   */
  const handleProfile = () => {
    handleMenuClose();
    navigate('/profile');
  };

  /**
   * Handle user logout
   */
  const handleLogout = () => {
    handleMenuClose();
    logout();
  };

  return (
    <AppBar position="sticky" elevation={1}>
      <Toolbar>
        {/* Company Logo/Brand */}
        <Box
          onClick={() => navigate('/dashboard')}
          sx={{
            display: 'flex',
            alignItems: 'center',
            cursor: 'pointer',
            flexGrow: 1,
          }}
        >
          <Typography
            variant="h6"
            component="div"
            sx={{
              fontWeight: 600,
              letterSpacing: 0.5,
            }}
          >
            Lakeside Mutual
          </Typography>
        </Box>

        {/* Navigation Links */}
        <Box sx={{ display: 'flex', gap: 1, mr: 2 }}>
          <Button
            color="inherit"
            onClick={() => navigate('/dashboard')}
            sx={{ textTransform: 'none' }}
          >
            Dashboard
          </Button>
          <Button
            color="inherit"
            onClick={() => navigate('/policies')}
            sx={{ textTransform: 'none' }}
          >
            Policies
          </Button>
          <Button
            color="inherit"
            onClick={() => navigate('/claims')}
            sx={{ textTransform: 'none' }}
          >
            Claims
          </Button>
        </Box>

        {/* Notifications */}
        <IconButton color="inherit" sx={{ mr: 1 }}>
          <Notifications />
        </IconButton>

        {/* User Menu */}
        <Box sx={{ display: 'flex', alignItems: 'center' }}>
          <IconButton
            size="large"
            edge="end"
            aria-label="account of current user"
            aria-controls="user-menu"
            aria-haspopup="true"
            onClick={handleMenuOpen}
            color="inherit"
          >
            <Avatar
              sx={{
                width: 32,
                height: 32,
                bgcolor: 'secondary.main',
                fontSize: '0.875rem',
              }}
            >
              {user?.firstName?.[0]}
              {user?.lastName?.[0]}
            </Avatar>
          </IconButton>
        </Box>

        {/* User Menu Dropdown */}
        <Menu
          id="user-menu"
          anchorEl={anchorEl}
          open={isMenuOpen}
          onClose={handleMenuClose}
          transformOrigin={{ horizontal: 'right', vertical: 'top' }}
          anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
          PaperProps={{
            sx: { minWidth: 200 },
          }}
        >
          {/* User Info */}
          <Box sx={{ px: 2, py: 1 }}>
            <Typography variant="subtitle2" noWrap>
              {user?.firstName} {user?.lastName}
            </Typography>
            <Typography variant="body2" color="text.secondary" noWrap>
              {user?.email}
            </Typography>
          </Box>

          <Divider />

          {/* Menu Items */}
          <MenuItem onClick={handleProfile}>
            <Person sx={{ mr: 1 }} />
            My Profile
          </MenuItem>

          <Divider />

          <MenuItem onClick={handleLogout}>
            <Logout sx={{ mr: 1 }} />
            Logout
          </MenuItem>
        </Menu>
      </Toolbar>
    </AppBar>
  );
};

export default Header;
