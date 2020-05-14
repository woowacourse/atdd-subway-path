package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE((LineStation::getDistance), (LineStation::getDuration)),
    DURATION((LineStation::getDuration), (LineStation::getDistance));

    private Function<LineStation, Integer> getWeight;
    private Function<LineStation, Integer> getInformation;

    PathType(Function<LineStation, Integer> getWeight, Function<LineStation, Integer> getInformation) {
        this.getWeight = getWeight;
        this.getInformation = getInformation;
    }

    public int getGetWeight(LineStation lineStation) {
        return getWeight.apply(lineStation);
    }

    public int getGetInformation(LineStation lineStation) {
        return getInformation.apply(lineStation);
    }
}
