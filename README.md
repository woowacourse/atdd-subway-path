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
  
+요금 정책으로 인한 추가 기능
#### 노선별 추가 요금
- [x] 지하철 노선 등록, 조회, 수정 api에 추가 요금 정보 추가
- [ ] 경로 중 추가요금이 있는 노선을 환승하여 이용할 경우 가장 높은 금액의 추가 요금만 적용
- [ ] age가 음수일 경우, age가 150이 넘는 경우, 
  * 400 BAD REQUEST
  
#### 연령별 요금 할인
- [ ] 청소년(13<=age<19)은 운임에서 350원을 공제한 금액의 20%할인 
- [ ] 어린이(6<=age<13)는 운임에서 350원을 공제한 금액의 50%할인
