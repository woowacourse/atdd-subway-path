package wooteco.subway.domain.path;

import java.util.List;

import wooteco.subway.domain.property.Distance;
import wooteco.subway.domain.property.fare.Fare;
import wooteco.subway.domain.section.Section;

public interface PathFinder {
    List<Section> getSections();

    List<Fare> getPassingFares();

    Distance getDistance();
}
