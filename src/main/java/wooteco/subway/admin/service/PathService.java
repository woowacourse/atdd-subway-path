package wooteco.subway.admin.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Edge;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse findShortestPathByDistance(PathRequest pathRequest) {
        final Map<Long, Station> stations = stationRepository.findAll()
            .stream()
            .collect(toMap(Station::getId, station -> station));
        final List<Line> lines = lineRepository.findAll();
        Path path = new Path(stations, lines);
        GraphPath<Station, Edge> graphPath = path.getGraphPath(pathRequest.getSource(), pathRequest.getTarget(),
            pathRequest.getType());
        final List<Station> shortestPath = graphPath.getVertexList();
        final List<Edge> edgeList = graphPath.getEdgeList();
        final int distance = edgeList.stream().mapToInt(Edge::getDistance).sum();
        final int duration = edgeList.stream().mapToInt(Edge::getDuration).sum();

        return new PathResponse(StationResponse.listOf(shortestPath), distance, duration);
    }
}