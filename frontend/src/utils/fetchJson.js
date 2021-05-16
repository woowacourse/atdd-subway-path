export async function fetchJsonWithHeaderAndBody(url, method, header, body) {
    let headers = {
        "Content-Type": "application/json"
    }
    headers = { ...headers, ...header };

    return await fetch(url, {
        method,
        headers,
        body: JSON.stringify(body)
    });
}

export async function fetchJsonWithHeader(url, method, header) {
    let headers = {
        "Content-Type": "application/json"
    }
    headers = { ...headers, ...header };

    return await fetch(url, {
        method,
        headers
    });
}

export async function fetchJsonWithBody(url, method, body) {
    let headers = {
        "Content-Type": "application/json"
    }

    return await fetch(url, {
        method,
        headers,
        body: JSON.stringify(body)
    });
}