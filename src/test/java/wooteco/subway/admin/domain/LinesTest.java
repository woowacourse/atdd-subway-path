package wooteco.subway.admin.domain;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LinesTest {
	private Lines lines;
	private List<Line> lineSources = new ArrayList<>();
	private Line line1;

	@BeforeEach
	void setUp() {
		line1 = new Line(1L, "2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5, "bg-green-500");
		line1.addLineStation(new LineStation(null, 1L, 10, 10));
		line1.addLineStation(new LineStation(1L, 2L, 10, 10));
		line1.addLineStation(new LineStation(2L, 3L, 10, 10));
		lineSources.add(line1);

		lines = new Lines(lineSources);
	}

	@DisplayName("Lines 객체를 생성한다.")
	@Test
	void createSubway_WhenNormalCase_ReturnInstance() {
		List<Line> lines = new ArrayList<>();
		Lines subway = new Lines(lines);
		assertThat(subway).isInstanceOf(Lines.class);
		assertThat(subway).isNotNull();
	}

	@DisplayName("Lines 객체를 생성시 null 값이 주입되면 예외처리한다.")
	@Test
	void createSubway_WhenNull_ThrowException() {
		List<Line> lineSources = null;
		assertThatThrownBy(() -> new Lines(lineSources)).isInstanceOf(IllegalArgumentException.class)
			.hasMessage("List<Line>이 null일 수 없습니다.");
	}

	@DisplayName("lineStationId들을 반환한다.")
	@Test
	void toLineStationIds_ReturnListLong() {
		List<Long> lineStationIds = lines.toLineStationIds();

		assertEquals(lineStationIds.get(0), 1L);
		assertEquals(lineStationIds.get(1), 2L);
		assertEquals(lineStationIds.get(2), 3L);
	}

	@DisplayName("lineStation들을 반환한다.")
	@Test
	void toLineStations_ReturnListLineStation() {
		List<LineStation> lineStationIds = lines.toLineStations();

		assertEquals(lineStationIds.get(0).getPreStationId(), null);
		assertEquals(lineStationIds.get(1).getPreStationId(), 1L);
		assertEquals(lineStationIds.get(2).getPreStationId(), 2L);
	}
}