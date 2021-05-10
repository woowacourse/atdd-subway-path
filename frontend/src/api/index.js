const DEFAULT_OPTION = {
  headers: {
    "Content-Type": "application/json",
    Authorization: `Bearer ${localStorage.getItem("token")}` || "",
  },
};

const BASE_URL = "http://localhost:8080";

const request = async (url, option = {}, hasResponseData = true) => {
  try {
    const response = await fetch(url, option);
    if (!response.ok) {
      throw new Error(`${response.status}`);
    }
    if (hasResponseData) {
      return response.json();
    }
  } catch (e) {
    throw new Error(e);
  }
};

const apiService = {
  get(url) {
    return request(`${BASE_URL}/${url}`, {
      method: "GET",
      ...DEFAULT_OPTION,
    });
  },
  post(url, data, hasResponseData) {
    return request(
      `${BASE_URL}/${url}`,
      {
        method: "POST",
        ...DEFAULT_OPTION,
        body: JSON.stringify(data),
      },
      hasResponseData
    );
  },
  update(url, data, hasResponseData) {
    return request(
      `${BASE_URL}/${url}`,
      {
        method: "PUT",
        ...DEFAULT_OPTION,
        body: JSON.stringify(data),
      },
      hasResponseData
    );
  },
  delete(url, hasResponseData) {
    return request(
      `${BASE_URL}/${url}`,
      {
        method: "DELETE",
        ...DEFAULT_OPTION,
      },
      hasResponseData
    );
  },
};

export default apiService;
