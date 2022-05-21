package wooteco.subway.domain.path.fare;

public abstract class Decorator implements Fare {

    protected final Fare target;

    protected Decorator(Fare target) {
        this.target = target;
    }

    protected int delegate() {
        return target.calculate();
    }
}
