package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.dao.StationDao;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.DuplicateNameException;
import wooteco.subway.exception.NotFoundStationException;
import wooteco.subway.repository.JdbcStationRepository;
import wooteco.subway.repository.StationRepository;

@JdbcTest
class StationServiceTest {

    private final StationService stationService;

    @Autowired
    public StationServiceTest(JdbcTemplate jdbcTemplate) {
        StationDao stationDao = new StationDao(jdbcTemplate);
        StationRepository stationRepository = new JdbcStationRepository(stationDao);
        stationService = new StationService(stationRepository);
    }

    @DisplayName("새로운 지하철역을 등록한다.")
    @Test
    void createStation() {
        // given
        String name = "선릉역";
        StationRequest stationRequest = new StationRequest(name);

        // when
        StationResponse stationResponse = stationService.createStation(stationRequest);

        // then
        assertThat(stationResponse.getName()).isEqualTo(name);
    }

    @DisplayName("중복된 이름의 지하철역을 등록할 경우 예외를 발생한다.")
    @Test
    void createStation_throwsExceptionWithDuplicateName() {
        // given
        String name = "선릉역";
        StationRequest stationRequest = new StationRequest(name);
        stationService.createStation(stationRequest);

        // when & then
        assertThatThrownBy(() -> stationService.createStation(stationRequest))
                .isInstanceOf(DuplicateNameException.class);
    }

    @DisplayName("등록된 모든 지하철역을 반환한다.")
    @Test
    void getAllStations() {
        // given
        String name1 = "선릉역";
        String name2 = "역삼역";
        String name3 = "강남역";
        StationRequest stationRequest1 = new StationRequest(name1);
        StationRequest stationRequest2 = new StationRequest(name2);
        StationRequest stationRequest3 = new StationRequest(name3);

        stationService.createStation(stationRequest1);
        stationService.createStation(stationRequest2);
        stationService.createStation(stationRequest3);

        // when
        int actual = stationService.getAllStations().size();

        // then
        assertThat(actual).isEqualTo(3);
    }

    @DisplayName("등록된 지하철역을 삭제한다.")
    @Test
    void delete() {
        // given
        StationRequest stationRequest = new StationRequest("선릉역");
        StationResponse stationResponse = stationService.createStation(stationRequest);

        // when
        stationService.delete(stationResponse.getId());
        List<StationResponse> allStations = stationService.getAllStations();

        // then
        assertThat(allStations).hasSize(0);
    }

    @DisplayName("삭제하려는 지하철 역 ID가 존재하지 않을 경우 예외를 발생한다.")
    @Test
    void delete_throwsExceptionIfIdNotExist() {
        assertThatThrownBy(() -> stationService.delete(100L))
                .isInstanceOf(NotFoundStationException.class);
    }
}
