package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.LineRepository;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.domain.station.StationRepository;
import wooteco.subway.dto.service.request.SectionCreateRequest;
import wooteco.subway.dto.service.request.SectionDeleteRequest;

@Service
public class SectionService {
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public SectionService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional
    public void save(SectionCreateRequest sectionCreateRequest) {
        Section section = new Section(stationRepository.find(sectionCreateRequest.getUpStationId()),
            stationRepository.find(sectionCreateRequest.getDownStationId()),
            sectionCreateRequest.getDistance());

        Line line = lineRepository.find(sectionCreateRequest.getLineId());
        line.updateToAdd(section);

        lineRepository.addSection(line, section);
    }

    @Transactional
    public void delete(SectionDeleteRequest sectionDeleteRequest) {
        long lineId = sectionDeleteRequest.getLineId();

        Line line = lineRepository.find(lineId);
        Station station = stationRepository.find(sectionDeleteRequest.getStationId());
        Section deletedSection = line.delete(station);

        lineRepository.deleteSection(line, deletedSection);
    }
}
