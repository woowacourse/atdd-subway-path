package wooteco.subway.member.dao;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestConstructor;

import wooteco.subway.exception.MemberNotFoundException;
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
        //given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Member createdMember = memberDao.insert(member);

        //when
        String newEmail = "test2@test.com";
        String newPassword = "newPassword";
        int newAge = 123;
        Member member1 = new Member(createdMember.getId(), newEmail, newPassword, newAge);
        memberDao.update(member1);

        //then
        assertEquals(newEmail, memberDao.findById(member1.getId()).get().getEmail());
        assertEquals(newPassword, memberDao.findById(member1.getId()).get().getPassword());
        assertEquals(newAge, memberDao.findById(member1.getId()).get().getAge());
    }

    @Test
    void deleteById() {
        //given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Member createdMember = memberDao.insert(member);

        //then
        assertDoesNotThrow(()-> memberDao.deleteById(createdMember.getId()));
    }

    @Test
    void findById() {
        //given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Member createdMember = memberDao.insert(member);

        //when
        Member foundMember = memberDao.findById(createdMember.getId()).get();

        //then
        assertEquals(foundMember.getEmail(), createdMember.getEmail());
        assertEquals(foundMember.getPassword(), createdMember.getPassword());
        assertEquals(foundMember.getAge(), createdMember.getAge());
    }

    @Test
    void findByEmail() {
        //given
        Member member = new Member(EMAIL, PASSWORD, AGE);
        Member createdMember = memberDao.insert(member);

        //when
        Member foundMember = memberDao.findByEmail(createdMember.getEmail()).get();

        //then
        assertEquals(foundMember.getEmail(), createdMember.getEmail());
        assertEquals(foundMember.getPassword(), createdMember.getPassword());
        assertEquals(foundMember.getAge(), createdMember.getAge());
    }
}