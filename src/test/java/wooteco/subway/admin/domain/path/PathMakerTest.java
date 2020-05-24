package wooteco.subway.admin.domain.path;

import org.junit.jupiter.api.Test;
import wooteco.subway.admin.domain.PathSearchType;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.admin.domain.path.util.DummyLineStationsCreator.createLineStations;
import static wooteco.subway.admin.domain.path.util.DummyStationsCreator.createStations;

class PathMakerTest {

    @Test
    void computeShortestPath() {
        // given
        PathMaker pathMaker = new PathMaker(createLineStations(), createStations());
        // when
        Path path = pathMaker.computeShortestPath(1L, 4L, PathSearchType.DISTANCE);
        // then
        assertThat(path.getStations().getStations()).hasSize(4);
    }


}