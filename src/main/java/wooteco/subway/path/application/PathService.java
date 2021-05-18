package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.line.domain.Sections;
import wooteco.subway.path.domain.Graph;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;
import wooteco.subway.station.domain.Station;
import wooteco.subway.station.dto.StationResponse;

import java.util.Arrays;
import java.util.List;

@Service
public class PathService {
    private final SectionDao sectionDao;

    public PathService(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    public PathResponse findPath(Long sourceId, Long targetId) {
        List<Section> sections = sectionDao.findByStationIds(Arrays.asList(sourceId, targetId));
        Graph graph = new Graph(sections);
        Path path = graph.shortestPath(new Station(sourceId), new Station(targetId));
        return new PathResponse(StationResponse.listOf(path.stations()), (int) path.distance());
    }
}
