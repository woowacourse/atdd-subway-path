package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import wooteco.subway.dao.entity.LineEntity;

@JdbcTest
@Import(LineDao.class)
public class LineDaoTest {

    @Autowired
    private LineDao lineDao;

    @Test
    @DisplayName("노선 저장")
    void save() {
        LineEntity line = new LineEntity("1호선", "blue", 0);
        LineEntity savedLine = lineDao.save(line);
        assertThat(savedLine.getId()).isNotNull();
        assertThat(savedLine.getName()).isEqualTo("1호선");
        assertThat(savedLine.getExtraFare()).isEqualTo(0);
    }

    @ParameterizedTest
    @CsvSource({"1호선, blue", "1호선, green ", "2호선, blue"})
    @DisplayName("노선 이름, 색깔 중복 여부 조회")
    void duplicateNameOrColor(String name, String color) {
        LineEntity line = new LineEntity("1호선", "blue", 0);
        lineDao.save(line);
        assertThat(lineDao.existByNameOrColor(name, color)).isTrue();
    }

    @Test
    @DisplayName("id로 노선 조회")
    void findById() {
        LineEntity line = lineDao.save(new LineEntity("1호선", "blue", 0));
        LineEntity findLine = lineDao.findById(line.getId()).get();
        assertThat(findLine.getId()).isNotNull();
        assertThat(findLine.getName()).isEqualTo("1호선");
        assertThat(findLine.getExtraFare()).isEqualTo(0);
    }

    @Test
    @DisplayName("노선 전체 조회")
    void findAll() {
        LineEntity line1 = new LineEntity("1호선", "blue", 0);
        LineEntity line2 = new LineEntity("2호선", "red", 0);
        lineDao.save(line1);
        lineDao.save(line2);
        List<LineEntity> liens = lineDao.findAll();
        assertThat(liens).hasSize(2);
    }

    @Test
    @DisplayName("노선 수정")
    void modifyById() {
        LineEntity savedLine = lineDao.save(new LineEntity("1호선", "blue", 0));
        lineDao.modifyById(new LineEntity(savedLine.getId(), "2호선", "red", 0));
        LineEntity updateLine = lineDao.findById(savedLine.getId()).get();
        assertThat(updateLine.getName()).isEqualTo("2호선");
        assertThat(updateLine.getColor()).isEqualTo("red");
        assertThat(updateLine.getExtraFare()).isEqualTo(0);
    }

    @Test
    @DisplayName("id로 노선 삭제")
    void deleteById() {
        LineEntity savedLine = lineDao.save(new LineEntity("1호선", "blue", 0));
        lineDao.deleteById(savedLine.getId());
        assertThat(lineDao.findAll()).hasSize(0);
    }

}
