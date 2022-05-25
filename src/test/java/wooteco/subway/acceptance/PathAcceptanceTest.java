package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.utils.HttpMethod;
import wooteco.utils.HttpUtils;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("인수테스트 - /paths")
public class PathAcceptanceTest extends AcceptanceTest {

    private final Station 강남역 = new Station(1L, "강남역");
    private final Station 선릉역 = new Station(2L, "선릉역");
    private final Station 잠실역 = new Station(3L, "잠실역");
    private final Station 청계산입구역 = new Station(4L, "청계산입구역");

    @DisplayName("GET /paths?source={source}&target={target}&age={age} - 경로 조회 테스트")
    @Nested
    class SearchTest {

        @Test
        void 경로_조회_성공시_200_OK() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            databaseFixtureUtils.saveLine("노선", "색상");
            databaseFixtureUtils.saveSection(1L, 강남역, 선릉역, 10);
            databaseFixtureUtils.saveSection(1L, 선릉역, 잠실역, 5);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.GET,
                    toPath(1L, 3L, 30));
            PathResponse actualBody = response.jsonPath().getObject(".", PathResponse.class);
            PathResponse expectedBody = new PathResponse(
                    List.of(new StationResponse(1L, "강남역"), new StationResponse(2L, "선릉역"),
                            new StationResponse(3L, "잠실역")), 15, 1350);

            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(actualBody).isEqualTo(expectedBody);
        }

        @Test
        void 존재하지_않는_지하철역이_입력된_경우_404_NOT_FOUND() {
            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.GET,
                    toPath(1L, 3L, 30));

            assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
        }

        @Test
        void 연결되지_않은_지하철역들_사이의_경로를_조회하려는_경우_400_BAD_REQUEST() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역, 청계산입구역);
            databaseFixtureUtils.saveLine("노선", "색상");
            databaseFixtureUtils.saveSection(1L, 강남역, 선릉역, 10);
            databaseFixtureUtils.saveSection(1L, 잠실역, 청계산입구역, 10);

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.GET,
                    toPath(1L, 3L, 30));

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        @Test
        void 구간에_등록되지_않은_지하철역이_입력된_경우_400_BAD_REQUEST() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            databaseFixtureUtils.saveLine("등록된 노선", "색상");

            ExtractableResponse<Response> response = HttpUtils.send(HttpMethod.GET,
                    toPath(1L, 2L, 30));

            assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        }

        private String toPath(Long source, Long target, int age) {
            return String.format("/paths?source=%d&target=%d&age=%d", source, target, age);
        }
    }
}
