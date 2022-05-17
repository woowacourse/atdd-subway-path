package wooteco.subway.domain;

import wooteco.subway.exception.IllegalInputException;

public class SubwayMap {

    private final Sections sections;

    public SubwayMap(final Sections sections) {
        this.sections = sections;
    }

    public void getPath(final Station sourceStation, final Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new IllegalInputException("출발역과 도착역이 동일합니다.");
        }
    }
}
