# 🚇 지하철 노선도 미션

스프링 과정 실습을 위한 지하철 노선도 애플리케이션

## 🛠 1단계 기능 목록

### 지하철역 관리

- 지하철 역을 생성한다
    - 성공 : Http status(201) / 지하철 이름(name)과 식별자(id)를 응답한다.
    - 실패(이미 존재하는 역 이름) : Http status(400) / 에러 메시지를 응답한다.
- 모든 지하철 역을 조회한다
    - 성공 : Http status(200) 모든 지하철 역의 이름(name)과 식별자(id)를 반환한다.
- 지하철 역을 삭제한다.
    - 성공 : Http status(204)
    - 실패(존재하지 않는 역) : Http status(400) / 에러메시지를 반환한다.

### 지하철 노선 관리

- 지하철 노선을 생성한다.
    - 성공 : Http status(201) / 식별자(id)와 노선 이름(name), 노선 색(color), 상행선, 하행선를 응답한다.
    - 실패(이미 존재하는 노선 이름) : Http status(400) / 에러 메시지를 응답한다.

- 지하철 노선 목록을 조회한다.
    - 성공 : Http status(200) / 모든 노선의 식별자(id)와 노선 이름(name), 노선 색(color), 상행선, 하행선를 응답한다.

- 지하철 노선을 조회한다.
    - 성공 : Http status(200) / 노선의 식별자(id)와 노선 이름(name), 노선 색(color), 상행선, 하행선를 응답한다.
    - 실패(존재하지 않는 노선) : Http status(400) / 에러 메시지를 응답한다.

- 지하철 노선을 수정한다.
    - 성공 : Http status(204)
    - 실패(존재하지 않는 노선) : Http status(400) / 에러 메시지를 응답한다.

### 구간 관리

- 구간을 등록한다.
    - 성공 : Http status(200)
    - 실패 : Http status(400) / 에러 메시지를 응답한다.
        - 노선에 상행선과 하행선이 이미 존재하는 경우
        - 노선에 상행선과 하행선 중 하나도 없는 경우
        - 기존 구간의 사이에 새로운 구간을 추가하는데 새로운 구간의 길이가 더 긴 경우

- 구간을 삭제한다.
    - 성공 : Http status(200)
    - 실패(노선에 구간이 하나인 경우) : Http status(400)

### 경로 조회

- 최단 경로를 조회한다.
    - 성공 : Http status(200) / 경유역 목록과 소요 거리와 운임비용을 응답한다.
        - 기본운임(10㎞ 이내): 기본운임 1,250원
        - 이용 거리 초과 시 추가운임 부과
        - 10km~50km: 5km 까지 마다 100원 추가
        - 50km 초과: 8km 까지 마다 100원 추가
    - 실패 : Http status(400) / 예외 메시지를 응답한다.
        - 구간이 연결되어 있지 않는 경우
        - 존재하지 않는 역으로 요청한 경우

## 🛠 2단계 기능 목록

### 추가된 요금 정책

- 노선별 추가 요금
    - [x] 추가 요금이 있는 노선을 이용할 경우 측정된 요금에 추가된다.
        - ex) 900원 추가 요금이 있는 노선 8km 이용 시 1,250원 -> 2,150원
        - ex) 900원 추가 요금이 있는 노선 12km 이용 시 1,350원 -> 2,250원
    - [x] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용된다.
        - ex) 0원, 500원, 900원의 추가 요금이 있는 노선들을 경유하여 8km 이용 시 1,250원 -> 2,150원

- 연령별 요금 할인
    - [x] 청소년: 운임에서 350원을 공제한 금액의 20% 할인이 적용된다.
        - 청소년: 13세 이상~19세 미만
    - [x] 어린이: 운임에서 350원을 공제한 금액의 50% 할인이 적용된다.
        - 어린이: 6세 이상~13세 미만
    - [x] 우대: 무임혜택이 적용된다.
        - 우대: 6세 미만, 65세 이상

## 🎞 시나리오

### Feature: 지하철역 관리

