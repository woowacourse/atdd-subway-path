package wooteco.subway.domain.pricing.implement;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import wooteco.subway.domain.FareCacluateSpecification;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.pricing.PricingStrategy;

public class LineAdditionalPricingStrategy implements PricingStrategy {

    @Override
    public int calculateFee(FareCacluateSpecification specification) {
        List<Line> linesInPath = selectLinesInPath(specification.getSections(), specification.lineMapById());
        return getExpensiveExtraFare(linesInPath);
    }

    private int getExpensiveExtraFare(List<Line> linesInPath) {
        return linesInPath.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElseThrow(() -> new IllegalArgumentException("경로가 존재하지 않아 요금을 계산할 수 없습니다."));
    }

    private List<Line> selectLinesInPath(List<Section> sections, Map<Long, Line> lineMapById) {
        return sections.stream()
                .map(it -> lineMapById.get(it.getLineId()))
                .distinct()
                .collect(Collectors.toList());
    }
}
