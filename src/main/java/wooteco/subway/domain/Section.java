package wooteco.subway.domain;

public class Section {

    private final Station upStation;
    private final Station downStation;
    private final Distance distance;
    private final Long id;

    private Section(Long id, Station upStation, Station downStation, Distance distance) {
        this.id = id;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section createWithId(Long id, Station upStation, Station downStation, int distance) {
        return new Section(id, upStation, downStation, new Distance(distance));
    }

    public static Section createWithoutId(Station upStation, Station downStation, int distance) {
        return new Section(null, upStation, downStation, new Distance(distance));
    }

    public static Section createWithoutId(Station upStation, Station downStation, Distance distance) {
        return new Section(null, upStation, downStation, distance);
    }

    public boolean isEqualToUpStation(Station station) {
        return upStation.equals(station);
    }

    public boolean isEqualToDownStation(Station station) {
        return downStation.equals(station);
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public Distance getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return "Section{" +
                "upStation=" + upStation +
                ", downStation=" + downStation +
                ", distance=" + distance +
                ", id=" + id +
                '}';
    }
}
