package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MemberJpaRepository {

    private final EntityManager em;

    public Member save(Member member) {
        em.persist(member);
        return member;
    }
    
    public void delete(Member member) {
        em.remove(member);
    }
    
    public List<Member> findAll() {
        return em.createQuery("select m from Member m",Member.class)
                .getResultList();
    }
    
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class,id);
        return Optional.ofNullable(member);
    }
    
    public long count() {
        return em.createQuery("select count(m) from Member m",Long.class) // Long 타입으로 반환하기
                .getSingleResult(); // 싱글리졀트
    }
    

    public Member find(Long id) {
        return em.find(Member.class,id);
    }
    
    /**
     * 생성자 규칙이있음
     * */

    public List<Member> findByUsernameAndAgeGreaterThan(String username, int age) {
        return em.createQuery("select m from Member m where m.username = :username and m.age > :age")
                .setParameter("username", username)
                .setParameter("age", age)
                .getResultList();
    }

    /**
     *
     * 순수 페이징 정렬
     * */

    public List<Member> findByPage(int age,int offset, int limit) {
       return em.createQuery("select m from Member m where m.age = :age order by m.username desc")
                .setParameter("age",age)
                .setFirstResult(offset) // 어디서부트 가져올꺼야
                .setMaxResults(limit)  //몇개 가져올꺼야
                .getResultList();
    }


    public long totalCount(int age) {
        return em.createQuery("select count(m) from Member m where m.age = :age", Long.class)
                .setParameter("age",age)
                .getSingleResult(); // 하나밖에 없을때
    }

    /**
     *
     * update
     * */

    public int bulkAgePlus(int age) {
       int result =  em.createQuery("update Member m set m.age = m.age +1 where m.age >= :age ")
                .setParameter("age",age)
                .executeUpdate();
       return result;

    }
}
