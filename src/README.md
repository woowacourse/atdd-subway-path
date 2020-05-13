# 실습 - 지하철 노선도 조회

## 요구사항

- 모든 지하철 노선과 각 노선에 포함된 지하철역 조회 기능 구현
- **인수 테스트와 단위 테스트를 작성**
- 페이지 연동

## 기능 목록

### 지하철 노선도 페이지 조회

- 모든 지하철 노선과 지하철역 목록을 조회

## 시나리오

```
Feature: 전체 지하철 노선도 정보 조회

  Scenario: 지하철 노선도 정보 조회를 한다.
    Given 지하철역이 여러 개 추가되어있다.
    And 지하철 노선이 여러 개 추가되어있다.
    And 지하철 노선에 지하철역이 여러 개 추가되어있다.
    
    When 지하철 노선도 전체 조회 요청을 한다.
    
    Then 지하철 노선도 전체를 응답 받는다.

```

## 미션 진행 순서

1. 인수 조건 파악하기
2. 인수 테스트 작성하기
3. 인수 테스트 성공 시키기
4. **기능 구현**
5. API를 활용하여 페이지 연동하기

# 1단계 - 캐시 적용

## 요구사항

### HTTP 캐시 적용하기

- HTTP Cache의 종류를 학습
- 지하철 노선도 조회 시 ETag를 통해 캐시를 적용
- LineControllerTest의 ETag 테스트를 성공 시키기

## 기대결과

```
// 첫 요청

MockHttpServletRequest:
      HTTP Method = GET
      Request URI = /lines/detail
       Parameters = {}
          Headers = []
             Body = <no character encoding set>
    Session Attrs = {}

...

MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = [ETag:""0727d6d45daa377cab7a5f8f35d252b3d"", Content-Type:"application/json", Content-Length:"510"]
     Content type = application/json
             Body = {"lineDetailResponse":[{"id":null,"name":null,"startTime":null,"endTime":null,"intervalTime":0,"createdAt":null,"updatedAt":null,"stations":[{"id":null,"name":null,"createdAt":null},{"id":null,"name":null,"createdAt":null},{"id":null,"name":null,"createdAt":null}]},{"id":null,"name":null,"startTime":null,"endTime":null,"intervalTime":0,"createdAt":null,"updatedAt":null,"stations":[{"id":null,"name":null,"createdAt":null},{"id":null,"name":null,"createdAt":null},{"id":null,"name":null,"createdAt":null}]}]}
    Forwarded URL = null
   Redirected URL = null
          Cookies = []
          
// 두번째 요청

MockHttpServletRequest:
      HTTP Method = GET
      Request URI = /lines/detail
       Parameters = {}
          Headers = [If-None-Match:""0727d6d45daa377cab7a5f8f35d252b3d""]
             Body = <no character encoding set>
    Session Attrs = {}

...

MockHttpServletResponse:
           Status = 304
    Error message = null
          Headers = [ETag:""0727d6d45daa377cab7a5f8f35d252b3d"", Content-Type:"application/json"]
     Content type = application/json
             Body = 
    Forwarded URL = null
   Redirected URL = null
          Cookies = []

```



# 2단계 - 지하철 경로 조회 1

## 요구사항

- 출발역과 도착역의 최단 경로를 조회하는 기능 구현
- 기본적인 기능 구현(Happy Case)을 목표로하고 예외 상황(Side Case)에 대한 처리는 다음 단계에서 고려
- **TDD 프로세스를 따라서 개발 진행**
- 인수 조건 & 인수 테스트 작성
- 기능 구현시 필요한 단위 테스트 작성
- **중복코드를 제거**(테스트 코드도 마찬가지로 중복제거)
- 객체지향 생활체조 준수

## 기능 목록

### 경로 조회 API

- 출발역과 도착역을 입력
- 최단 거리 기준으로 경로와 기타 정보를 응답
    - 총 소요시간, 총 거리 등
- 최단 경로가 하나가 아닐 경우 어느 경로든 하나만 응답

### Request

```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=%EC%96%91%EC%9E%AC%EC%8B%9C%EB%AF%BC%EC%9D%98%EC%88%B2%EC%97%AD&target=%EC%84%A0%EB%A6%89%EC%97%AD
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8

```

### Response

```
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
            "name": "양재시민의숲역",
            "createdAt": "2020-05-09T23:54:12.007"
        },
        {
            "id": 4,
            "name": "양재역",
            "createdAt": "2020-05-09T23:54:11.995"
        },
        {
            "id": 1,
            "name": "강남역",
            "createdAt": "2020-05-09T23:54:11.855"
        },
        {
            "id": 2,
            "name": "역삼역",
            "createdAt": "2020-05-09T23:54:11.876"
        },
        {
            "id": 3,
            "name": "선릉역",
            "createdAt": "2020-05-09T23:54:11.893"
        }
    ],
    "distance": 40,
    "duration": 40
}
```