package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.PathStrategy;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;
    private final PathStrategy pathStrategy;

    public PathService(
        final LineRepository lineRepository,
        final StationRepository stationRepository,
        final PathStrategy pathStrategy
    ) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
        this.pathStrategy = pathStrategy;
    }

    public PathResponse getPath(final PathRequest request) {
        return pathStrategy.getPath(lineRepository.findAll(), stationRepository.findAll(), request);
    }
}
