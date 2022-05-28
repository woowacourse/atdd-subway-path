package wooteco.subway.dao;

import java.util.List;

import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Section;

public interface SectionDao {

    Long save(Section section);

    List<Section> findByLineId(Long lineId);

    boolean update(Long sectionId, Long downStationId, Distance distance);

    boolean deleteById(Long sectionId);

    List<Section> findAll();
}
