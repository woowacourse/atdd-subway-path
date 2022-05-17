package wooteco.subway.domain;

import java.util.List;

public class Route {

    private final Sections sections;

    public Route(final Sections sections) {
        this.sections = sections;
    }

    public List<Station> calculateShortestPath(final Station source, final Station target) {
        SubwayGraph subwayGraph = initSubwayGraph();
        return subwayGraph.findShortestPath(source, target);
    }

    public double calculateShortestDistance(final Station source, final Station target) {
        SubwayGraph subwayGraph = initSubwayGraph();
        return subwayGraph.findShortestDistance(source, target);
    }

    private SubwayGraph initSubwayGraph() {
        SubwayGraph subwayGraph = new SubwayGraph();
        subwayGraph.init(sections);
        return subwayGraph;
    }
}
