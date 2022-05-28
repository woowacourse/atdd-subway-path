package wooteco.subway.dao;

import wooteco.subway.domain.Section;

import java.util.List;

public interface SectionDao {
    Long save(Section section, Long lineId);

    void update(List<Section> sections);

    boolean delete(Long deletedSectionId);
}
