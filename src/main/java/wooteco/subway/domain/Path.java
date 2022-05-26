package wooteco.subway.domain;

import wooteco.subway.exception.ClientException;
import java.util.*;

public class Path {

    private final List<Long> stationIds;
    private final List<Long> lineIds;
    private final int distance;

    public Path(List<Long> stationIds, List<Long> lineIds, int distance) {
        this.stationIds = Collections.unmodifiableList(stationIds);
        this.lineIds = lineIds;
        this.distance = distance;
    }

    public double calculateFare(List<Line> lines, Long age) {
        int lineExtraFare = findMostExpensiveExtraFare(lines);
        return Age.findByAge(age).calc(lineExtraFare + Fare.calculateFare(distance));
    }

    private int findMostExpensiveExtraFare(List<Line> lines) {
        return lineIds
                .stream()
                .mapToInt(id -> findExtraFare(lines, id))
                .max()
                .orElseThrow(NoSuchElementException::new);
    }

    private int findExtraFare(List<Line> lines, Long id) {
        return lines.stream()
                .filter(line -> line.getId() == id)
                .findAny()
                .orElseThrow(() -> new ClientException("존재하지 않은 line id입니다."))
                .getExtraFare();
    }

    public List<Long> getStationIds() {
        return stationIds;
    }

    public int getDistance() {
        return distance;
    }
}
