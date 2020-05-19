package wooteco.subway.admin.domain;

import java.util.Arrays;
import java.util.function.Function;

public enum PathType {
    DISTANCE(LineStation::getDistance),
    DURATION(LineStation::getDuration);

    private Function<LineStation, Integer> weight;

    PathType(Function<LineStation, Integer> weight) {
        this.weight = weight;
    }

    public static PathType of(String name) {
        return Arrays.stream(values())
                .filter(val -> name.equals(val.name()))
                .findFirst()
                .orElseThrow(
                        () -> new IllegalArgumentException(
                                String.format("%s 역이 존재하지 않습니다.", name)));
    }

    public int getWeight(LineStation lineStation) {
        return weight.apply(lineStation);
    }
}
