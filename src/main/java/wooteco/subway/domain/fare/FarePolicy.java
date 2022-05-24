package wooteco.subway.domain.fare;

public abstract class FarePolicy implements Fare {

    protected final Fare target;

    protected FarePolicy(Fare target) {
        this.target = target;
    }

    protected int delegate() {
        return target.calculate();
    }
}
