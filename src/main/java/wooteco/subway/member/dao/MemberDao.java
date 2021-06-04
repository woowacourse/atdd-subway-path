package wooteco.subway.member.dao;

import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.application.DuplicatedFieldException;
import wooteco.subway.exception.application.NonexistentTargetException;
import wooteco.subway.member.domain.Member;

@Repository
public class MemberDao {

    private static final int SUCCESSFUL_AFFECTED_COUNT = 1;

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    private final RowMapper<Member> rowMapper = (rs, rowNum) ->
        new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("age")
        );


    public MemberDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
    }

    public Member insert(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        try {
            Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
            return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
        } catch (DuplicateKeyException e) {
            throw new DuplicatedFieldException("멤버 이메일: " + member.getEmail());
        }
    }

    public void update(Member member) {
        String sql = "update MEMBER set email = ?, password = ?, age = ? where id = ?";

        try {
            int updatedCount = jdbcTemplate.update(
                sql, member.getEmail(), member.getPassword(), member.getAge(), member.getId()
            );
            if (updatedCount < SUCCESSFUL_AFFECTED_COUNT) {
                throw new NonexistentTargetException(("멤버ID: " + member.getId()));
            }
        } catch (DuplicateKeyException e) {
            throw new DuplicatedFieldException(String.format("멤버 이메일: ", member.getEmail()));
        }
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = ?";
        int deletedCount = jdbcTemplate.update(sql, id);
        if (deletedCount < SUCCESSFUL_AFFECTED_COUNT) {
            throw new NonexistentTargetException("멤버ID: " + id);
        }
    }

    public Optional<Member> findById(Long id) {
        String sql = "select * from MEMBER where id = ?";
        return Optional.ofNullable(
            DataAccessUtils.singleResult(jdbcTemplate.query(sql, rowMapper, id))
        );
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "select * from MEMBER where email = ?";
        return Optional.ofNullable(
            DataAccessUtils.singleResult(jdbcTemplate.query(sql, rowMapper, email))
        );
    }
}
