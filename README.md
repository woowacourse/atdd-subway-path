## 지하철 경로조회 미션

### 기능 요구사항

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
  - [x] 응답으로 station의 리스트(stations), distance, fare를 반환한다.
  - [x] `예외` source -> target 의 경로가 없다면 예외를 발생한다.

## 지하철 요금 정책 추가

### 기능 요구사항
- [x] 추가된 요금 정책 반영하기
  - [x] 추가 요금이 있는 노선을 이용할 경우 측정된 요금에 추가
    - 계산 결과에 추가 요금을 추가한다.
    - 경로 중 여러 노선을 환승할 경우 가장 추가 요금이 높은 것만 적용한다.
  - [x] 연령별 요금할인을 적용한다.
    - 청소년: 운임에서 350원을 공제한 금액의 20% 할인
      - 청소년은 13세 이상 ~ 19세 미만이다. 
      - ( 요금 - 350 ) * 0.8
    - 어린이: 운임에서 350원을 공제한 금액의 50% 할인
      - 어린이는 6세 이상 ~ 13세 미만이다.
      - ( 요금 - 350 ) * 0.5
    - 6세 미만의 유아는 무료이다.


### 피드백 반영
- [ ] 누락된 테스트 코드 작성
- [ ] id 인자에 primitive type 사용해보기
- [ ] 연령과 할인율에 enum 사용해보기
- [ ] findPassedLineIds() 와 lineDao.getMaxFareByLineIds(passedLineIds)의 비즈니스 로직을 도메인 객체 안으로 숨겨보기
- [ ] 테스트 최적화 해보기 ( 테스트 속도개선 )
