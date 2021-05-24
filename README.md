# 지하철 노선도 미션
스프링 과정 실습을 위한 지하철 노선도 애플리케이션

# 나의 미션 TODO

1. 인증 토큰 제대로 사용했는지 확인
   - 헤더에 넣는 방식이 올바른지
   - 토큰 key 선택 -> Email
    
2. 회원 정보 수정 후 토큰 재등록
   - 수정 정보로 다시 로그인 요청 수행
   
3. 인터셉터로 토큰 유효 선처리
   - CORS / pre-flight request
   - [읽어보기](https://ko.javascript.info/fetch-crossorigin)

4. 테스트 코드
   - Exception 상황을 가짜로 주입할 순 없을까?
    
5. 코드 리팩토링
   - DB 예외 발생 위치 dao vs service ??
   - Optional은 항상 옳을까? dao에서 그냥 터트리는게 더 나을 수 도 있지 않을까?
   - ```java
private void replaceSectionWithDownStation(Section newSection, Section existSection) {
   if (existSection.getDistance() <= newSection.getDistance()) {
      throw new RuntimeException();
   }
   this.sections.add(new Section(newSection.getDownStation(), existSection.getDownStation(), existSection.getDistance() - newSection.getDistance()));
   this.sections.remove(existSection);
}
```
   
6. 꼼꼼한 예외 처리
   - 회원 등록, 정보 변경 시 -> Email 중복 여부 확인 
   
## 🚀 Getting Started

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

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/woowacourse/atdd-subway-path/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-path/blob/master/LICENSE) licensed.
