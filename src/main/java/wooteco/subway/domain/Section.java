package wooteco.subway.domain;

import java.util.Objects;
import wooteco.subway.domain.vo.LineId;
import wooteco.subway.domain.vo.SectionDistance;
import wooteco.subway.domain.vo.SectionId;
import wooteco.subway.domain.vo.StationId;

public class Section implements Comparable<Section> {

    private final SectionId id;
    private final LineId lineId;
    private Station upStation;
    private Station downStation;
    private SectionDistance distance;

    public Section(SectionId id, LineId lineId, Station upStation, Station downStation, SectionDistance distance) {
        this.id = id;
        this.lineId = lineId;
        this.upStation = upStation;
        this.downStation = downStation;
        this.distance = distance;
    }

    public Section(LineId lineId, Station upStation, Station downStation, SectionDistance distance) {
        this(null, lineId, upStation, downStation, distance);
    }

    public Section(Station upStation, Station downStation, SectionDistance distance) {
        this(null, null, upStation, downStation, distance);
    }

    public boolean isSameLineId(LineId lineId) {
        return Objects.equals(this.lineId, lineId);
    }

    public boolean hasSameDownStation(Section other) {
        return Objects.equals(this.downStation, other.downStation);
    }

    public boolean hasSameUpStation(Section other) {
        return Objects.equals(this.upStation, other.upStation);
    }

    public boolean isAddable(Section other) {
        return hasSameUpStation(other) || hasSameDownStation(other);
    }

    public boolean isWider(Section input) {
        return this.distance.getDistance() > input.distance.getDistance();
    }

    public boolean upStationIsSameToDownStation(Section other) {
        return Objects.equals(this.upStation, other.downStation);
    }

    public boolean downStationIsSameToUpStation(Section other) {
        return Objects.equals(this.downStation, other.upStation);
    }

    public boolean hasStationById(StationId stationId) {
        return upStation.isSameId(stationId) || downStation.isSameId(stationId);
    }

    public void shortenUpStation(Section input) {
        this.upStation = input.downStation;
        this.distance = this.distance.minus(input.distance);
    }

    public void shortenDownStation(Section input) {
        this.downStation = input.upStation;
        this.distance = this.distance.minus(input.distance);
    }

    public Long getId() {
        return id.getId();
    }

    public Long getLineId() {
        return lineId.getId();
    }

    public Station getUpStation() {
        return upStation;
    }

    public Station getDownStation() {
        return downStation;
    }

    public long getDistance() {
        return distance.getDistance();
    }

    @Override
    public int compareTo(Section o) {
        if (Objects.equals(this.downStation, o.upStation)) {
            return -1;
        }
        if (Objects.equals(this.upStation, o.downStation)) {
            return 1;
        }
        return 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Section section = (Section) o;
        return Objects.equals(lineId, section.lineId) && Objects.equals(upStation, section.upStation)
                && Objects.equals(downStation, section.downStation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lineId, upStation, downStation);
    }
}
