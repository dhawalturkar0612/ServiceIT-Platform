import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import {
  Box, Typography, Button, Container, Grid, Card, CardContent,
  Chip, IconButton, Tooltip, AppBar, Toolbar, Stack, Paper,
  Accordion, AccordionSummary, AccordionDetails, Divider,
} from '@mui/material';
import { motion, AnimatePresence } from 'framer-motion';

// Icons
import VerifiedRoundedIcon from '@mui/icons-material/VerifiedRounded';
import AccessTimeRoundedIcon from '@mui/icons-material/AccessTimeRounded';
import SecurityRoundedIcon from '@mui/icons-material/SecurityRounded';
import ArrowForwardRoundedIcon from '@mui/icons-material/ArrowForwardRounded';
import Brightness4Icon from '@mui/icons-material/Brightness4';
import Brightness7Icon from '@mui/icons-material/Brightness7';
import HandymanRoundedIcon from '@mui/icons-material/HandymanRounded';
import FlashOnRoundedIcon from '@mui/icons-material/FlashOnRounded';
import StarRoundedIcon from '@mui/icons-material/StarRounded';
import CheckCircleRoundedIcon from '@mui/icons-material/CheckCircleRounded';
import ExpandMoreRoundedIcon from '@mui/icons-material/ExpandMoreRounded';
import PaymentsRoundedIcon from '@mui/icons-material/PaymentsRounded';
import SupportAgentRoundedIcon from '@mui/icons-material/SupportAgentRounded';
import EngineeringRoundedIcon from '@mui/icons-material/EngineeringRounded';

// Custom Motion & Haikei Primitives
import { Float, PulseBadge, GlowCard } from '../../components/common/MotionPrimitives';
import { HaikeiAmbientBlobs, GlassContainer } from '../../components/common/HaikeiDecorations';
import { useTheme } from '../../context/ThemeContext';

const fadeUp = (delay = 0) => ({
  initial: { opacity: 0, y: 30 },
  whileInView: { opacity: 1, y: 0 },
  viewport: { once: true },
  transition: { duration: 0.5, delay, ease: [0.25, 0.1, 0.25, 1.0] },
});

const services = [
  { name: 'Plumbing', icon: '🔧', desc: 'Pipe repair, tap fix & drainage', price: '₹199', tag: 'High Demand', color: '#6C63FF' },
  { name: 'Electrician', icon: '⚡', desc: 'Wiring, MCB switches & fans', price: '₹249', tag: 'Popular', color: '#FFB800' },
  { name: 'Cleaning', icon: '🧹', desc: 'Deep home, sofa & bathroom cleaning', price: '₹499', tag: 'Best Value', color: '#43E97B' },
  { name: 'Carpentry', icon: '🪚', desc: 'Furniture assembly & lock repair', price: '₹299', tag: 'Verified', color: '#FF6584' },
  { name: 'Painting', icon: '🎨', desc: 'Interior walls, waterproofing & touchup', price: '₹999', tag: 'Expert Pros', color: '#38EF7D' },
  { name: 'AC Repair', icon: '❄️', desc: 'Cooling fixes, gas refill & deep service', price: '₹399', tag: 'Seasonal Top', color: '#00C6FF' },
];

const stats = [
  { value: '25,000+', label: 'Happy Customers', sub: 'Across 12+ categories' },
  { value: '1,200+', label: 'Verified Specialists', sub: '100% background checked' },
  { value: '99.4%', label: 'On-Time Arrival', sub: 'Guaranteed 30-min window' },
  { value: '4.9 ★', label: 'Average Rating', sub: 'Based on 18K+ verified reviews' },
];

const workflowSteps = [
  {
    step: '01',
    title: 'Select a Service',
    desc: 'Browse transparent pricing across 50+ home repair and maintenance services.',
    icon: <HandymanRoundedIcon sx={{ fontSize: 28, color: '#6C63FF' }} />,
  },
  {
    step: '02',
    title: 'Pick a Time Slot',
    desc: 'Choose a date and convenient window that fits your schedule seamlessly.',
    icon: <AccessTimeRoundedIcon sx={{ fontSize: 28, color: '#FF6584' }} />,
  },
  {
    step: '03',
    title: 'Verified Pro Arrives',
    desc: 'A background-checked professional arrives equipped with professional tools.',
    icon: <EngineeringRoundedIcon sx={{ fontSize: 28, color: '#43E97B' }} />,
  },
  {
    step: '04',
    title: 'Pay After Satisfaction',
    desc: 'Enjoy quality service, then pay securely through Razorpay with complete buyer escrow.',
    icon: <PaymentsRoundedIcon sx={{ fontSize: 28, color: '#00C6FF' }} />,
  },
];

