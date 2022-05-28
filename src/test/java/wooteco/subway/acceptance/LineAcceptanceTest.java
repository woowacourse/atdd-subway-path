package wooteco.subway.acceptance;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.LineResponse;
import wooteco.subway.dto.StationRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static wooteco.subway.acceptance.AcceptanceFixture.*;

public class LineAcceptanceTest extends AcceptanceTest {

    @BeforeEach()
    void setStation() {
        //given
        insert(new StationRequest("강남역"), "/stations", 201);
        insert(new StationRequest("역삼역"), "/stations", 201);
        insert(new StationRequest("선릉역"), "/stations", 201);
        insert(new StationRequest("잠실역"), "/stations", 201);
    }

    @Test
    @DisplayName("지하철 노선 생성")
    void createLines() {
        /*
        * Scenario: 지하철 노선 생성
        *   Given 지하철 역이 등록되어 있다.
        *   When 지하철 노선 등록을 요청한다.
        *   Then 생성한 지하철 노선을 응답받는다.
        * */

        //when & then
        insert(new LineRequest("신분당선", "bg-red-600",
                1L, 2L, 10, 100), "/lines", 201)
                .header("Location", is("/lines/1"))
                .body("name", is("신분당선"))
                .body("color", is("bg-red-600"));
    }

    @Test
    @DisplayName("중복된 지하철 노선 생성 예외")

    void checkDuplicateLine() {
        /*
         * Scenario: 중복된 지하철 노선 생성시 예외
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 중복된 이름의 지하철 노선 등록을 요청한다.
         *   Then 지하철 노선은 등록에 실패하고 예외 메시지를 받게 된다.
         * */

        //given
        insert(new LineRequest("신분당선", "bg-red-600",
                1L, 2L, 10, 100), "/lines", 201);

        //when & then
        insert(new LineRequest("신분당선", "bg-red-600",
                1L, 2L, 10, 100), "/lines", 404);
    }

    @Test
    @DisplayName("지하철 노선 조회 예외 - 존재하지 않는 노선 id")
    void getLineException() {
        /*
         * Scenario: 존재하지 않는 지하철 노선 조회 예외
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 존재하지 않는 지하철 노선 조회를 요청한다.
         *   Then 지하철 노선 조회에 실패하고 예외 메시지를 받게 된다.
         * */

        //given
        insert(new LineRequest("신분당선", "bg-red-600",
                1L, 2L, 10, 100), "/lines", 201);

        //when & then
        select("/lines/" + 0L, 404);
    }

    @Test
    @DisplayName("지하철 노선 조회")
    void getLine() {
        /*
         * Scenario: 지하철 노선 조회
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 지하철 노선 조회를 요청한다.
         *   Then 지하철 노선 정보를 응답받는다.
         * */

        //given
        long id = insert(new LineRequest("신분당선", "bg-red-600",
                1L, 2L, 10, 100), "/lines", 201)
                .extract().jsonPath().getLong("id");

        // then
        select("/lines/" + id, 200).body("stations.size()", is(2));
    }

    @Test
    @DisplayName("지하철 노선 목록 조회")
    void getLines() {
        /*
         * Scenario: 전체 지하철 목록 조회
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선들이 등록되어 있다.
         *   When 전체 지하철 노선 목록 조회를 요청한다.
         *   Then 전체 지하철 노선 정보를 응답받는다.
         * */

        //given
        ExtractableResponse<Response> createResponse = insert(new LineRequest("신분당선", "bg-red-600",
                1L, 2L, 10, 100), "/lines", 201).extract();
        ExtractableResponse<Response> newCreateResponse = insert(new LineRequest("분당선", "bg-green-600",
                1L, 2L, 10, 100), "/lines", 201).extract();

        //when
        ExtractableResponse<Response> response = select("/lines", 200).extract();

        List<Long> expectedLineIds = Stream.of(createResponse, newCreateResponse)
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", LineResponse.class).stream()
                .map(LineResponse::getId)
                .collect(Collectors.toList());

        //then
        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @Test
    @DisplayName("지하철 노선 수정")
    void modifyLine() {
        /*
         * Scenario: 지하철 노선 수정
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 지하철 노선 수정을 요청한다.
         *   Then 수정이 성공적으로 처리되었다는 응답을 받는다.
         * */

        // given
        long id = insert(new LineRequest("신분당선", "bg-red-600",
                1L, 2L, 10, 100), "/lines", 201)
                .extract().jsonPath().getLong("id");

        //when & then
        put("/lines/" + id, new LineRequest("분당선", "bg-red-600", 1L, 2L,
                10, 100), 200);
    }

    @Test
    @DisplayName("지하철 노선 수정 예외 - 기존에 존재하던 노선 이름으로 변경한 경우")
    void checkDuplicateSameName() {
        /*
         * Scenario: 이미 존재하고 있는 노선 이름으로 노선의 이름을 수정한 경우 예외
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 이미 존재하는 지하철 이름으로 지하철 노선 이름을 수정 요청한다.
         *   Then 지하철 노선 수정에 실패하고 예외 메시지를 받게 된다.
         * */

        // given
        long id = insert(new LineRequest("신분당선", "bg-red-600",
                1L, 2L, 10, 100), "/lines", 201)
                .extract().jsonPath().getLong("id");
        insert(new LineRequest("분당선", "bg-green-600",
                2L, 3L, 10, 100), "/lines", 201);

        //when & then
        put("/lines/" + id, new LineRequest("분당선", "bg-red-600", 1L, 2L, 10, 100)
                , 404);
    }

    @Test
    @DisplayName("지하철 노선 삭제")
    void deleteLine() {
        /*
         * Scenario: 지하철 노선 삭제
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 지하철 노선 삭제를 요청한다.
         *   Then 수정이 성공적으로 수행되었다는 응답을 받게 된다.
         * */

        // given
        long id = insert(new LineRequest("신분당선", "bg-red-600",
                1L, 2L, 10, 100), "/lines", 201)
                .extract().jsonPath().getLong("id");

        //when & then
        delete("/lines/" + id, 204);
    }
}
