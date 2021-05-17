<p align="center">
    <img width="200px;" src="https://raw.githubusercontent.com/woowacourse/atdd-subway-admin-frontend/master/images/main_logo.png"/>
</p>
<p align="center">
  <img alt="npm" src="https://img.shields.io/badge/npm-%3E%3D%205.5.0-blue">
  <img alt="node" src="https://img.shields.io/badge/node-%3E%3D%209.3.0-blue">
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

### Install
#### npm 설치
```
cd frontend
npm install
```
> `frontend` 디렉토리에서 수행해야 합니다.

### Usage
#### webpack server 구동
```
npm run serve
```
#### application 구동
```
./gradlew bootRun
```
<br>

## 구현할 기능 목록
- [x] 프론트 작업
    - [x] 회원 관리/로그인 관리
        - [x] 멤버 생성 API 호출
        - [x] 로그인 API 호출
        - [x] 멤버 데이터 불러오는 API 호출
        - [x] 멤버 정보 업데이트 API 호출
        - [x] 멤버 정보 삭제 API 호출
    - [x] 역 관리
        - [x] 역 조회 API 호출
        - [x] 역 추가 API 호출
        - [x] 역 삭제 API 호출
    - [x] 노선 관리
        - [x] 노선 목록 조회 API 호출
        - [x] 노선 추가 API 호출
        - [x] 노선 삭제 API 호출
        - [x] 노선 수정 API 호출
    - [x] 구간 관리
        - [x] 구간 추가 API 호출
        - [x] 구간 삭제 API 호출
    - [ ] 경로 관리
        - [ ] 최단 거리 검색 API 호출
        - [ ] 최단 거리 경로조회 API 호출

- [x] 백엔드 작업
    - [x] JWT 라이브러리를 통한 액세스 토큰 발급하기
        - [x] POST: /login/token 구현하기
    - [ ] Login Interceptor 구현하기
    - [x] 발급한 토큰을 기반으로 회원 관리 기능 추가하기
        - [x] GET: /members/me 구현하기
        - [x] PUT: /members/me 구현하기
        - [x] DELETE: /members/me 구현하기
    - [ ] 출발역과 도착역 사이의 최단 경로 구하는 API 구현하기
        - [ ] jgrapht 라이브러리 활용하기
        - [ ] PathAcceptanceTest 통과하도록 구현하기
        - [ ] 경로와 함께 총 거리를 출력하기
            - [ ] 환승도 고려하기
        
## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/woowacourse/atdd-subway-path/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-path/blob/master/LICENSE) licensed.
