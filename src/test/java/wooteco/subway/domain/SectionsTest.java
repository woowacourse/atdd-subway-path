package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.Fixtures.GANGNAM;
import static wooteco.subway.Fixtures.HYEHWA;
import static wooteco.subway.Fixtures.JAMSIL;
import static wooteco.subway.Fixtures.SECTION_1_2_10;
import static wooteco.subway.Fixtures.SECTION_1_3_10;
import static wooteco.subway.Fixtures.SECTION_2_3_10;
import static wooteco.subway.Fixtures.SECTION_3_4_10;
import static wooteco.subway.Fixtures.SUNGSHIN;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@Nested
public class SectionsTest {

    @Test
    @DisplayName("구간들에서 역들을 추출한다.")
    void toStationIds() {
        final Sections sections = new Sections(new Section(new Station(1L, HYEHWA), new Station(2L, SUNGSHIN), 10),
                new Section(new Station(2L, SUNGSHIN), new Station(3L, GANGNAM), 10));

        final List<Station> stations = sections.toSortedStations();

        assertThat(stations).containsExactly(new Station(1L, HYEHWA), new Station(2L, SUNGSHIN), new Station(3L, GANGNAM));
    }

    @Nested
    @DisplayName("지하철 구간을 추가한다.")
    class AddSectionTest {

        @Test
        @DisplayName("1-2 구간이 있을 때, 2-3 구간을 추가한다.")
        void addSection1() {
            // given
            final Sections sections = new Sections(SECTION_1_2_10);

            // when
            sections.add(SECTION_2_3_10);

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2_10, SECTION_2_3_10);
        }

        @Test
        @DisplayName("2-3 구간이 있을 때, 1-2 구간을 추가한다.")
        void addSection2() {
            // given
            final Sections sections = new Sections(SECTION_2_3_10);

            // when
            sections.add(SECTION_1_2_10);

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2_10, SECTION_2_3_10);
        }

        @Test
        @DisplayName("1-3 구간이 있을 때, 1-2 구간을 추가한다. 1-3 구간이 삭제되고, 1-2, 1-3 구간이 추가된다.")
        void addSection3() {
            // given
            final Sections sections = new Sections(SECTION_1_3_10);

            // when
            sections.add(new Section(new Station(1L, HYEHWA), new Station(2L, SUNGSHIN), 5));

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2_10, SECTION_2_3_10);
        }

        @Test
        @DisplayName("1-3 구간이 있을 때, 2-3 구간을 추가한다. 1-3 구간이 삭제되고, 1-2, 1-3 구간이 추가된다.")
        void addSection4() {
            // given
            final Sections sections = new Sections(SECTION_1_3_10);

            // when
            sections.add(new Section(new Station(2L, SUNGSHIN), new Station(3L, GANGNAM), 5));

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2_10, SECTION_2_3_10);
        }

        @Test
        @DisplayName("1-3 구간이 있을 때, 2-3 구간을 추가한다. 이때 2-3 구간의 거리가 1-3 구간의 거리보다 같거나 큰 경우, 예외를 발생키신다.")
        void exceptionAddLongSection() {
            // given
            final Sections sections = new Sections(SECTION_1_3_10);

            // when & then
            assertThatThrownBy(() -> sections.add(SECTION_2_3_10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("구간의 길이가 너무 길어 추가할 수 없습니다.");
        }

        @Test
        @DisplayName("추가하는 구간이 이미 존재하는 경우, 예외를 발생시킨다.")
        void exceptionAddDifferentLineIdSection() {
            // given
            final Sections sections = new Sections(SECTION_1_3_10);
            sections.add(new Section(new Station(1L, HYEHWA), new Station(2L, SUNGSHIN), 5));

            // when & then
            assertThatThrownBy(() -> sections.add(SECTION_2_3_10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 있는 구간은 추가할 수 없습니다.");
        }

        @Test
        @DisplayName("추가하는 구간이 기존 구간과 연결되지 않는 경우, 예외를 발생시킨다.")
        void exceptionAddDisconnectedSection() {
            // given
            final Sections sections = new Sections(SECTION_1_2_10);

            // when & then
            assertThatThrownBy(() -> sections.add(SECTION_3_4_10))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("기존의 구간과 연결되는 구간만 추가할 수 있습니다.");
        }
    }

    @Nested
    @DisplayName("지하철 구간을 추가한다.")
    class RemoveSectionTest {

        @Test
        @DisplayName("1-2, 2-3 구간이 있을 때, 1 역을 삭제한다.")
        void removeSection1() {
            // given
            final Sections sections = new Sections(SECTION_1_2_10, SECTION_2_3_10);

            // when
            sections.remove(1L);

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_2_3_10);
        }

        @Test
        @DisplayName("1-2, 2-3 구간이 있을 때, 3 역을 삭제한다.")
        void removeSection2() {
            // given
            final Sections sections = new Sections(SECTION_1_2_10, SECTION_2_3_10);

            // when
            sections.remove(3L);

            // then
            assertThat(sections.getValues()).containsOnly(SECTION_1_2_10);
        }

        @Test
        @DisplayName("1-2, 2-3, 3-4 구간이 있을 때, 2 역을 삭제한다.")
        void removeSection3() {
            // given
            final Sections sections = new Sections(List.of(SECTION_1_2_10, SECTION_2_3_10, SECTION_3_4_10));

            // when
            sections.remove(2L);

            // then
            assertThat(sections.getValues()).containsOnly(
                    new Section(new Station(1L, HYEHWA), new Station(3L, GANGNAM), 20), SECTION_3_4_10);
            assertThat(sections.getValues().get(1).getDistance()).isEqualTo(20);
        }

        @Test
        @DisplayName("1-2, 2-3, 3-4 구간이 있을 때, 3 역을 삭제한다.")
        void removeSection4() {
            // given
            final Sections sections = new Sections(SECTION_1_2_10, SECTION_2_3_10, SECTION_3_4_10);

            // when
            sections.remove(3L);

            // then
            assertThat(sections.getValues()).containsOnly(
                    new Section(new Station(2L, SUNGSHIN), new Station(4L, JAMSIL), 20), SECTION_1_2_10);
            assertThat(sections.getValues().get(1).getDistance()).isEqualTo(20);
        }

        @Test
        @DisplayName("1-2구간이 있을 때, 1 역을 삭제하면 예외를 발생시킨다.")
        void exceptionRemoveSection1() {
            // given
            final Sections sections = new Sections(SECTION_1_2_10);

            // when & then
            assertThatThrownBy(() -> sections.remove(1L))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("노선에 구간은 1개 이상이어야 합니다.");
        }
    }
}
