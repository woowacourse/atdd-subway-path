package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.SectionEntity;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.section.creationStrategy.ConcreteCreationStrategy;
import wooteco.subway.domain.section.deletionStrategy.ConcreteDeletionStrategy;
import wooteco.subway.domain.section.sortStrategy.ConcreteSortStrategy;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.SectionRequest;
import wooteco.subway.repository.SectionRepository;

import java.util.Optional;

@Transactional
@Service
public class SectionService {
    private final SectionRepository sectionRepository;

    public SectionService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void save(Long lineId, SectionRequest sectionRequest) {
        Sections sections = new Sections(sectionRepository.findByLineId(lineId),
                new ConcreteCreationStrategy(), new ConcreteDeletionStrategy(), new ConcreteSortStrategy());

        SectionEntity sectionEntity = new SectionEntity(lineId, sectionRequest.getUpStationId(), sectionRequest.getDownStationId(), sectionRequest.getDistance());
        Section section = sectionRepository.save(sectionEntity);
        Optional<Section> overLappedSection = sections.getSectionOverLappedBy(section);
        sections.save(section);

        if (overLappedSection.isPresent()) {
            Section revisedSection = sections.fixOverLappedSection(overLappedSection.get(), section);
            sectionRepository.update(SectionEntity.of(revisedSection));
        }
    }

    @Transactional
    public void delete(Long lineId, Long stationId) {
        Sections sections = new Sections(sectionRepository.findByLineId(lineId),
                new ConcreteCreationStrategy(), new ConcreteDeletionStrategy(), new ConcreteSortStrategy());

        Station station = sectionRepository.findStationById(stationId);
        Optional<Section> connectedSection = sections.fixDisconnectedSection(lineId, station);
        connectedSection.ifPresent(section -> sectionRepository.save(SectionEntity.of(section)));

        sections.delete(lineId, station);
        sectionRepository.delete(lineId, stationId);
    }
}
