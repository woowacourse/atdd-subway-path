package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.Arrays;
import java.util.List;

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
        Stations requestStations = new Stations(stationRepository.findAllByNameIn(Arrays.asList(source, target)));

        Long sourceId = requestStations.findIdByName(source);
        Long targetId = requestStations.findIdByName(target);

        //전체 노선을 가져온다
        Lines lines = new Lines(lineRepository.findAll());
        //최단 경로를 가져온다.
        SubwayGraphs subwayGraphs = lines.makeSubwayGraphs(sourceId, targetId);
        PathDetail path = subwayGraphs.getPath(sourceId, targetId, SubwayGraphKey.of(pathRequest.getKey()));

        List<Station> stations = stationRepository.findAllByIdIn(path.getPaths());
        return PathResponse.of(path, new Stations(stations));
    }
}
