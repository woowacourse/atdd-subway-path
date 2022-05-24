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

## 1단계 요구사항 도출

### 경로

- [x] 경로를 조회한다.
    - [x] 출발역과 도착역 사이에 최단 거리 경로를 구한다.
    - [x] 여러 노선의 환승도 고려한다.
- [x] 거리를 계산한다.

### 요금

- [x] 요금을 계산한다. (요금은 거리비례제로 계산된다.)
    - [x] 기본운임(10㎞ 이내): 기본운임 1,250원 
    - [x] 이용 거리 초과 시 추가운임 부과 
      - [x] 10km~50km: 5km 까지 마다 100원 추가 
      - [x] 50km 초과: 8km 까지 마다 100원 추가

## 2단계 요구사항 도출

### 노선

- [ ] 추가 요금이 있는 노선을 이용 할 경우 측정된 요금에 추가한다.
- [ ] 경로 중 추가요금이 있는 노선을 환승하여 이용 할 경우 가장 높은 금액의 추가 요금만 적용한다.

### 요금
- [ ] 청소년은 운임에서 350원을 공제한 금액의 20% 할인한다. (청소년: 13세 이상~19세 미만)
- [ ] 어린이는 운임에서 350원을 공제한 금액의 50% 할인한다. (어린이: 6세 이상~13세 미만)

## 리팩토링할 사항 정리
- [x] Math.ceil 5, 8 int에서 double로 바꾸기
- [x] Custom exception 합치기 (구체적인것 => 포괄적인것)
- [x] Exception, RuntimeException 메시지 숨기기
- [ ] Graph 라이브러리 주입받도록 변경(인터페이스를 선언하고 구현체 주입받기)

