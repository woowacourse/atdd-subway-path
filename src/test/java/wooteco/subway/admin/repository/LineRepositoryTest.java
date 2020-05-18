package wooteco.subway.admin.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.relational.core.conversion.DbActionExecutionException;
import wooteco.subway.admin.domain.Line;
import wooteco.subway.admin.domain.LineStation;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJdbcTest
public class LineRepositoryTest {
    @Autowired
    private LineRepository lineRepository;

    @Test
    void addLineStation() {
        // given
        Line line = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line persistLine = lineRepository.save(line);
        persistLine.addLineStation(new LineStation(null, 1L, 10, 10));
        persistLine.addLineStation(new LineStation(1L, 2L, 10, 10));

        // when
        Line resultLine = lineRepository.save(persistLine);

        // then
        assertThat(resultLine.getStations()).hasSize(2);
    }

    @Test
    void saveDuplicatedLine() {
        Line line1 = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        Line line2 = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);

        lineRepository.save(line1);
        assertThatThrownBy(() -> lineRepository.save(line2))
                .isInstanceOf(DbActionExecutionException.class)
                .hasCauseInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void saveNullLine() {
        Line line = new Line("2호선", null, null, 5);
        assertThatThrownBy(() -> lineRepository.save(line))
                .isInstanceOf(DbActionExecutionException.class)
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void nullLineStation() {
        Line line = new Line("2호선", LocalTime.of(05, 30), LocalTime.of(22, 30), 5);
        line.addLineStation(new LineStation(null, null, 10, 10));
        assertThatThrownBy(() -> lineRepository.save(line))
                .isInstanceOf(DbActionExecutionException.class)
                .hasCauseInstanceOf(DataIntegrityViolationException.class);
    }
}
