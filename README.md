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

## 기능 목록 🛠

- 경로 조회 기능
    - [X] 출발역, 도착역을 입력하면 최딘 거리를 계산한다.
      - [X] [ERROR] 출발역과 도착역이 같을 경우 예외를 발생시킨다.
      - [ ] [ERROR] 갈 수 없는 경로일 경우 예외를 발생시킨다.
    - 최단 거리에 대한 요금을 계산한다.
        - [X] 기본운임(10㎞ 이내): 기본운임 1,250원
        - 이용 거리 초과 시 추가운임 부과
            - [X] 10km~50km: 5km 까지 마다 100원 추가
            - [X] 50km 초과: 8km 까지 마다 100원 추가

<br>

## 페어 규칙 🧨

- 하나의 노트북으로 진행하며, 교대 시간은 13분을 기준으로 한다.
- 50분 단위로 10분씩 쉬는 시간을 가진다.
- 최대한 `final` 키워드를 활용한다.
- 인수 테스트부터 시작해 하향식으로 구현을 해나간다.
- 커밋 단위로 간단히 리팩토링을 같이 진행한다.
- 일일회고를 진행한다.
- 페어 종료 후 각자 리팩토링을 하면서 정보를 공유한다.
- 화요일 10시 기능 구현 완료를 목표로한다.

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
