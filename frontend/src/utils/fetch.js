export {getFetch, postFetch, deleteFetch, putFetch}

function getFetch(url) {
    return fetch(`${url}`)
        .then(data => {
            if (!data.ok) {
                throw new Error(data.status);
            }
            return data.json()
        }).catch((error) => {
            console.log(error)
        });
}

function postFetch(url, body = {}) {
    return fetch(`${url}`, {
        method: "post",
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(data => {
        if (!data.ok) {
            throw new Error(data.status);
        }
        return data.json()
    }).catch((error) => {
        console.log(error)
    });
}

function putFetch(url, body = {}) {
    return fetch(`${url}`, {
        method: "put",
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(data => {
        if (!data.ok) {
            throw new Error(data.status);
        }
        return data.json()
    }).catch((error) => {
        console.log(error)
    });
}

function deleteFetch(url) {
    return fetch(`${url}`, {
        method: "delete",
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(data => {
        if (!data.ok) {
            throw new Error(data.status);
        }
    }).catch((error) => {
        console.log(error)
    });
}