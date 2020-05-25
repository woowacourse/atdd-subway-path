package wooteco.subway.domain.path;

import static wooteco.subway.exception.InvalidPathException.DUPLICATE_DEPARTURE_AND_DESTINATION;
import static wooteco.subway.exception.InvalidPathException.NOT_BLANK;
import static wooteco.subway.exception.InvalidPathException.NOT_EXIST_STATION;

import java.util.List;
import java.util.Objects;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Station;
import wooteco.subway.exception.InvalidPathException;

public class PathGenerator {
	private final String sourceName;
	private final String targetName;
	private final WeightStrategy strategy;

	public PathGenerator(String sourceName, String targetName, String type) {
		validate(sourceName, targetName);
		this.strategy = WeightType.findStrategy(type);
		this.sourceName = sourceName;
		this.targetName = targetName;
	}

	private void validate(String source, String target) {
		validateBlank(source, target);
		validateDuplication(source, target);
	}

	private void validateBlank(String source, String target) {
		if (Objects.isNull(source) || Objects.isNull(target) || source.isEmpty() || target.isEmpty()) {
			throw new InvalidPathException(NOT_BLANK);
		}
	}

	private void validateDuplication(String source, String target) {
		if (source.equals(target)) {
			throw new InvalidPathException(DUPLICATE_DEPARTURE_AND_DESTINATION);
		}
	}

	public Path generate(List<Line> lines, List<Station> stations) {
		Station source = findStationByName(stations, sourceName);
		Station target = findStationByName(stations, targetName);
		Graph graph = new Graph(lines, stations, strategy);
		return graph.createPath(source, target);
	}

	private Station findStationByName(List<Station> stations, String stationName) {
		return stations.stream()
			.filter(station -> stationName.equals(station.getName()))
			.findFirst()
			.orElseThrow(() -> new InvalidPathException(NOT_EXIST_STATION, stationName));
	}
}
