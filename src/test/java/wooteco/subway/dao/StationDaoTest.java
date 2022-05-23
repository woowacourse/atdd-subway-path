package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.test.context.TestConstructor.AutowireMode;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.application.StationService;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@SpringBootTest
@Transactional
@TestConstructor(autowireMode = AutowireMode.ALL)

class StationDaoTest {

    private final StationDao stationDao;
    private final StationService stationService;

    private Station 잠실역;
    private Station 선릉역;

    public StationDaoTest(StationDao stationDao, StationService stationService) {
        this.stationDao = stationDao;
        this.stationService = stationService;
    }

    @BeforeEach
    void setUp() {
        잠실역 = stationService.save(new StationRequest("잠실역"));
        선릉역 = stationService.save(new StationRequest("선릉역"));
    }

    @DisplayName("id로 역을 조회한다.")
    @Test
    void queryById() {
        StationResponse stationResponse = stationDao.queryById(잠실역.getId()).orElseThrow();
        assertThat(stationResponse.getId()).isEqualTo(잠실역.getId());
        assertThat(stationResponse.getName()).isEqualTo(잠실역.getName());
    }

    @DisplayName("모든 역을 조회한다.")
    @Test
    void queryAll() {
        List<StationResponse> stationResponses = stationDao.queryAll();
        assertThat(stationResponses).containsExactly(new StationResponse(잠실역), new StationResponse(선릉역));
    }
}
