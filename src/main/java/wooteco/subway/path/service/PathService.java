package wooteco.subway.path.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.path.domain.PathGraphAlgorithm;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PathService {
    private final StationDao stationDao;
    private final PathGraphAlgorithm pathGraphAlgorithm;

    public PathService(StationDao stationDao, PathGraphAlgorithm pathGraphAlgorithm) {
        this.stationDao = stationDao;
        this.pathGraphAlgorithm = pathGraphAlgorithm;
    }

    public PathResponse findShortPath(Long source, Long target) {
        List<Station> shortestPath = pathGraphAlgorithm.getShortestPath(source, target);
        int shortestDistance = pathGraphAlgorithm.getShortestDistance(source, target);
        List<StationResponse> stationResponses = stationsToStationResponses(shortestPath);
        return new PathResponse(stationResponses, shortestDistance);
    }

    private List<StationResponse> stationsToStationResponses(List<Station> shortestPath) {
        return shortestPath.stream()
                .map(station -> stationDao.findById(station.getId()))
                .map(StationResponse::of)
                .collect(Collectors.toList());
    }
}
