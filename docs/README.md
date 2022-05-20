## 페어 규칙

- 방역수칙 준수
- 식사시간 미루지 않기
- 중간중간에 의식적으로 쉬는시간 갖기 (2시간 넘기지 말기)
- 토론이 과열되면 잠깐 멈추고 제 3의 대안 생각해보기
- 기록 잘 해두기
    - 토론한 과정, 해결하지 못한 이슈, 순간적인 궁금증이나 아이디어 등
- 아무쪼록 손절만 하지 말기
    - 다음주 회식날 웃으면서 만나기~^.^

## 목표

### 공통

- TDD (E2E) 테스트를 작성하는 방법과 이 테스트를 통해 마음의 안정을 갖고 싶다.
- 요구사항을 추가한 후에도 DB와 관계없이 잘 돌아가는 도메인이었으면 좋겠다
    - 계층을 분리하자! (비즈니스 로직과 dao의 의존성을 분리하자)
- 도메인 로직을 객체지향을 놓지 않고 구현해보고 싶다. (클린 코드)
    - 웹을 떠나서 레벨 1에서 배운거를 잊지않기
- 과설계 X (요구사항 내에서 구현)

### 토닉

- jdbcTemplate, SimpleJdbcInsert? 등등 DB를 연결하는 커넥션을 다루는 객체들의 특징들을 알아가고 싶다.
- JsonPath 다루는 방법

### 포키

- 만들어둔 RestAssured Test fixture 구조를 유지하고 (필요하다면) 개선, 확장해보고 싶음
- (optional)Repository 계층을 적용까진 못해도 알아가보고는 싶다
    - 지금은 도메인에서 DB 접근을 위한 정보들을 가지고 있는데, 이 것이 분리될 수는 없을까 하는 고민이 있음
- (optional)지금은 예외처리가 다소 포괄적으로 느껴지는데, custom exception을 구현해서 예외 context를 세분화 해보고싶음

### 야호

- custom exception 써보기!
- 해당 로직이 해당 계층에 어울리는지 한번 더 고민해보기

---

### 기능 목록

경로 조회 (**GET** /paths?source={id}&target={id}&age={age} → 200 OK)

- [X] 최단 경로를 조회한다
    - [X] 모든 노선의 구간에 대해 조회한다
- [X] 구한 최단 경로의 총 거리를 구한다
- [X] 구한 최단 경로의 요금을 구한다

### 도메인 설계

- PathCalculator
    - 경로 조회
    - 거리
- FareCalculator
    - 요금 계산

### 리팩터링

- Dao
    - [X] 인터페이스 없애기
    - [X] find 쿼리 값이 없을 때 Optional 처리
        - 조회 값이 없으면 예외 처리까지
    - [X] 이름 중복 처리 로직을 Service 로 이동
    - [ ] JdbcTemplate -> NamedParam 수정
    - [ ] Dto 만들어 필드 수정시 생기는 변경 범위 격리
    - [ ] 정적 팩터리 메서드 대신 부생성자 이용 고려해보기
- Domain
    - [ ] Sections 의 구간들이 같은 Line 이라는 사실 나타내기
    - [ ] 불필요한 getter 사용 제거
    - [ ] 원시값 포장을 통한 validation 추가
- Test
    - [ ] 변수 명을 한글로할지, 영어로할지 고민
- 그 외
    - [ ] 컨벤션 확인
    - [ ] 네이밍이 명시적인지 확인
    - [ ] 불필요한 중복이 없는지 확인
    - [X] 지역 변수 final 삭제

### 스티치의 1단계 첫번째 리뷰

- [ ] Dao 에 @Repository 대신 @Component 사용하기
- [ ] RowMapper를 메서드 또는 상수로 관리하기
- [ ] LineDao 의 createNewObject() 사용한 이유 알아보고, 새로운 객체를 반환하도록 수정하기
- [ ] NamedParameterJdbcTemplate 으로 수정
- [ ] FareCalculator 가 필드로 distance 를 갖도록 수정
- [ ] 매직넘버 상수로 관리하기
- [ ] @RequestParam @ModelAttribute 로 묶어보기
- [ ] LineService 의 create 메서드를 기능 단위로 메서드 분리해보기
- [ ] StationService 의 try-catch 수정해보기
