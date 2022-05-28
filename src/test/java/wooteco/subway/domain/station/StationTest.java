package wooteco.subway.domain.station;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@DisplayName("지하철역")
class StationTest {

    private static final String STATION_NAME = "강남역";

    @DisplayName("식별자가 지정되지 않으면 임시값을 할당한다.")
    @Test
    void createWithoutId() {
        long actual = (new Station(STATION_NAME)).getId();
        assertThat(actual).isEqualTo(0L);
    }

    @DisplayName("식별자를 비교한다.")
    @ParameterizedTest
    @CsvSource(value = {"1,1,true", "1,2,false"})
    void equals(long id1, long id2, boolean expected) {
        Station thisStation = new Station(id1, STATION_NAME);
        Station otherStation = new Station(id2, STATION_NAME);

        boolean actual = thisStation.equals(otherStation);
        assertThat(actual).isEqualTo(expected);
    }
}
