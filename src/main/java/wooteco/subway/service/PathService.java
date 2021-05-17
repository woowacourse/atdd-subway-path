package wooteco.subway.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Sections;
import wooteco.subway.domain.Station;
import wooteco.subway.domain.Stations;
import wooteco.subway.web.dto.PathResponse;
import wooteco.subway.web.dto.StationResponse;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse getShortestPath(Long sourceStationId, Long targetStationId) {
        List<Station> stationsGroup = stationDao.findAll();
        Stations stations = new Stations(stationsGroup);
        List<Section> sectionsGroup = sectionDao.findAll();
        Sections sections = new Sections(sectionsGroup);

        Station sourceStation = stations.findStationById(sourceStationId);
        Station targetStation = stations.findStationById(targetStationId);

        Path path = new Path(stations, sections);
        List<Station> shortestPath
            = path.calculateShortestPath(sourceStation, targetStation);
        List<StationResponse> stationResponses = convertStationToDto(shortestPath);

        double shortestDistance
            = path.calculateShortestDistance(sourceStation, targetStation);

        return new PathResponse(stationResponses, (int) shortestDistance);
    }

    private List<StationResponse> convertStationToDto(List<Station> stations) {
        return stations.stream()
            .map(station -> new StationResponse(station.getId(), station.getName()))
            .collect(Collectors.toList());
    }
}
