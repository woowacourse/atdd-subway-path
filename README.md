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
      - [X] [ERROR] 갈 수 없는 경로일 경우 예외를 발생시킨다.
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

## 일일 회고 😉

<details>
<summary>2022.05.17</summary>

### **느낀점**

`엘리`: 디버깅하느라 너무 시간이 오래 걸리고 초조함을 안겨드린것 같아 죄송합니다... 노력하겠습니다. 앞으로 개발 얘기 페어 하면서 많이 나누면 재밌을 것 같습니다.

`아서`: 반성을 많이 했다. 귀찮은 일을 마다하지 않고 적극적으로 해봐야겠다. 고민을 했지만 적용해보지 않은 부분이 많았다.

### **페어에게 좋았던 점**

`엘리`: 일단 텐션이 좀 잘맞는것같다. 내가 생각한 부분에 대한 아서의 다른 생각을 말해줘서 너무 좋았다.

`아서`: 테스트에 대해 배울 수 있는 점이 있었다. 고민을 코드로 적용한 부분이 있어 배울점이 많았다.

### **아쉬웠던 점**

`엘리`: 아서의 코드에 더 자신감이 있었으면 좋겠다.

`아서`: MockMvc를 공부하고 쓰면 좋겠다. 테스트 툴에 대해 앞으로 같이 공부해나가면 좋겠다.
</details>

<details>
<summary>2022.05.18</summary>

### **느낀점**

`엘리`: 생각보다 빨리 끝난듯 아닌듯. 이전 미션 과정에서의 흔적들이 시간을 많이 잡아먹은 것 같아 아쉽다.

`아서`: 레거시를 디버깅하는 시간이 있어서 좋았다. 테스트를 어떻게 하는게 좋을까에 대한 고민을 본격적으로 해봐야겠다. 테스트의 중복을 어떻게 제거할지 고민을 해봐야겠다.

### **페어에게 좋았던 점**

`엘리`: 테스트의 필요성에 대해 의문을 제기해주셔서 이 부분에 대해 고민할 수 있었던 것 같아 좋았다. 내가 작성했었던 코드에서 문제가 발생했을 때 그래도 기운을 북돋아주시는 말들을 해줘서 너무 고마웠다. 트랙패드를 빌려줘서 너무너무 고마웠다.

`아서`: 테스트에 대해서 본인이 경험한 것을 공유해주고, 비슷한 환경이 나왔을 때 책임감있게 디버깅해줘서 고마웠다.

### **아쉬웠던 점**

`엘리`: 도메인 객체를 나누는 방식이 그동안 내가 생각했던 방식과 조금 다른 부분이 있어서 이것을 이해하는데 스스로 어려움이 있었다. 결국 서로 이야기를 통해 만족스럽게 객체를 분리할 수 있었던 것 같다.

`아서`: 레거시코드에 추가로 개발을 하는 과정이 좀 어려웠다.
</details>

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
