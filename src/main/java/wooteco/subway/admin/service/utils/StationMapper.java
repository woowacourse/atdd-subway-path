package wooteco.subway.admin.service.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import wooteco.subway.admin.domain.Station;

public class StationMapper {
    public static Map<Long, Station> toMap(List<Station> stations) {
        return stations.stream()
                .collect(Collectors.toMap(Station::getId, Function.identity()));
    }
}
