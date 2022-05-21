package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import wooteco.subway.dto.PathRequest;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.dto.StationResponse;

@SuppressWarnings("NonAsciiCharacters")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Sql({"classpath:schema-test.sql", "classpath:service-test-ddl.sql"})
class PathServiceTest {

    @Autowired
    private PathService pathService;

    @DisplayName("findShortestPath는 최단경로를 찾는다")
    @Nested
    class findShortestPathTest {
        @Test
        void 가장_짧은_경로를_찾는다() {
            PathRequest pathRequest = new PathRequest(1L, 3L, 10);
            PathResponse path = pathService.findShortestPath(pathRequest);
            List<StationResponse> stations = path.getStations();

            assertAll(() -> {
                assertThat(stations)
                        .extracting("id")
                        .containsExactly(1L, 2L, 3L);
                assertThat(path)
                        .extracting("distance", "fare")
                        .containsExactly(15, 1350);
            });
        }

        @Test
        void 존재하지_않는_역_id를_입력받은_경우_예외발생() {
            PathRequest pathRequest = new PathRequest(9999L, 3L, 10);
            assertThatThrownBy(() -> pathService.findShortestPath(pathRequest))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
