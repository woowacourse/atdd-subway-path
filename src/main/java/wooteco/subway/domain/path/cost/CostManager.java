package wooteco.subway.domain.path.cost;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CostManager {

    private static final int BASIC_FARE = 1250;

    private final List<CostSection> costSections;

    public CostManager(List<CostSection> costSections) {
        validateEmptySections(costSections);
        this.costSections = new ArrayList<>(costSections);
        Collections.sort(this.costSections);
    }

    private void validateEmptySections(List<CostSection> costSections) {
        if (costSections.isEmpty()) {
            throw new IllegalArgumentException();
        }
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
            totalFare += current.calculateFareWithBound(next, totalDistance);
        }
        CostSection lastSection = costSections.get(lastIndex);
        totalFare += lastSection.calculateFareWithBound(CostSection.ofInfinity(), totalDistance);
        return totalFare + BASIC_FARE;
    }
}
