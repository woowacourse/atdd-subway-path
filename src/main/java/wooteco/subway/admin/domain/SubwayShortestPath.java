package wooteco.subway.admin.domain;

import java.util.List;

public interface SubwayShortestPath {
	List<Station> getVertexList();

	int getDistance();

	int getDuration();
}
