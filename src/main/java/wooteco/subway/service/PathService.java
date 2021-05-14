package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.controller.dto.response.PathResponseDto;
import wooteco.subway.controller.dto.response.StationResponseDto;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

@Service
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(LineDao lineDao, StationDao stationDao) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponseDto findPath(Long sourceStationId, Long targetStationId) {
        List<Line> allLines = lineDao.findAll();
        Station sourceStation = stationDao.findById(sourceStationId);
        Station targetStation = stationDao.findById(targetStationId);
        Path path = new Path(allLines, sourceStation, targetStation);
        List<Station> shortestPath = path.findShortestPath();
        List<StationResponseDto> shortestPathResponseDto = StationResponseDto.listOf(shortestPath);
        return new PathResponseDto(shortestPathResponseDto, path.findTotalDistance());
    }
}
