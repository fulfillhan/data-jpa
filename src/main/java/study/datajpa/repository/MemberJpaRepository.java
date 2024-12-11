package study.datajpa.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.datajpa.entity.Member;

import java.util.List;
import java.util.Optional;

//순수 jpa 리포지토리
@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    @PersistenceContext
    private final EntityManager em;

    public Member save(Member member){
        em.persist(member);
        return member;
    }
    
    public void delete(Member member){
        em.remove(member);
    }
    
    public List<Member> findAll(){
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }
    
    //Optional 봔환하기
    public Optional<Member> findById(Long id){
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    //단건 조회
    public Long count(){
        return em.createQuery("select count(m) from Member m", Long.class)
                .getSingleResult();
    }

    public Member find(Long id){
        return em.find(Member.class, id);
    }

    public List<Member> findByUsernameAndGraterThan(String username,int age){
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }


    //페이징 하기
  /*  public List<Member> findByPaging(int age, int offset, int limit){
       return em.createQuery("select m from Member m where m.age =:age order by m.username desc")
                .setParameter("age", age)
                .setParameter(offset)
                .setParameter(limit)
                .getResultList();
    }*/

    public long totalCount(int age){
        return em.createQuery("select count (m) from Member m where age = :age",Long.class)
                .setParameter("age",age)
                .getSingleResult();
    }

    //벌크성 쿼리
    public int bulkAgePlus(int age){
        return em.createQuery("update Member m set m.age = m.age+1 where m.age >= :age")
                .setParameter("age",age)
                .executeUpdate();
    }



}
