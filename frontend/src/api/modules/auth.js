import apiService from "../index";

const BASE_URL = "login/token";

export const authApiService = {
  login(userInfo) {
    return apiService.post(`${BASE_URL}`, userInfo);
  },
};
