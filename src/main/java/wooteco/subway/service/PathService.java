package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(SectionRepository sectionRepository,
                       StationRepository stationRepository) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findPath(Long sourceId, Long targetId, int age) {
        Sections sections = sectionRepository.findAll();
        Station source = stationRepository.findById(sourceId);
        Station target = stationRepository.findById(targetId);
        Path path = new Path(sections.findShortestPath(source, target));
        return new PathResponse(path.findStationsOnPath(), path.calculateShortestDistance(), path.chargeFare());
    }
}
