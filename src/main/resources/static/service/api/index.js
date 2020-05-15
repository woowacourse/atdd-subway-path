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
};

const api = (() => {
  const request = (uri, config) => fetch(uri, config)
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => {
    if (data.ok) {
      return data.json();
    }
    return data.json().then(error => {
      throw new Error(error.message);
    })
  });

  const station = {
    show() {
      return requestWithJsonData(`api/stations`);
    },
  };

  const line = {
    getAll() {
      return request(`api/lines/detail`)
    },
    getAllDetail() {
      return requestWithJsonData(`api/lines/detail`)
    }
  };

  const path = {
    find(params) {
      return requestWithJsonData(`api/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
    }
  };

  return {
    line,
    path,
    station
  }
})();

export default api
