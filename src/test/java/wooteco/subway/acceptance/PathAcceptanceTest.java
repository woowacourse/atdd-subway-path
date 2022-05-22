package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.acceptance.SimpleRestAssured.get;
import static wooteco.subway.acceptance.SimpleRestAssured.makeLineJson;
import static wooteco.subway.acceptance.SimpleRestAssured.makeSectionJson;
import static wooteco.subway.acceptance.SimpleRestAssured.makeStationJson;
import static wooteco.subway.acceptance.SimpleRestAssured.post;
import static wooteco.subway.acceptance.SimpleRestAssured.postLine;
import static wooteco.subway.acceptance.SimpleRestAssured.postStation;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.dto.PathResponse;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("/paths에 대한 인수테스트")
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("GET /paths?source={source}&target={target}&age={age} - 경로 조회 테스트")
    @Nested
    class ShowPath extends AcceptanceTest {

        @Test
        void 성공시_200_OK() {
            postStation(makeStationJson("강남역"));
            postStation(makeStationJson("양재역"));
            postStation(makeStationJson("양재시민의숲역"));
            postLine(makeLineJson("1호선", "파란색", 1L, 3L, 8));
            postLine(makeLineJson("2호선", "초록색", 1L, 2L, 3));
            post(makeSectionJson(2L, 3L, 3), "/lines/2/sections");

            ExtractableResponse<Response> response = get(
                    "/paths?source=1&target=3&age=15");
            PathResponse actual = response.jsonPath().getObject(".", PathResponse.class);

            assertAll(() -> {
                assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
                assertThat(actual)
                        .extracting("distance", "fare")
                        .containsExactly(6, 1250);
            });
        }

        @Test
        void 존재하지_않는_역의_id를_입력할_경우_400_BAD_REQUEST() {
            ExtractableResponse<Response> response = get(
                    "/paths?source=9999&target=3&age=15");

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 연결되어_있지_않은_역의_최단거리를_조회할_경우_400_BAD_REQUEST() {
            postStation(makeStationJson("강남역"));
            postStation(makeStationJson("양재역"));
            postStation(makeStationJson("양재시민의숲역"));
            postStation(makeStationJson("선릉역"));
            postLine(makeLineJson("1호선", "파란색", 1L, 2L, 8));
            postLine(makeLineJson("2호선", "초록색", 3L, 4L, 3));

            ExtractableResponse<Response> response = get(
                    "/paths?source=1&target=4&age=15");

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }
    }
}
