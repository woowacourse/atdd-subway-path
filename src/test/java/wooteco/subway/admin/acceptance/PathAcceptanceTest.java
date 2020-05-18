package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.vo.PathType;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.PathResponse;

/*
Feature: 지하철 경로 조회
    Scenario: 지하철 경로를 조회한다.
        Given: 지하철 노선이 등록되어 있다.
        And: 지하철 역이 등록되어 있다.
        And: 지하철 노선에 역이 등록되어 있다.

        When: 출발역과 도착역을 입력한다.
        Then: 최단거리 경로와 총 소요시간, 총 거리를 응답한다.
*/
public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 경로를 조회한다.")
    @Test
    void findPathTest() {
        //Given: 지하철 노선이 등록되어 있다.
        LineResponse secondLine = createLine(LINE_NAME_2);
        LineResponse thirdLine = createLine(LINE_NAME_3);
        //And: 지하철 역이 등록되어 있다.
        Long jamsil = createStation(STATION_NAME_JAMSIL);
        Long seolleung = createStation(STATION_NAME_SEOLLEUNG);
        Long yeoksam = createStation(STATION_NAME_YEOKSAM);
        Long kangnam = createStation(STATION_NAME_KANGNAM);
        Long gyodae = createStation(STATION_NAME_GYODAE);
        Long jamwon = createStation(STATION_NAME_JAMWON);
        Long sinsa = createStation(STATION_NAME_SINSA);
        //And: 지하철 노선에 역이 등록되어 있다.
        addLineStation(secondLine.getId(), null, jamsil);
        addLineStation(secondLine.getId(), jamsil, seolleung);
        addLineStation(secondLine.getId(), seolleung, yeoksam);
        addLineStation(secondLine.getId(), yeoksam, kangnam);
        addLineStation(secondLine.getId(), kangnam, gyodae);
        addLineStation(thirdLine.getId(), null, gyodae);
        addLineStation(thirdLine.getId(), gyodae, jamwon);
        addLineStation(thirdLine.getId(), jamwon, sinsa);

        //When: 출발역과 도착역을 입력한다.
        PathResponse path = findPath(yeoksam, jamwon);
        //Then: 최단거리 경로와 총 소요시간, 총 거리를 응답한다.
        assertThat(path.getStations().get(0).getId()).isEqualTo(yeoksam);
        assertThat(path.getStations().get(1).getId()).isEqualTo(kangnam);
        assertThat(path.getStations().get(2).getId()).isEqualTo(gyodae);
        assertThat(path.getStations().get(3).getId()).isEqualTo(jamwon);
        assertThat(path.getDuration()).isEqualTo(30);
        assertThat(path.getDistance()).isEqualTo(30);
    }

    private PathResponse findPath(Long source, Long target) {
        return given()
            .queryParam("source", source)
            .queryParam("target", target)
            .queryParam("type", PathType.DISTANCE.name())
            .when()
            .get("/paths")
            .then()
            .log().all()
            .extract()
            .as(PathResponse.class);
    }
}
