package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
}