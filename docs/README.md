### API

- 경로 조회
- URI : GET /paths
- response : List<Stations>, distance, fare
- requestParameters : source, target, age

### 구현할 기능 목록

- 요금을 계산한다.
    - 1 ~ 10까지 기본 요금 1250원
    - 11 ~ 50까지 5km 당 추가 100원 (최대 800원)
    - 50초과 8km 당 추가 100원
- 최단 거리를 계산한다.
