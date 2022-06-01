package wooteco.subway.domain.path;

import static org.assertj.core.api.Assertions.assertThat;

import static wooteco.subway.domain.path.Fixture.강남;
import static wooteco.subway.domain.path.Fixture.강남_역삼_선릉;
import static wooteco.subway.domain.path.Fixture.선릉;
import static wooteco.subway.domain.path.Fixture.역삼;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PathTest {
    public static final int BASIC_FARE = 1250;
    private PathAlgorithm pathAlgorithm = new ShortestPath(강남_역삼_선릉);

    @Test
    @DisplayName("추가 요금이 있는 노선을 이용하면 추가 요금이 함께 부과된다.")
    void calculateFare_extra_100() {
        Path path = pathAlgorithm.getPath(강남, 역삼);
        Fare extraFare = path.calculateFare(new Age(20));

        assertThat(extraFare.getValue()).isEqualTo(BASIC_FARE + 100);
    }

    @Test
    @DisplayName("추가 요금이 있는 노선을 여러 개 이용하면 최대 추가 요금만 부과된다.")
    void calculateFare_extra_200() {
        Path path = pathAlgorithm.getPath(강남, 선릉);
        Fare extraFare = path.calculateFare(new Age(20));

        assertThat(extraFare.getValue()).isEqualTo(BASIC_FARE + 200);
    }
}
