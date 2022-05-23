package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.FareCalculator;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Service
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(SectionDao sectionDao, StationDao stationDao, final LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathResponse findPath(Long source, Long target, int age) {
        List<Section> sections = sectionDao.findAll();
        Path path = new Path(sections);

        List<Long> shortestPath = path.calculateShortestPath(source, target);
        int shortestDistance = path.calculateShortestDistance(source, target);
        List<Long> lineIds = path.calculateExtraFare(source, target);

        List<StationResponse> stationResponses = shortestPath.stream()
                .map(stationDao::getById)
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new PathResponse(stationResponses, shortestDistance,
                FareCalculator.calculate(shortestDistance) + getExtraFare(lineIds));
    }

    private int getExtraFare(List<Long> lineIds) {
        return lineIds.stream()
                .mapToInt(lineDao::getExtraFare)
                .max()
                .orElse(0);
    }
}
