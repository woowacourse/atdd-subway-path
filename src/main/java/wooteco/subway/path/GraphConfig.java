package wooteco.subway.path;

import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.line.dao.SectionDao;
import wooteco.subway.line.domain.Section;
import wooteco.subway.path.domain.Graph;

@Configuration
public class GraphConfig {

    private final SectionDao sectionDao;

    public GraphConfig(SectionDao sectionDao) {
        this.sectionDao = sectionDao;
    }

    @Bean
    public Graph graph() {
        List<Section> sections = sectionDao.findAll();
        return new Graph(sections);
    }
}
