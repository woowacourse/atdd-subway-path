package wooteco.subway.domain.section.creationStrategy;

import wooteco.subway.domain.section.Section;

import java.util.List;
import java.util.Optional;

public interface CreationStrategy {

    void save(List<Section> sections, Section section);

    Optional<Section> getSectionOverLappedBy(List<Section> sections, Section section);

    Section fixOverLappedSection(List<Section> sections, Section overLappedSection, Section section);
}
