package wooteco.subway.ui.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.ExtraFare;
import wooteco.subway.domain.PathCalculator;
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
        List<Station> stations = stationDao.findAll();
        PathCalculator pathCalculator = PathCalculator.from(stations, sections);

        Station sourceStation = stationDao.findById((long)source);
        Station targetStation = stationDao.findById((long)target);
        List<Station> path = pathCalculator.calculateShortestPath(sourceStation, targetStation);

        double distance = pathCalculator.calculateShortestDistance(sourceStation, targetStation);
        int fare = ExtraFare.calculateTotalFare(distance);

        return new PathResponse(StationResponse.of(path), distance, fare);
    }
}
