package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayMap;
import wooteco.subway.dto.path.PathResponse;
import wooteco.subway.exception.station.NoSuchStationException;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;

    public PathService(final SectionDao sectionDao, final StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
    }

    public PathResponse find(final Long sourceStationId, final Long targetStationId) {
        final SubwayMap subwayMap = toSubwayMap();
        final Station sourceStation = findStationById(sourceStationId);
        final Station targetStation = findStationById(targetStationId);

        final List<Station> stations = subwayMap.searchPath(sourceStation, targetStation);
        final Distance distance = subwayMap.searchDistance(sourceStation, targetStation);
        final Fare fare = distance.calculateFare();

        return PathResponse.of(stations, distance, fare);
    }

    private SubwayMap toSubwayMap() {
        final Sections sections = sectionDao.findAll();
        return new SubwayMap(sections);
    }

    private Station findStationById(final Long stationId) {
        return stationDao.findById(stationId)
                .orElseThrow(NoSuchStationException::new);
    }
}
