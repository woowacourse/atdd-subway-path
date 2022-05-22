package wooteco.subway.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import wooteco.subway.service.dto.response.StationResponse;

@DisplayName("지하철역 관련 기능")
public class StationAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("지하철 역 생성 API는")
    class Describe_Line_Create_API {

        @Nested
        @DisplayName("등록할 역 정보를 입력받는 경우")
        class Context_Input_Save_Station {

            @Test
            @DisplayName("지하철 역을 생성하고 생성된 역을 응답한다.")
            void it_returns_station() {
                ExtractableResponse<Response> response = 역_저장_응답("서초역");

                assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
                assertThat(response.body().jsonPath().getString("name")).isEqualTo("서초역");
                assertThat(response.header("Location")).isNotBlank();
            }
        }

        @Nested
        @DisplayName("등록된 역이 중복된 경우")
        class Context_Input_Duplicate {

            @Test
            @DisplayName("400 응답을 한다.")
            void it_returns_400() {
                역_저장_응답("강남역");

                ExtractableResponse<Response> response = 역_저장_응답("강남역");

                assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
            }
        }
    }

    @Nested
    @DisplayName("지하철 역 조회 API는")
    class Describe_Line_Get_API {

        @Nested
        @DisplayName("역 조회 요청을 한 경우")
        class Context_Input_Get_Stations {

            @BeforeEach
            void setUp() {
                역_저장_응답("강남역");
                역_저장_응답("역삼역");
            }

            @Test
            @DisplayName("저장된 지하철 역을 응답한다.")
            void it_returns_stations() {
                ExtractableResponse<Response> response = 역_전쳬_조회();

                assertThat(response.statusCode()).isEqualTo(200);

                List<StationResponse> responses = response
                        .body()
                        .jsonPath()
                        .getList(".", StationResponse.class);
                assertThat(responses)
                        .extracting("name")
                        .isEqualTo(List.of("강남역", "역삼역"));
            }
        }
    }

    @Nested
    @DisplayName("지하철 역 삭제 API는")
    class Describe_Line_Delete_API {

        @Nested
        @DisplayName("저장된 역 id로 삭제 요청을 한 경우")
        class Context_Input_Delete_Station {

            private long 강남;

            @BeforeEach
            void setUp() {
                강남 = 역_저장("강남역");
            }

            @Test
            @DisplayName("저장된 지하철 역이 삭제된다.")
            void it_returns_stations() {
                ExtractableResponse<Response> response = 역_삭제(강남);
                assertThat(response.statusCode()).isEqualTo(204);

                ExtractableResponse<Response> 역_목록 = 역_전쳬_조회();
                List<StationResponse> responses = 역_목록
                        .body()
                        .jsonPath()
                        .getList(".", StationResponse.class);
                assertThat(responses).extracting("name").doesNotContain("강남역");
            }
        }

        @Nested
        @DisplayName("존재하지 않는 역 id로 삭제 요청을 한 경우")
        class Context_Input_Delete_Not_Exist_Station {

            @Test
            @DisplayName("역이 삭제되지 않고 204 응답을 한다.")
            void it_returns_stations() {
                ExtractableResponse<Response> response = 역_삭제(1L);

                assertThat(response.statusCode()).isEqualTo(204);
            }
        }
    }

    private ExtractableResponse<Response> 역_삭제(long id) {
        return RestAssured.given().log().all()
                .when()
                .delete("/stations/" + id)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 역_저장_응답(String name) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);

        return RestAssured.given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/stations")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 역_전쳬_조회() {
        return RestAssured.given().log().all()
                .when()
                .get("/stations")
                .then().log().all()
                .extract();
    }
}
