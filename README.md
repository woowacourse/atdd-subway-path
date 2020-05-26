# 지하철 경로 조회 - TDD

## 실습 - 지하철 노선도 조회

### 요구사항

- 모든 지하철 노선과 각 노선에 포함된 지하철역 조회 기능 구현
- 인수 테스트와 단위 테스트를 작성 (Outside-In)
- 페이지 연동



### 기능 목록

#### 지하철 노선도 페이지 조회

- 모든 지하철 노선과 지하철역 목록을 조회



#### 시나리오

```
Feature: 전체 지하철 노선도 정보 조회

  Scenario: 지하철 노선도 정보 조회를 한다.
    Given 지하철역이 여러 개 추가되어있다.
    And 지하철 노선이 여러 개 추가되어있다.
    And 지하철 노선에 지하철역이 여러 개 추가되어있다.
    
    When 지하철 노선도 전체 조회 요청을 한다.
    
    Then 지하철 노선도 전체를 응답 받는다.
```



### 실습

#### 미션 진행 순서

1. 인수 조건 파악하기 - fin
2. 인수 테스트 작성하기 - fin
3. 인수 테스트 성공 시키기 - fin
4. 기능 구현 - fin
5. API를 활용하여 페이지 연동하기 - fin



#### 기능 구현 순서 - Outside In

1. 인수 테스트 작성
2. Mock 서버와 DTO 정의
3. 컨트롤러 구현
4. 서비스 구현 - TDD로 진행
5. 인수 테스트 검증 후 확인



## 1단계 : 캐시 적용

### 요구 사항

#### HTTP 캐시 적용하기

- HTTP Cache의 종류를 학습 - fin
- 지하철 노선도 조회시 ETag를 통해 캐시를 적용 - fin
- LineControllerTest의 ETag 테스트를 성공시키기 - fin


### 추가로 생각해보기

#### 서버 캐시 적용

- 서버의 리소스를 줄이기 위해서는 서버쪽에도 캐시 설정이 필요
- 아래의 링크를 참고
  - [A Guide To Caching in Spring](https://www.baeldung.com/spring-cache-tutorial)



## 2단계 : 지하철 경로 조회 1

### 요구사항

- 출발역과 도착역의 최단 경로를 조회하는 기능 구현
- 기본적인 기능 구현(Happy Case)을 목표로 하고 예외 상황(Side Case)에 대한 처리는 다음 단계에서 고려
- TDD 프로세스를 따라서 개발 진행
- 인수 조건 & 인수 테스트 작성
- 기능 구현시 필요한 단위 테스트 작성
- 중복 코드를 제거(테스트 코드도 마찬가지로 중복 제거)
- 객체지향 생활체조 준수



### 기능 목록

#### 경로 조회 API

- 출발역과 도착역을 입력
- 최단 거리 기준으로 경로와 기타 정보를 응답
  - 총 소요시간, 총 거리 등
- 최단 경로가 하나가 아닐 경우 어느 경로든 하나만 응답



#### 경로 조회 화면

- 출발역과 도착역의 경로 정보 노출
  - 총 소요시간, 정차역 등
- 즐겨찾기 버튼과 최소 시간 기준 조회는 다른 미션이므로 무시
  - 최소시간 기준은 3단계 미션
  - 즐겨찾기(별)은 3주차 미션



#### 미션 수행 순서

1. 인수 조건 작성 및 인수 테스트 작성하기

   - Gherkin 문법을 활용하여 인수 조건 작성

   >  https://cucumber.io/docs/gherkin/reference

2. 인수 테스트 성공 시키기

   - mock 서버와 dto를 정의하여 인수 테스트 성공 시키기

3. 기능 구현
   - 컨트롤러 레이어 구현 이후 서비스 레이어 구현 시 서비스 테스트 우선 작성 후 기능 구현
   - 서비스 테스트 내부에서 도메인들간의 로직의 흐름을 검증, 이 때 사용되는 도메인은 mock 객체를 활용
   - 외부 라이브러리를 활용한 로직을 검증할 때는 가급적 실제 객체를 활용
   - Happy 케이스에 대한 부분만 구현(Side 케이스에 대한 구현은 다음 단계에서 진행)
4. API를 활용하여 페이지 연동하기
   
   - 정상적인 기능에 대한 처리 우선 적용



#### 최단 경로 라이브러리

- jgrapht 라이브러리를 활용하면 간편하게 최단거리를 조회할 수 있음
- 정점(vertext)과 간선(edge), 그리고 가중치 개념을 이용
  - 정점: 지하철역(Station)
  - 간선: 지하철역 연결정보(LineStaion)
  - 가중치: 거리 or 소요시간
- 최단 거리 기준 조회 시 가중치를 `거리`로 설정

``` java
@Test
public void getDijkstraShortestPath() {
    WeightedMultigraph<String, DefaultWeightedEdge> graph
            = new WeightedMultigraph(DefaultWeightedEdge.class);
    graph.addVertex("v1");
    graph.addVertex("v2");
    graph.addVertex("v3");
    graph.setEdgeWeight(graph.addEdge("v1", "v2"), 2);
    graph.setEdgeWeight(graph.addEdge("v2", "v3"), 2);
    graph.setEdgeWeight(graph.addEdge("v1", "v3"), 100);

    DijkstraShortestPath dijkstraShortestPath
            = new DijkstraShortestPath(graph);
    List<String> shortestPath 
            = dijkstraShortestPath.getPath("v3", "v1").getVertexList();

    assertThat(shortestPath.size()).isEqualTo(3);
}
```

> [jgrapht graph-algorithms](https://jgrapht.org/guide/UserOverview#graph-algorithms)



#### 외부 라이브러리 테스트

- 외부 라이브러리의 구현을 수정할 수 없기 때문에 단위 테스트를 하지 않음
- 외부 라이브러리를 사용하는 직접 구현하는 로직을 검증해야 함
- 직접 구현하는 로직 검증 시 외부 라이브러리 부분은 실제 객체를 활용



## 3단계 - 지하철 경로 조회 2

### 예외처리 및 Side 케이스

#### 요구사항

- Happy 케이스 이외 예외 상황에 대한 기능 구현
- 단위 테스트를 통해 출발역과 도착역이 연결되어 있지 않은 경우 검증



## 4단계 - 지하철 경로 조회 3

### 요구사항

- 최소 시간 경로 기능을 추가
- 최단 경로 조회 기능과의 코드 중복 제거(테스트 코드 포함)



## 리팩토링

### 1차 피드백 반영

- [x] Bean으로 주입받는 변수들 final로 수정
- [x] PathController에 대한 테스트 코드 작성 (side case도 함께)
- [x] Enum으로 PathType 구현 후 리팩토링
- [x] PathAcceptanceTest를 ParameterizedTest로 수정해보기
- [x] PathService에 DijkstraShortestPath.findPathBetween() 메서드를 적용할 수 있는 부분 찾아보기
- [x] 경로 검색시 css가 변경되지 않는 부분 수정



### 2차 피드백 반영

- [ ] `PathController` 의 `searchPath`  메서드의 `RequestParam` 을 커맨드 객체(RequestDto)로 변경 및 유효성 검사
- [ ] 최단거리, 최소시간에 대한 조회 결과를 한 번에 보내지 말고 요청마다 전달하도록 수정(캐싱 적용이 필요)
- [ ] 출발역과 도착역이 같은 경우, 존재하지 않는 출발역에 대한 조회 요청이 이루어질 경우에 대한 테스트 케이스 작성