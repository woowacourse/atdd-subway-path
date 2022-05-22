<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <a href="https://techcourse.woowahan.com/c/Dr6fhku7" alt="woowacourse subway">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/woowacourse/atdd-subway-map">
</p>

<br>

# 지하철 노선도 미션

스프링 과정 실습을 위한 지하철 노선도 애플리케이션

<br>

## 기능 구현 목록
- 2단계 요금 요구 사항
  - [ ] 노선 별 추가 요금
    - [ ] 노선을 생성할 때 추가 요금도 입력받을 수 있다.
    - [ ] 추가 요금이 있는 노선을 이용하면 가장 비싼 노선의 추가요금만 더 한다.
  - [ ] 연령 별 요금 할인
    - [ ] 연령 별 요금 할인은 거리 별 추가 요금 + 노선 별 추가 요금을 더한 이후 적용한다.
    - [ ] 청소년인 경우 운임에서 350원을 공제한 금액에서 20%을 할인한 금액을 적용한다.
    - [ ] 청소년은 13 <= 나이 < 19 인 경우를 의미한다.
    - [ ] 어린이 인 경우 운임에서 350원을 공제한 금액에서 50%을 할인한 금액을 적용한다.
    - [ ] 어린이는 6 <= 나이 < 13 인 경우를 의미한다.
### API

#### Station

- [x] 지하철역을 등록 할 수 있다.
    - [x] 이름이 중복이면 등록 할 수 었다.
    - [x] 이름이 공백이 될 수 없다.
    - [x] 이름의 길이가 15자가 넘으면 안된다.
    - [x] 등록을 하기 위해서 name이 필요하다.
    - [x] 등록을 하면 역 ID와 name을 응답한다.
- [x] 모든 지하철역 목록을 조회 할 수 있다.
- [x] 지하철역을 삭제 할 수 있다.
    - 역 ID를 받아서 해당하는 역을 삭제한다
    - 존재하지 않는 역 id는 삭제할 수 없다.

#### Line

- [x] 노선을 등록 할 수 있다.
    - 노선의 이름과, 색, 구간(시작역, 끝역, 거리)가 필요하다.
    - 이름이 중복이면 등록 할 수 었다.
    - 등록을 하면 역 ID와 name, color, 노선에 포함된 역들을 응답한다.
- [x] 모든 노선을 목록을 조회 할 수 있다.
    - 각 노선의 id, name, color 역들을 포함한다.
- [x] id에 해당하는 노선을 조회 할 수 있다.
- [x] id에 해당하는 노선 정보를 수정 할 수 있다.
- [x] 노선을 삭제 할 수 있다.
    - 노선 ID를 받아서 해당하는 노선을 삭제한다

#### Section

- [x] 구간을 등록할 수 있다.
  - 상행 역 ID, 하행 역 ID, 구간 길이가 필요하다.
  - 성공시 200 상태 코드만 응답한다.
  - 이미 있는 역들을 가진 구간을 등록하려면 예외 발생한다.
  - 구간을 내부로 삽입하는 경우 기존 구간보다 삽입되는 구간이 길면 예외 발생한다.
  - 
- [x] 구간을 삭제할 수 있다.
  - 역 ID가 필요하다
  - 성공시 200 상태 코드만 응답한다.

### Domain

- Station
  - [x] 역의 이름이 공백인지 검사한다.
  - [x] 역의 이름이 15자를 초과하는지 검사한다.
- Line
  - [x] 노선의 이름이 공백인지 검사한다.
  - [x] 노선은 반드시 한 개 이상의 구간을 가진다.
- Section
  - [x] 구간이 쪼갤 수 있는 지 확인한다.
  - [x] 구간을 쪼개서 반환한다.
    - [x] 시작점과 끝점이 모두 같으면 예외를 반환한다.
    - [x] 구간을 쪼개는 경우 삽입되는 구간이 원래 구간보다 길거나 같으면 예외를 반환한다.
  - [x] 구간에 특정 역이 포함되는 지 확인한다.
  - [x] 두 구간을 연결한 한 구간을 만든다.
    - [x] 두 구간이 연결되지 않으면 예외를 반환한다.
    - [x] 두 구간이 시작과 끝이 동일하면 예외를 반환한다.
- Sections
  - [x] 특정 구간과 분할된 구간을 찾는다.
    - [x] 특정 구간이 이미 연결된 구간이면 예외를 반환한다.
    - [x] 특정 구간이 현재 구간들과 접점이 없으면 예외를 반환한다.
  - [x] 역 id로 삭제할 구간들을 구한다.
    - [x] 현재 구간이 1개인데 삭제할 구간을 구하면 예외를 던진다.
  - [x] 특정 역과 근접한 구간들을 반환한다.
- DeletableSection
  - [x] 삭제할 구간이 2개 이상이면 예외를 반환한다.
  - [x] 근접한 구간을 합친 하나의 구간을 반환한다.
