package wooteco.subway.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import wooteco.subway.LinesFixture;
import wooteco.subway.domain.Path;
import wooteco.subway.domain.Station;

@SpringBootTest
@Transactional
class PathServiceTest {

	@Autowired
	private PathService pathService;
	@Autowired
	private LinesFixture linesFixture;
	private Map<String, Station> stations;

	//            신사 (3호선)
	//            |10|
	// 신반포 (9호선) 잠원
	//       \10\ |10|
	// 내방 >10> 고속터미널 >10> 반포 >10> 논현 (7호선)
	//            |10| \14\
	//            서초 >3> 사평 (새호선)

	@BeforeEach
	void init() {
		stations = linesFixture.initAndReturnStationMap();
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
