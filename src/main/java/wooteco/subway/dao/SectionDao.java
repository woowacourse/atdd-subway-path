package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.domain.Section;

public interface SectionDao {

    Long save(final Section section);

    void updateLineOrderByInc(final long lineId, final Long lineOrder);

    boolean existByLineId(final long lineId);

    List<Section> findAllByLineId(final long lineId);

    void deleteById(final Long id);

    List<Section> findByLineIdAndStationId(final long lineId, final long stationId);

    void updateLineOrderByDec(final long lineId, final Long lineOrder);

    List<Section> findAll();
}
