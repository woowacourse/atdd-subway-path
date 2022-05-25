package wooteco.subway.acceptance;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;

import io.restassured.response.ValidatableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.line.LineRequest;
import wooteco.subway.dto.section.SectionRequest;
import wooteco.subway.dto.station.StationRequest;

@DisplayName("지하철 노선 관련 기능")
class LineAcceptanceTest extends AcceptanceTest {

    private static final String NAME = "name";
    private static final String COLOR = "color";
    private static final String STATION_NAMES = "stations.name";

    private static final String RED_LINE_NAME = "1호선";
    private static final String RED_LINE_COLOR = "bg-red-600";
    private static final String GREEN_LINE_NAME = "2호선";
    private static final String GREEN_LINE_COLOR = "bg-green-600";

    private Station seolleung;
    private Station yeoksam;
    private Station wangsimni;
    private Station dapsimni;
    private Station samsung;

    private LineRequest redLineRequest;
    private LineRequest greenLineRequest;

    @BeforeEach
    void setUpData() {
        seolleung = requestPost(new StationRequest(SEOLLEUNG), STATION_URL_PREFIX).extract().as(Station.class);
        yeoksam = requestPost(new StationRequest(YEOKSAM), STATION_URL_PREFIX).extract().as(Station.class);
        wangsimni = requestPost(new StationRequest(WANGSIMNI), STATION_URL_PREFIX).extract().as(Station.class);
        dapsimni = requestPost(new StationRequest(DAPSIMNI), STATION_URL_PREFIX).extract().as(Station.class);
        samsung = requestPost(new StationRequest(SAMSUNG), STATION_URL_PREFIX).extract().as(Station.class);

        redLineRequest = new LineRequest(
                RED_LINE_NAME,
                RED_LINE_COLOR,
                seolleung.getId(),
                yeoksam.getId(),
                10,
                0
        );
        greenLineRequest = new LineRequest(
                GREEN_LINE_NAME,
                GREEN_LINE_COLOR,
                wangsimni.getId(),
                dapsimni.getId(),
                7,
                0
        );
    }

    @Test
    @DisplayName("지하철 노선과 구간을 생성한다.")
    void CreateLine_WithSection_Success() {
        // when
        final ValidatableResponse response = requestPost(redLineRequest, LINE_URL_PREFIX);
        final long lineId = findId(response);

        // then
        response.statusCode(HttpStatus.CREATED.value())
                .header(LOCATION, equalTo(LINE_URL_PREFIX + "/" + lineId))
                .body(NAME, equalTo(RED_LINE_NAME))
                .body(COLOR, equalTo(RED_LINE_COLOR))
                .body(STATION_NAMES, contains(SEOLLEUNG, YEOKSAM));
    }

    @DisplayName("모든 노선을 조회한다.")
    @Test
    void Show_Lines() {
        // given
        requestPost(redLineRequest, LINE_URL_PREFIX);
        requestPost(greenLineRequest, LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestGet(LINE_URL_PREFIX);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body(STATION_NAMES, hasItems(contains(SEOLLEUNG, YEOKSAM), contains(WANGSIMNI, DAPSIMNI)))
                .body(NAME, contains(RED_LINE_NAME, GREEN_LINE_NAME))
                .body(COLOR, contains(RED_LINE_COLOR, GREEN_LINE_COLOR));
    }

    @DisplayName("id로 노선을 조회한다.")
    @Test
    void ShowLine() {
        // given
        final long id = createAndGetId(redLineRequest, LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestGet(LINE_URL_PREFIX + "/" + id);

        // then
        response.statusCode(HttpStatus.OK.value())
                .body(NAME, equalTo(RED_LINE_NAME))
                .body(COLOR, equalTo(RED_LINE_COLOR))
                .body(STATION_NAMES, contains(SEOLLEUNG, YEOKSAM));
    }

    @DisplayName("id로 5개의 역을 포함한 노선을 상행에서 하행 순서로 정렬해서 조회한다.")
    @Test
    void ShowLine_5StationsOrderByUpStation_OK() {
        // given
        final long lineId = createAndGetId(redLineRequest, LINE_URL_PREFIX);

        requestPostSection(new SectionRequest(dapsimni.getId(), yeoksam.getId(), 5), lineId);
        requestPostSection(new SectionRequest(yeoksam.getId(), wangsimni.getId(), 5), lineId);
        requestPostSection(new SectionRequest(samsung.getId(), yeoksam.getId(), 3), lineId);

        // when
        final ValidatableResponse response = requestGet(LINE_URL_PREFIX + "/" + lineId);

        // then
        // 선릉 - 답십리 - 삼성 - 역삼 - 왕십리
        response.statusCode(HttpStatus.OK.value())
                .body(NAME, equalTo(RED_LINE_NAME))
                .body(COLOR, equalTo(RED_LINE_COLOR))
                .body(STATION_NAMES, contains(SEOLLEUNG, DAPSIMNI, SAMSUNG, YEOKSAM, WANGSIMNI));
    }

    @Test
    @DisplayName("존재하지 않은 id로 조회하면 NOT_FOUND를 반환한다.")
    void ShowLine_NotExistId_NotFound() {
        // when
        final ValidatableResponse response = requestGet(LINE_URL_PREFIX + "/999");

        // then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("지하철 노선 정보를 수정한다.")
    void UpdateLine() {
        // given
        final long id = createAndGetId(redLineRequest, LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPut(greenLineRequest, LINE_URL_PREFIX + "/" + id);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("수정하려는 노선 이름이 중복되면 BAD_REQUEST를 반환한다.")
    void UpdateLine_DuplicateName_BadRequest() {
        // given
        requestPost(greenLineRequest, LINE_URL_PREFIX);
        final long id = createAndGetId(redLineRequest, LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestPut(greenLineRequest, LINE_URL_PREFIX + "/" + id);

        // then
        response.statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    @DisplayName("수정하려는 노선 id가 존재하지 않으면 404를 반환한다.")
    void UpdateLine_NotExistId_BadRequest() {
        // when
        final ValidatableResponse response = requestPut(redLineRequest, LINE_URL_PREFIX + "/999");

        // then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    @DisplayName("지하철 노선 정보를 삭제한다.")
    void DeleteLine() {
        // given
        final long id = createAndGetId(redLineRequest, LINE_URL_PREFIX);

        // when
        final ValidatableResponse response = requestDelete(LINE_URL_PREFIX + "/" + id);

        // then
        response.statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    @DisplayName("존재하지 않는 노선을 제거하면 404를 반환한다.")
    void DeleteLine_NotExistId_BadRequest() {
        // when
        final ValidatableResponse response = requestDelete(LINE_URL_PREFIX + "/999");

        // then
        response.statusCode(HttpStatus.NOT_FOUND.value());
    }
}
