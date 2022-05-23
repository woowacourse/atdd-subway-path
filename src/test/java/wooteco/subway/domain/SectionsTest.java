package wooteco.subway.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.section.creationStrategy.ConcreteCreationStrategy;
import wooteco.subway.domain.section.deletionStrategy.ConcreteDeletionStrategy;
import wooteco.subway.domain.section.sortStrategy.ConcreteSortStrategy;
import wooteco.subway.domain.station.Station;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SectionsTest {

    private Sections sections;
    private Station 강남역;
    private Station 선릉역;
    private Station 잠실역;
    private Station 사당역;

    @BeforeEach
    void setSections() {
        강남역 = new Station("강남역");
        선릉역 = new Station("선릉역");
        잠실역 = new Station("잠실역");
        사당역 = new Station("사당역");
        List<Section> sectionList = new ArrayList<>();
        sectionList.add(new Section(1L, 1L, 강남역, 선릉역, 10));

        sections = new Sections(sectionList, new ConcreteCreationStrategy(), new ConcreteDeletionStrategy(), new ConcreteSortStrategy());
    }

    @Test
    @DisplayName("섹션을 성공적으로 생성")
    void saveSection() {
        Section section = new Section(2L, 1L, 선릉역, 잠실역, 10);
        sections.save(section);

        assertTrue(sections.getSortedStations().contains(잠실역));
    }

    @Test
    @DisplayName("상행역과 하행역이 모두 존재하는 경우에 대한 예외처리")
    void checkExistence() {
        Section section = new Section(2L, 1L, 강남역, 선릉역, 15);

        assertThatThrownBy(() -> sections.save(section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역이 이미 모두 존재합니다.");
    }

    @Test
    @DisplayName("노선에 상행역과 하행역이 모두 존재하지 않는 경우에 대한 예외처리")
    void checkConnected() {
        Section section = new Section(2L, 1L, 잠실역, 사당역, 15);

        assertThatThrownBy(() -> sections.save(section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("기존 노선과 연결된 구간이 아닙니다.");

    }

    @Test
    @DisplayName("갈래길의 길이가 기존 구간보다 긴 경우에 대한 예외처리")
    void checkDistance() {
        Section section = new Section(2L, 1L, 강남역, 잠실역, 15);

        assertThatThrownBy(() -> sections.save(section))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("적절한 거리가 아닙니다.");
    }

    @Test
    @DisplayName("갈래길이 발생하지 않으면 Optional.empty를 반환.")
    void overLappedSectionNotExist() {
        Section section = new Section(2L, 1L, 선릉역, 잠실역, 10);

        assertTrue(sections.getSectionOverLappedBy(section).isEmpty());
    }

    @Test
    @DisplayName("갈래길이 발생하면 수정된 기존 구간을 반환.")
    void fixOverLappedSection() {
        Section section = new Section(2L, 1L, 강남역, 잠실역, 10);

        assertTrue(sections.getSectionOverLappedBy(section).isPresent());
    }

    @Test
    @DisplayName("섹션 제거")
    void deleteSection() {
        Section section1 = new Section(2L, 1L, 선릉역, 잠실역, 10);
        Section section2 = new Section(3L, 1L, 잠실역, 사당역, 10);
        sections.save(section1);
        sections.save(section2);
        sections.delete(1L, 선릉역);

        assertFalse(sections.getSortedStations().contains(선릉역));
    }

    @Test
    @DisplayName("제거하려는 구간이 노선의 유일한 구간인 경우에 대한 예외처리")
    void checkDelete() {
        assertThatThrownBy(() -> sections.delete(1L, 선릉역))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("노선의 유일한 구간은 삭제할 수 없습니다.");
    }

    @Test
    @DisplayName("구간 제거로 인해 끊긴 구간이 없으면 Optional.empty를 반환")
    void disconnectedSectionNotExist() {
        Section section = new Section(2L, 1L, 선릉역, 잠실역, 10);
        sections.save(section);

        assertTrue(sections.fixDisconnectedSection(1L, 잠실역).isEmpty());
    }

    @Test
    @DisplayName("구간 제거로 인해 끊긴 구간이 있으면 수정된 구간을 반환")
    void fixDisconnectedSection() {
        Section section = new Section(2L, 1L, 선릉역, 잠실역, 10);
        sections.save(section);

        assertTrue(sections.fixDisconnectedSection(1L, 선릉역).isPresent());
    }

    @Test
    @DisplayName("역 정렬")
    void SortStations() {

        Section section = new Section(2L, 1L, 잠실역, 강남역, 5);
        sections.save(section);

        List<Station> sortedStations = sections.getSortedStations();

        assertThat(sortedStations.get(0).getName()).isEqualTo("잠실역");
        assertThat(sortedStations.get(1).getName()).isEqualTo("강남역");
        assertThat(sortedStations.get(2).getName()).isEqualTo("선릉역");
    }
}
