import api from './api';

// GET /api/admin/logs (ADMIN only)
export const getAllAuditLogs = () => api.get('/admin/logs');

// GET /api/admin/logs/recent (ADMIN only)
export const getRecentAuditLogs = () => api.get('/admin/logs/recent');
