package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.service.dto.response.StationResponse;

@SuppressWarnings({"InnerClassMayBeStatic", "NonAsciiCharacters"})
@DisplayName("경로 관련 기능")
public class PathAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("경로 조회 API는")
    class Describe_Path_Get_API {

        @Nested
        @DisplayName("출발역과 도착역을 입력받는 경우")
        class Context_Input_Get_Path {

            long 출발역;
            long 도착역;

            @Test
            @DisplayName("200응답을 한다.")
            void it_returns_path() {
                setUp();
                ExtractableResponse<Response> response = 경로_조회(출발역, 도착역);

                assertThat(response.statusCode()).isEqualTo(200);
            }

            @Test
            @DisplayName("최단 경로, 거리, 요금을 응답한다.")
            void it_returns_path2() {
                setUp();
                ExtractableResponse<Response> response = 경로_조회(출발역, 도착역);

                List<StationResponse> stations = response.body().jsonPath()
                        .getList("stations", StationResponse.class);
                int distance = response.body().jsonPath().getInt("distance");
                int fare = response.body().jsonPath().getInt("fare");

                assertThat(stations)
                        .extracting("name")
                        .containsExactly("강남", "성수", "합정");
                assertThat(distance).isEqualTo(20);
                assertThat(fare).isEqualTo(1950);
            }

            private void setUp() {
                long 강남 = 역_저장("강남");
                long 삼성 = 역_저장("삼성");
                long 건대 = 역_저장("건대");
                long 성수 = 역_저장("성수");
                long 왕십리 = 역_저장("왕십리");
                long 합정 = 역_저장("합정");

                출발역 = 강남;
                도착역 = 합정;

                long 이호선 = 노선_저장(노선_저장_파라미터("2호선", "green", 강남, 성수, 30, 0));
                long 분당선 = 노선_저장(노선_저장_파라미터("분당선", "yellow", 왕십리, 강남, 30, 500));

                구간_등록(이호선, 구간_등록_파라미터(강남, 삼성, 10));
                구간_등록(이호선, 구간_등록_파라미터(삼성, 건대, 10));

                구간_등록(분당선, 구간_등록_파라미터(왕십리, 합정, 10));
                구간_등록(분당선, 구간_등록_파라미터(합정, 성수, 10));
            }
        }
    }

    private ExtractableResponse<Response> 경로_조회(long 출발역, long 도착역) {
        return RestAssured.given().log().all()
                .queryParams(경로조회_파라미터(출발역, 도착역))
                .when()
                .get("/paths")
                .then().log().all()
                .extract();
    }

    private Map<String, Object> 경로조회_파라미터(long 출발역, long 도착역) {
        Map<String, Object> params = new HashMap<>();
        params.put("source", 출발역);
        params.put("target", 도착역);
        params.put("age", 15);
        return params;
    }

}
