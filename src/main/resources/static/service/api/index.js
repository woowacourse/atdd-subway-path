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
        alert(ERROR_MESSAGE[error]);
      });
      return;
    }
    return response.json()
  });

  const line = {
    getAll() {
      return request(`/line-details`)
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
