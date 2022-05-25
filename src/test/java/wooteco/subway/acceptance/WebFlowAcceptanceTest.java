package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.service.dto.LineRequest;
import wooteco.subway.service.dto.LineResponse;
import wooteco.subway.service.dto.SectionRequest;
import wooteco.subway.service.dto.StationRequest;
import wooteco.subway.service.dto.StationResponse;

@DisplayName("지하철 관리 및 경로 조회")
public class WebFlowAcceptanceTest extends AcceptanceTest {

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

    }

    @Nested
    @DisplayName("지하철 역 관련 기능")
    class Describe_Station_Of {
        ExtractableResponse<Response> response_신설동;
        ExtractableResponse<Response> response_성수;

        @BeforeEach
        void beforeEach() {
            response_신설동 = postWithBody("/stations", new StationRequest("신설동역"));
            response_성수 = postWithBody("/stations", new StationRequest("성수역"));
        }

        @Nested
        @DisplayName("새로운 지하철 역을 등록한 경우에")
        class Context_When_Add_New_Station {
            StationRequest request_용답 = new StationRequest("용답역");

            @Test
            @DisplayName("상태 코드 201을 반환한다.")
            void It_Returns_200_Status_Code() {
                ExtractableResponse<Response> response_용답 = postWithBody("/stations", request_용답);

                assertThat(response_용답.statusCode()).isEqualTo(201);
            }

            @Test
            @DisplayName("지하철역 목록에 등록된 역이 추가된다.")
            void It_Returns_Added_Stations() {
                ExtractableResponse<Response> response_용답 = postWithBody("/stations", request_용답);

                ExtractableResponse<Response> 지하철_역_목록 = get("/stations");

                List<Long> expectedLineIds = Stream.of(response_신설동, response_성수, response_용답)
                        .map(response -> response.jsonPath().getLong("id"))
                        .collect(Collectors.toList());

                List<Long> resultLineIds = 지하철_역_목록.jsonPath().getList(".", StationResponse.class).stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList());

                assertThat(resultLineIds).containsAll(expectedLineIds);
            }
        }

        @Nested
        @DisplayName("지하철 역을 삭제한 경우에")
        class Context_When_Delete_Station {

            @Test
            @DisplayName("상태 코드 204를 반환한다.")
            void It_Returns_204_Status_Code() {
                String url = "/stations/" + response_성수.jsonPath().getLong("id");
                ExtractableResponse<Response> deleteResponse = delete(url);

                assertThat(deleteResponse.statusCode()).isEqualTo(204);
            }

            @Test
            @DisplayName("지하철역 목록에 선택한 역이 삭제된다.")
            void It_Returns_Deleted_Stations() {
                String url = "/stations/" + response_성수.jsonPath().getLong("id");
                delete(url);

                ExtractableResponse<Response> 지하철_역_목록 = get("/stations");
                List<Long> expectedLineIds = Stream.of(response_신설동)
                        .map(response -> response.jsonPath().getLong("id"))
                        .collect(Collectors.toList());

                List<Long> resultLineIds = 지하철_역_목록.jsonPath().getList(".", StationResponse.class).stream()
                        .map(StationResponse::getId)
                        .collect(Collectors.toList());

                assertThat(resultLineIds.size()).isEqualTo(1);
                assertThat(resultLineIds).containsAll(expectedLineIds);
            }
        }
    }

    @Nested
    @DisplayName("지하철 노선 관련 기능")
    class Describe_Line_Of {
        ExtractableResponse<Response> response_동묘앞;
        ExtractableResponse<Response> response_신설동;
        ExtractableResponse<Response> response_용두;
        ExtractableResponse<Response> response_성수;

        ExtractableResponse<Response> line2Response;

        @BeforeEach
        void beforeEach() {
            response_동묘앞 = postWithBody("/stations", new StationRequest("동묘앞역"));
            response_신설동 = postWithBody("/stations", new StationRequest("신설동역"));
            response_용두 = postWithBody("/stations", new StationRequest("용두역"));
            response_성수 = postWithBody("/stations", new StationRequest("성수역"));

            LineRequest line2Request = new LineRequest("2호선", "green", response_신설동.jsonPath().getLong("id"),
                    response_성수.jsonPath().getLong("id"), 10, 800);
            line2Response = postWithBody("/lines", line2Request);
        }

        @Nested
        @DisplayName("지하철 노선을 등록한 경우에")
        class Context_When_Create_Line {
            String lineName = "1호선";
            String lineColor = "blue";

            @Test
            @DisplayName("등록한 노선이 추가된 노선 목록을 확인할 수 있다.")
            void It_Returns_Added_Lines() {
                // when
                LineRequest line1Request = new LineRequest(lineName, lineColor, response_동묘앞.jsonPath().getLong("id"),
                        response_신설동.jsonPath().getLong("id"), 10, 800);
                ExtractableResponse<Response> line1Response = postWithBody("/lines", line1Request);

                ExtractableResponse<Response> responses = get("/lines");

                List<LineResponse> lineResponses = responses.jsonPath().getList(".", LineResponse.class);

                List<Long> expectedLineIds = Stream.of(line2Response, line1Response)
                        .map(response -> response.jsonPath().getLong("id"))
                        .collect(Collectors.toList());

                List<Long> resultLineIds = lineResponses.stream()
                        .map(LineResponse::getId)
                        .collect(Collectors.toList());

                assertThat(resultLineIds.size()).isEqualTo(2);
                assertThat(resultLineIds).containsAll(expectedLineIds);
            }
        }

        @Nested
        @DisplayName("지하철 노선을 수정한 경우에")
        class Context_When_Edit_Line {
            String updateLineName = "3호선";
            String updateLineColor = "ORANGE";

            @Test
            @DisplayName("수정된 노선이 추가된 노선 목록을 확인할 수 있다.")
            void It_Returns_Edited_Lines() {
                // when
                LineRequest updateLineRequest = new LineRequest(updateLineName, updateLineColor,
                        response_신설동.jsonPath().getLong("id"),
                        response_성수.jsonPath().getLong("id"), 10, 800);

                ExtractableResponse<Response> updateResponse = putWithBody(
                        "/lines/" + line2Response.jsonPath().getLong("id"), updateLineRequest);

                ExtractableResponse<Response> responses = get("/lines");
                List<LineResponse> lineResponses = responses.jsonPath().getList(".", LineResponse.class);

                List<String> resultLineNames = lineResponses.stream()
                        .map(LineResponse::getName)
                        .collect(Collectors.toList());

                assertThat(resultLineNames.size()).isEqualTo(1);
                assertThat(resultLineNames.contains("3호선")).isTrue();
            }
        }

        @Nested
        @DisplayName("지하철 노선을 수정한 경우에")
        class Context_When_Delete_Line {

            @Test
            @DisplayName("선택한 노선이 삭제된 노선 목록을 확인할 수 있다.")
            void It_Returns_Edited_Lines() {
                // when
                Long deleteLineId = line2Response.jsonPath().getLong("id");
                ExtractableResponse<Response> deleteResponse = delete("/lines/" + deleteLineId);

                ExtractableResponse<Response> responses = get("/lines");
                List<LineResponse> lineResponses = responses.jsonPath().getList(".", LineResponse.class);

                List<String> resultLineNames = lineResponses.stream()
                        .map(LineResponse::getName)
                        .collect(Collectors.toList());

                assertThat(resultLineNames.size()).isEqualTo(0);
            }
        }
    }

    @Nested
    @DisplayName("지하철 구간 관련 기능")
    class Describe_Section_Of {
        ExtractableResponse<Response> response_동묘앞;
        ExtractableResponse<Response> response_신설동;
        ExtractableResponse<Response> response_용두;
        ExtractableResponse<Response> response_성수;

        ExtractableResponse<Response> line2Response;

        @BeforeEach
        void beforeEach() {
            response_동묘앞 = postWithBody("/stations", new StationRequest("동묘앞역"));
            response_신설동 = postWithBody("/stations", new StationRequest("신설동역"));
            response_용두 = postWithBody("/stations", new StationRequest("용두역"));
            response_성수 = postWithBody("/stations", new StationRequest("성수역"));

            LineRequest line2Request = new LineRequest("2호선", "green", response_신설동.jsonPath().getLong("id"),
                    response_성수.jsonPath().getLong("id"), 10, 800);
            line2Response = postWithBody("/lines", line2Request);

        }

        @Nested
        @DisplayName("지하철 노선을 선택한 경우")
        class Context_When_Select_Line {

            @Test
            @DisplayName("선택한 노선의 상행 종점과 하행 종점 지하철 역 정보를 알 수 있다.")
            void It_Returns_Edited_Lines() {
                Long lineId = line2Response.jsonPath().getLong("id");
                // when
                ExtractableResponse<Response> response = get("/lines/" + lineId);

                // then
                LineResponse lineResponse = response.body().as(LineResponse.class);
                List<String> stationNames = lineResponse.getStations().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList());

                assertAll(() -> {
                    assertThat(lineResponse.getId()).isEqualTo(lineId);
                    assertThat(lineResponse.getName()).isEqualTo("2호선");
                    assertThat(lineResponse.getColor()).isEqualTo("green");
                    assertThat(stationNames).containsAll(List.of("신설동역", "성수역"));
                });
            }
        }

        @Nested
        @DisplayName("지하철 구간을 등록한 경우")
        class Context_When_Add_Section {

            @Test
            @DisplayName("기존의 구간에서 신규 구간을 추가한 구간 목록을 확인할 수 있다.")
            void It_Returns_Added_Sections() {
                Long lineId = line2Response.jsonPath().getLong("id");

                SectionRequest sectionRequest = new SectionRequest(response_신설동.jsonPath().getLong("id"),
                        response_용두.jsonPath().getLong("id"), 5);

                // when
                ExtractableResponse<Response> addSectionResponse =
                        postWithBody("/lines/" + lineId + "/sections", sectionRequest);

                ExtractableResponse<Response> findLineResponse = get("/lines/" + lineId);

                // then
                LineResponse lineResponse = findLineResponse.body().as(LineResponse.class);
                List<String> stationNames = lineResponse.getStations().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList());

                assertThat(stationNames).containsAll(List.of("신설동역", "용두역", "성수역"));
            }
        }

        @Nested
        @DisplayName("지하철 구간을 삭제한 경우")
        class Context_When_Delete_Section {
            Long lineId;

            @BeforeEach
            void before() {
                lineId = line2Response.jsonPath().getLong("id");

                SectionRequest sectionRequest = new SectionRequest(response_신설동.jsonPath().getLong("id"),
                        response_용두.jsonPath().getLong("id"), 5);
                // when
                ExtractableResponse<Response> addSectionResponse =
                        postWithBody("/lines/" + lineId + "/sections", sectionRequest);
            }

            @Test
            @DisplayName("선택한 구간이 삭제된 구간 목록을 확인할 수 있다.")
            void It_Returns_Deleted_Sections() {
                ExtractableResponse<Response> findLineResponse = get("/lines/" + lineId);
                delete("/lines/" + lineId + "/sections?stationId=" + response_성수.jsonPath().getLong("id"));
                // then
                LineResponse lineResponse = findLineResponse.body().as(LineResponse.class);
                List<String> stationNames = lineResponse.getStations().stream()
                        .map(StationResponse::getName)
                        .collect(Collectors.toList());

                assertThat(stationNames).containsAll(List.of("신설동역", "용두역"));
            }
        }
    }

    @Nested
    @DisplayName("지하철 경로 검색 기능")
    class Describe_Path_Of {
        ExtractableResponse<Response> response_동묘앞;
        ExtractableResponse<Response> response_신설동;
        ExtractableResponse<Response> response_용두;
        ExtractableResponse<Response> response_성수;

        ExtractableResponse<Response> line2Response;

        @BeforeEach
        void beforeEach() {
            response_동묘앞 = postWithBody("/stations", new StationRequest("동묘앞역"));
            response_신설동 = postWithBody("/stations", new StationRequest("신설동역"));
            response_용두 = postWithBody("/stations", new StationRequest("용두역"));
            response_성수 = postWithBody("/stations", new StationRequest("성수역"));

            LineRequest line2Request = new LineRequest("2호선", "green", response_신설동.jsonPath().getLong("id"),
                    response_성수.jsonPath().getLong("id"), 10, 800);
            line2Response = postWithBody("/lines", line2Request);
            SectionRequest sectionRequest = new SectionRequest(response_신설동.jsonPath().getLong("id"),
                    response_용두.jsonPath().getLong("id"), 5);
            ExtractableResponse<Response> addSectionResponse =
                    postWithBody("/lines/" + line2Response.jsonPath().getLong("id") + "/sections", sectionRequest);
        }

        @Nested
        @DisplayName("출발지와 목적지 지하철역과 나이를 입력하면")
        class Context_When_Find_Path {
            int age = 21;

            @Test
            @DisplayName("계산된 이용 요금과 경로의 지하철 역 목록을 반환한다.")
            void It_Returns_Fare_And_PathOfStations() {
                String url =
                        "/paths?source=" + response_신설동.jsonPath().getLong("id") + "&target=" + response_성수.jsonPath()
                                .getLong("id") + "&age=" + age;

                ExtractableResponse<Response> response = get(url);
                // when
                PathResponse pathResponse = response.body().as(PathResponse.class);
                // then
                assertAll(() -> {
                    assertThat(pathResponse.getStations().size()).isEqualTo(3);
                    assertThat(pathResponse.getDistance()).isEqualTo(10);
                    assertThat(pathResponse.getFare()).isEqualTo(2050);
                });
            }
        }
    }
}
