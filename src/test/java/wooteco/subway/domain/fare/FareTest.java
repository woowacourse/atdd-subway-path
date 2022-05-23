package wooteco.subway.domain.fare;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Lines;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathFindingStrategy;

public class FareTest {

    Section section = new Section(new Station("강남역"), new Station("선릉역"), 10);
    Line line1 = new Line(1L, "2호선", "green", 900, new Sections(List.of(section)));
    Line line2 = new Line(2L, "3호선", "green", 500, new Sections(List.of(section)));
    Line line3 = new Line(3L, "4호선", "green", 0, new Sections(List.of(section)));

    @DisplayName("운임에 따른 요금을 반환한다(19세 이상)")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @CsvSource(value = {"9,2150", "12,2250", "16,2350", "58,3050"})
    void getFareOver19(String distance, String resultFare) {
        PathFindingStrategy pathFindingStrategy = new TestPathFindingStrategy(Integer.parseInt(distance),
            List.of(1L, 2L, 3L));
        Fare fare = new Fare(new DistanceFareStrategy(), new AgeDiscountStrategy());
        Path path = new Path(new Lines(List.of(line1, line2)), pathFindingStrategy, new Station("강남역"),
            new Station("선릉역"));
        assertThat(fare.calculate(path, 19)).isEqualTo(Integer.parseInt(resultFare));
    }

    @DisplayName("운임에 따른 요금을 반환한다(13세 이상 19세 미만)")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @CsvSource(value = {"9,1440", "12,1520", "16,1600", "58,2160"})
    void getFareOver13Under19(String distance, String resultFare) {
        PathFindingStrategy pathFindingStrategy = new TestPathFindingStrategy(Integer.parseInt(distance),
            List.of(1L, 2L, 3L));
        Fare fare = new Fare(new DistanceFareStrategy(), new AgeDiscountStrategy());
        Path path = new Path(new Lines(List.of(line1, line2)), pathFindingStrategy, new Station("강남역"),
            new Station("선릉역"));
        assertThat(fare.calculate(path, 18)).isEqualTo(Integer.parseInt(resultFare));
    }

    @DisplayName("운임에 따른 요금을 반환한다(13세 이상 19세 미만)")
    @ParameterizedTest(name = "{displayName} : {arguments}")
    @CsvSource(value = {"9,900", "12,950", "16,1000", "58,1350"})
    void getFareOver6Under13(String distance, String resultFare) {
        PathFindingStrategy pathFindingStrategy = new TestPathFindingStrategy(Integer.parseInt(distance),
            List.of(1L, 2L, 3L));
        Fare fare = new Fare(new DistanceFareStrategy(), new AgeDiscountStrategy());
        Path path = new Path(new Lines(List.of(line1, line2)), pathFindingStrategy, new Station("강남역"),
            new Station("선릉역"));
        assertThat(fare.calculate(path, 6)).isEqualTo(Integer.parseInt(resultFare));
    }

    private class TestPathFindingStrategy implements PathFindingStrategy {
        private final int distance;
        private final List<Long> testIds;

        public TestPathFindingStrategy(int distance, List<Long> testIds) {
            this.distance = distance;
            this.testIds = testIds;
        }

        @Override
        public int getShortestDistance(Station source, Station target, Lines lines) {
            return distance;
        }

        @Override
        public List<Station> getShortestPath(Station source, Station target, Lines lines) {
            return null;
        }

        @Override
        public List<Long> getLineIds(Station source, Station target, Lines lines) {
            return testIds;
        }
    }
}
