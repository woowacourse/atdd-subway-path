package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SectionsTest {

    private static final Section SECTION_1_2 = new Section(1L, 1L, 1L, 2L, 10);
    private static final Section SECTION_2_3 = new Section(2L, 1L, 2L, 3L, 10);
    private static final Section SECTION_3_4 = new Section(3L, 1L, 3L, 4L, 10);

    @DisplayName("구간 목록에 포함된 모든 역 id를 반환한다.")
    @Test
    void getStationIds() {
        final Sections sections = new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4));

        assertThat(sections.getSortedStationIdsInSingleLine()).containsExactly(1L, 2L, 3L, 4L);
    }

    @DisplayName("삭제 가능한 구간을 반환한다.")
    @Test
    void getSectionsToDelete() {
        final Sections sections = new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4));

        assertAll(
                () -> assertThat(sections.getSectionsToDelete(1L).size()).isEqualTo(1),
                () -> assertThat(sections.getSectionsToDelete(2L).size()).isEqualTo(2)
        );
    }

    @Test
    void getSectionIds() {
        final Sections sections = new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4));

        final List<Long> sectionIds = sections.getSectionIds();

        assertThat(sectionIds).containsExactly(1L, 2L, 3L);
    }

    @Test
    void merge() {
        final Sections sections = new Sections(List.of(SECTION_1_2, SECTION_2_3));

        final Section mergedSection = sections.merge();

        assertThat(mergedSection.getDistance()).isEqualTo(20);
    }
}
