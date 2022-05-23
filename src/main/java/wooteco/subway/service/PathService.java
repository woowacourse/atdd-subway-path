package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import org.jgrapht.GraphPath;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.ShortestPathEdge;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.strategy.BasicFareStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.respones.PathResponse;
import wooteco.subway.exception.NotFoundException;
import wooteco.subway.reopository.LineRepository;
import wooteco.subway.reopository.SectionRepository;
import wooteco.subway.reopository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, LineRepository lineRepository,
                       SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse createShortestPath(PathRequest pathRequest) {
        Station source = stationRepository
                .findById(pathRequest.getSource())
                .orElseThrow(() -> new NotFoundException("최단 경로의 상행역을 찾을 수 없습니다."));
        Station target = stationRepository
                .findById(pathRequest.getTarget())
                .orElseThrow(() -> new NotFoundException("최단 경로의 하행역을 찾을 수 없습니다."));

        List<Section> sections = sectionRepository.findAll();

        Path path = new Path(sections);
        GraphPath<Station, ShortestPathEdge> shortestPath = path.createShortestPath(source, target);

        int distance = (int) shortestPath.getWeight();
        Fare fare = new Fare(new BasicFareStrategy());
        int lineExtraFare = fare.calculateMaxLineExtraFare(findLine(shortestPath.getEdgeList()));
        return new PathResponse(shortestPath.getVertexList(), distance,
                fare.calculateFare(distance) + lineExtraFare);
    }

    private List<Line> findLine(List<ShortestPathEdge> edges) {
        List<Line> lines = new ArrayList<>();
        for (ShortestPathEdge edge : edges) {
            lines.add(lineRepository.findById(edge.getLineId())
                    .orElseThrow(() -> new NotFoundException("노선을 찾을 수 없습니다.")));
        }
        return lines;
    }
}

