import apiService from "../index";

const BASE_URL = "lines";

export const lineApiService = {
  get(id) {
    return apiService.get(`${BASE_URL}/${id}`);
  },
  getAll() {
    return apiService.get(`${BASE_URL}`);
  },
  post(data) {
    return apiService.post(`${BASE_URL}`, data);
  },
  update(id, data) {
    return apiService.update(`${BASE_URL}/${id}`, data, false);
  },
  delete(id) {
    return apiService.delete(`${BASE_URL}/${id}`, false);
  },
  createSection({ lineId, section }) {
    return apiService.post(`${BASE_URL}/${lineId}/sections`, section, false);
  },
  deleteSection({ lineId, stationId }) {
    return apiService.delete(
      `${BASE_URL}/${lineId}/sections?stationId=${stationId}`,
      false
    );
  },
};
