package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.Test;
import wooteco.subway.admin.dto.LineWithStationsResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class WholeSubwayLineAcceptanceTest extends AcceptanceTest {
    @Test
    void wholeSubway() {

        //given
        LineResponse lineResponse1 = createLine("1호선");
        StationResponse stationResponse1 = createStation("강남역");
        StationResponse stationResponse2 = createStation("역삼역");
        StationResponse stationResponse3 = createStation("삼성역");
        addEdge(lineResponse1.getId(), null, stationResponse1.getId());
        addEdge(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
        addEdge(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());
        //and
        LineResponse lineResponse2 = createLine("2호선");
        StationResponse stationResponse4 = createStation("잠실역");
        StationResponse stationResponse5 = createStation("양재역");
        StationResponse stationResponse6 = createStation("양재시민의숲역");
        addEdge(lineResponse2.getId(), null, stationResponse4.getId());
        addEdge(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId());
        addEdge(lineResponse2.getId(), stationResponse5.getId(), stationResponse6.getId());

        //when
        List<LineWithStationsResponse> responses = retrieveWholeSubway().getLineWithStationsResponse();

        //then
        assertThat(responses.size()).isEqualTo(2);
        assertThat(responses.get(0).getStations().size()).isEqualTo(3);
        assertThat(responses.get(1).getStations().size()).isEqualTo(3);

    }
}
