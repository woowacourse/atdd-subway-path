package wooteco.subway.service;

import org.springframework.stereotype.Service;
import wooteco.subway.controller.dto.response.PathResponse;
import wooteco.subway.controller.dto.response.StationResponse;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

import java.util.List;

@Service
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse findPath(Long sourceStationId, Long targetStationId) {
        List<Line> allLines = lineDao.findAll();
        Station sourceStation = stationDao.findById(sourceStationId);
        Station targetStation = stationDao.findById(targetStationId);
        Path path = new Path(allLines, sourceStation, targetStation);
        List<Station> shortestPath = path.findShortestPath();
        List<StationResponse> shortestPathResponseDto = StationResponse.listOf(shortestPath);
        return new PathResponse(shortestPathResponseDto, path.findTotalDistance());
    }
}
