package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.FareCondition;
import wooteco.subway.domain.fare.FareStrategyFactory;
import wooteco.subway.domain.path.PathCalculator;
import wooteco.subway.domain.path.ShortestPathEdge;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;
    private final LineRepository lineRepository;
    private final PathCalculator pathCalculator;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository,
                       LineRepository lineRepository, PathCalculator pathCalculator) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
        this.lineRepository = lineRepository;
        this.pathCalculator = pathCalculator;
    }

    public PathResponse calculateMinDistance(final PathRequest pathRequest) {
        Sections sections = new Sections(sectionRepository.findAll());
        Station startStation = stationRepository.findById(pathRequest.getSource());
        Station endStation = stationRepository.findById(pathRequest.getTarget());

        Path path = new Path(startStation, endStation);
        List<StationResponse> stationResponses =
                toStationResponses(pathCalculator.calculateShortestStations(sections, path));
        int distance = pathCalculator.calculateShortestDistance(sections, path);

        int fare = FareStrategyFactory.get(distance)
                .calculateFare(new FareCondition(
                        distance,
                        pathRequest.getAge(),
                        findMaxExtraFare(pathCalculator.findPassedEdges(sections, path)))
                );
        return new PathResponse(stationResponses, distance, fare);
    }

    private int findMaxExtraFare(final List<ShortestPathEdge> edges) {
        List<Long> lineIds = edges.stream()
                .map(ShortestPathEdge::getLineId)
                .distinct()
                .collect(Collectors.toList());
        return lineRepository.findMaxExtraFare(lineIds);
    }

    private List<StationResponse> toStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

}
