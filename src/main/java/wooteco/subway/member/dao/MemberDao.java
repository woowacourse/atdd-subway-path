package wooteco.subway.member.dao;

import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.subway.member.domain.Member;

@Repository
public class MemberDao {

    private static final String MEMBER_TABLE = "MEMBER";
    private static final String ID = "id";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";
    private static final String AGE = "age";

    private static final RowMapper<Member> MEMBER_ROW_MAPPER = (rs, rowNum) ->
            new Member(
                    rs.getLong(ID),
                    rs.getString(EMAIL),
                    rs.getString(PASSWORD),
                    rs.getInt(AGE)
            );

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    public MemberDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

        DataSource dataSource = Objects.requireNonNull(jdbcTemplate.getDataSource());
        this.insertAction = new SimpleJdbcInsert(dataSource)
                .withTableName(MEMBER_TABLE)
                .usingGeneratedKeyColumns(ID);
    }

    public Member insert(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = insertAction.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public void update(Member member) {
        String sql = "update MEMBER set email = ?, age = ? where id = ?";
        Object[] params = {member.getEmail(), member.getAge(), member.getId()};
        jdbcTemplate.update(sql, params);
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = ?";
        jdbcTemplate.update(sql, id);
    }

    public Member findById(Long id) {
        String sql = "select * from MEMBER where id = ?";
        return jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, id);
    }

    public Member findByEmail(String email) {
        String sql = "select * from MEMBER where email = ?";
        return jdbcTemplate.queryForObject(sql, MEMBER_ROW_MAPPER, email);
    }
}
