package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SectionsTest {

    private final Section section1 = new Section(1L, 1L, 3L, 1L, 10);
    private final Section section2 = new Section(2L, 1L, 2L, 3L, 10);
    private final Section section3 = new Section(3L, 1L, 4L, 2L, 10);
    private final Section section4 = new Section(4L, 1L, 5L, 4L, 10);

    private Sections sections;

    @BeforeEach
    void init() {
        sections = new Sections(new ArrayList<>(List.of(section3, section2, section1, section4)));
    }

    @DisplayName("Section 추가 시 수정되는 데이터")
    @ParameterizedTest
    @MethodSource("parameterProvider")
    void find_update_when_add(Section newSection, Section expected) {
        // given

        // when
        Optional<Section> section = sections.findUpdateWhenAdd(newSection);

        // then
        assertThat(section).isEqualTo(Optional.ofNullable(expected));
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
                Arguments.arguments(
                        new Section(1L, 4L, 6L, 5),
                        new Section(3L, 1L, 6L, 2L, 5)
                ),
                Arguments.arguments(
                        new Section(1L, 6L, 5L, 5),
                        null
                )
        );
    }

    @DisplayName("station id 목록")
    @Test
    void select_sorted_ids() {
        // given

        // when
        List<Long> ids = sections.getSortedStationIds();

        // then
        assertThat(ids).containsExactly(section4.getUpStationId(), section4.getDownStationId(),
                section3.getDownStationId(), section2.getDownStationId(), section1.getDownStationId());
    }

    @DisplayName("Section 삭제 시 수정되는 데이터")
    @ParameterizedTest
    @MethodSource("parameterProvider3")
    void find_update_when_delete(Long removeStationId, Long expected) {
        // given

        // when
        Long removedId = sections.findRemoveSectionId(removeStationId);

        // then
        assertThat(removedId).isEqualTo(expected);
    }

    private static Stream<Arguments> parameterProvider3() {
        return Stream.of(
                Arguments.arguments(1L, 1L),
                Arguments.arguments(3L, 2L),
                Arguments.arguments(5L, 4L)
        );
    }

    @DisplayName("Section 삭제 시 삭제되는 구간의 id")
    @ParameterizedTest
    @MethodSource("parameterProvider2")
    void when_delete(Long removeId, Section expected) {
        // given

        // when
        Optional<Section> section = sections.findUpdateWhenRemove(removeId);

        // then
        assertThat(section).isEqualTo(Optional.ofNullable(expected));
    }

    private static Stream<Arguments> parameterProvider2() {
        return Stream.of(
                Arguments.arguments(
                        1L,
                        null
                ),
                Arguments.arguments(
                        3L,
                        new Section(1L, 1L, 2L, 1L, 20)
                ),
                Arguments.arguments(
                        5L,
                        null
                )
        );
    }
}