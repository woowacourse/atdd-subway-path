package wooteco.subway.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.domain.Distance;
import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.fare.Fare;
import wooteco.subway.domain.subwaymap.SubwayMap;

@Service
@Transactional
public class PathService {

    private final LineService lineService;
    private final StationService stationService;

    public PathService(final LineService lineService, final StationService stationService) {
        this.lineService = lineService;
        this.stationService = stationService;
    }

    @Transactional(readOnly = true)
    public Path find(final Long sourceStationId, final Long targetStationId, final int age) {
        final SubwayMap subwayMap = toSubwayMap();
        final Station sourceStation = stationService.findById(sourceStationId);
        final Station targetStation = stationService.findById(targetStationId);

        final List<Station> stations = subwayMap.searchPath(sourceStation, targetStation);
        final Distance distance = subwayMap.searchDistance(sourceStation, targetStation);
        final int extraFare = subwayMap.calculateMaxExtraFare(sourceStation, targetStation);
        final Fare fare = Fare.from(extraFare)
                .addExtraFareByDistance(distance)
                .discountByAge(age);

        return new Path(stations, distance, fare);
    }

    private SubwayMap toSubwayMap() {
        final List<Line> lines = lineService.findAll();
        return new SubwayMap(lines);
    }
}
