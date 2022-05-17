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
- 출발역과 도착역 사이의 최단 경로 및 요금을 구하는 API만들기
  - 최단 경로를 구한다.
    - 한 노선뿐만 아니라 환승도 고려한다.
    - 최단 경로는 다익스트라 라이브러리를 활용한다.
  - 거리에 따른 요금을 계산한다.
    - 10km 까지는 1250원이다.
    - 10~50km 까지는 5km마다 100원씩 증액한다.
    - 50km 이상부터는 8km마다 100원씩 증액한다.

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
