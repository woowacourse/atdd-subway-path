package wooteco.subway.domain.path.cost;

public class CostSection implements Comparable<CostSection> {

    private final int threshold;
    private final int delimiter;
    private final int fare;

    public CostSection(int threshold, int delimiter, int fare) {
        this.threshold = threshold;
        this.delimiter = delimiter;
        this.fare = fare;
    }

    public int calculateOverFare(int nowDistance) {
        int faredDistance = nowDistance - threshold;
        if (faredDistance <= 0) {
            return 0;
        }
        return (int) ((Math.ceil((faredDistance - 1) / delimiter) + 1) * fare);
    }

    public int calculateMaxFare(CostSection nextSection) {
        return (int) Math.ceil(((nextSection.threshold - threshold) / delimiter) * fare);
    }

    @Override
    public int compareTo(CostSection o) {
        return Integer.compare(threshold, o.threshold);
    }
}
