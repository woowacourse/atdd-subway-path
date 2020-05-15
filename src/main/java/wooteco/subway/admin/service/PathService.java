package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.domain.PathDetail;
import wooteco.subway.admin.domain.Station;
import wooteco.subway.admin.domain.Stations;
import wooteco.subway.admin.domain.SubwayGraphKey;
import wooteco.subway.admin.domain.SubwayGraphs;
import wooteco.subway.admin.dto.PathRequest;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.List;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(PathRequest pathRequest) {
        Station sourceStation = stationRepository.findByName(pathRequest.getSource())
                .orElseThrow(IllegalArgumentException::new);
        Station targetStation = stationRepository.findByName(pathRequest.getTarget())
                .orElseThrow(IllegalArgumentException::new);

        Long sourceId = sourceStation.getId();
        Long targetId = targetStation.getId();

        //전체 노선을 가져온다
        Lines lines = new Lines(lineRepository.findAll());
        //최단 경로를 가져온다.
        SubwayGraphs subwayGraphs = lines.makeSubwayGraphs(sourceId, targetId);
        PathDetail path = subwayGraphs.getPath(sourceId, targetId, SubwayGraphKey.of(pathRequest.getKey()));

        List<Station> stations = stationRepository.findAllByIdIn(path.getPaths());
        return PathResponse.of(path, new Stations(stations));
    }
}
