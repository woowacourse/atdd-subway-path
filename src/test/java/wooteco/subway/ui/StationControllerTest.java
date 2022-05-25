package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.station.StationRequest;
import wooteco.subway.dto.station.StationResponse;
import wooteco.subway.exception.station.DuplicateStationException;
import wooteco.subway.exception.station.NoSuchStationException;

class StationControllerTest extends ControllerTest {

    @Autowired
    private StationController stationController;

    @Test
    @DisplayName("지하철 역을 생성한다.")
    void CreateStation() {
        // given
        final String name = "강남역";
        final StationRequest request = new StationRequest(name);

        // when
        final ResponseEntity<StationResponse> response = stationController.createStation(request);

        final StationResponse actual = response.getBody();

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(actual.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("지하철 역 이름이 중복되면 예외를 던진다.")
    void CreateStation_DuplicateName_ExceptionThrown() {
        // given
        final String name = "강남역";
        stationDao.insert(new Station(name));

        final StationRequest request = new StationRequest(name);

        // when, then
        assertThatThrownBy(() -> stationController.createStation(request))
                .isInstanceOf(DuplicateStationException.class);
    }

    @Test
    @DisplayName("지하철 역 목록을 조회한다.")
    void ShowStations() {
        // given
        final List<String> expectedNames = List.of(
                "강남역",
                "역삼역",
                "선릉역"
        );

        for (String name : expectedNames) {
            final Station station = new Station(name);
            stationDao.insert(station);
        }

        // when
        final ResponseEntity<List<StationResponse>> response = stationController.showStations();

        final List<String> actualNames = response.getBody()
                .stream()
                .map(StationResponse::getName)
                .collect(Collectors.toList());

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(actualNames).isEqualTo(expectedNames);
    }

    @Test
    @DisplayName("지하철 역을 삭제한다.")
    void DeleteStation() {
        // given
        final Long id = stationDao.insert(new Station("강남역"))
                .orElseThrow()
                .getId();

        // when
        final ResponseEntity<Void> response = stationController.deleteStation(id);

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("존재하지 않는 역을 삭제하면 예외를 던진다.")
    void DeleteStation_NotExist_ExceptionThrown() {
        // when, then
        assertThatThrownBy(() -> stationController.deleteStation(999L))
                .isInstanceOf(NoSuchStationException.class);
    }
}