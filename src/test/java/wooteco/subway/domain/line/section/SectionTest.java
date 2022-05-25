package wooteco.subway.domain.line.section;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.TestFixture.강남역_ID;
import static wooteco.subway.TestFixture.삼성역_ID;
import static wooteco.subway.TestFixture.선릉역_ID;
import static wooteco.subway.TestFixture.역삼역_ID;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

@DisplayName("지하철구간")
class SectionTest {

    private static final long SECTION_ID = 1L;
    private static final long DISTANCE = 10;

    @DisplayName("상행역과 하행역은 동일할 수 없다.")
    @Test
    void validateStationsNotSame() {
        assertThatThrownBy(() -> new Section(SECTION_ID, 강남역_ID, 강남역_ID, DISTANCE))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행역과 하행역은 동일할 수 없습니다.");
    }

    @DisplayName("구간을 비교하다")
    @Nested
    @TestInstance(Lifecycle.PER_CLASS)
    class CompareTest {

        @DisplayName("해당 구간의 상행역을 포함하고 있는지 확인한다.")
        @ParameterizedTest
        @MethodSource("provideForContainsUpStationOf")
        void containsUpStationOf(Section source, Section target, boolean expected) {
            boolean actual = source.containsUpStationOf(target);
            assertThat(actual).isEqualTo(expected);
        }

