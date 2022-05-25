package wooteco.subway.domain.fare;

import static java.util.Arrays.stream;

public enum AgeFarePolicy {

    CHILD_AGE_POLICY {
        @Override
        public boolean condition(int age) {
            return age >= 6 && age < 13;
        }

        @Override
        public int calculate(int amount) {
            return (int) ((amount - 350) * 0.5);
        }
    },

    YOUTH_AGE_POLICY {
        @Override
        public boolean condition(int age) {
            return age >= 13 && age < 19;
        }

        @Override
        public int calculate(int amount) {
            return (int) ((amount - 350) * 0.8);
        }
    },

    ADULT_AGE_POLICY {
        @Override
        public boolean condition(int age) {
            return age >= 19 || (age >= 0 && age < 6);
        }

        @Override
        public int calculate(int amount) {
            return amount;
        }
    };

    abstract public boolean condition(int age);

    abstract public int calculate(int amount);

    public static int calculateFare(int fare, int age) {
        return stream(values())
                .filter(AgeFarePolicy -> AgeFarePolicy.condition(age))
                .map(AgeFarePolicy -> AgeFarePolicy.calculate(fare))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("연령을 잘못 입력하였습니다."));
    }
}
