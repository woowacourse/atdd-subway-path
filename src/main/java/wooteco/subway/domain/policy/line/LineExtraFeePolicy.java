package wooteco.subway.domain.policy.line;

import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import wooteco.subway.domain.element.Line;
import wooteco.subway.domain.policy.FarePolicy;

public class LineExtraFeePolicy implements FarePolicy {
    private final List<Line> lines;

    public LineExtraFeePolicy(List<Line> lines) {
        this.lines = lines;
    }

    @Override
    public double calculate(double baseFare) {
        return baseFare + getMaxFeeLine(lines).getExtraFare();
    }

    private Line getMaxFeeLine(List<Line> lines) {
        return lines.stream()
                .max(Comparator.comparingDouble(Line::getExtraFare))
                .orElseThrow(NoSuchElementException::new);
    }
}
