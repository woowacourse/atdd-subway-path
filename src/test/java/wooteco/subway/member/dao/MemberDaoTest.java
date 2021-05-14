package wooteco.subway.member.dao;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;
import wooteco.subway.member.domain.Member;

import javax.sql.DataSource;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@JdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberDaoTest {

    private final JdbcTemplate jdbcTemplate;
    private final DataSource dataSource;
    private final MemberDao memberDao;
    private Member originMember;

    MemberDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.dataSource = dataSource;
        this.memberDao = new MemberDao(jdbcTemplate, dataSource);
    }

    @BeforeEach
    void init() {
        Member member = new Member("test@test.com", "pass", 23);
        originMember = memberDao.insert(member);
    }

    @Test
    void insert() {
    }

    @Test
    void update() {
        //given
        String newEmail = "newTest@test.com";
        String newPassWord = "new";
        int newAge = 20;

        //when
        memberDao.update(new Member(originMember.getId(), newEmail, newPassWord, newAge));
        Member findMember = memberDao.findByEmail(newEmail).get();

        //then
        assertThat(findMember.getEmail()).isEqualTo(newEmail);
        assertThat(findMember.getPassword()).isEqualTo(newPassWord);
        assertThat(findMember.getAge()).isEqualTo(newAge);
    }

    @Test
    void deleteById() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByEmail() {
    }
}