package wooteco.subway.admin.domain.subwayShortestPath;

import java.util.List;

import wooteco.subway.admin.domain.Station;

public interface SubwayShortestPath {
	List<Station> getVertexList();

	int getDistance();

	int getDuration();
}
