package wooteco.subway.domain.line.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.TestFixture.강남_역삼;
import static wooteco.subway.TestFixture.강남역_ID;
import static wooteco.subway.TestFixture.삼성역_ID;
import static wooteco.subway.TestFixture.선릉_삼성;
import static wooteco.subway.TestFixture.선릉역_ID;
import static wooteco.subway.TestFixture.역삼_선릉;
import static wooteco.subway.TestFixture.역삼역_ID;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("지하철노선 집합")
class OrderedSectionsTest {

    private static final SectionSorter SECTION_SORTER = new SectionSorter();
    private static final long TEMPORARY_SECTION_ID = 99;
    private static final long TEMPORARY_SECTION_DISTANCE = 99;

    @DisplayName("지하철구간은 하나 이상이어야 한다.")
    @Test
    void validateSectionsNotEmpty() {
        assertThatThrownBy(() -> new OrderedSections(Collections.emptyList(), SECTION_SORTER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("지하철구간은 하나 이상이어야 합니다.");
    }

    @DisplayName("구간을 정렬한다.")
    @Test
    void sortSections() {
        OrderedSections sections = new OrderedSections(List.of(선릉_삼성, 역삼_선릉, 강남_역삼), SECTION_SORTER);

        List<Section> expected = List.of(강남_역삼, 역삼_선릉, 선릉_삼성);
        assertThat(sections.getSections()).isEqualTo(expected);
    }

    @DisplayName("구간을 추가하다")
    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class AppendTest {

        @DisplayName("상행역_ID과 하행역_ID 모두 포함하고 있으면 추가할 수 없다.")
        @ParameterizedTest
        @MethodSource("providerForAppendUpStationAndDownStationBothExist")
        void appendUpStationAndDownStationBothExist(OrderedSections source, Section target) {
            assertThatThrownBy(() -> source.append(target))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("해당 구간의 상행역과 하행역이 이미 노선에 존재합니다.");
        }

        private Stream<Arguments> providerForAppendUpStationAndDownStationBothExist() {
            OrderedSections source = new OrderedSections(List.of(강남_역삼, 역삼_선릉, 선릉_삼성), SECTION_SORTER);
            return Stream.of(
                    Arguments.of(source, new Section(TEMPORARY_SECTION_ID, 강남역_ID, 역삼역_ID, 1)),
                    Arguments.of(source, new Section(TEMPORARY_SECTION_ID, 강남역_ID, 선릉역_ID, 1)),
                    Arguments.of(source, new Section(TEMPORARY_SECTION_ID, 강남역_ID, 삼성역_ID, 1))
            );
        }

        @DisplayName("상행역_ID과 하행역_ID 둘 중 하나도 포함되어 있지 않으면 추가할 수 없다.")
        @Test
        void appendUpStationAndDownStationNeitherExist() {
            OrderedSections source = new OrderedSections(List.of(역삼_선릉), SECTION_SORTER);
            Section target = new Section(TEMPORARY_SECTION_ID, 강남역_ID, 삼성역_ID, 1);

            assertThatThrownBy(() -> source.append(target))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("해당 구간의 상행역과 하행역이 노선에 존재하지 않습니다.");
        }

        @DisplayName("상행 종점을 추가한다.")
        @Test
        void appendUpStation() {
            OrderedSections sections = new OrderedSections(List.of(역삼_선릉), SECTION_SORTER);
            sections.append(강남_역삼);

            List<Long> expected = List.of(강남역_ID, 역삼역_ID, 선릉역_ID);
            assertThat(sections.getStations()).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }

        @DisplayName("하행 종점을 추가한다.")
        @Test
        void appendDownStation() {
            OrderedSections sections = new OrderedSections(List.of(역삼_선릉), SECTION_SORTER);
            sections.append(선릉_삼성);

            List<Long> expected = List.of(역삼역_ID, 선릉역_ID, 삼성역_ID);
            assertThat(sections.getStations()).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }

        @DisplayName("상행역_ID이 동일한 구간 사이에 구간을 추가한다.")
        @Test
        void appendBetweenSectionsWithSameUpStation() {
            OrderedSections sections = new OrderedSections(
                    List.of(new Section(TEMPORARY_SECTION_ID, 강남역_ID, 삼성역_ID, TEMPORARY_SECTION_DISTANCE)), SECTION_SORTER);
            sections.append(강남_역삼);

            List<Long> expected = List.of(강남역_ID, 역삼역_ID, 삼성역_ID);
            assertThat(sections.getStations()).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }

        @DisplayName("하행역_ID이 동일한 구간 사이에 구간을 추가한다.")
        @Test
        void appendBetweenSectionsWithSameDownStation() {
            OrderedSections sections = new OrderedSections(
                    List.of(new Section(TEMPORARY_SECTION_ID, 강남역_ID, 삼성역_ID, TEMPORARY_SECTION_DISTANCE)), SECTION_SORTER);
            sections.append(선릉_삼성);

            List<Long> expected = List.of(강남역_ID, 선릉역_ID, 삼성역_ID);
            assertThat(sections.getStations()).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }
    }

    @DisplayName("역을 제거하다")
    @Nested
    class RemoveTest {

        @DisplayName("구간이 하나뿐인 노선의 역_ID을 제거한다.")
        @Test
        void removeStationFromLineWithOnlyOneSection() {
            OrderedSections sections = new OrderedSections(List.of(강남_역삼), SECTION_SORTER);
            assertThatThrownBy(() -> sections.remove(강남역_ID))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("노선의 구간이 하나이므로 구간을 삭제할 수 없습니다.");
        }

        @DisplayName("노선에 존재하지 않는 역_ID을 제거한다.")
        @Test
        void removeStationNotBelongToLine() {
            OrderedSections sections = new OrderedSections(List.of(강남_역삼, 역삼_선릉), SECTION_SORTER);
            assertThatThrownBy(() -> sections.remove(삼성역_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("노선에 포함되어 있는 역이 아닙니다.");
        }

        @DisplayName("상행종점역_ID을 제거한다.")
        @Test
        void removeUpStation() {
            OrderedSections sections = new OrderedSections(List.of(강남_역삼, 역삼_선릉), SECTION_SORTER);
            sections.remove(강남역_ID);

            List<Long> actual = sections.getStations();
            assertThat(actual).doesNotContain(강남역_ID);
        }

        @DisplayName("하행종점역_ID을 제거한다.")
        @Test
        void removeDownStation() {
            OrderedSections sections = new OrderedSections(List.of(강남_역삼, 역삼_선릉), SECTION_SORTER);
            sections.remove(선릉역_ID);

            List<Long> actual = sections.getStations();
            assertThat(actual).doesNotContain(선릉역_ID);
        }

        @DisplayName("중간역_ID을 제거한다.")
        @Test
        void removeMiddleStation() {
            OrderedSections sections = new OrderedSections(List.of(강남_역삼, 역삼_선릉), SECTION_SORTER);
            sections.remove(역삼역_ID);

            List<Long> actual = sections.getStations();
            assertThat(actual).doesNotContain(역삼역_ID);
        }
    }
}
