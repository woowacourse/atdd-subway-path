package wooteco.subway.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;

@Service
public class SectionService {

    private final SectionDao sectionDao;
    private final StationService stationService;
    private final LineService lineService;

    public SectionService(final SectionDao sectionDao, final StationService stationService, final LineService lineService) {
        this.sectionDao = sectionDao;
        this.stationService = stationService;
        this.lineService = lineService;
    }

    @Transactional
    public Section saveInitialSection(final Section section) {
        return sectionDao.save(section);
    }

    @Transactional
    public Section addSection(final long lineId, final Section requestSection) {
        final Station upStation = stationService.findStationById(requestSection.getUpStation().getId());
        final Station downStation = stationService.findStationById(requestSection.getDownStation().getId());
        lineService.findLineById(lineId);

        Section section = new Section(upStation, downStation, requestSection.getDistance(), lineId);
        final Sections sections = new Sections(sectionDao.findAllByLineId(lineId));
        sections.add(section);

        sectionDao.deleteByLineId(lineId);
        sectionDao.saveAll(sections.getSections());

        return section;
    }

    @Transactional
    public void delete(final Long lineId, final Long stationId) {
        final Sections sections = new Sections(sectionDao.findAllByLineId(lineId));
        sections.remove(stationId);
        sectionDao.deleteByLineId(lineId);
        sectionDao.saveAll(sections.getSections());
    }

    @Transactional(readOnly = true)
    public List<Station> findStationsByLine(final long lineId) {
        final List<Section> lineSections = sectionDao.findAllByLineId(lineId);
        final Sections sections = new Sections(lineSections);
        return sections.extractStations();
    }

    @Transactional(readOnly = true)
    public List<Section> findAllSections() {
        return sectionDao.findAll();
    }
}
