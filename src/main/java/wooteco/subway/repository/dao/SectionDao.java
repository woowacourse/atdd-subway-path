package wooteco.subway.repository.dao;

import java.util.List;
import wooteco.subway.domain.Section;
import wooteco.subway.repository.entity.SectionEntity;

public interface SectionDao {

    Long save(Section section);

    List<SectionEntity> findByLineId(Long lineId);

    boolean update(Long sectionId, Long downStationId, int distance);

    boolean deleteById(Long sectionId);

    List<SectionEntity> findAll();

    void insertAll(List<Section> insertSections);

    void deleteAll();
}
