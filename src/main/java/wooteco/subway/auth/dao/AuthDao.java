package wooteco.subway.auth.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import wooteco.subway.member.domain.Member;

import java.util.List;
import java.util.Optional;

@Repository
public class AuthDao {
    private final JdbcTemplate jdbcTemplate;

    public AuthDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Member> findByEmail(String email) {
        String sql = "SELECT id, email, password, age FROM member WHERE email = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Member member = new Member(
                    rs.getLong("id"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getInt("age")
            );
            return member;
        }, email).stream().findAny();
    }
}
