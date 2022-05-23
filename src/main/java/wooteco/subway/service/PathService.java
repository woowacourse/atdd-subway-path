package wooteco.subway.service;

import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.AgeGroup;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public PathService(StationDao stationDao, SectionDao sectionDao, LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId, int age) {
        final Sections sections = new Sections(sectionDao.findAll());
        final Path path = Path.of(sections, sourceId, targetId);

        final List<Long> shortestPath = path.getShortestPath();
        final List<StationResponse> stations = getStationResponses(shortestPath);

        final int distance = path.getShortestPathWeight();
        final int extraFare = getExtraFare(sections, shortestPath);

        final Fare fare = Fare.of(distance, extraFare);

        return new PathResponse(stations, distance, fare.getDiscountedValue(AgeGroup.from(age)));
    }

    private List<StationResponse> getStationResponses(List<Long> shortestPath) {
        final List<Station> stations = shortestPath.stream()
                .map(stationDao::findById)
                .map(Optional::orElseThrow)
                .collect(toList());

        return stations.stream()
                .map(StationResponse::new)
                .collect(toList());
    }

    private int getExtraFare(Sections sections, List<Long> path) {
        List<Integer> extraFares = new ArrayList<>();
        for (int index = 0; index < path.size() - 1; index++) {
            Section section = sections.getSectionByStationIds(path.get(index), path.get(index + 1));
            final Line line = lineDao.findById(section.getLineId())
                    .orElseThrow(() -> new IllegalArgumentException("해당하는 line이 존재하지 않습니다."));
            extraFares.add(line.getExtraFare());
        }
        return Collections.max(extraFares);
    }
}
