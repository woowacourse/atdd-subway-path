package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.InvalidSectionInsertException;

class LineSectionsTest {

    private Section section1;
    private Section section2;
    private Section section3;
    private LineSections lineSections;

    @BeforeEach
    void setUp() {
        section1 = new Section(1L, 1L, 1L, 2L, 10, 1L);
        section2 = new Section(2L, 1L, 2L, 3L, 10, 2L);
        section3 = new Section(3L, 1L, 3L, 4L, 10, 3L);
        lineSections = new LineSections(List.of(section1, section2, section3));
    }

    @Test
    @DisplayName("상행역 id와 하행역 id, 거리를 입력받아 구간이 추가될 수 있는 지 검사시에 이미 존재하는 두 역을 사용하면 예외가 발생")
    void validateSectionBothStationExist() {
        assertThatThrownBy(() -> lineSections.validateSection(1L, 3L, 5))
                .isInstanceOf(InvalidSectionInsertException.class)
                .hasMessageContaining("상행, 하행이 대상 노선에 둘 다 존재합니다.");
    }

    @Test
    @DisplayName("상행역 id와 하행역 id, 거리를 입력받아 구간이 추가될 수 있는 지 검사시에 존재하지 않는 두 역을 사용하면 예외가 발생")
    void validateSectionNoneStationExist() {
        assertThatThrownBy(() -> lineSections.validateSection(5L, 6L, 5))
                .isInstanceOf(InvalidSectionInsertException.class)
                .hasMessageContaining("상행, 하행이 대상 노선에 둘 다 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상행역 id와 하행역 id, 거리를 입력받아 구간이 추가될 수 있는 지 검사시에"
            + " 기존 구간 사이에 새 역이 등록될 시 기존 구간보다 거리가 길거나 같으면 예외가 발생")
    void validateSectionTooLongDistance() {
        assertThatThrownBy(() -> lineSections.validateSection(1L, 5L, 10))
                .isInstanceOf(InvalidSectionInsertException.class)
                .hasMessageContaining("역 사이에 새로운 역을 등록할 경우, 기존 역 사이 길이보다 크거나 같으면 등록할 수 없습니다.");
    }

    @Test
    @DisplayName("추가되는 구간 정보를 입력받아 수정될 구간과 새로 등록될 구간 정보를 반환 - 상행이 같은 사이 추가")
    void findOverlapSection1() {
        final SectionUpdateResult targetSections
                = lineSections.findOverlapSection(section2.getUpStationId(), 5L, 6);

        final Section updatedSection = targetSections.getUpdatedSection();
        final Section addedSection = targetSections.getAddedSection();
        assertAll(
                () -> assertThat(updatedSection.getId())
                        .isEqualTo(section2.getId()),
                () -> assertThat(updatedSection.getUpStationId())
                        .isEqualTo(section2.getUpStationId()),
                () -> assertThat(updatedSection.getDownStationId())
                        .isEqualTo(5L),
                () -> assertThat(updatedSection.getDistance())
                        .isEqualTo(6),

                () -> assertThat(addedSection.getUpStationId())
                        .isEqualTo(5L),
                () -> assertThat(addedSection.getDownStationId())
                        .isEqualTo(section2.getDownStationId()),
                () -> assertThat(addedSection.getDistance())
                        .isEqualTo(4)
        );
    }

    @Test
    @DisplayName("추가되는 구간 정보를 입력받아 수정될 구간과 새로 등록될 구간 정보를 반환 - 하행이 같은 사이 추가")
    void findOverlapSection2() {
        final SectionUpdateResult targetSections
                = lineSections.findOverlapSection(5L, section2.getDownStationId(), 6);

        final Section updatedSection = targetSections.getUpdatedSection();
        final Section addedSection = targetSections.getAddedSection();
        assertAll(
                () -> assertThat(updatedSection.getId())
                        .isEqualTo(section2.getId()),
                () -> assertThat(updatedSection.getUpStationId())
                        .isEqualTo(section2.getUpStationId()),
                () -> assertThat(updatedSection.getDownStationId())
                        .isEqualTo(5L),
                () -> assertThat(updatedSection.getDistance())
                        .isEqualTo(4),

                () -> assertThat(addedSection.getUpStationId())
                        .isEqualTo(5L),
                () -> assertThat(addedSection.getDownStationId())
                        .isEqualTo(section2.getDownStationId()),
                () -> assertThat(addedSection.getDistance())
                        .isEqualTo(6)
        );
    }

    @Test
    @DisplayName("추가되는 구간 정보를 입력받아 수정될 구간과 새로 등록될 구간 정보를 반환 - 맨 앞에 추가")
    void findOverlapSection3() {
        final SectionUpdateResult targetSections
                = lineSections.findOverlapSection(5L, section1.getUpStationId(), 6);

        final Section updatedSection = targetSections.getUpdatedSection();
        final Section addedSection = targetSections.getAddedSection();

        assertAll(
                () -> assertThat(updatedSection.getId())
                        .isEqualTo(section1.getId()),
                () -> assertThat(updatedSection.getUpStationId())
                        .isEqualTo(section1.getUpStationId()),
                () -> assertThat(updatedSection.getDownStationId())
                        .isEqualTo(section1.getDownStationId()),
                () -> assertThat(updatedSection.getDistance())
                        .isEqualTo(section1.getDistance()),

                () -> assertThat(addedSection.getUpStationId())
                        .isEqualTo(5L),
                () -> assertThat(addedSection.getDownStationId())
                        .isEqualTo(section1.getUpStationId()),
                () -> assertThat(addedSection.getDistance())
                        .isEqualTo(6)
        );
    }

    @Test
    @DisplayName("추가되는 구간 정보를 입력받아 수정될 구간과 새로 등록될 구간 정보를 반환 - 맨 뒤에 추가")
    void findOverlapSection4() {
        final SectionUpdateResult targetSections
                = lineSections.findOverlapSection(section3.getDownStationId(), 5L, 6);

        final Section updatedSection = targetSections.getUpdatedSection();
        final Section addedSection = targetSections.getAddedSection();

        assertAll(
                () -> assertThat(updatedSection.getId())
                        .isEqualTo(section3.getId()),
                () -> assertThat(updatedSection.getUpStationId())
                        .isEqualTo(section3.getUpStationId()),
                () -> assertThat(updatedSection.getDownStationId())
                        .isEqualTo(section3.getDownStationId()),
                () -> assertThat(updatedSection.getDistance())
                        .isEqualTo(section3.getDistance()),

                () -> assertThat(addedSection.getUpStationId())
                        .isEqualTo(section3.getDownStationId()),
                () -> assertThat(addedSection.getDownStationId())
                        .isEqualTo(5L),
                () -> assertThat(addedSection.getDistance())
                        .isEqualTo(6)
        );
    }
}