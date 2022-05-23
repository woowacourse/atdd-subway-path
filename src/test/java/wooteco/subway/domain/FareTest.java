package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class FareTest {

    @DisplayName("운임에 따른 요금을 반환한다.")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @CsvSource(value = {"9,1250", "12,1350", "16,1450", "58,2150"})
    void getBaseFare(String distance, String resultFare) {
        PathFindingStrategy pathFindingStrategy = new TestPathFindingStrategy(Integer.parseInt(distance));
        Fare fare = new Fare(new DistanceFareStrategy());
        Path path = new Path(new Lines(List.of()), pathFindingStrategy, new Station("강남역"), new Station("선릉역"));
        assertThat(fare.calculate(path)).isEqualTo(Integer.parseInt(resultFare));
    }

    private class TestPathFindingStrategy implements PathFindingStrategy {
        private final int distance;

        public TestPathFindingStrategy(int distance) {
            this.distance = distance;
        }

        @Override
        public int getShortestDistance(Station source, Station target, Lines lines) {
            return distance;
        }

        @Override
        public List<Station> getShortestPath(Station source, Station target, Lines lines) {
            return null;
        }
    }
}
