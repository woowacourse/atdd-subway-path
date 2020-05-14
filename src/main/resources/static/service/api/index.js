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
  const request = (uri, config) => fetch(uri, config);
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json());

  const line = {
    get(id) {
      return requestWithJsonData(`/lines/${id}`)
    },
    getAll() {
      return requestWithJsonData(`/lines`)
    },
    getAllDetail() {
      return requestWithJsonData(`/lines/detail`)
    },
    addLineStation(lineId, lineStationCreateRequestView) {
      return request(`/lines/${lineId}/stations`, METHOD.POST(lineStationCreateRequestView))
    },
    create(data) {
      return requestWithJsonData(`/lines`, METHOD.POST(data))
    },
    update(id, data) {
      return request(`/lines/${id}`, METHOD.PUT(data))
    },
    deleteLineStation(lineId, stationId) {
      return request(`/lines/${lineId}/stations/${stationId}`, METHOD.DELETE())
    },
    delete(id) {
      return request(`/lines/${id}`, METHOD.DELETE())
    }
  }

  const path = {
    find(params) {
      return requestWithJsonData(`/lines/path/${params.source}/${params.target}`);
    }
  }

  return {
    line,
    path
  }
})()

export default api
