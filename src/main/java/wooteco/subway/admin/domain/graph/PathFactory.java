package wooteco.subway.admin.domain.graph;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;

import wooteco.subway.admin.domain.entity.Line;
import wooteco.subway.admin.domain.entity.Station;

public class PathFactory {
	public static Graph<Long, SubwayEdge> from(List<Line> allLines, Map<Long, Station> allStationsById) {
		return createMultiGraph(allLineStations, allStationsById, weightType.getWeightStrategy());
	}
}