const features = [
  {
    icon: <VerifiedRoundedIcon sx={{ fontSize: 32, color: '#6C63FF' }} />,
    title: 'Verified Professionals Only',
    desc: 'Every provider undergoes stringent document verification and admin onboarding before serving you.',
    badge: '100% Vetted',
  },
  {
    icon: <SecurityRoundedIcon sx={{ fontSize: 32, color: '#FF6584' }} />,
    title: 'Secure Razorpay Checkout',
    desc: 'Industry-standard 256-bit encrypted checkout supporting UPI, Cards, NetBanking, and Wallets.',
    badge: 'Escrow Protected',
  },
  {
    icon: <AccessTimeRoundedIcon sx={{ fontSize: 32, color: '#43E97B' }} />,
    title: 'Guaranteed On-Time Arrival',
    desc: 'Direct slot dispatch with real-time appointment tracking and automatic provider buffer windows.',
    badge: 'Instant Slot',
  },
  {
    icon: <SupportAgentRoundedIcon sx={{ fontSize: 32, color: '#00C6FF' }} />,
    title: '24/7 Dedicated Support',
    desc: 'Friendly customer assistance for rescheduling, service warranties, and instant dispute resolution.',
    badge: 'Always Here',
  },
];

const faqs = [
  {
    q: 'How are service providers verified on SERVICEiT?',
    a: 'Every provider is required to submit government ID proofs, trade certifications, and background documents. Our admin team manually reviews and approves each profile before they can accept bookings.',
  },
  {
    q: 'Is payment processed immediately or after service?',
    a: 'Your payments are handled safely via Razorpay. Booking payments are securely held and released only when the service is satisfactorily completed.',
  },
  {
    q: 'Can I reschedule or cancel a booking?',
    a: 'Yes, you can easily reschedule or cancel your appointment directly from your Consumer Dashboard before the scheduled window at no extra cost.',
  },
  {
    q: 'How do I join as a Service Provider?',
    a: 'Simply click "Become a Provider", sign up with your trade details, and once verified by our administrative team, you can start receiving customer service requests right away.',
  },
];

