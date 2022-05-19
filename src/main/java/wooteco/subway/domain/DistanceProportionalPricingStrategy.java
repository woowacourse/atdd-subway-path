package wooteco.subway.domain;

import java.util.List;

public class DistanceProportionalPricingStrategy implements PricingStrategy {

    public static final int BASIC_FEE = 1250;
    public static final int SECOND_FEE_CONDITION = 50;
    public static final int SECOND_FEE_UNIT = 8;
    public static final int FIRST_FEE_CONDITION = 10;
    public static final int FIRST_FEE_UNIT = 5;
    public static final int UNIT_MONEY = 100;

    @Override
    public int calculateFee(List<Section> sections) {
        int distance = calculateDistance(sections);
        if (distance > SECOND_FEE_CONDITION) {
            return BASIC_FEE + calculateCost(distance - SECOND_FEE_CONDITION, SECOND_FEE_UNIT)
                    + calculateCost(SECOND_FEE_CONDITION - FIRST_FEE_CONDITION, FIRST_FEE_UNIT);
        }
        if (distance > FIRST_FEE_CONDITION) {
            return BASIC_FEE + calculateCost(distance - FIRST_FEE_CONDITION, FIRST_FEE_UNIT);
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
