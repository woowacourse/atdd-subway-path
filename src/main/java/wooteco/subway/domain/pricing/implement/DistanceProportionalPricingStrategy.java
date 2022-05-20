package wooteco.subway.domain.pricing.implement;

import java.util.List;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.pricing.PricingStrategy;

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

    // distance에서 unit거리만큼 UNIT_MONEY가 계속 추가요금으로 붙는다.
    // ex. distance: 1, unit: 5 -> 1*UNIT_MONEY가 추가요금
    // ex. distance: 5, unit: 5 일때 1*UNIT_MONEY가 추가요금
    // ex. distance: 6, unit: 5 일때 2*UNIT_MONEY가 추가요금
    private int calculateCost(int distance, int unit) {
        int additionalCostCount = 1;
        additionalCostCount += (distance - 1) / unit;
        return additionalCostCount * UNIT_MONEY;
    }

    private int calculateDistance(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0, Integer::sum);
    }
}