const LandingPage = () => {
  const navigate = useNavigate();
  const { mode, toggleTheme } = useTheme();

  return (
    <Box sx={{ minHeight: '100vh', display: 'flex', flexDirection: 'column', overflowX: 'hidden' }}>
      
      {/* 1. Glassmorphic Navigation Bar */}
      <AppBar
        position="sticky"
        color="transparent"
        elevation={0}
        sx={{
          backdropFilter: 'blur(20px)',
          WebkitBackdropFilter: 'blur(20px)',
          bgcolor: mode === 'dark' ? 'rgba(15, 15, 26, 0.85)' : 'rgba(255, 255, 255, 0.85)',
          borderBottom: '1px solid',
          borderColor: mode === 'dark' ? 'rgba(255,255,255,0.08)' : 'rgba(108, 99, 255, 0.1)',
          zIndex: 1100,
        }}
      >
        <Container maxWidth="lg">
          <Toolbar disableGutters sx={{ py: 1.2 }}>
            {/* Logo */}
            <Box
              component={motion.div}
              whileHover={{ scale: 1.03 }}
              whileTap={{ scale: 0.98 }}
              sx={{ display: 'flex', alignItems: 'center', gap: 1.5, flexGrow: 1, cursor: 'pointer' }}
              onClick={() => navigate('/')}
            >
              <Box
                sx={{
                  width: 40,
                  height: 40,
                  borderRadius: 2.5,
                  background: 'linear-gradient(135deg, #6C63FF 0%, #FF6584 100%)',
                  display: 'flex',
                  alignItems: 'center',
                  justifyContent: 'center',
                  boxShadow: '0 8px 20px rgba(108, 99, 255, 0.35)',
                }}
              >
                <Typography sx={{ color: '#fff', fontWeight: 900, fontSize: 18 }}>S</Typography>
              </Box>
              <Typography
                variant="h6"
                fontWeight={900}
                sx={{
                  background: 'linear-gradient(135deg, #6C63FF 0%, #FF6584 100%)',
                  WebkitBackgroundClip: 'text',
                  WebkitTextFillColor: 'transparent',
                  letterSpacing: -0.5,
                  fontSize: { xs: 20, sm: 22 },
                }}
              >
                SERVICEiT
              </Typography>
            </Box>

            {/* Nav Actions */}
            <Stack direction="row" spacing={1.5} alignItems="center">
              <Tooltip title={`Switch to ${mode === 'light' ? 'dark' : 'light'} mode`}>
                <IconButton
                  onClick={toggleTheme}
                  size="small"
                  sx={{
                    p: 1,
                    bgcolor: mode === 'dark' ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.04)',
                    borderRadius: 2,
                  }}
                >
                  {mode === 'dark' ? <Brightness7Icon fontSize="small" sx={{ color: '#FFB800' }} /> : <Brightness4Icon fontSize="small" />}
                </IconButton>
              </Tooltip>

              <Button
                variant="outlined"
                component={Link}
                to="/login"
                sx={{
                  borderRadius: 2.5,
                  px: 2.5,
                  textTransform: 'none',
                  fontWeight: 700,
                  borderColor: 'divider',
                  display: { xs: 'none', sm: 'inline-flex' },
                }}
              >
                Sign In
              </Button>

              <Button
                variant="contained"
                component={Link}
                to="/register"
                sx={{
                  borderRadius: 2.5,
                  px: 3,
                  py: 1,
                  textTransform: 'none',
                  fontWeight: 800,
                  background: 'linear-gradient(135deg, #6C63FF 0%, #FF6584 100%)',
                  boxShadow: '0 6px 20px rgba(108, 99, 255, 0.35)',
                  '&:hover': {
                    background: 'linear-gradient(135deg, #5950ee 0%, #eb4f6f 100%)',
                    transform: 'translateY(-2px)',
                    boxShadow: '0 8px 25px rgba(108, 99, 255, 0.45)',
                  },
                  transition: 'all 0.2s ease',
                }}
              >
                Get Started
              </Button>
            </Stack>
          </Toolbar>
        </Container>
      </AppBar>

      {/* 2. Hero Section with Haikei Organic Blobs & Motion */}
      <Box
        sx={{
          pt: { xs: 8, md: 12 },
          pb: { xs: 10, md: 14 },
          position: 'relative',
          overflow: 'hidden',
          bgcolor: 'background.default',
        }}
      >
        <HaikeiAmbientBlobs />

        <Container maxWidth="lg" sx={{ position: 'relative', zIndex: 1 }}>
          <Grid container spacing={{ xs: 5, md: 6 }} alignItems="center">
            
            {/* Left Column: Hero Text & CTAs */}
            <Grid item xs={12} md={6.5}>
              <motion.div
                initial={{ opacity: 0, y: 25 }}
                animate={{ opacity: 1, y: 0 }}
                transition={{ duration: 0.6 }}
              >
                {/* Pulsing Live Badge */}
                <Box sx={{ mb: 3 }}>
                  <PulseBadge label="⚡ 1,200+ Verified Pros Active Near You" dotColor="#43E97B" />
                </Box>

                <Typography
                  variant="h1"
                  sx={{
                    fontSize: { xs: '2.4rem', sm: '3.4rem', md: '3.8rem' },
                    fontWeight: 900,
                    lineHeight: 1.12,
                    letterSpacing: -1.2,
                    mb: 2.5,
                  }}
                >
                  Doorstep Home Services,{' '}
                  <Box
                    component="span"
                    sx={{
                      background: 'linear-gradient(135deg, #6C63FF 0%, #FF6584 100%)',
                      WebkitBackgroundClip: 'text',
                      WebkitTextFillColor: 'transparent',
                      display: 'inline-block',
                    }}
                  >
                    Delivered with Care.
                  </Box>
                </Typography>

                <Typography
                  variant="body1"
                  color="text.secondary"
                  sx={{
                    fontSize: { xs: 16, sm: 18 },
                    lineHeight: 1.65,
                    mb: 4,
                    maxWidth: 520,
                  }}
                >
                  Book certified plumbers, electricians, cleaners, and carpenters in seconds. 
                  Enjoy transparent pricing, scheduled arrival, and 100% satisfaction guarantee.
                </Typography>

                {/* Primary Action Buttons */}
                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2} sx={{ mb: 4 }}>
                  <Button
                    variant="contained"
                    size="large"
                    onClick={() => navigate('/register')}
                    endIcon={<ArrowForwardRoundedIcon />}
                    sx={{
                      px: 4,
                      py: 1.8,
                      fontSize: 16,
                      fontWeight: 800,
                      borderRadius: 3,
                      background: 'linear-gradient(135deg, #6C63FF 0%, #FF6584 100%)',
                      boxShadow: '0 10px 30px rgba(108, 99, 255, 0.4)',
                      '&:hover': {
                        transform: 'translateY(-3px)',
                        boxShadow: '0 14px 35px rgba(108, 99, 255, 0.5)',
                      },
                      transition: 'all 0.25s ease',
                    }}
                  >
                    Book a Service
                  </Button>

                  <Button
                    variant="outlined"
                    size="large"
                    onClick={() => navigate('/register?role=PROVIDER')}
                    sx={{
                      px: 3.5,
                      py: 1.8,
                      fontSize: 16,
                      fontWeight: 700,
                      borderRadius: 3,
                      borderColor: 'divider',
                      bgcolor: mode === 'dark' ? 'rgba(255,255,255,0.03)' : 'background.paper',
                      backdropFilter: 'blur(10px)',
                      '&:hover': {
                        borderColor: 'primary.main',
                        transform: 'translateY(-3px)',
                        bgcolor: mode === 'dark' ? 'rgba(108,99,255,0.1)' : 'rgba(108,99,255,0.05)',
                      },
                      transition: 'all 0.25s ease',
                    }}
                  >
                    Become a Provider
                  </Button>
                </Stack>

                {/* Trust Guarantee Badges */}
                <Stack direction="row" spacing={3} alignItems="center" flexWrap="wrap">
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <CheckCircleRoundedIcon sx={{ color: '#43E97B', fontSize: 20 }} />
                    <Typography variant="body2" fontWeight={600} color="text.secondary">
                      100% Verified Pros
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <CheckCircleRoundedIcon sx={{ color: '#43E97B', fontSize: 20 }} />
                    <Typography variant="body2" fontWeight={600} color="text.secondary">
                      Razorpay Secured
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                    <CheckCircleRoundedIcon sx={{ color: '#43E97B', fontSize: 20 }} />
                    <Typography variant="body2" fontWeight={600} color="text.secondary">
                      Fixed Fair Rates
                    </Typography>
                  </Box>
                </Stack>

              </motion.div>
            </Grid>

            {/* Right Column: Interactive Motion Showcase Card */}
            <Grid item xs={12} md={5.5}>
              <Box sx={{ position: 'relative' }}>
                
                {/* Floating Top Badge */}
                <Float distance={12} duration={5} style={{ position: 'absolute', top: -25, left: -20, zIndex: 10 }}>
                  <Paper
                    elevation={0}
                    sx={{
                      p: 1.5,
                      px: 2,
                      borderRadius: 3,
                      display: 'flex',
                      alignItems: 'center',
                      gap: 1.2,
                      border: '1px solid rgba(67, 233, 123, 0.4)',
                      bgcolor: mode === 'dark' ? 'rgba(26, 26, 46, 0.95)' : 'rgba(255, 255, 255, 0.95)',
                      backdropFilter: 'blur(12px)',
                      boxShadow: '0 8px 30px rgba(0,0,0,0.12)',
                    }}
                  >
                    <Box
                      sx={{
                        width: 32,
                        height: 32,
                        borderRadius: 2,
                        bgcolor: 'rgba(67, 233, 123, 0.15)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                      }}
                    >
                      <StarRoundedIcon sx={{ color: '#FFB800', fontSize: 22 }} />
                    </Box>
                    <Box>
                      <Typography variant="caption" fontWeight={800} sx={{ display: 'block' }}>
                        4.9 ★ Top Rated
                      </Typography>
                      <Typography variant="caption" color="text.secondary" fontSize={11}>
                        18,000+ reviews
                      </Typography>
                    </Box>
                  </Paper>
                </Float>

                {/* Main Glass Showcase Card */}
                <GlassContainer
                  sx={{
                    p: { xs: 3, sm: 4 },
                    borderRadius: 5,
                    border: '1px solid',
                    borderColor: mode === 'dark' ? 'rgba(255,255,255,0.12)' : 'rgba(108, 99, 255, 0.15)',
                    boxShadow: mode === 'dark' ? '0 20px 60px rgba(0,0,0,0.6)' : '0 20px 60px rgba(108, 99, 255, 0.12)',
                  }}
                >
                  {/* Card Header with Live Notification */}
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
                    <Typography variant="subtitle1" fontWeight={800}>
                      Instant Booking Hub
                    </Typography>
                    <Chip
                      size="small"
                      label="LIVE"
                      sx={{
                        bgcolor: 'rgba(255, 101, 132, 0.15)',
                        color: '#FF6584',
                        fontWeight: 800,
                        fontSize: 11,
                        borderRadius: 1.5,
                      }}
                    />
                  </Box>

                  {/* 4 Interactive Service Cards */}
                  <Grid container spacing={2}>
                    {services.slice(0, 4).map((s) => (
                      <Grid item xs={6} key={s.name}>
                        <Box
                          component={motion.div}
                          whileHover={{ scale: 1.04, y: -4 }}
                          whileTap={{ scale: 0.98 }}
                          onClick={() => navigate('/login')}
                          sx={{
                            p: 2,
                            borderRadius: 3.5,
                            bgcolor: mode === 'dark' ? 'rgba(255,255,255,0.03)' : 'rgba(255,255,255,0.7)',
                            border: '1px solid',
                            borderColor: mode === 'dark' ? 'rgba(255,255,255,0.08)' : 'rgba(0,0,0,0.06)',
                            cursor: 'pointer',
                            transition: 'all 0.25s ease',
                            '&:hover': {
                              borderColor: s.color,
                              boxShadow: `0 8px 25px -5px ${s.color}33`,
                            },
                          }}
                        >
                          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 1 }}>
                            <Typography fontSize={32}>{s.icon}</Typography>
                            <Chip
                              label={s.price}
                              size="small"
                              sx={{
                                fontWeight: 800,
                                fontSize: 11,
                                height: 22,
                                bgcolor: `${s.color}18`,
                                color: s.color,
                                borderRadius: 1.5,
                              }}
                            />
                          </Box>
                          <Typography fontWeight={800} variant="body2" sx={{ mb: 0.5 }}>
                            {s.name}
                          </Typography>
                          <Typography variant="caption" color="text.secondary" sx={{ display: 'block', fontSize: 11 }}>
                            {s.desc}
                          </Typography>
                        </Box>
                      </Grid>
                    ))}
                  </Grid>

                  {/* Fast Action Footer */}
                  <Box sx={{ mt: 3, pt: 2.5, borderTop: '1px solid', borderColor: 'divider', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Typography variant="caption" color="text.secondary" fontWeight={600}>
                      💡 Transparent rates with no hidden fees
                    </Typography>
                    <Button
                      size="small"
                      onClick={() => navigate('/login')}
                      endIcon={<ArrowForwardRoundedIcon sx={{ fontSize: 16 }} />}
                      sx={{ textTransform: 'none', fontWeight: 700 }}
                    >
                      View All
                    </Button>
                  </Box>
                </GlassContainer>

                {/* Floating Bottom Badge */}
                <Float distance={10} duration={6} delay={1.5} style={{ position: 'absolute', bottom: -20, right: -15, zIndex: 10 }}>
                  <Paper
                    elevation={0}
                    sx={{
                      p: 1.5,
                      px: 2,
                      borderRadius: 3,
                      display: 'flex',
                      alignItems: 'center',
                      gap: 1.2,
                      border: '1px solid rgba(108, 99, 255, 0.4)',
                      bgcolor: mode === 'dark' ? 'rgba(26, 26, 46, 0.95)' : 'rgba(255, 255, 255, 0.95)',
                      backdropFilter: 'blur(12px)',
                      boxShadow: '0 8px 30px rgba(0,0,0,0.12)',
                    }}
                  >
                    <Box
                      sx={{
                        width: 32,
                        height: 32,
                        borderRadius: 2,
                        bgcolor: 'rgba(108, 99, 255, 0.15)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                      }}
                    >
                      <SecurityRoundedIcon sx={{ color: '#6C63FF', fontSize: 20 }} />
                    </Box>
                    <Box>
                      <Typography variant="caption" fontWeight={800} sx={{ display: 'block' }}>
                        Razorpay Escrow
                      </Typography>
                      <Typography variant="caption" color="text.secondary" fontSize={11}>
                        Pay after job completion
                      </Typography>
                    </Box>
                  </Paper>
                </Float>

              </Box>
            </Grid>

          </Grid>
        </Container>
      </Box>

      {/* 3. Live Metrics Strip */}
      <Box
        sx={{
          py: 4,
          bgcolor: mode === 'dark' ? 'rgba(26, 26, 46, 0.6)' : 'rgba(108, 99, 255, 0.04)',
          borderTop: '1px solid',
          borderBottom: '1px solid',
          borderColor: 'divider',
        }}
      >
        <Container maxWidth="lg">
          <Grid container spacing={3}>
            {stats.map((item, idx) => (
              <Grid item xs={6} md={3} key={item.label}>
                <Box
                  component={motion.div}
                  {...fadeUp(idx * 0.08)}
                  sx={{ textAlign: 'center', px: 1 }}
                >
                  <Typography
                    variant="h4"
                    fontWeight={900}
                    sx={{
                      background: 'linear-gradient(135deg, #6C63FF 0%, #FF6584 100%)',
                      WebkitBackgroundClip: 'text',
                      WebkitTextFillColor: 'transparent',
                      mb: 0.5,
                      letterSpacing: -0.5,
                    }}
                  >
                    {item.value}
                  </Typography>
                  <Typography variant="subtitle2" fontWeight={800}>
                    {item.label}
                  </Typography>
                  <Typography variant="caption" color="text.secondary" sx={{ display: 'block', mt: 0.2 }}>
                    {item.sub}
                  </Typography>
                </Box>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>

      {/* 4. Popular Services Grid */}
      <Box sx={{ py: { xs: 9, md: 12 }, position: 'relative' }}>
        <Container maxWidth="lg">
          
          <Box component={motion.div} {...fadeUp()} sx={{ textAlign: 'center', mb: 6 }}>
            <Chip
              label="Curated Solutions"
              size="small"
              sx={{
                mb: 1.5,
                fontWeight: 700,
                color: 'primary.main',
                bgcolor: 'rgba(108, 99, 255, 0.1)',
                borderRadius: 2,
              }}
            />
            <Typography variant="h3" fontWeight={900} sx={{ letterSpacing: -0.8, mb: 1.5 }}>
              Popular Doorstep Services
            </Typography>
            <Typography color="text.secondary" sx={{ fontSize: 16, maxWidth: 550, mx: 'auto' }}>
              From emergency repairs to seasonal upkeep, get skilled professionals dispatched to your doorstep.
            </Typography>
          </Box>

          <Grid container spacing={3}>
            {services.map((s) => (
              <Grid item xs={12} sm={6} md={4} key={s.name}>
                <GlowCard
                  hoverGlow={`${s.color}40`}
                  onClick={() => navigate('/login')}
                  sx={{ cursor: 'pointer', height: '100%', display: 'flex', flexDirection: 'column' }}
                >
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
                    <Box
                      sx={{
                        width: 58,
                        height: 58,
                        borderRadius: 3,
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        fontSize: 32,
                        bgcolor: `${s.color}15`,
                        border: `1px solid ${s.color}30`,
                      }}
                    >
                      {s.icon}
                    </Box>
                    <Chip
                      label={s.tag}
                      size="small"
                      sx={{
                        fontWeight: 700,
                        fontSize: 11,
                        bgcolor: `${s.color}18`,
                        color: s.color,
                        borderRadius: 2,
                      }}
                    />
                  </Box>

                  <Typography variant="h6" fontWeight={800} sx={{ mb: 1 }}>
                    {s.name}
                  </Typography>

                  <Typography variant="body2" color="text.secondary" sx={{ mb: 3, flexGrow: 1, lineHeight: 1.6 }}>
                    {s.desc}
                  </Typography>

                  <Divider sx={{ my: 2 }} />

                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                    <Box>
                      <Typography variant="caption" color="text.secondary" sx={{ display: 'block' }}>
                        Starts from
                      </Typography>
                      <Typography variant="subtitle1" fontWeight={900} color="primary.main">
                        {s.price}
                      </Typography>
                    </Box>

                    <Button
                      variant="outlined"
                      size="small"
                      endIcon={<ArrowForwardRoundedIcon sx={{ fontSize: 16 }} />}
                      sx={{ borderRadius: 2, textTransform: 'none', fontWeight: 700 }}
                    >
                      Book
                    </Button>
                  </Box>
                </GlowCard>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>

      {/* 5. How It Works Section */}
      <Box
        sx={{
          py: { xs: 9, md: 12 },
          bgcolor: mode === 'dark' ? 'rgba(26, 26, 46, 0.4)' : 'rgba(108, 99, 255, 0.03)',
          borderTop: '1px solid',
          borderBottom: '1px solid',
          borderColor: 'divider',
          position: 'relative',
        }}
      >
        <Container maxWidth="lg">
          <Box component={motion.div} {...fadeUp()} sx={{ textAlign: 'center', mb: 8 }}>
            <Chip
              label="Smooth Workflow"
              size="small"
              sx={{
                mb: 1.5,
                fontWeight: 700,
                color: '#FF6584',
                bgcolor: 'rgba(255, 101, 132, 0.1)',
                borderRadius: 2,
              }}
            />
            <Typography variant="h3" fontWeight={900} sx={{ letterSpacing: -0.8, mb: 1.5 }}>
              How SERVICEiT Works
            </Typography>
            <Typography color="text.secondary" sx={{ fontSize: 16, maxWidth: 540, mx: 'auto' }}>
              Four simple steps from booking to hassle-free doorstep service delivery.
            </Typography>
          </Box>

          <Grid container spacing={3.5}>
            {workflowSteps.map((step, idx) => (
              <Grid item xs={12} sm={6} md={3} key={step.step}>
                <Box
                  component={motion.div}
                  {...fadeUp(idx * 0.1)}
                  whileHover={{ y: -6 }}
                  sx={{
                    p: 3,
                    borderRadius: 4,
                    bgcolor: 'background.paper',
                    border: '1px solid',
                    borderColor: 'divider',
                    boxShadow: '0 6px 25px rgba(0,0,0,0.03)',
                    height: '100%',
                    position: 'relative',
                    transition: 'all 0.25s ease',
                    '&:hover': {
                      borderColor: 'primary.main',
                      boxShadow: '0 12px 35px rgba(108, 99, 255, 0.15)',
                    },
                  }}
                >
                  <Typography
                    variant="h2"
                    fontWeight={900}
                    sx={{
                      position: 'absolute',
                      top: 15,
                      right: 20,
                      opacity: mode === 'dark' ? 0.08 : 0.06,
                      fontSize: 56,
                      letterSpacing: -2,
                    }}
                  >
                    {step.step}
                  </Typography>

                  <Box
                    sx={{
                      width: 50,
                      height: 50,
                      borderRadius: 3,
                      bgcolor: mode === 'dark' ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.04)',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      mb: 2.5,
                    }}
                  >
                    {step.icon}
                  </Box>

                  <Typography variant="h6" fontWeight={800} sx={{ mb: 1 }}>
                    {step.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary" sx={{ lineHeight: 1.6 }}>
                    {step.desc}
                  </Typography>
                </Box>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>

      {/* 6. Why Choose Us (Features Grid) */}
      <Box sx={{ py: { xs: 9, md: 12 } }}>
        <Container maxWidth="lg">
          <Box component={motion.div} {...fadeUp()} sx={{ textAlign: 'center', mb: 8 }}>
            <Chip
              label="Trusted Standard"
              size="small"
              sx={{
                mb: 1.5,
                fontWeight: 700,
                color: '#43E97B',
                bgcolor: 'rgba(67, 233, 123, 0.12)',
                borderRadius: 2,
              }}
            />
            <Typography variant="h3" fontWeight={900} sx={{ letterSpacing: -0.8, mb: 1.5 }}>
              Why Homeowners Trust Us
            </Typography>
            <Typography color="text.secondary" sx={{ fontSize: 16, maxWidth: 540, mx: 'auto' }}>
              We set the gold standard in technician vetting, consumer security, and service guarantees.
            </Typography>
          </Box>

          <Grid container spacing={3}>
            {features.map((f) => (
              <Grid item xs={12} sm={6} md={3} key={f.title}>
                <GlowCard sx={{ height: '100%', p: 3 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start', mb: 2.5 }}>
                    <Box
                      sx={{
                        width: 54,
                        height: 54,
                        borderRadius: 3,
                        bgcolor: mode === 'dark' ? 'rgba(255,255,255,0.05)' : 'rgba(0,0,0,0.04)',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                      }}
                    >
                      {f.icon}
                    </Box>
                    <Chip
                      label={f.badge}
                      size="small"
                      sx={{
                        fontSize: 10,
                        fontWeight: 800,
                        borderRadius: 1.5,
                        bgcolor: 'rgba(108, 99, 255, 0.1)',
                        color: 'primary.main',
                      }}
                    />
                  </Box>

                  <Typography variant="h6" fontWeight={800} sx={{ mb: 1.2, fontSize: 18 }}>
                    {f.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary" sx={{ lineHeight: 1.6 }}>
                    {f.desc}
                  </Typography>
                </GlowCard>
              </Grid>
            ))}
          </Grid>
        </Container>
      </Box>

      {/* 7. Frequently Asked Questions (Accordion with Motion) */}
      <Box
        sx={{
          py: { xs: 8, md: 10 },
          bgcolor: mode === 'dark' ? 'rgba(26, 26, 46, 0.3)' : 'rgba(108, 99, 255, 0.02)',
          borderTop: '1px solid',
          borderColor: 'divider',
        }}
      >
        <Container maxWidth="md">
          <Box component={motion.div} {...fadeUp()} sx={{ textAlign: 'center', mb: 6 }}>
            <Typography variant="h4" fontWeight={900} sx={{ letterSpacing: -0.5, mb: 1 }}>
              Frequently Asked Questions
            </Typography>
            <Typography color="text.secondary" sx={{ fontSize: 15 }}>
              Everything you need to know about bookings, cancellations, and safety.
            </Typography>
          </Box>

          <Stack spacing={2}>
            {faqs.map((faq) => (
              <Accordion
                key={faq.q}
                elevation={0}
                sx={{
                  borderRadius: '16px !important',
                  border: '1px solid',
                  borderColor: 'divider',
                  bgcolor: 'background.paper',
                  '&:before': { display: 'none' },
                  boxShadow: '0 4px 15px rgba(0,0,0,0.02)',
                }}
              >
                <AccordionSummary expandIcon={<ExpandMoreRoundedIcon sx={{ color: 'primary.main' }} />}>
                  <Typography fontWeight={700} sx={{ fontSize: 16 }}>
                    {faq.q}
                  </Typography>
                </AccordionSummary>
                <AccordionDetails sx={{ pt: 0 }}>
                  <Typography variant="body2" color="text.secondary" sx={{ lineHeight: 1.7 }}>
                    {faq.a}
                  </Typography>
                </AccordionDetails>
              </Accordion>
            ))}
          </Stack>
        </Container>
      </Box>

      {/* 8. Modern Cosmic Gradient CTA Card with Haikei Style */}
      <Box sx={{ py: { xs: 8, md: 11 } }}>
        <Container maxWidth="lg">
          <Box component={motion.div} {...fadeUp(0.1)}>
            <Paper
              elevation={0}
              sx={{
                p: { xs: 5, sm: 7, md: 8 },
                borderRadius: 6,
                background: 'linear-gradient(135deg, #6C63FF 0%, #3F37C9 50%, #FF6584 100%)',
                color: '#fff',
                textAlign: 'center',
                position: 'relative',
                overflow: 'hidden',
                boxShadow: '0 25px 60px rgba(108, 99, 255, 0.35)',
              }}
            >
              {/* Floating ambient glow lights */}
              <Box
                sx={{
                  position: 'absolute',
                  top: -100,
                  right: -100,
                  width: 380,
                  height: 380,
                  borderRadius: '50%',
                  background: 'rgba(255, 255, 255, 0.18)',
                  filter: 'blur(50px)',
                  pointerEvents: 'none',
                }}
              />
              <Box
                sx={{
                  position: 'absolute',
                  bottom: -100,
                  left: -100,
                  width: 380,
                  height: 380,
                  borderRadius: '50%',
                  background: 'rgba(0, 0, 0, 0.25)',
                  filter: 'blur(50px)',
                  pointerEvents: 'none',
                }}
              />

              <Box sx={{ position: 'relative', zIndex: 1, maxWidth: 680, mx: 'auto' }}>
                <Chip
                  icon={<FlashOnRoundedIcon sx={{ fontSize: 16, color: '#fff !important' }} />}
                  label="Fast, Reliable & Guaranteed"
                  size="small"
                  sx={{
                    mb: 3,
                    bgcolor: 'rgba(255, 255, 255, 0.2)',
                    color: '#fff',
                    fontWeight: 800,
                    borderRadius: 2,
                    backdropFilter: 'blur(10px)',
                  }}
                />

                <Typography
                  variant="h2"
                  fontWeight={900}
                  sx={{
                    mb: 2.5,
                    fontSize: { xs: '2rem', sm: '2.8rem', md: '3.4rem' },
                    letterSpacing: -1,
                    lineHeight: 1.15,
                  }}
                >
                  Ready to Book Your First Service?
                </Typography>

                <Typography
                  variant="body1"
                  sx={{
                    mb: 4.5,
                    fontSize: { xs: 16, sm: 18 },
                    opacity: 0.92,
                    lineHeight: 1.6,
                    textAlign: 'center',
                  }}
                >
                  Join thousands of satisfied customers across Pune. Book certified technicians in under 60 seconds with total peace of mind.
                </Typography>

                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2.5} justifyContent="center" alignItems="center">
                  <Button
                    variant="contained"
                    size="large"
                    onClick={() => navigate('/register')}
                    endIcon={<ArrowForwardRoundedIcon />}
                    sx={{
                      width: { xs: '100%', sm: 'auto' },
                      minWidth: 220,
                      bgcolor: '#fff',
                      color: '#6C63FF',
                      fontWeight: 800,
                      px: 4.5,
                      py: 1.8,
                      borderRadius: 3.5,
                      fontSize: 16,
                      boxShadow: '0 8px 30px rgba(0,0,0,0.18)',
                      '&:hover': {
                        bgcolor: '#f5f5ff',
                        transform: 'translateY(-3px)',
                        boxShadow: '0 12px 35px rgba(0,0,0,0.25)',
                      },
                      transition: 'all 0.25s ease',
                    }}
                  >
                    Get Started Free
                  </Button>

                  <Button
                    variant="outlined"
                    size="large"
                    onClick={() => navigate('/login')}
                    sx={{
                      width: { xs: '100%', sm: 'auto' },
                      minWidth: 160,
                      color: '#fff',
                      borderColor: 'rgba(255, 255, 255, 0.6)',
                      fontWeight: 700,
                      px: 4,
                      py: 1.8,
                      borderRadius: 3.5,
                      fontSize: 16,
                      backdropFilter: 'blur(10px)',
                      '&:hover': {
                        bgcolor: 'rgba(255, 255, 255, 0.15)',
                        borderColor: '#fff',
                        transform: 'translateY(-3px)',
                      },
                      transition: 'all 0.25s ease',
                    }}
                  >
                    Sign In
                  </Button>
                </Stack>
              </Box>
            </Paper>
          </Box>
        </Container>
      </Box>

      {/* 9. Minimalist Modern Footer */}
      <Box
        sx={{
          bgcolor: 'background.paper',
          py: 4,
          textAlign: 'center',
          borderTop: '1px solid',
          borderColor: 'divider',
          mt: 'auto',
        }}
      >
        <Container maxWidth="lg">
          <Typography variant="body2" color="text.secondary" fontWeight={500}>
            © 2026 <strong>SERVICEiT</strong>. All rights reserved. Built with Spring Boot, React, and Modern Motion UI.
          </Typography>
        </Container>
      </Box>

    </Box>
  );
};

export default LandingPage;
