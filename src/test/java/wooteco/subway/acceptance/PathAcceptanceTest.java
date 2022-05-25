package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.SubwayFixtures.LINES_URI;
import static wooteco.subway.SubwayFixtures.SECOND_LINE_SECTIONS_URI;
import static wooteco.subway.SubwayFixtures.STATIONS_URI;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.ui.dto.request.LineRequest;
import wooteco.subway.ui.dto.request.SectionRequest;
import wooteco.subway.ui.dto.request.StationRequest;
import wooteco.subway.ui.dto.response.PathResponse;

@DisplayName("경로 조회 E2E")
@Sql("/truncate.sql")
class PathAcceptanceTest extends AcceptanceTest {

    @ParameterizedTest(name = "나이 : {0}, 추가요금 : 20000, 기대요금 : {1}")
    @CsvSource(value = {"1, 0", "9, 10550", "10, 10550",
            "11, 10550", "15, 16880", "16, 16880", "50, 21450",
            "51, 21450", "58, 21450", "59, 21450", "66, 0", "67, 0"})
    @DisplayName("연령별 요금 조회 테스트")
    void fareInPaths(long age, long expected) {
        post(STATIONS_URI, toJson(new StationRequest("성수역")));
        post(STATIONS_URI, toJson(new StationRequest("삼성역")));
        post(STATIONS_URI, toJson(new StationRequest("선릉역")));
        post(STATIONS_URI, toJson(new StationRequest("역삼역")));
        post(STATIONS_URI, toJson(new StationRequest("강남역")));
        post(LINES_URI, toJson(new LineRequest("2호선", "bg-600-green", 2L, 4L, 20L, 20000L)));
        post(SECOND_LINE_SECTIONS_URI, toJson(new SectionRequest(1L, 2L, 10L)));
        post(SECOND_LINE_SECTIONS_URI, toJson(new SectionRequest(4L, 5L, 10L)));
        post(SECOND_LINE_SECTIONS_URI, toJson(new SectionRequest(2L, 3L, 5L)));

        // given
        final String pathURI = String.format("/paths?source=2&target=4&age=%d", age);

        // when
        final PathResponse response = get(pathURI).as(PathResponse.class);

        // then
        assertAll(
                () -> assertThat(response.getStations())
                        .extracting("name")
                        .containsExactly("삼성역", "선릉역", "역삼역"),
                () -> assertThat(response.getDistance()).isEqualTo(20),
                () -> assertThat(response.getFare()).isEqualTo(expected)
        );
    }
}
