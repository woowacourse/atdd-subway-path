package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.fixture.TestFixture.미금역;
import static wooteco.subway.fixture.TestFixture.서현역;
import static wooteco.subway.fixture.TestFixture.역삼역;
import static wooteco.subway.fixture.TestFixture.잠실역;
import static wooteco.subway.fixture.TestFixture.정자역;
import static wooteco.subway.fixture.TestFixture.판교역;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;

@SuppressWarnings("NonAsciiCharacters")
class SectionsTest {

    @DisplayName("Section 추가")
    @ParameterizedTest
    @MethodSource("parameterProvider")
    void find_update_when_add(Section newSection) {
        // given
        Section 판교_to_역삼 = new Section(1L, 1L, 판교역, 역삼역, 10);
        Section 정자_to_판교 = new Section(2L, 1L, 정자역, 판교역, 10);
        Section 서현_to_정자 = new Section(3L, 1L, 서현역, 정자역, 10);
        Section 잠실_to_서현 = new Section(4L, 1L, 잠실역, 서현역, 10);
        Sections sections = new Sections(new ArrayList<>(List.of(서현_to_정자, 정자_to_판교, 판교_to_역삼, 잠실_to_서현)));
        int before = sections.getValue().size();

        // when
        sections.add(newSection);

        // then
        int after = sections.getValue().size();
        assertThat(before + 1).isEqualTo(after);
    }

    private static Stream<Arguments> parameterProvider() {
        return Stream.of(
                Arguments.arguments(
                        new Section(1L, 서현역, 미금역, 5)
                ),
                Arguments.arguments(
                        new Section(1L, 미금역, 잠실역, 5)
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
        Sections sections = new Sections(new ArrayList<>(List.of(서현_to_정자, 정자_to_판교, 판교_to_역삼, 잠실_to_서현)));

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

    @DisplayName("Section 삭제")
    @ParameterizedTest
    @ValueSource(longs = {1L, 3L, 5L})
    void find_update_when_delete(Long removeStationId) {
        // given
        Section 판교_to_역삼 = new Section(1L, 1L, 판교역, 역삼역, 10);
        Section 정자_to_판교 = new Section(2L, 1L, 정자역, 판교역, 10);
        Section 서현_to_정자 = new Section(3L, 1L, 서현역, 정자역, 10);
        Section 잠실_to_서현 = new Section(4L, 1L, 잠실역, 서현역, 10);
        Sections sections = new Sections(new ArrayList<>(List.of(서현_to_정자, 정자_to_판교, 판교_to_역삼, 잠실_to_서현)));
        int beforeSize = sections.getValue().size();

        // when
        sections.remove(removeStationId);

        // then
        int afterSize = sections.getValue().size();
        assertThat(afterSize).isEqualTo(beforeSize - 1);
    }
}
