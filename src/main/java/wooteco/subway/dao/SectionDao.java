package wooteco.subway.dao;

import java.util.List;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;

public interface SectionDao {
    void save(Section section, Long lineId);

    void save(Sections sections, Long lineId);

    int delete(Section section);

    int deleteByLine(Long lineId);

    List<Section> findAll();
}
