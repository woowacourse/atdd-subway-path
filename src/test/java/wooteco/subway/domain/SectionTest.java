package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static wooteco.subway.Fixtures.STATION_3;
import static wooteco.subway.Fixtures.STATION_1;
import static wooteco.subway.Fixtures.STATION_2;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class SectionTest {

    @Test
    @DisplayName("상행, 하행 역이 같은 경우 예외를 발생시킨다.")
    void exceptionSameStationId() {
        assertThatThrownBy(() -> new Section(new Station(1L, STATION_1), new Station(1L, STATION_1), 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("상행, 하행 역은 서로 달라야합니다.");
    }

    @Test
    @DisplayName("같은 상행 역 ID를 가지는 지 확인한다. - 참")
    void hasSameUpStationId_true() {
        // given
        final Section section1 = new Section(new Station(1L, STATION_1), new Station(2L, STATION_2), 10);
        final Section section2 = new Section(new Station(1L, STATION_1), new Station(3L, STATION_3), 10);

        // when
        final Boolean actual = section1.hasSameUpStation(section2);

        // then
        assertThat(actual).isTrue();
    }

    @Test
    @DisplayName("같은 상행 역 ID를 가지는 지 확인한다. - 거짓")
    void hasSameUpStationId_false() {
        // givne
        final Section section1 = new Section(new Station(1L, STATION_1), new Station(2L, STATION_2), 10);
        final Section section2 = new Section(new Station(2L, STATION_2), new Station(3L, STATION_3), 10);

        // when
        final Boolean actual = section1.hasSameUpStation(section2);

        // then
        assertThat(actual).isFalse();
    }
}
