package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathGenerator;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.path.jgraph.JGraphPathGenerator;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
@Transactional
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final PathGenerator pathGenerator;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository,
                       PathGenerator pathGenerator) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.pathGenerator = pathGenerator;
    }

    @Transactional(readOnly = true)
    public Path getPath(Long from, Long to) {
        List<Section> allSections = sectionRepository.findAll();
        Station fromStation = stationRepository.findById(from);
        Station toStation = stationRepository.findById(to);
        return pathGenerator.findPath(allSections, fromStation, toStation);
    }

    public Fare getFare(Long from, Long to, int age) {
        return getPath(from, to).calculateFare(age);
    }
}
