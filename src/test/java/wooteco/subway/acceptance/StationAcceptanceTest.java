package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dto.request.StationRequest;
import wooteco.subway.dto.response.StationResponse;

@DisplayName("지하철역 관련 기능")
@Sql("/stationInitSchema.sql")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStationTest() {
        // given
        final StationRequest stationRequest = new StationRequest("강남역");

        // when
        final ExtractableResponse<Response> response =
                requestHttpPost(stationRequest, "/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateNameTest() {
        // given
        final StationRequest stationRequest = new StationRequest("강남역");
        requestHttpPost(stationRequest, "/stations");

        // when
        final ExtractableResponse<Response> response =
                requestHttpPost(stationRequest, "/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStationsTest() {
        /// given
        final StationRequest stationRequest1 = new StationRequest("강남역");
        final ExtractableResponse<Response> createResponse1 =
                requestHttpPost(stationRequest1, "/stations");

        final StationRequest stationRequest2 = new StationRequest("역삼역");
        final ExtractableResponse<Response> createResponse2 =
                requestHttpPost(stationRequest2, "/stations");

        // when
        final ExtractableResponse<Response> response = requestHttpGet("/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        final List<Long> expectedStationIds = extractStationIdsWithLocation(createResponse1,
                createResponse2);
        final List<Long> resultStationIds = extractStationIdsWithJson(response);
        assertThat(resultStationIds).containsAll(expectedStationIds);
    }

    private List<Long> extractStationIdsWithJson(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", StationResponse.class)
                .stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());
    }

    private List<Long> extractStationIdsWithLocation(ExtractableResponse<Response> createResponse1,
                                                     ExtractableResponse<Response> createResponse2) {
        return Stream.of(createResponse1, createResponse2)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStationTest() {
        // given
        final StationRequest stationRequest = new StationRequest("강남역");
        final ExtractableResponse<Response> createResponse =
                requestHttpPost(stationRequest, "/stations");

        // when
        final String uri = createResponse.header("Location");
        final ExtractableResponse<Response> response = requestHttpDelete(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
