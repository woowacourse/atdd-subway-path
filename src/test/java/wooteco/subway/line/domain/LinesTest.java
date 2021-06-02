package wooteco.subway.line.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.station.domain.Station;

class LinesTest {

    private Station 서초, 교대, 강남, 고터;
    private Section 서초_교대, 교대_강남, 교대_고터, 고터_강남;
    private Lines lines;

    @BeforeEach
    void setUp() {
        서초 = new Station(1L, "서초역");
        교대 = new Station(2L, "교대역");
        강남 = new Station(3L, "강남역");
        고터 = new Station(4L, "고속터미널역");
        lines = new Lines(
            Arrays.asList(createLineTwo(), createLineThree())
        );
    }

    private Line createLineTwo() {
        서초_교대 = new Section(1L, 서초, 교대, 5);
        교대_강남 = new Section(2L, 교대, 강남, 3);

        return new Line(
            1L, "2호선", "green lighten-1",
            new Sections(Arrays.asList(서초_교대, 교대_강남))
        );
    }

    private Line createLineThree() {
        교대_고터 = new Section(3L, 교대, 고터, 1);
        고터_강남 = new Section(4L, 고터, 강남, 1);

        return new Line(
            2L, "3호선", "orange lighten-1",
            new Sections(Arrays.asList(교대_고터, 고터_강남))
        );
    }

    @DisplayName("전체 노선에서 모든 역을 무작위 순서로 가져온다.")
    @Test
    void toDistinctStations() {
        // when
        Set<Station> stations = lines.toDistinctStations();

        // then
        assertThat(stations).containsExactlyInAnyOrder(서초, 교대, 강남, 고터);
    }

    @DisplayName("전체 노선에서 모든 구간을 무작위 순서로 가져온다.")
    @Test
    void toAllSections() {
        // when
        Set<Section> sections = lines.toAllSections();

        // then
        assertThat(sections).containsExactlyInAnyOrder(서초_교대, 교대_강남, 교대_고터, 고터_강남);
    }

    @DisplayName("전체 노선에서 특정 구간을 포함하는 노선을 알아낸다.")
    @Test
    void findLineBySectionContaining() {
        // when
        Line line = lines.findLineBySectionContaining(서초_교대);

        // then
        assertThat(line).usingRecursiveComparison()
            .isEqualTo(createLineTwo());
    }
}
