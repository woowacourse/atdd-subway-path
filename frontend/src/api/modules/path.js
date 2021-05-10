import apiService from "../index";

const BASE_URL = "paths";

export const pathApiService = {
  get({ source, target }) {
    return apiService.get(`${BASE_URL}/?source=${source}&target=${target}`);
  },
};
