package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.exception.ClientException;

public class Path {

    private final List<Long> stationIds;
    private final List<Long> lineIds;
    private final int distance;

    public Path(final List<Long> stationIds, final List<Long> lineIds, final int distance) {
        validateStationIds(stationIds);
        validateLineIds(lineIds);
        validateDistance(distance);
        this.stationIds = stationIds;
        this.lineIds = lineIds;
        this.distance = distance;
    }

    private void validateStationIds(List<Long> stationIds) {
        if (stationIds.size() <= 0) {
            throw new ClientException("[ERROR] 경로의 역이 존재하지 않습니다.");
        }
    }

    private void validateLineIds(List<Long> lineIds) {
        if (lineIds.size() <= 0) {
            throw new ClientException("[ERROR] 경로가 지나가는 노선이 존재하지 않습니다.");
        }
    }

    private void validateDistance(int distance) {
        if (distance <= 0) {
            throw new ClientException("[ERROR] 경로의 길이가 0 이하 일 수 없습니다.");
        }
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public List<Long> getLineIds() {
        return lineIds;
    }

    public int getDistance() {
        return distance;
    }
}
