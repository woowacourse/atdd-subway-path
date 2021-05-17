export {apiRequest};

const LOCAL_HOST = 'http://localhost:8080'
const BEARER = 'Bearer'
const CAN_METHOD_LIST = ['GET', 'POST', 'PUT', 'DELETE']

const orThrow = (result) => {
    if (result.error) {
        console.error(result.error)
        new Error(result.error)
    }
    return result;
}

async function apiRequest(method, url = '', body = {}, needToken = false) {
    const isCanMethod = CAN_METHOD_LIST.findIndex(canMethod => canMethod === method.toUpperCase()) !== -1
    if(!method || !isCanMethod) {
        throw new Error(`not valid method : ${method} / can method : get, post, delete, put`);
    }

    const headers = {'Content-Type': 'application/json'};
    if (needToken) {
        headers.Authorization = `${BEARER} ${localStorage.getItem('accessToken')}`
    }

    const configure = {
        method: method,
        headers: headers,
        credentials: 'include',
    }

    if(body && method.toUpperCase() !== 'GET'){
        configure.body = JSON.stringify(body)
    }

    let result = await fetch(`${LOCAL_HOST}/${url}`, configure)
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
