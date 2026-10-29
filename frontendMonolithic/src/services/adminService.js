import api from './api';

// =============================================
// Service Catalog Management (Admin CRUD)
// =============================================

// GET /api/admin/services
export const adminGetAllServices = () => api.get('/admin/services');

// GET /api/admin/services/{serviceId}
export const adminGetServiceById = (id) => api.get(`/admin/services/${id}`);

// POST /api/admin/services
export const adminCreateService = (data) => api.post('/admin/services', data);

// PUT /api/admin/services/{serviceId}
export const adminUpdateService = (id, data) =>
  api.put(`/admin/services/${id}`, data);

// DELETE /api/admin/services/{serviceId}
export const adminDeleteService = (id) => api.delete(`/admin/services/${id}`);

// =============================================
// Provider Management
// =============================================

// GET /api/admin/providers
export const adminGetAllProviders = () => api.get('/admin/providers');

// GET /api/admin/providers/pending
export const adminGetPendingProviders = () =>
  api.get('/admin/providers/pending');

// PUT /api/admin/providers/{userId}/approve
export const adminApproveProvider = (userId) =>
  api.put(`/admin/providers/${userId}/approve`);

// PUT /api/admin/providers/{userId}/reject
export const adminRejectProvider = (userId) =>
  api.put(`/admin/providers/${userId}/reject`);

// =============================================
// Consumer Management
// =============================================

// GET /api/admin/consumers
export const adminGetAllConsumers = () => api.get('/admin/consumers');

// =============================================
// Booking Management
// =============================================

// GET /api/admin/bookings
export const adminGetAllBookings = () => api.get('/admin/bookings');

// =============================================
// Payment Management
// =============================================

// GET /api/admin/payments
export const adminGetAllPayments = () => api.get('/admin/payments');

// =============================================
// User Management
// =============================================

// GET /api/users
export const adminGetAllUsers = () => api.get('/users');

// DELETE /api/users/{userId}
export const adminDeleteUser = (userId) => api.delete(`/users/${userId}`);
