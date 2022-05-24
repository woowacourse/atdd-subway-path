package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Stations;
import wooteco.subway.dto.section.SectionCreationRequest;
import wooteco.subway.dto.section.SectionDeletionRequest;
import wooteco.subway.exception.IllegalInputException;

@Service
@Transactional
public class SectionService {

    private static final int VALID_STATION_COUNT = 1;

    private final SectionDao sectionDao;
    private final LineService lineService;
    private final StationService stationService;

    public SectionService(final SectionDao sectionDao,
                          @Lazy final LineService lineService,
                          @Lazy final StationService stationService) {
        this.sectionDao = sectionDao;
        this.lineService = lineService;
        this.stationService = stationService;
    }

    public Section insert(final SectionCreationRequest request) {
        final Section section = toSection(request);
        final Long id = sectionDao.insert(section);
        return new Section(
                id,
                section.getLine(),
                section.getUpStation(),
                section.getDownStation(),
                new Distance(section.getDistance())
        );
    }

    public void save(final SectionCreationRequest request) {
        lineService.findById(request.getLineId());
        validateStationCount(request);

        sectionDao.findBy(request.getLineId(), request.getUpStationId(), request.getDownStationId())
                .ifPresentOrElse(existingSection -> insertBetween(request, existingSection),
                        () -> extendEndStation(request)
                );
    }

    private void validateStationCount(final SectionCreationRequest request) {
        final Stations stations = new Stations(stationService.findAllByLineId(request.getLineId()));
        final int stationCount = stations.calculateMatchCount(request.getUpStationId(), request.getDownStationId());
        if (stationCount != VALID_STATION_COUNT) {
            throw new IllegalInputException("상행역과 하행역 중 하나의 역만 노선에 포함되어 있어야 합니다.");
        }
    }

    private void insertBetween(final SectionCreationRequest request, final Section existingSection) {
        sectionDao.deleteById(existingSection.getId());
        final Section newSection = toSection(request);
        final List<Section> sections = existingSection.assign(newSection);
        sectionDao.insertAll(sections);
    }

    private Section toSection(final SectionCreationRequest request) {
        final Line line = lineService.findById(request.getLineId());
        final Station upStation = stationService.findById(request.getUpStationId());
        final Station downStation = stationService.findById(request.getDownStationId());
        return new Section(
                line,
                upStation,
                downStation,
                new Distance(request.getDistance())
        );
    }

    private void extendEndStation(final SectionCreationRequest request) {
        sectionDao.findByLineIdAndUpStationId(request.getLineId(), request.getDownStationId())
                .ifPresent(section -> extendSection(request));

        sectionDao.findByLineIdAndDownStationId(request.getLineId(), request.getUpStationId())
                .ifPresent(section -> extendSection(request));
    }

    private void extendSection(final SectionCreationRequest request) {
        final Section newUpSection = toSection(request);
        sectionDao.insert(newUpSection);
    }

    public void delete(final SectionDeletionRequest request) {
        lineService.findById(request.getLineId());

        final Sections sections = sectionDao.findAllByLineId(request.getLineId());
        final Station stationToDelete = stationService.findById(request.getStationId());
        final Sections deletableSections = sections.findDeletableSections(stationToDelete);
        deleteAll(deletableSections);

        if (deletableSections.needMerge()) {
            final Section mergedSection = deletableSections.toMergedSection();
            sectionDao.insert(mergedSection);
        }
    }

    private void deleteAll(final Sections deletableSections) {
        final List<Long> sectionIds = deletableSections.getValues()
                .stream()
                .map(Section::getId)
                .collect(Collectors.toList());
        sectionDao.deleteByIdIn(sectionIds);
    }

    public boolean existStation(final Long stationId) {
        return sectionDao.existStation(stationId);
    }
}
