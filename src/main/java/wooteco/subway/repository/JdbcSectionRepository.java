package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.entity.SectionEntity;
import wooteco.subway.exception.NotFoundStationException;

@Repository
public class JdbcSectionRepository implements SectionRepository {

    private final SectionDao sectionDao;
    private final StationRepository stationRepository;

    public JdbcSectionRepository(SectionDao sectionDao, StationRepository stationRepository) {
        this.sectionDao = sectionDao;
        this.stationRepository = stationRepository;
    }

    @Override
    public Long save(Long lineId, Section section) {
        return sectionDao.save(lineId, section);
    }

    @Override
    public List<Section> findAll() {
        List<SectionEntity> sectionEntities = sectionDao.findAll();
        return sectionEntities.stream()
                .map(this::entityToSection)
                .collect(Collectors.toList());
    }

    @Override
    public Sections findSectionsByLineId(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findSectionsByLineId(lineId);
        List<Section> sections = sectionEntities.stream()
                .map(this::entityToSection)
                .collect(Collectors.toList());
        return new Sections(sections);
    }

    private Section entityToSection(SectionEntity sectionEntity) {
        Long id = sectionEntity.getId();
        Station upStation = getStationById(sectionEntity.getUpStationId());
        Station downStation = getStationById(sectionEntity.getDownStationId());
        Distance distance = new Distance(sectionEntity.getDistance());
        Long lineId = sectionEntity.getLineId();

        return new Section(id, lineId, upStation, downStation, distance);
    }

    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(NotFoundStationException::new);
    }

    @Override
    public void deleteAllSectionsByLineId(Long lineId) {
        sectionDao.deleteAllSectionsByLineId(lineId);
    }
}
