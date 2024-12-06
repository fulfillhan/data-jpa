package study.datajpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test
    void testEntity(){
        Team team1 = new Team("team1");
        Team team2 = new Team("team2");
        em.persist(team1);
        em.persist(team2);

        Member member1 = new Member("member1", 20, team1);
        Member member2 = new Member("member2", 19, team1);
        Member member3 = new Member("member3", 19, team2);
        Member member4 = new Member("member4", 20, team2);

        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        //초기화
        em.flush();// sql쿼리를 db로 전송한다
        em.clear();// 영속성 컨텍스트에 데이터를 정리한다. 캐시 삭제

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : members) {
            System.out.println("member = " + member);
            System.out.println("member.Team() = " + member.getTeam());
        }


    }


}