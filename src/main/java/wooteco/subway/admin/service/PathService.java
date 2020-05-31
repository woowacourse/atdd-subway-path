package wooteco.subway.admin.service;

import static java.util.stream.Collectors.*;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Path;
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
        Map<Long, Station> stations = getStations();
        List<Line> lines = lineRepository.findAll();
        Path path = new Path(pathRequest.getSource(), pathRequest.getTarget(), pathRequest.getType(), stations, lines);

        List<Station> shortestPath = path.getShortestPath();
        int distance = path.getDistanceByWeight();
        int duration = path.getDurationByWeight();

        return new PathResponse(StationResponse.listOf(shortestPath), distance, duration);
    }

    private Map<Long, Station> getStations() {
        return stationRepository.findAll()
            .stream()
            .collect(toMap(Station::getId, station -> station));
    }
}