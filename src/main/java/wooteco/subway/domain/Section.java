package wooteco.subway.domain;

public class Section {

    private final Long id;
    private final Long lineId;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    private Section(Long id, Long lineId, Station upStation, Station downStation, int distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public static Section createWithId(Long id, Station upStation, Station downStation, int distance) {
        return new Section(id, null, upStation, downStation, distance);
    }

    public static Section createWithoutId(Station upStation, Station downStation, int distance) {
        return new Section(null, null, upStation, downStation, distance);
    }

    public static Section createWithLine(Long id, Long lineId, Station upStation, Station downStation, int distance){
        return new Section(id, lineId, upStation, downStation, distance);
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

    public int getDistance() {
        return distance;
    }

    public Long getLineId() {
        return lineId;
    }
}
