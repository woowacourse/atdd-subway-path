package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.path.Path2;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.DtoAssembler;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.repository.SubwayRepository;

@Service
public class PathService {

    private final SubwayRepository subwayRepository;
    private final StationRepository stationRepository;

    public PathService(SubwayRepository subwayRepository, StationRepository stationRepository) {
        this.subwayRepository = subwayRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(long sourceStationId, long targetStationId) {
        Station startStation = stationRepository.findExistingStation(sourceStationId);
        Station endStation = stationRepository.findExistingStation(targetStationId);
        List<Section> sections = subwayRepository.findAllSections();
        Path2 path = Path2.of(startStation, endStation, sections);

        return DtoAssembler.assemble(path);
    }
}
