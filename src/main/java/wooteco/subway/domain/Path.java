package wooteco.subway.domain;

import java.util.List;
import wooteco.subway.util.NullChecker;

public class Path {

    private final List<Station> stationsInPath;
    private final List<Section> sectionsInPath;
    private final int distance;

    public Path(List<Station> stationsInPath, List<Section> sectionsInPath, int distance) {
        NullChecker.validateNonNull(stationsInPath, sectionsInPath);
        this.stationsInPath = stationsInPath;
        this.sectionsInPath = sectionsInPath;
        this.distance = distance;
    }

    public List<Station> getStationsInPath() {
        return stationsInPath;
    }

    public List<Section> getSectionsInPath() {
        return sectionsInPath;
    }

    public int getDistance() {
        return distance;
    }
}
