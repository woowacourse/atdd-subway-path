package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.CalculatePathsException;

class SubwayGraphTest {

    @DisplayName("최단 경로와 경로에 따른 거리를 구한다.")
    @Test
    void calculateShortestPath() {
        Station Jamsil = new Station(1L, "잠실역");
        Station Seolleung = new Station(2L, "선릉역");
        Station Gangnam = new Station(3L, "강남역");
        Station Konkuk = new Station(4L, "건대역");
        Station Sagajeong = new Station(5L, "사가정역");

        Section first = new Section(1L, 1L, Jamsil, Seolleung, 3);
        Section second = new Section(2L, 2L, Seolleung, Gangnam, 3);
        Section third = new Section(3L, 2L, Gangnam, Konkuk, 4);
        Section fourth = new Section(4L, 3L, Konkuk, Sagajeong, 5);
        Section fifth = new Section(5L, 4L, Sagajeong, Jamsil, 7);

        Sections sections = new Sections(List.of(first, second, third, fourth, fifth));

        SubwayGraph subwayGraph = new SubwayGraph(sections);
        Paths paths = subwayGraph.createPathsResult(Jamsil, Konkuk);

        assertThat(paths.getStations()).isEqualTo(List.of(Jamsil, Seolleung, Gangnam, Konkuk));
        assertThat(paths.getDistance()).isEqualTo(10);
    }

    @DisplayName("노선에 없는 출발역 또는 도착역에 대해 최단경로를 구할 때, 예외를 발생한다.")
    @Test()
    void pathNotFoundException_target() {
        Station Jamsil = new Station(1L, "잠실역");
        Station Seolleung = new Station(2L, "선릉역");
        Station Gangnam = new Station(3L, "강남역");
        Station Konkuk = new Station(4L, "건대역");
        Station Sagajeong = new Station(5L, "사가정역");
        Station Samsung = new Station(6L, "삼성역");
        Station Isu = new Station(7L, "이수역");

        Section first = new Section(1L, 1L, Jamsil, Seolleung, 3);
        Section second = new Section(2L, 2L, Seolleung, Gangnam, 3);
        Section third = new Section(3L, 2L, Gangnam, Konkuk, 4);
        Section fourth = new Section(4L, 3L, Konkuk, Sagajeong, 5);
        Section fifth = new Section(5L, 4L, Sagajeong, Jamsil, 7);

        Sections sections = new Sections(List.of(first, second, third, fourth, fifth));

        SubwayGraph subwayGraph = new SubwayGraph(sections);

        assertThatThrownBy(() -> subwayGraph.createPathsResult(Jamsil, Samsung))
                .isInstanceOf(CalculatePathsException.class)
                .hasMessage("출발역과 도착역 중, 노선에 등록되지 않은 역이 있습니다.");

        assertThatThrownBy(() -> subwayGraph.createPathsResult(Isu, Konkuk))
                .isInstanceOf(CalculatePathsException.class)
                .hasMessage("출발역과 도착역 중, 노선에 등록되지 않은 역이 있습니다.");
    }
}
