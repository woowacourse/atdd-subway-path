package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Sections {

    private final List<Section> sections;

    public Sections(List<Section> sections) {
        this.sections = new ArrayList<>(sections);
    }

    public Path findShortestPath(PathFinder pathFinder, Long source, Long target) {
        return pathFinder.findShortestPath(sections, source, target);
    }
}
