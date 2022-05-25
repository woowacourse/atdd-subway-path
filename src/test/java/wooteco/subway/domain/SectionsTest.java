package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.Fixtures.STATION_3;
import static wooteco.subway.Fixtures.STATION_1;
import static wooteco.subway.Fixtures.STATION_4;
import static wooteco.subway.Fixtures.SECTION_1_2;
import static wooteco.subway.Fixtures.SECTION_1_3;
import static wooteco.subway.Fixtures.SECTION_2_3;
import static wooteco.subway.Fixtures.SECTION_3_4;
import static wooteco.subway.Fixtures.STATION_2;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Nested
@DisplayName("Sections 클래스")
class SectionsTest {

    @Test
    @DisplayName("toSortedStations 메서드는 구간들에 포함된 역들을 상행에서 하행순으로 반환한다.")
    void toSortedStations() {
        final Sections sections = new Sections(new Section(new Station(1L, STATION_1), new Station(2L, STATION_2), 10),
                new Section(new Station(2L, STATION_2), new Station(3L, STATION_3), 10));

        final List<Station> stations = sections.toSortedStations();

        assertThat(stations).containsExactly(new Station(1L, STATION_1), new Station(2L, STATION_2), new Station(3L,
                STATION_3));
    }

    @Nested
    @DisplayName("AddSection 메소드는")
    class AddSectionTest {

        @Test
        @DisplayName("1-2 구간이 있을 때 2-3 구간을 추가하면, 2-3 구간이 추가된다.")
        void addSection1() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_2));

            // when
            sections.add(SECTION_2_3);

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2, SECTION_2_3);
        }

        @Test
        @DisplayName("2-3 구간이 있을 때, 1-2 구간을 추가하면, 1-2 구간이 추가된다.")
        void addSection2() {
            // given
            final Sections sections = new Sections(List.of(SECTION_2_3));

            // when
            sections.add(SECTION_1_2);

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2, SECTION_2_3);
        }

        @Test
        @DisplayName("1-3 구간이 있을 때 1-2 구간을 추가하면, 1-3 구간이 삭제되고, 1-2, 2-3 구간이 추가된다.")
        void addSection3() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_3));

            // when
            sections.add(new Section(new Station(1L, STATION_1), new Station(2L, STATION_2), 5));

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2, SECTION_2_3);
        }

        @Test
        @DisplayName("1-3 구간이 있을 때 2-3 구간을 추가하면, 1-3 구간이 삭제되고 1-2,1-3 구간이 추가된다.")
        void addSection4() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_3));

            // when
            sections.add(new Section(new Station(2L, STATION_2), new Station(3L, STATION_3), 5));

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2, SECTION_2_3);
        }

        @Test
        @DisplayName("1-3 구간이 있을 때 거리가 길거나 같은 2-3 구간을 추가하면 예외를 던진다.")
        void exceptionAddLongSection() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_3));

            // when & then
            assertThatThrownBy(() -> sections.add(SECTION_2_3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("구간의 길이가 너무 길어 추가할 수 없습니다.");
        }

        @Test
        @DisplayName("존재하는 구간을 추가하면 예외를 던진다.")
        void exceptionAddDifferentLineIdSection() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_3));
            sections.add(new Section(new Station(1L, STATION_1), new Station(2L, STATION_2), 5));

            // when & then
            assertThatThrownBy(() -> sections.add(SECTION_2_3))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 있는 구간은 추가할 수 없습니다.");

        }
    }

    @Nested
    @DisplayName("removeSection 메서드는.")
    class RemoveSectionTest {

        @Test
        @DisplayName("1-2, 2-3 구간이 있고 1역을 삭제하면, 1-2가 삭제된다.")
        void removeSection1() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_2, SECTION_2_3));

            // when
            sections.remove(1L);

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_2_3);
        }

        @Test
        @DisplayName("1-2, 2-3 구간이 있고 3역을 삭제하면, 2-3이 삭제된다.")
        void removeSection2() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_2, SECTION_2_3));

            // when
            sections.remove(3L);

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2);
        }

        @Test
        @DisplayName("1-2, 2-3, 3-4 구간이 있고 2역을 삭제하면, 1-2,2-3 구간이 병합된다.")
        void removeSection3() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4));

            // when
            sections.remove(2L);

            // then
            assertThat(sections.getValues()).containsOnly(
                    new Section(new Station(1L, STATION_1), new Station(3L, STATION_3), 20), SECTION_3_4);
            assertThat(sections.getValues().get(1).getDistance()).isEqualTo(20);
        }

        @Test
        @DisplayName("1-2, 2-3, 3-4 구간이 있고 3 역을 삭제하면, 2-3,3-4 구간이 병합된다.")
        void removeSection4() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_2, SECTION_2_3, SECTION_3_4));

            // when
            sections.remove(3L);

            // then
            assertThat(sections.getValues()).containsOnly(
                    new Section(new Station(2L, STATION_2), new Station(4L, STATION_4), 20), SECTION_1_2);
            assertThat(sections.getValues().get(1).getDistance()).isEqualTo(20);
        }

        @Test
        @DisplayName("1-2구간이 있고 1 역을 삭제하면, 예외를 던진다.")
        void exceptionRemoveSection1() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_2));

            // when & then
            assertThatThrownBy(() -> sections.remove(1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("노선에 구간은 1개 이상이어야 합니다.");
        }
    }
}
