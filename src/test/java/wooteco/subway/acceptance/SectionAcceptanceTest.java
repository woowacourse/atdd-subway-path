package wooteco.subway.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.section.SectionRequest;
import wooteco.subway.dto.station.StationRequest;

@DisplayName("지하철 구간 관련 기능")
class SectionAcceptanceTest extends AcceptanceTest {

    private static final String NAME = "name";
    private static final String COLOR = "color";
    private static final String STATION_NAMES = "stations.name";
    private static final String MESSAGE = "message";

    private static final String LINE_NAME = "2호선";
    private static final String LINE_COLOR = "bg-green-600";

    private Station yeoksam;
    private Station seolleung;
    private Station samsung;

    @BeforeEach
    void setUpData() {
        yeoksam = requestPost(new StationRequest(YEOKSAM), STATION_URL_PREFIX).extract().as(Station.class);
        seolleung = requestPost(new StationRequest(SEOLLEUNG), STATION_URL_PREFIX).extract().as(Station.class);
        samsung = requestPost(new StationRequest(SAMSUNG), STATION_URL_PREFIX).extract().as(Station.class);
    }

    @Test
    @DisplayName("기존 구간 사이에 새로운 상행 구간을 등록한다.")
    void CreateSection_NewUpSection_OK() {
        // given
        final SectionRequest request = new SectionRequest(yeoksam.getId(), seolleung.getId(), 7);
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPostSection(request, lineId);
        final ValidatableResponse actualResponse = requestGet(LINE_URL_PREFIX + "/" + lineId);

        // then
        response.statusCode(HttpStatus.CREATED.value());
        actualResponse.body(NAME, equalTo(LINE_NAME))
                .body(COLOR, equalTo(LINE_COLOR))
                .body(STATION_NAMES, contains(YEOKSAM, SEOLLEUNG, SAMSUNG));
    }

    @Test
    @DisplayName("기존 구간 사이에 새로운 하행 구간을 등록한다.")
    void CreateSection_NewDownSection_OK() {
        // given
        final SectionRequest request = new SectionRequest(seolleung.getId(), samsung.getId(), 7);
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPostSection(request, lineId);
        final ValidatableResponse actualResponse = requestGet(LINE_URL_PREFIX + "/" + lineId);

        // then
        response.statusCode(HttpStatus.CREATED.value());
        actualResponse.body(NAME, equalTo(LINE_NAME))
                .body(COLOR, equalTo(LINE_COLOR))
                .body(STATION_NAMES, contains(YEOKSAM, SEOLLEUNG, SAMSUNG));

    }

