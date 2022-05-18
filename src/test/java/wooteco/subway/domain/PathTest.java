package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

@SuppressWarnings("NonAsciiCharacters")
class PathTest {

    @ParameterizedTest(name = "거리가 {0}일 때, 요금이 {1}원이다.")
    @CsvSource(value = {"10,1250", "50,2050", "90,2550"})
    void 요금을_계산한다(int distance, int fare) {
        Station 강남역 = new Station("강남역");
        Station 선릉역 = new Station("선릉역");

        Path path = new Path(List.of(강남역, 선릉역), distance);

        assertThat(path.calculateFare()).isEqualTo(fare);
    }
}
