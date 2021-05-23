# step3-1 피드백

- [x] `@RequestParam` 지양? 최소한의 검증로직
- [x] Path의 필드 줄이기
- [x] Path의 적절한 클래스명은?
- [x] Path에서 키를 Station이라는 Entity로 가지는게 부담 StationId를 키로 가지는 것은?
- [x] 하나의 API 로직에서 DB Access가 너무 많이 일어남. Path 쪽에 남긴 코멘트처럼 stationId 기반으로 Path가 수정된다면, stationId만 모아서 한번에 Station list 를 조회한 후 응답을 가공하도록 수정
- [x] 서비스 레이어의 주요 책임 중 하나인 트랜잭션 관리
- [ ] 서비스 레이어 테스트