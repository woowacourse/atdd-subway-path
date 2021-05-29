package wooteco.subway.path.event;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import wooteco.subway.line.event.LinesEvent;
import wooteco.subway.path.domain.PathGraphAlgorithm;

@Component
public class PathEventListener {

    private PathGraphAlgorithm pathGraphAlgorithm;

    public PathEventListener(PathGraphAlgorithm pathGraphAlgorithm) {
        this.pathGraphAlgorithm = pathGraphAlgorithm;
    }

    @EventListener
    public void changeGraph(LinesEvent lines) {
        pathGraphAlgorithm.updatePaths(lines.getLines());
    }
}
