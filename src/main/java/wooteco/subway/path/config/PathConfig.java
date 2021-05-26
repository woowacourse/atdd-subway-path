package wooteco.subway.path.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import wooteco.subway.line.dao.LineDao;
import wooteco.subway.path.domain.PathRepository;
import wooteco.subway.path.repository.PathRepositoryImpl;

@Configuration
public class PathConfig {

    @Bean
    public PathRepository pathRepository(LineDao lineDao) {
        PathRepository pathRepository = new PathRepositoryImpl();
        pathRepository.generateAllPath(lineDao.findAll());
        return pathRepository;
    }
}
