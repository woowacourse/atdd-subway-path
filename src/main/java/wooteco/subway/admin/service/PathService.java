package wooteco.subway.admin.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.PathDetail;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.IllegalPathRequestException;
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
    public PathResponse findPath(PathRequest pathRequest) {
        String sourceName = pathRequest.getSource();
        String targetName = pathRequest.getTarget();
        validateDuplicatedName(sourceName, targetName);

        Stations stations = new Stations(stationRepository.findAll());
        Lines lines = new Lines(lineRepository.findAll());

        Long sourceId = stations.findIdByName(sourceName);
        Long targetId = stations.findIdByName(targetName);
        PathDetail path = lines.getShortestPath(sourceId, targetId, pathRequest);
        return PathResponse.of(path, stations);
    }

    private void validateDuplicatedName(final String source, final String target) {
        if (Objects.equals(source, target)) {
            throw new IllegalPathRequestException(String.format("출발역과 도착역이 같습니다.(%s)", source));
        }
    }
}
