package com.example.springdatajpa.repository;



import com.example.springdatajpa.entity.Member;

import java.util.List;

public interface MemberRepositoryCustom {

    /**
     * 이름을 맞춰줘야함. .커스텀명 + Impl 이어야 따로 db연결 가능
     *  ex ) MemberRepositoryCustom = > MemberRepositoryCustomImpl
     * */
    
    List<Member> findMemberCustom();
}
