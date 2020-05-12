# 지하철 경로 조회 - TDD

## 지하철 경로 조회 1
### 요구 사항
- 출발역과 도착역의 최단 경로를 조회하는 기능 구현
- 기본적인 기능 구현(Happy Case)을 목표로하고 예외 상황(Side Case)에 대한 처리는 다음 단계에서 고려
- TDD 프로세스를 따라서 개발 진행
- 인수 조건 & 인수 테스트 작성
- 기능 구현시 필요한 단위 테스트 작성
- 중복코드를 제거(테스트 코드도 마찬가지로 중복제거)
- 객체지향 생활체조 준수

### 기능 목록
#### 경로 조회 API
- [ ] 출발역과 도착역을 입력
- [ ] 최단 거리 기준으로 경로와 기타 정보를 응답
    - [ ] 총 소요시간, 총 거리 등
- [ ] 최단 경로가 하나가 아닐 경우 어느 경로든 하나만 응답


##### Request
```
HTTP/1.1 200 
Request method:	GET
Request URI:	http://localhost:55494/paths?source=%EC%96%91%EC%9E%AC%EC%8B%9C%EB%AF%BC%EC%9D%98%EC%88%B2%EC%97%AD&target=%EC%84%A0%EB%A6%89%EC%97%AD
Headers: 	Accept=application/json
		Content-Type=application/json; charset=UTF-8
```

##### Response
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

#### 경로 조회 화면
- [ ] 출발역과 도착역의 경로 정보 노출
    - [ ] 총 소요시간, 정차역 등
- [ ] 즐겨찾기 버튼과 최소시간 기준 조회는 다른 미션이므로 무시
