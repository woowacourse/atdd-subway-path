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

## 🖍 기능 구현 목록

1. 지하철역 관리 기능
    - [x]  입력받은 이름으로 지하철 역 생성
        - [x]  이미 있는 역의 이름이 입력으로 들어올 경우 예외 발생
    - [x]  전체 지하철역 조회
    - [x]  입력받은 이름의 지하철역 삭제

2. 지하철 노선 관리 기능
    - [x]  입력받은 이름과 노선 컬러와 요금 노선 생성
        - [x]  이미 존재하는 노선과 이름 또는 노선 컬러가 겹칠 경우 예외 발생
    - [x]  지하철 전체 노선 조회
    - [x]  입력받은 지하철 노선의 정보 조회
    - [x]  지하철 노선 수정
        - [x]  수정하려는 이름 혹은 컬러가 다른 노선과 겹칠 경우 예외 발생
    - [x]  지하철 노선 삭제

3. 경로 조회 기능
    - [x] 경로 찾기
      - [x] jgrapht 라이브러리를 활용하여 각 구간의 거리가 최소가 되는 경로를 찾는다.
      - [x] 여러 노선의 환승을 고려하여 최단 경로를 찾는다. 
      - [x] 출발지와 도착지가 같은 경우에는 예외가 발생한다.
      - [x] 현재 구성되어 있는 구간을 통해서 갈 수 없는 경로를 찾으려고 하면 예외가 발생한다.
    - [x] 요금 계산
      - [x] 기본 운임(10km 이내)는 1,250원 이다.
      - [x] 이용 거리 초과 시 추가 운임을 부과한다.
        - [x] 10km ~ 50km: 5km 까지 마다 100원 추가
        - [x] 50km 초과: 8km 까지 마다 100원 추가

4. 추가된 요금 정책
   - [x] 노선별 추가 요금
      - [x] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가
      - [x] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
   - [ ] 연령별 요금 할인
     - [ ] 청소년: 운임에서 350원을 공제한 금액의 20%할인
     - [ ] 어린이: 운임에서 350원을 공제한 금액의 50%할인
## 🚀 Getting Started

### Usage

#### application 구동

```
./gradlew bootRun
```

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-map/blob/master/LICENSE) licensed.
