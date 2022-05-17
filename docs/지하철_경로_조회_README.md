## 1단계 요구사항

> [제공된 페이지](https://d2owgqwkhzq0my.cloudfront.net/index.html) 를 통해 테스트할 수 있다.  
> [API 문서](https://techcourse-storage.s3.ap-northeast-2.amazonaws.com/c4c291f19953498e8eda8a38253eed51#Path) 에 따른다.

- 경로 조회 API 구현
    - 최단 경로 라이브러리 `JGraphT` 이용
    - 요금 계산
        - 기본 금액 1250원 (10km까지 적용)
        - 5km마다 100원 추가 (50km까지 적용)
        - 8km마다 100원 추가
- 기존 API 수정
    - 지하철 노선에 extraFare 정보 추가

## 페어 규칙

- 7시까지 페어 진행
- 5분 단위로 드라이빙 하되, 너무 짧다고 느껴지면 유동적으로 변경
- 2시간 코딩 + 10분 쉬는 시간
- TDD 하기