package wooteco.subway.domain;

import java.util.List;

public interface PathFinder {

    Path getShortestPath(List<Section> sections, long departureId, long arrivalId);
}
