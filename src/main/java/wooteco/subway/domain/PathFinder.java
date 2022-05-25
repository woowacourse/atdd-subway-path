package wooteco.subway.domain;

import java.util.List;

public interface PathFinder {
    Path findShortestPathByGraph(Station source, Station target);

    void addVertex(List<Station> stations);

    void addEdge(Station upStation, Station downStation, Section section);
}
