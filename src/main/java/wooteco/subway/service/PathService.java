package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathManager;
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
        PathManager pathManager = PathManager.of(subwayRepository.findAllSections());
        Station startStation = stationRepository.findExistingStation(sourceStationId);
        Station endStation = stationRepository.findExistingStation(targetStationId);
        Path optimalPath = pathManager.calculateOptimalPath(startStation, endStation);

        return DtoAssembler.assemble(optimalPath);
    }
}
