package wooteco.subway.application;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.domain.Fare;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.dto.PathResponse;

@Service
public class PathService {

    private final StationService stationService;
    private final LineService lineService;
    private final SectionDao sectionDao;

    public PathService(StationService stationService, LineService lineService, SectionDao sectionDao) {
        this.stationService = stationService;
        this.lineService = lineService;
        this.sectionDao = sectionDao;
    }

    public PathResponse getPath(Long sourceStationId, Long targetStationId) {
        Station departure = stationService.findStationById(sourceStationId);
        Station arrival = stationService.findStationById(targetStationId);

        List<Section> sections = sectionDao.findAll();
        final Path path = Path.from(sections, departure, arrival);
        final int extraFare = lineService.findMaxExtraFareByLineIds(path.getUsedLines());
        final Fare fare = Fare.from(path.getDistance(), extraFare);
        return PathResponse.of(path, fare);
    }
}
