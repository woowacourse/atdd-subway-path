package wooteco.subway.path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wooteco.subway.service.PathService;
import wooteco.subway.web.dto.PathResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class PathTest {
    @Autowired
    private PathService pathService;

    @Test
    public void test() {
        PathResponse shortestPath = pathService.getShortestPath(1L, 2L);
    }
}
