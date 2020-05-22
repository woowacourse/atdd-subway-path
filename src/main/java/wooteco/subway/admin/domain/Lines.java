package wooteco.subway.admin.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toList;

public class Lines {
	private final List<Line> lines;

	public Lines(final List<Line> lines) {
		validateLines(lines);
		this.lines = lines;
	}

	private void validateLines(List<Line> lines) {
		if (Objects.isNull(lines)) {
			throw new IllegalArgumentException("List<Line>이 null일 수 없습니다.");
		}
	}

	public List<Long> toLineStationIds() {
		return lines.stream()
				.flatMap(line -> line.getLineStationsId().stream())
				.collect(collectingAndThen(toList(),
						Collections::unmodifiableList));
	}

	public List<LineStation> toLineStations() {
		return lines.stream()
				.flatMap(line -> line.getStations().stream())
				.collect(collectingAndThen(toList(),
						Collections::unmodifiableList));
	}

	public List<LineDetail> toLineDetails(Stations stations) {
		return lines.stream()
				.map(line -> LineDetail.of(line, stations.mapStationsBy(line.getLineStationsId())))
				.collect(toList());
	}
}
