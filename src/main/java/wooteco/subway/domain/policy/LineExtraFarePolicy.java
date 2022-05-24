package wooteco.subway.domain.policy;

import java.util.List;
import java.util.NoSuchElementException;

public class LineExtraFarePolicy implements FarePolicy {

    private final List<Integer> extraFares;

    public LineExtraFarePolicy(List<Integer> extraFares) {
        this.extraFares = extraFares;
    }

    @Override
    public int apply(int fare) {
        return fare + extraFares.stream()
            .mapToInt(extraFare -> extraFare)
            .max()
            .orElseThrow(() -> new NoSuchElementException("지나간 경로에 추가 요금이 존재하지 않습니다."));
    }
}
