package wooteco.subway.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import wooteco.subway.LinesFixture;

class LinesTest {

	private Lines lines;

	@BeforeEach
	void init() {
		lines = new Lines(LinesFixture.toList());
	}

	@DisplayName("한 라인에서 경로를 순방향 조회한다.")
	@Test
	void singleLine() {
		// given
		Station source = new Station("내방역");
		Station target = new Station("논현역");

		// when
		List<Station> stations = lines.findPath(source, target)
			.getStations();

		// then
		assertThat(stations)
			.map(Station::getName)
			.containsExactly("내방역", "고속터미널역", "반포역", "논현역");
	}

	@DisplayName("한 라인에서 경로를 역방향 조회한다.")
	@Test
	void singleLineReverse() {
		// given
		Station source = new Station("논현역");
		Station target = new Station("내방역");

		// when
		List<Station> stations = lines.findPath(source, target)
			.getStations();

		// then
		assertThat(stations)
			.map(Station::getName)
			.containsExactly("논현역", "반포역", "고속터미널역", "내방역");
	}

	@DisplayName("두 라인이 겹쳤을 때 경로를 조회한다.")
	@Test
	void doubleLine() {
		// given
		Station source = new Station("신사역");
		Station target = new Station("논현역");

		// when
		List<Station> stations = lines.findPath(source, target)
			.getStations();

		// then
		assertThat(stations)
			.map(Station::getName)
			.containsExactly("신사역", "잠원역", "고속터미널역", "반포역", "논현역");
	}

	@DisplayName("두 경로 중 짧은 거리를 선택한다.")
	@Test
	void shortestPath() {
		// given
		Station source = new Station("내방역");
		Station target = new Station("사평역");

		// when
		List<Station> stations = lines.findPath(source, target)
			.getStations();

		// then
		assertThat(stations)
			.map(Station::getName)
			.containsExactly("내방역", "고속터미널역", "서초역", "사평역");
	}

	@DisplayName("최단 경로 거리를 반환한다.")
	@Test
	void pathDistance() {
		// given
		Station source = new Station("내방역");
		Station target = new Station("논현역");

		// when
		Path path = lines.findPath(source, target);

		// then
		assertThat(path.getDistance()).isEqualTo(30);
	}
}
