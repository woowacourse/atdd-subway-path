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

### 기능 요구 사항
1. 최단 경로 기능
   - 출발역과 도착역을 입력받아 최단 거리를 구한다.
   - 출발역과 도착역이 존재하지 않는 역인 경우 예외를 발생시킨다.
   - 출발역에서 도착역으로 갈 수 없는 경우 예외를 발생시킨다.
   
2. 요금 계산 기능
   - 10km까지는 1250원
   - 10km~50km까지는 5km마다 100원 추가
   - 50km~는 8km마다 100원 추가
   - 노선에 따라 추가 요금을 부과한다.
     - 추가 요금이 있는 노선을 이용할 경우 측정된 요금에 추가한다.
     - 추가요금이 있는 노선을 환승하여 이용하는 경우, 가장 높은 금액의 추가 요금만 적용한다.
   - 나이에 따라 할인을 적용한다.
     - 13세 이상 19세 미만인 경우, 운임에서 350원을 공제한 금액의 80% 결제
     - 6세 이상 13세 미만인 경우, 운임에서 350원을 공제한 금액의 50% 결제
