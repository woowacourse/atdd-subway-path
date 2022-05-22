<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <a href="https://techcourse.woowahan.com/c/Dr6fhku7" alt="woowacuorse subway">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/woowacourse/atdd-subway-path">
</p>

<br>

# 지하철 노선도 미션

스프링 과정 실습을 위한 지하철 노선도 애플리케이션

## 요구 기능 정리

### 경로 조회 관리

- 최단거리 조회 기능
    - [x] 출발역과 도착역을 입력받아서 최단거리 조회를 할 수 있다.
    - [x] 최단거리 조회에서는 출발역과 도착역 사이의 모든 역들을 조회할 수 있다.
    - [x] 최단거리 조회 결과에서는 총 거리와 총 요금을 확인할 수 있다.

- 요금 기능
    - [x] 기본 운임(10km)은 1,250원이다.
    - [x] 10km ~ 50km로 운임 거리를 초과했을 경우 5km 마다 100원 추가
    - [x] 50km 이상 운임 거리를 초과했을 경우 8km 마다 100원 추가

### 요금 정책 추가

- 노선별 추가 요금
    - [x] 노선 생성 시 추가 요금을 받는다.
    - [x] 추가 요금이 있는 노선을 이용할 경우 측정된 요금에 추가 요금을 추가한다.
    - [x] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.

- 연령별 요금 할인
    - [x] 19세 이상의 성인은 할인이 없다.
    - [x] 13세 이상 19세 미만의 경우 운임에서 350원을 공제한 금액의 20%를 할인한다.
    - [x] 6세 이상 13세 미만일 경우 운임에서 350원을 공제한 금액의 50%를 할인한다.
    - [x] 6세 미만일 경우 운임을 받지 않는다.

<br>

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

버그를 발견한다면, [Issues](https://github.com/woowacourse/atdd-subway-path/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-path/blob/master/LICENSE) licensed.
