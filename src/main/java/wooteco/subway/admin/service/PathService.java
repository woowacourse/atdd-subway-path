package wooteco.subway.admin.service;

import org.springframework.stereotype.Service;
import wooteco.subway.admin.domain.Lines;
import wooteco.subway.admin.dto.PathResponse;
import wooteco.subway.admin.repository.LineRepository;
import wooteco.subway.admin.repository.StationRepository;

import java.util.Arrays;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final StationRepository stationRepository;

    public PathService(final LineRepository lineRepository, final StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(final Long sourceId, final Long targetId) {
        //전체 노선을 가져온다
        Lines lines = new Lines(lineRepository.findAll());
        //입력받은 역들을 구간으로 갖고 있는 노선들 반환
        Lines pathLine = lines.findByStationIdIn(Arrays.asList(sourceId, targetId));


        return new PathResponse();
    }
}
