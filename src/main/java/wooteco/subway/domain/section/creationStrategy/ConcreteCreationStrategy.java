package wooteco.subway.domain.section.creationStrategy;

import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;

import java.util.List;
import java.util.Optional;

public class ConcreteCreationStrategy implements CreationStrategy {

    public void save(List<Section> sections, Section section) {
        checkSavable(sections, section);
        sections.add(section);
    }

    public Optional<Section> getSectionOverLappedBy(List<Section> sections, Section newSection) {
        return sections.stream()
                .filter(section -> section.isOverLappedWith(newSection))
                .filter(section -> !section.equals(newSection))
                .findFirst();
    }

    public Section fixOverLappedSection(List<Section> sections, Section overLappedSection, Section section) {
        Section revisedSection = overLappedSection.revisedBy(section);
        sections.remove(overLappedSection);
        sections.add(revisedSection);
        return revisedSection;
    }

    private void checkSavable(List<Section> sections, Section section) {
        checkExistence(sections, section);
        checkConnected(sections, section);
        checkDistance(sections, section);
    }

    private void checkExistence(List<Section> sections, Section section) {
        if (isLineAlreadyHasBothStationsOf(sections, section)) {
            throw new IllegalArgumentException("상행역과 하행역이 이미 모두 존재합니다.");
        }
    }

    private boolean isLineAlreadyHasBothStationsOf(List<Section> sections, Section section) {
        Long lineId = section.getLineId();

        return existByLineAndStation(sections, lineId, section.getUpStation())
                && existByLineAndStation(sections, lineId, section.getDownStation());
    }

    private boolean existByLineAndStation(List<Section> sections, Long lineId, Station station) {
        return sections.stream()
                .filter(section -> lineId.equals(section.getLineId()))
                .anyMatch(section -> section.hasStation(station));
    }

    private void checkConnected(List<Section> sections, Section section) {
        if (!existConnectedTo(sections, section)) {
            throw new IllegalArgumentException("기존 노선과 연결된 구간이 아닙니다.");
        }
    }

    private boolean existConnectedTo(List<Section> sections, Section newSection) {
        return sections.stream()
                .anyMatch(section -> section.isConnectedTo(newSection));
    }

    private void checkDistance(List<Section> sections, Section newSection) {
        Optional<Section> overLappedSection = getSectionOverLappedBy(sections, newSection);

        if (overLappedSection.isPresent() && newSection.isLongerThan(overLappedSection.get())) {
            throw new IllegalArgumentException("적절한 거리가 아닙니다.");
        }
    }
}
