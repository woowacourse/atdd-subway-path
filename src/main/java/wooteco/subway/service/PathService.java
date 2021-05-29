package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.SubwayPath;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

import java.util.List;

@Service
public class PathService {
    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse findShortestPath(long fromStationId, long toStationId) {
        Station fromStation = stationDao.findById(fromStationId);
        Station toStation = stationDao.findById(toStationId);

        SubwayPath path = SubwayPath.of(sectionDao.findAll());

        List<Station> stationsOfPath = path.findShortestPath(fromStation, toStation);

        List<StationResponse> stationResponsesOfPath = StationResponse.listOf(stationsOfPath);

        return new PathResponse(stationResponsesOfPath, path.getTotalDistance(fromStation, toStation));
    }
}
