package wooteco.subway.ui.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {
    private final SectionDao sectionDao;
    private final LineDao lineDao;
    private final StationDao stationDao;

    public PathService(SectionDao sectionDao, LineDao lineDao, StationDao stationDao) {
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
        this.stationDao = stationDao;
    }

    public PathResponse getPath(PathRequest pathRequest) {
        long sourceStationId = pathRequest.getSource();
        long targetStationId = pathRequest.getTarget();
        int age = pathRequest.getAge();
        return getPath(sourceStationId, targetStationId, age);
    }

    private PathResponse getPath(long source, long target, int age) {
        List<Line> lines = lineDao.findAll();
        PathCalculator pathCalculator = PathCalculator.from(lines);

        Station sourceStation = stationDao.findById(source)
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));
        Station targetStation = stationDao.findById(target)
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 역이 존재하지 않습니다."));

        List<Station> path = pathCalculator.calculateShortestPath(sourceStation, targetStation);
        List<Long> lineIds = pathCalculator.calculateShortestPathLines(sourceStation, targetStation);
        int maxExtraFare = lineIds.stream()
                .map(lineDao::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
        double distance = pathCalculator.calculateShortestDistance(sourceStation, targetStation);

        return new PathResponse(StationResponse.of(path), distance, Fare.of(distance, maxExtraFare, age));
    }
}
