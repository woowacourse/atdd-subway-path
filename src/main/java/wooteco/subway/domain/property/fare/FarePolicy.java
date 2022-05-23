package wooteco.subway.domain.property.fare;

public interface FarePolicy {
    Fare apply(Fare fare);
}
