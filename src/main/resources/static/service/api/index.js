const api = (() => {
  const request = (uri, config) => fetch(uri, config)
  const requestWithJsonData = (uri, config) => fetch(uri, config)
  .then(async data => {
    if (!data.ok) {
      const error = await data.json();
      throw new Error(error["errorMessage"]);
    }
    return data;
  })
  .then(data => data.json())

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

  return {
    line,
    path
  }
})()

export default api
