package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import wooteco.subway.domain.vo.StationName;

class StationTest {

    @DisplayName("Station 객체 생성 테스트. 이름으로 Station 객체를 생성할 수 있다.")
    @Test
    void testMethodNameHere() {
        // given & when
        final Station station = new Station(StationName.from("삼성역"));

        // when &then
        assertThat(station).isNotNull();
    }

    @DisplayName("boolean isSameId(Long id) 테스트")
    @Test
    void isSameId() {
        // given
        final Station station = new Station(1L, StationName.from("삼성역"));

        // when
        final boolean actual = station.isSameId(1L);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("Long getId() 테스트")
    @Test
    void getId() {
        // given
        final long expected = 1L;
        final Station station = new Station(expected, StationName.from("삼성역"));

        // when
        final Long actual = station.getId();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("String getName() 테스트")
    @Test
    void getName() {
        final String expected = "삼성역";
        final Station station = new Station(1L, StationName.from(expected));

        // when
        final String actual = station.getName();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("boolean equals(Station other) 테스트. 이름이 같은 지하철역은 동등하다")
    @Test
    void testEquals() {
        // given
        final Station station = new Station(1L, StationName.from("삼성역"));
        final Station stationWithSameName = new Station(2L, StationName.from("삼성역"));

        // when
        final boolean actual = station.equals(stationWithSameName);

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("int hashCode() 테스트. 이름이 같은 지하철역은 동등하다.")
    @Test
    void testHashCode() {
        // given
        final Station station = new Station(1L, StationName.from("삼성역"));
        final Station stationWithSameName = new Station(2L, StationName.from("삼성역"));

        // when
        final int actual = Stream.of(station, stationWithSameName)
                .collect(Collectors.toSet())
                .size();

        // then
        assertThat(actual).isOne();
    }

    @ParameterizedTest(name = "생성 시도 매개변수 : {0}")
    @ValueSource(strings = {"", "   ",
            "255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255자가넘는이름255!"})
    @DisplayName("빈 값으로 Station 생성 시도시 예외가 발생한다")
    void creatingStationWithNullOrEmptyNameShouldFail(String input) {
        // given & when & then
        assertThatThrownBy(() -> new Station(StationName.from(input)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
