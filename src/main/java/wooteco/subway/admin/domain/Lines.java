package wooteco.subway.admin.domain;

import java.util.List;

/**
 *   class description
 *
 *   @author ParkDooWon
 */
public class Lines {
	private List<Line> lines;

	private Lines(List<Line> lines) {
		this.lines = lines;
	}

	public static Lines of(List<Line> lines) {
		return new Lines(lines);
	}

	public List<Line> getLines() {
		return lines;
	}
}
