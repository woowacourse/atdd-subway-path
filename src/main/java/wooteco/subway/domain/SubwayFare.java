package wooteco.subway.domain;

import java.util.List;
import org.jgrapht.GraphPath;

public class SubwayFare {

    private static final double FIVE_KM = 5.0;
    private static final double EIGHT_KM = 8.0;
    private static final int OVER_FEE = 100;
    private static final int FEE_BASE = 1250;
    private static final int FEE_50KM = 2050;
    private static final int BASE_FEE_DISTANCE = 10;
    private static final int OVER_FEE_DISTANCE = 50;
    private static final int DEFAULT_FEE = 0;

    private final GraphPath<Station, LineWeightedEdge> path;

    public SubwayFare(GraphPath<Station, LineWeightedEdge> path) {
        this.path = path;
    }

    public int calculate(int age) {
        AgeDiscountPolicy discountPolicy = AgeDiscountPolicy.find(age);
        int basicFare = calculateOverFare((int) path.getWeight());
        int extraFare = getExtraFare(path.getEdgeList());
        return discountPolicy.calculate(basicFare + extraFare);
    }

    private int getExtraFare(List<LineWeightedEdge> edges) {
        return edges.stream()
                .mapToInt(edge -> edge.getLine().getExtraFare())
                .filter(edge -> edge >= DEFAULT_FEE)
                .max()
                .orElse(DEFAULT_FEE);
    }

    private int calculateOverFare(int distance) {
        if (distance <= BASE_FEE_DISTANCE) {
            return FEE_BASE;
        }
        if (distance <= OVER_FEE_DISTANCE) {
            return (int) ((Math.ceil((distance - BASE_FEE_DISTANCE) / FIVE_KM)) * OVER_FEE) + FEE_BASE;
        }
        return (int) ((Math.ceil((distance - OVER_FEE_DISTANCE) / EIGHT_KM)) * OVER_FEE) + FEE_50KM;
    }
}
