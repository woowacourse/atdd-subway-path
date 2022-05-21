package wooteco.subway.domain.path.cost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CostManager {

    private static final int BASIC_FARE = 1250;

    private final List<CostSection> costSections;

    public CostManager(List<CostSection> costSections) {
        this.costSections = new ArrayList<>(costSections);
        Collections.sort(this.costSections);
    }

    public int calculateFare(int totalDistance) {
        if (totalDistance <= 0) {
            return 0;
        }
        int totalFare = 0;
        int lastIndex = costSections.size() - 1;
        for (int i = 0; i < lastIndex; i++) {
            CostSection current = costSections.get(i);
            CostSection next = costSections.get(i + 1);
            totalFare += Math.min(current.calculateOverFare(totalDistance), current.calculateMaxFare(next));
        }
        totalFare += costSections.get(lastIndex).calculateOverFare(totalDistance);
        return totalFare + BASIC_FARE;
    }
}
