package wooteco.subway.util;

import java.util.Objects;

public class NullChecker {

    private static final String NULL_FIELD_EXCEPTION = "객체의 필드에 null이 주입되었습니다.";

    public static void validateNonNull(Object... objects) {
        for (Object obj : objects) {
            checkNull(obj);
        }
    }

    private static void checkNull(Object object) {
        if (Objects.isNull(object)) {
            throw new IllegalArgumentException(NULL_FIELD_EXCEPTION);
        }
    }
}
