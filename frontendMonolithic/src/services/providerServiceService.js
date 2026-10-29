import api from './api';

// GET /api/provider-services/search?service=...&city=...&sort=...
export const searchProviderServices = (params) =>
  api.get('/provider-services/search', { params });

// GET /api/provider-services/{providerServiceId}
export const getProviderServiceById = (id) =>
  api.get(`/provider-services/${id}`);

// GET /api/provider-services/provider/{providerId}
export const getServicesByProvider = (providerId) =>
  api.get(`/provider-services/provider/${providerId}`);

// POST /api/provider-services (PROVIDER)
export const createProviderService = (data) =>
  api.post('/provider-services', data);

// PUT /api/provider-services/{providerServiceId} (PROVIDER)
export const updateProviderService = (id, data) =>
  api.put(`/provider-services/${id}`, data);

// DELETE /api/provider-services/{providerServiceId} (PROVIDER)
export const deleteProviderService = (id) =>
  api.delete(`/provider-services/${id}`);
