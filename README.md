<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <a href="https://techcourse.woowahan.com/c/Dr6fhku7" alt="woowacourse subway">
    <img alt="Website" src="https://img.shields.io/website?url=https%3A%2F%2Fedu.nextstep.camp%2Fc%2FR89PYi5H">
  </a>
  <img alt="GitHub" src="https://img.shields.io/github/license/woowacourse/atdd-subway-map">
</p>

<br>

# 지하철 노선도 미션
스프링 과정 실습을 위한 지하철 노선도 애플리케이션  

## 3단계 도메인 기능 요구사항  
- [x] 최단 경로 찾기
- [x] 요금 계산하기


## api 요구사항  
- [x] 경로 조회



# 경로 조회 2단계
노선별 추가 요금
- [x] line entity가 extra_fare 칼럼 갖도록 수정
- [x] line domain이 extraFare 필드 갖도록 수정
- [x] 추가 요금을 받도록 api 수정
- [x] 추가 요금이 있는 노선을 이용할 경우 측정된 요금에 추가한다.
- [x] 환승하는 경우 가장 높은 금액의 추가 요금만 적용한다.
연령별 추가 
- [ ] 청소년은 운임에서 350원 공제한 금액의 20%를 할인한다.
- [ ] 어린이는 운임에서 350원을 공제한 금액의 50%를 할인한다.


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

버그를 발견한다면, [Issues](https://github.com/woowacourse/atdd-subway-map/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-map/blob/master/LICENSE) licensed.
