package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.*;
import static wooteco.subway.domain.fixture.SectionFixture.*;
import static wooteco.subway.domain.fixture.StationFixture.*;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.domain.fixture.LineFixture;
import wooteco.subway.domain.line.LineSeries;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.PathNotFoundException;

class PathFinderTest {

    @Test
    @DisplayName("올바른 경로를 찾는다.")
    public void findPath() {
        // given
        final LineSeries lineSeries = new LineSeries(List.of(LineFixture.getLineAbc(), LineFixture.getLineBd()));
        final PathFinder pathFinder = JgraphtPathFinder.of(lineSeries, getStationA(), getStationD());

        // when
        final List<Section> sections = pathFinder.getSections();
        // then
        Assertions.assertAll(
            () -> assertThat(sections).hasSize(2),
            () -> assertThat(sections).containsExactly(getSectionAb(), getSectionBd())
        );
    }

    @Test
    @DisplayName("최단 경로를 찾는다.")
    public void findShortedPath() {
        // given
        final LineSeries lineSeries = new LineSeries(List.of(LineFixture.getLineAbc(), LineFixture.getLineAc()));
        final PathFinder pathFinder = JgraphtPathFinder.of(lineSeries, getStationA(), getStationC());
        // when
        final List<Section> sections = pathFinder.getSections();
        // then
        Assertions.assertAll(
            () -> assertThat(sections).hasSize(1),
            () -> assertThat(sections).containsExactly(getSectionAc())
        );
    }

    @Test
    @DisplayName("역이 구간에 등록되지 않아서 경로가 없는 경우 예외를 던진다.")
    public void throwsExceptionWithPathNotFoundOfStationNotEnrolled() {
        // given & when
        final LineSeries lineSeries = new LineSeries(List.of(LineFixture.getLineAbc(), LineFixture.getLineBd()));

        // then
        assertThatExceptionOfType(PathNotFoundException.class)
            .isThrownBy(() -> JgraphtPathFinder.of(lineSeries, getStationA(), getStationX()));
    }

    @Test
    @DisplayName("구간이 겹치지 않아서 경로가 없는 경우 예외를 던진다.")
    public void throwsExceptionWithPathNotFoundOfSeperatedLine() {
        // given & when
        final LineSeries lineSeries = new LineSeries(List.of(LineFixture.getLineAbc(), LineFixture.getLineXy()));

        // then
        assertThatExceptionOfType(PathNotFoundException.class)
            .isThrownBy(() -> JgraphtPathFinder.of(lineSeries, getStationA(), getStationX()));
    }

    @Test
    @DisplayName("경로에 대한 길이를 구한다.")
    public void findDistance() {
        // given
        final LineSeries lineSeries = new LineSeries(List.of(LineFixture.getLineAbc(), LineFixture.getLineBd()));
        final PathFinder pathFinder = JgraphtPathFinder.of(lineSeries, getStationA(), getStationD());

        // when
        final int distance = pathFinder.getDistance().getValue();

        // then
        assertThat(distance).isEqualTo(17L);
    }
}