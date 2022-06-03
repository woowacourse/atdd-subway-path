package wooteco.subway.domain.path;

import wooteco.subway.domain.Section;

import java.util.List;

public interface PathDecisionStrategy {

    PathDecision decidePath(final List<Section> sections, final Path path);
}
