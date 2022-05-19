package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @DisplayName("운임에 따른 요금을 반환한다.")
    @ParameterizedTest(name = "{displayName} : {0} km, {1} 원")
    @CsvSource(value = {"1,1250", "10,1250", "11,1350", "50,2050", "51,2150", "59,2250"})
    void getBaseFare(String distance, String resultFare) {
        Fare fare = new Fare(new DistanceFareStrategy());
        assertThat(fare.calculate(Integer.parseInt(distance))).isEqualTo(Integer.parseInt(resultFare));
    }
}
