package wooteco.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.dto.StationRequest;
import wooteco.subway.dto.StationResponse;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static wooteco.subway.acceptance.AcceptanceFixture.*;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("지하철역 생성")
    void createStation() {
        /*
         * Scenario: 지하철역 생성
         *   When 지하철 역 등록을 요청한다.
         *   Then 생성된 지하철 역을 응답받는다.
         * */

        //given
        insert(new StationRequest("강남역"), "/stations", 201)
                .header("Location", is("/stations/1"))
                .body("name", is("강남역"));
    }

    @Test
    @DisplayName("중복된 지하철역 생성")
    void createStationWithDuplicateName() {
        /*
         * Scenario: 지하철역 생성
         *   Given 지하철 역 등록되어 있다.
         *   When 중복된 지하철 역 등록을 요청한다.
         *   Then 지하철 역 등록을 실패하고 예외 메시지를 응답받는다.
         * */

        //given
        insert(new StationRequest("강남역"), "/stations", 201);

        //when & then
        insert(new StationRequest("강남역"), "/stations", 404);
    }

    @Test
    @DisplayName("지하철역 목록 조회")
    void getStations() {
        /*
         * Scenario: 지하철역 목록 조회
         *   Given 지하철 역들이 등록되어 있다.
         *   When 지하철 역 목록 조회를 요청한다.
         *   Then 지하철 역의 목록을 응답받는다.
         * */

        ///given
        ExtractableResponse<Response> stationResponse = insert(new StationRequest("강남역"), "/stations", 201).extract();
        ExtractableResponse<Response> newStationResponse = insert(new StationRequest("역삼역"), "/stations", 201).extract();

        //when
        ExtractableResponse<Response> response = select("/stations", 200).extract();

        //then
        List<Long> expectedLineIds = Arrays.asList(stationResponse, newStationResponse).stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[2]))
                .collect(Collectors.toList());
        List<Long> resultLineIds = response.jsonPath().getList(".", StationResponse.class).stream()
                .map(StationResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    @Test
    @DisplayName("지하철역 제거")
    void deleteStation() {
        /*
         * Scenario: 지하철역 제거
         *   Given 지하철 역이 등록되어 있다.
         *   When 지하쳘 역 삭제를 요청한다.
         *   Then 지하철 역 삭제가 성공적으로 처리되었다는 응답을 받는다.
         * */

        //given
        ExtractableResponse<Response> stationResponse = insert(new StationRequest("강남역"),
                "/stations", 201).extract();

        //when & then
        String uri = stationResponse.header("Location");
        delete(uri, 204);
    }
}
