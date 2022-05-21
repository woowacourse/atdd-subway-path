package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import wooteco.subway.service.StationService;
import wooteco.subway.service.dto.StationResponse;
import wooteco.subway.ui.dto.StationRequest;
import wooteco.subway.utils.RestAssuredUtil;

@DisplayName("지하철역 관련 기능 - StationAcceptanceTest")
public class StationAcceptanceTest extends AcceptanceTest {

    private Long stationId1;
    private Long stationId2;

    @Autowired
    private StationService stationService;

    @BeforeEach
    void init() {
        stationId1 = stationService.save(new StationRequest("강남역")).getId();
        stationId2 = stationService.save(new StationRequest("역삼역")).getId();
    }

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        StationRequest stationRequest = new StationRequest("선릉역");

        // when
        ExtractableResponse<Response> response = RestAssuredUtil.post("/stations", stationRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        StationRequest stationRequest = new StationRequest("강남역");

        ExtractableResponse<Response> response = RestAssuredUtil.post("/stations", stationRequest);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given

        // when
        ExtractableResponse<Response> response = RestAssuredUtil.get("/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Long> stationIds = generateStationIds(response);

        assertThat(stationIds).containsAll(List.of(stationId1, stationId2));
    }

    private List<Long> generateStationIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        StationRequest stationRequest = new StationRequest("잠실역");
        ExtractableResponse<Response> createResponse = RestAssuredUtil.post("/stations", stationRequest);

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = RestAssuredUtil.delete(uri, new HashMap<>());

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
