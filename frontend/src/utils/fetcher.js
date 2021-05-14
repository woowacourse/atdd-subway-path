const URL = "http://localhost:8080";

export async function requestGet(uri = '', params = {}, token) {
  let urlSearchParams = new URLSearchParams();
  for (let key in params) {
    urlSearchParams.append(key, params[key])
  }

  const headers = await makeHeaders({
    'accept': 'application/json'
  }, token);

  return await fetch(`${URL}${uri}?${urlSearchParams.toString()}`, {
    method: 'GET',
    headers: headers
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(`${response.status}`);
    }
    return response.text();
  })
  .then(text => {
    if (text)
      return JSON.parse(text)
    else return {};
  });
}

export async function requestPost(uri = '', data = {}, token) {
  const headers = await makeHeaders({
    'content-type': 'application/json',
    'accept': 'application/json'
  }, token);
  return await fetch(`${URL}${uri}`, {
    method: 'POST',
    headers: headers,
    body: JSON.stringify(data)
  })
  .then(response => {
    if (!response.ok) {
      throw new Error(`${response.status}`);
    }
    return response.text();
  })
  .then(text => {
    if (text)
      return JSON.parse(text);
    else return {};
  });
}

export async function requestDelete(uri = '', data = {}, token) {
  const headers = await makeHeaders({
    'content-type': 'application/json',
    'accept': 'application/json'
  }, token);
  return await fetch(`${URL}${uri}`, {
    method: 'DELETE',
    headers: headers,
    body: JSON.stringify(data)
  })
}

export async function requestPut(uri = '', data = {}, token) {
  const headers = await makeHeaders({
    'content-type': 'application/json',
    'accept': 'application/json'
  }, token);
  return await fetch(`${URL}${uri}`, {
    method: 'PUT',
    headers: headers,
    body: JSON.stringify(data)
  })
}

async function makeHeaders(headers, token) {
  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }
  return headers;
}
