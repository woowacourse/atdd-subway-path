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


# 기능 요구사항 

- [x] 출발역과 도착역 사이의 최단경로를 구할 수 있다.
  - [x] 갈 수 없는 경로를 조회하면 예외가 발생한다.
  - [x] 갈 수 있는 모든 구간 안에 도착지가 있는지 확인할 수 있다.
- [x] 해당 경로의 거리와 요금을 계산한다.
  - [x] 최단 경로의 거리를 구할 수 있다.
  - [x] 이동 거리마다 추가 운임이 부과된다.
    - [x] 10KM 이내이면 1250원으로 계산한다
    - [x] 10~50 KM는 5KM마다 100원 추가.
    - [x] 50 KM 초과는 8KM마다 100원 추가.능

# API 요구사항
- [ ] `GET /paths?source={sourseId}&target={targetId}&age={age}`
  - [ ] 응답으로 200 OK 상태를 반환
  - [ ] BODY에 지나온 경로와 거리, 요금을 반환한다.
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
