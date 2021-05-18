package wooteco.subway.path;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wooteco.subway.AcceptanceTest;
import wooteco.subway.dao.SectionDao;

public class PathServiceTest extends AcceptanceTest {
    @Autowired
    SectionDao sectionDao;

    @Test
    void findAll() {
        sectionDao.findAll();
    }
}
