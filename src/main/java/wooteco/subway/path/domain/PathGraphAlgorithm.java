package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public interface PathGraphAlgorithm {
    List<Station> getShortestPath(Long from, Long to);

    int getShortestDistance(Long from, Long to);

    void add(Long upStation, Long downStation, int distance);

    void updatePaths(List<Section> lines);
}
