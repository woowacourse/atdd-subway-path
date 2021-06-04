package wooteco.subway.path.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

class PathTest {

    private List<Station> 이호선_역들;
    private List<PathEdge> 이호선_구간들;
    private Station 서초, 교대, 강남;
    private Section 서초_교대, 교대_강남;
    private Line 이호선;

    @BeforeEach
    void setUp() {
        initializeSubwayData();
        initializePathArguments();
    }

    @DisplayName("구간이 없는 경로는 생성에 실패한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void creationFailed(List<PathEdge> pathEdges) {
        // when, then
        assertThatThrownBy(() -> new Path(이호선_역들, pathEdges))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("Path 생성에 실패했습니다");
    }

    @DisplayName("구간에 속해있는 역 정보를 가져온다.")
    @Test
    void toStations() {
        // when
        List<Station> stations = new Path(이호선_역들, 이호선_구간들)
            .toStations();

        // then
        assertThat(stations).containsExactly(서초, 교대, 강남);
    }

    @DisplayName("구간의 거리를 가져온다.")
    @Test
    void toDistance() {
        // when
        int distance = new Path(이호선_역들, 이호선_구간들)
            .toDistance();

        // then
        assertThat(distance).isEqualTo(8);
    }

    private void initializeSubwayData() {
        서초 = new Station(1L, "서초역");
        교대 = new Station(2L, "교대역");
        강남 = new Station(3L, "강남역");
        서초_교대 = new Section(1L, 서초, 교대, 5);
        교대_강남 = new Section(2L, 교대, 강남, 3);
        이호선 = new Line(
            1L,
            "2호선",
            "green lighten-1",
            new Sections(Arrays.asList(서초_교대, 교대_강남))
        );
    }

    private void initializePathArguments() {
        이호선_역들 = Arrays.asList(서초, 교대, 강남);
        이호선_구간들 = Arrays.asList(
            new PathEdge(서초_교대, 이호선),
            new PathEdge(교대_강남, 이호선)
        );
    }
}