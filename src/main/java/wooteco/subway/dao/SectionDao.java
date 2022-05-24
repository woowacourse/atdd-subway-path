package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.secion.Section;

public interface SectionDao {

    Section save(Section section);

    List<Section> findByLineId(Long lineId);

    List<Section> findAll();

    int update(List<Section> sections);

    int delete(Section section);
}
