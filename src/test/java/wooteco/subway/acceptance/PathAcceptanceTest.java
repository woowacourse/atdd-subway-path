package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static wooteco.subway.SubwayFixtures.LINES_URI;
import static wooteco.subway.SubwayFixtures.SECOND_LINE_SECTIONS_URI;
import static wooteco.subway.SubwayFixtures.STATIONS_URI;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.ui.dto.request.LineRequest;
import wooteco.subway.ui.dto.request.SectionRequest;
import wooteco.subway.ui.dto.request.StationRequest;
import wooteco.subway.ui.dto.response.PathResponse;

@DisplayName("경로 조회 E2E")
@Sql("classpath:/schema-test.sql")
class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로 조회 테스트")
    @TestFactory
    Stream<DynamicTest> sectionTest() {
        post(STATIONS_URI, toJson(new StationRequest("성수역")));
        post(STATIONS_URI, toJson(new StationRequest("삼성역")));
        post(STATIONS_URI, toJson(new StationRequest("선릉역")));
        post(STATIONS_URI, toJson(new StationRequest("역삼역")));
        post(STATIONS_URI, toJson(new StationRequest("강남역")));
        post(LINES_URI, toJson(new LineRequest("2호선", "bg-600-green", 2L, 4L, 20)));
        post(SECOND_LINE_SECTIONS_URI, toJson(new SectionRequest(1L, 2L, 10)));
        post(SECOND_LINE_SECTIONS_URI, toJson(new SectionRequest(4L, 5L, 10)));
        post(SECOND_LINE_SECTIONS_URI, toJson(new SectionRequest(2L, 3L, 5)));
        post(SECOND_LINE_SECTIONS_URI, toJson(new SectionRequest(4L, 5L, 5)));

        return Stream.of(
                dynamicTest("성수-삼성-선릉-역삼-강남 에서 삼성-역삼 경로를 조회하면 삼성-선릉-역삼 및 거리와 요금이 반환된다", () -> {
                    // given
                    final String pathURI = "/paths?source=2&target=4&age=15";

                    // when
                    final PathResponse response = get(pathURI).as(PathResponse.class);

                    // then
                    assertAll(
                            () -> assertThat(response.getStations())
                                    .extracting("name")
                                    .containsExactly("삼성역", "선릉역", "역삼역"),
                            () -> assertThat(response.getDistance()).isEqualTo(20),
                            () -> assertThat(response.getFare()).isEqualTo(1450)
                    );
                })
        );
    }
}
