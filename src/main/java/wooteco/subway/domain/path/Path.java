package wooteco.subway.domain.path;

import java.util.List;
import wooteco.subway.domain.Station;

public interface Path {
    List<Station> getVertexList();
    int getWeight();
}