```
Scenario: 지하철역 생성
    When 지하철역 생성을 요청한다.
    Then 지하철역 생성이 성공한다.
```

```
Scenario: 중복되는 이름을 가진 지하철역 생성
    Given "강남역" 이라는 이름을 가진 지하철역을 생성한다.
    When  "강남역" 이라는 이름을 가진 지하철역을 추가 생성 요청한다.
    Then  지하철역 생성이 실패한다.
```

```
Scenario: 지하철역 조회
    Given "강남역" 이라는 이름을 가진 지하철역을 생성 요청한다.
    When  "강남역" 이라는 이름을 가진 지하철역 조회 요청을 보낸다.
    Then  "강남역" 이라는 이름을 가진 지하철역을 응답한다.
```

```
Scenario: 지하철역 제거
    Given "강남역" 이라는 이름을 가진 지하철역을 생성 요청한다.
    When  "강남역" 이라는 이름을 가진 지하철역 삭제 요청을 보낸다.
    Then  "강남역" 이라는 이름을 가진 지하철역이 삭제된다.
```

### Feature: 지하철 노선 관리

```
Scenario: 지하철 노선 생성
    When 지하철 노선 생성을 요청한다.
    Then 지하철 노선 생성이 성공한다.
```

```
Scenario: 중복되는 이름을 가진 지하철 노선 생성
    Given "2호선" 이라는 이름을 가진 지하철 노선을 생성한다.
    When  "2호선" 이라는 이름을 가진 지하철 노선을 추가 생성 요청한다.
    Then  지하철 노선 생성이 실패한다.
```

```
Scenario: 지하철 노선 목록 조회
    Given n개의 지하철 노선 생성을 요청한다.
    When  지하철 노선 목록 조회를 요청한다.
    Then  n개의 지하철 노선이 담긴 지하철 노선 목록을 응답한다.
```

```
Scenario: 지하철 노선 조회
    Given "2호선" 이라는 이름을 가진 지하철 노선을 생성 요청한다.
    When  "2호선" 이라는 이름을 가진 노선 조회 요청을 보낸다.
    Then  "2호선" 이라는 이름을 가진 노선을 응답한다.
```

### Feature: 지하철 구간 관리

```
Scenario: 상행 종점이 같은 지하철 구간 생성
    Given "2호선"이라는 노선에 상행 종점이 "강남역"이고 하행 종점이 "잠실역"인 구간이 등록된다.
    When  상행 종점이 "강남역"이고 하행 종점이 "역삼역"인 구간 추가 요청이 들어온다.
    Then  "2호선"이라는 노선에 "강남역", "역삼역", "잠실역" 순으로 구간이 생성된다.
```

```
Scenario: 하행 종점이 같은 지하철 구간 생성
    Given "2호선"이라는 노선에 상행 종점이 "강남역"이고 하행 종점이 "잠실역"인 구간이 등록된다.
    When  상행 종점이 "역삼역"이고 하행 종점이 "잠실역"인 구간 추가 요청이 들어온다.
    Then  "2호선"이라는 노선에 "강남역", "역삼역", "잠실역" 순으로 구간이 생성된다.
```

```
Scenario: 상행 종점을 연장
    Given "2호선"이라는 노선에 상행 종점이 "역삼역"이고 하행 종점이 "잠실역"인 구간이 등록된다.
    When  상행 종점이 "강남역"이고 하행 종점이 "역삼역"인 구간 추가 요청이 들어온다.
    Then  "2호선"이라는 노선에 "강남역", "역삼역", "잠실역" 순으로 구간이 생성된다.
```

```
Scenario: 하행 종점을 연장
    Given "2호선"이라는 노선에 상행 종점이 "강남역"이고 하행 종점이 "역삼역"인 구간이 등록된다.
    When  상행 종점이 "역삼역"이고 하행 종점이 "잠실역"인 구간 추가 요청이 들어온다.
    Then  "2호선"이라는 노선에 "강남역", "역삼역", "잠실역" 순으로 구간이 생성된다.
```

