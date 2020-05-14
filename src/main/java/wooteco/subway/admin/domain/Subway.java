package wooteco.subway.admin.domain;

import java.util.List;
import java.util.Objects;

public class Subway {
	private final List<Line> lines;

	public Subway(final List<Line> lines) {
		validateLines(lines);
		this.lines = lines;
	}

	private void validateLines(List<Line> lines) {
		if (Objects.isNull(lines)) {
			throw new IllegalArgumentException("List<Line>이 null일 수 없습니다.");
		}
	}
}
