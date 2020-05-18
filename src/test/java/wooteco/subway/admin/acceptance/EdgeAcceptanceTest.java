package wooteco.subway.admin.acceptance;

import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.dto.WholeSubwayResponse;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class EdgeAcceptanceTest extends AcceptanceTest {

    @DisplayName("전체 지하철 노선도 정보 조회")
    @TestFactory
    Stream<DynamicTest> getAllEdge() {
        return Stream.of(
                dynamicTest("노선, 역, 구간 추가", () -> {
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
                }),
                dynamicTest("전체 노선 조회", () -> {
                    WholeSubwayResponse wholeSubwayResponse = given().
                            contentType(ContentType.JSON).
                            accept(MediaType.APPLICATION_JSON_VALUE).
                            when().
                            get("/lines/map").
                            then().
                            log().all().
                            statusCode(HttpStatus.OK.value()).
                            extract().as(WholeSubwayResponse.class);

                    LineDetailResponse expectedTwo = wholeSubwayResponse.getResponses().get(0);
                    assertThat(expectedTwo.getStations()).hasSize(2);

                    LineDetailResponse expectedEight = wholeSubwayResponse.getResponses().get(1);
                    assertThat(expectedEight.getStations()).hasSize(2);
                })
        );

    }

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @TestFactory
    Stream<DynamicTest> manageLineStation() {
        //given
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선");

        return Stream.of(
                dynamicTest("구간 추가", () -> {
                    addEdge(lineResponse.getId(), null, stationResponse1.getId());
                    addEdge(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
                    addEdge(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

                    LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
                    assertThat(lineDetailResponse.getStations()).hasSize(3);
                }),
                dynamicTest("구간 제거", () -> {
                    removeLineStation(lineResponse.getId(), stationResponse2.getId());

                    LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
                    assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
                })
        );
    }
}
