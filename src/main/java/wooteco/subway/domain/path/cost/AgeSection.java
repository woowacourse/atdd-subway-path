package wooteco.subway.domain.path.cost;

import java.util.Arrays;
import org.springframework.dao.DuplicateKeyException;

public enum AgeSection {

    CHILD(6, 12, 350, 0.5),
    ADOLESCENCE(13, 18, 350, 0.8),
    ADULT(19, 200, 0, 1.0);

    private final int lowerBound;
    private final int upperBound;
    private final int discountValue;
    private final double discountRatio;

    AgeSection(int lowerBound, int upperBound, int discountValue, double discountRatio) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.discountValue = discountValue;
        this.discountRatio = discountRatio;
    }

    public static int calculateByAge(int fare, int age) {
        AgeSection ageSection = Arrays.stream(AgeSection.values())
                .filter(eachAge -> eachAge.lowerBound <= age && age <= eachAge.upperBound)
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
        return (int) ((fare - ageSection.discountValue) * ageSection.discountRatio);
    }
}
