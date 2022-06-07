package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DistanceFareTest {
    @ParameterizedTest(name = "{0}을 입력하면 기본 거리 요금을 측정한다.")
    @ValueSource(ints = {9, 10, 1})
    void DistanceFareBasic(int distance) {
        //when
        DistanceFare distanceFare = DistanceFare.valueOf(distance);
        //then
        assertThat(distanceFare).isEqualTo(DistanceFare.BASIC_DISTANCE);
    }

    @ParameterizedTest(name = "{0}을 입력하면 중거리 요금을 측정한다.")
    @ValueSource(ints = {11, 25, 50})
    void DistanceFareMiddle(int distance) {
        //when
        DistanceFare distanceFare = DistanceFare.valueOf(distance);
        //then
        assertThat(distanceFare).isEqualTo(DistanceFare.MIDDLE_DISTANCE);
    }

    @ParameterizedTest(name = "{0}을 입력하면 장거리 요금을 측정한다.")
    @ValueSource(ints = {51, 70})
    void DistanceFareLong(int distance) {
        //when
        DistanceFare distanceFare = DistanceFare.valueOf(distance);
        //then
        assertThat(distanceFare).isEqualTo(DistanceFare.LONG_DISTANCE);
    }

    @ParameterizedTest(name = "{0}을 입력하면 에러를 발생시킨다.")
    @ValueSource(ints = {0, -1})
    void DistanceFareError(int distance) {
        assertThatThrownBy(() -> DistanceFare.valueOf(distance))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("입력받은 거리값에 따른 요금 정책을 찾을 수 없습니다.");
    }
}