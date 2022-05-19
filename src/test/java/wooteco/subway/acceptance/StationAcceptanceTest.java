package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;
import java.util.List;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    private StationRequest stationRequest;

    @BeforeEach
    void setup() {
        stationRequest = new StationRequest("강남역");
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        final ExtractableResponse<Response> response = post("/stations", stationRequest);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(response.header("Location")).isNotBlank()
        );
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성할 경우 예외를 발생한다.")
    @Test
    void createStationWithDuplicateName() {
        post("/stations", stationRequest);

        final ExtractableResponse<Response> response = post("/stations", stationRequest);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        final StationRequest stationRequest1 = new StationRequest("강남역");
        final StationRequest stationRequest2 = new StationRequest("역삼역");
        final ExtractableResponse<Response> createResponse1 = post("/stations", stationRequest1);
        final ExtractableResponse<Response> createResponse2 = post("/stations", stationRequest2);
        final StationResponse stationResponse1 = createResponse1.jsonPath().getObject(".", StationResponse.class);
        final StationResponse stationResponse2 = createResponse2.jsonPath().getObject(".", StationResponse.class);

        final ExtractableResponse<Response> response = get("/stations");

        assertThat(response.jsonPath().getList(".", StationResponse.class)).usingRecursiveComparison()
                .isEqualTo(List.of(stationResponse1, stationResponse2));
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        final ExtractableResponse<Response> createResponse = post("/stations", stationRequest);
        final String uri = createResponse.header("Location");

        final ExtractableResponse<Response> response = delete(uri);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
