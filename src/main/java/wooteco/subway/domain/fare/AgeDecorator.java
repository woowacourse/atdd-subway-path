package wooteco.subway.domain.fare;

public class AgeDecorator extends Decorator {

    private final int age;

    public AgeDecorator(final Fare fare, final int age) {
        super(fare);
        this.age = age;
    }

    @Override
    public double calculateExtraFare() {
        double price = super.calculateExtraFare();
        return AgeFare.valueOf(age, price);
    }
}
