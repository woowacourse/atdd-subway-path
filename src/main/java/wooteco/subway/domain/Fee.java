package wooteco.subway.domain;

import wooteco.subway.exception.ExceptionMessage;
import wooteco.subway.exception.domain.FeeException;

public class Fee {
    private final static Fee DEFAULT_FEE = new Fee(1250L);
    private static final long DEFAULT_FEE_AMOUNT = 1250;
    private static final int EXTRA_FEE_AMOUNT = 2050;
    private static final int MAXIMUM_DEFAULT_DISTANCE = 10;
    private static final int MAXIMUM_EXTRA_DISTANCE = 50;
    private static final long PER_UNIT_FEE_AMOUNT = 100L;
    private static final int DEFAULT_UNIT = 5;
    private static final int EXTRA_UNIT = 8;
    private static final int MINIMUM_DISTANCE = 0;

    private final Long feeAmount;

    public Fee(Long feeAmount) {
        this.feeAmount = feeAmount;
    }

    public static Fee from(int distance) {
        if (distance <= MINIMUM_DISTANCE) {
            throw new FeeException(ExceptionMessage.UNDER_MIN_DISTANCE.getContent());
        }
        if (distance <= MAXIMUM_DEFAULT_DISTANCE) {
            return DEFAULT_FEE;
        }
        if (distance <= MAXIMUM_EXTRA_DISTANCE) {
            return calculateFee(distance - MAXIMUM_DEFAULT_DISTANCE, DEFAULT_UNIT, DEFAULT_FEE_AMOUNT);
        }
        return calculateFee(distance - MAXIMUM_EXTRA_DISTANCE, EXTRA_UNIT, EXTRA_FEE_AMOUNT);
    }

    private static Fee calculateFee(int remainDistance, int unit, long baseFeeAmount) {
        int additionalFeeCount = remainDistance / unit;
        if (remainDistance % unit != MINIMUM_DISTANCE) {
            additionalFeeCount ++;
        }
        return new Fee(baseFeeAmount + additionalFeeCount * PER_UNIT_FEE_AMOUNT);
    }


    public Long getValue() {
        return feeAmount;
    }
}
