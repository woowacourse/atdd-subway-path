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
        LineResponse lineResponse1 = createLine("브라운선");
        LineResponse lineResponse2 = createLine("씨유선");
        StationResponse stationResponse1 = createStation("럿고역");
        StationResponse stationResponse2 = createStation("빙봉역");
        StationResponse stationResponse3 = createStation("기쁨역");
        StationResponse stationResponse4 = createStation("티거역");
        StationResponse stationResponse5 = createStation("앨런역");

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
