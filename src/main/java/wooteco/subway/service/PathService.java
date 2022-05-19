package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.ShortestPathCalculator;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Transactional(readOnly = true)
@Service
public class PathService {

    private final StationRepository stationRepository;
    private  final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse calculateMinDistance(final PathRequest pathRequest) {
        Sections sections = new Sections(sectionRepository.findAll());
        Station startStation = stationRepository.findById(pathRequest.getSource());
        Station endStation = stationRepository.findById(pathRequest.getTarget());

        Path path = new Path(initializeShortestPathCalculator(sections), startStation, endStation);

        List<StationResponse> stationResponses = toStationResponses(path.findShortestStations());
        int distance = path.calculateMinDistance();
        int fare = path.calculateFare();
        return new PathResponse(stationResponses, distance, fare);
    }

    private ShortestPathCalculator initializeShortestPathCalculator(final Sections sections) {
        WeightedMultigraph<Station, DefaultWeightedEdge> graph
                = new WeightedMultigraph<>(DefaultWeightedEdge.class);
        ShortestPathCalculator shortestPathCalculator = new ShortestPathCalculator(graph,
                new DijkstraShortestPath<>(graph));
        shortestPathCalculator.initializeGraph(sections);
        return shortestPathCalculator;
    }

    private List<StationResponse> toStationResponses(final List<Station> stations) {
        return stations.stream()
                .map(StationResponse::new)
                .collect(Collectors.toList());
    }

}
