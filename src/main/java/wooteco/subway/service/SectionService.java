package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.SectionServiceRequest;

@Transactional
@Service
public class SectionService {

    private static final int MATCHING_STATION_SIZE = 1;
    private static final int DELETABLE_SECTION_SIZE = 2;

    private final StationService stationService;
    private final SectionDao sectionDao;

    public SectionService(StationService stationService, SectionDao sectionDao) {
        this.stationService = stationService;
        this.sectionDao = sectionDao;
    }

    public void save(Section section) {
        sectionDao.save(section);
    }

    public void connect(SectionServiceRequest sectionServiceRequest) {
        validate(sectionServiceRequest);
        Long lineId = sectionServiceRequest.getLindId();
        Long upStationId = sectionServiceRequest.getUpStationId();
        Long downStationId = sectionServiceRequest.getDownStationId();
        Section newSection = new Section(lineId, upStationId, downStationId, sectionServiceRequest.getDistance());

        sectionDao.findBy(lineId, upStationId, downStationId)
                .ifPresentOrElse(
                        section -> insert(section, newSection),
                        () -> extend(newSection)
                );
    }

    private void validate(SectionServiceRequest sectionServiceRequest) {
        List<Long> stationIds = stationService.findAllByLineId(sectionServiceRequest.getLindId())
                .stream()
                .map(Station::getId)
                .collect(Collectors.toList());

        long matchingStations =
                Stream.of(sectionServiceRequest.getUpStationId(), sectionServiceRequest.getDownStationId())
                        .filter(stationIds::contains)
                        .count();

        if (matchingStations != MATCHING_STATION_SIZE) {
            throw new IllegalArgumentException("상행 종점과 하행 종점 중 하나의 종점만 포함되어야 합니다.");
        }
    }

    private void insert(Section section, Section newSection) {
        sectionDao.deleteById(section.getId());
        List<Section> sections = section.split(newSection);
        sectionDao.saveAll(sections);
    }

    private void extend(Section newSection) {
        sectionDao.save(newSection);
    }

    public void delete(Long lineId, Long stationId) {
        Sections sections = sectionDao.findAllByLineId(lineId);
        Sections sectionsToDelete = sections.getSectionsToDelete(stationId);
        List<Long> sectionIds = sectionsToDelete.getSectionIds();
        deleteSections(sectionIds);
        mergeSectionsIfNecessary(sectionsToDelete);
    }

    private void deleteSections(List<Long> sectionIds) {
        for (Long id : sectionIds) {
            sectionDao.deleteById(id);
        }
    }

    private void mergeSectionsIfNecessary(Sections sectionsToDelete) {
        if (sectionsToDelete.size() == DELETABLE_SECTION_SIZE) {
            Section mergedSection = sectionsToDelete.merge();
            sectionDao.save(mergedSection);
        }
    }

    public Sections findAllByLineId(Long lineId) {
        return sectionDao.findAllByLineId(lineId);
    }

    public Sections findAll() {
        return sectionDao.findAll();
    }
}
