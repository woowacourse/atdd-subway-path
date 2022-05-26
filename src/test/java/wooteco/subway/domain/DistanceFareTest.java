package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceFareTest {
    @ParameterizedTest
    @ValueSource(ints = {9, 10, 1})
    @DisplayName("10km 이하면 기본 거리 요금을 측정한다.")
    void DistanceFareBasic(int distance){
        //when
        DistanceFare distanceFare = DistanceFare.from(distance);
        //then
        assertThat(distanceFare).isEqualTo(DistanceFare.BASIC_DISTANCE);
    }

    @ParameterizedTest
    @ValueSource(ints = {11, 25, 50})
    @DisplayName("10km 초과, 50km 이하면 중거리 요금을 측정한다.")
    void DistanceFareMiddle(int distance){
        //when
        DistanceFare distanceFare = DistanceFare.from(distance);
        //then
        assertThat(distanceFare).isEqualTo(DistanceFare.MIDDLE_DISTANCE);
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 70})
    @DisplayName("50km 초과면 장거리 요금을 측정한다.")
    void DistanceFareLong(int distance){
        //when
        DistanceFare distanceFare = DistanceFare.from(distance);
        //then
        assertThat(distanceFare).isEqualTo(DistanceFare.LONG_DISTANCE);
    }
}