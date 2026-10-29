import React, { useState } from 'react';
import { useForm, Controller } from 'react-hook-form';
import { yupResolver } from '@hookform/resolvers/yup';
import {
  Box, Typography, TextField, Button, Link as MuiLink,
  ToggleButtonGroup, ToggleButton, InputAdornment, IconButton,
  CircularProgress,
} from '@mui/material';
import PersonRoundedIcon from '@mui/icons-material/PersonRounded';
import WorkRoundedIcon from '@mui/icons-material/WorkRounded';
import VisibilityRoundedIcon from '@mui/icons-material/VisibilityRounded';
import VisibilityOffRoundedIcon from '@mui/icons-material/VisibilityOffRounded';
import ArrowBackRoundedIcon from '@mui/icons-material/ArrowBackRounded';
import CheckCircleRoundedIcon from '@mui/icons-material/CheckCircleRounded';
import { Link, useNavigate } from 'react-router-dom';
import { motion } from 'framer-motion';
import toast from 'react-hot-toast';

import { registerSchema } from '../../utils/validators';
import { register as registerUser } from '../../services/authService';
import { getErrorMessage } from '../../utils/formatters';
import { HaikeiAmbientBlobs, GlassContainer } from '../../components/common/HaikeiDecorations';

const RegisterPage = () => {
  const navigate = useNavigate();
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [loading, setLoading] = useState(false);

  const { control, handleSubmit, formState: { errors } } = useForm({
    resolver: yupResolver(registerSchema),
    defaultValues: { fullName: '', email: '', phone: '', password: '', confirmPassword: '', role: 'CONSUMER' },
  });

  const onSubmit = async (data) => {
    setLoading(true);
    try {
      const { confirmPassword, ...payload } = data;
      await registerUser(payload);
      toast.success('Registration successful! Please check your email to verify your account before logging in.');
      navigate('/verify-pending');
    } catch (err) {
      toast.error(getErrorMessage(err));
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
        py: { xs: 4, sm: 6 },
      }}
    >
      <HaikeiAmbientBlobs />

      <motion.div
        initial={{ opacity: 0, y: 25, scale: 0.98 }}
        animate={{ opacity: 1, y: 0, scale: 1 }}
        transition={{ duration: 0.5, ease: 'easeOut' }}
        style={{ width: '100%', maxWidth: 500, position: 'relative', zIndex: 1 }}
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
              Create Account
            </Typography>
            <Typography color="text.secondary" variant="body2" sx={{ mt: 0.5 }}>
              Join thousands of happy users on SERVICEiT
            </Typography>
          </Box>

          <form onSubmit={handleSubmit(onSubmit)} noValidate>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2.2 }}>
              
              {/* Role Selection */}
              <Box>
                <Typography variant="caption" fontWeight={700} color="text.secondary" sx={{ mb: 1, display: 'block', textTransform: 'uppercase', letterSpacing: 0.5 }}>
                  Account Type
                </Typography>
                <Controller
                  name="role"
                  control={control}
                  render={({ field }) => (
                    <ToggleButtonGroup
                      value={field.value}
                      exclusive
                      onChange={(_, v) => v && field.onChange(v)}
                      fullWidth
                      sx={{
                        p: 0.5,
                        bgcolor: (theme) => theme.palette.mode === 'dark' ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.04)',
                        borderRadius: 3,
                        border: '1px solid',
                        borderColor: 'divider',
                        '& .MuiToggleButton-root': {
                          border: 'none',
                          borderRadius: '10px !important',
                          py: 1.2,
                          fontWeight: 700,
                          textTransform: 'none',
                          transition: 'all 0.2s ease',
                          '&.Mui-selected': {
                            bgcolor: 'primary.main',
                            color: '#fff',
                            boxShadow: '0 4px 15px rgba(108, 99, 255, 0.35)',
                            '&:hover': {
                              bgcolor: 'primary.dark',
                            },
                          },
                        },
                      }}
                    >
                      <ToggleButton value="CONSUMER" sx={{ gap: 1 }}>
                        <PersonRoundedIcon fontSize="small" /> Consumer
                      </ToggleButton>
                      <ToggleButton value="PROVIDER" sx={{ gap: 1 }}>
                        <WorkRoundedIcon fontSize="small" /> Service Provider
                      </ToggleButton>
                    </ToggleButtonGroup>
                  )}
                />
                {errors.role && <Typography variant="caption" color="error">{errors.role.message}</Typography>}
              </Box>

              <Controller
                name="fullName"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Full Name"
                    error={!!errors.fullName}
                    helperText={errors.fullName?.message}
                    fullWidth
                    autoComplete="name"
                  />
                )}
              />

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

              <Controller
                name="phone"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Phone Number"
                    error={!!errors.phone}
                    helperText={errors.phone?.message}
                    fullWidth
                    inputProps={{ maxLength: 10 }}
                    autoComplete="tel"
                  />
                )}
              />

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
                    autoComplete="new-password"
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

              <Controller
                name="confirmPassword"
                control={control}
                render={({ field }) => (
                  <TextField
                    {...field}
                    label="Confirm Password"
                    type={showConfirmPassword ? 'text' : 'password'}
                    error={!!errors.confirmPassword}
                    helperText={errors.confirmPassword?.message}
                    fullWidth
                    autoComplete="new-password"
                    InputProps={{
                      endAdornment: (
                        <InputAdornment position="end">
                          <IconButton onClick={() => setShowConfirmPassword(!showConfirmPassword)} edge="end">
                            {showConfirmPassword ? <VisibilityOffRoundedIcon /> : <VisibilityRoundedIcon />}
                          </IconButton>
                        </InputAdornment>
                      ),
                    }}
                  />
                )}
              />

              <Button
                type="submit"
                variant="contained"
                size="large"
                fullWidth
                disabled={loading}
                sx={{
                  py: 1.6,
                  mt: 1,
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
                startIcon={loading ? <CircularProgress size={18} color="inherit" /> : <CheckCircleRoundedIcon />}
              >
                {loading ? 'Creating Account...' : 'Create Account'}
              </Button>

              <Typography variant="body2" textAlign="center" color="text.secondary">
                Already have an account?{' '}
                <MuiLink component={Link} to="/login" fontWeight={700} color="primary.main">
                  Sign In
                </MuiLink>
              </Typography>
            </Box>
          </form>
        </GlassContainer>
      </motion.div>
    </Box>
  );
};

export default RegisterPage;