    @ParameterizedTest
    @DisplayName("등록하려는 구간의 길이가 기존 구간의 길이보다 더 길거나 같으면 400을 반환한다.")
    @ValueSource(ints = {10, 11})
    void CreateSection_InvalidDistance_BadRequest(final int newSectionDistance) {
        // given
        final SectionRequest request = new SectionRequest(yeoksam.getId(), seolleung.getId(), newSectionDistance);
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPostSection(request, lineId);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE, equalTo("기존 구간의 길이 보다 작지 않습니다."));
    }

    @Test
    @DisplayName("등록하려는 노선에 상행, 하행 역이 이미 모두 포함되어 있으면 400을 반환한다.")
    void CreateSection_BothStationAlreadyIncluded_BadRequest() {
        // given
        final SectionRequest request = new SectionRequest(yeoksam.getId(), samsung.getId(), 7);
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPostSection(request, lineId);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE, equalTo("상행역과 하행역 중 하나의 역만 노선에 포함되어 있어야 합니다."));
    }

    @Test
    @DisplayName("등록하려는 노선에 상행, 하행 역이 모두 포함되어 있지 않으면 400을 반환한다.")
    void CreateSection_BothStationNotIncluded_BadRequest() {
        // given
        final long upStationId = createAndGetId(new StationRequest(DAPSIMNI), STATION_URL_PREFIX);
        final long downStationId = createAndGetId(new StationRequest(WANGSIMNI), STATION_URL_PREFIX);
        final SectionRequest request = new SectionRequest(upStationId, downStationId, 7);
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPostSection(request, lineId);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE, equalTo("상행역과 하행역 중 하나의 역만 노선에 포함되어 있어야 합니다."));
    }

    @Test
    @DisplayName("새로운 상행 종점을 등록한다.")
    void CreateSection_NewUpStation_OK() {
        // given
        final SectionRequest request = new SectionRequest(seolleung.getId(), yeoksam.getId(), 7);
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPostSection(request, lineId);
        final ValidatableResponse actualResponse = requestGet(LINE_URL_PREFIX + "/" + lineId);

        // then
        response.statusCode(HttpStatus.CREATED.value());
        actualResponse.body(NAME, equalTo(LINE_NAME))
                .body(COLOR, equalTo(LINE_COLOR))
                .body(STATION_NAMES, contains(SEOLLEUNG, YEOKSAM, SAMSUNG));
    }

    @Test
    @DisplayName("새로운 하행 종점을 등록한다.")
    void CreateSection_NewDownStation_OK() {
        // given
        final SectionRequest request = new SectionRequest(samsung.getId(), seolleung.getId(), 7);
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPostSection(request, lineId);
        final ValidatableResponse actualResponse = requestGet(LINE_URL_PREFIX + "/" + lineId);

        // then
        response.statusCode(HttpStatus.CREATED.value());
        actualResponse.body(NAME, equalTo(LINE_NAME))
                .body(COLOR, equalTo(LINE_COLOR))
                .body(STATION_NAMES, contains(YEOKSAM, SAMSUNG, SEOLLEUNG));
    }

    @Test
    @DisplayName("상행 종점 구간을 삭제한다.")
    void DeleteSection_UpEndStation_OK() {
        // given
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        requestPostSection(new SectionRequest(samsung.getId(), seolleung.getId(), 7), lineId);

        // when
        final ValidatableResponse response = requestDeleteSection(lineId, yeoksam.getId());
        final ValidatableResponse actualResponse = requestGet(LINE_URL_PREFIX + "/" + lineId);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        actualResponse.body(NAME, equalTo(LINE_NAME))
                .body(COLOR, equalTo(LINE_COLOR))
                .body(STATION_NAMES, contains(SAMSUNG, SEOLLEUNG));
    }

    @Test
    @DisplayName("하행 종점 구간을 삭제한다.")
    void DeleteSection_DownEndStation_OK() {
        // given
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        requestPostSection(new SectionRequest(samsung.getId(), seolleung.getId(), 7), lineId);

        // when
        final ValidatableResponse response = requestDeleteSection(lineId, seolleung.getId());
        final ValidatableResponse actualResponse = requestGet(LINE_URL_PREFIX + "/" + lineId);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        actualResponse.body(NAME, equalTo(LINE_NAME))
                .body(COLOR, equalTo(LINE_COLOR))
                .body(STATION_NAMES, contains(YEOKSAM, SAMSUNG));
    }

    @Test
    @DisplayName("종점이 아닌 중간 역을 삭제한다.")
    void DeleteSection_NotEndStation_OK() {
        // given
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        requestPostSection(new SectionRequest(samsung.getId(), seolleung.getId(), 7), lineId);

        // when
        final ValidatableResponse response = requestDeleteSection(lineId, samsung.getId());
        final ValidatableResponse actualResponse = requestGet(LINE_URL_PREFIX + "/" + lineId);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
        actualResponse.body(NAME, equalTo(LINE_NAME))
                .body(COLOR, equalTo(LINE_COLOR))
                .body(STATION_NAMES, contains(YEOKSAM, SEOLLEUNG));
    }

    @Test
    @DisplayName("구간이 하나인 역은 삭제할 수 없다.")
    void DeleteSection_LastOneSection_BadRequest() {
        // given
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestDeleteSection(lineId, samsung.getId());

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value())
                .body(MESSAGE, equalTo("구간을 삭제할 수 없습니다."));
    }

    @Test
    @DisplayName("삭제하려는 구간이 노선에 존재하지 않으면 404을 반환한다.")
    void DeleteSection_NotIncludedStation_BadRequest() {
        // given
        final long lineId = createAndGetId(new LineRequest(
                LINE_NAME,
                LINE_COLOR,
                yeoksam.getId(),
                samsung.getId(),
                10
        ), LINE_URL_PREFIX);

        requestPostSection(new SectionRequest(samsung.getId(), seolleung.getId(), 7), lineId);

        // when
        final ValidatableResponse response = requestDeleteSection(lineId, 999L);

        // then
        response.statusCode(HttpStatus.NOT_FOUND.value())
                .body(MESSAGE, equalTo("구간이 존재하지 않습니다."));
    }
}
