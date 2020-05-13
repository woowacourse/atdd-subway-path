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