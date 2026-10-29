import React, { useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import {
  Box, Typography, TextField, Button, Link as MuiLink,
  InputAdornment, IconButton, CircularProgress, Alert,
} from '@mui/material';
import VisibilityRoundedIcon from '@mui/icons-material/VisibilityRounded';
import VisibilityOffRoundedIcon from '@mui/icons-material/VisibilityOffRounded';
import ArrowBackRoundedIcon from '@mui/icons-material/ArrowBackRounded';
import LockRoundedIcon from '@mui/icons-material/LockRounded';
import { Link, useNavigate, useLocation, useSearchParams } from 'react-router-dom';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';

import { loginSchema } from '../../utils/validators';
import { login } from '../../services/authService';
import { useAuth } from '../../context/AuthContext';
import { getErrorMessage } from '../../utils/formatters';
import { HaikeiAmbientBlobs, GlassContainer } from '../../components/common/HaikeiDecorations';
import { Float } from '../../components/common/MotionPrimitives';

const LoginPage = () => {
  const { loginUser } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [searchParams] = useSearchParams();

  const [showPassword, setShowPassword] = useState(false);
  const [loading, setLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState('');

  const { control, handleSubmit, formState: { errors } } = useForm({
    resolver: yupResolver(loginSchema),
    defaultValues: { email: '', password: '' },
  });

  const onSubmit = async (data) => {
    setLoading(true);
    setErrorMsg('');
    try {
      const res = await login(data);
      loginUser(res.data);
      toast.success(`Welcome back, ${res.data.fullName}!`);
      
      const from = location.state?.from?.pathname;
      const dashboardMap = {
        ADMIN: '/admin/dashboard',
        CONSUMER: '/consumer/dashboard',
        PROVIDER: '/provider/dashboard',
      };
      navigate(from || dashboardMap[res.data.role] || '/', { replace: true });
    } catch (err) {
      const msg = getErrorMessage(err);
      setErrorMsg(msg);
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box
      sx={{
        minHeight: '100vh',
        display: 'flex',
        alignItems: 'center',
        justifyContent: 'center',
        position: 'relative',
        overflow: 'hidden',
        bgcolor: 'background.default',
        p: 2.5,
      }}
    >
      <HaikeiAmbientBlobs />

      <motion.div
        initial={{ opacity: 0, y: 25, scale: 0.98 }}
        animate={{ opacity: 1, y: 0, scale: 1 }}
        transition={{ duration: 0.5, ease: 'easeOut' }}
        style={{ width: '100%', maxWidth: 460, position: 'relative', zIndex: 1 }}
      >
        <GlassContainer
          sx={{
            p: { xs: 3.5, sm: 5 },
            borderRadius: 5,
            position: 'relative',
            overflow: 'hidden',
          }}
        >
          {/* Top Decorative Gradient Accent Line */}
          <Box
            sx={{
              position: 'absolute',
              top: 0,
              left: 0,
              right: 0,
              height: 4,
              background: 'linear-gradient(90deg, #6C63FF 0%, #FF6584 100%)',
            }}
          />

          {/* Back Button */}
          <Box sx={{ mb: 2 }}>
            <Button
              startIcon={<ArrowBackRoundedIcon />}
              onClick={() => navigate('/')}
              size="small"
              sx={{ color: 'text.secondary', fontWeight: 600, textTransform: 'none', px: 1 }}
            >
              Back to Home
            </Button>
          </Box>

          {/* Header */}
          <Box sx={{ textAlign: 'center', mb: 3.5 }}>
            <Box
              sx={{
                width: 50,
                height: 50,
                borderRadius: 3,
                background: 'linear-gradient(135deg, #6C63FF, #FF6584)',
                display: 'flex',
                alignItems: 'center',
                justifyContent: 'center',
                mx: 'auto',
                mb: 2,
                boxShadow: '0 8px 25px rgba(108, 99, 255, 0.35)',
              }}
            >
              <Typography sx={{ color: '#fff', fontWeight: 900, fontSize: 22 }}>S</Typography>
            </Box>
            <Typography variant="h4" fontWeight={900} sx={{ letterSpacing: -0.5 }}>
              Welcome Back
            </Typography>
            <Typography color="text.secondary" variant="body2" sx={{ mt: 0.5 }}>
              Sign in to manage your SERVICEiT bookings
            </Typography>
          </Box>

          {searchParams.get('verified') === 'true' && (
            <Alert severity="success" sx={{ mb: 2.5, borderRadius: 2 }}>
              🎉 Email verified successfully! You can now log in to your account.
            </Alert>
          )}

          {searchParams.get('reset') === 'true' && (
            <Alert severity="success" sx={{ mb: 2.5, borderRadius: 2 }}>
              🔒 Password reset successfully! Please sign in with your new password.
            </Alert>
          )}

          {errorMsg && (
            <Alert severity="error" sx={{ mb: 2.5, borderRadius: 2 }}>{errorMsg}</Alert>
          )}

          <form onSubmit={handleSubmit(onSubmit)} noValidate>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2.5 }}>
              <Controller
                name="email"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Email Address"
                    type="email"
                    error={!!errors.email}
                    helperText={errors.email?.message}
                    fullWidth
                    autoComplete="email"
                  />
                )}
              />

              <Box>
                <Controller
                  name="password"
                  control={control}
                  render={({ field }) => (
                    <TextField
                      {...field}
                      label="Password"
                      type={showPassword ? 'text' : 'password'}
                      error={!!errors.password}
                      helperText={errors.password?.message}
                      fullWidth
                      autoComplete="current-password"
                      InputProps={{
                        endAdornment: (
                          <InputAdornment position="end">
                            <IconButton onClick={() => setShowPassword(!showPassword)} edge="end">
                              {showPassword ? <VisibilityOffRoundedIcon /> : <VisibilityRoundedIcon />}
                            </IconButton>
                          </InputAdornment>
                        ),
                      }}
                    />
                  )}
                />
                <Box sx={{ display: 'flex', justifyContent: 'flex-end', mt: 1 }}>
                  <MuiLink
                    component={Link}
                    to="/forgot-password"
                    variant="body2"
                    fontWeight={600}
                    underline="hover"
                    color="primary.main"
                  >
                    Forgot Password?
                  </MuiLink>
                </Box>
              </Box>

              <Button
                type="submit"
                variant="contained"
                size="large"
                fullWidth
                disabled={loading}
                sx={{
                  py: 1.6,
                  borderRadius: 3,
                  fontSize: 16,
                  fontWeight: 800,
                  background: 'linear-gradient(135deg, #6C63FF 0%, #FF6584 100%)',
                  boxShadow: '0 8px 25px rgba(108, 99, 255, 0.35)',
                  '&:hover': {
                    background: 'linear-gradient(135deg, #5950ee 0%, #eb4f6f 100%)',
                    transform: 'translateY(-2px)',
                    boxShadow: '0 10px 30px rgba(108, 99, 255, 0.45)',
                  },
                  transition: 'all 0.25s ease',
                }}
                startIcon={loading ? <CircularProgress size={18} color="inherit" /> : <LockRoundedIcon />}
              >
                {loading ? 'Signing in...' : 'Sign In'}
              </Button>

              <Typography variant="body2" textAlign="center" color="text.secondary">
                Don't have an account?{' '}
                <MuiLink component={Link} to="/register" fontWeight={700} color="primary.main">
                  Create one now
                </MuiLink>
              </Typography>
            </Box>
          </form>
        </GlassContainer>
      </motion.div>
    </Box>
  );
};

export default LoginPage;
