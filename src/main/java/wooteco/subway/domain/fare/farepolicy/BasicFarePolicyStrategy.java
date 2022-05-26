package wooteco.subway.domain.fare.farepolicy;

class BasicFarePolicyStrategy implements FarePolicyStrategy {

    @Override
    public int calculateFare(int distance) {
        return 1250;
    }
}
