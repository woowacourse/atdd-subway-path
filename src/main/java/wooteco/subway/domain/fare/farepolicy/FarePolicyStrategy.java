package wooteco.subway.domain.fare.farepolicy;

interface FarePolicyStrategy {
    int calculateFare(int distance);
}
