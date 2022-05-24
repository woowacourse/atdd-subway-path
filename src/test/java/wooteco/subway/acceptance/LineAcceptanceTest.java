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
    private ExtractableResponse<Response> responseCreateLine;
    private Long upStationId;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();
        responseCreateLine = insertLine(LINE_NAME, LINE_COLOR, List.of(STATION_NAME1, STATION_NAME2));
    }

    private ExtractableResponse<Response> insertLine(String name, String color, List<String> stationNames) {
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
        //given
        var stations = responseCreateLine.jsonPath().getList("stations", Station.class);
        var names = stations.stream()
                .map(Station::getName)
                .collect(Collectors.toList());

        //when
        assertAll(
                () -> assertThat(responseCreateLine.jsonPath().getString("name")).isEqualTo(LINE_NAME),
                () -> assertThat(responseCreateLine.jsonPath().getString("color")).isEqualTo(LINE_COLOR),
                () -> assertThat(responseCreateLine.jsonPath().getInt("extraFare")).isEqualTo(EXTRA_FARE),
                () -> assertThat(names).containsExactly(STATION_NAME1, STATION_NAME2)
        );
    }

    @Test
    void createLineByDuplicateName() {
        var response = insertLine(LINE_NAME, "테스트색", List.of("테스트3역", "테스트4역"));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void findLineById() {
        var response = find("/lines/" + getId(responseCreateLine));

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.body().jsonPath().get("name").toString()).isEqualTo(LINE_NAME),
                () -> assertThat(response.body().jsonPath().get("color").toString()).isEqualTo(LINE_COLOR)
        );
    }

    @Test
    void findAllLine() {
        /// given
        var responseCreateLine2 = insertLine("테스트2호선", "테스트2색", List.of("테스트3역", "테스트4역"));

        // when
        var response = RestAssured.given().log().all()
                .when()
                .get("/lines")
                .then().log().all()
                .extract();
        var ids = getIds(response);
        var stationNames = response.jsonPath().getList("stations.name", String.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(ids).contains(getId(responseCreateLine)),
                () -> assertThat(ids).contains(getId(responseCreateLine2)),
                () -> assertThat(stationNames).containsExactly("[테스트1역, 테스트2역]", "[테스트3역, 테스트4역]")
        );
    }

    private List<String> getIds(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .map(Object::toString)
                .collect(Collectors.toList());
    }

    @Test
    void updateLine() {
        /// given
        var id = getId(responseCreateLine);

        // when
        var response = requestUpdateLine(id, "테스트2호선", "테스트색");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> requestUpdateLine(String id, String name, String color) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("color", color);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .put("/lines/" + id)
                .then().log().all()
                .extract();
    }

    @DisplayName("다른 노선이 가지고있는 정보로 현재 노선을 업데이트시 400에러 발생")
    @Test
    void failUpdateLine() {
        /// given
        var firstInsertId = getId(responseCreateLine);

        insertLine("테스트2호선", "테스트2색", List.of("테스트3역", "테스트4역"));

        // when
        var response = requestUpdateLine(firstInsertId, "테스트2호선", "테스트색");

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void deleteLineByLineId() {
        var response = delete("/lines/" + getId(responseCreateLine));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


    @Test
    void deleteByInvalidLineId() {
        /// given
        var invalidId = "-1";

        // when
        var response = delete("/lines/" + invalidId);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void createSectionByValidInformation() {
        var newDownStationId = 100L;

        var response = create(upStationId, newDownStationId);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private ExtractableResponse<Response> create(Long upStationId, Long downStationId) {
        var path = "/lines/" + getId(responseCreateLine) + "/sections";

        Map<String, String> params = new HashMap<>();
        params.put("upStationId", upStationId.toString());
        params.put("downStationId", downStationId.toString());
        params.put("distance", "1");

        return create(path, params);
    }

    @Test
    void deleteSection() {
        var newDownStationId = 100L;
        create(upStationId, newDownStationId);

        var path = "/lines/" + getId(responseCreateLine) + "/sections?stationId=" + upStationId;
        var response = delete(path);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}

