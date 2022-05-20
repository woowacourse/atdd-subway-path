package wooteco.subway.domain;

import java.util.List;

public interface Path {

    List<Long> getShortestPathStationIds(Long departureId, Long arrivalId);

    int getShortestPathDistance(Long departureId, Long arrivalId);
}
