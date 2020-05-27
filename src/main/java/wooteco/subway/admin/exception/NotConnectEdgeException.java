package wooteco.subway.admin.exception;

import wooteco.subway.admin.domain.Station;

public class NotConnectEdgeException extends RuntimeException {
    public NotConnectEdgeException(final Station startStation, final Station endStation) {
        super(startStation.getName() + "으로부터 " + endStation.getName() + "까지 연결되어 있지 않습니다!");
    }
}
