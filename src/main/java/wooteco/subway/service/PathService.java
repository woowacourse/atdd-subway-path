package wooteco.subway.service;

import java.util.List;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.JGraphPathFinder;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.PathFinderFactory;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final PathFinderFactory pathFinderFactory;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository,
                       PathFinderFactory pathFinderFactory) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.pathFinderFactory = pathFinderFactory;
    }

    public PathResponse getPath(Long from, Long to) throws Exception {
        PathFinder pathFinder = pathFinderFactory.getObject();
        Station fromStation = stationRepository.findById(from);
        Station toStation = stationRepository.findById(to);
        List<Station> stations = pathFinder.calculatePath(fromStation, toStation);
        int distance = pathFinder.calculateDistance(fromStation, toStation);
        return PathResponse.of(stations, distance, Fare.from(distance));
    }
}
