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

## 요구사항 정리
- [x] 출발역과 도착역 사이의 최단 경로 및 요금을 구하는 API를 만든다.
  - [x] 요청에 대한 검증을 추가한다.
    - [x] 등록되어있지 않은 역에 대해 경로 검색을 할 경우 예외가 발생한다.
    - [x] 같은 역 사이의 경로 검색 요청이 있을 경우 예외가 발생한다.
    - [x] 노선에 연결되어있지 않은 역을 이용해 경로 검색시 예외가 발생한다. (역은 등록되어있지만 구간에는 등록되어있지 않은 경우)
    - [x] 검색한 경로를 이동할 수 없는 경우 예외가 발생한다. (환승을 하여도 이동할 수 없는 경우)
  - [x] 최단 경로를 구한다.
    - [x] 한 노선뿐만 아니라 환승도 고려한다.
    - [x] 최단 경로는 다익스트라 라이브러리를 활용한다.
  - [x] 거리에 따른 요금을 계산한다.
    - [x] 10km 까지는 1250원이다.
    - [x] 10~50km 까지는 5km마다 100원씩 증액한다.
    - [x] 50km 초과부터는 8km마다 100원씩 증액한다.
- [x] 요금 정책 추가한다.
  - [x] 요금 정책 추가에 따른 노선 생성/업데이트 검증을 변경 및 추가한다.
    - [x] 노선 생성시, 노선 추가요금 입력이 없을시 추가요금이 0원으로 설정된다.
    - [x] 노선 추가요금이 0 이상의 정수가 아니면 예외를 발생시킨다.
  - [x] 노선별로 추가 요금 정책을 추가한다.
    - [x] 추가요금이 있는 노선을 이용하면 요금이 추가된다.
    - [x] 경로중 추가 요금이 있는 여러 노선을 이용할 경우 가장 높은 금액의 추가 요금을 적용한다.
  - [x] 연령별 요금 할인 정책을 추가한다.
    - [x] 청소년: 운임에서 350원을 공제한 금액의 20%할인한다.
    - [x] 어린이: 운임에서 350원을 공제한 금액의 50%할인한다.
  

- [API 문서](https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/c4c291f19953498e8eda8a38253eed51#Path)

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
