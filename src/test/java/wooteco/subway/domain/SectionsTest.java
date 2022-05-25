package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Sections 테스트")
class SectionsTest {

    private static final Long DUMMY_LINE_ID = 1L;

    Sections sut = new Sections();
    Station station1;
    Station station2;
    Station station3;
    Station station4;

    @BeforeEach
    void setUp() {
        //given
        station1 = new Station(1L, "station1");
        station2 = new Station(2L, "station2");
        station3 = new Station(3L, "station3");
        station4 = new Station(4L, "station4");
    }
    @Nested
    @DisplayName("add 메서드는")
    class Describe_add {

        @Nested
        @DisplayName("만약 이전에 Section이 추가된 적이 없다면")
        class Context_with_no_line_registered {

            @DisplayName("주어진 Section을 그대로 추가한다")
            @Test
            void add_section_without_change() {
                // given
                Section givenSection = new Section(1L, DUMMY_LINE_ID, station1, station2, 10);

                // when
                sut.add(givenSection);

                // then
                assertThat(sut.values()).containsExactly(givenSection);
            }
        }

        @Nested
        @DisplayName("만약 상행 종점역과 주어진 Section의 하행역이 같다면")
        class Context_with_given_sections_down_station_is_same_with_sections_up_terminal {

            @DisplayName("주어진 구간을 상행 종점으로 추가한다")
            @Test
            void add__given_section_to_up_terminal() {
                // given
                Section previouslyRegistered = new Section(1L, DUMMY_LINE_ID, station1, station2, 10);
                Section givenSection = new Section(2L, DUMMY_LINE_ID, station3, station1, 10);
                sut.add(previouslyRegistered);

                // when
                sut.add(givenSection);

                // then
                assertThat(sut.values()).containsOnly(givenSection, previouslyRegistered);
            }
        }

        @Nested
        @DisplayName("만약 하행 종점역과 주어진 Section의 상행역이 같다면")
        class Context_with_given_sections_up_station_is_same_with_sections_down_terminal {

            @DisplayName("주어진 구간을 하행 종점으로 추가한다")
            @Test
            void add_given_section_to_down_terminal() {
                // given
                Section previouslyRegistered = new Section(1L, DUMMY_LINE_ID, station1, station2, 10);
                Section givenSection = new Section(2L, DUMMY_LINE_ID, station2, station3, 10);
                sut.add(previouslyRegistered);

                // when
                sut.add(givenSection);

                // then
                assertThat(sut.values()).containsOnly(previouslyRegistered, givenSection);
            }
        }

        @Nested
        @DisplayName("만약 같은 상행, 하행역을 가진 구간이 이미 등록되어 있다면")
        class Context_with_given_sections_stations_are_already_registered{

            @DisplayName("예외와 예외 메시지를 던진다")
            @Test
            void throws_exception() {
                // given
                Section previouslyRegistered = new Section(1L, DUMMY_LINE_ID, station1, station2, 10);
                Section givenSection = new Section(2L, DUMMY_LINE_ID, station1, station2, 9);
                sut.add(previouslyRegistered);

                // when && then
                assertThatThrownBy(() -> sut.add(givenSection))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("연결 가능한 구간이 아닙니다.");
            }
        }

        @Nested
        @DisplayName("만약 테스트 대상에 주어진 Section의 하행역과 상행역이 이미 등록되어 있다면")
        class Context_with_given_sections_up_and_down_station_already_registered_in_sut {

            @DisplayName("예외와 예외 메시지를 던진다")
            @Test
            void throws_exception() {
                // given
                Section previouslyRegistered1 = new Section(1L, DUMMY_LINE_ID, station1, station2, 10);
                Section previouslyRegistered2 = new Section(2L, DUMMY_LINE_ID, station2, station3, 10);
                Section givenSection = new Section(3L, DUMMY_LINE_ID, station1, station3, 10);

                sut.add(previouslyRegistered1);
                sut.add(previouslyRegistered2);

                // when && then
                assertThatThrownBy(() -> sut.add(givenSection))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("연결 가능한 구간이 아닙니다.");
            }
        }

        @Nested
        @DisplayName("만약 연결할 구간이 존재하지 않는다면")
        class Context_with_no_section_to_link {

