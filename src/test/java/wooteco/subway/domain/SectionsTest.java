package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import wooteco.subway.exception.IllegalSectionException;

@SuppressWarnings("InnerClassMayBeStatic")
@DisplayName("Sections 클래스")
class SectionsTest {

    @Nested
    @DisplayName("add 메서드는")
    class Describe_add {
        private final Section givenSection = new Section(1L, 2L, 3L, 2);
        private final List<Section> sectionList = new ArrayList<>(Arrays.asList(givenSection));
        private final Sections sections = new Sections(sectionList);


        @Nested
        @DisplayName("새로 등록할 구간의 하행역이 이미 등록된 구간의 상행역으로 존재한다면")
        class Context_with_up_section {
            @Test
            @DisplayName("구간을 추가한다.")
            void it_does_not_throw_exception() {
                assertDoesNotThrow(() -> sections.add(new Section(1L, 1L, 2L, 1)));
            }
        }

        @Nested
        @DisplayName("새로 등록할 구간의 상행역이 이미 등록된 구간의 하행역으로 존재한다면")
        class Context_with_down_section {
            @Test
            @DisplayName("구간을 추가한다.")
            void it_does_not_throw_exception() {
                assertDoesNotThrow(() -> sections.add(new Section(1L, 3L, 4L, 1)));
            }
        }

        @Nested
        @DisplayName("두 역이 이미 존재한다면")
        class Context_with_existed_section {
            @Test
            @DisplayName("예외가 발생한다.")
            void it_does_throw_exception() {
                assertThatThrownBy(() -> sections.add(new Section(1L, 3L, 2L, 1)))
                        .isInstanceOf(IllegalSectionException.class)
                        .hasMessage(("이미 동일한 구간이 등록되어 있습니다."));
            }
        }

        @Nested
        @DisplayName("기존 구간 사이에 새로운 구간이 추가된다면")
        class Context_with_between_add_section {

            @Test
            @DisplayName("추가될 거리가 더 작을 때 두 구간으로 나뉜다.")
            void it_divided_two_section() {
                sections.add(new Section(1L, 2L, 1L, 1));
                assertThat(sections.getSections()).hasSize(2)
                        .extracting("distance")
                        .containsExactly(1, 1);
            }

            @Test
            @DisplayName("추가될 구간의 거리가 같거나 더 클 때 예외가 발생한다.")
            void it_does_throw_exception() {
                assertThatThrownBy(() -> sections.add(new Section(1L, 2L, 1L, 2)))
                        .isInstanceOf(IllegalSectionException.class)
                        .hasMessage(("등록하려는 구간 길이가 기존 구간의 길이와 같거나 더 길 수 없습니다."));
            }
        }

        @Nested
        @DisplayName("기존 구간에 존재하지 않는 역이 추가될 때")
        class Context_with_no_existed_section {
            @Test
            @DisplayName("예외가 발생한다.")
            void it_does_throw_exception() {
                assertThatThrownBy(() -> sections.add(new Section(1L, 4L, 5L, 2)))
                        .isInstanceOf(IllegalSectionException.class)
                        .hasMessage(("등록할 구간의 적어도 하나의 역은 등록되어 있어야 합니다."));
            }
        }
    }

    @Nested
    @DisplayName("delete 메서드는")
    class Describe_delete {
        private final Section givenSection1 = new Section(1L, 2L, 3L, 2);
        private final List<Section> sectionList = new ArrayList<>(Arrays.asList(givenSection1));
        private final Sections sections = new Sections(sectionList);

        @Nested
        @DisplayName("stationId가 주어지면")
        class Context_with_stationId {
            private final Long stationId = 3L;

            @Test
            @DisplayName("구간이 하나 이상 존재하면 구간을 삭제한다.")
            void it_delete_section() {
                sections.add(new Section(1L, 3L, 4L, 3));
                sections.delete(stationId);
                assertThat(sections.getSections()).hasSize(1)
                        .extracting("upStationId", "downStationId", "distance")
                        .contains(
                                tuple(2L, 4L, 5)
                        );
            }

            @Test
            @DisplayName("구간이 하나라면 구간을 삭제할 수 없다.")
            void it_can_not_delete_section() {
                assertThatThrownBy(() -> sections.delete(stationId)).isInstanceOf(IllegalSectionException.class)
                        .hasMessage("노선이 구간을 하나는 가져야하므로 구간을 제거할 수 없습니다.");
            }
        }
    }

    @Nested
    @DisplayName("getSortedSections 메서드는")
    class Describe_getSortedSections {

        @Nested
        @DisplayName("호출했을 때")
        class Context_with_sections {

            private final Section givenSection1 = new Section(1L, 2L, 3L, 3);
            private final Section givenSection2 = new Section(1L, 3L, 4L, 3);
            private final Section givenSection3 = new Section(1L, 4L, 5L, 3);
            private final Section givenSection4 = new Section(1L, 5L, 6L, 3);
            private final List<Section> sectionList = new ArrayList<>(
                    Arrays.asList(givenSection1, givenSection2, givenSection3, givenSection4));
            private final Sections sections = new Sections(sectionList);

            @Test
            @DisplayName("순서를 정렬한 구간들을 출력한다.")
            void it_get_sorted_sections() {
                assertThat(sections.getSortedSections())
                        .extracting("upStationId", "downStationId", "distance")
                        .containsExactly(
                                tuple(2L, 3L, 3),
                                tuple(3L, 4L, 3),
                                tuple(4L, 5L, 3),
                                tuple(5L, 6L, 3)
                        );
            }
        }
    }
}