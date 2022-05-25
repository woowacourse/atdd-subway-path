package wooteco.subway;

import static java.util.stream.Collectors.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import wooteco.subway.domain.line.Line;
import wooteco.subway.domain.line.section.Section;
import wooteco.subway.domain.Station;
import wooteco.subway.service.LineService;
import wooteco.subway.service.StationService;

@Component
public class LinesFixture {

	private final LineService lineService;
	private final StationService stationService;

	public LinesFixture(LineService lineService, StationService stationService) {
		this.lineService = lineService;
		this.stationService = stationService;
	}

	//            신사 (3호선)
	//            |10|
	//            잠원
	//            |10|
	// 내방 >10> 고속터미널 >10> 반포 >10> 논현 (7호선)
	//            |10| \14\
	//    (새호선) 서초 >3> 사평
	//						(9호선)
	public static List<Line> toList() {
		Line line7 = new Line("7호선", "red", 700,
			List.of(
				makeSection("내방역", "고속터미널역", 10),
				makeSection("고속터미널역", "반포역", 10),
				makeSection("반포역", "논현역", 10))
		);

		Line line3 = new Line("3호선", "blue", 300,
			List.of(
				makeSection("신사역", "잠원역", 10),
				makeSection("잠원역", "고속터미널역", 10),
				makeSection("고속터미널역", "서초역", 10))
		);

		Line line9 = new Line("9호선", "yellow", 900,
			List.of(makeSection("고속터미널역", "사평역", 14)));

		Line newLine = new Line("새호선", "black", 0,
			List.of(makeSection("서초역", "사평역", 3))
		);
		return List.of(line7, line3, line9, newLine);
	}

	private static Section makeSection(String source, String target, int distance) {
		return new Section(new Station(source), new Station(target), distance);
	}

	public Map<String, Station> initAndReturnStationMap() {
		Map<String, Station> stations = new HashMap<>(saveStations("내방역", "고속터미널역", "반포역", "논현역"));

		createLine7(stations);
		stations.putAll(saveStations("신사역", "잠원역", "서초역"));
		createLine3(stations);

		Station sapyung = stationService.create("사평역");
		createLine9(stations, sapyung);

		lineService.create("새호선", "red",
			makeSection(stations.get("서초역"), sapyung, 3), 0);
		return stations;
	}

	private void createLine7(Map<String, Station> stations) {
		Line line7 = lineService.create("7호선", "red",
			new Section(stations.get("내방역"), stations.get("고속터미널역"),
				10), 700);
		lineService.addSection(line7.getId(),
			makeSection(stations.get("고속터미널역"), stations.get("반포역"), 10));
		lineService.addSection(line7.getId(),
			makeSection(stations.get("반포역"), stations.get("논현역"), 10));
	}

	private void createLine3(Map<String, Station> stations) {
		Line line3 = lineService.create("3호선", "red",
			new Section(stations.get("신사역"), stations.get("잠원역"), 10), 300);
		lineService.addSection(line3.getId(),
			makeSection(stations.get("잠원역"), stations.get("고속터미널역"), 10));
		lineService.addSection(line3.getId(),
			makeSection(stations.get("고속터미널역"), stations.get("서초역"), 10));
	}

	private void createLine9(Map<String, Station> stations, Station sapyung) {
		stations.put("사평역", sapyung);
		lineService.create("9호선", "red", makeSection(
			stations.get("고속터미널역"), sapyung, 14), 900);
	}

	private Map<String, Station> saveStations(String... names) {
		return Arrays.stream(names)
			.collect(toMap(name -> name, stationService::create));
	}

	private Section makeSection(Station source, Station target, int distance) {
		return new Section(source, target, distance);
	}
}
