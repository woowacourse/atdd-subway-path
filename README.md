## 지하철 경로조회 미션

### 1단계 기능 요구사항

- [x] jgrapht 학습테스트 해보기
- [x] 최단 거리 경로 로직 구현
    - [x] 모든 section 가져오기
    - [x] section 들로 jgrapht 로 graph 만들기
    - [x] jgrapht 로 최단거리의 경로를 구하기
- [x] 요금 계산 로직 구현
    - [x] 기본운임(10㎞ 이내): 기본운임 1,250원
    - [x] 10km~50km: 5km 까지 마다 100원 추가
    - [x] 50km 초과: 8km 까지 마다 100원 추가
- [x] 경로 조회 API 구현 (GET /paths?source=1&target=5&age=15)
  - [x] 응답값으로 200 OK를 준다.
  - [x] 응답으로 station 의 리스트(stations), distance, fare 를 반환한다.
  - [x] `예외` source -> target 의 경로가 없다면 예외를 발생한다.

### 2단계 기능 요구사항
- [x] schema.sql Line 에 extraFare 추가하기
- [x] Line, LineResponse, LineRequest 에 extraFare 추가하기
- [x] Graph 에서 최소 경로 구할 때 Section 리스트로 구하기
- [x] 요금 구하기
  - [x] 노선별 추가요금 포함하여 요금 구하기
  - [x] 연령별 요금 할인에 따라 요금 구하기
    - 청소년(13이상 19미만): 운임에서 350원을 공제한 금액의 20%할인
    - 어린이(6이상 13미만): 운임에서 350원을 공제한 금액의 50%할인
