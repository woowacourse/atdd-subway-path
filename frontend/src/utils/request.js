export {getRequest, postRequest, putRequest, deleteRequest};

const LOCAL_HOST = 'http://localhost:8080'
const BEARER = 'Bearer'

const orThrow = (result) => {
    if (result.error) {
        console.error(result.error)
        new Error(result.error)
    }
    return result;
}

async function getRequest(url = '', needToken = false) {
    const headers = {'Content-Type': 'application/json'};
    if (needToken) {
        headers.Authorization = `${BEARER} ${localStorage.getItem('accessToken')}`
    }
    let result = await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'get',
        headers: headers,
        credentials: 'include'
    })
        .then(response =>
            response.text()
        ).then(text => {
                if (text) {
                    return JSON.parse(text)
                } else
                    return {};
            }
        )

    return orThrow(result)
}

async function postRequest(url = '', body = {}, needToken = false) {
    const headers = {'Content-Type': 'application/json'};
    if (needToken) {
        headers.Authorization = `${BEARER} ${localStorage.getItem('accessToken')}`
    }
    let result = await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'post',
        headers: headers,
        credentials: 'include',
        body: JSON.stringify(body)
    })
        .then(response =>
            response.text()
        ).then(text => {
                if (text) {
                    return JSON.parse(text)
                } else
                    return {};
            }
        )

    return orThrow(result)
}

async function putRequest(url = '', body = {}, needToken = false) {
    const headers = {'Content-Type': 'application/json'};
    if (needToken) {
        headers.Authorization = `${BEARER} ${localStorage.getItem('accessToken')}`
    }
    let result = await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'put',
        headers: headers,
        credentials: 'include',
        body: JSON.stringify(body)
    })
        .then(response =>
            response.text()
        ).then(text => {
                if (text) {
                    return JSON.parse(text)
                } else
                    return {};
            }
        )

    return orThrow(result)
}


async function deleteRequest(url = '', body = {}, needToken = false) {
    const headers = {'Content-Type': 'application/json'};
    if (needToken) {
        headers.Authorization = `${BEARER} ${localStorage.getItem('accessToken')}`
    }
    let result = await fetch(`${LOCAL_HOST}/${url}`, {
        method: 'delete',
        headers: headers,
        credentials: 'include',
        body: JSON.stringify(body)
    })
        .then(response =>
            response.text()
        ).then(text => {
                if (text) {
                    return JSON.parse(text)
                } else
                    return {};
            }
        )

    return orThrow(result)
}