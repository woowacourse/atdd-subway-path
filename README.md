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
    
* [x] source에서 target으로 못 가는 경우
    * 400 BAD REQUEST
  
* [x] source와 target이 같은 경우 
  * 400 BAD REQUEST
  
## 지하철 요금 계산 명세

* 노선별 추가 요금
  * [x] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
  * [x] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
  
* 연령별 요금 할인
  * [x] 청소년(13세 이상 19세 미만): 운임에서 350원을 공제한 금액의 20%할인
  * [x] 어린이(6세 이상 13세 미만: 운임에서 350원을 공제한 금액의 50%할인