package wooteco.subway.domain.property.fare;

public interface FarePolicy {
    int apply(int fare);
}
