# 기능 구현 목록

## 로그인 기능 구현
- [ ] 프론트 API 호출 기능 구현 (fetch외 라이브러리 사용)    
- [ ] 토큰 발급 API 구현
    - [ ] request
    - [ ] response
    
```
//request
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}

//response
HTTP/1.1 200
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sun, 27 Dec 2020 04:32:26 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJlbWFpbEBlbWFpbC5jb20iLCJpYXQiOjE2MDkwNDM1NDYsImV4cCI6MTYwOTA0NzE0Nn0.dwBfYOzG_4MXj48Zn5Nmc3FjB0OuVYyNzGqFLu52syY"
}
```

## 회원 관리 기능 구현
- [ ] 프론트 API 호출 기능 구현 (fetch외 라이브러리 사용)
- [ ] 토큰 발급 API 구현 (내 정보 기능)