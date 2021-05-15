export {getRequest, postRequest, putRequest, deleteRequest};

const LOCAL_HOST = 'http://localhost:8080'

const orThrow = (result) => {
    if (result.error) {
        console.error(result.error)
        new Error(result.error)
    }
    return result;
}

async function getRequest(url = '') {
    let result = await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'get',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include'
    })
        .then(response =>
            response.json()
        );

    return orThrow(result)
}

async function postRequest(url = '', body = {}) {
    let result = await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'post',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(body)
    })
        .then(response =>
            response.json()
        );

    return orThrow(result)
}

async function putRequest(url = '', body = {}) {
    let result = await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'put',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(body)
    })
        .then(response =>
            response.json()
        );

    return orThrow(result)
}


async function deleteRequest(url = '', body = {}) {
    let result = await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'delete',
        headers: {
            'Content-Type': 'application/json'
        },
        credentials: 'include',
        body: JSON.stringify(body)
    })
        .then(response =>
            response.json()
        );

    return orThrow(result)
}