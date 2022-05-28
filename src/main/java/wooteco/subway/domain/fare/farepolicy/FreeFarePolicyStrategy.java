package wooteco.subway.domain.fare.farepolicy;

class FreeFarePolicyStrategy implements FarePolicyStrategy {

    @Override
    public int calculateFare(int distance) {
        return 0;
    }
}
