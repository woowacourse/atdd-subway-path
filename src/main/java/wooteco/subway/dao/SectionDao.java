package wooteco.subway.dao;

import java.util.List;
import wooteco.subway.dao.entity.SectionEntity;

public interface SectionDao {

    SectionEntity save(SectionEntity sectionEntity);

    int saveAll(List<SectionEntity> sectionEntities);

    List<SectionEntity> findAll();

    SectionEntity findById(Long id);

    List<SectionEntity> findByLineId(Long id);

    void update(SectionEntity sectionEntity);

    void delete(Long id);
}
