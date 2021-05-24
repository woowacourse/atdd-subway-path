package wooteco.subway.auth.domain;

import wooteco.subway.auth.application.AuthorizedException;

import java.util.Objects;

public class AuthorizationPayLoad {

    private final String payLoad;

    public AuthorizationPayLoad(final String payLoad) {
        if (Objects.isNull(payLoad)) {
            throw new AuthorizedException("payLoad는 null일 수 없습니다.");
        }
        this.payLoad = payLoad;
    }

    public String value() {
        return payLoad;
    }
}
