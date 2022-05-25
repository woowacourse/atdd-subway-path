package wooteco.subway.service;

import java.util.List;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.discountpolicy.AgeDiscountFactory;
import wooteco.subway.domain.discountpolicy.AgeRange;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.infrastructure.PathCalculatorDijkstra;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.service.dto.PathDto;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(final StationRepository stationRepository, final LineRepository lineRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findShortestPath(final PathRequest request) {
        final PathDto pathDto = PathDto.from(request);
        final Station source = stationRepository.findById(pathDto.getSource());
        final Station target = stationRepository.findById(pathDto.getTarget());
        final List<Line> lines = lineRepository.findAll();

        final PathCalculator pathCalculator = new PathCalculatorDijkstra(lines);
        final GraphPath<Station, DefaultWeightedEdge> path = pathCalculator.findShortestPath(source, target);
        final List<Station> stations = path.getVertexList();
        final int distance = (int) path.getWeight();
        final int fare = (new Fare(AgeDiscountFactory.from(AgeRange.from(pathDto.getAge())))).calculate(distance, stations, lines);

        return new PathResponse(stations, distance, fare);
    }
}
