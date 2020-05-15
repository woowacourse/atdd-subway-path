package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE((LineStation::getDistance), (LineStation::getDuration)),
    DURATION((LineStation::getDuration), (LineStation::getDistance));

    private Function<LineStation, Integer> getWeight;
    private Function<LineStation, Integer> getExtraInformation;

    PathType(Function<LineStation, Integer> getWeight, Function<LineStation, Integer> getExtraInformation) {
        this.getWeight = getWeight;
        this.getExtraInformation = getExtraInformation;
    }

    public int getWeight(LineStation lineStation) {
        return getWeight.apply(lineStation);
    }

    public int getExtraInformation(LineStation lineStation) {
        return getExtraInformation.apply(lineStation);
    }
}
