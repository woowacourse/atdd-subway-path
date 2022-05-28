package wooteco.subway.dao;

import java.util.List;

import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.fare.Fare;

public interface SectionDao {
    void save(Section section, Long lineId);

    void save(Sections sections, Long lineId);

    int delete(Section section);

    int deleteByLine(Long lineId);

    List<Section> findAll();

    Fare findExtraFareById(Long id);
}
