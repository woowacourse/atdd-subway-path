export function get(url, additionalHeaders) {
    let headers = {
        "Content-Type": "application/json"
    }
    headers = { ...headers, ...additionalHeaders };

    return fetch(url, {
        method: 'GET',
        headers
    });
}

export function post(url, body, additionalHeaders) {
    let headers = {
        "Content-Type": "application/json"
    }
    headers = { ...headers, ...additionalHeaders };

    return fetch(url, {
        method: 'POST',
        headers,
        body: JSON.stringify(body)
    });
}

export function put(url, body, additionalHeaders) {
    let headers = {
        "Content-Type": "application/json",
        'Authorization': "Bearer " + localStorage.getItem("token")
    }

    headers = { ...headers, ...additionalHeaders };
    return fetch(url, {
        method: 'UPDATE',
        headers,
        body: JSON.stringify(body)
    });
}

export function remove(url, additionalHeaders) {
    let headers = {
        "Content-Type": "application/json",
        'Authorization': "Bearer " + localStorage.getItem("token")
    }

    headers = { ...headers, ...additionalHeaders };
    return fetch(url, {
        method: 'DELETE',
        headers
    });
}

export async function keepLogin() {
    const accessToken = localStorage.getItem("token");
    if (accessToken !== null) {
        return await get("/api/members/me", {'Authorization': 'Bearer ' + accessToken})
    }
}