package wooteco.subway.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dao.FakeLineDao;
import wooteco.subway.dao.FakeSectionDao;
import wooteco.subway.dao.FakeStationDao;

class RouteServiceTest {

    private RouteService routeService;

    @BeforeEach
    void setUp() {
        FakeLineDao.init();
        FakeSectionDao.init();
        FakeStationDao.init();
        routeService = new RouteService(new FakeLineDao(), new FakeSectionDao(), new FakeStationDao());
    }

    @DisplayName("경로를 조회한다.")
    @Test
    void showRoute() {
        Long sourceStationId = 1L;
        Long targetStationId = 5L;
        int age = 15;
        routeService.createRoute(sourceStationId, targetStationId, age);
    }
}
