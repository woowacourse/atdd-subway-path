const METHOD = {
	PUT() {
		return {
			method: 'PUT',
		};
	},
	DELETE() {
		return {
			method: 'DELETE',
		};
	},
	POST(data) {
		return {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json',
			},
			body: JSON.stringify({
				...data,
			}),
		};
	},
};

const api = (() => {
	const request = (uri, config) => fetch(uri, config);
	const requestWithJsonData = (uri, config) =>
		fetch(uri, config).then((data) => data.json());

	const line = {
		getAll() {
			return request(`/api/lines/detail`);
		},
		getAllDetail() {
			return requestWithJsonData(`/api/lines/detail`);
		},
	};

	const path = {
		find(params) {
			return request(
				`/api/paths?source=${params.source}&target=${params.target}`,
			);
		},
	};

	const station = {
		getAll() {
			return requestWithJsonData(`/api/stations`);
		},
	};

	return {
		line,
		path,
		station,
	};
})();

export default api;
