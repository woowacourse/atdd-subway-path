package wooteco.subway.admin.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Path;
import wooteco.subway.admin.domain.PathType;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.dto.StationResponse;
import wooteco.subway.admin.exception.NotFoundPathException;
import wooteco.subway.admin.exception.SourceTargetSameException;
import wooteco.subway.admin.repository.PathRepository;
import wooteco.subway.admin.service.utils.StationMapper;

@Service
public class PathService {
    private final StationService stationService;
    private final PathRepository pathRepository;

    public PathService(StationService stationService, PathRepository pathRepository) {
        this.stationService = stationService;
        this.pathRepository = pathRepository;
    }

    public PathResponse findPath(String sourceName, String targetName, String type) {
        validate(sourceName, targetName);

        Station source = stationService.findByName(sourceName);
        Station target = stationService.findByName(targetName);
        PathType pathType = PathType.of(type);

        Path path = pathRepository.findPath(source, target, pathType)
                .orElseThrow(() -> new NotFoundPathException(sourceName, targetName));

        Map<Long, Station> stations = StationMapper.toMap(stationService.findAll());

        List<Station> vertices = path.vertices()
                .stream()
                .map(stations::get)
                .collect(Collectors.toList());

        return new PathResponse(StationResponse.listOf(vertices), path.distance(),
                path.duration());
    }

    private void validate(String sourceName, String targetName) {
        if (Objects.equals(targetName, sourceName)) {
            throw new SourceTargetSameException(sourceName);
        }
    }
}