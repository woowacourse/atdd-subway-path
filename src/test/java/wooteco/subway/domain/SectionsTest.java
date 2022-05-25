package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.fixture.TestFixture.미금역;
import static wooteco.subway.fixture.TestFixture.서현역;
import static wooteco.subway.fixture.TestFixture.역삼역;
import static wooteco.subway.fixture.TestFixture.잠실역;
import static wooteco.subway.fixture.TestFixture.정자역;
import static wooteco.subway.fixture.TestFixture.판교역;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    @DisplayName("Section 추가 시 수정되는 데이터")
    @ParameterizedTest
    @MethodSource("parameterProvider")
    void find_update_when_add(Section newSection, Section expected) {
        // given
        Section 판교_to_역삼 = new Section(1L, 1L, 판교역, 역삼역, 10);
        Section 정자_to_판교 = new Section(2L, 1L, 정자역, 판교역, 10);
        Section 서현_to_정자 = new Section(3L, 1L, 서현역, 정자역, 10);
        Section 잠실_to_서현 = new Section(4L, 1L, 잠실역, 서현역, 10);
        Sections sections = new Sections(List.of(서현_to_정자, 정자_to_판교, 판교_to_역삼, 잠실_to_서현));

        // when
        Optional<Section> section = sections.findUpdateWhenAdd(newSection);

        // then
        assertThat(section).isEqualTo(Optional.ofNullable(expected));
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
                Arguments.arguments(
                        new Section(1L, 서현역, 미금역, 5),
                        new Section(3L, 1L, 미금역, 정자역, 5)
                ),
                Arguments.arguments(
                        new Section(1L, 미금역, 잠실역, 5),
                        null
                )
        );
    }

    @DisplayName("station id 목록")
    @Test
    void select_sorted_ids() {
        // given
        Section 판교_to_역삼 = new Section(1L, 1L, 판교역, 역삼역, 10);
        Section 정자_to_판교 = new Section(2L, 1L, 정자역, 판교역, 10);
        Section 서현_to_정자 = new Section(3L, 1L, 서현역, 정자역, 10);
        Section 잠실_to_서현 = new Section(4L, 1L, 잠실역, 서현역, 10);
        Sections sections = new Sections(List.of(서현_to_정자, 정자_to_판교, 판교_to_역삼, 잠실_to_서현));

        // when
        List<Long> ids = sections.getSortedStationIds();

        // then
        assertThat(ids).containsExactly(
                잠실_to_서현.getUpStation().getId(),
                잠실_to_서현.getDownStation().getId(),
                서현_to_정자.getDownStation().getId(),
                정자_to_판교.getDownStation().getId(),
                판교_to_역삼.getDownStation().getId()
        );
    }

    @DisplayName("Section 삭제 시 수정되는 데이터")
    @ParameterizedTest
    @MethodSource("parameterProvider3")
    void find_update_when_delete(Long removeStationId, Long expected) {
        // given
        Section 판교_to_역삼 = new Section(1L, 1L, 판교역, 역삼역, 10);
        Section 정자_to_판교 = new Section(2L, 1L, 정자역, 판교역, 10);
        Section 서현_to_정자 = new Section(3L, 1L, 서현역, 정자역, 10);
        Section 잠실_to_서현 = new Section(4L, 1L, 잠실역, 서현역, 10);
        Sections sections = new Sections(List.of(서현_to_정자, 정자_to_판교, 판교_to_역삼, 잠실_to_서현));

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
        Section 판교_to_역삼 = new Section(1L, 1L, 판교역, 역삼역, 10);
        Section 정자_to_판교 = new Section(2L, 1L, 정자역, 판교역, 10);
        Section 서현_to_정자 = new Section(3L, 1L, 서현역, 정자역, 10);
        Section 잠실_to_서현 = new Section(4L, 1L, 잠실역, 서현역, 10);
        Sections sections = new Sections(List.of(서현_to_정자, 정자_to_판교, 판교_to_역삼, 잠실_to_서현));

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
                        new Section(1L, 1L, 정자역, 역삼역, 20)
                ),
                Arguments.arguments(
                        5L,
                        null
                )
        );
    }
}
