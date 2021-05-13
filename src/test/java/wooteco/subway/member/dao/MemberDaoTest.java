package wooteco.subway.member.dao;

import static org.assertj.core.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

import wooteco.subway.member.domain.Member;

@DataJdbcTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class MemberDaoTest {
    private static final String EMAIL = "test@test.com";
    private static final String PASSWORD = "test";
    private static final int AGE = 12;

    private final MemberDao memberDao;

    public MemberDaoTest(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.memberDao = new MemberDao(jdbcTemplate, dataSource);
    }

    @DisplayName("회원을 등록한다.")
    @Test
    void insert() {
        //given
        Member member = new Member(EMAIL, PASSWORD, AGE);

        //when
        Member savedMember = memberDao.insert(member);

        //then
        assertThat(savedMember.getEmail()).isEqualTo(member.getEmail());
    }

    @DisplayName("이미 존재하는 회원을 등록한다.")
    @Test
    void duplicateInsert() {
        //given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        memberDao.insert(member);

        //then
        assertThatThrownBy(() -> memberDao.insert(member))
            .isInstanceOf(DuplicateKeyException.class);
    }

    @Test
    void update() {
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