package wooteco.subway.repository;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.section.Distance;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.entity.LineEntity;
import wooteco.subway.entity.SectionEntity;
import wooteco.subway.exception.NotFoundStationException;

@Repository
public class JdbcSectionRepository implements SectionRepository {

    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationRepository stationRepository;

    public JdbcSectionRepository(SectionDao sectionDao, LineDao lineDao, StationRepository stationRepository) {
        this.sectionDao = sectionDao;
        this.stationRepository = stationRepository;
        this.lineDao = lineDao;
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
        Line line = getLineById(sectionEntity.getLineId());

        return new Section(id, line, upStation, downStation, distance);
    }

    private Station getStationById(Long stationId) {
        return stationRepository.findById(stationId)
                .orElseThrow(NotFoundStationException::new);
    }

    private Line getLineById(Long lineId) {
        LineEntity lineEntity = lineDao.findById(lineId);
        return new Line(lineEntity.getId(), lineEntity.getName(), lineEntity.getColor(),
                new Fare(lineEntity.getExtraFare()));
    }

    @Override
    public void deleteAllSectionsByLineId(Long lineId) {
        sectionDao.deleteAllSectionsByLineId(lineId);
    }
}
