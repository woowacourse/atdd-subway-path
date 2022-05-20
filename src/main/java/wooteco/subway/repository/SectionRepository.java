package wooteco.subway.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.SectionDto;

@Repository
@Transactional(readOnly = true)
public class SectionRepository {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineRepository lineRepository;

    public SectionRepository(final StationDao stationDao, final SectionDao sectionDao,
                             final LineRepository lineRepository) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineRepository = lineRepository;
    }

    public Line findLineById(final Long lineId) {
        return lineRepository.findById(lineId);
    }

    public Station findStationById(final Long stationId) {
        return stationDao.findById(stationId);
    }

    @Transactional
    public void addSections(final Long lineId, final List<Section> sections) {
        for (Section section : sections) {
            sectionDao.save(lineId, SectionDto.from(section));
        }
    }

    @Transactional
    public void deleteSections(final Long lineId, final List<Section> sections) {
        for (Section section : sections) {
            sectionDao.deleteById(lineId, section.getUpStationId());
        }
    }
}
