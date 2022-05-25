package wooteco.subway.domain.fare;

import java.util.Comparator;
import java.util.List;
import wooteco.subway.domain.Line;
import wooteco.subway.exception.CalculatePathsException;
import wooteco.subway.exception.datanotfound.LineNotFoundException;

public class FareCalculator {

    private final double distance;

    public FareCalculator(final double distance) {
        validateDistanceOverThanZero(distance);
        this.distance = distance;
    }

    private void validateDistanceOverThanZero(final double distance) {
        if (distance <= 0) {
            throw new CalculatePathsException("최단 경로의 거리가 0이하 이기 때문에 요금을 계산 할 수 없습니다.");
        }
    }

    public int calculateFare(final List<Line> lines, final int age) {
        int extraFare = findExtraFareToCharged(lines);
        int totalFare = FarePolicy.calculateFare(distance) + extraFare;
        return AgePolicy.calculateFareByAgePolicy(totalFare, age);
    }

    private int findExtraFareToCharged(final List<Line> lines) {
        return lines.stream()
                .max(Comparator.comparing(Line::getExtraFare))
                .orElseThrow(() -> new LineNotFoundException("존재하는 노선이 없습니다."))
                .getExtraFare();
    }
}
