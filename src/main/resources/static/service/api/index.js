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
  const request = (uri, config) => fetch(uri, config).then(async data => {
    if (!data.ok) {
      alert(await data.text());
    }
    return data.json()
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
