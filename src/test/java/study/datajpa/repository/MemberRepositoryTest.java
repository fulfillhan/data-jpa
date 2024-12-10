package study.datajpa.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test
    void testMember(){

        System.out.println("memberRepository.getClass() = " + memberRepository.getClass());
        //memberRepository.getClass() = class jdk.proxy3.$Proxy127

        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);

        Member findMember = memberRepository.findById(member.getId()).get();

        //검증
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member);

    }

    @Test
    public void basicCrud(){
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // 단건 조회 검증
        Member findMember1 = memberRepository.findById(member1.getId()).get();
        Member findMember2 = memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);

        //갯수 검증
        Long count = memberRepository.count();
        assertThat(count).isEqualTo(2);

        //삭제하기
        memberRepository.delete(member1);
        memberRepository.delete(member2);

        //삭제후 갯수 검증
        Long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);

    }

    @Test
    public void findByUsernameAndGraterThan(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("AAA");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void testQuery(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<Member> findUsers = memberRepository.findUser("AAA", 10);

        assertThat(findUsers.get(0)).isEqualTo(member1);

    }

    @Test
    public void findUsernameTest(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        List<String> userList = memberRepository.findUserList();
        for (String result : userList) {
            System.out.println("result = " + result);
        }

    }

    //dto로 조회하기
    @Test
    public void findUserDtoTest(){
        Team team = new Team("teamA");
        teamRepository.save(team);

        Member member1 = new Member("AAA",10);
        memberRepository.save(member1);


        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }

    }

    @Test
    public void returnType(){
        Member member1 = new Member("AAA",10);
        Member member2 = new Member("AAA",20);

        memberRepository.save(member1);
        memberRepository.save(member2);

        //컬렉션 조회 결과가 없으면 빈 컬렉션 반환
        List<Member> result = memberRepository.findListByUsername("AA");
        System.out.println("result = " + result);
        // 실행결과 0
    }





}