            @DisplayName("예외와 예외 메시지를 던진다")
            @Test
            void throws_exception() {
                // given
                Section previouslyRegistered = new Section(1L, DUMMY_LINE_ID, station1, station2, 10);
                Section givenSection = new Section(2L, DUMMY_LINE_ID, station3, station4, 10);

                sut.add(previouslyRegistered);

                // when && then
                assertThatThrownBy(() -> sut.add(givenSection))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("연결 가능한 구간이 아닙니다.");
            }
        }

        @Nested
        @DisplayName("만약 주어진 Section의 상행역과 기존에 등록된 Section의 상행역과 같으면")
        class Context_with_given_sections_up_station_is_same_with_up_station {

            @DisplayName("갈래길이 생기지 않도록 Section을 수정하여 추가한다")
            @Test
            void add_section_with_change_not_to_make_fork_road() {
                // given
                Section previouslyRegistered = new Section(1L, DUMMY_LINE_ID, station1, station3, 10);
                Section givenSection = new Section(2L, DUMMY_LINE_ID, station1, station2, 7);
                sut.add(previouslyRegistered);

                // when
                sut.add(givenSection);

                // then
                Set<Section> values = sut.values();
                assertThat(values).containsOnly(
                        new Section(1L, DUMMY_LINE_ID, station1, station2, 7),
                        new Section(2L, DUMMY_LINE_ID, station2, station3, 3)
                );
            }
        }

        @Nested
        @DisplayName("만약 주어진 Section의 하행역과 기존에 등록된 Section의 하행역과 같으면")
        class Context_with_given_sections_down_station_is_same_with_down_station {

            @DisplayName("갈래길이 생기지 않도록 Section을 수정하여 추가한다")
            @Test
            void add_section_with_change_not_to_make_fork_road() {
                // given
                Section previouslyRegistered = new Section(1L, DUMMY_LINE_ID, station1, station3, 10);
                Section givenSection = new Section(2L, DUMMY_LINE_ID, station2, station3, 7);
                sut.add(previouslyRegistered);

                // when
                sut.add(givenSection);

                // then
                Set<Section> values = sut.values();
                assertThat(values).containsOnly(
                        new Section(1L, DUMMY_LINE_ID, station1, station2, 3),
                        new Section(2L, DUMMY_LINE_ID, station2, station3, 7)
                );
            }
        }

        @Nested
        @DisplayName("만약 기존 구간의 사이에 주어진 구간을 추가할 때 주어진 구간의 거리가 기존 구간의 거리보다 크면 (상행역이 같을때)")
        class Context_with_given_sections_distance_is_longer_than_previous_sections_distance1 {

            @DisplayName("예외와 예외 메시지를 던진다")
            @Test
            void throws_exception() {
                // given
                Sections sut = new Sections();
                Section previouslyRegistered = new Section(1L, DUMMY_LINE_ID, station1, station3, 10);
                Section givenSection = new Section(2L, DUMMY_LINE_ID, station1, station2, 10);
                sut.add(previouslyRegistered);

                // when && then
                assertThatThrownBy(() -> sut.add(givenSection))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("구간의 길이가 올바르지 않습니다.");
            }
        }

        @Nested
        @DisplayName("만약 기존 구간의 사이에 주어진 구간을 추가할 때 주어진 구간의 거리가 기존 구간의 거리보다 크면 (하행역이 같을때)")
        class Context_with_given_sections_distance_is_longer_than_previous_sections_distance2 {

            @DisplayName("예외와 예외 메시지를 던진다")
            @Test
            void throws_exception() {
                // given
                Section previouslyRegistered = new Section(1L, DUMMY_LINE_ID, station1, station3, 10);
                Section givenSection = new Section(2L, DUMMY_LINE_ID, station2, station3, 10);
                sut.add(previouslyRegistered);

                // when && then
                assertThatThrownBy(() -> sut.add(givenSection))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("구간의 길이가 올바르지 않습니다.");
            }
        }
    }

    @Nested
    @DisplayName("removeStation 메서드는")
    class Describe_removeStation {
        Sections sut = new Sections();

        @Nested
        @DisplayName("만약 기존에 구간이 등록되어 있고 주어진 Station이 상행 종점역과 같다면")
        class Context_with_has_section_already_and_given_station_is_same_with_up_terminal {