        private Stream<Arguments> provideForContainsUpStationOf() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            return Stream.of(
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 강남역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 역삼역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 강남역_ID, 선릉역_ID, DISTANCE), true),
                    Arguments.of(source, new Section(SECTION_ID, 역삼역_ID, 선릉역_ID, DISTANCE), true));
        }

        @DisplayName("해당 구간의 하행역을 포함하고 있는지 확인한다.")
        @ParameterizedTest
        @MethodSource("provideForContainsDownStationOf")
        void containsDownStationOf(Section source, Section target, boolean expected) {
            boolean actual = source.containsDownStationOf(target);
            assertThat(actual).isEqualTo(expected);
        }

        private Stream<Arguments> provideForContainsDownStationOf() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            return Stream.of(
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 강남역_ID, DISTANCE), true),
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 역삼역_ID, DISTANCE), true),
                    Arguments.of(source, new Section(SECTION_ID, 강남역_ID, 선릉역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 역삼역_ID, 선릉역_ID, DISTANCE), false));
        }

        @DisplayName("하행역이 대상의 상행역과 일치하는지 확인한다.")
        @ParameterizedTest
        @MethodSource("provideForIsPreviousOf")
        void isPreviousOf(Section source, Section target, boolean expected) {
            boolean actual = source.isPreviousOf(target);
            assertThat(actual).isEqualTo(expected);
        }

        private Stream<Arguments> provideForIsPreviousOf() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            return Stream.of(
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 강남역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 역삼역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 강남역_ID, 선릉역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 역삼역_ID, 선릉역_ID, DISTANCE), true));
        }

        @DisplayName("상행역이 대상의 하행역과 일치하는지 확인한다.")
        @ParameterizedTest
        @MethodSource("provideForIsNextOf")
        void isNextOf(Section source, Section target, boolean expected) {
            boolean actual = source.isNextOf(target);
            assertThat(actual).isEqualTo(expected);
        }

        private Stream<Arguments> provideForIsNextOf() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            return Stream.of(
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 강남역_ID, DISTANCE), true),
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 역삼역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 강남역_ID, 선릉역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 역삼역_ID, 선릉역_ID, DISTANCE), false));
        }

        @DisplayName("상행역이 일치하는지 확인한다.")
        @ParameterizedTest
        @MethodSource("provideForEqualsUpStation")
        void equalsUpStation(Section source, Section target, boolean expected) {
            boolean actual = source.equalsUpStation(target);
            assertThat(actual).isEqualTo(expected);
        }

        private Stream<Arguments> provideForEqualsUpStation() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            return Stream.of(
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 강남역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 역삼역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 강남역_ID, 선릉역_ID, DISTANCE), true),
                    Arguments.of(source, new Section(SECTION_ID, 역삼역_ID, 선릉역_ID, DISTANCE), false));
        }

        @DisplayName("하행역이 일치하는지 확인한다.")
        @ParameterizedTest
        @MethodSource("provideForEqualsDownStation")
        void equalsDownStation(Section source, Section target, boolean expected) {
            boolean actual = source.equalsDownStation(target);
            assertThat(actual).isEqualTo(expected);
        }

        private Stream<Arguments> provideForEqualsDownStation() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            return Stream.of(
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 강남역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 선릉역_ID, 역삼역_ID, DISTANCE), true),
                    Arguments.of(source, new Section(SECTION_ID, 강남역_ID, 선릉역_ID, DISTANCE), false),
                    Arguments.of(source, new Section(SECTION_ID, 역삼역_ID, 선릉역_ID, DISTANCE), false));

        }

        @DisplayName("해당 역을 상행역으로 지니고 있는지 확인한다.")
        @ParameterizedTest
        @MethodSource("provideForContainsAsUpStation")
        void containsAsUpStation(Section source, long target, boolean expected) {
            boolean actual = source.containsAsUpStation(target);
            assertThat(actual).isEqualTo(expected);
        }

        private Stream<Arguments> provideForContainsAsUpStation() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            return Stream.of(
                    Arguments.of(source, 강남역_ID, true),
                    Arguments.of(source, 역삼역_ID, false),
                    Arguments.of(source, 선릉역_ID, false));
        }

        @DisplayName("해당 역을 하행역으로 지니고 있는지 확인한다.")
        @ParameterizedTest
        @MethodSource("provideForContainsAsDownStation")
        void containsAsDownStation(Section source, long target, boolean expected) {
            boolean actual = source.containsAsDownStation(target);
            assertThat(actual).isEqualTo(expected);
        }

        private Stream<Arguments> provideForContainsAsDownStation() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            return Stream.of(
                    Arguments.of(source, 강남역_ID, false),
                    Arguments.of(source, 역삼역_ID, true),
                    Arguments.of(source, 선릉역_ID, false));
        }
    }

    @DisplayName("구간을 쪼개다")
    @Nested
    class SplitTest {

        @DisplayName("기존 구간의 상행역과 일치하는 경우")
        @Test
        void splitWithSameUpStation() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, 10);
            Section target = new Section(SECTION_ID, 강남역_ID, 선릉역_ID, 3);

            List<Section> actual = source.split(target);
            List<Section> expected = List.of(
                    new Section(SECTION_ID, 강남역_ID, 선릉역_ID, 3),
                    new Section(SECTION_ID, 선릉역_ID, 역삼역_ID, 7));

            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }

        @DisplayName("기존 구간의 하행역과 일치하는 경우")
        @Test
        void splitWithSameDownStation() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, 10);
            Section target = new Section(SECTION_ID, 선릉역_ID, 역삼역_ID, 3);

            List<Section> actual = source.split(target);
            List<Section> expected = List.of(
                    new Section(SECTION_ID, 강남역_ID, 선릉역_ID, 7),
                    new Section(SECTION_ID, 선릉역_ID, 역삼역_ID, 3));

            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }

        @DisplayName("상행역 또는 하행역이 일치하지 않는 구간으로 기존의 구간을 쪼개다.")
        @Test
        void splitWithNonConnectedSection() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            Section target = new Section(SECTION_ID, 선릉역_ID, 삼성역_ID, DISTANCE - 1);
            assertThatThrownBy(() -> source.split(target))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("상행역과 하행역이 일치하지 않습니다.");
        }

        @DisplayName("거리가 길거나 같은 구간으로 기존의 구간을 쪼개다.")
        @ParameterizedTest
        @CsvSource(value = {"10,10", "10,11"})
        void splitWithLongestDistance(long sourceDistance, long targetDistance) {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, sourceDistance);
            Section target = new Section(SECTION_ID, 강남역_ID, 선릉역_ID, targetDistance);
            assertThatThrownBy(() -> source.split(target))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("기존 구간의 거리보다 길거나 같습니다.");
        }
    }

    @DisplayName("구간을 합치다")
    @Nested
    class MergeTest {

        @DisplayName("하행역과 대상의 상행역이 일치하는 경우")
        @Test
        void mergeAsPreviousSection() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, 10);
            Section target = new Section(SECTION_ID, 역삼역_ID, 선릉역_ID, 3);

            Section actual = source.merge(target);
            Section expected = new Section(SECTION_ID, 강남역_ID, 선릉역_ID, 13);

            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }

        @DisplayName("상행역과 대상의 하행역이 일치하는 경우")
        @Test
        void mergeAsNextSection() {
            Section source = new Section(SECTION_ID, 역삼역_ID, 선릉역_ID, 10);
            Section target = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, 3);

            Section actual = source.merge(target);
            Section expected = new Section(SECTION_ID, 강남역_ID, 선릉역_ID, 13);

            assertThat(actual).usingRecursiveComparison()
                    .ignoringFields("id")
                    .isEqualTo(expected);
        }

        @DisplayName("이어지지 않는 구간들을 합치다.")
        @Test
        void mergeWithNonConnectedSection() {
            Section source = new Section(SECTION_ID, 강남역_ID, 역삼역_ID, DISTANCE);
            Section target = new Section(SECTION_ID, 선릉역_ID, 삼성역_ID, DISTANCE);
            assertThatThrownBy(() -> source.merge(target))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("두 구간은 이어지지 않았습니다.");
        }
    }
}
