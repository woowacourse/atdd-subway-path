package wooteco.subway.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.*;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;
    private final LineRepository lineRepository;

    public PathService(SectionRepository sectionRepository,
                       StationRepository stationRepository,
                       LineRepository lineRepository
    ) {
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
        this.lineRepository = lineRepository;
    }

    public PathResponse findPath(Long sourceId, Long targetId, int age) {
        Sections sections = sectionRepository.findAll();
        Station source = stationRepository.findById(sourceId);
        Station target = stationRepository.findById(targetId);
        List<Line> lines = lineRepository.findAll();
        Path path = new Path(sections.findShortestPath(source, target, lines));
        int fare = Fare.chargeFare(path, age);

        return new PathResponse(path.findStationsOnPath(), path.calculateShortestDistance(), fare);
    }
}
