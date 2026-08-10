import api from './api';

// GET /api/service-providers/me
export const getMyProviderProfile = () => api.get('/service-providers/me');

// POST /api/service-providers
export const createProviderProfile = (data) => api.post('/service-providers', data);

// PUT /api/service-providers/me
export const updateProviderProfile = (data) => api.put('/service-providers/me', data);

// DELETE /api/service-providers/me
export const deleteProviderProfile = () => api.delete('/service-providers/me');

// GET /api/service-providers/{providerId}
export const getProviderById = (providerId) =>
  api.get(`/service-providers/${providerId}`);

// GET /api/service-providers (Admin)
export const getAllProviders = () => api.get('/service-providers');
