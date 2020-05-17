package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.LineDetailResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

public class WholeLineStationAcceptanceTest extends AcceptanceTest {

    @DisplayName("전체 노선별 지하철 역 전체 조회")
    @Test
    void showWholeLineStation() {
        createLine("브라운선");
        createLine("씨유선");
        createStation("럿고역");
        createStation("빙봉역");
        createStation("기쁨역");
        createStation("티거역");
        createStation("앨런역");

        List<LineResponse> lines = getLines();
        assertThat(lines.size()).isEqualTo(2);

        LineDetailResponse lineResponse1 = getLine(lines.get(0).getId());
        LineDetailResponse lineResponse2 = getLine(lines.get(1).getId());

        List<StationResponse> stations = getStations();

        assertThat(stations.size()).isEqualTo(5);

        StationResponse stationResponse1 = stations.get(0);
        StationResponse stationResponse2 = stations.get(1);
        StationResponse stationResponse3 = stations.get(2);
        StationResponse stationResponse4 = stations.get(3);
        StationResponse stationResponse5 = stations.get(4);

        addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());

        addLineStation(lineResponse2.getId(), null, stationResponse3.getId());
        addLineStation(lineResponse2.getId(), stationResponse3.getId(), stationResponse4.getId());
        addLineStation(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId());

        List<LineDetailResponse> lineDetailResponses = getLineDetails();

        assertThat(lineDetailResponses.size()).isEqualTo(2);
        assertThat(lineDetailResponses.get(0).getStations().size()).isEqualTo(3);
        assertThat(lineDetailResponses.get(1).getStations().size()).isEqualTo(3);
    }
}
