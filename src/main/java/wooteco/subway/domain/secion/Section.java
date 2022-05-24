package wooteco.subway.domain.secion;

import wooteco.subway.domain.Station;
import wooteco.subway.exception.AddSectionException;
import wooteco.subway.exception.PositiveDigitException;

public class Section {

    private final Long id;
    private final Long lineId;
    private Station upStation;
    private Station downStation;
    private int distance;

    public Section(final Long lineId, final Station upStation, final Station downStation, final int distance) {
        this(0L, lineId, upStation, downStation, distance);
    }

    public Section(final Long id, final Long lineId, final Station upStation, final Station downStation,
                   final int distance) {
        validatePositiveDistance(distance);
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    private void validatePositiveDistance(final int distance) {
        if (distance <= 0) {
            throw new PositiveDigitException("구간의 길이가 양수가 아닙니다.");
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

    public boolean isUpperThan(final Section otherSection) {
        return this.downStation.equals(otherSection.upStation);
    }

    public boolean isLowerThan(final Section otherSection) {
        return this.upStation.equals(otherSection.downStation);
    }

    public boolean isConnectedWith(final Section otherSection) {
        return contain(otherSection.upStation) || contain(otherSection.downStation);
    }

    private boolean contain(final Station station) {
        return this.upStation.equals(station) || this.downStation.equals(station);
    }

    public boolean hasSameStationWith(final Station station) {
        return this.upStation.equals(station) || this.downStation.equals(station);
    }

    public boolean hasSameUpStationWith(final Station station) {
        return this.upStation.equals(station);
    }

    public boolean hasSameDownStationWith(final Station station) {
        return this.downStation.equals(station);
    }

    public void updateUpStationFrom(final Section otherSection) {
        validateLongerThan(otherSection);
        this.upStation = otherSection.downStation;
        this.distance = this.distance - otherSection.distance;
    }

    public void updateDownStationFrom(final Section otherSection) {
        validateLongerThan(otherSection);
        this.downStation = otherSection.upStation;
        this.distance = this.distance - otherSection.distance;
    }

    private void validateLongerThan(final Section otherSection) {
        if (this.distance <= otherSection.distance) {
            throw new AddSectionException("현재 구간의 길이가 추가하려는 구간의 길이보다 작거나 같습니다.");
        }
    }

    public void combineSection(final Section otherSection) {
        this.downStation = otherSection.downStation;
        this.distance = this.distance + otherSection.distance;
    }
}
