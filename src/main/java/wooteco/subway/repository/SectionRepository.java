package wooteco.subway.repository;

import java.util.List;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;

public interface SectionRepository {
    List<Section> findAll();

    Sections findSectionsByLineId(Long lineId);

    Long save(Long lineId, Section section);

    void deleteAllSectionsByLineId(Long lineId);
}
