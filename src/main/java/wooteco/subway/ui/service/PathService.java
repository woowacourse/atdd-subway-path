package wooteco.subway.ui.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.PathSearcher;
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

    public PathResponse getPath(Long source, Long target) {
        List<Section> sections = sectionDao.findAll();
        List<Station> stations = stationDao.findAll();
        PathSearcher pathSearcher = PathSearcher.from(stations, sections);

        Station sourceStation = stationDao.findById(source);
        Station targetStation = stationDao.findById(target);
        List<Station> path = pathSearcher.searchShortestPath(sourceStation, targetStation);

        Distance distance = pathSearcher.calculateShortestDistance(sourceStation, targetStation);
        int fare = distance.calculateFare();

        return new PathResponse(StationResponse.of(path), distance.getValue(), fare);
    }
}
