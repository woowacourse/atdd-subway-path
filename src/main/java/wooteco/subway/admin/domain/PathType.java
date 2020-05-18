package wooteco.subway.admin.domain;

import java.util.Arrays;
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

    public static PathType of(String type) {
        try {
            return Arrays.stream(PathType.values())
                    .filter(pathType -> pathType.equals(type))
                    .findAny()
                    .orElseThrow(IllegalAccessException::new);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("요청한 경로 조회 타입이 존재하지 않습니다.");
        }
    }


    public int getWeight(LineStation lineStation) {
        return getWeight.apply(lineStation);
    }

    public int getExtraInformation(LineStation lineStation) {
        return getExtraInformation.apply(lineStation);
    }
}
