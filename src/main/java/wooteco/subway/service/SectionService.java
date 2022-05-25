package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.section.Sections;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.dto.service.request.SectionCreateRequest;
import wooteco.subway.dto.service.request.SectionDeleteRequest;

@Service
public class SectionService {
    private final SectionDao sectionDao;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionService(LineRepository lineRepository, SectionDao sectionDao, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionDao = sectionDao;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void save(SectionCreateRequest sectionCreateRequest) {
        Section section = new Section(stationRepository.find(sectionCreateRequest.getUpStationId()),
            stationRepository.find(sectionCreateRequest.getDownStationId()),
            sectionCreateRequest.getDistance());

        Line line = lineRepository.find(sectionCreateRequest.getLineId());
        line.updateToAdd(section);

        sectionDao.save(line.getId(), section);
        saveUpdatedLine(line);
    }

    private void saveUpdatedLine(Line line) {
        Sections sections = line.getSections();
        sections.forEach(section1 -> sectionDao.update(line.getId(), section1));
    }

    @Transactional
    public void delete(SectionDeleteRequest sectionDeleteRequest) {
        long lineId = sectionDeleteRequest.getLineId();

        Line line = lineRepository.find(lineId);
        Station station = stationRepository.find(sectionDeleteRequest.getStationId());
        Section deletedSection = line.delete(station);

        sectionDao.delete(lineId, deletedSection);
        line.getSections().forEach(section -> sectionDao.update(lineId, section));
    }
}
