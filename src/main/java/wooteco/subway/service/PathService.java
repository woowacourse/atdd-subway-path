package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathGenerator;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.jgraph.JGraphPathGenerator;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
@Transactional
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final PathGenerator pathGenerator;

    public PathService(SectionRepository sectionRepository, StationRepository stationRepository,
                       JGraphPathGenerator pathGenerator) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.pathGenerator = pathGenerator;
    }

    @Transactional(readOnly = true)
    public PathResponse getPath(Long from, Long to) {
        List<Section> allSections = sectionRepository.findAll();
        Station fromStation = stationRepository.findById(from);
        Station toStation = stationRepository.findById(to);
        Path path = pathGenerator.findPath(allSections, fromStation, toStation);
        return PathResponse.of(path, path.calculateFare(0));
    }
}
