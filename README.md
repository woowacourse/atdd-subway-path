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
@@ -37,3 +43,20 @@
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
