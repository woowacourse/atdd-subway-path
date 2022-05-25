package wooteco.subway.repository;

import java.util.List;
import wooteco.subway.domain.Section;

public interface SectionRepository {

    Long save(Section section);

    List<Section> findByLineId(Long lineId);

    boolean update(Long sectionId, Long downStationId, int distance);

    boolean deleteById(Long sectionId);

    List<Section> findAll();

    void updateAll(List<Section> insertSections);
}
