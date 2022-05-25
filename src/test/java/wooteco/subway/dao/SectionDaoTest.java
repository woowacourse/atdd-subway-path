package wooteco.subway.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static wooteco.subway.domain.fixtures.TestFixtures.강남;
import static wooteco.subway.domain.fixtures.TestFixtures.성수;
import static wooteco.subway.domain.fixtures.TestFixtures.왕십리;
import static wooteco.subway.domain.fixtures.TestFixtures.합정;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import wooteco.subway.dao.entity.SectionEntity;
import wooteco.subway.domain.line.Line;

@JdbcTest
@Import(SectionDao.class)
public class SectionDaoTest {

    @Autowired
    private SectionDao sectionDao;

    @Test
    @DisplayName("구간 저장")
    void save() {
        Line line = new Line(1L, "2호선", "green");
        SectionEntity entity = sectionDao
                .save(new SectionEntity(line.getId(), 성수.getId(), 합정.getId(), 10));

        assertThat(entity.getUpStationId()).isEqualTo(성수.getId());
        assertThat(entity.getDownStationId()).isEqualTo(합정.getId());
        assertThat(entity.getDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("구간 전체 조회")
    void findAll() {
        Line line = new Line(1L, "2호선", "green");
        SectionEntity 성수_합정 = sectionDao
                .save(new SectionEntity(line.getId(), 성수.getId(), 합정.getId(), 10));
        SectionEntity 합정_강남 = sectionDao
                .save(new SectionEntity(line.getId(), 합정.getId(), 강남.getId(), 10));

        List<SectionEntity> entities = sectionDao.findAll();

        assertThat(entities)
                .usingRecursiveComparison()
                .ignoringFields("id")
                .isEqualTo(List.of(성수_합정, 합정_강남));
    }

    @Test
    @DisplayName("구간 변경")
    void update() {
        Line line = new Line(1L, "2호선", "green");
        SectionEntity save = sectionDao
                .save(new SectionEntity(line.getId(), 성수.getId(), 합정.getId(), 10));

        sectionDao
                .update(new SectionEntity(save.getId(), line.getId(), 성수.getId(), 왕십리.getId(), 10));

        List<SectionEntity> sections = sectionDao.findByLineId(line.getId());

        assertThat(sections.get(0).getUpStationId()).isEqualTo(성수.getId());
        assertThat(sections.get(0).getDownStationId()).isEqualTo(왕십리.getId());
        assertThat(sections.get(0).getDistance()).isEqualTo(10);
    }

    @Test
    @DisplayName("구간 삭제")
    void delete() {
        Line line = new Line(1L, "2호선", "green");
        SectionEntity save = sectionDao
                .save(new SectionEntity(line.getId(), 성수.getId(), 합정.getId(), 10));

        sectionDao.deleteById(save.getId());

        List<SectionEntity> sections = sectionDao.findByLineId(line.getId());

        assertThat(sections).hasSize(0);
    }

    @Test
    @DisplayName("구간에 역이 존재하는 지 확인")
    void existedStation() {
        Line line = new Line(1L, "2호선", "green");
        sectionDao.save(new SectionEntity(line.getId(), 성수.getId(), 합정.getId(), 10));

        assertThat(sectionDao.existedByStation(성수.getId())).isTrue();
        assertThat(sectionDao.existedByStation(합정.getId())).isTrue();
    }
}
