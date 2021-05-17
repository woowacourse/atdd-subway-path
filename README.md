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

## ✏️ Code Review Process
[텍스트와 이미지로 살펴보는 온라인 코드 리뷰 과정](https://github.com/next-step/nextstep-docs/tree/master/codereview)

<br>

## 🐞 Bug Report

버그를 발견한다면, [Issues](https://github.com/woowacourse/atdd-subway-path/issues) 에 등록해주세요 :)

<br>

## 📝 License

This project is [MIT](https://github.com/woowacourse/atdd-subway-path/blob/master/LICENSE) licensed.




---
title: "토비의 스프링"
excerpt: 2장
permalink: /study/38
categories:
- study
- spring
- techcourse
  tags:
- study
- spring
- techcourse
  last_modified_at: 2021-05-11
---
# 토비의 스프링
## [2장] 테스트
스프링을 하면서 테스트를 안만든다? 바보다.
### [2.1] UserDaoTest 다시 보기
- 웹 단위 테스트의 문제점
  dao, service, controller, jsp, view 어디서 틀렸는지 빠르게 알 수 없다.
- 작은 단위 테스트
  외부 리소스에 의존하지 않는 단위 테스트를 진행하자.
- 지속적인 개선과 점진적인 개발을 위한 테스트
### [2.2] UserDaoTest 개선
- 테스트 검증의 자동화
  실행한 결과를 출력하도록 함으로써 스스로 실행해주고 결과까지 알려주는 자동화가 되었다.
- 테스트의 효율적인 수행과 결과 관리
  드디어 JUnit을 사용하자. 단순하고 실용적인 단위 테스트 제작에 도움을 준다.
  Junit 테스트로 전환: Junit도 프레임워크이고 이또한 IoC로 개발자가 만든 클래스의 객체를 생성하고 실행하는 일을 담당한다.
  테스트 메서드 전환: 메서드가 `public`, 메서드 위에 `@Test`
  검증 코드 전환: 여기서의 assertThat( )의 첫번째 파라미터는 matcher 사용하는 거, hamcrest 임포트하면 사용 가능하다. JUnit4라는데 아직 추가 안해봤다.
  `JUnitCore.main("springbook.user.dao.UserDaoTest");`를 메인 메서드에 넣으면 자동으로 테스트 돈다고 한다.
### [2.3] 개발자를 위한 테스팅 프레임워크 JUnit
JUnit 테스트는 main(), System.out.println()으로 만든 테스트라 단순하고 빠른 것이다.
- 중복 방지를 위한 deleteAll()과 getCount() 추가
  이때 `delete * from user`이 아니라 `delete from user`해야한다.
  전부 다 지울 때는 drop 써서인가? 첨 알았다..
  이 작업을 통해 항상 동일한 결과를 보장하는 테스트가 완성되었다.
  대충 성공하게 하는 테스트를 만드는 건 위험하단다.. 내 얘긴가..?
- getCount() 테스트
  한 번에 하나의 검증만 하도록
  처음 0 확인, 하나씩 추가하면서 증가 확인
  **디폴트 생성자 넣어주는게 자바빈 규약이란다..**
- addAndGet() 테스트 보완
  유저 추가하고 찾아서 이름, 비번 같은지 검사
- get() 예외조건 테스트
  id 값에 해당하는 정보가 없다면?
  null을 반환하거나, 예외를 던져 처리할 수 있다.
  여기서는 spring의 EmptyResultDataAccessException을 던지도록 한다고 했다.
  JUnit4에서는 `@Test(expected=EmptyResultDataAccessException.class)`를 주면 예외가 던져지길 기대하는 문법이라고 한다.
  우리는 성장한 JUnit5를 써서 assertThatThrownBy를 사용하도록 하자.
  항상 통과하는 테스트가 아닌 **네거티브 테스트를 먼저 만들라**고 로드 존슨님이 조언하셨다.
  빨리 테스트 만들고 넘어갈라고 초록불만 기다리지 말고 예외상황도 전부 테스트하자.
#### 테스트가 이끄는 개발
- 기능설계를 위한 테스트
  테스트는 기능을 어떻게 설계할지 보여주는 정의서가 될 수 있다.
  정의를 하고 이에 따른 프로덕션 코드를 만들면 테스트를 통해 검증까지 빠르게 할 수 있다.
  TDD의 원칙은 **실패한 테스트를 성공시키기 위한 목적이 아닌 코드는 만들지 않는다.** 였다.. 잊지 말자..
  TDD를 하면 코드를 짜고 실행하는 간격이 짧아 오류를 빠르게 대응할 수 있다는 것에 있다.
  테스트가 잘 구현되어야 코드에 대한 신뢰도 생기는 것이다. 돌아가기만 하는 테스트는 필요없다.
- 테스트 코드 개선
  테스트 코드도 리팩토링의 대상이다. 중복도 제거하고 이해하기 쉽고 변경이 쉬운 코드로 만들어야 한다.
  `@Before`을 통한 중복 제거
- JUnit의 테스트 수행 방식
  테스트 클래스에서 public, void인 `@Test` 찾기
  테스트 클래스 객체 생성
  @Before 메서드 있으면 실행
  @Test 붙은 메서드 하나 호출, 결과 저장
  @After 붙은 메서드 있으면 실행
  나머지 테스트 실행
  테스트 결과 반환
  이때 하나의 메서드를 실행할 때마다 독립적으로 실행되는 것을 보장해줒기 위해 매번 테스트 클래스를 버리고 새로 만든다.
  인스턴스 변수도 매번 새로 만들어준다.
  따라서 공통의 코드는 메서드 추출을 하거나, 별도의 클래스를 만드는 것도 방법이다.
  `픽스처`: 테스트를 수행하는데 필요한 정보나 오브젝트
  테스트 개수만큼 @Before 반복되고 애플리케이션 컨텍스트 생성(모든 싱글톤 빈 오브젝트 초기화)도 반복된다.
  또힌, 어떠한 빈은 리소스가 많이 필요하기도, 독립적으로 스레드를 사용하기도 한다.
  얘네들은 잘 안지우면 새로 만들 때 문제가 되기도 한다.
  이는 `@BeforeClass`로도 해결할 수 있다.
- 스프링 테스트 컨텍스트 프레임워크 적용
  간단한 어노테이션 설정으로 하나의 애플리케이션 컨텍스트만 만들고 공유할 수 있다.
  `ApplicationContext` 변수로 빼고 `@Autowired` 적용, 클래스에 `@RunWith`, `@ContextConfiguration` 선언하면 하나만 만들어서 공유할 수 있다고 한다.
  하지만 Junit4의 설정인지라 먹지가 않는다.
```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(location="/applicationContext.xml") // 텍스트가 만들어줄 위치 설정
```
이거를 수행하면 테스트 메서드 실행시마다 하나의 어플리케이션 컨텍스트가 사용됨을 알 수 있는데, 테스트가 만들어질 때마다 어플리케이션 컨텍스트 자신을 테스트 오브젝트의 특정 필드에 주입한다.
이렇게 만든 테스트는 돌릴수록 빨라진다.
- @Autowired
  autowired가 붙은 인스턴스 변수가 있으면 테스트 컨텍스트 프레임워크는 컨텍스트 내 빈을 찾는다.
  있으면 인스턴스 변수에 주입한다. 이때 필드의 타입정보만을 이용해 빈을 자동으로 가져오는 것을 자동와이어링이라고 한다.
  이렇게 `UserDao`도 주입받을 수 있다.
#### DI와 테스트
UserDao와 DB 커넥션 생성 클라스 사이에 DataSource Interface가 필요할까?
userDao는 자신이 쓰는 오브젝트 클래스가 뭔지 알 필요가 없다.
오브젝트 생성에 대한 부담도 없다.
다른 차원의 서비스 기능 도입에도 쉽다.
효율적인 테스트를 손쉽게 만들 수 있다.
- 테스트 코드에 의한 DI
  DI를 사용하면 작은 단위를 독립적으로 테스트하는데 중요한 역할을 한다.
  테스트를 위한 Datasource의 di를 위해 수동 DI를 한다. 하지만 애플리케이션 컨텍스트의 상태를 변경하는 것은 옳지 않으므로 하나의 메서드에서만 실행하고 이로인해 변경된 애플리케이션 컨텍스트를 폐기하고 새로운 컨텍스트를 생성하는 `@DirtiesContext`를 사용한다.
  또 다른 방법으로 테스트 전용 설정 파일을 만드는 것이 있다.
- 컨테이너 없는 DI 테스트
  사실 UserDao에서는 스프링 컨테이너를 동작하게 할 필요가 없다.
  필요한 오브젝트 생성과 관계설정을 테스트에서 직접해서 시간도 빨라지고 단순해졌다.
  DI 컨테이너나 프레임워크가 DI를 가능하게 해주는 것이 아니고 편리하게 사용하도록 해주는 것이다.(비침투적 기술)
### [2.5] 학습 테스트로 배우는 스프링
학습테스트란 내가 사용할 api나 프레임워크의 기능을 테스트로 구현하며 익히는 것
- 다양한 조건에 따른 기능을 손쉽게 확인
- 학습 테스트 코드를 개발 중에 참고 간으
- 프레임워크나 제품 업그레이드 시 호환성 검증에 도움
- 테스트 작성에 훈련
- 새로운 기술 공부 과정 해피~
#### 버그 테스트
오류를 드러내는 테스트
- 테스트 완성도 높아짐
- 버그 내용 명확하게 분석
- 기술 문제 해결에 도움
+ 동등분할: 모든 결과를 내는 상황을 조합해 테스트
+ 경계값 분석: 경계 모두 테스트