```
Scenario: 기존 구간보다 길이가 긴 구간 추가 요청
    Given "2호선"이라는 노선에 상행 종점이 "강남역"이고 하행 종점이 "역삼역"이며 구간의 길이는 10인 구간이 등록된다.
    When  상행 종점이 "역삼역"이고 하행 종점이 "잠실역"이며 구간의 길이가 10인 구간의 추가 요청이 들어온다.
    Then  구간 생성에 실패한다.
```

```
Scenario: 기존 구간과 겹치는 종점이 없는 구간 추가 요청
    Given "2호선"이라는 노선에 상행 종점이 "강남역"이고 하행 종점이 "잠실역"인 구간이 등록된다.
    When  상행 종점이 "역삼역"이고 하행 종점이 "성수역"인 구간 추가 요청이 들어온다.
    Then  구간 생성에 실패한다.
```

```
Scenario: 기존 구간과 상행과 하행 종점이 모두 같은 구간 추가 요청
    Given "2호선"이라는 노선에 상행 종점이 "강남역"이고 하행 종점이 "역삼역"인 구간이 등록된다.
    When  상행 종점이 "강남역"이고 하행 종점이 "역삼역"인 구간 추가 요청이 들어온다.
    Then  구간 생성에 실패한다.
```

```
Scenario: 상행과 하행 종점이 동일한 구간 추가 요청
    When  상행 종점이 "강남역"이고 하행 종점도 "강남역"인 구간 추가 요청이 들어온다.
    Then  구간 생성에 실패한다.
```

```
Scenario: 구간의 길이가 1 미만인 구간 추가 요청
    Given "2호선"이라는 노선에 상행 종점이 "강남역"이고 하행 종점이 "역삼역"인 구간이 등록된다.
    When  상행 종점이 "역삼역"이고 하행 종점이 "잠실역"이며 구간의 길이가 0인 구간의 추가 요청이 들어온다.
    Then  구간 생성에 실패한다.
```

```
Scenario: 상행 종점 제거
    Given "2호선"이라는 노선에 "강남역", "역삼역", "잠실역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 5, 5로 등록된다.
    When  "2호선"이라는 노선에서 "강남역"을 제거 요청한다.
    Then  "2호선"이라는 노선에 "역삼역", "잠실역" 순의 길이가 5인 구간이 남겨진다.
```

```
Scenario: 중간역 제거
    Given "2호선"이라는 노선에 "강남역", "역삼역", "잠실역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 5, 5로 등록된다.
    When  "2호선"이라는 노선에서 "역삼역"을 제거 요청한다.
    Then  "2호선"이라는 노선에 "강남역", "잠실역" 순의 길이가 10인 구간이 남겨진다.
```

```
Scenario: 하행 종점 제거
    Given "2호선"이라는 노선에 "강남역", "역삼역", "잠실역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 5, 5로 등록된다.
    When  "2호선"이라는 노선에서 "잠실역"을 제거 요청한다.
    Then  "2호선"이라는 노선에 "강남역", "역삼역" 순의 길이가 5인 구간이 남겨진다.
```

```
Scenario: 구간이 하나인 노선에서 구간 제거
    Given "2호선"이라는 노선에 "강남역", "역삼역" 순으로 구간이 등록된다.
    When  "2호선"이라는 노선에서 "역삼역"을 제거 요청한다.
    Then  구간 삭제에 실패한다.
```

```
Scenario: 노선에 포함되지 않은 구간 제거
    Given "2호선"이라는 노선에 "강남역", "역삼역", "잠실역" 순으로 구간이 등록된다.
    When  "2호선"이라는 노선에서 "성수역"을 제거 요청한다.
    Then  구간 삭제에 실패한다.
```

### Feature: 지하철 경로 조회 관리

```
Scenario: 지하철역간 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "선릉역", "천호역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록된다.
    When  20살 탑승자의 "강남역" 부터 "천호역" 까지 경로 조회 요청이 들어온다.
    Then  경로는 "강남역", "선릉역", "천호역" 순으로 응답한다.
    Then  거리는 4이며, 운임비용은 1250원으로 응답한다.
```

