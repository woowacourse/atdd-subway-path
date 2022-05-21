package wooteco.subway.exception.section;

import wooteco.subway.exception.InvalidInputException;

public class SectionNotFoundException extends InvalidInputException {

    private static final String MESSAGE = "존재하지 않는 구간입니다.";

    public SectionNotFoundException() {
        super(MESSAGE);
    }

}
