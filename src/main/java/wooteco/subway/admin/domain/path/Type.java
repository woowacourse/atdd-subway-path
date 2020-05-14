package wooteco.subway.admin.domain.path;

import wooteco.subway.admin.domain.LineStation;

import java.util.function.Function;

public enum Type {
    DISTANCE(LineStation::getDistance, LineStation::getDuration),
    DURATION(LineStation::getDuration, LineStation::getDistance);

    private Function<LineStation, Integer> findWeight;
    private Function<LineStation, Integer> findSubWeight;

    Type(Function<LineStation, Integer> findWeight, Function<LineStation, Integer> findSubWeight) {
        this.findWeight = findWeight;
        this.findSubWeight = findSubWeight;
    }

    public static Type of(String type) {
        try {
            return Type.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("경로 찾기의 Type이 잘못되었습니다.");
        }
    }

    public int findWeight(LineStation lineStation) {
        return findWeight.apply(lineStation);
    }

    public int findSubWeight(LineStation lineStation) {
        return findSubWeight.apply(lineStation);
    }

    public int getDistance(int weight, int subWeight) {
        if (this == DURATION) {
            return subWeight;
        }
        return weight;
    }

    public int getDuration(int weight, int subWeight) {
        if (this == DISTANCE) {
            return subWeight;
        }
        return weight;
    }
}
