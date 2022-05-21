package wooteco.subway.domain.path.fare2;

import java.util.Comparator;
import java.util.List;
import wooteco.subway.domain.line.LineInfo;

public class LineOverFare extends Decorator {

    private static final String LINE_INFO_NOT_FOUND_EXCEPTION = "노선 정보가 제공되지 않았습니다.";
    private final List<LineInfo> lineInfos;

    public LineOverFare(Fare delegate, List<LineInfo> lineInfos) {
        super(delegate);
        this.lineInfos = lineInfos;
    }

    @Override
    public int calculate() {
        int fare = super.delegate();
        int lineExtraFare = calculateMaxExtraFare();
        return fare + lineExtraFare;
    }

    private Integer calculateMaxExtraFare() {
        return lineInfos.stream()
                .map(LineInfo::getExtraFare)
                .max(Comparator.comparingInt(Integer::intValue))
                .orElseThrow(() -> new IllegalArgumentException(LINE_INFO_NOT_FOUND_EXCEPTION));
    }
}
