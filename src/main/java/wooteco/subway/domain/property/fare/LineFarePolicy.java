package wooteco.subway.domain.property.fare;

import java.util.List;

import wooteco.subway.exception.UnexpectedException;

public class LineFarePolicy implements FarePolicy {

    private final List<Fare> extraFares;

    public LineFarePolicy(List<Fare> extraFares) {
        this.extraFares = extraFares;
    }

    @Override
    public Fare apply(Fare fare) {
        return fare.surcharge(extraFares.stream()
            .mapToInt(Fare::getAmount)
            .max()
            .orElseThrow(() -> new UnexpectedException("지나간 경로에 추가 금액이 존재하지 않습니다.")));
    }
}
