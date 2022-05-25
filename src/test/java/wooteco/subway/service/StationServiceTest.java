package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.StationServiceResponse;

@SpringBootTest
class StationServiceTest extends ServiceTest {

    private static final String 강남역 = "강남역";
    private static final String 선릉역 = "선릉역";
    private static final String 잠실역 = "잠실역";

    private final StationService stationService;

    @Autowired
    public StationServiceTest(StationService stationService) {
        this.stationService = stationService;
    }

    @DisplayName("지하철 역을 저장한다.")
    @Test
    void save() {
        final StationServiceResponse stationServiceResponse = stationService.save(강남역);

        assertThat(stationServiceResponse.getName()).isEqualTo(강남역);
    }

    @DisplayName("같은 이름의 지하철 역을 저장하는 경우 예외가 발생한다.")
    @Test
    void saveExistingName() {
        stationService.save(강남역);

        assertThatThrownBy(() -> stationService.save(강남역))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("모든 지하철 역을 조회한다.")
    @Test
    void findAll() {
        final StationServiceResponse station1 = stationService.save(강남역);
        final StationServiceResponse station2 = stationService.save(선릉역);
        final StationServiceResponse station3 = stationService.save(잠실역);

        final List<StationServiceResponse> stations = stationService.findAll();

        assertAll(
                () -> assertThat(stations.get(0).getId()).isEqualTo(station1.getId()),
                () -> assertThat(stations.get(0).getName()).isEqualTo(station1.getName()),
                () -> assertThat(stations.get(1).getId()).isEqualTo(station2.getId()),
                () -> assertThat(stations.get(1).getName()).isEqualTo(station2.getName()),
                () -> assertThat(stations.get(2).getId()).isEqualTo(station3.getId()),
                () -> assertThat(stations.get(2).getName()).isEqualTo(station3.getName())
        );
    }

    @DisplayName("노선에 해당하는 지하철역들을 반환한다.")
    @Test
    @Sql("classpath:lineStations.sql")
    void findAllByLineId() {
        final List<Station> stationIds = stationService.findAllByLineId(1L);
        assertThat(stationIds).containsExactly(
                new Station(1L, 강남역),
                new Station(2L, 선릉역),
                new Station(3L, 잠실역));
    }

    @DisplayName("지하철 역을 삭제한다.")
    @Test
    void deleteById() {
        final StationServiceResponse stationServiceResponse = stationService.save(선릉역);

        stationService.deleteById(stationServiceResponse.getId());

        assertThat(stationService.findAll().size()).isZero();
    }
}
