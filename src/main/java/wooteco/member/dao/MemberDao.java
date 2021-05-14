package wooteco.member.dao;

import java.util.Collections;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import wooteco.member.domain.Member;

@Repository
public class MemberDao {
    private final SimpleJdbcInsert simpleJdbcInsert;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final RowMapper<Member> rowMapper = (rs, rowNum) ->
        new Member(
            rs.getLong("id"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getInt("age")
        );

    public MemberDao(DataSource dataSource, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.simpleJdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName("member")
            .usingGeneratedKeyColumns("id");
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public Member insert(Member member) {
        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return new Member(id, member.getEmail(), member.getPassword(), member.getAge());
    }

    public Optional<Member> findById(Long id) {
        String sql = "select * from MEMBER where id = :id";
        MapSqlParameterSource param = new MapSqlParameterSource("id", id);
        Member member = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(sql, param, rowMapper));
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "select * from MEMBER where email = :email";
        MapSqlParameterSource param = new MapSqlParameterSource("email", email);
        Member member = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(sql, param, rowMapper));
        return Optional.ofNullable(member);
    }

    public Optional<Member> findByEmailAndPassword(String email, String password) {
        String sql = "select * from MEMBER where email = :email and password = :password";
        MapSqlParameterSource params = new MapSqlParameterSource("email", email)
            .addValue("password", password);
        Member member = DataAccessUtils.singleResult(namedParameterJdbcTemplate.query(sql, params, rowMapper));
        return Optional.ofNullable(member);
    }

    public void update(Member member) {
        String sql = "update MEMBER set email = :email, password = :password, age = :age where id = :id";
        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(member);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public void deleteById(Long id) {
        String sql = "delete from MEMBER where id = :id";
        namedParameterJdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }
}
