package wooteco.subway.path.application;

import org.springframework.stereotype.Service;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.line.domain.Line;
import wooteco.subway.path.domain.Path;
import wooteco.subway.path.dto.PathResponse;

import java.util.List;

@Service
public class PathService {
    private final LineDao lineDao;

    public PathService(final LineDao lineDao) {
        this.lineDao = lineDao;
    }

    public PathResponse findShortestPath(final Long sourceId, final Long targetId) {
        List<Line> lines = lineDao.findAll();
        Path path = new Path(lines);

        return path.findShortestPath(sourceId, targetId);
    }
}
