package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,Long> { //클래스 , pk 타입

    /**
     * username = ? and agge > GreaterThan 임
     * 문법이 틀리면 안됨. 정해진 규칙이 있음
     * */
    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findTop3HelloBy();
    
    /**
     * Query 메소드 ** 실무에서 많이사용 *** 복잡한 jql
     * */

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username,@Param("age") int age);

    /**
     *
     * 단순히 값 하나를 조회
     * JPA (@Embedded) 도 이방식으로 조회가능
     * */

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    /**
     * Dto 로 직접 조회
     * */
    @Query("select new com.example.springdatajpa.repository.MemberDto(m.id,m.username,t.name)" + "from Member m join m.team t")
    List<MemberDto> findMemberDto();
    
    /**
     * 컬렉션을 조회 하고싶을때
     * */

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);
}
