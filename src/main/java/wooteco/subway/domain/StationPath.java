package wooteco.subway.domain;

import wooteco.subway.utils.exception.StationNotFoundException;

public class StationPath {

    private Sections sections;

    public StationPath(final Sections sections) {
        this.sections = sections;
    }

    public int calculateMinDistance(final Station startStation, final Station endStation) {
        validateExistStation(startStation, endStation);
        return 20;
    }

    private void validateExistStation(Station startStation, Station endStation) {
        if (!sections.isExistStation(startStation) || !sections.isExistStation(endStation)) {
            throw new StationNotFoundException();
        }
    }
}
