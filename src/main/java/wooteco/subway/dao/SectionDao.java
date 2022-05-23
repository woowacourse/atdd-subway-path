package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;

public interface SectionDao {

    Section save(Section section);

    List<Section> findByLineId(long lineId);

    List<Section> findAll();

    int update(List<Section> sections);

    int delete(Section section);
}
