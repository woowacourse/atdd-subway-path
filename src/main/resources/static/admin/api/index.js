const METHOD = {
  PUT(data) {
    return {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
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
  const request = (uri, config) => fetch(uri, config)
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(data => data.json())

  const station = {
    get(id) {
      return request(`/stations/${id}`)
    },
    getAll() {
      return request(`/stations`)
    },
    create(data) {
      return request(`/stations`, METHOD.POST(data))
    },
    update(data, id) {
      return request(`/stations/${id}`, METHOD.PUT(data))
    },
    delete(id) {
      return request(`/stations/${id}`, METHOD.DELETE())
    }
  }

  const line = {
    get(id) {
      return request(`/lines/${id}`)
    },
    getAll() {
      return request(`/lines`)
    },
    getAllDetail() {
      return request(`/lines/detail`)
    },
    addLineStation(lineId, lineStationCreateRequestView) {
      return request(`/lines/${lineId}/stations`, METHOD.POST(lineStationCreateRequestView))
    },
    create(data) {
      return request(`/lines`, METHOD.POST(data))
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

  return {
    station,
    line
  }
})()

export default api
