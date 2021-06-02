package wooteco.subway.path.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.application.ValidationFailureException;
import wooteco.subway.line.domain.Line;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.station.domain.Station;

class PathEdgeTest {

    private Station stationA, stationB, stationC;
    private Section sectionAB, sectionBC;
    private Line line;

    @BeforeEach
    void setUp() {
        stationA = new Station(1L, "stationA");
        stationB = new Station(2L, "stationB");
        stationC = new Station(3L, "stationC");
        sectionAB = new Section(1L, stationA, stationB, 5);
        sectionBC = new Section(2L, stationB, stationC, 3);
        Sections sections = new Sections(Arrays.asList(sectionAB, sectionBC));
        line = new Line(1L, "line", "green lighten-1", sections);
    }

    @DisplayName("생성에 성공한다.")
    @Test
    void creationSuccessful() {
        // when
        PathEdge pathEdge = new PathEdge(sectionAB, line);

        // then
        assertAll(
            () -> assertThat(pathEdge).extracting("section")
                .isEqualTo(sectionAB),

            () -> assertThat(pathEdge).extracting("lineName")
                .isEqualTo(line.getName())
        );
    }

    @DisplayName("노선에 구간이 없으면 생성에 실패한다.")
    @Test
    void creationFailed() {
        // given
        Section section = new Section(3L, stationA, stationC, 7);

        // when, then
        assertThatThrownBy(() -> new PathEdge(section, line))
            .isInstanceOf(ValidationFailureException.class)
            .hasMessageContaining("노선에 해당 구간이 없습니다");
    }
}