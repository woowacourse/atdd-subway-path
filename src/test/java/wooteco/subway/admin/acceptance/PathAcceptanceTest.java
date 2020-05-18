package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.admin.dto.PathResponse;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/truncate.sql")
public class PathAcceptanceTest extends AcceptanceTest {
    /*
     * Scenario: 지하철 경로 조회를 한다.
     *      Given 지하철역이 여러 개 추가되어있다.
     *      And 지하철 노선이 추가되어있다.
     *      And 지하철 경로가 여러 개 추가되어있다.
     *
     *      When 출발역과 도착역을 입력하여 최단 경로를 조회하는 요청을 한다.
     *      Then 최단 거리 기준으로 경로와 기타 정보를 응답한다.
     *      And 소요시간과 거리등의 정보를 포함한다.
     *      And 최단 경로는 하나만 응답한다.
     *
     *      When 같은 출발역과 도착역을 입력하여 최단 경로를 조회하는 요청을 한다.
     *      Then 이름이 중복된다는 예외를 발생시킨다.
     *
     *      When 존재하지 않는 역을 입력하여 최단 경로를 조회하는 요청을 한다.
     *      Then 역이 존재하지 않는다는 예외를 발생시킨다.
     *
     *      When 연결되어 있지 않은 출발역과 도착역으로 최단 경로를 조회하는 요청을 한다.
     *      Then 경로를 찾을 수 없다는 예외를 발생시킨다.
     *
     *      When 건대입구와 강남구청의 최단 경로를 조회하는 요청을 한다.
     *      Then 최소 거리 기준 경로의 응답을 받는다.
     *      And 최단 시간 기준 경로의 응답을 받는다.
     */

    @DisplayName("지하철 최단 경로 조회")
    @Test
    void getPath() {
        // given: 지하철역, 지하철 노선, 지하철 경로 추가
        setSubwayInformation();
        // when: 출발역과 도착역을 입력하여 최단 경로를 조회 요청
        PathResponse path = findPath("왕십리", "강남구청", "DISTANCE");

        assertThat(path.getStations().size()).isEqualTo(4);
        assertThat(path.getStations().get(1).getName()).isEqualTo("서울숲");
        assertThat(path.getStations().get(2).getName()).isEqualTo("압구정로데오");
        assertThat(path.getStations().get(3).getName()).isEqualTo("강남구청");

        //then
        assertThat(path.getDuration()).isEqualTo(3);
        assertThat(path.getDistance()).isEqualTo(9);

        //when
        String duplicationErrorMessage = findPathError("왕십리", "왕십리").getMessage();

        //then
        assertThat(duplicationErrorMessage).isEqualTo("출발역과 도착역은 동일할 수 없습니다.");

        //when
        String noExistErrorMessage = findPathError("작은곰", "오렌지").getMessage();

        //then
        assertThat(noExistErrorMessage).isEqualTo("존재하지 않는 역은 입력할 수 없습니다.");

        //when
        String noPathErrorMessage = findPathError("왕십리", "잠실").getMessage();

        //then
        assertThat(noPathErrorMessage).isEqualTo("경로를 찾을 수 없습니다. 노선도를 확인해주세요.");

        //when
        PathResponse minimumDistancePath = findPath("건대입구", "강남구청", "DISTANCE");
        PathResponse minimumDurationPath = findPath("건대입구", "강남구청", "DURATION");

        //then
        assertThat(minimumDistancePath.getDuration()).isEqualTo(12);
        assertThat(minimumDistancePath.getDistance()).isEqualTo(21);

        assertThat(minimumDurationPath.getDuration()).isEqualTo(11);
        assertThat(minimumDurationPath.getDistance()).isEqualTo(29);
    }

    private Exception findPathError(String source, String target) {
        return given().
                when().
                get("/paths?source=" + source + "&target=" + target + "&type=DISTANCE").
                then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value()).
                extract().as(IllegalArgumentException.class);
    }

    private PathResponse findPath(String source, String target, String type) {
        return given().
                when().
                get("/paths?source=" + source + "&target=" + target + "&type=" + type).
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(PathResponse.class);
    }
}
