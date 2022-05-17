package wooteco.subway.dao;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.SectionsOnLine;

import java.util.List;

public interface SectionDao {

    Section save(Long lineId, Section section);

    SectionsOnLine findById(Long lineId);

    void delete(Long lineId, Section section);

    List<Section> findAll();
}
