package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.strategy.BasicFareStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.respones.PathResponse;
import wooteco.subway.reopository.SectionRepository;
import wooteco.subway.reopository.StationRepository;

@Service
public class PathService {

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse createShortestPath(PathRequest pathRequest) {
        Station source = stationRepository.findById(pathRequest.getSource(), "최단 경로의 상행역을 찾을 수 없습니다.");
        Station target = stationRepository.findById(pathRequest.getTarget(), "최단 경로의 하행역을 찾을 수 없습니다.");

        List<Section> sections = sectionRepository.findAll();

        Path path = new Path(sections);
        List<Station> shortestPath = path.createShortestPath(source, target);

        int distance = path.calculateDistance(source, target);
        Fare fare = new Fare();

        return new PathResponse(shortestPath, distance,
                fare.calculateFare(distance, new BasicFareStrategy()));
    }
}

