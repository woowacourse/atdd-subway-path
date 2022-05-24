package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.domain.fare.AgeDiscountFare;
import wooteco.subway.domain.fare.BasicFare;
import wooteco.subway.domain.fare.DistanceOverFare;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.fare.LineOverFare;
import wooteco.subway.domain.line.LineInfo;
import wooteco.subway.domain.path.Navigator;
import wooteco.subway.domain.path.NavigatorJgraphtAdapter;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.section.Section;
import wooteco.subway.domain.station.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.repository.SectionRepository;
import wooteco.subway.repository.StationRepository;
import wooteco.subway.repository.LineRepository;

@Service
public class PathService {

    private final LineRepository lineRepository;
    private final SectionRepository sectionRepository;
    private final StationRepository stationRepository;

    public PathService(LineRepository lineRepository, SectionRepository sectionRepository, StationRepository stationRepository) {
        this.lineRepository = lineRepository;
        this.sectionRepository = sectionRepository;
        this.stationRepository = stationRepository;
    }

    public PathResponse findShortestPath(long sourceStationId, long targetStationId, int age) {
        Station startStation = stationRepository.findExistingStation(sourceStationId);
        Station endStation = stationRepository.findExistingStation(targetStationId);
        List<Section> sections = sectionRepository.findAllSections();

        Navigator<Station, Section> navigator = new NavigatorJgraphtAdapter(sections);
        Path path = Path.of(startStation, endStation, navigator);
        int distance = path.getDistance();
        List<LineInfo> lines = lineRepository.findAllLinesByIds(path.getPassingLineIds());
        Fare fare = new BasicFare();
        fare = new DistanceOverFare(fare, distance);
        fare =new LineOverFare(fare, lines);
        fare = new AgeDiscountFare(fare, age);
        return PathResponse.of(path, fare.calculate());
    }
}
