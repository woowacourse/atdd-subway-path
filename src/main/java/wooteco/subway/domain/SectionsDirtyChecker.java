package wooteco.subway.domain;

import java.util.List;
import java.util.stream.Collectors;

public class SectionsDirtyChecker {

    private final List<Section> snapShot;

    public SectionsDirtyChecker(List<Section> snapShot) {
        this.snapShot = snapShot;
    }

    public static SectionsDirtyChecker from(List<Section> sections) {
        List<Section> snapshot = sections.stream()
            .map(section -> new Section(
                section.getId(),
                section.getUpStation(),
                section.getDownStation(),
                section.getDistance()))
            .collect(Collectors.toList());
        return new SectionsDirtyChecker(snapshot);
    }

    public Sections findUpdated(List<Section> sections) {
        List<Section> updatedSections = sections.stream()
            .filter(updated -> snapShot.stream()
                .anyMatch(origin -> origin.isSameId(updated) && !origin.equals(updated))
            ).collect(Collectors.toList());
        return new Sections(updatedSections);
    }

    public Sections findDeleted(List<Section> sections) {
        List<Long> ids = sections.stream()
            .map(Section::getId)
            .collect(Collectors.toList());
        List<Section> deletedSections = snapShot.stream()
            .filter(origin -> !ids.contains(origin.getId()))
            .collect(Collectors.toList());
        return new Sections(deletedSections);
    }

    public Sections findSaved(List<Section> sections) {
        List<Section> savedSections = sections.stream()
            .filter(updated -> !updated.hasId())
            .collect(Collectors.toList());
        return new Sections(savedSections);
    }
}
