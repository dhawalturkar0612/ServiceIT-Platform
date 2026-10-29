import React from 'react';
import { Box } from '@mui/material';
import { motion } from 'framer-motion';

// 1. Haikei Ambient Floating Blobs (Creates modern mesh gradient background)
export const HaikeiAmbientBlobs = ({ sx = {} }) => (
  <Box
    sx={{
      position: 'absolute',
      inset: 0,
      overflow: 'hidden',
      pointerEvents: 'none',
      zIndex: 0,
      ...sx,
    }}
  >
    {/* Purple Primary Orb */}
    <Box
      component={motion.div}
      animate={{
        x: [0, 40, 0],
        y: [0, -30, 0],
        scale: [1, 1.15, 1],
      }}
      transition={{ duration: 12, repeat: Infinity, ease: 'easeInOut' }}
      sx={{
        position: 'absolute',
        top: '-10%',
        left: '-5%',
        width: { xs: 320, md: 550 },
        height: { xs: 320, md: 550 },
        borderRadius: '50%',
        background: 'radial-gradient(circle, rgba(108, 99, 255, 0.28) 0%, rgba(108, 99, 255, 0) 70%)',
        filter: 'blur(60px)',
      }}
    />

    {/* Pink/Coral Secondary Orb */}
    <Box
      component={motion.div}
      animate={{
        x: [0, -35, 0],
        y: [0, 45, 0],
        scale: [1, 1.2, 1],
      }}
      transition={{ duration: 14, repeat: Infinity, ease: 'easeInOut', delay: 1 }}
      sx={{
        position: 'absolute',
        top: '20%',
        right: '-8%',
        width: { xs: 300, md: 500 },
        height: { xs: 300, md: 500 },
        borderRadius: '50%',
        background: 'radial-gradient(circle, rgba(255, 101, 132, 0.22) 0%, rgba(255, 101, 132, 0) 70%)',
        filter: 'blur(70px)',
      }}
    />

    {/* Emerald/Cyan Fresh Glow */}
    <Box
      component={motion.div}
      animate={{
        x: [0, 25, 0],
        y: [0, 25, 0],
        scale: [1, 1.1, 1],
      }}
      transition={{ duration: 16, repeat: Infinity, ease: 'easeInOut', delay: 2 }}
      sx={{
        position: 'absolute',
        bottom: '5%',
        left: '25%',
        width: { xs: 260, md: 450 },
        height: { xs: 260, md: 450 },
        borderRadius: '50%',
        background: 'radial-gradient(circle, rgba(67, 233, 123, 0.16) 0%, rgba(67, 233, 123, 0) 70%)',
        filter: 'blur(65px)',
      }}
    />
  </Box>
);

// 2. Haikei Smooth Wave Divider (Top)
export const HaikeiWaveTop = ({ fill = 'currentColor', height = 60, opacity = 1, sx = {} }) => (
  <Box
    sx={{
      width: '100%',
      overflow: 'hidden',
      lineHeight: 0,
      transform: 'rotate(180deg)',
      ...sx,
    }}
  >
    <svg
      viewBox="0 0 1200 120"
      preserveAspectRatio="none"
      style={{
        position: 'relative',
        display: 'block',
        width: 'calc(100% + 1.3px)',
        height,
      }}
    >
      <path
        d="M0,0 C150,90 350,-40 500,45 C650,130 900,10 1200,50 L1200,120 L0,120 Z"
        fill={fill}
        opacity={opacity}
      />
    </svg>
  </Box>
);

// 3. Haikei Smooth Wave Divider (Bottom)
export const HaikeiWaveBottom = ({ fill = 'currentColor', height = 60, opacity = 1, sx = {} }) => (
  <Box
    sx={{
      width: '100%',
      overflow: 'hidden',
      lineHeight: 0,
      ...sx,
    }}
  >
    <svg
      viewBox="0 0 1200 120"
      preserveAspectRatio="none"
      style={{
        position: 'relative',
        display: 'block',
        width: 'calc(100% + 1.3px)',
        height,
      }}
    >
      <path
        d="M0,0 C150,90 350,-40 500,45 C650,130 900,10 1200,50 L1200,120 L0,120 Z"
        fill={fill}
        opacity={opacity}
      />
    </svg>
  </Box>
);

// 4. GlassContainer - High quality frosted glass card
export const GlassContainer = ({ children, sx = {}, ...props }) => (
  <Box
    sx={{
      backdropFilter: 'blur(20px)',
      WebkitBackdropFilter: 'blur(20px)',
      background: (theme) =>
        theme.palette.mode === 'dark'
          ? 'rgba(26, 26, 46, 0.75)'
          : 'rgba(255, 255, 255, 0.85)',
      border: '1px solid',
      borderColor: (theme) =>
        theme.palette.mode === 'dark'
          ? 'rgba(255, 255, 255, 0.08)'
          : 'rgba(108, 99, 255, 0.12)',
      boxShadow: (theme) =>
        theme.palette.mode === 'dark'
          ? '0 12px 40px rgba(0, 0, 0, 0.5)'
          : '0 12px 40px rgba(108, 99, 255, 0.08)',
      borderRadius: 4,
      ...sx,
    }}
    {...props}
  >
    {children}
  </Box>
);
