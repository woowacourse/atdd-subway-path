package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.mapper.TypeRef;
import wooteco.subway.admin.dto.response.LineDetailResponse;
import wooteco.subway.admin.dto.response.LineResponse;
import wooteco.subway.admin.dto.response.StandardResponse;
import wooteco.subway.admin.dto.response.StationResponse;
import wooteco.subway.admin.dto.response.WholeSubwayResponse;

public class LineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("지하철 노선에서 지하철역 추가 / 제외")
    @Test
    void manageLineStation() {
        StationResponse stationResponse1 = createStation(STATION_NAME_KANGNAM);
        StationResponse stationResponse2 = createStation(STATION_NAME_YEOKSAM);
        StationResponse stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);

        LineResponse lineResponse = createLine("2호선").getData();

        addLineStation(lineResponse.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse.getId(), stationResponse2.getId(), stationResponse3.getId());

        LineDetailResponse lineDetailResponse = getLine(lineResponse.getId());
        assertThat(lineDetailResponse.getStations()).hasSize(3);

        removeLineStation(lineResponse.getId(), stationResponse2.getId());

        LineDetailResponse lineResponseAfterRemoveLineStation = getLine(lineResponse.getId());
        assertThat(lineResponseAfterRemoveLineStation.getStations().size()).isEqualTo(2);
    }

    @DisplayName("지하철 노선도 전체 정보 조회")
    @Test
    public void wholeSubway() {
        LineResponse lineResponse1 = createLine("2호선").getData();
        StationResponse stationResponse1 = createStation("강남역");
        StationResponse stationResponse2 = createStation("역삼역");
        StationResponse stationResponse3 = createStation("삼성역");
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());

        LineResponse lineResponse2 = createLine("신분당선").getData();
        StationResponse stationResponse4 = createStation("잠실역");
        StationResponse stationResponse5 = createStation("양재역");
        StationResponse stationResponse6 = createStation("양재시민의숲역");
        addLineStation(lineResponse2.getId(), null, stationResponse4.getId());
        addLineStation(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId());
        addLineStation(lineResponse2.getId(), stationResponse5.getId(), stationResponse6.getId());

        List<LineDetailResponse> responses = retrieveWholeSubway().getLineDetailResponse();
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStations().size()).isEqualTo(3);
        assertThat(responses.get(1).getStations().size()).isEqualTo(3);
    }

    private WholeSubwayResponse retrieveWholeSubway() {
        StandardResponse<WholeSubwayResponse> response = given().
            when().
            get("/lines/detail").
            then().
            extract().
            body().
            as(new TypeRef<StandardResponse<WholeSubwayResponse>>() {
            });
        return response.getData();
    }
}
