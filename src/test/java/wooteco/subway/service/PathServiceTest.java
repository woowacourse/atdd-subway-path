package wooteco.subway.service;

import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	private final Map<String, Station> stations = new HashMap<>();

	//            신사 (3호선)
	//            |10|
	// 신반포 (9호선) 잠원
	//       \10\ |10|
	// 내방 >10> 고속터미널 >10> 반포 >10> 논현 (7호선)
	//            |10| \14\
	//            서초 >3> 사평 (새호선)

	@BeforeEach
	void init() {
		stations.putAll(saveStations("내방역", "고속터미널역", "반포역", "논현역"));

		Line line7 = lineService.create("7호선", "red",
			new Section(stations.get("내방역"), stations.get("고속터미널역"), 10));
		lineService.addSection(line7.getId(),
			makeSection(stations.get("고속터미널역"), stations.get("반포역"), 10));
		lineService.addSection(line7.getId(),
			makeSection(stations.get("반포역"), stations.get("논현역"), 10));

		stations.putAll(saveStations("신사역", "잠원역", "서초역"));

		Line line3 = lineService.create("3호선", "red",
			new Section(stations.get("신사역"), stations.get("잠원역"), 10));
		lineService.addSection(line3.getId(),
			makeSection(stations.get("잠원역"), stations.get("고속터미널역"), 10));
		lineService.addSection(line3.getId(),
			makeSection(stations.get("고속터미널역"), stations.get("서초역"), 10));

		Station sapyung = stationService.create("사평역");
		stations.put("사평역", sapyung);
		lineService.create("9호선", "red", makeSection(
			stations.get("고속터미널역"), sapyung, 14));

		lineService.create("새호선", "red",
			makeSection(stations.get("서초역"), sapyung, 3));
	}

	private Map<String, Station> saveStations(String... names) {
		return Arrays.stream(names)
			.collect(toMap(name -> name, name -> stationService.create(name)));
	}

	private Section makeSection(Station source, Station target, int distance) {
		return new Section(source, target, distance);
	}

	@DisplayName("한 라인에서 조회한다.")
	@Test
	void findPath() {
		// given
		Station source = stations.get("내방역");
		Station target = stations.get("논현역");

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
		Station source = stations.get("논현역");
		Station target = stations.get("내방역");

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
		Station source = stations.get("신사역");
		Station target = stations.get("논현역");

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
		Station source = stations.get("내방역");
		Station target = stations.get("사평역");

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
		Station source = stations.get("내방역");
		Station target = stations.get("논현역");

		// when
		Path path = pathService.findPath(source, target);

		// then
		assertThat(path.getDistance()).isEqualTo(30);
	}

	@DisplayName("최단 경로의 요금을 계산한다.")
	@Test
	void fare() {
		// given
		Station source = stations.get("신사역");
		Station target = stations.get("논현역");

		// when
		Path path = pathService.findPath(source, target);

		// then
		assertThat(path.calculateFare()).isEqualTo(1950);
	}
}
