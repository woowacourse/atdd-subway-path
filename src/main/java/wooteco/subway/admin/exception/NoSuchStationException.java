package wooteco.subway.admin.exception;

import com.google.common.base.Strings;

public class NoSuchStationException extends RuntimeException {
    private static final String MESSAGE = "%s역이 존재하지 않습니다.";

    public NoSuchStationException() {
        this(Strings.nullToEmpty(null));
    }

    public NoSuchStationException(String s) {
        super(String.format(MESSAGE, s));
    }

    public NoSuchStationException(Exception e) {
        super(e);
    }
}
