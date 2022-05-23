package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createLineResponse;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createSectionResponse;
import static wooteco.subway.acceptance.AcceptanceTestFixture.createStationResponse;
import static wooteco.subway.acceptance.AcceptanceTestFixture.deleteSectionResponse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

@DisplayName("지하철구간 관련 기능")
public class SectionAcceptanceTest extends AcceptanceTest {
    private final Station 건대입구 = new Station(1L, "건대입구역");
    private final Station 잠실 = new Station(2L, "잠실역");
    private final Station 선릉 = new Station(3L, "선릉역");
    private final Station 강남 = new Station(4L, "강남역");
    private final Station 노원 = new Station(5L, "노원역");
    private final Station 서울대입구 = new Station(6L, "서울대입구역");
    private final Station 성수 = new Station(7L, "성수역");

    private final LineRequest 이호선 =
            new LineRequest("2호선", "bg-green-600",
                    1L, 4L, 50, 900);

    private final SectionRequest 건대입구_강남 =
            new SectionRequest(1L, 4L, 50);
    private final SectionRequest 건대입구_잠실 =
            new SectionRequest(1L, 2L, 30);
    private final SectionRequest 잠실_강남 =
            new SectionRequest(2L, 4L, 20);
    private final SectionRequest 선릉_강남 =
            new SectionRequest(3L, 4L, 10);
    private final SectionRequest 노원_건대입구 =
            new SectionRequest(5L, 1L, 30);
    private final SectionRequest 건대입구_서울대입구 =
            new SectionRequest(1L, 6L, 60);
    private final SectionRequest 강남_서울대입구 =
            new SectionRequest(4L, 6L, 10);
    private final SectionRequest 성수_건대입구 =
            new SectionRequest(7L, 1L, 10);


    @BeforeEach
    void init() {
        createStationResponse(new StationRequest(건대입구.getName()));
        createStationResponse(new StationRequest(잠실.getName()));
        createStationResponse(new StationRequest(선릉.getName()));
        createStationResponse(new StationRequest(강남.getName()));
        createStationResponse(new StationRequest(노원.getName()));
        createStationResponse(new StationRequest(서울대입구.getName()));
        createStationResponse(new StationRequest(성수.getName()));

        createLineResponse(이호선);
    }

    @DisplayName("지하철 구간을 같은 상행역에 등록한다.")
    @Test
    void createSectionSameUpStation() {
        // given
        // when
        ExtractableResponse<Response> response = createSectionResponse(1L, 건대입구_잠실);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        findSectionsByCreateLine(1L,
                new StationResponse(건대입구),
                new StationResponse(잠실),
                new StationResponse(강남)
        );
    }

    @DisplayName("지하철 구간을 같은 하행역에 등록한다.")
    @Test
    void createSectionSameDownStation() {
        // given
        // when
        ExtractableResponse<Response> response = createSectionResponse(1L, 잠실_강남);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        findSectionsByCreateLine(1L,
                new StationResponse(건대입구),
                new StationResponse(잠실),
                new StationResponse(강남)
        );
    }

    @DisplayName("상행 종점을 등록한다.")
    @Test
    void createFinalUpSection() {
        // given
        // when
        ExtractableResponse<Response> response = createSectionResponse(1L, 성수_건대입구);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        findSectionsByCreateLine(1L,
                new StationResponse(성수),
                new StationResponse(건대입구),
                new StationResponse(강남)
        );
    }

    @DisplayName("하행 종점을 등록한다.")
    @Test
    void createFinalDownSection() {
        // given
        // when
        ExtractableResponse<Response> response = createSectionResponse(1L, 강남_서울대입구);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        findSectionsByCreateLine(1L,
                new StationResponse(건대입구),
                new StationResponse(강남),
                new StationResponse(서울대입구)
        );
    }

    private void findSectionsByCreateLine(Long lineId,
                                          StationResponse stationResponse1,
                                          StationResponse stationResponse2,
                                          StationResponse stationResponse3) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines/" + lineId)
                .then().log().all()
                .extract();

