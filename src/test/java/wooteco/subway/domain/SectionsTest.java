package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.service.dto.SectionServiceDeleteRequest;
import wooteco.subway.service.dto.SectionServiceRequest;

class SectionsTest {

    @Test
    @DisplayName("1 - 2 - 3의 구간에서 2 - 4의 구간이 중간 구간인지 판별한다.")
    void isMiddleSection() {
        // given
        Sections sections = createSections();
        Section section = new Section(2L, 4L);

        // when
        boolean result = sections.isMiddleSection(section);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void hasStationId() {
        // given
        Sections sections = createSections();

        // when
        boolean result = sections.hasStationId(1L);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void findSectionByUpStationId() {
        // given
        Sections sections = createSections();

        // when
        Section section = sections.findSectionByUpStationId(1L);
        Long upStationId = section.getUpStationId();

        // then
        assertThat(upStationId).isEqualTo(1L);
    }

    @Test
    void findSectionByDownStationId() {
        // given
        Sections sections = createSections();

        // when
        Section section = sections.findSectionByDownStationId(2L);
        Long upStationId = section.getDownStationId();

        // then
        assertThat(upStationId).isEqualTo(2L);
    }

    @Test
    void sortedStationId() {
        // given
        Sections sections = createSections();

        // when
        List<Long> stationIds = sections.sortedStationId();
        List<Long> result = List.of(1L, 2L, 3L);

        // then
        assertThat(stationIds).containsExactlyInAnyOrderElementsOf(result);
    }

    @Test
    @DisplayName("구간이 한개만 존재할 경우, false를 반환한다.")
    void isSingleSection() {
        // given
        List<Section> inputSections = List.of(new Section(1L, 2L));
        Sections sections = new Sections(inputSections);

        // when
        boolean result = sections.canRemoveSection();

        // then
        assertThat(result).isFalse();
    }

    @Test
    @DisplayName("구간이 한개만 비어있을 경우, false를 반환한다.")
    void isEmpty() {
        // given
        List<Section> inputSections = List.of();
        Sections sections = new Sections(inputSections);

        // when
        boolean result = sections.canRemoveSection();

        // then
        assertThat(result).isFalse();
    }

    Sections createSections() {
        List<Section> inputSections = List.of(new Section(1L, 2L), new Section(2L, 3L));
        return new Sections(inputSections);
    }

    @Test
    @DisplayName("상행점 구간을 저장한다. 정렬된 구간 : 2-3-4-5 -> 1-2-3-4-5")
    void saveFirstStation() {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(line, 2L, 3L, 3);
        Section section2 = new Section(line, 3L, 4L, 4);
        Section section3 = new Section(line, 4L, 5L, 5);
        List<Section> inputSections = new ArrayList<>();
        inputSections.add(section1);
        inputSections.add(section2);
        inputSections.add(section3);
        Sections sections = new Sections(inputSections);


        // when
        Section section4 = new Section(line, 1L, 2L, 2);
        List<Section> resultSections = sections.insert(section4);
        List<Long> stationIds = sections.sortedStationId();
        Long firstPointStationId = stationIds.get(0);

        Section firstSection = resultSections.stream()
            .filter(i -> i.mathUpStationId(firstPointStationId))
            .findAny()
            .get();

        // then
        assertAll(
            () -> assertThat(firstSection.getUpStationId()).isEqualTo(1L),
            () -> assertThat(firstSection.getDistance()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("하행점 구간을 저장한다. 정렬된 구간 : 1-2-3-4 -> 1-2-3-4-5")
    void saveLastStation() {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(line, 1L, 2L, 3);
        Section section2 = new Section(line, 2L, 3L, 4);
        Section section3 = new Section(line, 3L, 4L, 5);
        Section section4 = new Section(line, 4L, 5L, 2);
        List<Section> inputSections = new ArrayList<>();
        inputSections.add(section1);
        inputSections.add(section2);
        inputSections.add(section3);
        Sections sections = new Sections(inputSections);

        // when
        List<Section> resultSections = sections.insert(section4);
        List<Long> stationIds = sections.sortedStationId();
        Long lastPointStationId = stationIds.get(3);
        Section lastSection = resultSections.stream()
            .filter(i -> i.mathUpStationId(lastPointStationId))
            .findAny()
            .get();

        // then
        assertAll(
            () -> assertThat(lastSection.getDownStationId()).isEqualTo(5L),
            () -> assertThat(lastSection.getDistance()).isEqualTo(2)
        );
    }

    @Test
    @DisplayName("중간 지점 구간을 저장한다. 정렬된 구간 : 1-3구간에서 1-2 구간을 추가하는 경우")
    void saveMiddleStation1() {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(line, 1L, 3L, 3);
        List<Section> inputSections = new ArrayList<>();
        inputSections.add(section1);
        Sections sections = new Sections(inputSections);

        // when
        Section section2 = new Section(line, 1L, 2L, 2);
        List<Section> resultSections = sections.insert(section2);
        List<Long> stationIds = sections.sortedStationId();

        Section resultSection1 = resultSections.stream()
            .filter(i -> i.mathUpStationId(stationIds.get(0)))
            .findAny()
            .get();

        Section resultSection2 = resultSections.stream()
            .filter(i -> i.mathUpStationId(stationIds.get(1)))
            .findAny()
            .get();

        // then
        assertAll(
            () -> assertThat(resultSection1.getDistance()).isEqualTo(2),
            () -> assertThat(resultSection2.getDistance()).isEqualTo(1)
        );
    }

    @Test
    @DisplayName("중간 지점 구간을 저장한다. 정렬된 구간 : 1-3구간에서 2-3 구간을 추가하는 경우")
    void saveMiddleStation2() {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(line, 1L, 3L, 4);
        List<Section> inputSections = new ArrayList<>();
        inputSections.add(section1);
        Sections sections = new Sections(inputSections);

        // when
        Section section2 = new Section(line, 2L, 3L, 3);
        List<Section> resultSections = sections.insert(section2);

        List<Long> stationIds = sections.sortedStationId();

        Section resultSection1 = resultSections.stream()
            .filter(i -> i.mathUpStationId(stationIds.get(0)))
            .findAny()
            .get();

        Section resultSection2 = resultSections.stream()
            .filter(i -> i.mathUpStationId(stationIds.get(1)))
            .findAny()
            .get();

        // then
        assertAll(
            () -> assertThat(resultSection1.getDistance()).isEqualTo(1),
            () -> assertThat(resultSection2.getDistance()).isEqualTo(3)
        );
    }

    @Test
    @DisplayName("생성할 중간 지점 구간의 길이가 기존 구간의 길이보다 길거나 같은 경우 예외가 발생한다.")
    void validateMiddleStationDistance() {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(line, 2L, 3L, 3);
        Section section2 = new Section(line, 3L, 4L, 4);
        Section section3 = new Section(line, 4L, 5L, 5);
        Sections sections = new Sections(List.of(section1, section2, section3));

        Section inputSection = new Section(line, 2L, 1L, 4);
        assertThatThrownBy(() ->
            sections.insert(inputSection))
            .hasMessage("등록할 구간의 길이가 기존 역 사이의 길이보다 길거나 같으면 안됩니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("중간 지점 구간 제거한다. 1-2-3-4 -> 1-2-4")
    void deleteMiddleSection() {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(1L, line, 1L, 2L, 3);
        Section section2 = new Section(2L, line, 2L, 3L, 4);
        Section section3 = new Section(3L, line, 3L, 4L, 5);
        List<Section> inputSections = new ArrayList<>();
        inputSections.add(section1);
        inputSections.add(section2);
        inputSections.add(section3);
        Sections sections = new Sections(inputSections);

        // when
        Section removeSection = new Section(line, 3L, 3L, 0);
        List<Section> resultSections = sections.removeSection(removeSection);
        List<Long> result = sections.sortedStationId();

        Section section = resultSections.stream()
            .filter(i -> i.mathUpStationId(2L))
            .findAny()
            .get();

        // then
        List<Long> expected = List.of(1L, 2L, 4L);
        assertAll(
            () -> assertThat(result).containsExactlyInAnyOrderElementsOf(expected),
            () -> assertThat(section.getDistance()).isEqualTo(9)
        );
    }

    @Test
    @DisplayName("구간이 하나 밖에 없을 경우, 예외가 발생한다.")
    void validateDeleteEndStationSection() {
        // given
        Line line = new Line("name", "color", 0);
        Section section1 = new Section(line, 1L, 2L, 3);
        Sections sections = new Sections(List.of(section1));
        Section removeSection = new Section(line, 1L, 1L, 0);

        // then
        assertThatThrownBy(() ->
            sections.removeSection(removeSection))
            .hasMessage("구간을 제거할 수 없는 상태입니다.")
            .isInstanceOf(IllegalArgumentException.class);
    }
}
