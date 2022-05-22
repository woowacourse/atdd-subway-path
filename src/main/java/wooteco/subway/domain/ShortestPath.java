package wooteco.subway.domain;

import java.util.List;

public class ShortestPath {

    private final List<Station> vertexes;
    private final Sections sections;
    private final int distance;

    public ShortestPath(final List<Station> vertexes, final Sections sections, final int distance) {
        this.vertexes = vertexes;
        this.sections = sections;
        this.distance = distance;
    }

    public List<Station> getVertexes() {
        return vertexes;
    }

    public Sections getSections() {
        return sections;
    }

    public int getDistance() {
        return distance;
    }
}
