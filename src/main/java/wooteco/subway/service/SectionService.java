package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import wooteco.exception.badrequest.InvalidSectionOnLineException;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Route;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.SectionServiceDto;
import wooteco.subway.web.dto.response.StationResponse;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationService stationService;
    private final Route route;

    public SectionService(SectionDao sectionDao, StationService stationService, Route route) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
        this.route = route;
    }

    public SectionServiceDto saveByLineCreate(Line line, @Valid SectionServiceDto dto) {
        Section section = newSection(line, dto);
        return saveSectionAtEnd(section);
    }

    public SectionServiceDto save(Line line, @Valid SectionServiceDto dto) {
        Sections sections = new Sections(sectionDao.findAllByLineId(line.getId()));
        Section section = newSection(line, dto);
        sections.validateInsertable(section);

        if (sections.isBothEndSection(section)) {
            return saveSectionAtEnd(section);
        }
        return saveSectionAtMiddle(section, sections);
    }

    private Section newSection(Line line, @Valid SectionServiceDto dto) {
        Station upStation = stationService.findById(dto.getUpStationId());
        Station downStation = stationService.findById(dto.getDownStationId());
        Distance distance = new Distance(dto.getDistance());
        return new Section(line, upStation, downStation, distance);
    }

    private SectionServiceDto saveSectionAtEnd(Section section) {
        SectionServiceDto sectionServiceDto = SectionServiceDto.from(sectionDao.insert(section));

        route.updateGraph(sectionDao.findAll());
        return sectionServiceDto;
    }

    private SectionServiceDto saveSectionAtMiddle(Section section, Sections sections) {
        Section legacySection = sections.findByMatchStation(section);
        sectionDao.delete(legacySection);
        sectionDao.insert(legacySection.updateForSave(section));
        SectionServiceDto sectionServiceDto = SectionServiceDto.from(sectionDao.insert(section));

        route.updateGraph(sectionDao.findAll());
        return sectionServiceDto;
    }

    public void delete(Line line, @NotNull Long stationId) {
        Sections sections = new Sections(sectionDao.findAllByLineId(line.getId()));
        Station station = stationService.findById(stationId);
        sections.validateDeletableCount();
        sections.validateExistStation(station);

        if (sections.isBothEndStation(station)) {
            deleteStationAtEnd(line, station);
            return;
        }
        deleteStationAtMiddle(line, station);
    }

    private void deleteStationAtEnd(Line line, Station station) {
        if (sectionDao.findByLineIdAndUpStationId(line.getId(), station.getId()).isPresent()) {
            sectionDao.deleteByLineIdAndUpStationId(line.getId(), station.getId());
        }
        sectionDao.deleteByLineIdAndDownStationId(line.getId(), station.getId());

        route.updateGraph(sectionDao.findAll());
    }

    private void deleteStationAtMiddle(Line line, Station station) {
        Section upSection = sectionDao.findByLineIdAndDownStationId(line.getId(), station.getId())
            .orElseThrow(InvalidSectionOnLineException::new);
        Section downSection = sectionDao.findByLineIdAndUpStationId(line.getId(), station.getId())
            .orElseThrow(InvalidSectionOnLineException::new);

        Section updatedSection = upSection.updateForDelete(downSection);
        sectionDao.delete(upSection);
        sectionDao.delete(downSection);
        sectionDao.insert(updatedSection);

        route.updateGraph(sectionDao.findAll());
    }

    public List<StationResponse> findAllByLind(Line line) {
        Sections sections = new Sections(sectionDao.findAllByLineId(line.getId()));

        return sections.sortedStations()
            .stream()
            .map(StationResponse::from)
            .collect(Collectors.toList());
    }
}

