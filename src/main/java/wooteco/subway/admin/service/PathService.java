package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;

import wooteco.subway.admin.domain.DijkstraPath;
import wooteco.subway.admin.domain.PathStrategy;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.dto.PathResponse;
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

    public PathResponse getPath(final String source, final String target, final PathType type) {
        PathStrategy pathStrategy = new DijkstraPath(
            lineRepository.findAll(),
            stationRepository.findAll(),
            source, target
        );
        return pathStrategy.getPath(type);
    }
}
