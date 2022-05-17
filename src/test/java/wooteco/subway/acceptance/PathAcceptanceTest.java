package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.ori.acceptancetest.SpringBootAcceptanceTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import wooteco.subway.controller.dto.LineRequest;
import wooteco.subway.controller.dto.PathResponse;
import wooteco.subway.controller.dto.SectionRequest;
import wooteco.subway.controller.dto.StationRequest;
import wooteco.subway.controller.dto.StationResponse;

@DisplayName("경로 조회 인수 테스트")
@SpringBootAcceptanceTest
public class PathAcceptanceTest {

	private List<Long> stationIds;

	@BeforeEach
	void init() {
		stationIds = RestUtil.postStations("강남역", "역삼역", "선릉역", "교대역", "잠실역");
		LineRequest lineRequest1 = new LineRequest(
			"신분당선", "bg-red-600", stationIds.get(0), stationIds.get(1), 11);
		ExtractableResponse<Response> lineResponse = RestUtil.post(lineRequest1);
		Long lineId = RestUtil.getIdFromLine(lineResponse);
		RestUtil.post(lineId, new SectionRequest(stationIds.get(1), stationIds.get(2), 10));

		LineRequest lineRequest2 = new LineRequest(
			"2호선", "bg-red-600", stationIds.get(3), stationIds.get(1), 11);
		ExtractableResponse<Response> lineResponse2 = RestUtil.post(lineRequest2);
		Long lineId2 = RestUtil.getIdFromLine(lineResponse2);
		RestUtil.post(lineId2, new SectionRequest(stationIds.get(1), stationIds.get(4), 10));
	}

	@DisplayName("경로를 조회한다.")
	@Test
	void findPath() {
		//given
		Long source = stationIds.get(0);
		Long target = stationIds.get(4);
		// when
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.when()
			.get("/paths?source=" + source + "&target=" + target + "&age=15")
			.then().log().all()
			.extract();

		// then
		PathResponse pathResponse = RestUtil.toResponseDto(response, PathResponse.class);
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(pathResponse.getStations())
				.map(StationResponse::getName)
				.containsExactly("강남역", "역삼역", "잠실역"),
			() -> assertThat(pathResponse.getDistance()).isEqualTo(21),
			() -> assertThat(pathResponse.getFare()).isEqualTo(1550)
		);
	}

	@DisplayName("구간에 없는 역으로 경로를 찾으면 400 에러가 발생한다.")
	@Test
	void pathBadRequest() {
		// given
		ExtractableResponse<Response> stationResponse = RestUtil.post(new StationRequest("잠실새내역"));

		// when
		Long stationId = RestUtil.getIdFromStation(stationResponse);
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			.when()
			.get("/paths?source=" + stationIds.get(0) + "&target=" + stationId + "&age=15")
			.then().log().all()
			.extract();

		// then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}
}