            @DisplayName("구간 정보 변경 없이 상행 종점이 포함된 구간을 제거한다.")
            @Test
            void remove_up_terminal_without_change() {
                // given
                Section previouslyRegistered1 = new Section(1L, DUMMY_LINE_ID, station1, station2, 10);
                Section previouslyRegistered2 = new Section(2L, DUMMY_LINE_ID, station2, station3, 10);
                sut.add(previouslyRegistered1);
                sut.add(previouslyRegistered2);

                // when
                sut.removeStation(station3);

                // then
                assertThat(sut.values()).containsOnly(previouslyRegistered1);
            }
        }

        @Nested
        @DisplayName("만약 기존에 구간이 등록되어 있고 주어진 Station이 하행 종점역과 같다면")
        class Context_with_has_section_already_and_given_station_is_same_with_down_terminal {

            @DisplayName("구간 정보 변경 없이 하행 종점이 포함된 구간을 제거한다.")
            @Test
            void remove_down_terminal_without_change_section() {
                // given
                Section previouslyRegistered1 = new Section(1L, DUMMY_LINE_ID, station1, station2, 10);
                Section previouslyRegistered2 = new Section(2L, DUMMY_LINE_ID, station2, station3, 10);
                sut.add(previouslyRegistered1);
                sut.add(previouslyRegistered2);

                // when
                sut.removeStation(station3);

                // then
                assertThat(sut.values()).containsOnly(previouslyRegistered1);
            }
        }

        @Nested
        @DisplayName("만약 주어진 Station이 다른 역 사이에 존재하면")
        class Context_with_given_station_in_between_other_stations {

            @DisplayName("주어진 역을 제거하고 구간 정보를 변경한다.")
            @Test
            void remove_given_station_and_change_section() {
                // given
                sut.add(new Section(1L, DUMMY_LINE_ID, station1, station2, 10));
                sut.add(new Section(2L, DUMMY_LINE_ID, station2, station3, 10));
                sut.add(new Section(3L, DUMMY_LINE_ID, station3, station4, 10));

                // when
                sut.removeStation(station2);

                // then
                assertThat(sut.values()).containsOnly(
                        new Section(1L, DUMMY_LINE_ID, station1, station3, 20),
                        new Section(3L, DUMMY_LINE_ID, station3, station4, 10)
                );
            }
        }

        @Nested
        @DisplayName("만약 테스트 대상에 Section이 하나만 등록되어 있다면")
        class Context_with_sut_has_one_section {

            @DisplayName("예외를 던진다.")
            @Test
            void throws_exception() {
                // given
                sut.add(new Section(1L, DUMMY_LINE_ID, station1, station2, 10));

                // when && then
                assertAll(
                        () -> assertThatThrownBy(() -> sut.removeStation(station1))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("구간이 하나인 경우 역을 제거할 수 없습니다."),
                        () -> assertThatThrownBy(() -> sut.removeStation(station2))
                                .isInstanceOf(IllegalArgumentException.class)
                                .hasMessageContaining("구간이 하나인 경우 역을 제거할 수 없습니다.")
                );
            }
        }

        @Nested
        @DisplayName("만약 등록된 Section에 주어진 역이 포함되어 있지 않으면")
        class Context_with_given_station_not_exist_in_sections {

            @DisplayName("예외를 던진다.")
            @Test
            void throws_exception() {
                // given
                sut.add(new Section(1L, DUMMY_LINE_ID, station1, station2, 10));
                sut.add(new Section(2L, DUMMY_LINE_ID, station2, station3, 10));

                // when && then
                assertThatThrownBy(() -> sut.removeStation(station4))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessageContaining("해당 역이 존재하지 않습니다.");
            }
        }
    }

    @Nested
    @DisplayName("sortedStations 메서드는")
    class Describe_sortedStations {
        @DisplayName("Section을 정렬하여 반환한다.")
        @Test
        void returnSortedStations() {
            // given
            sut.add(new Section(1L, DUMMY_LINE_ID, station1, station3, 10));
            sut.add(new Section(2L, DUMMY_LINE_ID, station3, station2, 10));

            // when
            List<Station> stations = sut.sortedStations();

            // then
            assertThat(stations).containsExactly(station1, station3, station2);
        }
    }
}
