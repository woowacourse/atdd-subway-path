package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.domain.fixture.StationFixture.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.fixture.LineFixture;
import wooteco.subway.domain.fixture.SectionFixture;
import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.PathNotFoundException;

class PathFinderTest {

    @Test
    @DisplayName("올바른 경로를 찾는다.")
    public void findPath() {
        // given
        final List<Section> sections = List.of(SectionFixture.getSectionAb(), SectionFixture.getSectionBc());
        final PathFinder pathFinder = PathFinder.of(sections, getStationA(), getStationC());

        // when
        final List<Station> shortestPath = pathFinder.findShortestPath();

        // then
        Assertions.assertAll(
                () -> assertThat(shortestPath).hasSize(3),
                () -> assertThat(shortestPath).containsExactly(getStationA(), getStationB(), getStationC())
        );
    }

    @Test
    @DisplayName("최단 경로를 찾는다.")
    public void findShortedPath() {
        // given
        final List<Section> sections = List.of(SectionFixture.getSectionAb(), SectionFixture.getSectionBc(), SectionFixture.getSectionAc());
        final PathFinder pathFinder = PathFinder.of(sections, getStationA(), getStationC());

        // when
        final List<Station> shortestPath = pathFinder.findShortestPath();
        final int distance = pathFinder.findDistance();

        // then
        Assertions.assertAll(
                () -> assertThat(shortestPath).hasSize(2),
                () -> assertThat(shortestPath).containsExactly(getStationA(), getStationC()),
                () -> assertThat(distance).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("역이 구간에 등록되지 않아서 경로가 없는 경우 예외를 던진다.")
    public void throwsExceptionWithPathNotFoundOfStationNotEnrolled() {
        // given
        final List<Section> sections = List.of(SectionFixture.getSectionAb());

        // when & then
        assertThatExceptionOfType(PathNotFoundException.class)
                .isThrownBy(() -> PathFinder.of(sections, getStationA(), getStationC()));
    }

    @Test
    @DisplayName("구간이 겹치지 않아서 경로가 없는 경우 예외를 던진다.")
    public void throwsExceptionWithPathNotFoundOfSeperatedLine() {
        // given
        final List<Section> sections = List.of(SectionFixture.getSectionAb(), SectionFixture.getSectionXy());

        // when & then
        assertThatExceptionOfType(PathNotFoundException.class)
                .isThrownBy(() -> PathFinder.of(sections, getStationA(), getStationX()));
    }
}
