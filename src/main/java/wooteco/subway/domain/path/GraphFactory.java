package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.element.Section;

public class GraphFactory {
    public static Graph getGraph(List<Section> sections) {
        return new SubwayGraph(sections);
    }
}
