package wooteco.subway.path.domain;

import wooteco.subway.line.domain.Section;
import wooteco.subway.station.domain.Station;

import java.util.List;

public class PathGraph {
    private final PathGraphAlgorithm graphAlgorithm;

    public PathGraph(PathGraphAlgorithm graphAlgorithm, List<Section> sections) {
        this.graphAlgorithm = graphAlgorithm;
        sections.forEach(
                section -> {
                    add(section.getUpStation().getId(), section.getDownStation().getId(), section.getDistance());
                });
    }

    public void add(Long upStation, Long downStation, int distance) {
        graphAlgorithm.add(upStation, downStation, distance);
    }

    public List<Station> getShortestPath(Long from, Long to) {
        return graphAlgorithm.getShortestPath(from, to);
    }

    public int getShortestDistance(Long from, Long to) {
        return graphAlgorithm.getShortestDistance(from, to);
    }
}
