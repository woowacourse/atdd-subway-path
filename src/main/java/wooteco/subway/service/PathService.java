package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Discounter;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.PathCalculator;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.dto.request.PathRequest;
import wooteco.subway.service.dto.response.PathResponse;

@Service
public class PathService {

    private static final int DEFAULT_EXTRA_FARE = 0;

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final LineDao lineDao;

    public PathService(final StationDao stationDao, final SectionDao sectionDao, final LineDao lineDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.lineDao = lineDao;
    }

    public PathResponse findShortestPath(final PathRequest pathRequest) {
        final Path path = pathRequest.toPath();
        final int age = pathRequest.getAge();

        final PathCalculator pathCalculator = new PathCalculator(sectionDao.findAll());
        final List<Long> stationIds = pathCalculator.findShortestPath(path);
        final double distance = pathCalculator.findShortestDistance(path);
        final double fare = Fare.calculate(distance, getExtraFare(stationIds));

        return PathResponse.from(convertStation(stationIds), distance, Discounter.discount(fare, age));
    }

    private int getExtraFare(final List<Long> stationIds) {
        List<Section> sections = getSectionsIncludedStations(stationIds);
        List<Line> lines = getLinesIncludedSections(sections);
        return lines.stream()
                .mapToInt(Line::getExtraFare)
                .max()
                .orElse(DEFAULT_EXTRA_FARE);
    }

    private List<Section> getSectionsIncludedStations(final List<Long> stationIds) {
        List<Section> sections = new ArrayList<>();
        for (Long stationId : stationIds) {
            sections.add(sectionDao.findByStationId(stationId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지하철역입니다.")));
        }
        return sections;
    }

    private List<Line> getLinesIncludedSections(final List<Section> sections) {
        List<Line> lines = new ArrayList<>();
        for (Section section : sections) {
            lines.add(lineDao.find(section.getLineId())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 노선입니다.")));
        }
        return lines;
    }

    private List<Station> convertStation(final List<Long> stationIds) {
        return stationIds.stream()
                .map(stationDao::findById)
                .map(station -> station.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지하철역입니다.")))
                .collect(Collectors.toList());
    }
}
