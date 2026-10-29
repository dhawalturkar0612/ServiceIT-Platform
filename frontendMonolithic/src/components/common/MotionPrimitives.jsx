import React from 'react';
import { motion } from 'framer-motion';
import { Box } from '@mui/material';

// 1. Float Primitive - Infinite gentle floating effect
export const Float = ({ children, delay = 0, duration = 4, distance = 10, style = {}, ...props }) => (
  <motion.div
    animate={{
      y: [-distance / 2, distance / 2, -distance / 2],
      rotate: [-0.5, 0.5, -0.5],
    }}
    transition={{
      duration,
      repeat: Infinity,
      ease: 'easeInOut',
      delay,
    }}
    style={{ display: 'inline-block', ...style }}
    {...props}
  >
    {children}
  </motion.div>
);

// 2. PulseBadge - Glowing pulsing status badge
export const PulseBadge = ({ label, dotColor = '#43E97B', sx = {} }) => (
  <Box
    sx={{
      display: 'inline-flex',
      alignItems: 'center',
      gap: 1.2,
      px: 1.8,
      py: 0.6,
      borderRadius: 99,
      bgcolor: 'rgba(108, 99, 255, 0.08)',
      border: '1px solid rgba(108, 99, 255, 0.2)',
      backdropFilter: 'blur(8px)',
      fontSize: 13,
      fontWeight: 700,
      color: 'text.primary',
      ...sx,
    }}
  >
    <Box sx={{ position: 'relative', width: 9, height: 9 }}>
      <Box
        component={motion.div}
        animate={{ scale: [1, 2.2, 1], opacity: [0.8, 0, 0.8] }}
        transition={{ duration: 1.8, repeat: Infinity, ease: 'easeOut' }}
        sx={{
          position: 'absolute',
          inset: 0,
          borderRadius: '50%',
          bgcolor: dotColor,
        }}
      />
      <Box
        sx={{
          position: 'absolute',
          inset: 0,
          borderRadius: '50%',
          bgcolor: dotColor,
        }}
      />
    </Box>
    <span>{label}</span>
  </Box>
);

// 3. Stagger Container for cascading card reveals
export const staggerContainer = (staggerDelay = 0.08) => ({
  hidden: { opacity: 0 },
  visible: {
    opacity: 1,
    transition: {
      staggerChildren: staggerDelay,
    },
  },
});

export const staggerItem = {
  hidden: { opacity: 0, y: 24 },
  visible: {
    opacity: 1,
    y: 0,
    transition: { duration: 0.5, ease: [0.25, 0.1, 0.25, 1.0] },
  },
};

// 4. GlowCard - Glassmorphic card with dynamic hover glow
export const GlowCard = ({ children, sx = {}, hoverGlow = 'rgba(108, 99, 255, 0.25)', ...props }) => (
  <Box
    component={motion.div}
    whileHover={{ y: -6, transition: { duration: 0.25, ease: 'easeOut' } }}
    whileTap={{ scale: 0.99 }}
    sx={{
      position: 'relative',
      borderRadius: 4,
      p: 3,
      bgcolor: 'background.paper',
      border: '1px solid',
      borderColor: 'divider',
      boxShadow: '0 4px 20px rgba(0,0,0,0.03)',
      transition: 'border-color 0.25s ease, box-shadow 0.25s ease',
      overflow: 'hidden',
      '&:hover': {
        borderColor: 'primary.main',
        boxShadow: `0 14px 35px -8px ${hoverGlow}`,
      },
      ...sx,
    }}
    {...props}
  >
    {children}
  </Box>
);
