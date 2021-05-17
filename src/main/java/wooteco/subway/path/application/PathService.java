package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.dao.SectionTable;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.application.StationService;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.ArrayList;
import java.util.List;

@Service
public class PathService {
    private final StationService stationService;
    private final SectionDao sectionDao;

    public PathService(StationService stationService, SectionDao sectionDao) {
        this.stationService = stationService;
        this.sectionDao = sectionDao;
    }


    public PathResponse searchPath(Long sourceStationId, Long targetStationId) {
        Station sourceStation = stationService.findStationById(sourceStationId);
        Station targetStation = stationService.findStationById(targetStationId);
        List<SectionTable> sectionTables = sectionDao.findAll();
        List<Section> sections = new ArrayList<>();
        for (SectionTable sectionTable: sectionTables){
            Station upStation = stationService.findStationById(sectionTable.getUpStationId());
            Station downStation = stationService.findStationById(sectionTable.getDownStationId());
            sections.add(new Section(sectionTable.getId(), upStation, downStation, sectionTable.getDistance()));
        }

        Path path = new Path(sourceStation, targetStation, sections);
        List<Station> shortestStations = path.getShortestStations();
        int shortestDistance = path.getShortestDistance();

        return new PathResponse(StationResponse.listOf(shortestStations), shortestDistance);
    }
}
