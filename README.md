# 🚀 경로 조회 기능

## 지하철 경로 조회

### 요청
```http
GET /paths?source=1&target=5 HTTP/1.1
Accept: application/json
Host: localhost:8080
```

### 응답
```http
HTTP/1.1 200 OK
Vary: Origin
Vary: Access-Control-Request-Method
Vary: Access-Control-Request-Headers
Content-Type: application/json
Content-Length: 239

{
  "stations" : [ {
    "id" : 1,
    "name" : "지하철역이름"
  }, {
    "id" : 2,
    "name" : "새로운지하철역이름"
  }, {
    "id" : 3,
    "name" : "또다른지하철역이름"
  } ],
  "distance" : 9,
  "fare" : 1250
}
```

### 기능 명세
* [x] 정상적인 경우 요청 값 반환 
  
* [x] source 지하철 역이 없는 경우, target 지하철 역이 없는 경우
    * 404 NOT FOUND
    
* [ ] source에서 target으로 못 가는 경우
    * 400 BAD REQUEST
  
* [x] source와 target이 같은 경우 
  * 400 BAD REQUEST
  

### TODO
* PathSearcher 추출 -> 못가는 경우 예외 발생 테스트 짜기