package wooteco.subway.ui.service;

import java.util.List;

import org.springframework.stereotype.Service;

import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.path.Path;
import wooteco.subway.domain.path.PathCalculator;
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;
import wooteco.subway.dto.response.StationResponse;

@Service
public class PathService {
    private final LineDao lineDao;
    private final StationDao stationDao;
    private final PathCalculator pathCalculator;

    public PathService(LineDao lineDao, StationDao stationDao, PathCalculator pathCalculator) {
        this.lineDao = lineDao;
        this.stationDao = stationDao;
        this.pathCalculator = pathCalculator;
    }

    public PathResponse getPath(PathRequest pathRequest) {
        long sourceStationId = pathRequest.getSource();
        long targetStationId = pathRequest.getTarget();
        int age = pathRequest.getAge();
        return getPath(sourceStationId, targetStationId, age);
    }

    private PathResponse getPath(long source, long target, int age) {
        List<Line> lines = lineDao.findAll();

        Station sourceStation = stationDao.findById(source)
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 상행역이 존재하지 않습니다."));
        Station targetStation = stationDao.findById(target)
                .orElseThrow(() -> new IllegalArgumentException("조회하고자 하는 하행역이 존재하지 않습니다."));

        Path path = pathCalculator.calculatePath(sourceStation, targetStation, lines);

        List<Station> stations = path.getStations();
        List<Long> lineIds = path.getLineIds();
        int maxExtraFare = lineDao.findByIds(lineIds).stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(0);
        double distance = path.getDistance();


        return new PathResponse(StationResponse.of(stations), distance, Fare.of(distance, maxExtraFare, age));
    }
}