```
Scenario: 추가요금이 존재하는 지하철역간 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "선릉역", "천호역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록되며, 추가요금은 900원으로 등록된다.
    When  20살 탑승자의 "강남역" 부터 "천호역" 까지 경로 조회 요청이 들어온다.
    Then  경로는 "강남역", "선릉역", "천호역" 순으로 응답한다.
    Then  거리는 4이며, 운임비용은 2150원으로 응답한다.
```

```
Scenario: 어린이 탑승자의 지하철역간 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "선릉역", "천호역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록된다.
    When  10살 어린이 탑승자의 "강남역" 부터 "천호역" 까지 경로 조회 요청이 들어온다.
    Then  경로는 "강남역", "선릉역", "천호역" 순으로 응답한다.
    Then  거리는 4이며, 운임비용은 450원으로 응답한다.
```

```
Scenario: 어린이 탑승자의 추가요금이 존재하는 지하철역간 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "선릉역", "천호역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록되며, 추가요금은 900원으로 등록된다.
    When  10살 어린이 탑승자의 "강남역" 부터 "천호역" 까지 경로 조회 요청이 들어온다.
    Then  경로는 "강남역", "선릉역", "천호역" 순으로 응답한다.
    Then  거리는 4이며, 운임비용은 900원으로 응답한다.
```

```
Scenario: 청소년 탑승자의 지하철역간 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "선릉역", "천호역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록된다.
    When  15살 청소년 탑승자의 "강남역" 부터 "천호역" 까지 경로 조회 요청이 들어온다.
    Then  경로는 "강남역", "선릉역", "천호역" 순으로 응답한다.
    Then  거리는 4이며, 운임비용은 720원으로 응답한다.
```

```
Scenario: 청소년 탑승자의 추가요금이 존재하는 지하철역간 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "선릉역", "천호역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록되며, 추가요금은 900원으로 등록된다.
    When  15살 청소년 탑승자의 "강남역" 부터 "천호역" 까지 경로 조회 요청이 들어온다.
    Then  경로는 "강남역", "선릉역", "천호역" 순으로 응답한다.
    Then  거리는 4이며, 운임비용은 1440원으로 응답한다.
```

```
Scenario: 우대 탑승자의 지하철역간 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "선릉역", "천호역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록된다.
    When  65살 우대 탑승자의 "강남역" 부터 "천호역" 까지 경로 조회 요청이 들어온다.
    Then  경로는 "강남역", "선릉역", "천호역" 순으로 응답한다.
    Then  거리는 4이며, 운임비용은 0원으로 응답한다.
```

```
Scenario: 우대 탑승자의 추가요금이 존재하는 지하철역간 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "선릉역", "천호역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록되며, 추가요금은 900원으로 등록된다.
    When  5살 우대 탑승자의 "강남역" 부터 "천호역" 까지 경로 조회 요청이 들어온다.
    Then  경로는 "강남역", "선릉역", "천호역" 순으로 응답한다.
    Then  거리는 4이며, 운임비용은 0원으로 응답한다.
```

```
Scenario: 구간에 등록되지 않은 지하철역으로 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "선릉역", "천호역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록된다.
    When  "강남역" 부터 "가락시장역" 까지 경로 조회 요청이 들어온다.
    Then  경로조회에 실패한다.
```

```
Scenario: 연결되지 않은 구간의 최단 경로 조회
    Given "2호선"이라는 노선에 "강남역", "선릉역", "수서역" 순으로 구간이 등록된다.
    Given 두 구간의 길이는 각각 2, 4로 등록된다.
    Given "3호선"이라는 노선에 "천호역", "가락시장역" 순으로 구간이 등록된다.
    Given 구간의 길이는 2로 등록된다.
    When  "강남역" 부터 "가락시장역" 까지 경로 조회 요청이 들어온다.
    Then  경로조회에 실패한다.
```
