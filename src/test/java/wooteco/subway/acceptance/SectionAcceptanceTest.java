package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;
import static wooteco.subway.SubwayFixtures.LINES_URI;
import static wooteco.subway.SubwayFixtures.SECOND_LINE_SECTIONS_URI;
import static wooteco.subway.SubwayFixtures.STATIONS_URI;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.ui.dto.request.LineRequest;
import wooteco.subway.ui.dto.request.SectionRequest;
import wooteco.subway.ui.dto.request.StationRequest;
import wooteco.subway.ui.dto.response.ExceptionResponse;
import wooteco.subway.ui.dto.response.LineResponse;
import wooteco.subway.ui.dto.response.StationResponse;

@DisplayName("구간 E2E")
@Sql("classpath:/schema-test.sql")
class SectionAcceptanceTest extends AcceptanceTest {

    @DisplayName("구간 추가, 삭제 테스트")
    @TestFactory
    Stream<DynamicTest> sectionTest() {
        post(STATIONS_URI, toJson(new StationRequest("성수역")));
        post(STATIONS_URI, toJson(new StationRequest("삼성역")));
        post(STATIONS_URI, toJson(new StationRequest("선릉역")));
        post(STATIONS_URI, toJson(new StationRequest("역삼역")));
        post(STATIONS_URI, toJson(new StationRequest("강남역")));
        post(LINES_URI, toJson(new LineRequest("2호선", "bg-600-green", 2L, 4L, 20)));

        return Stream.of(
                dynamicTest("삼성-역삼 에 성수-삼성을 추가하면 성수-삼성-역삼 이 된다", () -> {
                    // given
                    final String body = toJson(new SectionRequest(1L, 2L, 10));

                    // when
                    post(SECOND_LINE_SECTIONS_URI, body);
                    final LineResponse response = get(LINES_URI + "/1").as(LineResponse.class);
                    final List<StationResponse> actual = response.getStations();

                    // then
                    assertThat(actual).extracting("name").containsExactly("성수역", "삼성역", "역삼역");
                }),

                dynamicTest("성수-삼성-역삼 에 역삼-강남 을 추가하면 성수-삼성-역삼-강남 이 된다", () -> {
                    // given
                    final String body = toJson(new SectionRequest(4L, 5L, 10));

                    // when
                    post(SECOND_LINE_SECTIONS_URI, body);
                    final LineResponse response = get(LINES_URI + "/1").as(LineResponse.class);
                    final List<StationResponse> actual = response.getStations();

                    // then
                    assertThat(actual).extracting("name").containsExactly("성수역", "삼성역", "역삼역", "강남역");
                }),

                dynamicTest("성수-삼성-역삼-강남 에 삼성-선릉 을 추가하면 성수-삼성-선릉-역삼-강남 이 된다", () -> {
                    // given
                    final String body = toJson(new SectionRequest(2L, 3L, 5));

                    // when
                    post(SECOND_LINE_SECTIONS_URI, body);
                    final LineResponse response = get(LINES_URI + "/1").as(LineResponse.class);
                    final List<StationResponse> actual = response.getStations();

                    // then
                    assertThat(actual).extracting("name").containsExactly("성수역", "삼성역", "선릉역", "역삼역", "강남역");
                }),

                dynamicTest("성수-삼성-선릉-역삼-강남 에서 삼성역을 제거하면 성수-선릉-역삼-강남 이 된다", () -> {
                    // given
                    final String stationRemoveURI = "/lines/1/sections?stationId=2";

                    // when
                    delete(stationRemoveURI);
                    final LineResponse response = get(LINES_URI + "/1").as(LineResponse.class);
                    final List<StationResponse> actual = response.getStations();

                    // then
                    assertThat(actual).extracting("name").containsExactly("성수역", "선릉역", "역삼역", "강남역");
                }),

                dynamicTest("성수-선릉-역삼-강남 에 거리가 초과되면 구간 추가가 실패한다", () -> {
                    // given
                    final String body = toJson(new SectionRequest(1L, 2L, 50));

                    // when
                    final ExtractableResponse<Response> response = post(SECOND_LINE_SECTIONS_URI, body);
                    final ExceptionResponse exceptionResponse = response.as(ExceptionResponse.class);

                    // then
                    assertAll(
                            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                            () -> assertThat(exceptionResponse.getMessage()).contains("거리 초과로 인해 구간 추가에 실패하였습니다")
                    );
                })
        );
    }
}
