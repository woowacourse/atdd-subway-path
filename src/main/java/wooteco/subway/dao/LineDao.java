package wooteco.subway.dao;

import wooteco.subway.dao.entity.LineEntity;

import java.util.List;

public interface LineDao {

    LineEntity save(LineEntity lineEntity);

    List<LineEntity> findAll();

    LineEntity findById(Long id);

    LineEntity update(LineEntity lineEntity);

    void deleteById(Long id);
}
