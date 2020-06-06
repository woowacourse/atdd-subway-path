package wooteco.subway.admin.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.Edge;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJdbcTest
public class LineRepositoryTest {
    @Autowired
    private LineRepository lineRepository;

    @Test
    void addEdge() {
        // given
        Line line = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line persistLine = lineRepository.save(line);
        persistLine.addEdge(new Edge(null, 1L, 10, 10));
        persistLine.addEdge(new Edge(1L, 2L, 10, 10));

        // when
        Line resultLine = lineRepository.save(persistLine);

        // then
        assertThat(resultLine.getEdgesSize()).isEqualTo(2);
    }
}
