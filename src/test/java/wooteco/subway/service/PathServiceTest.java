package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dao.FakeLineDao;
import wooteco.subway.dao.FakeSectionDao;
import wooteco.subway.dao.FakeStationDao;

class PathServiceTest {

    private PathService pathService;

    @BeforeEach
    void setUp() {
        FakeSectionDao.init();
        FakeStationDao.init();
        pathService = new PathService(new FakeSectionDao(), new FakeStationDao());
    }

    @DisplayName("경로를 조회한다.")
    @Test
    void showRoute() {
        Long sourceStationId = 1L;
        Long targetStationId = 5L;
        int age = 15;
        pathService.createPath(sourceStationId, targetStationId, age);
    }
}