- Stations
  - [x] 역들을 ID 순서에 맞게 정렬해서 반환한다.
  - [x] ID에 맞는 역이 없으면 예외를 반환한다.
- StationDao
   - [x] 중복되지않는 이름의 역을 저장한다.
   - [x] 저장된 모든 역을 반환한다.
   - [x] ID에 해당하는 역을 삭제한다.
- LineDao
    - [x] 중복되지않는 이름의 노선을 저장한다.
    - [x] 저장된 모든 노선을 반환한다.
    - [x] ID에 해당하는 노선을 삭제한다.
    - [x] ID에 해당하는 노선을 반환한다.
- SectionDao
  - [x] 구간을 저장한다.
  - [x] 특정 노선의 구간들을 반환한다.
  - [x] 특정 노선을 삭제한다.

# 사용자 시나리오

Feature: 지하철 역 관리

  Scenario: 지하철 역 등록한다.
    
    When 이름을 입력해서 역 생성 요청한다.
    Then 지하철 노선 생성이 성공한다.

  Scenario: 지하철 역 목록 조회한다.
    
    when 지하철 역을 n개 추가 요청한다.
    then 지하철 역이 추가됐다.

    when 지하철 역 목록 요청한다.
    then 지하철 역 목록을 얻을 수 있다.
    and 지하철 역 목록은 n개다.

  Scenario: 지하철 역 삭제한다.

    when 지하철 역을 등록 요청한다.
    then 지하철 역 등록에 성공한다.
    
    when 등록한 역을 삭제 요청한다.
    then 지하철 역 삭제 성공한다.

Feature: 노선 관리 기능

  Scenario: 노선을 등록한다.

    when 역 2개를 등록한다.
    then 역 등록에 성공한다.

    when 노선을 등록한다.
    then 노선 등록에 성공한다.
    and 노선 정보와 역 목록 2개를 얻을 수 있다.

  Scenario: 모든 노선 조회한다.

    when 역 n개를 등록한다.
    then 역 등록에 성공한다.

    when 노선 m개를 등록한다.
    then 노선 m개 등록에 성공한다.

    when 노선 목록을 조회한다.
    then 노선 목록 조회에 성공한다.
    and 조회된 노선의 갯수는 m개다.

  Scenario: 특정 노선 조회한다.
    
    when 역 n개 등록한다.
    then 역 등록에 성공한다.

    when 노선 등록한다.
    then 노선 등록에 성공한다.

    when 등록된 노선을 조회한다.
    then 노선조회에 성공한다.
    and 조회된 노선은 등록한 노선의 이름과 동일하다.
    and 조회된 노선에는 등록된 노선의 역 정보 2개가 있다.

  Scenario: 특정 노선 수정한다.

    when 역 n개 등록한다.
    then 역 등록에 성공한다.

    when 노선 등록한다.
    then 노선 등록에 성공한다.

    when 등록된 노선을 수정한다.
    then 등록된 노선 수정에 성공한다.

    when 수정한 노선을 조회한다.
    then 조회된 노선의 이름이 수정하려한 내용과 같다.
    and 조회된 노선의 색깔이 수정하려한 내용과 같다.
    and 조회된 노선의 상행 종점과 하행 종점이 수정하려한 내용과 같다.

  Scenario: 특정 노선을 삭제한다.

    when 역 n개 등록한다.
    then 역 등록에 성공한다.

    when 노선을 등록한다.
    then 노선 등록에 성공한다.

    when 등록된 노선을 삭제 요청한다.
    then 노선 삭제에 성공한다.

Feature: 구간 관리 기능

  Scenario: 구간을 등록한다.
    
    when 역 상계 중계 하계 노원을 등록 요청한다.
    then 역 4개 등록 요청 성공한다.
  
    when 상계 하계를 종점으로 가지는 7호선을 등록 요청한다.
    then 노선 등록 요청에 성공한다.

    when 등록된 노선에 상계 중계 구간 등록 요청한다.
    then 구간 등록 요청에 성공한다.

Scenario: 구간을 삭제한다.

    when 역 상계 중계 하계 노원을 등록 요청한다.
    then 역 4개 등록 요청 성공한다.
  
    when 상계 하계를 종점으로 가지는 7호선을 등록 요청한다.
    then 노선 등록 요청에 성공한다.

    when 상계 중계 구간을 7호선에 등록한다.
    then 구간 등록에 성공한다.

    when 등록된 노선에 상계 중계 구간 등록 요청한다.
    then 구간 등록 요청에 성공한다.

## 🚀 Getting Started

### Usage

#### application 구동

```
./gradlew bootRun
```

<br>

## ✏️ Code Review Process

[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/woowacourse/atdd-subway-map/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-map/blob/master/LICENSE) licensed.
