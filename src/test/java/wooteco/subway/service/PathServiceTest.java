package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.exception.NotFoundException;

@SuppressWarnings("NonAsciiCharacters")
class PathServiceTest extends ServiceTest {

    @Autowired
    private PathService service;

    @DisplayName("findShortestPath 메서드는 최단 경로를 조회한다")
    @Nested
    class SaveTest {

        private final StationResponse STATION_RESPONSE1 = new StationResponse(1L, "강남역");
        private final StationResponse STATION_RESPONSE2 = new StationResponse(2L, "선릉역");
        private final StationResponse STATION_RESPONSE3 = new StationResponse(3L, "잠실역");

        @BeforeEach
        void setupStations() {
            testFixtureManager.saveStations("강남역", "선릉역", "잠실역");
            testFixtureManager.saveLine("노선", "색깔");
        }

        @Test
        void 최단경로에_대해_지하철역들의_목록과_거리_및_요금_정보를_반환() {
            testFixtureManager.saveSection(1L, 1L, 2L, 5);
            testFixtureManager.saveSection(1L, 2L, 3L, 5);

            PathResponse actual = service.findShortestPath(1L, 3L);
            PathResponse expected = new PathResponse(
                    List.of(STATION_RESPONSE1, STATION_RESPONSE2, STATION_RESPONSE3), 10, 1250);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 존재하지_않는_지하철역을_입력한_경우_예외발생() {
            testFixtureManager.saveSection(1L, 1L, 2L, 5);
            testFixtureManager.saveSection(1L, 2L, 3L, 5);

            assertThatThrownBy(() -> service.findShortestPath(1L, 9999999999L))
                    .isInstanceOf(NotFoundException.class);
        }
    }
}
