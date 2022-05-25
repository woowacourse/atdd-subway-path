package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.LineResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.utils.HttpMethod;
import wooteco.utils.HttpUtils;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("인수테스트 - /lines")
public class LineAcceptanceTest extends AcceptanceTest {

    private static final Station 강남역 = new Station(1L, "강남역");
    private static final Station 선릉역 = new Station(2L, "선릉역");
    private static final Station 잠실역 = new Station(3L, "잠실역");

    @DisplayName("POST /lines - 지하철 노선 생성 테스트")
    @Nested
    class CreateLineTest {

        private static final String PATH = "/lines";

        @Test
        void 성공시_201_CREATED() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            Map<String, Object> params = jsonLineOf("신분당선", "bg-red-600",
                    900, 1L, 2L, 10);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.POST, PATH, params);
            LineResponse actualBody = extractSingleLineResponseBody(response);
            LineResponse expectedBody = new LineResponse(1L, "신분당선", "bg-red-600", 900,
                    List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "선릉역")));

            assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
            assertThat(response.header("Location")).isNotBlank();
            assertThat(actualBody).isEqualTo(expectedBody);
        }

        @Test
        void 정보가_담기지_않은_경우_400_BAD_REQUEST() {
            Map<String, String> emptyParams = new HashMap<>();

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.POST, PATH, emptyParams);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 정보가_공백으로_구성된_경우_400_BAD_REQUEST() {
            Map<String, Object> blankParams = jsonLineOf("   ", "  ", 100, 1L, 2L, 10);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.POST, PATH, blankParams);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 거리_정보가_0이하인_경우_400_BAD_REQUEST() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            Map<String, Object> params = jsonLineOf("신분당선", "bg-red-600",
                    900, 1L, 2L, 0);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.POST, PATH, params);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 이미_존재하는_노선명_입력시_400_BAD_REQUEST() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            Map<String, Object> duplicateNameParams = jsonLineOf("중복되는 노선명", "bg-red-600", 100, 1L, 2L, 10);

            HttpUtils.send(HttpMethod.POST, "/lines", duplicateNameParams);
            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.POST, PATH, duplicateNameParams);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 존재하지_않는_지하철역을_종점으로_입력하면_404_NOT_FOUND() {
            Map<String, Object> params = jsonLineOf("노선명", "색상", 100, 9999L, 2L, 10);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.POST, PATH, params);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        private HashMap<String, Object> jsonLineOf(String name,
                                                   String color,
                                                   int extraFare,
                                                   Long upStationId,
                                                   Long downStationId,
                                                   int distance) {
            return new HashMap<>() {{
                put("name", name);
                put("color", color);
                put("extraFare", extraFare);
                put("upStationId", upStationId);
                put("downStationId", downStationId);
                put("distance", distance);
            }};
        }
    }

    @DisplayName("GET /lines - 지하철 노선 목록 조회 테스트")
    @Nested
    class ShowLinesTest {

        private static final String PATH = "/lines";

        private final StationResponse 강남역_응답 = new StationResponse(1L, "강남역");
        private final StationResponse 선릉역_응답 = new StationResponse(2L, "선릉역");
        private final StationResponse 잠실역_응답 = new StationResponse(3L, "잠실역");

        @Test
        void 성공시_200_OK() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            databaseFixtureUtils.saveLine("1호선", "노란색", 1000);
            databaseFixtureUtils.saveLine("2호선", "빨간색", 0);
            databaseFixtureUtils.saveSection(1L, 선릉역, 잠실역);
            databaseFixtureUtils.saveSection(1L, 강남역, 선릉역);
            databaseFixtureUtils.saveSection(2L, 강남역, 잠실역);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.GET, PATH);
            List<LineResponse> actualBody = extractJsonBody(response);
            List<LineResponse> expectedBody = List.of(
                    new LineResponse(1L, "1호선", "노란색", 1000, List.of(강남역_응답, 선릉역_응답,
                            잠실역_응답)),
                    new LineResponse(2L, "2호선", "빨간색", 0, List.of(강남역_응답, 잠실역_응답)));

            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(actualBody).isEqualTo(expectedBody);
        }

        private List<LineResponse> extractJsonBody(ExtractableResponse<Response> response) {
            return response.jsonPath().getList(".", LineResponse.class);
        }
    }

    @DisplayName("GET /lines/:id - 지하철 노선 조회 테스트")
    @Nested
    class ShowLineTest {

        @Test
        void 모든_구간은_상행종점부터_하행종점까지_순서대로_나열되며_성공시_200_OK() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            databaseFixtureUtils.saveLine("1호선", "노란색", 1000);
            databaseFixtureUtils.saveSection(1L, 강남역, 선릉역);
            databaseFixtureUtils.saveSection(1L, 잠실역, 강남역);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.GET, toPath(1L));
            LineResponse actualBody = extractSingleLineResponseBody(response);
            LineResponse expectedBody = new LineResponse(1L, "1호선", "노란색", 1000,
                    List.of(new StationResponse(3L, "잠실역"),
                            new StationResponse(1L, "강남역"),
                            new StationResponse(2L, "선릉역")));

            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(actualBody).isEqualTo(expectedBody);
        }

        @Test
        void 존재하지_않는_노선인_경우_404_NOT_FOUND() {
            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.GET, toPath(99999L));

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        private String toPath(Long id) {
            return String.format("/lines/%d", id);
        }
    }

    @DisplayName("PUT /lines/:id - 지하철 노선 수정 테스트")
    @Nested
    class UpdateLineTest {

        @Test
        void 성공시_200_OK() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            databaseFixtureUtils.saveLine("신분당선", "노란색", 0);
            databaseFixtureUtils.saveSection(1L, 강남역, 선릉역, 10);
            Map<String, Object> params = jsonLineOf("NEW 분당선", "bg-red-800", 900);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.PUT, toPath(1L), params);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        }

        @Test
        void 수정하려는_지하철_노선이_존재하지_않는_경우_404_NOT_FOUND() {
            Map<String, Object> params = jsonLineOf("NEW 분당선", "bg-red-600", 100);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.PUT, toPath(9999L), params);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        @Test
        void 이미_존재하는_지하철_노선_이름으로_수정시_400_BAD_REQUEST() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            databaseFixtureUtils.saveLine("현재 노선명", "노란색", 1000);
            databaseFixtureUtils.saveLine("존재하는 노선명", "노란색", 1000);
            databaseFixtureUtils.saveSection(1L, 강남역, 선릉역);
            databaseFixtureUtils.saveSection(2L, 강남역, 선릉역);
            Map<String, Object> duplicateNameParams = jsonLineOf("존재하는 노선명", "bg-red-600", 100);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.PUT, toPath(2L), duplicateNameParams);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 이름_혹은_색상_정보가_담기지_않은_경우_400_BAD_REQUEST() {
            Map<String, Object> emptyParams = new HashMap<>();

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.PUT, toPath(1L), emptyParams);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 이름_혹은_색상_정보가_공백으로_구성된_경우_400_BAD_REQUEST() {
            Map<String, Object> blankParams = jsonLineOf("   ", "  ", 1000);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.PUT, toPath(1L), blankParams);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 추가비용을_0미만으로_수정하려는_경우_400_BAD_REQUEST() {
            Map<String, Object> negativeExtraFareParams = jsonLineOf("분당선", "bg-red-800", -1);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.PUT, toPath(1L), negativeExtraFareParams);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private String toPath(Long id) {
            return String.format("/lines/%d", id);
        }

        private HashMap<String, Object> jsonLineOf(String name,
                                                   String color,
                                                   int extraFare) {
            return new HashMap<>() {{
                put("name", name);
                put("color", color);
                put("extraFare", extraFare);
            }};
        }
    }

    @DisplayName("DELETE /lines/:id - 지하철 노선 제거 테스트")
    @Nested
    class DeleteLineTest {

        @Test
        void 성공시_204_OK() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            databaseFixtureUtils.saveLine("존재하는 노선", "노란색");
            databaseFixtureUtils.saveSection(1L, 강남역, 선릉역);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.DELETE, toPath(1L));

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        }

        @Test
        void 삭제하려는_지하철_노선이_존재하지_않는_경우_404_NOT_FOUND() {
            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.DELETE, toPath(99999L));

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        private String toPath(Long id) {
            return String.format("/lines/%d", id);
        }
    }

    private LineResponse extractSingleLineResponseBody(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject(".", LineResponse.class);
    }
}
