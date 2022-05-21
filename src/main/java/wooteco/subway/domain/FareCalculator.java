package wooteco.subway.domain;

import wooteco.subway.exception.NotFoundException;

import java.util.List;

public class FareCalculator {

    private static final int BASIS_FARE = 1_250;
    private static final int BASIC_FARE_OVER_50KM = 2_050;
    private static final int FIRST_FARE_INCREASE_STANDARD = 10;
    private static final int LAST_FARE_INCREASE_STANDARD = 50;
    private static final int FIRST_FARE_INCREASE_STANDARD_UNIT = 5;
    private static final int LAST_FARE_INCREASE_STANDARD_UNIT = 8;
    private static final int INCREASE_RATE = 100;

    private final List<Line> lines;
    private final List<Section> sections;

    public FareCalculator(List<Line> lines, List<Section> sections) {
        this.lines = lines;
        this.sections = sections;
    }

    public int calculateFare(List<Long> pathStationIds, int distance, int age) {
        int basicFare = calculateBasicFare(distance);
        int extraLineFare = findExtraLineFare(pathStationIds);
        AgeDiscountPolicy ageDiscountPolicy = AgeDiscountPolicy.findAgePolicy(age);
        return ageDiscountPolicy.fareDiscount(basicFare + extraLineFare);
    }

    private int calculateBasicFare(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException();
        }
        if (distance <= FIRST_FARE_INCREASE_STANDARD) {
            return BASIS_FARE;
        }
        if (distance <= LAST_FARE_INCREASE_STANDARD) {
            return BASIS_FARE + INCREASE_RATE *
                    (int) Math.ceil((double) (distance - FIRST_FARE_INCREASE_STANDARD) / FIRST_FARE_INCREASE_STANDARD_UNIT);
        }
        return BASIC_FARE_OVER_50KM + INCREASE_RATE *
                (int) Math.ceil((double) (distance - LAST_FARE_INCREASE_STANDARD) / LAST_FARE_INCREASE_STANDARD_UNIT);
    }

    private int findExtraLineFare(List<Long> pathStationIds) {
        int maxFare = 0;
        for (int i = 0; i < pathStationIds.size() -1; i++) {
            Long previousStation = pathStationIds.get(i);
            Long nextStation = pathStationIds.get(i + 1);
            int fare = findMinimumSectionFare(previousStation, nextStation);
            maxFare = updateMaxFare(maxFare, fare);
        }
        return maxFare;
    }

    private int findMinimumSectionFare(Long previousStation, Long nextStation) {
        return sections.stream()
                .filter(s -> s.hasStation(previousStation) && s.hasStation(nextStation))
                .mapToInt(s -> findLineExtraFare(s.getLineId()))
                .min()
                .orElseThrow(() -> new NotFoundException("구간의 추가 요금을 찾지 못하였습니다."));
    }

    private int findLineExtraFare(Long lineId) {
        return lines.stream()
                .filter(l -> l.isSameLine(lineId))
                .mapToInt(Line::getExtraFare)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("노선의 추가 요금을 찾지 못하였습니다."));
    }

    private int updateMaxFare(int maxFare, int fare) {
        if (fare > maxFare) {
            maxFare = fare;
        }
        return maxFare;
    }
}
