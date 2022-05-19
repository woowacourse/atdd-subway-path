package wooteco.subway.reopository;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Repository;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.reopository.dao.LineDao;
import wooteco.subway.reopository.dao.StationDao;
import wooteco.subway.reopository.entity.SectionEntity;
import wooteco.subway.domain.Section;
import wooteco.subway.reopository.dao.SectionDao;

@Repository
public class SectionRepository {

    private final SectionDao sectionDao;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionRepository(SectionDao sectionDao, StationRepository stationRepository,
                             LineRepository lineRepository) {
        this.sectionDao = sectionDao;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public Long save(Section section) {
        return sectionDao.save(SectionEntity.from(section));
    }

    public List<Section> findByLineId(Long lineId) {
        return sectionDao.findByLineId(lineId).stream()
                .map(entity -> toSection(entity))
                .collect(toList());
    }

    public List<Section> findAll() {
        return sectionDao.findAll().stream()
                .map(entity -> toSection(entity))
                .collect(toList());
    }

    public void update(Section section) {
        sectionDao.update(SectionEntity.from(section));
    }

    public void deleteById(Long id) {
        sectionDao.deleteById(id);
    }

    private Section toSection(SectionEntity entity) {
        Line line = lineRepository.findById(entity.getLineId(), "조회하려는 노선이 없습니다.");
        Station upStation = stationRepository.findById(entity.getUpStationId(), "조회 하려는 상행역이 없습니다.");
        Station downStation = stationRepository.findById(entity.getDownStationId(), "조회 하려는 하행역이 없습니다.");
        return new Section(entity.getId(), line, upStation, downStation, entity.getDistance());
    }
}
