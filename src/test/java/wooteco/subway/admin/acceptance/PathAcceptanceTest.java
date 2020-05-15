package wooteco.subway.admin.acceptance;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PathAcceptanceTest extends AcceptanceTest {
    @DisplayName("경로 검색을 실행하고 올바른 결과가 나오는지 테스트")
    @Test
    void findPath() {
        //given
        LineResponse lineResponse1 = createLine("1호선");
        StationResponse stationResponse1 = createStation("강남역");
        StationResponse stationResponse2 = createStation("역삼역");
        StationResponse stationResponse3 = createStation("선릉역");
        addLineStation(lineResponse1.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse1.getId(), stationResponse1.getId(), stationResponse2.getId());
        addLineStation(lineResponse1.getId(), stationResponse2.getId(), stationResponse3.getId());
        //and
        LineResponse lineResponse2 = createLine("2호선");
        StationResponse stationResponse4 = createStation("양재역");
        StationResponse stationResponse5 = createStation("양재시민의숲역");
        addLineStation(lineResponse2.getId(), null, stationResponse1.getId());
        addLineStation(lineResponse2.getId(), stationResponse1.getId(), stationResponse4.getId());
        addLineStation(lineResponse2.getId(), stationResponse4.getId(), stationResponse5.getId());
        //and
        LineResponse lineResponse3 = createLine("3호선");
        StationResponse stationResponse6 = createStation("신촌역");
        addLineStation(lineResponse3.getId(), null, stationResponse3.getId());
        addLineStation(lineResponse3.getId(), stationResponse3.getId(), stationResponse6.getId(), 1000, 1);
        addLineStation(lineResponse3.getId(), stationResponse6.getId(), stationResponse5.getId());

        //when
        PathResponse pathResponseByDistance = calculatePath("양재시민의숲역", "선릉역", PathType.DISTANCE);
        //and
        PathResponse pathResponseByDuration = calculatePath("양재시민의숲역", "선릉역", PathType.DURATION);

        //then
        assertThat(pathResponseByDistance.getDistance()).isEqualTo(40);
        assertThat(pathResponseByDistance.getDuration()).isEqualTo(40);
        //and
        assertThat(pathResponseByDuration.getDistance()).isEqualTo(1010);
        assertThat(pathResponseByDuration.getDuration()).isEqualTo(11);

        //and
        List<StationResponse> stations1 = pathResponseByDistance.getStations();
        assertThat(stations1.get(0).getName()).isEqualTo("양재시민의숲역");
        assertThat(stations1.get(1).getName()).isEqualTo("양재역");
        assertThat(stations1.get(2).getName()).isEqualTo("강남역");
        assertThat(stations1.get(3).getName()).isEqualTo("역삼역");
        assertThat(stations1.get(4).getName()).isEqualTo("선릉역");
        //and
        List<StationResponse> stations2 = pathResponseByDuration.getStations();
        assertThat(stations2.get(0).getName()).isEqualTo("양재시민의숲역");
        assertThat(stations2.get(1).getName()).isEqualTo("신촌역");
        assertThat(stations2.get(2).getName()).isEqualTo("선릉역");
    }
}
