package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private Function<LineStation, Integer> function;

    PathType(Function<LineStation, Integer> function) {
        this.function = function;
    }

    public static PathType of(String name) {
        return Arrays.stream(values())
                .filter(val -> name.equals(val.name()))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("%s을(를) 찾을 수 없습니다.", name)));
    }

    public int getWeight(LineStation lineStation) {
        return function.apply(lineStation);
    }
}
