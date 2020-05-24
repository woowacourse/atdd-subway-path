package wooteco.subway.admin.acceptance;

import static java.util.Arrays.*;
import static java.util.stream.Collectors.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import wooteco.subway.admin.dto.LineResponse;
import wooteco.subway.admin.dto.ShortestPathResponse;
import wooteco.subway.admin.dto.StationResponse;

public class ShortestPathAcceptanceTest extends AcceptanceTest {
	private static final String DURATION = "DURATION";
	private static final String DISTANCE = "DISTANCE";

	private StationResponse stationResponse1;
	private StationResponse stationResponse2;
	private StationResponse stationResponse3;
	private StationResponse stationResponse4;
	private StationResponse stationResponse5;
	private StationResponse isolatedStation;

	private LineResponse lineTwoResponse;
	private LineResponse lineThreeResponse;
	private LineResponse isolatedLineResponse;

	private static Stream<Arguments> searchTypeStationsDistanceDurationSets() {
		return Stream.of(
			Arguments.of(DISTANCE, asList(STATION_NAME_KANGNAM, STATION_NAME_YEOKSAM,
				STATION_NAME_SEOLLEUNG, STATION_NAME_SADANG), 13, 10),
			Arguments.of(DURATION, asList(STATION_NAME_KANGNAM, STATION_NAME_YEOKSAM,
				STATION_NAME_SADANG), 14, 7)
		);
	}

	@Override
	@BeforeEach
	void setUp() {
		super.setUp();
		//given
		stationResponse1 = createStation(STATION_NAME_KANGNAM);
		stationResponse2 = createStation(STATION_NAME_YEOKSAM);
		stationResponse3 = createStation(STATION_NAME_SEOLLEUNG);
		stationResponse4 = createStation(STATION_NAME_SADANG);
		stationResponse5 = createStation(STATION_NAME_JAMSIL);
		isolatedStation = createStation(STATION_NAME_DANGGOGAE);

		lineTwoResponse = createLine(LINE_NAME_2);
		lineThreeResponse = createLine(LINE_NAME_3);
		isolatedLineResponse = createLine(LINE_NAME_BUNDANG);

		//2호선 노선안에 4개의 역을 구간으로 추가한다.
		addLineStation(lineTwoResponse.getId(), null, stationResponse1.getId(), 0, 0);
		addLineStation(lineTwoResponse.getId(), stationResponse1.getId(), stationResponse2.getId(), 5, 2);
		addLineStation(lineTwoResponse.getId(), stationResponse2.getId(), stationResponse3.getId(), 5, 3);
		addLineStation(lineTwoResponse.getId(), stationResponse3.getId(), stationResponse4.getId(), 3, 5);

		//3호선 노선안에 3개의 역을 구간으로 추가한다. 아울러, 2호선과 3호선은 연결되어있다.
		addLineStation(lineThreeResponse.getId(), null, stationResponse4.getId(), 0, 0);
		addLineStation(lineThreeResponse.getId(), stationResponse4.getId(), stationResponse2.getId(), 9, 5);
		addLineStation(lineThreeResponse.getId(), stationResponse2.getId(), stationResponse5.getId(), 3, 2);

		//분당선 노선 안에 1개의 역을 구간으로 추가한다. 분당선은 2호선, 3호선과 연결되어 있지 않다.
		addLineStation(isolatedLineResponse.getId(), null, isolatedStation.getId(), 0);
	}

	@DisplayName("출발지와 도착지를 입력받아 해당 구간정보를 찾는다.")
	@ParameterizedTest
	@MethodSource("searchTypeStationsDistanceDurationSets")
	void findShortestPath(String type, List<String> expectedStations, int expectedDistance,
		int expectedDuration) {
		//when
		ShortestPathResponse shortestPath = findShortestPath(STATION_NAME_KANGNAM, STATION_NAME_SADANG, type);
		//then
		assertThat(mapStationsToNames(shortestPath)).isEqualTo(expectedStations);
		assertThat(shortestPath.getDistance()).isEqualTo(expectedDistance);
		assertThat(shortestPath.getDuration()).isEqualTo(expectedDuration);
	}

	@DisplayName("출발지와 도착지가 같은 경우, 출발지 혹은 도착지가 비어있는경우, 출발지와 도착지가 연결되어있지 않는 경우 400 상태 코드 반환")
	@ParameterizedTest
	@MethodSource("sourceTargetPathSearchType")
	void findShortestPathException(String source, String target, String type) {
		//when
		assertThatThrownBy(() -> findShortestPath(source, target, type));
	}

	private static Stream<Arguments> sourceTargetPathSearchType() {
		return Stream.of(
			Arguments.of(STATION_NAME_KANGNAM, STATION_NAME_KANGNAM, DISTANCE),
			Arguments.of(STATION_NAME_KANGNAM, STATION_NAME_KANGNAM, DURATION),
			Arguments.of(null, STATION_NAME_KANGNAM, DISTANCE),
			Arguments.of(null, STATION_NAME_KANGNAM, DURATION),
			Arguments.of(STATION_NAME_KANGNAM, null, DISTANCE),
			Arguments.of(STATION_NAME_KANGNAM, null, DISTANCE),
			Arguments.of(null, null, DURATION),
			Arguments.of(null, null, DURATION),
			Arguments.of(STATION_NAME_KANGNAM, STATION_NAME_DANGGOGAE, DISTANCE),
			Arguments.of(STATION_NAME_KANGNAM, STATION_NAME_DANGGOGAE, DURATION)
		);
	}

	ShortestPathResponse findShortestPath(String source, String target, String type) {
		return
			given().
				when().
				param("source", source).
				param("target", target).
				param("type", type).
				get("/paths").
				then().
				log().all().
				statusCode(HttpStatus.OK.value()).
				extract().as(ShortestPathResponse.class);
	}

	private List<String> mapStationsToNames(ShortestPathResponse shortestPath) {
		return shortestPath.getStations().stream()
			.map(StationResponse::getName)
			.collect(collectingAndThen(toList(), Collections::unmodifiableList));
	}
}
