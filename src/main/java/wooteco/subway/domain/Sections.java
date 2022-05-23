package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;
import wooteco.subway.domain.util.PathFinderByJgrapht;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Path findShortestPath(Long source, Long target) {
        return new PathFinderByJgrapht(sections).findShortestPath(source, target);
    }
}
