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
import wooteco.subway.acceptance.DBTest;
import wooteco.subway.service.dto.StationServiceResponse;

@SpringBootTest
class StationServiceTest extends DBTest {

    private static final String SEOLLEUNG = "선릉역";

    private final StationService stationService;

    @Autowired
    public StationServiceTest(StationService stationService) {
        this.stationService = stationService;
    }

    @DisplayName("지하철 역을 저장한다.")
    @Test
    void save() {
        String stationName = "선릉역";
        StationServiceResponse stationServiceResponse = stationService.save(stationName);

        assertThat(stationServiceResponse.getName()).isEqualTo(stationName);
    }

    @DisplayName("같은 이름의 지하철 역을 저장하는 경우 예외가 발생한다.")
    @Test
    void saveExistingName() {
        stationService.save(SEOLLEUNG);

        assertThatThrownBy(() -> stationService.save(SEOLLEUNG))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("모든 지하철 역을 조회한다.")
    @Test
    void findAll() {
        String stationName1 = "선릉역";
        String stationName2 = "잠실역";
        String stationName3 = "사우역";
        StationServiceResponse station1 = stationService.save(stationName1);
        StationServiceResponse station2 = stationService.save(stationName2);
        StationServiceResponse station3 = stationService.save(stationName3);

        List<StationServiceResponse> stations = stationService.findAll();

        assertAll(
                () -> assertThat(stations.get(0).getId()).isEqualTo(station1.getId()),
                () -> assertThat(stations.get(0).getName()).isEqualTo(station1.getName()),
                () -> assertThat(stations.get(1).getId()).isEqualTo(station2.getId()),
                () -> assertThat(stations.get(1).getName()).isEqualTo(station2.getName()),
                () -> assertThat(stations.get(2).getId()).isEqualTo(station3.getId()),
                () -> assertThat(stations.get(2).getName()).isEqualTo(station3.getName())
        );
    }

    @DisplayName("지하철 역을 삭제한다.")
    @Test
    void deleteById() {
        StationServiceResponse stationServiceResponse = stationService.save(SEOLLEUNG);

        stationService.deleteById(stationServiceResponse.getId());

        assertThat(stationService.findAll().size()).isZero();
    }
}
