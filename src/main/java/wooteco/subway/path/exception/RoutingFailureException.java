package wooteco.subway.path.exception;

import wooteco.subway.exception.DomainRelatedException;

public class RoutingFailureException extends DomainRelatedException {

    public RoutingFailureException(String description) {
        super(String.format("경로탐색에 실패했습니다. (%s)", description));
    }
}
