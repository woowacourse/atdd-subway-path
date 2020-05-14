# 우아한 테크코스 - 지하철 경로 조회

## 기능 목록
- 지하철 노선도 페이지 조회
    - 모든 지하철 노선과 지하철역 목록을 조회
- 캐시 적용
- 최단 경로
- 예외 처리
- 최소 시간

## 시나리오(ATDD)

````gherkin
Feature: 전체 지하철 노선도 정보 조회

  Scenario: 지하철 노선도 정보 조회를 한다.
    Given 지하철역이 여러 개 추가되어있다.
    And 지하철 노선이 여러 개 추가되어있다.
    And 지하철 노선에 지하철역이 여러 개 추가되어있다.
    
    When 지하철 노선도 전체 조회 요청을 한다.
    
    Then 지하철 노선도 전체를 응답 받는다.
````

## TO-DO

- 최단 경로
    -[x] 인수테스트 ← 라이브러리 쓰기
    -[x] 컨르롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
    -[x] 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
    -[x] 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
    -[x] Happy 케이스에 대한 부분만 구현( Side 케이스에 대한 구현은 다음 단계에서 진행)
    
- 경로 조회 화면
    -[x] 출발역과 도착역의 경로 정보 노출
    -[x] 총 소요시간, 정차역 등
    
- 예외 처리
    -[x] 출발역과 도착역이 같은 경우
    -[x] 출발역과 도착역이 연결이 되어 있지 않은 경우
    -[x] 존재하지 않은 출발역이나 도착역을 조회 할 경우
    
- 최소 시간
    -[x] 최소 시간 경로 기능을 추가
    -[x] 최단 경로 조회 기능과의 중복 제거를 수행(테스트 코드도 마찬가지로 중복제거)

-[ ] 경로조회 로직 최적화