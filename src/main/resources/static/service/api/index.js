const METHOD = {
  GET() {
    return {
      method: 'GET'
    }
  },
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
  const request = (uri, config) => fetch(uri, config)
  const requestWithJsonData = (uri, config) => fetch(uri, config).then(async data => {
    if (!data.ok) {
      // alert(await data.text());
      throw new Error(await data.text())
    }
    return data.json()
  });

  const line = {
    getAll() {
      return request(`/lines/detail`)
    },
    getAllDetail() {
      return requestWithJsonData(`/lines/detail`)
    }
  }

  const path = {
    find(params) {
      return requestWithJsonData(`/paths?source=${params.source}&target=${params.target}&type=${params.type}`)
    }
  }

  const station = {
    getAll(){
      return requestWithJsonData(`/stations`, METHOD.GET());
    }
  }

  return {
    line,
    path,
    station
  }
})()

export default api
