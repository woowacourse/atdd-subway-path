package wooteco.subway.ui.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.fare.AgeFarePolicy;
import wooteco.subway.domain.fare.DistanceFarePolicy;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.distance.Kilometer;
import wooteco.subway.domain.fare.Age;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse getPath(PathRequest pathRequest) {
        List<Section> sections = sectionDao.findAll();
        Map<Section, Fare> sectionWithExtraFare = sections.stream()
                .collect(Collectors.toMap(
                        section -> section,
                        section -> sectionDao.findExtraFareById(section.getId())
                ));

        Station sourceStation = findStationById(pathRequest.getSource());
        Station targetStation = findStationById(pathRequest.getTarget());
        Age age = new Age(pathRequest.getAge());

        Path shortestPath = Path.of(sectionWithExtraFare, sourceStation, targetStation);
        List<Station> stations = shortestPath.getStations();
        Kilometer distance = shortestPath.getDistance();
        Fare fare = Fare.basic()
                .add(shortestPath.getExtraFare())
                .apply(distance)
                .apply(age);

        return new PathResponse(StationResponse.of(stations), distance.value(), fare.value());
    }

    private Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 역을 찾을 수 없습니다."));
    }
}
