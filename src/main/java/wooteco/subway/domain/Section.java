package wooteco.subway.domain;


public class Section {
    private static final String SAME_UP_AND_DOWN_STATION_ERROR = "상행과 하행의 지하철 역이 같을 수 없습니다.";
    private static final String NON_POSITIVE_DISTANCE_ERROR = "거리는 양수여야 합니다.";
    private static final String DISTANCE_IS_LONGER_THAN_THIS = "기존 역 사이보다 긴 길이를 등록할 수 없습니다.";

    private final Long id;
    private final Long lineId;
    private final Station upStation;
    private final Station downStation;
    private final int distance;

    public Section(Long id, Long lineId, Station upStation, Station downStation, int distance) {
        validate(upStation, downStation, distance);

        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(Long lineId, Station upStation, Station downStation, int distance) {
        this(null, lineId, upStation, downStation, distance);
    }

    public Section(Station upStation, Station downStation, int distance) {
        this(0L, upStation, downStation, distance);
    }


    private static void validate(Station upStation, Station downStation, int distance) {
        validateStationIds(upStation, downStation);
        validateDistance(distance);
    }

    private static void validateStationIds(Station upStation, Station downStation) {
        if (upStation.equals(downStation)) {
            throw new IllegalArgumentException(SAME_UP_AND_DOWN_STATION_ERROR);
        }
    }

    private static void validateDistance(int distance) {
        if (distance <= 0) {
            throw new IllegalArgumentException(NON_POSITIVE_DISTANCE_ERROR);
        }
    }

    public Section splitSection(Section section) {
        checkDistanceIsLongerThan(section);
        int newDistance = distance - section.distance;

        if (upStation.equals(section.upStation)) {
            return new Section(lineId, section.downStation, downStation, newDistance);
        }
        return new Section(lineId, upStation, section.upStation, newDistance);
    }

    public boolean isSameUpStation(Section section) {
        return upStation.equals(section.upStation);
    }

    public boolean isSameDownStation(Section section) {
        return downStation.equals(section.downStation);
    }

    public boolean isSameUpStationId(long id) {
        return upStation.isSameId(id);
    }

    public boolean isSameDownStationId(long id) {
        return downStation.isSameId(id);
    }

    private void checkDistanceIsLongerThan(Section section) {
        if (distance <= section.distance) {
            throw new IllegalArgumentException(DISTANCE_IS_LONGER_THAN_THIS);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getLineId() {
        return lineId;
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
}
