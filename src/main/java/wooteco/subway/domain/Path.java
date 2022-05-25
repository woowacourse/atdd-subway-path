package wooteco.subway.domain;

import java.util.List;

public interface Path {

    List<Long> getShortestPathStationIds(long departureId, long arrivalId);

    int getShortestPathDistance(long departureId, long arrivalId);

    List<Long> getShortestPathLineIds(long departureId, long arrivalId);
}
