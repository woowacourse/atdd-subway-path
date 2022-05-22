package wooteco.subway.domain.path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.station.Station;
import wooteco.subway.exception.PathNotFoundException;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static wooteco.subway.domain.fixture.SectionFixture.*;
import static wooteco.subway.domain.fixture.StationFixture.*;

class PathTest {

    @Test
    @DisplayName("최단경로를 구한다.")
    void findPath() {
        //given
        Path path = Path.of(
                List.of(
                        new Line(1L, "1호선", "파란색", 0, List.of(getSectionAb())),
                        new Line(2L, "2호선", "초록색", 0, List.of(getSectionBc())),
                        new Line(3L, "3호선", "분홍색", 0, List.of(getSectionAc()))
                ),
                getStationA(),
                getStationC()
        );

        //when
        List<Station> shortestPath = path.findShortestPath();
        int distance = path.findDistance();

        //then
        Assertions.assertAll(
                () -> assertThat(shortestPath).hasSize(2),
                () -> assertThat(shortestPath).containsExactly(getStationA(), getStationC()),
                () -> assertThat(distance).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("최단 경로에 사용된 노선 아이디를 전부 조회한다.")
    void findUsedLineId() {
        //given
        Path path = Path.of(
                List.of(
                        new Line(1L, "1호선", "파란색", 0, List.of(getSectionAb())),
                        new Line(2L, "2호선", "초록색", 0, List.of(getSectionBc())),
                        new Line(3L, "3호선", "분홍색", 0, List.of(getSectionAc()))
                ),
                getStationA(),
                getStationC()
        );

        //when
        Set<Long> usedLineId = path.findUsedLineId();

        //then
        assertThat(usedLineId).containsExactly(3L);
    }

    @Test
    @DisplayName("출발지와 도착지가 동일한 경우 예외가 발생한다.")
    void throwsExceptionWhenDistinctStation() {
        assertThatExceptionOfType(PathNotFoundException.class)
                .isThrownBy(() -> Path.of(
                        List.of(new Line(1L, "1호선", "파란색", 0, List.of(getSectionAb()))),
                        getStationA(),
                        getStationA()
                ))
                .withMessageContaining("출발지와 도착지는 동일할 수 없습니다.");
    }

    @Test
    @DisplayName("역이 구간에 등록되지 않아서 경로가 없는 경우 예외를 던진다.")
    public void throwsExceptionWithPathNotFoundOfStationNotEnrolled() {
        assertThatExceptionOfType(PathNotFoundException.class)
                .isThrownBy(() -> Path.of(
                        List.of(new Line(1L, "1호선", "파란색", 0, List.of(getSectionAb()))),
                        getStationA(),
                        getStationC()
                ))
                .withMessageContaining("노선에 등록되지 않은 역의 경로는 조회할 수 없습니다.");
    }

    @Test
    @DisplayName("구간이 겹치지 않아서 경로가 없는 경우 예외를 던진다.")
    public void throwsExceptionWithPathNotFoundOfSeperatedLine() {
        assertThatExceptionOfType(PathNotFoundException.class)
                .isThrownBy(() -> Path.of(
                        List.of(
                                new Line(1L, "1호선", "파란색", 0, List.of(getSectionAb())),
                                new Line(2L, "2호선", "초록색", 0, List.of(getSectionXy()))
                                ),
                        getStationA(),
                        getStationY()
                ))
                .withMessageContaining("경로가 존재하지 않습니다.");
    }
}
