package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StationsTest {

    @DisplayName("중복된 역들이 들어오면 예외를 반환한다.")
    @Test
    void notAllowDuplicateStations() {
        Station station = new Station("강남역");

        assertThatThrownBy(() -> new Stations(List.of(station, station)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 목록에 중복된 역이 있습니다.");
    }

    @DisplayName("생성자에 null이 들어오면 예외를 반환한다.")
    @Test
    void notAllowNullValues() {
        assertThatThrownBy(() -> new Stations(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 목록은 null일 수 없습니다.");
    }

    @DisplayName("역 목록에 null이 포함되면 예외를 반환한다.")
    @Test
    void notAllowNullStation() {
        List<Station> values = new ArrayList<>();
        values.add(null);

        assertThatThrownBy(() -> new Stations(values))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("역 목록은 null을 포함할 수 없습니다.");
    }

}
