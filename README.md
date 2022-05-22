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

## 🖍 기능 구현 목록

1. 지하철역 관리 기능
    - [x]  입력받은 이름으로 지하철 역 생성
        - [x]  이미 있는 역의 이름이 입력으로 들어올 경우 예외 발생
    - [x]  전체 지하철역 조회
    - [x]  입력받은 이름의 지하철역 삭제

2. 지하철 노선 관리 기능
    - [x]  입력받은 이름과 노선 컬러와 요금 노선 생성
        - [x]  이미 존재하는 노선과 이름 또는 노선 컬러가 겹칠 경우 예외 발생
    - [x]  지하철 전체 노선 조회
    - [x]  입력받은 지하철 노선의 정보 조회
    - [x]  지하철 노선 수정
        - [x]  수정하려는 이름 혹은 컬러가 다른 노선과 겹칠 경우 예외 발생
    - [x]  지하철 노선 삭제

3. 경로 조회 기능
    - [x] 경로 찾기
    - [x] 요금 계산

4. 요금 정책
    - [x] 기본운임비 `1,250원` + 이용 거리에 따른 초과 추가운임비
        - [x] 10km ~ 50km: 5km까지 마다 100원 추가
        - [x] 50km 초과: 8km까지 마다 100원 추가
    - [ ] 노선별 추가 요금 적용
    - [ ] 연령별 요금 할인 적용
        - [ ] 청소년(13세 이상~19세 미만): 운임에서 350원을 공제한 금액의 20%할인
        - [ ] 어린이(6세 이상~13세 미만): 운임에서 350원을 공제한 금액의 50%할인

## 🚀 Getting Started

### Usage

#### application 구동

```
./gradlew bootRun
```

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-map/blob/master/LICENSE) licensed.
