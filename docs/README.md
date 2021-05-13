# 기능 구현 목록

## 로그인 기능 구현
- [ ] 서버 로그인 API 기능 구현
  - [x] 로그인 (토큰 발급) 요청 처리하기
    - [x] DB에 존재하는 회원 정보이면, 토큰을 만들어서 반환하는 기능
    - [x] 올바른 토큰인지를 검증하는기능
    - [x] 토큰정보를 통해 회원정보를 조회하는 기능
      - [x] 토큰정보로부터 페이로드를 추출하는 기능
      - [x] 페이로드를 이용해서, 회원 정보를 조회하는 기능

- [ ] 프론트 API 호출 기능 구현 (fetch외 라이브러리 사용 가능)    
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
- [x] 프론트 로그인 API 호출 기능 js 구현 (fetch외 라이브러리 사용)
- [ ] 토큰 발급 API 구현 (내 정보 기능)