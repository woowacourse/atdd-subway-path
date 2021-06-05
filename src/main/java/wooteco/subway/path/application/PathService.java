package wooteco.subway.path.application;

import java.util.List;
import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.domain.PathAlgorithms;
import wooteco.subway.path.dto.PathRequest;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.dao.StationDao;
import wooteco.subway.station.domain.Station;

@Service
public class PathService {

    private final StationDao stationDao;
    private final SectionDao sectionDao;
    private final PathAlgorithms pathAlgorithms;

    public PathService(StationDao stationDao, SectionDao sectionDao,
        PathAlgorithms pathAlgorithms) {
        this.stationDao = stationDao;
        this.sectionDao = sectionDao;
        this.pathAlgorithms = pathAlgorithms;
    }

    public void resetGraph() {
        List<Section> sections = sectionDao.findAll();
        pathAlgorithms.resetGraph(sections);
    }

    public PathResponse findPath(PathRequest pathRequest) {
        Station source = stationDao.findById(pathRequest.getSource());
        Station target = stationDao.findById(pathRequest.getTarget());
        Path path = pathAlgorithms.findPath(source, target);

        return PathResponse.of(path);
    }
}
