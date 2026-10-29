import api from './api';

// POST /api/bookings (CONSUMER)
export const createBooking = (data) => api.post('/bookings', data);

// GET /api/bookings/my-bookings (CONSUMER)
export const getMyBookings = () => api.get('/bookings/my-bookings');

// GET /api/bookings/provider (PROVIDER)
export const getProviderBookings = () => api.get('/bookings/provider');

// GET /api/bookings/{bookingId} (ADMIN/CONSUMER/PROVIDER)
export const getBookingById = (id) => api.get(`/bookings/${id}`);

// PUT /api/bookings/{bookingId}/cancel (CONSUMER)
export const cancelBooking = (id) => api.put(`/bookings/${id}/cancel`);

// PUT /api/bookings/{bookingId}/complete (PROVIDER/ADMIN)
export const completeBooking = (id) => api.put(`/bookings/${id}/complete`);

// GET /api/bookings (ADMIN)
export const getAllBookings = () => api.get('/bookings');
