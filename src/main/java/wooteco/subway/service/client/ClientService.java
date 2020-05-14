package wooteco.subway.service.client;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.Path;
import wooteco.subway.dto.LineDetailResponse;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;
import wooteco.subway.dto.WholeSubwayResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class ClientService {
    private LineRepository lineRepository;
    private StationRepository stationRepository;

    public ClientService(LineRepository lineRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.stationRepository = stationRepository;
    }

    public WholeSubwayResponse wholeLines() {
        List<Line> lines = lineRepository.findAll();
        return lines.stream()
            .map(line -> {
                List<Station> stations = stationRepository.findAllById(line.getLineStationsId());
                return LineDetailResponse.of(line, stations);
            })
            .collect(Collectors.collectingAndThen(Collectors.toList(), WholeSubwayResponse::of));
    }

    @Transactional(readOnly = true)
    public PathResponse searchPathByShortestDistance(String source, String target) {
        List<Line> lines = lineRepository.findAll();

        List<Station> stations = stationRepository.findAll();

        Path path = new Path(lines, stations, source, target);

        return new PathResponse(StationResponse.listOf(path.getVertexList()), path.getWeight(),
            path.duration());
    }

}
