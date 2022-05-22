package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PathTest {

    @Test
    @DisplayName("경로를 조회한다.")
    void findPath() {
        //given
        List<Long> stationIds = List.of(1L, 2L, 3L, 4L, 5L);
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(1L, 1L, 1L, 2L, 10));
        sections.add(new Section(1L, 1L, 1L, 3L, 10));
        sections.add(new Section(1L, 1L, 3L, 5L, 10));
        sections.add(new Section(1L, 1L, 4L, 5L, 10));
        Path path = Path.of(1L, 5L, stationIds, new Sections(sections));
        //when
        List<Long> actualPath = path.getShortestPath();
        int actualTotalDistance = path.getTotalDistance();
        //then
        assertAll(
                () -> assertThat(actualPath).containsExactly(1L, 3L, 5L),
                () -> assertThat(actualTotalDistance).isEqualTo(20)
        );
    }

    @Test
    @DisplayName("중복된 경로가 있다면 가중치가 낮은 거리가 선택된다")
    void FindPathWithDuplicatedNodes() {
        //given
        List<Long> stationIds = List.of(1L, 2L, 3L, 4L, 5L);
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(1L, 1L, 1L, 2L, 10));
        sections.add(new Section(1L, 1L, 1L, 3L, 10));
        sections.add(new Section(1L, 1L, 1L, 3L, 5));
        sections.add(new Section(1L, 1L, 3L, 5L, 10));
        sections.add(new Section(1L, 1L, 4L, 5L, 10));
        Path path = Path.of(1L, 5L, stationIds, new Sections(sections));
        //when
        List<Long> actualPath = path.getShortestPath();
        int actualTotalDistance = path.getTotalDistance();
        //then
        assertAll(
                () -> assertThat(actualPath).containsExactly(1L, 3L, 5L),
                () -> assertThat(actualTotalDistance).isEqualTo(15)
        );
    }

    @Test
    @DisplayName("없는 출발 역 또는 도착 역 Id 를 입력받으면 예외를 반환한다.")
    void FindPathWithNotExistsStationId() {
        //given
        List<Long> stationIds = List.of(1L, 2L, 3L, 4L, 5L);
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(1L, 1L, 1L, 2L, 10));
        //when
        //then
        assertThatThrownBy(() -> Path.of(1L, -1L, stationIds, new Sections(sections)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 역을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("경로를 찾을 수 없으면 예외을 반환한다.")
    void FindPathWithNotExistsPath() {
        //given
        List<Long> stationIds = List.of(1L, 2L, 3L, 4L);
        List<Section> sections = new ArrayList<>();
        sections.add(new Section(1L, 1L, 1L, 2L, 10));
        sections.add(new Section(1L, 1L, 3L, 4L, 10));
        //when
        //then
        assertThatThrownBy(() -> Path.of(1L, 4L, stationIds, new Sections(sections)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 경로를 찾을 수 없습니다");
    }
}
