package wooteco.subway.path.dto;

import java.util.Map;
import javax.validation.constraints.NotNull;
import wooteco.subway.line.domain.Path;
import wooteco.subway.station.domain.Station;

public class PathFindRequest {
    @NotNull
    private Long source;
    @NotNull
    private Long target;

    public PathFindRequest(Long source, Long targetStationId) {
        this.source = source;
        this.target = targetStationId;
    }

    public Long getSource() {
        return source;
    }

    public Long getTarget() {
        return target;
    }

    public void setSource(Long source) {
        this.source = source;
    }

    public void setTarget(Long target) {
        this.target = target;
    }

    public boolean isAllStationInMap(Map<Long, Station> stationMap) {
        return stationMap.containsKey(source) && stationMap.containsKey(target);
    }

    public Path toPath() {
        return new Path(source, target);
    }
}
