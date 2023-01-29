package com.example.springdatajpa.repository;

import com.example.springdatajpa.entity.Member;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom{

    private final EntityManager entityManager;


    @Override
    public List<Member> findMemberCustom() {
        return entityManager.createQuery("select m from Member m")
                .getResultList();
    }
}
