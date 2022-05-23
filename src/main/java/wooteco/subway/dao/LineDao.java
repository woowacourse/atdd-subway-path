package wooteco.subway.dao;

import java.util.List;

import wooteco.subway.dao.entity.LineEntity;

public interface LineDao {

    LineEntity save(LineEntity lineEntity);

    List<LineEntity> findAll();

    LineEntity findById(Long id);

    LineEntity update(LineEntity lineEntity);

    void deleteById(Long id);
}
