export default async function jsonFetch(url, method, body, header) {
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