        Line line = new Line(lineId, 이호선.getName(), 이호선.getColor(), 이호선.getExtraFare());

        final LineResponse expected = new LineResponse(line,
                List.of(stationResponse1, stationResponse2, stationResponse3));
        final LineResponse actual = response.jsonPath().getObject(".", LineResponse.class);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }

    @DisplayName("존재하지 않는 노선에 지하철 구간을 등록할 때 예외를 발생시킨다.")
    @Test
    void createSectionNotExistLine() {
        // given
        // when
        ExtractableResponse<Response> response = createSectionResponse(3L, 노원_건대입구);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("존재하는 구간보다 더 긴 구간을 등록할 때 예외를 발생시킨다.")
    @Test
    void createLongerSection() {
        // given
        // when
        ExtractableResponse<Response> response = createSectionResponse(1L, 건대입구_서울대입구);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("이미 존재하는 구간을 등록할 때 예외를 발생시킨다.")
    @Test
    void createDuplicateSection() {
        // given
        // when
        ExtractableResponse<Response> response = createSectionResponse(1L, 건대입구_강남);
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("지하철 중간 구간을 제거한다.")
    @Test
    void deleteMiddleSection() {
        // given
        createSectionResponse(1L, 건대입구_잠실);
        createSectionResponse(1L, 선릉_강남);
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 2L);
        // when
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        findSectionsByDeleteLine(1L,
                new StationResponse(건대입구),
                new StationResponse(선릉),
                new StationResponse(강남)
        );
    }

    @DisplayName("지하철 상행 종점 구간을 제거한다.")
    @Test
    void deleteFinalUpSection() {
        // given
        createSectionResponse(1L, 건대입구_잠실);
        createSectionResponse(1L, 선릉_강남);
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 1L);
        // when
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        findSectionsByDeleteLine(1L,
                new StationResponse(잠실),
                new StationResponse(선릉),
                new StationResponse(강남)
        );
    }

    @DisplayName("지하철 하행 종점 구간을 제거한다.")
    @Test
    void deleteFinalDownSection() {
        // given
        createSectionResponse(1L, 건대입구_잠실);
        createSectionResponse(1L, 선릉_강남);
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 4L);
        // when
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        findSectionsByDeleteLine(1L,
                new StationResponse(건대입구),
                new StationResponse(잠실),
                new StationResponse(선릉)
        );
    }

    @DisplayName("존재하지 않는 구간을 제거할 때 예외를 발생시킨다.")
    @Test
    void deleteNotExistSection() {
        // given
        createSectionResponse(1L, 선릉_강남);
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 2L);
        // when
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("구간이 하나만 존재할 경우 구간을 제거할 때 예외를 발생시킨다.")
    @Test
    void deleteOnlyOneExistSection() {
        // given
        ExtractableResponse<Response> response = deleteSectionResponse(1L, 4L);
        // when
        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void findSectionsByDeleteLine(Long lineId, StationResponse stationResponse1,
                                          StationResponse stationResponse2,
                                          StationResponse stationResponse3) {
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .when()
                .get("/lines/" + lineId)
                .then().log().all()
                .extract();
        checkByDeleteValidSections(lineId, response, stationResponse1, stationResponse2, stationResponse3);
    }

    private void checkByDeleteValidSections(Long lineId, ExtractableResponse<Response> response,
                                            StationResponse stationResponse1,
                                            StationResponse stationResponse2,
                                            StationResponse stationResponse3) {
        final List<StationResponse> stationResponses =
                List.of(stationResponse1, stationResponse2, stationResponse3);

        Line line = new Line(lineId, 이호선.getName(), 이호선.getColor(), 이호선.getExtraFare());

        final LineResponse expected = new LineResponse(line, stationResponses);
        final LineResponse actual = response.jsonPath().getObject(".", LineResponse.class);

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);
    }
}
