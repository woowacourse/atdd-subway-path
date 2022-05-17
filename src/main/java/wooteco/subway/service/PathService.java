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
import wooteco.subway.dto.request.PathRequest;
import wooteco.subway.dto.response.PathResponse;

@Service
public class PathService {

    private StationDao stationDao;
    private SectionDao sectionDao;

    public PathService(StationDao stationDao, SectionDao sectionDao) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
    }

    public PathResponse createShortestPath(PathRequest pathRequest) {
        Long source = pathRequest.getSource();
        Long target = pathRequest.getTarget();

        List<Section> sections = sectionDao.findAll();

        Path path = new Path(new Sections(sections));
        List<Long> shortestPath = path.createShortestPath(source, target);

        List<Station> stations = shortestPath.stream()
                .map(station -> stationDao.findById(station))
                .collect(Collectors.toList());

        Long distance = Long.valueOf(path.calculateDistance(source, target));

        return new PathResponse(stations, distance, 0);
    }
}
