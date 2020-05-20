package wooteco.subway.admin.acceptance;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.admin.dto.ErrorResponse;
import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;

public class PathAcceptanceTest extends AcceptanceTest {

    @DisplayName("경로 조회 API")
    @Test
    void findPath() {
        //given
        StationResponse jamsil = createStation("잠실");
        StationResponse jamsilsaenae = createStation("잠실새내");
        StationResponse playgound = createStation("종합운동장");
        StationResponse samjun = createStation("삼전");
        StationResponse sukchongobun = createStation("석촌고분");
        StationResponse sukchon = createStation("석촌");

        LineResponse line2 = createLine("2호선");
        LineResponse line8 = createLine("8호선");
        LineResponse line9 = createLine("9호선");

        addLineStation(line2.getId(), null, jamsil.getId(), 0, 0);
        addLineStation(line2.getId(), jamsil.getId(), jamsilsaenae.getId(), 10, 1);
        addLineStation(line2.getId(), jamsilsaenae.getId(), playgound.getId(), 10, 1);

        addLineStation(line9.getId(), null, playgound.getId(), 0, 0);
        addLineStation(line9.getId(), playgound.getId(), samjun.getId(), 10, 1);
        addLineStation(line9.getId(), samjun.getId(), sukchongobun.getId(), 1, 10);
        addLineStation(line9.getId(), sukchongobun.getId(), sukchon.getId(), 1, 10);

        addLineStation(line8.getId(), null, jamsil.getId(), 0, 0);
        addLineStation(line8.getId(), jamsil.getId(), sukchon.getId(), 1, 10);

        //when
        PathResponse pathByDistance = findPath(jamsil.getName(), samjun.getName(), "distance");

        //then
        assertThat(pathByDistance.getStations()).hasSize(4);
        assertThat(pathByDistance.getStations()).extracting(StationResponse::getName)
            .containsExactly("잠실", "석촌", "석촌고분", "삼전");
        assertThat(pathByDistance.getTotalDistance()).isEqualTo(3);
        assertThat(pathByDistance.getTotalDuration()).isEqualTo(30);

        //when
        PathResponse pathByDuration = findPath(jamsil.getName(), samjun.getName(), "duration");

        //then
        assertThat(pathByDuration.getStations()).hasSize(4);
        assertThat(pathByDuration.getStations()).extracting(StationResponse::getName)
            .containsExactly("잠실", "잠실새내", "종합운동장", "삼전");
        assertThat(pathByDuration.getTotalDistance()).isEqualTo(30);
        assertThat(pathByDuration.getTotalDuration()).isEqualTo(3);

        //when
        assertThat(findPathByWrongType(jamsil.getName(), samjun.getName(), "transfer"))
            .isInstanceOf(ErrorResponse.class).extracting(ErrorResponse::getErrorMessage)
            .isEqualTo("transfer방식의 경로는 지원하지 않습니다.");
    }
}
