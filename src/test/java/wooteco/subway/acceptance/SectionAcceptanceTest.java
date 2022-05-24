package wooteco.subway.acceptance;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.dto.LineRequest;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.dto.StationRequest;

import static wooteco.subway.acceptance.AcceptanceFixture.delete;
import static wooteco.subway.acceptance.AcceptanceFixture.insert;

public class SectionAcceptanceTest extends AcceptanceTest {

    @BeforeEach()
    void setStation() {
        //given
        insert(new StationRequest("강남역"), "/stations", 201);
        insert(new StationRequest("역삼역"), "/stations", 201);
        insert(new StationRequest("선릉역"), "/stations", 201);
        insert(new StationRequest("잠실역"), "/stations", 201);
    }

    @Test
    @DisplayName("지하철 노선 구간 등록")
    void createSection() {
        /*
         * Scenario: 지하철 노선 구간 등록
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 지하철 노선에 구간 등록을 요청한다.
         *   Then 지하철 구간이 등록이 성공적으로 처리되었음을 알려준다.
         * */

        //given
        long id = insert(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10, 100),
                "/lines", 201).extract().jsonPath().getLong("id");

        //when & then
        insert(new SectionRequest(2L, 3L, 10), "/lines/" + id + "/sections", 201);
    }

    @Test
    @DisplayName("지하철 노선 구간 등록 예외 - 존재하지 않는 노선")
    void checkNotExistLine() {
        /*
         * Scenario: 존재하지 않는 지하철 노선에 구간을 등록하는 경우 예외
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 존재하지 않는 노선에 지하철 구간 등록을 요청한다.
         *   Then 지하철 구간 등록이 실패하고 예외 메시지를 응답받는다.
         * */

        //given
        insert(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10, 100), "/lines", 201);

        //when & then
        insert(new SectionRequest(2L, 3L, 10), "/lines/" + 0 + "/sections", 404);
    }

    @Test
    @DisplayName("지하철 노선 구간 등록 예외 - 역 사이 간격이 조건에 맞지 않는 경우")
    void createSectionLengthException() {
        /*
         * Scenario: 역 사이의 간격이 조건에 맞지 않는 경우, 지하철 구간 등록 예외
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 존재하지 않는 노선에 지하철 구간 등록을 요청한다.
         *   Then 지하철 구간 등록이 실패하고 예외 메시지를 응답받는다.
         * */

        //given
        long id = insert(new LineRequest("신분당선", "bg-red-600", 1L, 3L, 10, 100),
                "/lines", 201).extract().jsonPath().getLong("id");

        //when & then
        insert(new SectionRequest(1L, 2L, 10), "/lines/" + id + "/sections", 404);
    }

    @Test
    @DisplayName("지하철 노선 구간 등록 예외 - 노선에 상행, 하행역이 이미 모두 추가되어 있는 경우")
    void createSameSectionException() {
        /*
         * Scenario: 노선에 상행, 하행역이 이미 모두 추가되어 있는 경우, 지하철 구간 등록 예외
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 노선에 모두 존재하는 상행, 하행역으로 구간 등록을 요청한다.
         *   Then 지하철 구간 등록이 실패하고 예외 메시지를 응답받는다.
         * */

        //given
        long id = insert(new LineRequest("신분당선", "bg-red-600", 1L, 3L, 10, 100),
                "/lines", 201).extract().jsonPath().getLong("id");

        //when & then
        insert(new SectionRequest(1L, 3L, 2), "/lines/" + id + "/sections", 404);
    }

    @Test
    @DisplayName("지하철 노선 구간 등록 예외 - 등록하려는 노선이 하나도 겹치지 않는 경우")
    void createSectionNotAnySameException() {
        /*
         * Scenario: 노선에 상행, 하행역이 노선에 모두 존재하지 않는 경우, 지하철 구간 등록 예외
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   When 노선에 모두 존재하지 않는 상행, 하행역으로 구간 등록을 요청한다.
         *   Then 지하철 구간 등록이 실패하고 예외 메시지를 응답받는다.
         * */

        //given
        long id = insert(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10, 100),
                "/lines", 201).extract().jsonPath().getLong("id");

        //when & then
        insert(new SectionRequest(3L, 4L, 2), "/lines/" + id + "/sections", 404);
    }

    @Test
    @DisplayName("지하철 노선 구간 제거")
    void deleteSection() {
        /*
         * Scenario: 지하철 노선 구간 제거
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선이 등록되어 있다.
         *   And 지하철 구간이 등록되어 있다.
         *   When 지하철 노선의 구간의 삭제 요청을 한다.
         *   Then 지하철 구간 삭제를 성공적으로 처리되었다는 응답을 받는다.
         * */

        //given
        long id = insert(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10, 100),
                "/lines", 201).extract().jsonPath().getLong("id");

        //given
        insert(new SectionRequest(2L, 3L, 10), "/lines/" + id + "/sections", 201);

        //when & then
        delete("/lines/" + id + "/sections?stationId=2", 200);
    }

    @Test
    @DisplayName("지하철 구간 제거 예외 - 제거하려는 노선에서 구간이 1개인 경우")
    void deleteOneSectionException() {
        /*
         * Scenario: 노선에 구간이 1개일 때, 구간을 삭제하려는 경우 예외
         *   Given 지하철 역이 등록되어 있다.
         *   And 지하철 노선을 등록한다.
         *   When 지하철 노선에 구간이 1개 존재하는데, 삭제를 요청한다.
         *   Then 지하철 구간 삭제 요청을 실패하고 예외 메시지를 응답받는다.
         * */

        //given
        long id = insert(new LineRequest("신분당선", "bg-red-600", 1L, 2L, 10, 100),
                "/lines", 201).extract().jsonPath().getLong("id");

        //when & then
        delete("/lines/" + id + "/sections?stationId=2", 404);
    }
}
