package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.line.LineExtraFare;
import wooteco.subway.domain.path.Navigator;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.LineRepository;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository,
                       SectionRepository sectionRepository,
                       StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(long sourceStationId, long targetStationId, int age) {
        Station startStation = stationRepository.findExistingStation(sourceStationId);
        Station endStation = stationRepository.findExistingStation(targetStationId);

        Path path = new Path(startStation, endStation, new Navigator(sectionRepository.findAllSections()));
        List<Long> passingLineIds = path.getPassingLineIds();
        List<LineExtraFare> lineExtraFares = lineRepository.findLineExtraFaresByIds(passingLineIds);
        int fare = path.calculateFare(lineExtraFares, age);

        return PathResponse.of(path, fare);
    }
}
