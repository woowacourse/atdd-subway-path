package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import wooteco.subway.service.dto.response.LineResponse;

@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("노선 관련 기능")
public class LineAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("노선 생성 API는")
    class Describe_Line_CREATE_API {

        @Nested
        @DisplayName("역 2개와 노선을 입력받는 경우")
        class Context_Input_Save_Line {

            private Map<Object, Object> 노선_저장_파라미터;

            @BeforeEach
            void setUp() {
                long 잠실 = 역_저장("잠실");
                long 강남 = 역_저장("강남");
                노선_저장_파라미터 = 노선_요청_파라미터("신분당선", "bg-red-600", 잠실, 강남);
            }

            @Test
            @DisplayName("201 응답을 한다.")
            void it_returns_200() {
                ExtractableResponse<Response> response = 노선_저장(노선_저장_파라미터);

                assertThat(response.statusCode()).isEqualTo(201);
                assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            }

            @Test
            @DisplayName("역 2개와 노선 정보로 노선을 저장한다.")
            void it_returns_save() {
                ExtractableResponse<Response> response = 노선_저장(노선_저장_파라미터);

                assertThat(response.body().jsonPath().getString("name")).isEqualTo("신분당선");
                assertThat(response.body().jsonPath().getString("color")).isEqualTo("bg-red-600");
            }
        }

        @Nested
        @DisplayName("요청 파라미터로 빈 값을 사용하는 경우")
        class Context_Empty_Parameter {

            private Map<Object, Object> 빈_파라미터;

            @BeforeEach
            void setUp() {
                빈_파라미터 = new HashMap<>();
                빈_파라미터.put("name", "");
                빈_파라미터.put("color", "");
            }

            @Test
            @DisplayName("400응답을 한다.")
            void it_returns_400() {
                ExtractableResponse<Response> response = 노선_저장(빈_파라미터);

                assertThat(response.statusCode()).isEqualTo(400);
            }

            @Test
            @DisplayName("에러 메시지를 응답한다.")
            void it_returns_message() {
                ExtractableResponse<Response> response = 노선_저장(빈_파라미터);

                assertThat(response.body().jsonPath().getString("message")).isEqualTo("이름과 색깔은 공백일 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("노선 조회 API는")
    class Describe_Line_GET_API {

        @Nested
        @DisplayName("저장된 id로 노선을 조회하는 경우")
        class Context_Id_PathVariable {

            private long lineId;

            @BeforeEach
            void setUp() {
                long 잠실 = 역_저장("잠실");
                long 강남 = 역_저장("강남");
                lineId = saveLineAndGetId("2호선", "green", 잠실, 강남);
            }

            @Test
            @DisplayName("200 응답을 한다.")
            void it_returns_200() {
                ExtractableResponse<Response> response = 노선_조회(lineId);

                assertThat(response.statusCode()).isEqualTo(200);
                assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            }

            @Test
            @DisplayName("노선의 정보를 응답한다.")
            void it_returns_line() {
                ExtractableResponse<Response> response = 노선_조회(lineId);

                assertThat(response.body().jsonPath().getString("name")).isEqualTo("2호선");
                assertThat(response.body().jsonPath().getString("color")).isEqualTo("green");
            }
        }

        @Nested
        @DisplayName("존재하지 않는 id로 노선을 조회하는 경우")
        class Context_Id_Not_Found {

            @Test
            @DisplayName("404 응답을 한다.")
            void it_returns_404() {
                ExtractableResponse<Response> response = 노선_조회(1);

                assertThat(response.statusCode()).isEqualTo(404);
            }

            @Test
            @DisplayName("에러메시지를 응답한다.")
            void it_returns_message() {
                ExtractableResponse<Response> response = 노선_조회(1);

                assertThat(response.body().jsonPath().getString("message")).isEqualTo("조회하려는 id가 존재하지 않습니다. id : 1");
            }
        }
    }

    @Nested
    @DisplayName("노선 목록 조회 API는")
    class Describe_Lines_GET_API {

        @Nested
        @DisplayName("노선 목록 조회를 요청하면")
        class Context_Get_Lines {

            @BeforeEach
            void setUp() {
                saveLineAndGetId("1호선", "blue", 역_저장("창동"), 역_저장("강남"));
                saveLineAndGetId("2호선", "green", 역_저장("도봉"), 역_저장("의정부"));
            }

            @Test
            @DisplayName("200 응답을 한다.")
            void it_returns_200() {
                ExtractableResponse<Response> response = 노선_목록_조회();

                assertThat(response.statusCode()).isEqualTo(200);
                assertThat(response.contentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE);
            }

            @Test
            @DisplayName("노선 목록을 응답한다.")
            void it_returns_lines() {
                ExtractableResponse<Response> response = 노선_목록_조회();

                List<LineResponse> responses = response.body().jsonPath().getList(".", LineResponse.class);
                assertThat(responses).extracting("name").isEqualTo(List.of("1호선", "2호선"));
            }
        }
    }

    @Nested
    @DisplayName("노선 수정 API는")
    class Describe_Line_Update_API {

        private long lineId;
        private Map<Object, Object> 노선_수정_파라미터;

        @BeforeEach
        void setUp() {
            long 잠실 = 역_저장("잠실");
            long 강남 = 역_저장("강남");
            lineId = saveLineAndGetId("2호선", "green", 잠실, 강남);
            노선_수정_파라미터 = lineUpdateParam("신분당선", "bg-red-600");
        }

        @Nested
        @DisplayName("노선 id와 수정을 입력받는 경우")
        class Context_Update_Lines {

            @Test
            @DisplayName("200 응답을 한다.")
            void it_returns_200() {
                ExtractableResponse<Response> response = 노선_수정(lineId, 노선_수정_파라미터);

                assertThat(response.statusCode()).isEqualTo(200);
            }
        }

        @Nested
        @DisplayName("수정할 노선이 존재하지 않는 id인 경우")
        class Context_Update_Lines_Not_Found {

            @Test
            @DisplayName("404 응답을 한다.")
            void it_returns_404() {
                ExtractableResponse<Response> response = 노선_수정(lineId + 1L, 노선_수정_파라미터);

                assertThat(response.statusCode()).isEqualTo(404);
            }

            @Test
            @DisplayName("에러메시지를 응답한다.")
            void it_returns_message() {
                ExtractableResponse<Response> response = 노선_수정(lineId + 1L, 노선_수정_파라미터);

                assertThat(response.body().jsonPath().getString("message")).contains("조회하려는 id가 존재하지 않습니다.");
            }
        }

        @Nested
        @DisplayName("수정할 노선이 빈 값인 경우")
        class Context_Update_Lines_Empty_Parameter {

            @BeforeEach
            void setUp() {
                노선_수정_파라미터 = new HashMap<>();
                노선_수정_파라미터.put("name", "");
                노선_수정_파라미터.put("color", "");
            }

            @Test
            @DisplayName("400 응답을 한다.")
            void it_returns_400() {
                ExtractableResponse<Response> response = 노선_수정(lineId, 노선_수정_파라미터);

                assertThat(response.statusCode()).isEqualTo(400);
            }

            @Test
            @DisplayName("에러메시지를 응답한다.")
            void it_returns_message() {
                ExtractableResponse<Response> response = 노선_수정(lineId, 노선_수정_파라미터);

                assertThat(response.body().jsonPath().getString("message")).isEqualTo("이름과 색깔은 공백일 수 없습니다.");
            }
        }

        @Nested
        @DisplayName("수정할 노선이 중복인 경우")
        class Context_Update_Lines_Duplicate {

            private final Map<Object, Object> 노선_중복_파라미터 = lineUpdateParam("2호선", "green");

            @Test
            @DisplayName("400 응답을 한다.")
            void it_returns_400() {
                ExtractableResponse<Response> response = 노선_수정(lineId, 노선_중복_파라미터);

                assertThat(response.statusCode()).isEqualTo(400);
            }

            @Test
            @DisplayName("에러 메시지를 응답한다.")
            void it_returns_message() {
                ExtractableResponse<Response> response = 노선_수정(lineId, 노선_중복_파라미터);

                assertThat(response.body().jsonPath().getString("message")).isEqualTo("노선이 이름과 색상은 중복될 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("노선 삭제 API는")
    class Describe_Line_Delete_API {

        @Nested
        @DisplayName("존재하는 노선 id를 입력받는 경우 구간이 삭제되고")
        class Context_Delete_Id {

            private long lineId;

            @BeforeEach
            void setUp() {
                long 잠실 = 역_저장("잠실");
                long 강남 = 역_저장("강남");
                lineId = saveLineAndGetId("2호선", "green", 잠실, 강남);
            }

            @Test
            @DisplayName("204 응답을 한다.")
            void it_returns_200() {
                ExtractableResponse<Response> response = 노선_삭제(lineId);

                assertThat(response.statusCode()).isEqualTo(204);
            }
        }
    }

    @Test
    @DisplayName("노선을 id로 삭제한다.")
    void deleteById() {
        // given
        long id = saveLineAndGetId("1호선", "blue", 역_저장("창동"), 역_저장("도봉"));

        // when
        ExtractableResponse<Response> response = 노선_삭제(id);

        // then
        assertThat(response.statusCode()).isEqualTo(204);
    }

    @Test
    @DisplayName("구간을 등록한다.")
    void saveSection() {
        // given
        long upStationId = 역_저장("의정부");
        long lineId = saveLineAndGetId("1호선", "blue", upStationId, 역_저장("인천"));
        long downStationId = 역_저장("광운대");

        Map<String, Object> params = 구간_등록_파라미터(upStationId, downStationId, 5);

        // when
        ExtractableResponse<Response> response = 구간_등록(lineId, params);

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    @Test
    @DisplayName("구간을 삭제한다.")
    void deleteSection() {
        // given
        long upStationId = 역_저장("의정부");
        long downStationId = 역_저장("광운대");
        long addStationId = 역_저장("인천");
        long lineId = saveLineAndGetId("1호선", "blue", upStationId, downStationId);

        Map<String, Object> params = 구간_등록_파라미터(upStationId, addStationId, 3);

        RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/{lineId}/sections", lineId)
            .then().log().all()
            .extract();

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .queryParam("stationId", addStationId)
            .delete("/lines/{id}/sections", lineId)
            .then().log().all()
            .extract();

        // then
        assertThat(response.statusCode()).isEqualTo(200);
    }

    private ExtractableResponse<Response> 노선_저장(Map<Object, Object> 노선_요청_파라미터) {
        return RestAssured.given().log().all()
            .body(노선_요청_파라미터)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 노선_조회(long id) {
        return RestAssured.given().log().all()
            .when()
            .get("/lines/{id}", id)
            .then()
            .log().all().extract();
    }

    private ExtractableResponse<Response> 노선_목록_조회() {
        return RestAssured.given().log().all()
            .when()
            .get("/lines")
            .then().log().all()
            .extract();
    }

    private ExtractableResponse<Response> 노선_수정(Long lineId, Map<Object, Object> 노선_수정_파라미터) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(노선_수정_파라미터)
            .when()
            .put("/lines/{id}", lineId)
            .then()
            .log().all().extract();
    }

    private ExtractableResponse<Response> 노선_삭제(long id) {
        return RestAssured.given().log().all()
            .when()
            .delete("/lines/{id}", id)
            .then()
            .log().all().extract();
    }

    private long saveLineAndGetId(String name, String color, Long upStation, Long downStation) {
        Map<Object, Object> params = 노선_요청_파라미터(name, color, upStation, downStation);
        ExtractableResponse<Response> savedResponse = 노선_저장(params);
        return savedResponse.body().jsonPath().getLong("id");
    }

    private Map<Object, Object> 노선_요청_파라미터(String name, String color, Long upStationId, Long downStationId) {
        Map<Object, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", 10);
        return params;
    }

    private Map<Object, Object> lineUpdateParam(String name, String color) {
        Map<Object, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        return params;
    }

    private long 역_저장(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        ExtractableResponse<Response> savedResponse = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/stations")
            .then().log().all()
            .extract();
        return savedResponse.body().jsonPath().getLong("id");
    }

    private Map<String, Object> 구간_등록_파라미터(long upStationId, long downStationId, int distance) {
        Map<String, Object> params = new HashMap<>();
        params.put("upStationId", upStationId);
        params.put("downStationId", downStationId);
        params.put("distance", distance);
        return params;
    }

    private ExtractableResponse<Response> 구간_등록(long lineId, Map<String, Object> params) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
            .body(params)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/lines/{id}/sections", lineId)
            .then().log().all()
            .extract();
        return response;
    }
}
