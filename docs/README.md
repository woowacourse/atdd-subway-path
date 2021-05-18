## 기능 목록

### 1단계

#### 프론트엔드

- [x] 토큰 발급 요청 API 구현하기

#### 백엔드

- [x] 토큰 발급 API 구현하기
- request

```json
POST /login/token HTTP/1.1
content-type: application/json; charset=UTF-8
accept: application/json
{
    "password": "password",
    "email": "email@email.com"
}
```

- response

```json
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

- [x] cors 처리 설정

### 2단계

#### 프론트엔드

- [x] 내 정보 기능에 관한 API 호출 기능 구현하기

#### 백엔드

- [x] 토큰을 통한 인증 - 내 정보 기능
    - [x] "/members/me" 요청 시 토큰을 확인하여 로그인 정보를 받아오기
    - [x] @AuthenticationPrincipal과 AuthenticationPrincipalArgumentResolver을 활용
  
### 3단계
#### 프론트엔드
- [x] //TODO 부분 찾아서 진행
#### 백엔드
- [x] 출발역과 도착역 사이의 최단 거리 경로를 구하는 API 구현
- [x] 검색 시 경로와 함께 총 거리를 출력하기(요금은 무시)
- [x] 한 노선에서 경로 찾기 뿐만 아니라 여러 노선의 환승도 고려하기
- [x] 프론트엔드 코드 중 API를 호출하는 부분을 구현하여 기능이 잘 동작하도록 완성하기
- Request
```json
HTTP/1.1 200
Request method:	GET
Request URI:	http://localhost:55494/paths?source=1&target=6
Headers: 	Accept=application/json
Content-Type=application/json; charset=UTF-8

```
- Response
```json
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Sat, 09 May 2020 14:54:11 GMT
Keep-Alive: timeout=60
Connection: keep-alive

{
    "stations": [
        {
            "id": 5,
            "name": "양재시민의숲역"
        },
        {
            "id": 4,
            "name": "양재역"
        },
        {
            "id": 1,
            "name": "강남역"
        },
        {
            "id": 2,
            "name": "역삼역"
        },
        {
            "id": 3,
            "name": "선릉역"
        }
    ],
    "distance": 40
}

```