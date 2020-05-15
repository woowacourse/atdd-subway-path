package wooteco.subway.admin.domain;

import java.util.function.Function;

public enum PathType {
    DISTANCE(lineStation -> lineStation.getDistance(), "최단거리"),
    DURATION(lineStation -> lineStation.getDuration(), "최소시간");

    private final Function<LineStation, Integer> expression;
    private final String name;

    PathType(Function<LineStation, Integer> expression, String name) {
        this.expression = expression;
        this.name = name;
    }

    public int findWeightOf(LineStation lineStation) {
        return expression.apply(lineStation);
    }
}
