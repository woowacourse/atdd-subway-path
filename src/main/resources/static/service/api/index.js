import {ERROR_MESSAGE} from "../utils/constants.js";

const METHOD = {
  PUT() {
    return {
      method: 'PUT'
    }
  },
  DELETE() {
    return {
      method: 'DELETE'
    }
  },
  POST(data) {
    return {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  }
}

const api = (() => {
  const request = (uri, config) => fetch(uri, config).then(response => {
    if (!response.ok) {
      response.json().then(data => {
        const error = data.errorMessage;
        if (error === "INACCESSIBLE_STATION") {
          alert(ERROR_MESSAGE.INACCESSIBLE_STATION);
        }
        if (error === "NON_EXISTENT_DATA") {
          alert(ERROR_MESSAGE.NON_EXISTENT_DATA);
        }
        if (error === "SYSTEM_ERROR") {
          alert(ERROR_MESSAGE.SYSTEM_ERROR);
        }
      });
      return;
    }
    return response.json()
  });

  const line = {
    getAll() {
      return request(`/lineDetails`)
    }
  }

  const path = {
    findPath(params) {
      return request(`/path?source=${params.source}&target=${params.target}`)
    }
  }

  return {
    line,
    path
  }
})();

export default api
