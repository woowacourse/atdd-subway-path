package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.SectionJdbcDao;
import wooteco.subway.domain.Section;
import wooteco.subway.dto.SectionRequest;

@SpringBootTest
@Transactional
@Sql("classpath:setupSection.sql")
public class SectionServiceTest {

    @Autowired
    private SectionService sectionService;

    @Autowired
    private SectionJdbcDao sectionJdbcDao;

    @DisplayName("Section 정보를 저장한다.")
    @Test()
    void save() {
        Long lineId = 1L;
        SectionRequest sectionRequest = new SectionRequest(2L, 3L, 3);
        sectionService.save(lineId, sectionRequest);

        List<Section> sectionsByLineId = sectionJdbcDao.findByLineId(lineId);
        assertThat(sectionsByLineId.size()).isEqualTo(2);
    }

    @DisplayName("Section 정보를 삭제한다.")
    @Test()
    void delete() {
        Long lineId = 1L;
        Long stationId = 1L;
        SectionRequest sectionRequest = new SectionRequest(stationId, 3L, 3);
        sectionService.save(lineId, sectionRequest);

        sectionService.delete(lineId, stationId);

        List<Section> sectionsByLineId = sectionJdbcDao.findByLineId(lineId);
        assertThat(sectionsByLineId.size()).isEqualTo(1);
    }
}
