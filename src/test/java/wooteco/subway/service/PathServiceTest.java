package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.service.dto.PathResponse;
import wooteco.subway.service.dto.StationResponse;
import wooteco.subway.ui.dto.PathRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class PathServiceTest {

    @Autowired
    PathService pathService;

    private static StationResponse station1 = new StationResponse(1L, "1");
    private static StationResponse station2 = new StationResponse(2L, "2");
    private static StationResponse station3 = new StationResponse(3L, "3");
    private static StationResponse station4 = new StationResponse(4L, "4");
    private static StationResponse station5 = new StationResponse(5L, "5");
    private static StationResponse station6 = new StationResponse(6L, "6");
    private static StationResponse station7 = new StationResponse(7L, "7");
    private static StationResponse station8 = new StationResponse(8L, "8");

    @DisplayName("최단경로를 찾고 요금을 반환한다.")
    @ParameterizedTest
    @MethodSource("expectedPathResponse")
    void searchPathsTest(Long source, Long target, int age, PathResponse expectedResult) {
        PathResponse result = pathService.searchPaths(new PathRequest(source, target, age));

        assertThat(result).isEqualTo(expectedResult);
    }

    private static Stream<Arguments> expectedPathResponse() {
        return Stream.of(
                Arguments.of(1L, 5L, 6,
                        new PathResponse(5, (int)(0.5*((200 + 1250)-350)), List.of(station1, station2, station4, station5))),
                Arguments.of(1L, 5L, 13,
                        new PathResponse(5, (int)(0.8*((200 + 1250)-350)), List.of(station1, station2, station4, station5))),
                Arguments.of(1L, 5L, 19,
                        new PathResponse(5, 200 + 1250, List.of(station1, station2, station4, station5))),
                Arguments.of(6L, 5L, 6,
                        new PathResponse(6, (int)(0.5*((200 + 1250)-350)), List.of(station6, station7, station3, station4, station5))),
                Arguments.of(6L, 5L, 13,
                        new PathResponse(6, (int)(0.8*((200 + 1250)-350)), List.of(station6, station7, station3, station4, station5))),
                Arguments.of(6L, 5L, 19,
                        new PathResponse(6, 200 + 1250, List.of(station6, station7, station3, station4, station5)))
        );
    }
}
