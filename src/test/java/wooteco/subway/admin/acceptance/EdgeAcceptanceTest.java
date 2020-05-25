package wooteco.subway.admin.acceptance;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class EdgeAcceptanceTest extends AcceptanceTest {

    @DisplayName("전체 지하철 노선도 정보 조회")
    @Test
    void getAllEdge() {
        //given
        LineResponse lineNumberTwo = createLine("2호선");
        LineResponse lineNumberEight = createLine("8호선");

        StationResponse hongik = createStation("홍대입구역");
        StationResponse shinchon = createStation("신촌역");
        StationResponse jamsil = createStation("잠실역");
        StationResponse seokchon = createStation("석촌역");
        StationResponse mongchon = createStation("몽촌토성역");

        addEdge(lineNumberTwo.getId(), hongik.getId(), shinchon.getId(), 10, 10);
        addEdge(lineNumberTwo.getId(), shinchon.getId(), jamsil.getId(), 10, 10);
        addEdge(lineNumberEight.getId(), jamsil.getId(), seokchon.getId(), 10, 10);
        addEdge(lineNumberEight.getId(), seokchon.getId(), mongchon.getId(), 10, 10);

        //when
        WholeSubwayResponse wholeSubwayResponse = given().
                contentType(ContentType.JSON).
                accept(MediaType.APPLICATION_JSON_VALUE).
                when().
                get("/lines/map").
                then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                extract().as(WholeSubwayResponse.class);

        //then
        LineDetailResponse expectedTwo = wholeSubwayResponse.getResponses().get(0);
        assertThat(expectedTwo.getId()).isEqualTo(lineNumberTwo.getId());
        assertThat(expectedTwo.getStations()).hasSize(2);

        LineDetailResponse expectedEight = wholeSubwayResponse.getResponses().get(1);
        assertThat(expectedEight.getId()).isEqualTo(lineNumberEight.getId());
        assertThat(expectedEight.getStations()).hasSize(2);
    }

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        StationResponse kangnam = createStation(STATION_NAME_KANGNAM);
        StationResponse yeoksam = createStation(STATION_NAME_YEOKSAM);
        StationResponse seolleung = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineNumberTwo = createLine("2호선");

        addEdge(lineNumberTwo.getId(), null, kangnam.getId(), 10, 10);
        addEdge(lineNumberTwo.getId(), kangnam.getId(), yeoksam.getId(), 10, 10);
        addEdge(lineNumberTwo.getId(), yeoksam.getId(), seolleung.getId(), 10, 10);

        LineDetailResponse lineDetailResponse = getLine(lineNumberTwo.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeEdge(lineNumberTwo.getId(), yeoksam.getId());

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineNumberTwo.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }

    StationResponse createStation(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return post(
                "/stations",
                params,
                StationResponse.class
        );
    }

    LineDetailResponse getLine(Long id) {
        return get("/lines/" + id).as(LineDetailResponse.class);
    }

    LineResponse createLine(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("startTime", LocalTime.of(5, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("endTime", LocalTime.of(23, 30).format(DateTimeFormatter.ISO_LOCAL_TIME));
        params.put("intervalTime", "10");

        return post(
                "/lines",
                params,
                LineResponse.class
        );
    }

    void addEdge(Long lineId, Long preStationId, Long stationId, Integer distance,
            Integer duration) {
        Map<String, String> params = new HashMap<>();
        params.put("preStationId", preStationId == null ? "" : preStationId.toString());
        params.put("stationId", stationId.toString());
        params.put("distance", distance.toString());
        params.put("duration", duration.toString());

        post(
                "/lines/" + lineId + "/stations",
                params,
                LineResponse.class
        );
    }

    void removeEdge(Long lineId, Long stationId) {
        delete("/lines/" + lineId + "/stations/" + stationId);
    }
}
