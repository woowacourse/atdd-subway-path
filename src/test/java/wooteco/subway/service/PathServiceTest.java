package wooteco.subway.service;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.domain.Line;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Section;
import wooteco.subway.domain.Station;

@SpringBootTest
@Transactional
class PathServiceTest {

	@Autowired
	private StationService stationService;
	@Autowired
	private LineService lineService;
	@Autowired
	private PathService pathService;

	@BeforeEach
	void init() {
		List<Station> stations7 = saveStations("내방역", "고속터미널역", "반포역", "논현역");
		Line line7 = lineService.create("7호선", "red",
			new Section(stations7.get(0), stations7.get(1), 10));
		lineService.addSection(line7.getId(), makeSection(stations7.get(1), stations7.get(2), 10));
		lineService.addSection(line7.getId(), makeSection(stations7.get(2), stations7.get(3), 10));

		List<Station> stations3 = saveStations("신사역", "잠원역", "서초역");
		Line line3 = lineService.create("3호선", "red",
			new Section(stations3.get(0), stations3.get(1), 10));
		lineService.addSection(line3.getId(), makeSection(stations3.get(1), stations7.get(1), 10));
		lineService.addSection(line3.getId(), makeSection(stations7.get(1), stations3.get(2), 10));

		Station sapyung = stationService.create("사평역");
		lineService.create("9호선", "red", makeSection(
			stations7.get(1), sapyung, 14));

		lineService.create("새호선", "red", makeSection(stations3.get(2), sapyung, 3));
	}

	private List<Station> saveStations(String... names) {
		List<Station> stations = new ArrayList<>();
		for (String name : names) {
			Station station = stationService.create(name);
			stations.add(station);
		}
		return stations;
	}

	private Section makeSection(Station source, Station target, int distance) {
		return new Section(source, target, distance);
	}

	@DisplayName("한 라인에서 조회한다.")
	@Test
	void findPath() {
		// given
		Station source = findByName("내방역");
		Station target = findByName("논현역");

		// when
		Path path = pathService.findPath(source, target);

		// then
		assertThat(path.getStations())
			.map(Station::getName)
			.containsExactly("내방역", "고속터미널역", "반포역", "논현역");
	}

	@DisplayName("한 라인에서 역방향 경로를 조회한다.")
	@Test
	void findPathReverse() {
		// given
		Station source = findByName("논현역");
		Station target = findByName("내방역");

		// when
		List<Station> stations = pathService.findPath(source, target)
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
		Station source = findByName("신사역");
		Station target = findByName("논현역");

		// when
		List<Station> stations = pathService.findPath(source, target)
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
		Station source = findByName("내방역");
		Station target = findByName("사평역");

		// when
		List<Station> stations = pathService.findPath(source, target)
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
		Station source = findByName("내방역");
		Station target = findByName("논현역");

		// when
		Path path = pathService.findPath(source, target);

		// then
		assertThat(path.getDistance()).isEqualTo(30);
	}

	@DisplayName("최단 경로의 요금을 계산한다.")
	@Test
	void fare() {
		// given
		Station source = findByName("신사역");
		Station target = findByName("논현역");

		// when
		Path path = pathService.findPath(source, target);

		// then
		assertThat(path.calculateFare()).isEqualTo(1950);
	}

	private Station findByName(String name) {
		return stationService.findAllStations()
			.stream()
			.filter(station -> station.isSameName(name))
			.findAny()
			.orElseThrow();
	}
}
