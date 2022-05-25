package wooteco.subway.domain.fare;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Section;

public class Fare {

    private static final int BASE_FARE = 1250;

    private final int distance;
    private final int age;

    public Fare(int distance, int age) {
        this.distance = distance;
        this.age = age;
    }

    public int calculateFare(List<Long> path, List<Section> sections, List<Line> lines) {
        int extraFare = getMostExpensiveExtraFare(path, sections, lines);

        int fare = BASE_FARE
                + extraFare
                + calculateFirstExtraFare(distance - 10)
                + calculateSecondExtraFare(distance - 50);

        return AgeDiscountMapper.discount(age, fare);
    }

    private int getMostExpensiveExtraFare(List<Long> path, List<Section> sections, List<Line> lines) {
        List<Long> lineIds = sections.stream()
                .filter(it -> path.contains(it.getUpStationId())
                        || path.contains(it.getDownStationId()))
                .map(Section::getLineId)
                .collect(Collectors.toList());

        return lines.stream()
                .filter(it -> lineIds.contains(it.getId()))
                .distinct()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    private int calculateFirstExtraFare(int distanceInFirstRange) {
        if (distanceInFirstRange <= 0) {
            return 0;
        }
        return ((distanceInFirstRange - 1) / 5 + 1) * 100;
    }

    private int calculateSecondExtraFare(int distanceInSecondRange) {
        if (distanceInSecondRange <= 0) {
            return 0;
        }
        return ((distanceInSecondRange - 1) / 8 + 1) * 100;
    }
}
