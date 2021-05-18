import {LOCAL_STORAGE_KEYS} from "@/utils/constants";

export async function fetchJsonWithHeaderAndBody(url, method, header, body) {
    let headers = {
        "Content-Type": "application/json"
    }
    headers = {...headers, ...header};

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
    headers = {...headers, ...header};

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

export async function fetchJson(url, method) {
    let headers = {
        "Content-Type": "application/json"
    }

    return await fetch(url, {
        method,
        headers
    });
}

export function tokenHeaderIfExist() {
    if (localStorage.getItem(LOCAL_STORAGE_KEYS.AUTH)) {
        return {
            'Authorization': `Bearer ${localStorage.getItem(LOCAL_STORAGE_KEYS.AUTH)}`
        };
    }
    return null;
}