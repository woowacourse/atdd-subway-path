package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.fixture.DatabaseUsageTest;

@SuppressWarnings("NonAsciiCharacters")
class PathServiceTest extends DatabaseUsageTest {

    private final Station 강남역 = new Station(1L, "강남역");
    private final Station 선릉역 = new Station(2L, "선릉역");
    private final Station 잠실역 = new Station(3L, "잠실역");
    private final Station 청계산입구역 = new Station(4L, "청계산입구역");

    @Autowired
    private PathService service;

    @DisplayName("findShortestPath 메서드는 최단 경로를 조회한다")
    @Nested
    class FindShortestPathTest {

        private final StationResponse 강남역_응답 = new StationResponse(1L, "강남역");
        private final StationResponse 선릉역_응답 = new StationResponse(2L, "선릉역");
        private final StationResponse 잠실역_응답 = new StationResponse(3L, "잠실역");

        @Test
        void 최단경로에_대해_지하철역들의_목록과_거리_및_요금_정보를_반환() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("노선", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 5);
            saveSectionTestFixture(1L, 선릉역, 잠실역, 5);

            PathResponse actual = service.findShortestPath(1L, 3L, 25);
            PathResponse expected = new PathResponse(
                    List.of(강남역_응답, 선릉역_응답, 잠실역_응답), 10, 1250);

            assertThat(actual).isEqualTo(expected);
        }

        @DisplayName("요금 계산은 기본요금 => 거리 추가비용 => 노선 추가비용 => 나이 할인 순으로 적용된다.")
        @Test
        void fareCalculation() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("노선", "색깔", 500);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);
            saveSectionTestFixture(1L, 선릉역, 잠실역, 8);

            int actual = service.findShortestPath(1L, 3L, 15).getFare();
            int expected = (int) ((((1250 + 200) + 500) - 350) * 0.8);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        void 존재하지_않는_지하철역을_입력한_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역);
            saveLineTestFixture("노선", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 5);
            saveSectionTestFixture(1L, 선릉역, 잠실역, 5);

            assertThatThrownBy(() -> service.findShortestPath(1L, 9999999999L, 10))
                    .isInstanceOf(NotFoundException.class);
        }

        @Test
        void 연결되지_않은_지하철역들_사이의_경로를_조회하려는_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역, 잠실역, 청계산입구역);
            saveLineTestFixture("노선", "색깔", 0);
            saveSectionTestFixture(1L, 강남역, 선릉역, 10);
            saveSectionTestFixture(1L, 잠실역, 청계산입구역, 10);

            assertThatThrownBy(() -> service.findShortestPath(1L, 3L, 10))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 구간에_등록되지_않은_지하철역이_입력된_경우_예외발생() {
            databaseFixtureUtils.saveStations(강남역, 선릉역);
            saveLineTestFixture("노선", "색깔", 0);

            assertThatThrownBy(() -> service.findShortestPath(1L, 2L, 10))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    private void saveLineTestFixture(String name, String color, int extraFare) {
        databaseFixtureUtils.saveLines(new LineEntity(name, color, extraFare));
    }

    private void saveSectionTestFixture(Long lineId, Station upStation, Station downStation, int distance) {
        databaseFixtureUtils.saveSections(new Section(lineId, upStation,downStation, distance));
    }
}
