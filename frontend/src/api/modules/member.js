import apiService from "../index";

const BASE_URL = "members";

export const memberApiService = {
  get() {
    return apiService.get(`${BASE_URL}/me`);
  },
  post(data) {
    return apiService.post(`${BASE_URL}`, data, false);
  },
  update(data) {
    return apiService.update(`${BASE_URL}/me`, data, false);
  },
  delete() {
    return apiService.delete(`${BASE_URL}/me`, false);
  },
};
