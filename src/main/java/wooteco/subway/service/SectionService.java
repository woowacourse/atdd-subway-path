package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionEntity;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.section.creationStrategy.ConcreteCreationStrategy;
import wooteco.subway.domain.section.deletionStrategy.ConcreteDeletionStrategy;
import wooteco.subway.domain.section.sortStrategy.ConcreteSortStrategy;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.SectionRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class SectionService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public SectionService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    @Transactional
    public void saveSection(Long lineId, SectionRequest sectionRequest) {
        Sections sections = new Sections(findSectionsInLine(lineId),
                new ConcreteCreationStrategy(), new ConcreteDeletionStrategy(), new ConcreteSortStrategy());

        SectionEntity sectionEntity = new SectionEntity(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        Section section = saveSection(sectionEntity);
        Optional<Section> overLappedSection = sections.getSectionOverLappedBy(section);
        sections.save(section);

        if (overLappedSection.isPresent()) {
            Section revisedSection = sections.fixOverLappedSection(overLappedSection.get(), section);
            sectionDao.update(SectionEntity.of(revisedSection));
        }
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        Sections sections = new Sections(findSectionsInLine(lineId),
                new ConcreteCreationStrategy(), new ConcreteDeletionStrategy(), new ConcreteSortStrategy());

        Station station = stationDao.findById(stationId);
        Optional<Section> connectedSection = sections.fixDisconnectedSection(lineId, station);
        connectedSection.ifPresent(section -> saveSection(SectionEntity.of(section)));

        sections.delete(lineId, station);
        sectionDao.delete(lineId, stationId);
    }

    public Section saveSection(SectionEntity sectionEntity) {
        SectionEntity newSectionEntity = sectionDao.insert(sectionEntity);

        return assembleSection(newSectionEntity);
    }

    private List<Section> findSectionsInLine(Long lineId) {
        List<SectionEntity> sectionEntities = sectionDao.findByLineId(lineId);

        return sectionEntities.stream()
                .map(this::assembleSection)
                .collect(Collectors.toList());
    }

    private Section assembleSection(SectionEntity sectionEntity) {
        Station upStation = stationDao.findById(sectionEntity.getUpStationId());
        Station downStation = stationDao.findById(sectionEntity.getDownStationId());

        return new Section(sectionEntity.getId(), sectionEntity.getLineId(),
                upStation, downStation, sectionEntity.getDistance());
    }
}
