package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.PathDetail;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.domain.SubwayGraphKey;
import wooteco.subway.admin.domain.SubwayGraphs;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.exception.IllegalPathRequestException;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.Objects;

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
        String source = pathRequest.getSource();
        String target = pathRequest.getTarget();
        validateDuplicatedName(source, target);

        Stations stations = new Stations(stationRepository.findAll());
        Long sourceId = stations.findIdByName(source);
        Long targetId = stations.findIdByName(target);

        Lines lines = new Lines(lineRepository.findAll());
        SubwayGraphs subwayGraphs = lines.makeSubwayGraphs();
        PathDetail path = subwayGraphs.getPath(sourceId, targetId, SubwayGraphKey.of(pathRequest.getKey()));

        return PathResponse.of(path, stations);
    }

    private void validateDuplicatedName(final String source, final String target) {
        if (Objects.equals(source, target)) {
            throw new IllegalPathRequestException(String.format("출발역과 도착역이 같습니다.(%s)", source));
        }
    }
}
