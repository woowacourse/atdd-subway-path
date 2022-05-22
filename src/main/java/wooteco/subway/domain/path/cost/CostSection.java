package wooteco.subway.domain.path.cost;

public class CostSection implements Comparable<CostSection> {

    private static final int INFINITY = -1;

    private final int threshold;
    private final int delimiter;
    private final int fare;

    public CostSection(int threshold, int delimiter, int fare) {
        this.threshold = threshold;
        this.delimiter = delimiter;
        this.fare = fare;
    }

    static CostSection ofInfinity() {
        return new CostSection(INFINITY, 0, 0);
    }

    private int calculateOverFare(int nowDistance) {
        int faredDistance = nowDistance - threshold;
        if (faredDistance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((faredDistance - 1) / delimiter) + 1) * fare);
    }

    private int calculateMaxFare(CostSection nextSection) {
        return (int) Math.ceil(((nextSection.threshold - threshold) / delimiter) * fare);
    }

    public int calculateFareWithBound(CostSection nextSection, int distance) {
        if (nextSection.isInfinite()) {
            return calculateOverFare(distance);
        }
        return Math.min(calculateOverFare(distance), calculateMaxFare(nextSection));
    }

    private boolean isInfinite() {
        if (threshold == -1) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(CostSection o) {
        return Integer.compare(threshold, o.threshold);
    }
}
