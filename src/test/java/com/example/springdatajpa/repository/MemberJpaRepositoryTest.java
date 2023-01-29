package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.entity.Team;
import io.restassured.RestAssured;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MemberJpaRepositoryTest {

    @Autowired MemberJpaRepository memberJpaRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TeamRepository teamRepository;

    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Rollback(value = false)
    public void testMember() {


        //given
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);



        //when
        Member findMember = memberJpaRepository.find(savedMember.getId());

        //then
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    }

    @Test
    public void basicCRUD() {
        //given
        Member member1 = new Member("meber1");
        Member member2 = new Member("member2");
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);



        // ( 단건조회 )
        Member findMember1 = memberJpaRepository.findById(member1.getId()).get();
        Member findMember2 = memberJpaRepository.findById(member2.getId()).get();

        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);

        // 리스트 조회

        List<Member> findAll = memberJpaRepository.findAll();
        assertThat(findAll.size()).isEqualTo(2);

        // 카운트 검증
        long count = memberJpaRepository.count();
        assertThat(count).isEqualTo(2);

        // 삭제 검증
        memberJpaRepository.delete(member1);
        memberJpaRepository.delete(member2);

        long deletedcount = memberJpaRepository.count();
        assertThat(deletedcount).isEqualTo(0);


        System.out.println("memberJpaRepository = " + memberJpaRepository.getClass());
    }

    @Test
    public void find() {
        //given
        Member member1 = new Member("member1",10);
        Member member2 = new Member("member1",20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberRepository.findByUsernameAndAgeGreaterThan("member1", 15);

        assertThat(result.get(0).getUsername()).isEqualTo("member1");
        assertThat(result.get(0).getAge()).isEqualTo(20);
        assertThat(result.size()).isEqualTo(1);

    }

    @Test
    public void 헬로우() {
        List<Member> helloBy = memberRepository.findTop3HelloBy();

    }



    @Test
    public void 쿼리테스트() {
        //given
        Member member1 = new Member("member1",10);
        Member member2 = new Member("member1",20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<Member> result = memberRepository.findUser("member1", 10);
        assertThat(result.get(0)).isEqualTo(member1);

    }

    @Test
    public void Dto일반회원조회() {

        Member member1 = new Member("member1",10);
        Member member2 = new Member("member1",20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        List<String> usernameList = memberRepository.findUsernameList();
        for (String s : usernameList) {
            System.out.println("s = " + s);
        }


    }

    @Test
    public void Dto전체회원조회() {

        Team team = new Team("teamB");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDto = memberRepository.findMemberDto();
        for (MemberDto dto : memberDto) {
            System.out.println("dto = " + dto);
        }


    }
    
    /**
     * 
     * 여러 컬렉션을 조회
     * 
     * */

    @Test
    public void 컬렉션조회() {

        Team team = new Team("teamB");
        teamRepository.save(team);

        Member m1 = new Member("AAA",10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<Member> memberDto = memberRepository.findByNames(Arrays.asList("aaa","bbb"));
        for (Member member : memberDto) {
            System.out.println("member = " + member);
        }


    }

    @Test
    public void returnType() {

        Member member1 = new Member("member1",10);
        Member member2 = new Member("member1",20);
        memberJpaRepository.save(member1);
        memberJpaRepository.save(member2);

        /**
         * 리스트 널이 안되는걸 보장한다
         * List<Member> result = memberRepository.findListByUsername("sadada");
         * if ( result ==0 ) 사용하면 안됨
         * */

        Optional<Member> result = memberRepository.findOptionalByUsername("member1");
        System.out.println("result.size() = " + result);


    }


    @Test
    public void paging() {
        //given

        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
        memberJpaRepository.save(new Member("member4",10));
        memberJpaRepository.save(new Member("member5",10));

        int age = 10;
        int offset = 1; // 몇번째부터 시작
        int limit = 3; // 총보여줄 ㄱ수

        //when
        List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
        long totalCount = memberJpaRepository.totalCount(age);

        // 페이지 계산 공식 적용..
        // totalPage = totalCount / size..
        // 마지막페이지 ..
        // 최초 페이지 ..

        //then
        assertThat(members.size()).isEqualTo(3); // 0번부터 ~ 3개
        assertThat(totalCount).isEqualTo(5); //  5개
    }

    @Test
    public void pagingSpringDataJpa() {
        //given

        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",10));
        memberJpaRepository.save(new Member("member3",10));
        memberJpaRepository.save(new Member("member4",10));
        memberJpaRepository.save(new Member("member5",10));



        int age = 10;
        /**
         * 0 페이지에서 3개
         * */
        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));


        //when
        /**
         * totalcount 를 자동으로 만들어줌..
         * */
        Page<Member> page = memberRepository.findByAge(age,pageRequest);

        /**
         * Api 엔티티 내보내지말자 ! Dto 로 변환 꼭하기
         * */
        Page<MemberDto> toMap = page.map(member -> new MemberDto(member.getId(), member.getUsername(), null));


        //then
        /**
         * 내부에 있는 데이터 꺼낼때.
         * */
        List<Member> content = page.getContent(); // total 내용
        long totalElements = page.getTotalElements(); // total count


        assertThat(content.size()).isEqualTo(3); // 0~3개 조건저장햇으니깐
        assertThat(page.getTotalElements()).isEqualTo(5); // 총 5개 이니깐
        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 개수
        assertThat(page.isFirst()).isTrue(); // 첫번째 페이지
        assertThat(page.hasNext()).isTrue(); //다음 페이지 있냐
    }

    
    /**
     * 
     * 슬라이스
     * */
//    @Test
//    public void pagingSpringDataJpaSlice() {
//        //given
//
//        memberJpaRepository.save(new Member("member1",10));
//        memberJpaRepository.save(new Member("member2",10));
//        memberJpaRepository.save(new Member("member3",10));
//        memberJpaRepository.save(new Member("member4",10));
//        memberJpaRepository.save(new Member("member5",10));
//
//
//
//        int age = 10;
//        /**
//         * 0 페이지에서 3개
//         * */
//        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));
//
//
//        //when
//        /**
//         * totalcount 를 자동으로 만들어줌..
//         *
//         * limit 3개를 설정했지만 Slice 의 기능인 +1 로 4개가 적용됨
//         * */
//        Slice<Member> page = memberRepository.findByAge(age,pageRequest);
//
//
//        //then
//        /**
//         * 내부에 있는 데이터 꺼낼때.
//         * */
//        List<Member> content = page.getContent(); // total 내용
//
//
//        assertThat(content.size()).isEqualTo(3); // 0~3개 조건저장햇으니깐
////        assertThat(page.getTotalElements()).isEqualTo(5); // 총 5개 이니깐
//        assertThat(page.getNumber()).isEqualTo(0); // 페이지 번호
////        assertThat(page.getTotalPages()).isEqualTo(2); // 전체 페이지 개수
//        assertThat(page.isFirst()).isTrue(); // 첫번째 페이지
//        assertThat(page.hasNext()).isTrue(); //다음 페이지 있냐
//    }


    @Test
    public void bulkUpdate() {
        memberJpaRepository.save(new Member("member1",10));
        memberJpaRepository.save(new Member("member2",19));
        memberJpaRepository.save(new Member("member3",20));
        memberJpaRepository.save(new Member("member4",21));
        memberJpaRepository.save(new Member("member5",40));

        int i = memberJpaRepository.bulkAgePlus(20);

        assertThat(i).isEqualTo(3);

    }

    @Test
    public void bulkUpdateSpringDataJpa() {
        memberRepository.save(new Member("member1",10));
        memberRepository.save(new Member("member2",19));
        memberRepository.save(new Member("member3",20));
        memberRepository.save(new Member("member4",21));
        memberRepository.save(new Member("member5",40));

        int i = memberRepository.bulkAgePlus(20);
        
        /**
         * 영속성에 40이라는게 나와있어서.. 데잍들 보내야함
         * */
//        entityManager.clear(); // 영속성 데이터 날림

        List<Member> result = memberRepository.findListByUsername("member5");
        Member member5 = result.get(0);
        System.out.println("member5 = " + member5.getAge());


        assertThat(i).isEqualTo(3);


    }
    
    
    @Test
    public void findMemberLazy() {
        //given
        // member1 > teamA
        // member2 > teamb
        
        Team teamA = new Team("teamA");
        Team teamB = new Team("teamB");
        teamRepository.save(teamA);
        teamRepository.save(teamB);
        Member member1 = new Member("member1",10,teamA);
        Member member2 = new Member("member2",10,teamB);
        memberRepository.save(member1);
        memberRepository.save(member2);

        entityManager.flush();
        entityManager.clear();
        
        //when
        List<Member> members = memberRepository.findByUsername(member1.getUsername());

        for (Member member : members) {
            System.out.println("member = " + member.getUsername());
            System.out.println("member.getTeam() = " + member.getTeam().getName());
//            System.out.println("member.getUsername() = " + member.getTeam().getName());
        }

    }
    
    @Test
    public void queryHint() {
        
        //given
        Member member = memberRepository.save(new Member("member", 10));
        entityManager.flush();
        entityManager.clear();
        
        //when
        Member findMember = memberRepository.findReadOnlyByUsername(member.getUsername());
        findMember.setUsername("member2"); // 변경감지가 동작함 그래서 업데이트 쿼리가 동작함 QueryHints 를 쓰면 변경감지 안됨

        entityManager.flush(); // 강제로 영속성컨텍스트에서 >> 디비로 값 저장 시킴

    }

    @Test
    public void lock() {
        Member member = new Member("member1",10);
        memberRepository.save(member);
        entityManager.flush();
        entityManager.clear();

        List<Member> findMember = memberRepository.findLockByUsername("member1");
//        findMember.setUsername("member2");  // 잠겨서... 안된당./.

    }


    @Test
    public void callCustom() {
        List<Member> result = memberRepository.findMemberCustom();

    }
    
    @Test
    public void 등록수정일() throws Exception {
        
        Member member = new Member("member1");
        memberRepository.save(member); // @PrePersist
        
        Thread.sleep(100);
        member.setUsername("member2");

        entityManager.flush();
        entityManager.clear();


        Member findMember = memberRepository.findById(member.getId()).get();


        System.out.println("findMember.getCreateDate() = " + findMember.getCreateDate());
        System.out.println("findMember.getUpdateDate() = " + findMember.getLastModifiedDate());
        System.out.println("findMember.getUpdateDate() = " + findMember.getCreatedBy());
        System.out.println("findMember.getUpdateDate() = " + findMember.getLastModifiedBy());
    }
}