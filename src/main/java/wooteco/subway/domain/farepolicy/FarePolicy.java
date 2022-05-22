package wooteco.subway.domain.farepolicy;

@FunctionalInterface
public interface FarePolicy {

    static FarePolicy of(int age) {
        if (6 <= age && age < 13) {
            return new ChildrenPolicy();
        }
        if (13 <= age && age < 19) {
            return new TeenagerPolicy();
        }
        if (age < 6 || 65 <= age) {
            return new PreferentialPolicy();
        }
        return new BasicPolicy();
    }

    int calculate(int basicFare);
}
