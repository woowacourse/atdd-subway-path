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

        addEdge(lineNumberTwo.getId(), hongik.getId(), shinchon.getId());
        addEdge(lineNumberTwo.getId(), shinchon.getId(), jamsil.getId());
        addEdge(lineNumberEight.getId(), jamsil.getId(), seokchon.getId());
        addEdge(lineNumberEight.getId(), seokchon.getId(), mongchon.getId());

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

        addEdge(lineNumberTwo.getId(), null, kangnam.getId());
        addEdge(lineNumberTwo.getId(), kangnam.getId(), yeoksam.getId());
        addEdge(lineNumberTwo.getId(), yeoksam.getId(), seolleung.getId());

        LineDetailResponse lineDetailResponse = getLine(lineNumberTwo.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeEdge(lineNumberTwo.getId(), yeoksam.getId());

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineNumberTwo.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }

    void removeEdge(Long lineId, Long stationId) {
        delete("/lines/" + lineId + "/stations/" + stationId);
    }
}
