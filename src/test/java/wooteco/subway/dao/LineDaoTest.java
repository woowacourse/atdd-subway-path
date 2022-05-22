package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static wooteco.subway.Fixtures.GREEN;
import static wooteco.subway.Fixtures.LINE_2;
import static wooteco.subway.Fixtures.LINE_4;
import static wooteco.subway.Fixtures.SKY_BLUE;

import java.util.List;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.DuplicateKeyException;
import wooteco.subway.dao.entity.LineEntity;
import wooteco.subway.exception.notfound.NotFoundLineException;

@JdbcTest
public class LineDaoTest {

    @Autowired
    private DataSource dataSource;

    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(dataSource);
    }

    @Test
    @DisplayName("지하철 노선을 저장한다.")
    void save() {
        // when
        final Long id = lineDao.save(new LineEntity(LINE_2, SKY_BLUE, 900));

        // then
        final LineEntity savedLine = lineDao.findById(id);
        assertAll(
                () -> assertThat(savedLine.getId()).isNotNull(),
                () -> assertThat(savedLine.getName()).isEqualTo(LINE_2),
                () -> assertThat(savedLine.getColor()).isEqualTo(SKY_BLUE),
                () -> assertThat(savedLine.getExtraFare()).isEqualTo(900)
        );
    }

    @Test
    @DisplayName("같은 이름의 노선을 저장하는 경우, 예외가 발생한다.")
    void saveDuplicateName() {
        // given
        lineDao.save(new LineEntity(LINE_2, SKY_BLUE, 900));

        // when & then
        assertThatThrownBy(() -> lineDao.save(new LineEntity(LINE_2, GREEN, 900)))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("같은 색상의 노선을 저장하는 경우, 예외가 발생한다.")
    void saveDuplicateColor() {
        // given
        lineDao.save(new LineEntity(LINE_2, SKY_BLUE, 900));

        // when & then
        assertThatThrownBy(() -> lineDao.save(new LineEntity(LINE_4, SKY_BLUE, 900)))
                .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    @DisplayName("지하철 노선 목록을 조회한다.")
    void findAll() {
        // given
        lineDao.save(new LineEntity(LINE_2, SKY_BLUE, 900));
        lineDao.save(new LineEntity(LINE_4, GREEN, 900));

        // when
        final List<LineEntity> lines = lineDao.findAll();

        assertThat(lines).hasSize(2);
    }

    @Test
    @DisplayName("지하철 노선을 조회한다.")
    void find() {
        // given
        final Long id = lineDao.save(new LineEntity(LINE_2, SKY_BLUE, 900));

        // when
        final LineEntity foundLine = lineDao.findById(id);

        // then
        assertAll(
                () -> assertThat(foundLine.getName()).isEqualTo(LINE_2),
                () -> assertThat(foundLine.getColor()).isEqualTo(SKY_BLUE),
                () -> assertThat(foundLine.getExtraFare()).isEqualTo(900)
        );
    }

    @Test
    @DisplayName("존재하지 않는 Id 조회 시, 예외를 발생한다.")
    void findNotExistId() {
        // when & then
        assertThatThrownBy(() -> lineDao.findById(1L))
                .isInstanceOf(NotFoundLineException.class);
    }

    @Test
    @DisplayName("노선 정보를 업데이트 한다.")
    void update() {
        // given
        final Long id = lineDao.save(new LineEntity(LINE_2, SKY_BLUE, 900));

        // when
        lineDao.updateById(new LineEntity(id, LINE_4, GREEN, 800));

        // then
        final LineEntity updatedLine = lineDao.findById(id);
        assertAll(
                () -> assertThat(updatedLine.getName()).isEqualTo(LINE_4),
                () -> assertThat(updatedLine.getColor()).isEqualTo(GREEN),
                () -> assertThat(updatedLine.getExtraFare()).isEqualTo(800)
        );
    }

    @Test
    @DisplayName("노선을 삭제한다.")
    void delete() {
        // given
        final Long id = lineDao.save(new LineEntity(LINE_2, SKY_BLUE, 900));

        // when
        lineDao.deleteById(id);

        // then
        assertThat(lineDao.findAll()).hasSize(0);
    }

    @Test
    @DisplayName("ID를 이용해 지하철 노선이 있는지 확인한다.")
    void existsById() {
        // given
        final Long id = lineDao.save(new LineEntity(LINE_2, SKY_BLUE, 900));

        // when
        final boolean isExist = lineDao.existsById(id);

        // then
        assertThat(isExist).isTrue();
    }
}
