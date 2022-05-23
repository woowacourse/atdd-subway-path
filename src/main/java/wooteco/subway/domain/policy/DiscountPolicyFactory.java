package wooteco.subway.domain.policy;

import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.DiscountPolicyException;

public class DiscountPolicyFactory {

    public DiscountPolicy getDiscountPolicy(int age) {
        if (age < 0) {
            throw new DiscountPolicyException(ExceptionMessage.UNDER_MIN_AGE.getContent());
        }
        if (age >= 13 && age < 19) {
            return new StudentsPolicy();
        }
        if (age >= 6 && age < 13) {
            return new ChildrenPolicy();
        }
        if (age < 6) {
            return new InfantsPolicy();
        }
        return new AdultPolicy();
    }
}
