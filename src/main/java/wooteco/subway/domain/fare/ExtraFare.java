package wooteco.subway.domain.fare;

import java.util.List;

public final class ExtraFare extends FarePolicy {

    private static final String EXTRA_FARE_NOT_FOUND_EXCEPTION = "추가 요금 정보가 제공되지 않았습니다.";

    private final List<Integer> extraFares;

    public ExtraFare(Fare delegate, List<Integer> extraFares) {
        super(delegate);
        this.extraFares = extraFares;
    }

    @Override
    public int calculate() {
        int fare = super.delegate();
        int lineExtraFare = calculateMaxExtraFare();
        return fare + lineExtraFare;
    }

    private Integer calculateMaxExtraFare() {
        return extraFares.stream()
                .mapToInt(v -> v)
                .max()
                .orElseThrow(() -> new IllegalArgumentException(EXTRA_FARE_NOT_FOUND_EXCEPTION));
    }
}
