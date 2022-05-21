package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.service.ServiceTestFixture.deleteAllStation;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dao.JdbcStationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.StationServiceRequest;
import wooteco.subway.service.dto.StationResponse;

@JdbcTest
class StationServiceTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private StationService stationService;

    @BeforeEach
    void setUp() {
        StationDao stationDao = new JdbcStationDao(jdbcTemplate);
        deleteAllStation(stationDao);
        stationService = new StationService(stationDao);
    }

    @Test
    void save() {
        // given
        StationServiceRequest station = new StationServiceRequest("범고래");

        // when
        StationResponse result = stationService.save(station);

        // then
        String stationName = station.getName();
        String resultName = result.getName();
        assertThat(stationName).isEqualTo(resultName);
    }

    @Test
    void validateDuplication() {
        // given
        Station station1 = new Station("범고래");
        Station station2 = new Station("범고래");

        // when
        stationService.save(new StationServiceRequest(station1.getName()));

        // then
        assertThatThrownBy(
            () -> stationService.save(new StationServiceRequest(station2.getName())))
            .hasMessage("중복된 이름이 존재합니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAll() {
        // given
        StationResponse station1 = stationService.save(new StationServiceRequest("범고래"));
        StationResponse station2 = stationService.save(new StationServiceRequest("애쉬"));

        // when
        List<StationResponse> stations = stationService.findAll();

        // then
        assertThat(stations).filteredOn((station) -> station.getName().equals(station1.getName()))
            .hasSize(1);
        assertThat(stations).filteredOn((station) -> station.getName().equals(station2.getName()))
            .hasSize(1);
    }

    @Test
    void deleteById() {
        // given
        StationResponse savedStation = stationService.save(new StationServiceRequest("범고래"));

        // when
        stationService.deleteById(savedStation.getId());
        List<StationResponse> stations = stationService.findAll();

        // then
        assertThat(stations).filteredOn(
                (station) -> station.getName().equals(savedStation.getName()))
            .hasSize(0);
    }
}
