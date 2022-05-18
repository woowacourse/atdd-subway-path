package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @DisplayName("운임에 따른 요금을 반환한다.")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @CsvSource(value = {"9,1250", "12,1350", "16,1450", "58,2150"})
    void getBaseFare(String distance, String resultFare) {
        Fare fare = new Fare(new DistanceFareStrategy());
        assertThat(fare.calculate(Integer.parseInt(distance))).isEqualTo(Integer.parseInt(resultFare));
    }
}
