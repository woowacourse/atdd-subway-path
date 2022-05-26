package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class DistanceOverFarePolicyTest {

    @Test
    void 이동거리가_10_초과인_경우_5km_단위마다_최대_100원씩_추가비용() {
        DistanceOverFarePolicy distancePolicy = DistanceOverFarePolicy.OVER_TEN;

        int actual = distancePolicy.toOverFare(18);
        int expected = 200;

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"10,0", "11,100", "15,100", "50,800"})
    void 경계값_검증_10초과_50이하(int input, int expected) {
        DistanceOverFarePolicy distancePolicy = DistanceOverFarePolicy.OVER_TEN;

        int actual = distancePolicy.toOverFare(input);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 이동거리가_50_초과인_경우_50을_초과한_거리에_대해_8km_단위마다_최대_100원씩_추가비용() {
        DistanceOverFarePolicy distancePolicy = DistanceOverFarePolicy.OVER_FIFTY;

        int actual = distancePolicy.toOverFare(60);
        int expected = 200;

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"50,0", "51,100", "58,100", "59,200"})
    void 경계값_검증_50초과(int input, int expected) {
        DistanceOverFarePolicy distancePolicy = DistanceOverFarePolicy.OVER_FIFTY;

        int actual = distancePolicy.toOverFare(input);

        assertThat(actual).isEqualTo(expected);
    }
}
