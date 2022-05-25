package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.line.LineResponse;

public class LineAcceptanceTest extends AcceptanceTest {

    private static final int EXTRA_FARE = 500;
    private static final int DISTANCE = 2;
    private static final String LINE_NAME = "테스트1호선";
    private static final String LINE_COLOR = "테스트1색";
    private static final String STATION_NAME1 = "테스트1역";
    private static final String STATION_NAME2 = "테스트2역";
    private ExtractableResponse<Response> responseLine;
    private Long upStationId;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
//        responseLine = insertLine(LINE_NAME, LINE_COLOR, List.of(STATION_NAME1, STATION_NAME2));
    }

    @DisplayName("두 개의 역을 생성한 뒤에 두 역을 각각 상행 종점과 하행 종점으로 가지는 노선을 생성한다.")
    private ExtractableResponse<Response> createLine(String name, String color, List<String> stationNames) {
        var response1 = create("/stations", Map.of("name", stationNames.get(0)));
        var response2 = create("/stations", Map.of("name", stationNames.get(1)));

        upStationId = Long.parseLong(getId(response1));

        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("upStationId", getId(response1));
        params.put("downStationId", getId(response2));
        params.put("distance", String.valueOf(DISTANCE));
        params.put("extraFare", String.valueOf(EXTRA_FARE));

        return create("/lines", params);
    }

    @Test
    void createLine() {
        //when
        responseLine = createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));

        //then
        var names = responseLine.jsonPath().getList("stations", Station.class).stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(responseLine.jsonPath().getString("name")).isEqualTo("테스트1호선"),
                () -> assertThat(responseLine.jsonPath().getString("color")).isEqualTo("테스트1색"),
                () -> assertThat(responseLine.jsonPath().getInt("extraFare")).isEqualTo(EXTRA_FARE),
                () -> assertThat(names).containsExactly("테스트1역", "테스트2역")
        );
    }

    @ParameterizedTest
    @CsvSource(value = {"테스트1호선,테스트2색", "테스트2호선,테스트1색"})
    void createLineByInvalidInformation(String lineName, String lineColor) {
        //given
        createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));

        //when
        var response = createLine(lineName, lineColor, List.of("테스트3역", "테스트4역"));

        //then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value()),
                () -> assertThat(response.body().jsonPath().getString("message"))
                        .isEqualTo("[ERROR] 이미 존재하는 노선 정보 입니다.")
        );
    }

    @Test
    @DisplayName("노선 생성 후 해당 노선 조회하기")
    void findLineById() {
        //given
        var responseCreateLine = createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));

        //when
        var response = find("/lines/" + getId(responseCreateLine));

        //then
        var names = response.body().jsonPath().getList("stations", Station.class).stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getString("name")).isEqualTo(LINE_NAME),
                () -> assertThat(response.body().jsonPath().getString("color")).isEqualTo(LINE_COLOR),
                () -> assertThat(response.body().jsonPath().getInt("extraFare")).isEqualTo(EXTRA_FARE),
                () -> assertThat(names).containsExactly("테스트1역", "테스트2역")
        );
    }

    @Test
    @DisplayName("노선 두 개 생성 후 노선 목록 조회하기")
    void findAllLine() {
        /// given
        var responseCreateLine1 = createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));
        var responseCreateLine2 = createLine("테스트2호선", "테스트2색", List.of("테스트3역", "테스트4역"));

        // when
        var responseFindAll = findAll();

        // then
        var ids = getIds(responseFindAll);
        var stationNames = responseFindAll.jsonPath().getList("stations.name", String.class);
        var names = responseFindAll.jsonPath().getList("name", String.class);
        var colors = responseFindAll.jsonPath().getList("color", String.class);

        assertAll(
                () -> assertThat(responseFindAll.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(ids).containsExactly(getId(responseCreateLine1), getId(responseCreateLine2)),
                () -> assertThat(names).containsExactly("테스트1호선", "테스트2호선"),
                () -> assertThat(colors).containsExactly("테스트1색", "테스트2색"),
                () -> assertThat(stationNames).containsExactly("[테스트1역, 테스트2역]", "[테스트3역, 테스트4역]")
        );
    }

    private ExtractableResponse<Response> findAll() {
        return RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
    }

    private List<String> getIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Test
    @DisplayName("노선 생성 -> 노선의 이름, 색상, 추가요금 변경 -> 해당 노선 조회")
    void updateLine() {
        /// given
        var responseCreateLine = createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));
        var id = getId(responseCreateLine);

        // when
        var responseUpdateLine = update(id, "테스트2호선", "테스트2색", 1000);

        // then
        var response = find("/lines/" + id);

        assertAll(
                () -> assertThat(responseUpdateLine.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().getString("name")).isEqualTo("테스트2호선"),
                () -> assertThat(response.body().jsonPath().getString("color")).isEqualTo("테스트2색"),
                () -> assertThat(response.body().jsonPath().getInt("extraFare")).isEqualTo(1000)
        );
    }

    private ExtractableResponse<Response> update(String id, String name, String color, int extraFare) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);
        params.put("extraFare", String.valueOf(extraFare));

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    @DisplayName("다른 노선이 가지고있는 노선이름, 색상으로 현재 노선을 업데이트할 경우 에러발생")
    @ParameterizedTest
    @CsvSource(value = {"테스트2호선,테스트3색", "테스트3호선,테스트2색"})
    void failUpdateLine(String lineName, String lineColor) {
        /// given
        createLine("테스트2호선", "테스트2색", List.of("테스트3역", "테스트4역"));
        var responseCreateLine = createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));
        var lineId = getId(responseCreateLine);

        // when
        var response = update(lineId, lineName, lineColor, 1000);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deleteLineByLineId() {
        //given
        var responseCreateLine = createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));
        var lineId = getId(responseCreateLine);

        //when
        var responseDeleteLine = delete("/lines/" + lineId);

        //then
        var response = findAll();

        var anyMatchByDeletedLineId = response.jsonPath().getList("id", String.class).stream()
                .anyMatch(it -> it.equals(lineId));

        assertAll(
                () -> assertThat(responseDeleteLine.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
                () -> assertThat(anyMatchByDeletedLineId).isFalse()
        );
    }

    @Test
    void deleteByInvalidLineId() {
        // given
        var invalidId = "-1";

        // when
        var response = delete("/lines/" + invalidId);

        // then
        assertThat(response.jsonPath().getString("message")).isEqualTo("[ERROR] 해당 노선이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("노선 등록 -> 역 등록 -> 노선에 상행종점역 등록 -> 노선 조회하기")
    void createSection() {
        //given
        var responseCreateLine = createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));
        var responseCreateStation = create("/stations", Map.of("name", "테스트3역"));
        var lineId = Long.parseLong(getId(responseCreateLine));
        var newStationId = Long.parseLong(getId(responseCreateStation));

        //when
        createSection(lineId, newStationId, upStationId, 10);

        //then
        var response = find("lines/" + lineId);
        var names = response.jsonPath().getList("stations.name", String.class);

        assertThat(names).containsExactly("테스트1역", "테스트2역", "테스트3역");
    }

    @Test
    @DisplayName("노선 등록 -> 역 등록 -> 노선의 역 사이에 새로운 역을 등록할 경우 기존 역 사이보다 큰 경우")
    void failToCreateSection() {
        //given
        var responseCreateLine = createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));
        var responseCreateStation = create("/stations", Map.of("name", "테스트3역"));
        var lineId = Long.parseLong(getId(responseCreateLine));
        var newStationId = Long.parseLong(getId(responseCreateStation));

        //when
        var response = createSection(lineId, upStationId, newStationId, 10);

        //then
        assertThat(response.jsonPath().getString("message")).isEqualTo("[ERROR] 기존 역 사이 길이보다 크거나 같으면 등록을 할 수 없습니다.");
    }

    private ExtractableResponse<Response> createSection(Long lineId,
                                                        Long upStationId,
                                                        Long downStationId,
                                                        int distance
    ) {
        var path = "/lines/" + lineId + "/sections";

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", String.valueOf(distance));

        return create(path, params);
    }

    @Test
    @DisplayName("노선 등록 -> 역 등록 -> 노선에 상행 종점역 등록 -> 구간 제거(중간역 제거) -> 노선 조회하기")
    void deleteSection() {
        //given
        var responseCreateLine = createLine("테스트1호선", "테스트1색", List.of("테스트1역", "테스트2역"));
        var responseCreateStation = create("/stations", Map.of("name", "테스트3역"));
        var lineId = Long.parseLong(getId(responseCreateLine));
        var newStationId = Long.parseLong(getId(responseCreateStation));
        createSection(lineId, newStationId, upStationId, 10);

        //when
        var path = "lines/" + lineId + "/sections?stationId=" + upStationId;
        delete(path);

        //then
        var response = find("lines/" + lineId);
        var names = response.jsonPath().getList("stations.name", String.class);

        assertThat(names).containsExactly("테스트2역", "테스트3역");
    }
}

