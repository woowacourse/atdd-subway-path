package wooteco.subway.member.dao;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.exception.badrequest.NoRowHasBeenModifiedException;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;
import java.util.Optional;

@Repository
public class MemberDao {
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
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public Optional<Member> findById(long id) {
        try {
            String sql = "select * from MEMBER where id = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, id));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Member> findByEmail(String email) {
        try {
            String sql = "select * from MEMBER where email = ?";
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper, email));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Member member) {
        String sql = "update MEMBER set email = ?, password = ?, age = ? where id = ?";
        int updateRowNum = jdbcTemplate.update(sql,
                new Object[]{member.getEmail(), member.getPassword(), member.getAge(), member.getId()});
        if (updateRowNum <= 0) {
            throw new NoRowHasBeenModifiedException("수정된 행이 존재하지 않습니다.");
        }
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = ?";
        int deleteRowNum = jdbcTemplate.update(sql, id);
        if (deleteRowNum <= 0) {
            throw new NoRowHasBeenModifiedException("삭제된 행이 존재하지 않습니다.");
        }
    }
}
