<details>
<summary>1단계 페어 규칙 및 목표</summary>

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

</details>

<details>
<summary>1단계 기능 목록</summary>

### 기능 목록

경로 조회 (**GET** /paths?source={id}&target={id}&discountFareCalculator={discountFareCalculator} → 200 OK)

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
    - [X] JdbcTemplate -> NamedParam 수정
    - [X] Dto 만들어 필드 수정시 생기는 변경 범위 격리
    - [X] 정적 팩터리 메서드 대신 부생성자 이용 고려해보기
- 그 외
    - [x] 컨벤션 확인
    - [X] 네이밍이 명시적인지 확인
    - [X] 불필요한 중복이 없는지 확인
    - [X] 지역 변수 final 삭제

</details>

<details>
<summary>1단계 첫번째 리뷰</summary>

### 스티치의 1단계 첫번째 리뷰

- [X] Dao 에 @Repository 대신 @Component 사용하기
- [X] RowMapper를 메서드 또는 상수로 관리하기
- [X] LineDao 의 createNewObject() 사용한 이유 알아보고, 새로운 객체를 반환하도록 수정하기
- [X] NamedParameterJdbcTemplate 으로 수정
- [X] FareCalculator 가 필드로 distance 를 갖도록 수정
- [x] 매직넘버 상수로 관리하기
- [X] @RequestParam @ModelAttribute 로 묶어보기
- [X] LineService 의 create 메서드를 기능 단위로 메서드 분리해보기
- [X] StationService 의 try-catch 수정해보기

</details>

<details>
<summary>1단계 두번째 리뷰</summary>

### 스티치의 1단계 두번째 리뷰

- [X] 요금을 관리하는 객체에서 요금을 필드로 갖게하기
- [X] FareCalculator 의 생성자와 필드 위치 수정하기
- [X] FareCalculator 의 메서드 명이 메서드의 의도를 드러내도록 수정하기

</details>

<details>
<summary>2단계 기능 목록</summary>

### 리팩터링

- [X] test에서 map 대신 dto 를 사용하도록 수정

### 기능 목록

- 추가된 요금 정책
    - [X] Line 테이블에 extraFare int 컬럼 추가
    - [X] 추가 요금이 있는 노선을 이용할 경우 측정된 요금에 추가 요금을 추가
    - [X] 추가 요금이 있는 노선을 환승해 이용할 경우 가장 높은 추가 요금을 추가

- 연령별 요금 할인
    - [X] 청소년 요금
        - 13 <= discountFareCalculator < 19
        - 350원을 공제한 금액의 20% 할인
    - [X] 어린이 요금
        - 6 <= discountFareCalculator < 13
        - 350원을 공제한 금액의 50% 할인
    - [X] 유아 요금
        - discountFareCalculator < 6
        - 0원

### 도메인 설계

- Line
    - extraFare 를 필드로 만들기
- Fare
    - extraFare 가 있으면 추가해서 반환
    - age 를 인자로 같이 받기
        - 청소년 / 어린이 / 유아 의 경우 할인 계산해서 반환
- Name
    - 이름 원시값 포장
    - [X] 이름이 null 이면 예외 반환
    - [X] 이름에 공백이 포함되면 예외 반환
    - [X] 이름에 특수문자가 포함되면 예외 반환
- Color
    - 색상 원시값 포장
    - [X] 색상이 null 이거나 빈 값이면 예외 반환

</details>

<details>
<summary>2단계 첫번째 리뷰</summary>

### 네오의 2단계 첫번째 리뷰

- [ ] Color 의 상수와 필드 사이 1줄 띄우기
- [ ] Color 를 enum 으로 만들어보기
- [ ] DiscountFareCalculator enum 을 설명이 아닌 도메인에 가까운 이름으로 수정
- [ ] Fare 의 상수를 `50km, 10km` 가 아닌 `장거리, 단거리` 와 같은 이름으로 수정
- [ ] Name 의 Pattern 은 비싼 자원이므로 재사용 하기
- [ ] 알고리즘 or 라이브러리와 같이 변경이 될 수 있는 부분은 인터페이스를 사용
- [ ] Lines 도메인 만들기
- [ ] PathCalculator 자료형이 포함된 변수명 수정
- [ ] 에러 메세지를 더 친절하게 작성 (LineService)
- [ ] 900 으로 하드코딩 된 부분 수정
- [ ] PathService 의 DB O(n)번 접근하는 비용 문제 해결
- [ ] sql 파일 자동 정렬로 인한 가독성 문제 해결
- test
    - [ ] id 를 직접 사용하지 않으며 테스트를 할 수 있도록 수정
    - [ ] 테스트에만 사용되는 deleteAll 메서드를 사용하지 않는 구조로 수정
- JgraphtTest
    - [ ] assertThat 이 반복되는 곳은 assertAll 사용하기
    - [ ] test 명을 명시적으로 수정, DisplayName 사용
    - [ ] 변수명을 명시적으로 수정
- AcceptanceTest
    - [ ] test fixture 만들기
    - [ ] 1, 2와 같은 의미를 나타내지 못하는 변수명 구분 수정

</details>
