package wooteco.subway.member.dao;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
class MemberDaoTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private final JdbcTemplate jdbcTemplate;
    private final MemberDao memberDao;

    @Autowired
    public MemberDaoTest(JdbcTemplate jdbcTemplate, @Qualifier("dataSource") DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        memberDao = new MemberDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("이메일과 비밀번호로 사용자를 찾는다.")
    void findByEmailAndPassword() {
        Member member = memberDao.findByEmailAndPassword(EMAIL, PASSWORD).get();

        assertThat(member.getEmail()).isEqualTo(EMAIL);
        assertThat(member.getPassword()).isEqualTo(PASSWORD);
    }

    @Test
    @DisplayName("잘못된 이메일 또는 비밀번호로 사용자를 찾는다.")
    void findByWrongEmailOrPassword() {
        Optional<Member> member1 = memberDao.findByEmailAndPassword("wrong@email.com", PASSWORD);
        assertThat(member1.isPresent()).isFalse();
        Optional<Member> member2 = memberDao.findByEmailAndPassword(EMAIL, "wrongpassword");
        assertThat(member2.isPresent()).isFalse();
    }
}
