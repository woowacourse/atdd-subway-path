package wooteco.subway.domain;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private final List<Section> sections;

    public Route() {
        this.sections = new ArrayList<>();
    }

    public void addSections(final List<Section> sections) {
        this.sections.addAll(sections);
    }
}
