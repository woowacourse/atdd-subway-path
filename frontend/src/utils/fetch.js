export {getFetch, postFetch, deleteFetch, putFetch}

function getFetch(url) {
    return fetch(`${url}`).then(data => {
        if (data.status === 400) {
            exceptionHandling(data.json());
        } else if (!data.ok) {
            throw new Error(data.status);
        }
        return data.json()
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
        if (data.status === 400) {
            exceptionHandling(data.json());
        } else if (!data.ok) {
            throw new Error(data.status);
        }
        return data.json()
    })
}

function putFetch(url, body = {}) {
    return fetch(`${url}`, {
        method: "put",
        body: JSON.stringify(body),
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(data => {
        if (data.status === 400) {
            exceptionHandling(data.json());
        } else if (!data.ok) {
            throw new Error(data.status);
        }
        return data.json()
    })
}

function deleteFetch(url) {
    return fetch(`${url}`, {
        method: "delete",
        headers: {
            'Content-Type': 'application/json'
        }
    }).then(data => {
        if (data.status === 400) {
            exceptionHandling(data.json());
        } else if (!data.ok) {
            throw new Error(data.status);
        }
        return data
    })
}

function exceptionHandling(errorPromise) {
    errorPromise.then(data => {
        alert(data.errorMsg);
    })
}