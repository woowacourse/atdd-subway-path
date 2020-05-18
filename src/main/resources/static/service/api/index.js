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
  const request = (uri, config) => fetch(uri, config).then(data => data.json())

  const line = {
    getAll() {
      return request(`/lines/detail`)
    }
  }

  const path = {
    find(data) {
      return fetch(`/paths?startStationName=${data.startStationName}&targetStationName=${data.targetStationName}&type=${data.type}`);
    }
  }

  return {
    line,
    path
  }
})()

export default api
