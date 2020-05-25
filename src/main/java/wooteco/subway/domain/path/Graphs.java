package wooteco.subway.domain.path;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.InvalidPathException;

public class Graphs {
	private static Graphs GRAPHS = new Graphs();
	private static Map<WeightStrategy, Graph> graphs;

	public static Graphs getInstance() {
		return GRAPHS;
	}

	public void create(List<Line> lines, List<Station> stations) {
		if (Objects.isNull(graphs)) {
			graphs = new HashMap<>();
			Arrays.stream(PathType.values())
				.forEach(
					pathType -> graphs.put(pathType.getStrategy(), new Graph(lines, stations, pathType.getStrategy())));
		} else {
			graphs.forEach((pathType, graph) -> graph.create(lines, stations, pathType));
		}
	}

	public Path findPath(String source, String target, String type) {
		validateDuplication(source, target);
		WeightStrategy weightType = PathType.findPathType(type);

		Graph graph = graphs.get(weightType);
		return graph.createPath(source, target);
	}

	private void validateDuplication(String source, String target) {
		if (Objects.equals(source, target)) {
			throw new InvalidPathException(InvalidPathException.DUPLICATE_DEPARTURE_AND_DESTINATION);
		}
	}
}
