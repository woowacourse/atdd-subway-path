package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.JGraphPathFinder;
import wooteco.subway.domain.PathFinder;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
@Transactional
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    @Transactional(readOnly = true)
    public PathResponse getPath(Long from, Long to) {
        List<Section> allSections = sectionRepository.findAll();
        PathFinder pathFinder = JGraphPathFinder.of(allSections);
        Station fromStation = stationRepository.findById(from);
        Station toStation = stationRepository.findById(to);
        List<Station> stations = pathFinder.calculatePath(fromStation, toStation);
        int distance = pathFinder.calculateDistance(fromStation, toStation);
        return PathResponse.of(stations, distance, Fare.from(distance));
    }
}
