package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathDto;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathDto findShortestPath(Long departureId, Long arrivalId) {
        Sections sections = sectionDao.findAll();
        List<Long> shortestPathStationIds = sections.getShortestPathStationIds(departureId, arrivalId);
        List<Station> stations = shortestPathStationIds.stream()
                .map(stationDao::findById)
                .collect(Collectors.toList());

        int distance = sections.getShortestPathDistance(departureId, arrivalId);
        int fare = FareCalculator.getInstance().calculate(distance);
        return new PathDto(stations, distance, fare);
    }
}
