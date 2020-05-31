const api = (() => {
  const request = (uri, config) => fetch(uri, config);
  const requestWithJsonData = (uri, config) =>
    fetch(uri, config).then((data) => data.json());

  const line = {
    getAll() {
      return request(`/lines/detail`);
    },
    getAllDetail() {
      return requestWithJsonData(`/lines/detail`);
    },
  };

  const path = {
    find(params) {
      return request(
        `/paths?source=${params.source}&target=${params.target}&type=${params.type}`,
      );
    },
  };

  const station = {
    getAll() {
      return requestWithJsonData(`/stations`);
    },
  };

  return {
    line,
    path,
    station,
  };
})();

export default api;
