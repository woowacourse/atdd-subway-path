package wooteco.subway.path;

import java.util.List;
import java.util.stream.Collectors;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.line.dto.SectionResponse;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

public class PathService {

    private final StationService stationService;
    private final SectionDao sectionDao;

    public PathService(StationService stationService, SectionDao sectionDao) {
        this.stationService = stationService;
        this.sectionDao = sectionDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        Station sourceStation = stationService.findStationById(sourceId);
        Station targetStation = stationService.findStationById(targetId);
        Sections sections = new Sections(convertToSection(sectionDao.findAll()));
        Path path = new Path(sourceStation, targetStation, sections);
        return new PathResponse(convertToStationResponse(path.makePath()), path.totalDistance());
    }

    private List<Section> convertToSection(List<SectionResponse> responses) {
        return responses.stream()
            .map(response -> {
                Station upStation = stationService.findStationById(response.getUpStationId());
                Station downStation = stationService.findStationById(response.getDownStationId());
                return new Section(response.getId(), upStation, downStation, response.getDistance());
            })
            .collect(Collectors.toList());
    }

    private List<StationResponse> convertToStationResponse(List<Station> stations) {
        return stations.stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());
    }
}
