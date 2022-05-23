package wooteco.subway.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dto.PathResponse;
import wooteco.subway.repository.dao.LineDao;
import wooteco.subway.repository.dao.SectionDao;
import wooteco.subway.repository.dao.StationDao;
import wooteco.subway.repository.entity.LineEntity;
import wooteco.subway.repository.entity.SectionEntity;
import wooteco.subway.repository.entity.StationEntity;

@SpringBootTest
@Transactional
@Sql("classpath:testInit.sql")
public class PathServiceTest {

    @Autowired
    private PathService pathService;


    @Test
    @DisplayName("경로 조회한다")
    void getPath() {
        // given
        PathResponse pathResponse = pathService.getPath(3L, 1L, 20);

        // then
        assertThat(pathResponse.getStations()).hasSize(3);
        assertThat(pathResponse.getDistance()).isEqualTo(10);
        assertThat(pathResponse.getFare()).isEqualTo(1450L);
    }
}
