package wooteco.subway.path.event.handler;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import wooteco.subway.line.event.LineUpdatedEvent;
import wooteco.subway.path.domain.PathRepository;

@Component
public class LineUpdateEventHandler {
    private PathRepository pathRepository;

    public LineUpdateEventHandler(final PathRepository pathRepository) {
        this.pathRepository = pathRepository;
    }

    @EventListener
    public void updateLine(LineUpdatedEvent event) {
        pathRepository.generateAllPath(event.getLine());
    }
}
