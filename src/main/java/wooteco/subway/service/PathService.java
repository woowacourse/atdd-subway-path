package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.strategy.BasicFareStrategy;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.reopository.SectionRepository;
import wooteco.subway.reopository.StationRepository;
import wooteco.subway.reopository.dao.SectionDao;
import wooteco.subway.reopository.dao.StationDao;

@Service
public class PathService {

    private static final int BASIC_FARE = 1250;

    private final StationRepository stationRepository;
    private final SectionRepository sectionRepository;

    public PathService(StationRepository stationRepository, SectionRepository sectionRepository) {
        this.stationRepository = stationRepository;
        this.sectionRepository = sectionRepository;
    }

    public PathResponse createShortestPath(PathRequest pathRequest) {
        Long source = pathRequest.getSource();
        Long target = pathRequest.getTarget();

        List<Section> sections = sectionRepository.findAll();

        Path path = new Path(sections);
        List<Long> shortestPath = path.createShortestPath(source, target);

        List<Station> stations = shortestPath.stream()
                .map(station -> stationRepository.findById(station,"해당 역을 찾을 수 없음"))
                .collect(Collectors.toList());

        int distance = path.calculateDistance(source, target);
        Fare fare = new Fare(BASIC_FARE);

        return new PathResponse(stations, distance,
                fare.calculateFare(distance, new BasicFareStrategy()));
    }
}

