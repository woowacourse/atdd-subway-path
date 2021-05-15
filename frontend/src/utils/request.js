export {getRequest, postRequest, putRequest, deleteRequest};

const LOCAL_HOST = 'http://localhost:8080'

function getRequest(url = '') {
    return fetch(`${LOCAL_HOST}/${url}`, {
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include'
    })
        .then(
            response => response.json()
        )
}

async function postRequest(url = '', body = {}) {
    return await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(body)
    })
        .then(
            response => response.json()
        )
}

async function putRequest(url = '', body = {}) {
    return await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(body)
    })
        .then(
            response => response.json()
        )
}


async function deleteRequest(url = '', body = {}) {
    return await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(body)
    })
        .then(
            response => response.json()
        )
}