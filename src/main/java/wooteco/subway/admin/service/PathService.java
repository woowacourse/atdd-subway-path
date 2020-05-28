package wooteco.subway.admin.service;

import static java.util.stream.Collectors.*;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathSearchType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NoSuchStationException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Transactional
@Service
public class PathService {
    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public ShortestPathResponse getShortestPath(String source, String target, PathSearchType type) {
        Station sourceStation = stationRepository.findByName(source)
            .orElseThrow(() -> new NoSuchStationException("출발"));
        Station targetStation = stationRepository.findByName(target)
            .orElseThrow(() -> new NoSuchStationException("도착"));
        Long sourceStationId = sourceStation.getId();
        Long targetStationId = targetStation.getId();

        Path graphLines = new Path(lineRepository.findAll(), sourceStationId, targetStationId,
            type);
        List<Long> shortestPath = graphLines.findShortestPath();
        List<StationResponse> stationResponses = StationResponse.listOf(
            findStationsByIds(shortestPath));
        int distance = graphLines.calculateDistance(shortestPath, type);
        int duration = graphLines.calculateDuration(shortestPath, type);

        return new ShortestPathResponse(stationResponses, distance, duration);
    }

    private List<Station> findStationsByIds(List<Long> shortestPath) {
        return shortestPath.stream()
            .map(stationId -> stationRepository.findById(stationId)
                .orElseThrow(IllegalAccessError::new))
            .collect(toList());
    }
}
