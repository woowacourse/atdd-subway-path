package wooteco.subway.domain.policy;

import java.util.List;
import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.DiscountPolicyException;

public class DiscountPolicyFactory {

    private static final List<DiscountPolicy> discountPolicies = List.of(
            new AdultPolicy(),
            new ChildrenPolicy(),
            new StudentsPolicy(),
            new InfantsPolicy()
    );

    public DiscountPolicy getDiscountPolicy(int age) {
        return discountPolicies.stream()
                .filter(discountPolicy -> discountPolicy.checkAgeRange(age))
                .findFirst()
                .orElseThrow(() -> new DiscountPolicyException(ExceptionMessage.UNDER_MIN_AGE.getContent()));
    }
}
