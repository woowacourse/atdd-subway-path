package wooteco.subway.admin.util;

import java.util.Map;
import java.util.NoSuchElementException;

import wooteco.subway.admin.domain.Station;

public class StationsUtil {
    public static Station findStationWithValidation(Map<Long, Station> stations, Long stationId) {
        if (!stations.containsKey(stationId)) {
            throw new NoSuchElementException("등록되어있지 않은 역입니다.");
        }

        return stations.get(stationId);
    }
}
