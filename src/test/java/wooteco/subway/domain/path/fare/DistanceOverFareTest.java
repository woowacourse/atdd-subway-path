package wooteco.subway.domain.path.fare;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class DistanceOverFareTest {

    @Test
    void 거리가_10_이하인_경우_추가비용은_0원() {
        DistanceOverFare actual = DistanceOverFare.of(5);
        DistanceOverFare expected = new DistanceOverFare(0);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 거리가_10_초과인_경우_5km_단위마다_최대_100원씩_추가비용() {
        DistanceOverFare actual = DistanceOverFare.of(18);
        DistanceOverFare expected = new DistanceOverFare(200);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 거리가_50_초과인_경우_50을_초과한_거리에_대해_8km_단위마다_최대_100원씩_추가비용() {
        DistanceOverFare actual = DistanceOverFare.of(60);
        DistanceOverFare expected = new DistanceOverFare(1000);

        assertThat(actual).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"10,0", "11,100", "15,100", "16,200", "50,800", "51,900", "58,900", "59,1000"})
    void 경계값_검증(int input, int output) {
        DistanceOverFare actual = DistanceOverFare.of(input);
        DistanceOverFare expected = new DistanceOverFare(output);

        assertThat(actual).isEqualTo(expected);
    }
}
