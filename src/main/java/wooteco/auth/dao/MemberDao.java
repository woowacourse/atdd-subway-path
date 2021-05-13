package wooteco.auth.dao;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.auth.domain.Member;

@Repository
public class MemberDao {

    private SimpleJdbcInsert simpleJdbcInsert;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private RowMapper<Member> rowMapper = (rs, rowNum) ->
        new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("age")
        );


    public MemberDao(DataSource dataSource) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public Member insert(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public void update(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        String sql = "update MEMBER set email = :email, password = :password, age = :age where id = :id";
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void deleteById(Long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        String sql = "delete from MEMBER where id = :id";
        namedParameterJdbcTemplate.update(sql, params);
    }

    public Optional<Member> findById(Long id) {
        Map<String, Long> params = Collections.singletonMap("id", id);
        String sql = "select * from MEMBER where id = :id";
        return namedParameterJdbcTemplate.query(sql, params, rowMapper).stream().findAny();
    }

    public Optional<Member> findByEmail(String email) {
        Map<String, String> params = Collections.singletonMap("email", email);
        String sql = "select * from MEMBER where email = :email";
        return namedParameterJdbcTemplate.query(sql, params, rowMapper).stream().findAny();
    }
}
