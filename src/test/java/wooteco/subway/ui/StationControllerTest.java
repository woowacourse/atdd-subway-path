package wooteco.subway.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wooteco.subway.ui.dto.StationRequest;
import wooteco.subway.ui.dto.StationResponse;

class StationControllerTest extends ControllerTest {

    @Autowired
    private StationController stationController;

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        StationRequest stationRequest = new StationRequest("강남역");

        // when
        ResponseEntity<StationResponse> response = stationController.createStation(stationRequest);

        // then
        HttpStatus statusCode = response.getStatusCode();
        StationResponse actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(actual.getId()).isEqualTo(1L),
                () -> assertThat(actual.getName()).isEqualTo("강남역")
        );
    }

    @DisplayName("중복된 이름을 가진 지하철역을 생성하면 예외가 발생한다.")
    @Test
    void createDuplicateStation() {
        // given
        StationRequest stationRequest = new StationRequest("강남역");

        // when
        stationController.createStation(stationRequest);

        // then
        assertThatThrownBy(() -> stationController.createStation(stationRequest))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void showStations() {
        // given
        StationRequest stationRequest1 = new StationRequest("강남역");
        StationRequest stationRequest2 = new StationRequest("역삼역");
        ResponseEntity<StationResponse> stationResponse1 = stationController.createStation(stationRequest1);
        ResponseEntity<StationResponse> stationResponse2 = stationController.createStation(stationRequest2);

        // when
        ResponseEntity<List<StationResponse>> response = stationController.showStations();

        // then
        HttpStatus statusCode = response.getStatusCode();
        List<StationResponse> actual = response.getBody();

        assertAll(
                () -> assertThat(statusCode).isEqualTo(HttpStatus.OK),
                () -> assertThat(actual.size()).isEqualTo(2),
                () -> assertThat(actual.stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList()))
                        .containsExactly(stationResponse1.getBody().getId(), stationResponse2.getBody().getId()),
                () -> assertThat(actual.stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList()))
                        .containsExactly(stationResponse1.getBody().getName(), stationResponse2.getBody().getName())
        );
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationRequest stationRequest1 = new StationRequest("강남역");
        ResponseEntity<StationResponse> stationResponse = stationController.createStation(stationRequest1);

        // when
        ResponseEntity<Void> response = stationController.deleteStation(stationResponse.getBody().getId());

        // then
        HttpStatus statusCode = response.getStatusCode();

        assertThat(statusCode).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
