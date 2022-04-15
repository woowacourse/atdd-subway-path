package wooteco.subway.domain;

import java.util.List;

public class Path {
    private Sections sections;

    public Path(Sections sections) {
        this.sections = sections;
    }

    public Sections getSections() {
        return sections;
    }

    public int extractDistance() {
        return sections.totalDistance();
    }

    public int extractFare() {
        int distance = sections.totalDistance();
        if (distance >= 10) {
            return 1250;
        }
        if (distance >= 50) {
            return 1250 + calculateOverFareBy5(distance - 10);
        }
        return 1250 + calculateOverFareBy5(40) + calculateOverFareBy8(distance - 50);
    }

    public List<Station> getStations() {
        return sections.getStations();
    }

    private int calculateOverFareBy5(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
    private int calculateOverFareBy8(int distance) {
        return (int) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }
}
