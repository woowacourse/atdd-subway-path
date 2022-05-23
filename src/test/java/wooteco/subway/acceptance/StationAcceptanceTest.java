package wooteco.subway.acceptance;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철역을 생성한다.")
    @Test
    void createStation() {
        // given
        final StationRequest params = new StationRequest("강남역");

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.post(params, "/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        assertThat(response.as(StationResponse.class).getName()).isEqualTo("강남역");
    }

    @DisplayName("기존에 존재하는 지하철역 이름으로 지하철역을 생성한다.")
    @Test
    void createStationWithDuplicateName() {
        // given
        final StationRequest params = new StationRequest("강남역");
        AcceptanceFixture.post(params, "/stations");

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.post(params, "/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철역을 조회한다.")
    @Test
    void getStations() {
        /// given
        final StationRequest params1 = new StationRequest("강남역");
        ExtractableResponse<Response> createResponse1 = AcceptanceFixture.post(params1, "/stations");

        final StationRequest params2 = new StationRequest("역삼역");
        ExtractableResponse<Response> createResponse2 = AcceptanceFixture.post(params2, "/stations");

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.get("/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Long> expectedLineIds = List.of(extractId(createResponse1), extractId(createResponse2));
        List<Long> resultLineIds = extractIds(response);
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @DisplayName("지하철역을 제거한다.")
    @Test
    void deleteStation() {
        // given
        final StationRequest params = new StationRequest("강남역");

        ExtractableResponse<Response> createResponse = AcceptanceFixture.post(params, "/stations");

        // when
        String uri = createResponse.header("Location");
        ExtractableResponse<Response> response = AcceptanceFixture.delete(uri);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("역 등록 시 거리 입력 값을 검증한다.")
    @ParameterizedTest(name = "name = {0}")
    @MethodSource("validateName")
    public void validateArgument(String name) {
        // given
        final StationRequest params = new StationRequest(name);

        // when
        ExtractableResponse<Response> response = AcceptanceFixture.post(params, "/stations");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static Stream<Arguments> validateName() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of(" "),
                Arguments.of(
                        "123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789123456789")
        );
    }

    private Long extractId(ExtractableResponse<Response> response) {
        return response.jsonPath()
                .getObject(".", StationResponse.class)
                .getId();
    }

    private List<Long> extractIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(toList());
    }
}
