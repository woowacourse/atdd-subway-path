package wooteco.subway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;
import wooteco.subway.domain.*;
import wooteco.subway.service.dto.PathServiceRequest;
import wooteco.subway.service.dto.PathServiceResponse;
import wooteco.subway.service.dto.StationServiceResponse;

@Service
@Transactional(readOnly = true)
public class PathService {

    private final SectionDao sectionDao;
    private final StationDao stationDao;
    private final LineDao lineDao;

    public PathService(SectionDao sectionDao, StationDao stationDao, LineDao lineDao) {
        this.sectionDao = sectionDao;
        this.stationDao = stationDao;
        this.lineDao = lineDao;
    }

    public PathServiceResponse findShortestPath(PathServiceRequest pathRequest) {
        Path path = Path.of(new Dijkstra(new Sections(sectionDao.findAll())), pathRequest.getSource(), pathRequest.getTarget());
        final Lines lines = new Lines(lineDao.findByIds(new ArrayList<>(path.getLineIds())));
        final Line line = lines.getMaxExtraFare();
        int fee = Fare.calculateFare(path.getShortestDistance(), line.getExtraFare(), pathRequest.getAge());

        return new PathServiceResponse(getShortestPathStations(path.getShortestPath()), path.getShortestDistance(), fee);
    }

    private List<StationServiceResponse> getShortestPathStations(List<Long> shortestPath) {
        Stations stations = new Stations(stationDao.findById(shortestPath));
        return toStationServiceResponse(stations.arrangeStationsByIds(shortestPath));
    }

    private List<StationServiceResponse> toStationServiceResponse(List<Station> stations) {
        return stations.stream()
                .map(i -> new StationServiceResponse(i.getId(), i.getName()))
                .collect(Collectors.toList());
    }
}
