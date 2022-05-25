package wooteco.subway.dao;

import java.util.List;

import wooteco.subway.domain.section.Section;
import wooteco.subway.entity.SectionEntity;

public interface SectionDao {
    void save(Long lineId, Section section);

    List<SectionEntity> findByLine(Long lineId);

    List<SectionEntity> findAll();

    void update(Long lineId, Section section);

    void deleteAll(Long lineId);

    void delete(Long lineId, Section section);

    boolean existSectionUsingStation(Long stationId);
}
