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

## 기능 구현 목록

용어
* 청소년: 13세 이상 19세 미만
* 어린이: 6세 이상 13세 미만

- [ ] 노선별 추가 요금 정책을 추가한다.
  - [ ] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가한다.
  - [ ] 경로 중 추가요금이 있는 노선을 환승 하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.
  - [x] 노선을 등록, 수정할 때 추가 요금 정보를 받는다.
  - [x] 노선을 조회할 때, 추가 요금 정보를 응답한다.
- [ ] 연령별 요금 할인 정책을 추가한다.
  - [ ] 청소년의 경우 운임 요금에서 350원을 공제한 금액의 20%를 할인한다.
  - [ ] 어린이의 경우 운임 요금에서 350원을 공제한 금액의 50%를 할인한다.

* 노선 생성 수정시, extraFare 검증처리

## 인수 조건

### 경로를 조회한다.

1. 추가 요금이 있는 노선을 10키로 미만 이용한다.(환승은 하지 않는다)
2. 추가 요금이 있는 노선들을 환승한다.
3. 청소년이 노선을 이용한다.
4. 어린이가 노선을 이용한다.

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
