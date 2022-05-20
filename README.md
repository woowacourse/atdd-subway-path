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

## 기능 요구사항

### 경로 조회
  - **출발역과 도착역 사이의 최단 거리 경로**를 구하는 API를 설계한다.
  - 최단 거리 경로를 구할 때, `총 거리`와 `요금`을 같이 구해준다.
    - **요금 계산 방법**
        - 기본운임(`10㎞ 이내`): 기본운임 `1,250`원
        - 이용 거리 초과 시 추가운임 부과
          - `10km~50km`: `5km` 까지 마다 `100`원 추가
          - `50km 초과`: `8km` 까지 마다 `100`원 추가
  - 한 노선에서 뿐만 아니라, **여러 노선의 환승을 고려**해서 구해준다.
  


## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/woowacourse/atdd-subway-path/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-path/blob/master/LICENSE) licensed.
