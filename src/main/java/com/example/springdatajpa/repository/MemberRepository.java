package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member,Long>,MemberRepositoryCustom { //클래스 , pk 타입

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


    List<Member> findListByUsername(String username); //컬렉션
    Member findMemberByUsername(String username); // 단건
    Optional<Member> findOptionalByUsername(String username); // 단건 Optional

    /**
     * Pageable 페이지
     *
     *  *****************************실무에서는  카운터쿼리를 분리해줘야함 ********************
     * */

    @Query(value = "select m from Member m left join m.team t", countQuery = "select count(m.username) from Member m") //********실무에서는  카운터쿼리를 분리해줘야함 ********
    Page<Member> findByAge(int age, Pageable pageable);



    /**
     *
     * */

    @Modifying(clearAutomatically = true) // 이거 안느면 실행안됨 벌크성 업데ㅣ트 실행이되면 clearAuto 매틱이 자동으로 지워줌 ㅎ
    @Query("update Member m set m.age = m.age+1 where m.age >= :age")
    int bulkAgePlus(@Param("age") int age);

    /**
     * 패치조인
     * */

    @Query("select m from Member m left join fetch m.team")
    List<Member> findMemberFetchJoin();


    @Override
    @EntityGraph(attributePaths = {"team"}) // 패치조인이라고 보면된다.
    List<Member> findAll();

    @EntityGraph(attributePaths = {"team"}) // 패치조인이라고 보면된다.
    @Query("select m from Member m")
    List<Member> findMemberEntityGraph();

  //  @EntityGraph(attributePaths = {"team"})
    @EntityGraph("Member.all")
    List<Member> findByUsername(@Param("username") String username);

    /**
     * JPA  HINT
     * // 성능최적화를 위해 변경감지 다 무시함
     * */

    @QueryHints(value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    /***
     *
     * 락걸기
     * */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);

}
