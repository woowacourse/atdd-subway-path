package wooteco.subway.domain.fare.farestrategy;

public interface ChargeStrategy {
    long calculate(long fare);
}
