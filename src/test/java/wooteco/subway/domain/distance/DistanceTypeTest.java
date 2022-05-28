package wooteco.subway.domain.distance;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.distance.DistanceType.BASE_DISTANCE;
import static wooteco.subway.domain.distance.DistanceType.LONG_DISTANCE;
import static wooteco.subway.domain.distance.DistanceType.MIDDLE_DISTANCE;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class DistanceTypeTest {

    @ParameterizedTest
    @ValueSource(ints = {1, 3, 10})
    @DisplayName("10km 이내 거리이면 BASE_DISTANCE 이다.")
    void From_LessThan10_BaseDistanceReturned(int distanceValue) {
        //given
        Distance distance = new Distance(distanceValue);

        //when
        DistanceType actual = DistanceType.from(distance);

        //then
        assertThat(actual).isEqualTo(BASE_DISTANCE);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 33, 49, 50})
    @DisplayName("10km ~ 50km 사이의 거리이면 MIDDLE_DISTANCE 이다.")
    void From_MoreThan10LessThan50_MiddleDistanceReturned(int distanceValue) {
        //given
        Distance distance = new Distance(distanceValue);

        //when
        DistanceType actual = DistanceType.from(distance);

        //then
        assertThat(actual).isEqualTo(MIDDLE_DISTANCE);
    }


    @ParameterizedTest
    @ValueSource(ints = {51, 60})
    @DisplayName("50km 초과 거리이면 LONG_DISTANCE 이다.")
    void From_MoreThan50_LongDistanceReturned(int distanceValue) {
        //given
        Distance distance = new Distance(distanceValue);

        //when
        DistanceType actual = DistanceType.from(distance);

        //then
        assertThat(actual).isEqualTo(LONG_DISTANCE);
    }
}
