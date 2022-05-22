package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.path.GraphGenerator;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathManager;
import wooteco.subway.domain.path.cost.CostManager;
import wooteco.subway.domain.path.cost.CostSection;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.repository.SubwayRepository;

@Service
public class PathService {

    private final static List<CostSection> costSections = List.of(
            new CostSection(10, 5, 100),
            new CostSection(50, 8, 100));

    private final SubwayRepository subwayRepository;
    private final StationRepository stationRepository;

    public PathService(SubwayRepository subwayRepository, StationRepository stationRepository) {
        this.subwayRepository = subwayRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(long sourceStationId, long targetStationId) {
        PathManager pathManager = PathManager.of(
                GraphGenerator.toAdjacentPath(subwayRepository.findAllSections()));
        Station startStation = stationRepository.findExistingStation(sourceStationId);
        Station endStation = stationRepository.findExistingStation(targetStationId);
        Path optimalPath = pathManager.calculateOptimalPath(startStation, endStation);
        CostManager costManager = new CostManager(costSections);
        int fare = costManager.calculateFare(optimalPath.getTotalDistance());

        return PathResponse.of(optimalPath, fare);
    }
}
