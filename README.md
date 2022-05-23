# 지하철 경로 조회 미션 

## 1단계 기능 요구사항 작성

- [x] 지하철 노선 추가요금 추가
- [x] 지하철 전체 경로 조회
- [x] 지하철 목적지 최단 경로 얻기
- [x] 지하철 경로 거리 계산
- [x] 지하철 경로 요금 계산

## 1단계 리팩토링 목록
- [x] 예상치 못한 예외 발생시, 서버 관리자도 알도록 로그 추가
- [x] distance, extraFare 도메인에 검증 로직 작성
- [x] 경로 검색 예외 처리 기능 추가
  - [x] 출발지와 도착지가 구간으로 연결되어 있지 않아 경로를 찾을 수 없는 경우 예외 발생
- [x] calculateShortestPath, calculateShortestDistance 내부에서 최단 경로 구하는 로직이 중복 제거
- [x] stations 검증부 가독성 높이도록 수정
- [x] soft delete 방식 활용하도록 save 수정

## 2단계 기능 요구사항 작성
- [ ] 최단 경로 중 추가요금이 있는 경우, 가장 높은 금액의 추가요금 적용
- [ ] 연령별 요금 할인 적용
  - [ ] 6~12세 -> (기존 요금 -350원) * 0.8
  - [ ] 13~18세 -> (기존 요금 -350원) * 0.5
  - [ ] 0~5세, 65세~ -> 0원 부과