package wooteco.subway.ui.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.Fare.FarePolicy;
import wooteco.subway.domain.distance.Kilometer;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse getPath(int source, int target) {
        List<Section> sections = sectionDao.findAll();
        PathCalculator pathCalculator = PathCalculator.from(sections);

        Station sourceStation = findStationById((long)source);
        Station targetStation = findStationById((long)target);
        List<Station> path = pathCalculator.calculateShortestPath(sourceStation, targetStation);

        int distance = pathCalculator.calculateShortestDistance(sourceStation, targetStation);

        int fare = FarePolicy.getFare(Kilometer.from(distance)).value();

        return new PathResponse(StationResponse.of(path), distance, fare);
    }

    private Station findStationById(Long id) {
        return stationDao.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(id + "에 해당하는 역을 찾을 수 없습니다."));
    }
}
