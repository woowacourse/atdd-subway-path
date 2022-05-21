package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.fare.BasicFare;
import wooteco.subway.domain.path.fare.DistanceOverFare;
import wooteco.subway.domain.path.fare.Fare;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
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

        Path path = Path.of(startStation, endStation, sections);
        Fare fare = new DistanceOverFare(new BasicFare(), path.getDistance());
        return PathResponse.of(path, fare.calculate());
    }
}
