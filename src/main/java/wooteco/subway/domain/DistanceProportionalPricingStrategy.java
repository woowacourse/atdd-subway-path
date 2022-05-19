package wooteco.subway.domain;

import java.util.List;

public class DistanceProportionalPricingStrategy implements PricingStrategy {

    private static final int BASIC_FEE = 1250;
    private static final int SECOND_CONDITION_START_DISTANCE = 50;
    private static final int SECOND_CONDITION_DISTANCE_UNIT = 8;
    private static final int FIRST_CONDITION_START_DISTANCE = 10;
    private static final int FIRST_CONDITION_DISTANCE_UNIT = 5;
    private static final int UNIT_MONEY = 100;

    @Override
    public int calculateFee(List<Section> sections) {
        int distance = calculateDistance(sections);
        if (distance > SECOND_CONDITION_START_DISTANCE) {
            return BASIC_FEE + calculateCost(distance - SECOND_CONDITION_START_DISTANCE, SECOND_CONDITION_DISTANCE_UNIT)
                    + calculateCost(SECOND_CONDITION_START_DISTANCE - FIRST_CONDITION_START_DISTANCE, FIRST_CONDITION_DISTANCE_UNIT);
        }
        if (distance > FIRST_CONDITION_START_DISTANCE) {
            return BASIC_FEE + calculateCost(distance - FIRST_CONDITION_START_DISTANCE, FIRST_CONDITION_DISTANCE_UNIT);
        }
        return BASIC_FEE;
    }

    private int calculateCost(int distance, int unit) {
        return ((distance - 1) / unit + 1) * UNIT_MONEY;
    }

    private int calculateDistance(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0, Integer::sum);
    }
}
