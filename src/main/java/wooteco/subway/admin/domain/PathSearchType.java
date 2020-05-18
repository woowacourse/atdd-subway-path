package wooteco.subway.admin.domain;

import wooteco.subway.admin.domain.exception.IllegalPathSearchTypeException;

import java.util.Arrays;
import java.util.function.Function;

public enum PathSearchType {
    DISTANCE("DISTANCE", LineStation::getDistance),
    DURATION("DURATION", LineStation::getDuration);

    private final String value;
    private final Function<LineStation, Integer> valueByPathSearchType;

    PathSearchType(String value, Function<LineStation, Integer> valueByPathSearchType) {
        this.value = value;
        this.valueByPathSearchType = valueByPathSearchType;
    }

    public static PathSearchType of(String type) {
        return Arrays.stream(PathSearchType.values())
                .filter(pathSearchType -> pathSearchType.isSameValue(type))
                .findFirst()
                .orElseThrow(IllegalPathSearchTypeException::new);
    }

    public int getValueByPathSearchType(LineStation lineStation) {
        return this.valueByPathSearchType.apply(lineStation);
    }

    public boolean isSameValue(String value) {
        return this.value.equals(value);
    }

    public String getValue() {
        return value;
    }
}
