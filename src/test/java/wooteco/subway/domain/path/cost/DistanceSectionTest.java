package wooteco.subway.domain.path.cost;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

public class DistanceSectionTest {

    @ParameterizedTest
    @ValueSource(ints = {1,10})
    void 기본요금만_내야하는_거리인_경우(int dist) {
        assertThat(DistanceSection.calculateByDistance(dist)).isEqualTo(1250);
    }

    @Test
    void 운행하지_않은_경우() {
        assertThat(DistanceSection.calculateByDistance(0)).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource(value = {"11 : 1350", "50 : 2050"}, delimiter = ':')
    void 첫번째_할증구간인_10km_초과_50km_이하인_거리인_경우(int dist, int fare) {
        assertThat(DistanceSection.calculateByDistance(dist)).isEqualTo(fare);
    }

    @Test
    void 두번째_할증구간인_50km_초과인_거리인_경우() {
        assertThat(DistanceSection.calculateByDistance(51)).isEqualTo(2150);
    }
}
