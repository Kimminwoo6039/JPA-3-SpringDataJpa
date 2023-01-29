package com.example.springdatajpa.controller;

import com.example.springdatajpa.entity.Member;
import com.example.springdatajpa.repository.MemberDto;
import com.example.springdatajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;


    /**
     * Pk 값이 들어가있으면 도메인 컨버터를 사용가능하다.
     * */

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    /**
     * Pk 값이 들어가있으면 도메인 컨버터를 사용가능하다.
     * ** 컨버터 방식 ***
     * */

    @GetMapping("/members2/{id}")
    public String findMember2(@PathVariable("id") Member member) {
        return member.getUsername();
    }

    /**
     * 페이지
     * ?page=1&size=3&sort=id,desc 이렇게 넣을수 있다.
     *  @PageableDefault(size = 5) 로 size , default max 등등 구하기가능
     *  pageable total 카운트 쿼리는 나간다.
     * */

    @GetMapping("/members")
    public Page<MemberDto> list( Pageable pageable) {
        Page<Member> page = memberRepository.findAll(pageable);
    //    Page<MemberDto> collect = page.map(o -> new MemberDto(o.getId(), o.getUsername(), null));
        Page<MemberDto> collect = page.map(o -> new MemberDto(o)); // 줄이면 Page<MemberDto> collect = page.map(MemberDto::new);
        return collect;
    }



    //@PostConstruct
    public void init() {
        for (int i = 0; i < 100; i++) {
            memberRepository.save(new Member("user" + i,i));
        }
    }
}
