package wooteco.subway.infrastructure.jdbc.dao.entity;

import java.util.List;
import java.util.stream.Collectors;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.station.Station;

public class EntityAssembler {

    private EntityAssembler() {
    }

    public static LineEntity lineEntity(Line line) {
        return new LineEntity(line.getId(), line.getName(), line.getColor(), line.getExtraFare());
    }

    public static Line line(LineEntity lineEntity, List<Section> sections) {
        return new Line(lineEntity.getId(), sections,
                lineEntity.getName(), lineEntity.getColor(), lineEntity.getExtraFare());
    }

    public static List<SectionEntity> sectionEntities(long lineId, Line line) {
        return line.getSections()
                .stream()
                .map(section -> EntityAssembler.sectionEntity(lineId, section))
                .collect(Collectors.toUnmodifiableList());
    }

    public static SectionEntity sectionEntity(long lineId, Section section) {
        return new SectionEntity(section.getId(), lineId,
                section.getUpStationId(),
                section.getDownStationId(),
                section.getDistance());
    }

    public static Section section(SectionEntity sectionEntity) {
        return new Section(sectionEntity.getId(), sectionEntity.getUpStationId(), sectionEntity.getDownStationId(),
                sectionEntity.getDistance());
    }

    public static StationEntity stationEntity(Station station) {
        return new StationEntity(station.getId(), station.getName());
    }

    public static Station station(StationEntity stationEntity) {
        return new Station(stationEntity.getId(), stationEntity.getName());
    }
}
