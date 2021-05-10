import apiService from "../index";

const BASE_URL = "stations";

export const stationApiService = {
  get(id) {
    return apiService.get(`${BASE_URL}/${id}`);
  },
  getAll() {
    return apiService.get(`${BASE_URL}`);
  },
  post(data) {
    return apiService.post(`${BASE_URL}`, data);
  },
  delete(id) {
    return apiService.delete(`${BASE_URL}/${id}`, false);
  },
};
