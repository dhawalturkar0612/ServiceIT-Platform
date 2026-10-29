import api from './api';

// GET /api/consumers/me
export const getMyConsumerProfile = () => api.get('/consumers/me');

// POST /api/consumers
export const createConsumerProfile = (data) => api.post('/consumers', data);

// PUT /api/consumers/me
export const updateConsumerProfile = (data) => api.put('/consumers/me', data);

// DELETE /api/consumers/me
export const deleteConsumerProfile = () => api.delete('/consumers/me');
