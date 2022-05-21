package wooteco.subway.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import wooteco.subway.dao.LineDao;
import wooteco.subway.dao.SectionDao;
import wooteco.subway.dao.StationDao;

@SpringBootTest
@Transactional
abstract class ControllerTest {

    @Autowired
    protected StationDao stationDao;

    @Autowired
    protected SectionDao sectionDao;

    @Autowired
    protected LineDao lineDao;
}
