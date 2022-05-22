<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <a href="https://techcourse.woowahan.com/c/Dr6fhku7" alt="woowacuorse subway">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/woowacourse/atdd-subway-subwayGraph">
</p>

<br>

# 지하철 노선도 미션
스프링 과정 실습을 위한 지하철 노선도 애플리케이션

<br>

## 기능 구현 목록

* 경로를 조회하는 API를 구현한다.
* 경로와 함께 총 거리와 요금을 계산한다.
* 환승 노선을 고려한다.
  * 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용
* 연령별 요금 할인
  * 청소년: 운임에서 350원을 공제한 금액의 20%할인
  * 어린이: 운임에서 350원을 공제한 금액의 50%할인

## 도메인

최단경로를 찾는 객체(PathFinder)

* 모든 노선 정보를 알고 있음
* 출발역, 도착역을 받아서 최단 경로 계산

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

버그를 발견한다면, [Issues](https://github.com/woowacourse/atdd-subway-subwayGraph/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-subwayGraph/blob/master/LICENSE) licensed.
