package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.SubwayGraphs;
import wooteco.subway.admin.dto.PathRequest;
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

    public PathResponse findPath(PathRequest pathRequest) {
        Long sourceId = pathRequest.getSource();
        Long targetId = pathRequest.getTarget();

        //전체 노선을 가져온다
        Lines lines = new Lines(lineRepository.findAll());
        //최단 경로를 가져온다.
        SubwayGraphs subwayGraphs = lines.makeSubwayGraphs(sourceId, targetId);

        return new PathResponse();
    }
}
